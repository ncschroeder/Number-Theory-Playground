package com.numbertheoryplayground.calculationsimpl;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.Calculations.isPrime;

public final class TwoSquareTheoremAnswer {
    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = TEN_THOUSAND;
    
    /**
     * The first prime number ≥ the input that's 1 above a multiple of 4.
     */
    private int primeNum;
    // a and b are the ints whose squares sum to primeNum.
    private int a;
    private int b;
    
    public TwoSquareTheoremAnswer(int input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        primeNum = input;
        while (primeNum % 4 != 1) primeNum++;
        while (!isPrime(primeNum)) primeNum += 4;
        
        b = 0;
        for (a = 1; a < primeNum; a++) {
            int aSquared = a * a;
            int bSquared = primeNum - aSquared;
            var bDouble = Math.sqrt(bSquared);
            var bInt = (int) bDouble;
            if (bDouble == bInt) {
                b = bInt;
                break;
            }
        }
    }
    
    public int getPrimeNum() {
        return primeNum;
    }
    
    public int getA() {
        return a;
    }
    
    public int getB() {
        return b;
    }
}
