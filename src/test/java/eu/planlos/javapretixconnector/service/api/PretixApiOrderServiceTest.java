package eu.planlos.javapretixconnector.service.api;

import eu.planlos.javapretixconnector.config.PretixApiConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PretixApiOrderServiceTest {

    @Mock
    PretixApiConfig config;

    @Mock
    WebClient webClient;

    @Test
    void urlEndingWithSlash_returnsCorrectUrl() {
        PretixApiOrderService service = new PretixApiOrderService(config, webClient);
        when(config.organizer()).thenReturn("organizer");
        when(config.address()).thenReturn("https://test.domain/");

        String url = service.getEventUrl("event-name", "order-code");

        assertFalse(url.contains("//"));
    }
}