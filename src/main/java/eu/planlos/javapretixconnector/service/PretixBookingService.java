package eu.planlos.javapretixconnector.service;

import eu.planlos.javapretixconnector.model.*;
import eu.planlos.javapretixconnector.model.Position;
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
public class PretixBookingService {

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

    public Booking loadOrFetch(String organizer, String event, String code) {

        // Get from DB
        Optional<Booking> optionalBooking = bookingRepository.findByEventAndCode(event, code);
        if(optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            log.info("Loaded booking from db: {}", booking);
            return booking;
        }

        // or fetch from Pretix
        return fetchFromPretix(organizer, event, code);
    }

    private Booking fetchFromPretix(String organizer, String event, String code) {
        OrderDTO orderDTO = pretixApiOrderService.fetchOrderFromPretix(event, code);
        Booking booking = convert(organizer, event, orderDTO);
        return bookingRepository.save(booking);
    }

    public void fetchAll(String organizer, String event) {
        List<OrderDTO> orderDTOList = pretixApiOrderService.fetchAllOrders(event);
        List<Booking> bookingList = orderDTOList.stream().map(orderDTO -> convert(organizer, event, orderDTO)).collect(Collectors.toList());
        bookingRepository.saveAll(bookingList);
    }

    public String getOrderUrl(String event, String orderCode) {
        return pretixApiOrderService.getEventUrl(event, orderCode);
    }

    private Booking convert(String organizer, String event, OrderDTO orderDTO) {

        List<Position> positionList = new ArrayList<>();

        orderDTO.getPositions().forEach(positionDTO -> {
            Product product = productService.loadOrFetchProduct(event, new PretixId(positionDTO.item()), new PretixId(positionDTO.variation()));
            Map <Question, Answer> QnAmap = questionService.generateQuestionAnswerMap(event, positionDTO.answers().stream().map(questionService::convert).toList());

            positionList.add(new Position(product, QnAmap));
        });

        return new Booking(event, organizer, orderDTO.getCode(), orderDTO.getFirstName(), orderDTO.getLastName(), orderDTO.getEmail(), orderDTO.getExpires(), positionList);
    }
}
