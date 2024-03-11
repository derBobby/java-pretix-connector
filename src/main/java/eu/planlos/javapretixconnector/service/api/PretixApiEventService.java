package eu.planlos.javapretixconnector.service.api;

import eu.planlos.javapretixconnector.config.PretixApiConfig;
import eu.planlos.javapretixconnector.model.PretixException;
import eu.planlos.javapretixconnector.model.dto.list.EventsDTO;
import eu.planlos.javapretixconnector.model.dto.single.EventDTO;
import eu.planlos.javaspringwebutilities.web.WebClientRetryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PretixApiEventService extends PretixApiService {

    private static final String FETCH_MESSAGE = "Fetched event from Pretix: {}";

    public PretixApiEventService(PretixApiConfig config, @Qualifier("PretixWebClient") WebClient webClient) {
        super(config, webClient);
    }

    /*
     * Query
     */
    public List<EventDTO> fetchAllEvents() {

        if(isAPIDisabled()) {
            return Collections.emptyList();
        }

        EventsDTO dto = webClient
                .get()
                .uri(eventListUri())
                .retrieve()
                .bodyToMono(EventsDTO.class)
                .retryWhen(Retry
                        .fixedDelay(config.retryCount(), Duration.ofSeconds(config.retryInterval()))
                        .filter(WebClientRetryFilter::shouldRetry)
                )
                .doOnError(error -> log.error("Message fetching all events from Pretix API: {}", error.getMessage()))
                .block();

        if (dto != null) {
            List<EventDTO> eventsDTOList = new ArrayList<>(dto.results());
            eventsDTOList.forEach(eventDTO -> log.info(FETCH_MESSAGE, eventDTO));
            return eventsDTOList;
        }

        throw new PretixException(PretixException.IS_NULL);
    }

    /*
     * Uri generators
     */
    private String eventListUri() {
        return String.join(
                "",
                "api/v1/organizers/", config.organizer(),
                "/events/");
    }
}