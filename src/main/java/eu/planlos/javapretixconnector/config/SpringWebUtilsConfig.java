package eu.planlos.javapretixconnector.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/*
 * Reason for this class:
 * https://stackoverflow.com/a/72172859/7350955
 */
@Configuration
@ComponentScan(basePackages = "eu.planlos.javaspringwebutilities")
@Profile("java-pretix-connector-dev")
public class SpringWebUtilsConfig {
}
