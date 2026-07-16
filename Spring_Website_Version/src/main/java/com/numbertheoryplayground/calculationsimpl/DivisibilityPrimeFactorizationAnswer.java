package com.numbertheoryplayground.calculationsimpl;

import java.util.*;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndPower;

public final class DivisibilityPrimeFactorizationAnswer {
    private static final int MIN_INPUT = 10;
    private static final int MAX_INPUT = PrimeFactorization.MAX_INPUT;
    
    /**
     * Contains the factors and powers of the PF of the input number.
     */
    private final List<FactorAndPower> inputFps;
    
    private final List<PrimeFactorization> factorPfs;
    
    private DivisibilityPrimeFactorizationAnswer(PrimeFactorization inputPf) {
        inputFps = inputPf.getFps();
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
    
    public List<FactorAndPower> getInputFps() {
        return inputFps;
    }
    
    public List<PrimeFactorization> getFactorPfs() {
        return factorPfs;
    }
    
    
    /**
     * This method finds PFs of factors of the input PF's corresponding int, excluding 1 and the
     * corresponding int, by finding subfactorizations in the input PF and that's done by
     * finding combinations of factors and powers in that PF. The PFs in the list returned are
     * sorted by corresponding ints.
     */
    static List<PrimeFactorization> getFactorPfs(PrimeFactorization pf) {
        /*
        Let numFactors be the number of factors of the corresponding long of the input PF,
        including 1 and the corresponding long.
         */
        var numFactors = 1;
        for (FactorAndPower fp : pf.getFps()) {
            numFactors *= fp.power() + 1;
        }
        
        /*
        The algorithm below will add a PF to factorPfs that's the same as the input PF but
        then remove it, so the capacity for factorPfs will be set to numFactors - 1 and its
        size at the end will be numFactors - 2.
         */
        var factorPfs = new ArrayList<PrimeFactorization>(numFactors - 1);
        
        for (FactorAndPower fp : pf.getFps()) {
            int primeFactor = fp.factor();
            int maxPower = fp.power();
            /*
            In the 2nd for loop below, we want to iterate through all the PFs that are in factorPfs
            at this point, and not the ones that get added below. Use this variable for that.
             */
            int lastPfIndexToUse = factorPfs.size() - 1;
            
            for (var power = 1; power <= maxPower; power++) {
                var singleton = List.of(new FactorAndPower(primeFactor, power));
                factorPfs.add(new PrimeFactorization(singleton));
                
                for (var i = 0; i <= lastPfIndexToUse; i++) {
                    List<FactorAndPower> listFactorFps = factorPfs.get(i).getFps();
                    var newFactorFps = new ArrayList<FactorAndPower>(listFactorFps.size() + 1);
                    newFactorFps.addAll(listFactorFps);
                    newFactorFps.add(new FactorAndPower(primeFactor, power));
                    factorPfs.add(new PrimeFactorization(newFactorFps));
                }
            }
        }
        
        factorPfs.removeLast();
        factorPfs.sort(Comparator.comparingLong(PrimeFactorization::getCorrespondingLong));
        return factorPfs;
    }
}