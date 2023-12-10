package eu.planlos.javapretixconnector.common;

import java.io.Serial;

public class PretixException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 0L;

    public static final String IS_NULL = "The result from the Pretix API was NULL";

    public PretixException(String message) {
        super(message);
    }
}
