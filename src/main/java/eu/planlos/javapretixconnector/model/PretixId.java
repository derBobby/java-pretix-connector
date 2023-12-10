package eu.planlos.javapretixconnector.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(force = true)
@Getter
@ToString
@EqualsAndHashCode
public final class PretixId {
    private final Long idValue;

    public PretixId(@NotNull Long idValue) {
        this.idValue = idValue;
    }
}

