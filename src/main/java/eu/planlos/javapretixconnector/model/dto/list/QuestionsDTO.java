package eu.planlos.javapretixconnector.model.dto.list;

import eu.planlos.javapretixconnector.model.dto.single.QuestionDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionsDTO(
        @NotNull Integer count,
        String next,
        String previous,
        @NotNull List<QuestionDTO> results
) {}