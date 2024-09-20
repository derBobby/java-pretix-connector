package eu.planlos.javapretixconnector.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public final class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Embedded
    private PretixId pretixId;

    @NotNull
    @Lob
    @Column(columnDefinition="LONGTEXT")
    private String text;

    public Question(@NotNull PretixId pretixId, @NotNull String text) {
        this.pretixId = pretixId;
        this.text = text;
    }
}
