package eu.planlos.javapretixconnector.model.dto;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PretixSupportedActions {
    ORDER_PLACED("pretix.event.order.placed", "New"),
    ORDER_NEED_APPROVAL("pretix.event.order.placed.require_approval", "Need OK"),
    ORDER_APPROVED("pretix.event.order.approved", "Got OK"),
    ORDER_CHANGED("pretix.event.order.modified", "Changed"),
    ORDER_CANCELED("pretix.event.order.canceled", "Cancelled");

    private final String action;
    private final String description;

    PretixSupportedActions(String action, String description) {
        this.action = action;
        this.description = description;
    }

    public static boolean isSupportedAction(String actionString) {
        return Arrays.stream(values())
                .anyMatch(supportedAction -> supportedAction.getAction().equals(actionString));
    }

    public static PretixSupportedActions getEnumByAction(String targetAction) {
        return Arrays.stream(values())
                .filter(supportedAction -> supportedAction.getAction().equals(targetAction))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Action not available"));
    }
}