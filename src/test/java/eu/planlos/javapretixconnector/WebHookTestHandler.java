package eu.planlos.javapretixconnector;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WebHookTestHandler implements IPretixWebHookHandler {

    @Override
    public void handleApprovalNotification(String action, String event, String code) {

    }

    @Override
    public Optional<String> handleUserCreation(String action, String event, String code) {
        return Optional.empty();
    }
}
