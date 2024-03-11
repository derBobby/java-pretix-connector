package eu.planlos.javapretixconnector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Optional;

@ConfigurationProperties(prefix = "pretix.api")
public record PretixApiConfig(Boolean active, String address, String token, String organizer, Integer retryCount, Integer retryInterval) {

    @ConstructorBinding
    public PretixApiConfig(Boolean active, String address, String token, String organizer, Integer retryCount, Integer retryInterval) {
        this.active = Optional.ofNullable(active).orElse(false);
        this.address = Optional.ofNullable(address).orElse("not configured");
        this.token = Optional.ofNullable(token).orElse("not configured");
        this.organizer = Optional.ofNullable(organizer).orElse("not configured");
        this.retryCount = Optional.ofNullable(retryCount).orElse(1);
        this.retryInterval = Optional.ofNullable(retryInterval).orElse(1);
    }

    public boolean inactive() {
        return !active;
    }
}