package eu.planlos.javapretixconnector;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.planlos.javapretixconnector.config.PretixApiConfig;
import eu.planlos.javapretixconnector.config.PretixFeatureConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@TestConfiguration
public class TestContextConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PretixFeatureConfig pretixFeatureConfig(){
        return new PretixFeatureConfig(false, false);
    }

    @Bean
    public PretixApiConfig pretixApiConfig(){
        return new PretixApiConfig(false, "", "", "", Collections.emptyList(), 1, 1);
    }
}