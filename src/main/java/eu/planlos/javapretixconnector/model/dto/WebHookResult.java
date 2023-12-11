package eu.planlos.javapretixconnector.model.dto;

public record WebHookResult(boolean successful, String message) {

    @Override
    public String message() {
        return message == null || message.isEmpty() ? "No message" : message;
    }
}
