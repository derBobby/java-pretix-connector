package eu.planlos.javapretixconnector.model.dto.list;

import eu.planlos.javapretixconnector.model.dto.single.ItemDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ItemsDTO(
        @NotNull Integer count,
        String next,
        String previous,
        @NotNull List<ItemDTO> results
) {}