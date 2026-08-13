package com.numbertheoryplayground.calculationsimpl.gcdandlcm;

import java.util.*;
import com.numbertheoryplayground.calculationsimpl.PrimeFactorization;

import static com.numbertheoryplayground.InputValidation.assertIsInRange;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndExponent;
import static com.numbertheoryplayground.calculationsimpl.gcdandlcm.GcdAndLcmAnswer.*;

/**
 * This class uses prime factorizations to find the greatest common divisor (GCD) and
 * least common multiple (LCM) of 2 ints.
 */
public final class PrimeFactorizationAnswer {
    private final List<FactorAndExponent> input1PfFes;
    
    private final List<FactorAndExponent> input2PfFes;
    
    /**
     * If the GCD of the inputs is 1, this is null since only integers > 1 have a prime factorization.
     */
    private final PrimeFactorization gcdPf;
    
    private final PrimeFactorization lcmPf;
    
    PrimeFactorizationAnswer(int input1, int input2) {
        assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
        assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
        
        var input1Pf = new PrimeFactorization(input1);
        input1PfFes = input1Pf.getFes();
        
        if (input1 == input2) {
            input2PfFes = input1PfFes;
            lcmPf = gcdPf = input1Pf;
            return;
        }
        
        var input2Pf = new PrimeFactorization(input2);
        input2PfFes = input2Pf.getFes();
        var gcdPfFes = new ArrayList<FactorAndExponent>();
        var lcmPfFes = new ArrayList<FactorAndExponent>();
        
        for (FactorAndExponent fe : input1Pf) {
            int factor = fe.factor();
            int exponent1 = fe.exponent();
            
            input2Pf
            .getExponentOf(factor)
            .ifPresentOrElse(
                exponent2 -> {
                    gcdPfFes.add(new FactorAndExponent(factor, Math.min(exponent1, exponent2)));
                    lcmPfFes.add(new FactorAndExponent(factor, Math.max(exponent1, exponent2)));
                },
                () -> lcmPfFes.add(fe)
            );
        }
        
        for (FactorAndExponent fe : input2Pf) {
            if (!input1Pf.containsFactor(fe.factor())) {
                lcmPfFes.add(fe);
            }
        }
        
        gcdPf = gcdPfFes.isEmpty() ? null : new PrimeFactorization(gcdPfFes);
        lcmPf = new PrimeFactorization(lcmPfFes);
    }
    
    public List<FactorAndExponent> getInput1PfFes() {
        return input1PfFes;
    }
    
    public List<FactorAndExponent> getInput2PfFes() {
        return input2PfFes;
    }
    
    public PrimeFactorization getGcdPf() {
        return gcdPf;
    }
    
    public PrimeFactorization getLcmPf() {
        return lcmPf;
    }
}