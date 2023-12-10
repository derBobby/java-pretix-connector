package eu.planlos.javapretixconnector.model.dto;

import eu.planlos.javapretixconnector.model.validation.ValidAction;
import eu.planlos.javapretixconnector.model.validation.ValidCode;
import eu.planlos.javapretixconnector.model.validation.ValidEvent;
import eu.planlos.javapretixconnector.model.validation.ValidOrganizer;
import jakarta.validation.constraints.NotNull;

public record WebHookDTO(
        @NotNull Long notification_id,
        @NotNull @ValidOrganizer String organizer,
        @NotNull @ValidEvent String event,
        @NotNull @ValidCode String code,
        @NotNull @ValidAction String action) {
}


