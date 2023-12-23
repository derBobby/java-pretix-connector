package eu.planlos.javapretixconnector;

import eu.planlos.javapretixconnector.model.dto.PretixSupportedActions;
import eu.planlos.javapretixconnector.model.dto.WebHookResult;

public interface IPretixWebHookHandler {
    WebHookResult handleWebhook(String organizer, String event, String code, PretixSupportedActions action);
}
