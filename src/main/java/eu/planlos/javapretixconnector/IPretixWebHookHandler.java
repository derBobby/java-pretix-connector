package eu.planlos.javapretixconnector;

import eu.planlos.javapretixconnector.model.dto.PretixSupportedActions;
import eu.planlos.javapretixconnector.model.dto.WebHookResult;

public interface IPretixWebHookHandler {
    WebHookResult handleWebhook(PretixSupportedActions action, String event, String code);
}
