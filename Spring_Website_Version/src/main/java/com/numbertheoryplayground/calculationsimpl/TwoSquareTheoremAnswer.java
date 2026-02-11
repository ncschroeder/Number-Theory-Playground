package com.numbertheoryplayground.calculationsimpl;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.Calculations.isPrime;

public final class TwoSquareTheoremAnswer {
    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = TEN_THOUSAND;
    
    /**
     * The first Pythagorean prime ≥ the input.
     */
    private int pythagPrime;
    
    /**
     * a and b are the ints whose squares sum to pythagPrime.
     */
    private int a;
    
    private int b;
    
    public TwoSquareTheoremAnswer(int input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        pythagPrime = input;
        while (pythagPrime % 4 != 1) pythagPrime++;
        while (!isPrime(pythagPrime)) pythagPrime += 4;
        
        b = 0;
        for (a = 1; a < pythagPrime; a++) {
            int aSquared = a * a;
            int bSquared = pythagPrime - aSquared;
            var bDouble = Math.sqrt(bSquared);
            var bInt = (int) bDouble;
            if (bDouble == bInt) {
                b = bInt;
                break;
            }
        }
    }
    
    public int getPythagPrime() {
        return pythagPrime;
    }
    
    public int getA() {
        return a;
    }
    
    public int getB() {
        return b;
    }
}