package eu.planlos.javapretixconnector.model.dto.single;

import org.springframework.lang.NonNull;

public record AnswerDTO(
        @NonNull Long question,
        @NonNull String answer) {
}