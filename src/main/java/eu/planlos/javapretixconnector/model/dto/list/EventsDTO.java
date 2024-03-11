package eu.planlos.javapretixconnector.model.dto.list;

import eu.planlos.javapretixconnector.model.dto.single.EventDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EventsDTO(
        @NotNull Integer count,
        String next,
        String previous,
        @NotNull List<EventDTO> results
) {}