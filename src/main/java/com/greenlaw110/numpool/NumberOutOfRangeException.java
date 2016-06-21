package com.greenlaw110.numpool;

/**
 * Represent a error when a number is out of a scope of
 * a {@link Block}
 */
public class NumberOutOfRangeException extends IllegalArgumentException {
    private long l;
    public NumberOutOfRangeException(long number) {
        l = number;
    }

    @Override
    public String getMessage() {
        return "Number is out of the range: " + l;
    }
}
