package eu.planlos.javapretixconnector;

import eu.planlos.javapretixconnector.model.dto.PretixSupportedActions;
import eu.planlos.javapretixconnector.model.dto.WebHookResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Profile;

@Profile("java-pretix-connector-dev")
@SpringBootApplication
@ConfigurationPropertiesScan
public class PretixConnector implements IPretixWebHookHandler{

    public static void main(String[] args) {
        SpringApplication.run(PretixConnector.class, args);
    }

    @Override
    public WebHookResult handleWebhook(String organizer, String event, String code, PretixSupportedActions action) {
        return null;
    }
}