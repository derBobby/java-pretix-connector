package eu.planlos.javapretixconnector.model.dto.single;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record ItemCategoryDTO(
        @Id @NotNull Long id,
        @NotNull @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY) Map<String, String> name,
        @NotNull Boolean is_addon
) {
    public String getName() {
        return name.get("de-informal");
    }
}