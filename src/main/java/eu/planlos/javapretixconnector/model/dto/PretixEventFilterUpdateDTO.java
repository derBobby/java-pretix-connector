package eu.planlos.javapretixconnector.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.planlos.javapretixconnector.model.validation.ValidAction;
import eu.planlos.javapretixconnector.model.validation.ValidEvent;
import eu.planlos.javapretixconnector.model.validation.ValidFilterMap;
import eu.planlos.javapretixconnector.model.validation.ValidOrganizer;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record PretixEventFilterUpdateDTO(
        @JsonProperty("action") @NotNull @ValidAction String action,
        @JsonProperty("organizer") @NotNull @ValidOrganizer String organizer,
        @JsonProperty("event") @NotNull @ValidEvent String event,
        @JsonProperty("qna-list") @NotNull @ValidFilterMap Map<String, List<String>> filterMap) {
}