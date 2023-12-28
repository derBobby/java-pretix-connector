package eu.planlos.javapretixconnector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import eu.planlos.javapretixconnector.config.PretixEventFilterConfig;
import eu.planlos.javapretixconnector.model.PretixEventFilter;
import eu.planlos.javapretixconnector.repository.PretixEventFilterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PretixEventFilterServiceTest {

    @Mock
    PretixEventFilterRepository repository;

    @Test
    public void correctJsonForFilter_serviceCreated() throws JsonProcessingException {
        PretixEventFilterConfig config = new PretixEventFilterConfig("PROPERTIES", "{\"action\": \"pretix.event.order.placed\", \"organizer\": \"organizer\", \"event\": \"event\", \"qna-list\": {}}");
        when(repository.findAll()).thenReturn(Collections.emptyList());

        new PretixEventFilterService(config, new ObjectMapper(), repository);
    }

    @Test
    public void brokenJsonForFilter_serviceNotCreated() {
        PretixEventFilterConfig config = new PretixEventFilterConfig("PROPERTIES", "\"action\": \"pretix.event.order.placed\", \"organizer\": \"organizer\", \"event\": \"event\", \"qna-list\": {}}");
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(MismatchedInputException.class, () -> new PretixEventFilterService(config, new ObjectMapper(), repository));
    }

    @Test
    public void correctJsonWithTwoFilters_serviceCreated() throws JsonProcessingException {
        PretixEventFilterConfig config = new PretixEventFilterConfig("PROPERTIES", "{\"action\": \"pretix.event.order.placed\", \"organizer\": \"organizer\", \"event\": \"event\", \"qna-list\": {}}||{\"action\": \"pretix.event.order.canceled\", \"organizer\": \"organizer\", \"event\": \"event\", \"qna-list\": {}}");
        when(repository.findAll()).thenReturn(Collections.emptyList());

        new PretixEventFilterService(config, new ObjectMapper(), repository);

        verify(repository, times(1)).saveAll(argThat(list -> ((List<PretixEventFilter>) list).size() == 2));
    }
}