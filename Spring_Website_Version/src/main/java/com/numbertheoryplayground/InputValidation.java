package com.numbertheoryplayground;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InputValidation {
    // Max input constants.
    public static final long ONE_BILLION = 1_000_000_000L;
    public static final long NINE_QUADRILLION = 9_000_000_000_000_000L;
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class InvalidInputNumberException extends IllegalArgumentException {
        public InvalidInputNumberException() {}
    }
    
    public static void assertIsInRange(long l, long min, long max) {
        if (l < min || l > max) {
            throw new InvalidInputNumberException();
        }
    }
}
