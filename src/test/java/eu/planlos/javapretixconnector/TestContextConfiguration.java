package eu.planlos.javapretixconnector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@TestConfiguration
// The ComponentScan is for tests on PretixWebhookControllerTest.java, see comments there
@ComponentScan(basePackages = {"eu.planlos.javaspringwebutilities.web"})
public class TestContextConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}