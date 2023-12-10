package eu.planlos.javapretixconnector.model.validation;

import eu.planlos.javapretixconnector.model.dto.PretixSupportedActions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ActionValidatorTest {

    private final ActionValidator actionValidator = new ActionValidator();

    @Test
    public void actionIsValid_returnsTrue() {
        Assertions.assertTrue(actionValidator.isValid(PretixSupportedActions.ORDER_APPROVED.getAction(), null));
    }

    @Test
    public void actionIsInvalid_returnsFalse() {
        Assertions.assertFalse(actionValidator.isValid("fantasy.action", null));
    }
}