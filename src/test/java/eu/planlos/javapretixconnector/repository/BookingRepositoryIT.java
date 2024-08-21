package eu.planlos.javapretixconnector.repository;

import eu.planlos.javapretixconnector.TestContextConfiguration;
import eu.planlos.javapretixconnector.model.Booking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ContextConfiguration(classes = TestContextConfiguration.class)
class BookingRepositoryIT {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void injectedComponentsAreNotNull() {
        assertThat(bookingRepository).isNotNull();
    }

    @Test
    public void insertDuplicate_throwsException() {
        Booking firstBooking = new Booking("only organizer", "first event", "XC0DE", "Firstname", "Lastname", "email@example.com", ZonedDateTime.now(), new ArrayList<>());
        Booking secondBooking = new Booking("only organizer", "second event", "XC0DE", "Firstname", "Lastname", "email@example.com", ZonedDateTime.now(), new ArrayList<>());

        bookingRepository.saveAndFlush(firstBooking);
        assertThrows(DataIntegrityViolationException.class, () -> bookingRepository.saveAndFlush(secondBooking));
    }
}
