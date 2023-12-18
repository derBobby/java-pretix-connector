package eu.planlos.javapretixconnector.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
public class PretixWebClientConfig {

    @Bean
    @Qualifier("PretixWebClient")
    public static WebClient configurePretixWebClient(PretixApiConfig apiConfig) {

        log.info("Creating WebClient using:");
        log.info("- Pretix address: {}", apiConfig.address());
        log.info("- Pretix organizer: {}", apiConfig.organizer());

        return WebClient.builder()
                .baseUrl(apiConfig.address())
                //TODO inject from outside
//                .filter(WebClientRequestFilter.logRequest())
//                .filter(WebClientResponseFilter.logResponse())
//                .filter(WebClientResponseFilter.handleError())
                .defaultHeaders(httpHeaders -> httpHeaders.set(HttpHeaders.AUTHORIZATION, String.join("", "Token ", apiConfig.token())))
                .build();
    }
}