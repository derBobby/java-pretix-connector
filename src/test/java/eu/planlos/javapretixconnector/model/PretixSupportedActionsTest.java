package eu.planlos.javapretixconnector.model;

import eu.planlos.javapretixconnector.model.dto.PretixSupportedActions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PretixSupportedActionsTest {

    @Test
    public void getAction() {
        String action = PretixSupportedActions.ORDER_APPROVED.getAction();
        PretixSupportedActions retrievedEnum = PretixSupportedActions.getEnumByAction(action);
        assertEquals(PretixSupportedActions.ORDER_APPROVED, retrievedEnum);
    }
}