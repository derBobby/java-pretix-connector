package eu.planlos.javapretixconnector.controller;

import eu.planlos.javapretixconnector.IPretixWebHookHandler;
import eu.planlos.javapretixconnector.model.dto.PretixSupportedActions;
import eu.planlos.javapretixconnector.model.dto.WebHookDTO;
import eu.planlos.javapretixconnector.model.dto.WebHookResult;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PretixWebhookController.URL_WEBHOOK)
@Slf4j
public class PretixWebhookController {

    public static final String URL_WEBHOOK = "/api/v1/webhook";

    private final IPretixWebHookHandler webHookHandler;

    public PretixWebhookController(IPretixWebHookHandler webHookHandler) {
        this.webHookHandler = webHookHandler;
    }

    @PostMapping
    public ResponseEntity<String> webHook(@Valid @RequestBody WebHookDTO hook) {

        log.info("Incoming webhook={}", hook);

        try {
            WebHookResult webHookResult = webHookHandler.handleWebhook(getAction(hook), hook.event(), hook.code());
            String message = webHookResult.message();
            log.info("Webhook result: successful={} | message={}", webHookResult.successful(), message);

            return webHookResult.successful()
                    ? ResponseEntity.ok().body(String.format("WebHook has been processed: %s", message))
                    : ResponseEntity.badRequest().body(String.format("Error processing Webhook: %s", message));

        } catch (Exception e) {
            log.info("Webhook threw exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(String.format("Error processing Webhook: %s", e.getMessage()));
        }
    }

    private PretixSupportedActions getAction(WebHookDTO hook) {
        log.debug("Looking up Enum for {}", hook.action());
        return PretixSupportedActions.getEnumByAction(hook.action());
    }
}