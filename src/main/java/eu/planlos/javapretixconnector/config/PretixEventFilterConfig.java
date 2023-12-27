package eu.planlos.javapretixconnector.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ConfigurationProperties(prefix = "pretix.event-filter")
public class PretixEventFilterConfig {

    private final PretixEventFilterSource source;

    @Getter
    private final List<String> filterList;

    public PretixEventFilterConfig(
            String source,
            List<String> filterList) {
        this.source = Optional.ofNullable(source)
                .map(PretixEventFilterSource::fromString)
                .orElse(PretixEventFilterSource.PROPERTIES);

        this.filterList = Optional.ofNullable(filterList).orElse(Collections.emptyList());

        log.info("Creating pretix event filter config:");
        log.info("- filter source: {} -> {}", source, this.source);
        log.info("- filter count: {}", this.filterList.size());
        this.filterList.forEach(filter -> log.info("- {}", filter));
    }

    public boolean isPropertiesSourceConfigured() {
        return source.equals(PretixEventFilterSource.PROPERTIES);
    }

    public boolean isUserSourceConfigured() {
        return source.equals(PretixEventFilterSource.USER);
    }

    @Getter
    private enum PretixEventFilterSource {
        PROPERTIES("properties"),
        USER("user");

        private final String value;

        PretixEventFilterSource(String value) {
            this.value = value;
        }

        public static PretixEventFilterSource fromString(String text) {
            for (PretixEventFilterSource source : PretixEventFilterSource.values()) {
                if (source.value.equalsIgnoreCase(text)) {
                    return source;
                }
            }
            throw new IllegalArgumentException("pretix.event-filter.source must be set to one of the values: " + validSources());
        }

        private static String validSources() {
            return Arrays.stream(PretixEventFilterSource.values())
                    .map(PretixEventFilterSource::getValue)
                    .collect(Collectors.joining(", "));        }
    }
}