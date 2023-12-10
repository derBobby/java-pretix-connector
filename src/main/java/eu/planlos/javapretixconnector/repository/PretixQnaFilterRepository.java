package eu.planlos.javapretixconnector.repository;

import eu.planlos.javapretixconnector.model.PretixQnaFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PretixQnaFilterRepository extends JpaRepository<PretixQnaFilter, Long> {
    List<PretixQnaFilter> findByActionAndEvent(String action, String event);
}
