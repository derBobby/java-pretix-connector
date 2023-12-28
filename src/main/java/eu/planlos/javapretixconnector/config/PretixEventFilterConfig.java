package eu.planlos.javapretixconnector.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static eu.planlos.javapretixconnector.config.PretixEventFilterConfig.PretixEventFilterSource.PROPERTIES;

@Slf4j
@Configuration
public class PretixEventFilterConfig {

    private final PretixEventFilterSource source;

    @Getter
    private final List<String> filterList;

    public PretixEventFilterConfig(
            @Value("${pretix.event-filter.source:#{null}}") String source,
            @Value("${pretix.event-filter.filter-list:#{null}}") String filterList) {

        this.source = Optional.ofNullable(source)
                .filter(s -> !s.isEmpty())  // Filter out empty strings
                .map(PretixEventFilterSource::fromString)
                .orElse(PROPERTIES);
        this.filterList = Optional.ofNullable(filterList)
                .map(filterString -> Arrays.asList(filterString.split("\\|\\|")))
                .orElse(Collections.emptyList())
                .stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        log.info("Creating pretix event filter config:");
        log.info("- filter source: {} -> {}", source, this.source);
        log.info("- filter count: {}", this.filterList.size());
        this.filterList.forEach(filter -> log.info("- {}", filter));
    }

    public boolean isPropertiesSourceConfigured() {
        return source.equals(PROPERTIES);
    }

    public boolean isUserSourceConfigured() {
        return source.equals(PretixEventFilterSource.USER);
    }

    @Getter
    public enum PretixEventFilterSource {
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