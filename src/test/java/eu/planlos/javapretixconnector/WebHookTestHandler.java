package eu.planlos.javapretixconnector;

import eu.planlos.javapretixconnector.model.dto.PretixSupportedActions;
import eu.planlos.javapretixconnector.model.dto.WebHookResult;
import org.springframework.stereotype.Service;

@Service
public class WebHookTestHandler implements IPretixWebHookHandler {

    @Override
    public WebHookResult handleWebhook(String organizer, String event, String code, PretixSupportedActions action) {
        return new WebHookResult(true, "Test message");
    }
}
