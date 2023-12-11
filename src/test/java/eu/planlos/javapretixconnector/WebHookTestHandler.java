package eu.planlos.javapretixconnector;

import eu.planlos.javapretixconnector.model.dto.PretixSupportedActions;
import eu.planlos.javapretixconnector.model.dto.WebHookResult;
import org.springframework.stereotype.Service;

@Service
public class WebHookTestHandler implements IPretixWebHookHandler {

    @Override
    public WebHookResult handleWebhook(PretixSupportedActions action, String event, String code) {
        return new WebHookResult(true, "Test message");
    }
}
