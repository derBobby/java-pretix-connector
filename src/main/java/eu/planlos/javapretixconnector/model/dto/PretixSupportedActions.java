package eu.planlos.javapretixconnector.model.dto;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PretixSupportedActions {
    ORDER_PLACED("pretix.event.order.placed"),
    ORDER_NEED_APPROVAL("pretix.event.order.placed.require_approval"),
    ORDER_APPROVED("pretix.event.order.approved"),
    ORDER_CANCELED("pretix.event.order.canceled");

    private final String action;

    PretixSupportedActions(String action) {
        this.action = action;
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