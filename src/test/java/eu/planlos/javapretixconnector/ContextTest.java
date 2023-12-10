package eu.planlos.javapretixconnector;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ConfigurationPropertiesScan
@SpringBootApplication
@ContextConfiguration(classes = ContextTest.LibraryTestConfiguration.class)
class ContextTest {

    @Autowired
    private IPretixWebHookHandler libraryInterface;

    @Test
    void contextLoads() {
        //Test if Spring context starts
    }

    @TestConfiguration
    @ComponentScan(basePackages = "eu.planlos.javapretixconnector")
    static class LibraryTestConfiguration {
        @Bean
        public IPretixWebHookHandler libraryInterface() {
            return Mockito.mock(IPretixWebHookHandler.class);
        }
    }
}