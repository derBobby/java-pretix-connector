package eu.planlos.javapretixconnector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Optional;

@ConfigurationProperties(prefix = "pretix.feature")
public record PretixFeatureConfig(Boolean preloadEventDataEnabled, Boolean preloadOrdersEnabled) {

    @ConstructorBinding
    public PretixFeatureConfig(Boolean preloadEventDataEnabled, Boolean preloadOrdersEnabled) {
        this.preloadEventDataEnabled = Optional.ofNullable(preloadEventDataEnabled).orElse(false);
        this.preloadOrdersEnabled = Optional.ofNullable(preloadOrdersEnabled).orElse(false);
    }
}