package eu.planlos.javapretixconnector.model.dto.single;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public final class OrderDTO {

    @Id @NotNull
    private String code;

    @NotNull
    private InvoiceAddressDTO invoice_address;

    @NotNull @Email
    private String email;

    @NotNull
    private ZonedDateTime expires;

    @NotNull
    private List<PositionDTO> positions;

    public OrderDTO(@NotNull String code, @NotNull InvoiceAddressDTO invoice_address, @NotNull String email, @NotNull ZonedDateTime expires, @NotNull List<PositionDTO> positions) {
        this.code = code;
        this.invoice_address = invoice_address;
        this.email = email;
        this.expires = expires;
        this.positions = positions;
    }

    public String getFirstName() {
        return invoice_address.name_parts().given_name();
    }

    public String getLastName() {
        return invoice_address.name_parts().family_name();
    }

    @Override
    public String toString() {
        return String.format("code=%s, firstname=%s, lastname=%s, email=%s", code, getFirstName(), getLastName(), email);
    }
}