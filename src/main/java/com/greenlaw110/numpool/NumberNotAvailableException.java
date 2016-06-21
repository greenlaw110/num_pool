package com.greenlaw110.numpool;

/**
 * Represent an error that a number requested is already consumed
 */
public class NumberNotAvailableException extends IllegalArgumentException {
    private long l;
    public NumberNotAvailableException(long number) {
        l = number;
    }

    @Override
    public String getMessage() {
        return "Number is not available: " + l;
    }
}
