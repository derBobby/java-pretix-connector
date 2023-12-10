package eu.planlos.javapretixconnector.service.api;

import eu.planlos.javapretixconnector.common.PretixException;
import eu.planlos.javapretixconnector.config.PretixApiConfig;
import eu.planlos.javapretixconnector.model.PretixId;
import eu.planlos.javapretixconnector.model.dto.list.ItemCategoriesDTO;
import eu.planlos.javapretixconnector.model.dto.single.ItemCategoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static eu.planlos.javapretixconnector.common.PretixException.*;

@Slf4j
@Service
public class PretixApiItemCategoryService extends PretixApiService {

    private static final String FETCH_MESSAGE = "Fetched item category from Pretix: {}";

    public PretixApiItemCategoryService(PretixApiConfig config, @Qualifier("PretixWebClient") WebClient webClient) {
        super(config, webClient);
    }

    /*
     * Query
     */
    public List<ItemCategoryDTO> queryAllItemCategories(String event) {

        if(isAPIDisabled()) {
            return Collections.emptyList();
        }

        ItemCategoriesDTO dto = webClient
                .get()
                .uri(itemCategoryListUri(event))
                .retrieve()
                .bodyToMono(ItemCategoriesDTO.class)
                .retryWhen(Retry.fixedDelay(config.retryCount(), Duration.ofSeconds(config.retryInterval())))
                .doOnError(error -> log.error("Message fetching all item categories from Pretix API: {}", error.getMessage()))
                .block();

        if (dto != null) {
            List<ItemCategoryDTO> itemCategoryDTOList = new ArrayList<>(dto.results());
            itemCategoryDTOList.forEach(itemCategoryDTO -> log.info(FETCH_MESSAGE, itemCategoryDTO));
            return itemCategoryDTOList;
        }

        throw new PretixException(IS_NULL);
    }

    public ItemCategoryDTO queryItemCategory(String event, PretixId itemCategoryId) {

        if(isAPIDisabled()) {
            return null;
        }

        ItemCategoryDTO itemCategoryDTO = webClient
                .get()
                .uri(specificItemCategoryUri(event, itemCategoryId.getIdValue()))
                .retrieve()
                .bodyToMono(ItemCategoryDTO.class)
                .retryWhen(Retry.fixedDelay(config.retryCount(), Duration.ofSeconds(config.retryInterval())))
                .doOnError(error -> log.error("Message fetching item category={} from Pretix API: {}", itemCategoryId, error.getMessage()))
                .block();
        if (itemCategoryDTO != null) {
            log.info(FETCH_MESSAGE, itemCategoryDTO);
            return itemCategoryDTO;
        }

        throw new PretixException(IS_NULL);
    }

    /*
     * Uri generators
     */
    private String specificItemCategoryUri(String event, Long itemCategoryId) {
        return String.join("", itemCategoryListUri(event), Long.toString(itemCategoryId), "/");
    }

    private String itemCategoryListUri(String event) {
        return String.join(
                "",
                "api/v1/organizers/", config.organizer(),
                "/events/", event,
                "/categories/");
    }
}