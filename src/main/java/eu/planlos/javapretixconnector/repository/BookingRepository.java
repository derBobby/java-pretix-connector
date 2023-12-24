package eu.planlos.javapretixconnector.repository;

import eu.planlos.javapretixconnector.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByOrganizerAndEventAndCode(String organizer, String event, String code);
}
