package eu.planlos.javapretixconnector.repository;

import eu.planlos.javapretixconnector.common.audit.AuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface WebHookAuditRepository extends JpaRepository<AuditEntry, Long> {
    List<AuditEntry> findByDateGreaterThan(ZonedDateTime zdt);
}