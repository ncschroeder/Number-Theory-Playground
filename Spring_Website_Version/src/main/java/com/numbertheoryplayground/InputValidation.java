package com.numbertheoryplayground;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InputValidation {
    // Max input constants
    public static final long TEN_THOUSAND = 10_000;
    public static final long ONE_QUADRILLION = 1_000_000_000_000_000L;
    
    /**
     * This exception is used for back end validation and isn't supposed to be thrown since
     * there's also front end validation. The constructor is used by assertIsInRange below and
     * by Calculations.getGoldbachPrimePairStarts.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class InvalidInputNumberException extends IllegalArgumentException {
        private InvalidInputNumberException() {}
        
        private static final InvalidInputNumberException instance = new InvalidInputNumberException();
        
        public static InvalidInputNumberException getInstance() {
            return instance;
        }
    }
    
    public static void assertIsInRange(long input, long minInput, long maxInput) {
        if (input < minInput || input > maxInput) {
            throw InvalidInputNumberException.instance;
        }
    }
}