package com.numbertheoryplayground.calculationsimpl;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.Calculations.isPrime;

public class TwoSquareTheoremAnswer {
    public int primeNum;
    public int a;
    public int b;
    
    public TwoSquareTheoremAnswer(int input) {
        assertIsInRange(input, 0, ONE_BILLION);
        
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
}
