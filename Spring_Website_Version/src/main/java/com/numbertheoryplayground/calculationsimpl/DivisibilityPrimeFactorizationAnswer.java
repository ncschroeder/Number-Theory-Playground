package com.numbertheoryplayground.calculationsimpl;

import java.util.*;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndExponent;

public final class DivisibilityPrimeFactorizationAnswer {
    private static final int MIN_INPUT = 10;
    private static final int MAX_INPUT = PrimeFactorization.MAX_INPUT;
    
    private final List<FactorAndExponent> inputPfFes;
    
    private final List<PrimeFactorization> factorPfs;
    
    private DivisibilityPrimeFactorizationAnswer(PrimeFactorization inputPf) {
        inputPfFes = inputPf.getFes();
        factorPfs = getFactorPfs(inputPf);
    }
    
    /**
     * None of the fields matter if the input is prime.
     */
    public static DivisibilityPrimeFactorizationAnswer createIfNotPrime(int input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        var pf = new PrimeFactorization(input);
        return pf.isForAPrimeNumber() ? null : new DivisibilityPrimeFactorizationAnswer(pf);
    }
    
    public List<FactorAndExponent> getInputPfFes() {
        return inputPfFes;
    }
    
    public List<PrimeFactorization> getFactorPfs() {
        return factorPfs;
    }
    
    
    /**
     * This method finds PFs of factors of the input PF's corresponding int, excluding 1 and the
     * corresponding int, by finding subfactorizations in the input PF and that's done by
     * sorted by corresponding ints.
     * finding combinations of factors and exponents in that PF. The PFs in the list returned
     */
    static List<PrimeFactorization> getFactorPfs(PrimeFactorization pf) {
        /*
        Let numFactors be the number of factors of the corresponding long of the input PF,
        including 1 and the corresponding long.
         */
        var numFactors = 1;
        for (FactorAndExponent fe : pf) {
            numFactors *= fe.exponent() + 1;
        }
        
        /*
        The algorithm below will add a PF to factorPfs that's the same as the input PF but
        then remove it, so the capacity for factorPfs will be set to numFactors - 1 and its
        size at the end will be numFactors - 2.
         */
        var factorPfs = new ArrayList<PrimeFactorization>(numFactors - 1);
        
        for (FactorAndExponent fe : pf) {
            int primeFactor = fe.factor();
            int maxExponent = fe.exponent();
            /*
            In the 2nd for loop below, we want to iterate through all the PFs that are in factorPfs
            at this point, and not the ones that get added below. Use this variable for that.
             */
            int lastPfIndexToUse = factorPfs.size() - 1;
            
            for (var exponent = 1; exponent <= maxExponent; exponent++) {
                var feToAdd =
                    exponent == maxExponent ? fe : new FactorAndExponent(primeFactor, exponent);
                factorPfs.add(new PrimeFactorization(List.of(feToAdd)));
                
                for (var i = 0; i <= lastPfIndexToUse; i++) {
                    List<FactorAndExponent> listFactorFes = factorPfs.get(i).getFes();
                    var newFactorFes = new ArrayList<FactorAndExponent>(listFactorFes.size() + 1);
                    newFactorFes.addAll(listFactorFes);
                    newFactorFes.add(feToAdd);
                    factorPfs.add(new PrimeFactorization(newFactorFes));
                }
            }
        }
        
        factorPfs.removeLast();
        factorPfs.sort(Comparator.comparingLong(PrimeFactorization::getCorrespondingLong));
        return factorPfs;
    }
}