package eu.planlos.javapretixconnector.model.dto.single;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record ItemVariationDTO(@Id @NotNull Long id, @NotNull @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY) Map<String, String> value) {

    public String getName() {
        return value.get("de-informal");
    }
}