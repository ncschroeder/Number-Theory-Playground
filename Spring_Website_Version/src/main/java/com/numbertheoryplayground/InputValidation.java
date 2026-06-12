package com.numbertheoryplayground;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InputValidation {
    // Max input constants
    public static final int ONE_MILLION = 1_000_000;
    public static final int ONE_BILLION = 1_000_000_000;
    
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
    
    public static void assertIsInRange(int input, int minInput, int maxInput) {
        if (input < minInput || input > maxInput) {
            throw InvalidInputNumberException.instance;
        }
    }
}