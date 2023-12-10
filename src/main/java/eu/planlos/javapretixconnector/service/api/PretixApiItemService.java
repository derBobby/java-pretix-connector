package eu.planlos.javapretixconnector.service.api;

import eu.planlos.javapretixconnector.common.PretixException;
import eu.planlos.javapretixconnector.config.PretixApiConfig;
import eu.planlos.javapretixconnector.model.PretixId;
import eu.planlos.javapretixconnector.model.dto.list.ItemsDTO;
import eu.planlos.javapretixconnector.model.dto.single.ItemDTO;
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
public class PretixApiItemService extends PretixApiService {

    private static final String FETCH_MESSAGE = "Fetched item from Pretix: {}";

    public PretixApiItemService(PretixApiConfig config, @Qualifier("PretixWebClient") WebClient webClient) {
        super(config, webClient);
    }

    /*
     * Query
     */
    public List<ItemDTO> queryAllItems(String event) {

        if(isAPIDisabled()) {
            return Collections.emptyList();
        }

        ItemsDTO dto = webClient
                .get()
                .uri(itemListUri(event))
                .retrieve()
                .bodyToMono(ItemsDTO.class)
                .retryWhen(Retry.fixedDelay(config.retryCount(), Duration.ofSeconds(config.retryInterval())))
                .doOnError(error -> log.error("Message fetching all items from Pretix API: {}", error.getMessage()))
                .block();

        if (dto != null) {
            List<ItemDTO> itemsDTOList = new ArrayList<>(dto.results());
            itemsDTOList.forEach(item -> log.info(FETCH_MESSAGE, item));
            return itemsDTOList;
        }

        throw new PretixException(PretixException.IS_NULL);
    }

    public ItemDTO queryItem(String event, PretixId itemId) {

        if(isAPIDisabled()) {
            return null;
        }

        ItemDTO itemDTO = webClient
                .get()
                .uri(specificItemUri(event, itemId.getIdValue()))
                .retrieve()
                .bodyToMono(ItemDTO.class)
                .retryWhen(Retry.fixedDelay(config.retryCount(), Duration.ofSeconds(config.retryInterval())))
                .doOnError(error -> log.error("Message fetching item={} from Pretix API: {}", itemId, error.getMessage()))
                .block();
        if (itemDTO != null) {
            log.info(FETCH_MESSAGE, itemDTO);
            return itemDTO;
        }

        throw new PretixException(PretixException.IS_NULL);
    }

    /*
     * Uri generators
     */
    private String specificItemUri(String event, Long itemId) {
        return String.join("", itemListUri(event), Long.toString(itemId), "/");
    }

    private String itemListUri(String event) {
        return String.join(
                "",
                "api/v1/organizers/", config.organizer(),
                "/events/", event,
                "/items/");
    }
}