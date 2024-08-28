package eu.planlos.javapretixconnector.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor
public final class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String organizer;

    @NotNull
    private String event;

    @NotNull
    @Column(unique = true)
    private String code;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    @Email
    private String email;

    @NotNull
    private ZonedDateTime expires;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Item> itemList;

    public Booking(@NotNull String code, @NotNull String organizer, @NotNull String event, @NotNull String firstname, @NotNull String lastname, @NotNull String email, @NotNull ZonedDateTime expires, @NotNull List<Item> itemList) {
        this.code = code;
        this.organizer = organizer;
        this.event = event;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.expires = expires;
        this.itemList = itemList;
    }
}