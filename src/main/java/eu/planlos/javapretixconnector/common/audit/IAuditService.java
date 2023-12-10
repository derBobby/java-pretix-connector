package eu.planlos.javapretixconnector.common.audit;

import java.time.ZonedDateTime;
import java.util.List;

public interface IAuditService {
    List<AuditEntry> getAfter(ZonedDateTime zdt);
    List<AuditEntry> getAll();
}
