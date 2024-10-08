package eu.planlos.javapretixconnector.service;

import eu.planlos.javapretixconnector.TestContextConfiguration;
import eu.planlos.javapretixconnector.model.Booking;
import eu.planlos.javapretixconnector.model.PretixId;
import eu.planlos.javapretixconnector.model.Product;
import eu.planlos.javapretixconnector.model.ProductType;
import eu.planlos.javapretixconnector.model.dto.single.InvoiceAddressDTO;
import eu.planlos.javapretixconnector.model.dto.single.NamePartsDTO;
import eu.planlos.javapretixconnector.model.dto.single.OrderDTO;
import eu.planlos.javapretixconnector.model.dto.single.PositionDTO;
import eu.planlos.javapretixconnector.repository.BookingRepository;
import eu.planlos.javapretixconnector.repository.ProductRepository;
import eu.planlos.javapretixconnector.repository.ProductTypeRepository;
import eu.planlos.javapretixconnector.service.api.PretixApiOrderService;
import eu.planlos.javautilities.ZonedDateTimeUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static eu.planlos.javapretixconnector.PretixTestDataUtility.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@ContextConfiguration(classes = TestContextConfiguration.class)
@ExtendWith(MockitoExtension.class)
class PretixBookingServiceIT {

    @Mock
    PretixApiOrderService pretixApiOrderService;

    @Mock
    ProductService productService;

    @Mock
    QuestionService questionService;

    @Autowired
    ProductTypeRepository productTypeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    @SpyBean
    BookingRepository bookingRepository;

    PretixBookingService pretixBookingService;

    @BeforeEach
    void setup() {
        pretixBookingService = new PretixBookingService(pretixApiOrderService, productService, questionService, bookingRepository);
    }

    @Test
    public void duplicatedWebHook_fetchedOnlyOnce() {

        OrderDTO orderDTO = new OrderDTO(CODE_NEW, new InvoiceAddressDTO("name", new NamePartsDTO("first", "last")), "mail@example.com", ZonedDateTimeUtility.nowCET(), List.of(new PositionDTO("Name", 1L, 1L, Collections.emptyList())));
        when(pretixApiOrderService.fetchOrderFromPretix(any(), any())).thenReturn(orderDTO);
        when(productService.loadOrFetchProduct(any(), any(), any())).then(x -> {
            ProductType productType = productTypeRepository.save(new ProductType(new PretixId(1L), false, "Type"));
            return productRepository.save(new Product(new PretixId(1L), "Ticket", productType));
        });

        pretixBookingService.loadOrFetch(ORGANIZER, EVENT, CODE_NEW);
        pretixBookingService.loadOrFetch(ORGANIZER, EVENT, CODE_NEW);

        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    public void saveDuplicate_fails() {
        Booking first = new Booking(CODE_NEW, ORGANIZER, EVENT, "firstname", "lastname", "email@example.com", ZonedDateTime.now(), Collections.emptyList());
        Booking second = new Booking(CODE_NEW, ORGANIZER, EVENT, "firstname", "lastname", "email@example.com", ZonedDateTime.now(), Collections.emptyList());

        bookingRepository.saveAndFlush(first);
        assertThrows(DataIntegrityViolationException.class, () -> bookingRepository.saveAndFlush(second));
    }

}