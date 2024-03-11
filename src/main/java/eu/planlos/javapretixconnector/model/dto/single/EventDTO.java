package eu.planlos.javapretixconnector.model.dto.single;

import jakarta.validation.constraints.NotNull;

public record EventDTO(
        @NotNull String slug,
        @NotNull Boolean live,
        @NotNull Boolean testmode) {}