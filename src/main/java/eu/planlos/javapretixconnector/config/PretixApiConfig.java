package eu.planlos.javapretixconnector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

//TODO organizer not yet used from filter, authentication?
@ConfigurationProperties(prefix = "pretix.api")
public record PretixApiConfig(boolean active, String address, String token, String organizer, List<String> eventList, int retryCount, int retryInterval) {
    public boolean inactive() {
        return !active;
    }
}