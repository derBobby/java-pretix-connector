package eu.planlos.javapretixconnector.repository;

import eu.planlos.javapretixconnector.model.PretixEventFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PretixEventFilterRepository extends JpaRepository<PretixEventFilter, Long> {
    List<PretixEventFilter> findByActionAndOrganizerAndEvent(String action, String organizer, String event);
}
