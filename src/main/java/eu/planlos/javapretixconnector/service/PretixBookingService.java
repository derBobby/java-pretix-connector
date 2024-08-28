package eu.planlos.javapretixconnector.service;

import eu.planlos.javapretixconnector.model.*;
import eu.planlos.javapretixconnector.model.dto.single.OrderDTO;
import eu.planlos.javapretixconnector.repository.BookingRepository;
import eu.planlos.javapretixconnector.service.api.PretixApiOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PretixBookingService implements IPretixBookingService {

    private final PretixApiOrderService pretixApiOrderService;
    private final ProductService productService;
    private final QuestionService questionService;

    private final BookingRepository bookingRepository;

    public PretixBookingService(PretixApiOrderService pretixApiOrderService, ProductService productService, QuestionService questionService, BookingRepository bookingRepository) {
        this.pretixApiOrderService = pretixApiOrderService;
        this.productService = productService;
        this.questionService = questionService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking loadOrFetch(String organizer, String eventSlug, String code) {

        // Get from DB
        Optional<Booking> optionalBooking = bookingRepository.findByOrganizerAndEventAndCode(organizer, eventSlug, code);
        if(optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            log.info("Loaded booking from db: {}", booking);
            return booking;
        }

        // or fetch from Pretix
        return fetchFromPretix(organizer, eventSlug, code);
    }

    public List<Booking> loadAllLocal() {
        return bookingRepository.findAll();
    }

    private Booking fetchFromPretix(String organizer, String eventSlug, String code) {
        OrderDTO orderDTO = pretixApiOrderService.fetchOrderFromPretix(eventSlug, code);
        Booking booking = convert(organizer, eventSlug, orderDTO);
        return bookingRepository.save(booking);
    }

    @Override
    public void fetchAll(String organizer, String eventSlug) {
        List<OrderDTO> orderDTOList = pretixApiOrderService.fetchAllOrders(eventSlug);
        List<Booking> bookingList = orderDTOList.stream().map(orderDTO -> convert(organizer, eventSlug, orderDTO)).collect(Collectors.toList());
        bookingRepository.saveAll(bookingList);
    }

    @Override
    public String getOrderUrl(String event, String orderCode) {
        return pretixApiOrderService.getEventUrl(event, orderCode);
    }

    @Override
    public void deleteAll() {
        log.info("Deleting booking data");
        bookingRepository.deleteAll();
    }

    private Booking convert(String organizer, String event, OrderDTO orderDTO) {

        List<Item> itemList = new ArrayList<>();

        orderDTO.getPositions().forEach(positionDTO -> {
            Product product = productService.loadOrFetchProduct(event, new PretixId(positionDTO.item()), new PretixId(positionDTO.variation()));
            Map <Question, Answer> QnAmap = questionService.generateQuestionAnswerMap(event, positionDTO.answers().stream().map(questionService::convert).toList());

            itemList.add(new Item(product, QnAmap));
        });

        return new Booking(orderDTO.getCode(), organizer, event, orderDTO.getFirstName(), orderDTO.getLastName(), orderDTO.getEmail(), orderDTO.getExpires(), itemList);
    }
}
