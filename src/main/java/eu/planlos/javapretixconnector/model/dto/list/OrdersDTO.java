package eu.planlos.javapretixconnector.model.dto.list;


import eu.planlos.javapretixconnector.model.dto.single.OrderDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrdersDTO(
        @NotNull Integer count,
        String next,
        String previous,
        @NotNull List<OrderDTO> results
) {}