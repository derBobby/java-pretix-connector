package eu.planlos.javapretixconnector.service.api;

import eu.planlos.javapretixconnector.model.PretixException;
import eu.planlos.javapretixconnector.config.PretixApiConfig;
import eu.planlos.javapretixconnector.model.dto.list.OrdersDTO;
import eu.planlos.javapretixconnector.model.dto.single.OrderDTO;
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
public class PretixApiOrderService extends PretixApiService {

    //TODO Conditional Fetching - https://docs.pretix.eu/en/latest/api/fundamentals.html#conditional-fetching

    private static final String FETCH_MESSAGE = "Fetched order from Pretix: {}";

    public PretixApiOrderService(PretixApiConfig config, @Qualifier("PretixWebClient") WebClient webClient) {
        super(config, webClient);
    }

    /*
     * Query
     */
    public List<OrderDTO> fetchAllOrders(String event) {

        if(isAPIDisabled()) {
            return Collections.emptyList();
        }

        OrdersDTO dto = webClient
                .get()
                .uri(orderListUri(event))
                .retrieve()
                .bodyToMono(OrdersDTO.class)
                .retryWhen(Retry
                        .fixedDelay(config.retryCount(), Duration.ofSeconds(config.retryInterval()))
                        .filter(WebClientRetryFilter::shouldRetry)
                )
                .doOnError(error -> log.error("Message fetching all orders from Pretix API: {}", error.getMessage()))
                .block();

        if (dto != null) {
            List<OrderDTO> orderDTOList = new ArrayList<>(dto.results());
            orderDTOList.forEach(order -> log.info(FETCH_MESSAGE, order));
            return orderDTOList;
        }

        throw new PretixException(PretixException.IS_NULL);
    }

    public OrderDTO fetchOrderFromPretix(String event, String code) {

        if(isAPIDisabled()) {
            return null;
        }

        OrderDTO orderDto = webClient
                .get()
                .uri(specificOrderUri(event, code))
                .retrieve()
                .bodyToMono(OrderDTO.class)
                .retryWhen(Retry
                        .fixedDelay(config.retryCount(), Duration.ofSeconds(config.retryInterval()))
                        .filter(WebClientRetryFilter::shouldRetry)
                )
                .doOnError(error -> log.error("Message fetching order={} from Pretix API: {}", code, error.getMessage()))
                .block();
        if (orderDto != null) {
            log.info(FETCH_MESSAGE, orderDto);
            return orderDto;
        }

        throw new PretixException(PretixException.IS_NULL);
    }

    /*
     * Uri generators
     */
    private String specificOrderUri(String event, String orderCode) {
        return String.join(
                "",
                "api/v1/organizers/", config.organizer(),
                "/events/", event,
                "/orders/", orderCode, "/");
    }

    private String orderListUri(String event) {
        return String.join(
                "",
                "api/v1/organizers/", config.organizer(),
                "/events/", event,
                "/orders/");
    }

    public String getEventUrl(String event, String orderCode) {

        String address = config.address();
        String organizer = config.organizer();

        // Ensure address ends with a single slash
        if (address.endsWith("/")) {
            address = address.substring(0, address.length() - 1);
        }

        // Use String.format for better readability
        return String.format("%s/control/event/%s/%s/orders/%s/",
                address, organizer, event, orderCode);
    }
}