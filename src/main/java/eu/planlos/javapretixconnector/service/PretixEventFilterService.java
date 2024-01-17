package eu.planlos.javapretixconnector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.planlos.javapretixconnector.config.PretixEventFilterConfig;
import eu.planlos.javapretixconnector.model.*;
import eu.planlos.javapretixconnector.model.dto.PretixSupportedActions;
import eu.planlos.javapretixconnector.repository.PretixEventFilterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PretixEventFilterService {

    private final PretixEventFilterConfig pretixEventFilterConfig;
    private final PretixEventFilterRepository pretixEventFilterRepository;

    @Autowired
    public PretixEventFilterService(PretixEventFilterConfig pretixEventFilterConfig, ObjectMapper objectMapper, PretixEventFilterRepository pretixEventFilterRepository) throws JsonProcessingException {
        this.pretixEventFilterConfig = pretixEventFilterConfig;
        this.pretixEventFilterRepository = pretixEventFilterRepository;

        if(pretixEventFilterConfig.isPropertiesSourceConfigured()) {
            log.info("Filters provided by properties file are configured");
            handlePropertiesAsFilterSource(objectMapper);
            return;
        }
        log.info("Filters provided by user are configured");
    }

    private void handlePropertiesAsFilterSource(ObjectMapper objectMapper) throws JsonProcessingException {
        if(pretixEventFilterRepository.findAll().isEmpty()) {
            log.debug("No filters in DB found. Proceed to persist filters from properties file");
            persistFilterFromProperties(objectMapper);
        } else {
            throw new IllegalArgumentException("Config file is configured as source for filters, but filters already exist in DB. Clean DB or switch to source USER");
        }
    }

    private void persistFilterFromProperties(ObjectMapper objectMapper) throws JsonProcessingException {
        pretixEventFilterRepository.saveAll(
                new StringToPretixEventFilterConverter(objectMapper)
                        .convertAll(pretixEventFilterConfig.getFilterList()));
    }

    /**
     * Constructor package private for tests
     * @param pretixEventFilterList Test filter list
     */
    PretixEventFilterService(PretixEventFilterRepository pretixEventFilterRepository, List<PretixEventFilter> pretixEventFilterList) {
        this.pretixEventFilterConfig = new PretixEventFilterConfig(null, null);
        this.pretixEventFilterRepository = pretixEventFilterRepository;
        this.pretixEventFilterRepository.saveAll(pretixEventFilterList);
    }

    /*
     * User source methods
     */

    /**
     * Creates filter if it exists or not
     * Not idempotent repo access, will create new for each call
     * @param pretixEventFilter Filter object
     */
    public void addFilter(PretixEventFilter pretixEventFilter) {
        if(pretixEventFilter.getId() == null) {
            pretixEventFilterRepository.save(pretixEventFilter);
            return;
        }
        throw new IllegalArgumentException("Filter must not have id");
    }

    public Optional<PretixEventFilter> getFilter(Long id) {
        return pretixEventFilterRepository.findById(id);
    }

    public List<PretixEventFilter> getAllFilters() {
        return pretixEventFilterRepository.findAll();
    }

    /**
     * Creates or updates filter.
     * Idempotent method.
     * @param pretixEventFilter Filter object
     */
    public void updateFilter(PretixEventFilter pretixEventFilter) {
        if(pretixEventFilter.getId() == null) {
            throw new IllegalArgumentException("Filter must have have id");
        }
        if(! pretixEventFilterRepository.existsById(pretixEventFilter.getId())) {
            throw new EntityNotFoundException("Filter does not exist id=" + pretixEventFilter.getId());
        }
        pretixEventFilterRepository.save(pretixEventFilter);
    }

    public void deleteFilter(Long id) {
        if(getFilter(id).isPresent()) {
            pretixEventFilterRepository.deleteById(id);
            return;
        }
        throw new EntityNotFoundException("Filter does not exist id=" + id);
    }

    /*
     * Filtering methods
     */

    /**
     * Looks if filter exists for given action-booking that is interested in it.
     * @param action to be looked for
     * @param booking to be looked for
     * @return true if there is no  filter at all, that wants the booking
     */
    public boolean bookingNotWantedByAnyFilter(PretixSupportedActions action, Booking booking) {
        List<Position> ticketPositionList = booking.getPositionList().stream()
                .filter(position -> ! position.getProduct().getProductType().isAddon())
                .filter(position -> matchesEventFilter(action.getAction(), booking.getOrganizer(), booking.getEvent(), position.getQnA()))
                .toList();
        return ticketPositionList.isEmpty();
    }

    /**
     * Check if matching filter exists in database
     * Not private for tests.
     * @param action to be looked for
     * @param event to be looked for
     * @param qnaMap to be looked for
     * @return true if a filter exists
     */
    boolean matchesEventFilter(String action, String organizer, String event, Map<Question, Answer> qnaMap) {
        return pretixEventFilterRepository.findByActionAndOrganizerAndEvent(action, organizer, event).stream()
                .anyMatch(filter -> filter.filterQnA(qnaMap));
    }
}