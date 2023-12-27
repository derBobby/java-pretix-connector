package eu.planlos.javapretixconnector.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({PretixApiConfig.class, PretixFeatureConfig.class})
public class PretixConfigBeanConfiguration {
}