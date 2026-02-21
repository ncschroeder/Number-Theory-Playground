package com.numbertheoryplayground.calculationsimpl;

import java.util.*;
import java.util.stream.IntStream;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndPower;

public final class DivisibilityPrimeFactorizationAnswer {
    private static final long MIN_INPUT = 10;
    private static final long MAX_INPUT = TEN_THOUSAND;
    
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
     * corresponding int, by finding sub-factorizations in the input PF and that's done by
     * finding combinations of factors and powers in that PF. The PFs in the list returned are
     * sorted by corresponding ints.
     */
    static List<PrimeFactorization> getFactorPfs(PrimeFactorization pf) {
        var factorPfs = new ArrayList<PrimeFactorization>();
        
        for (FactorAndPower fp : pf.getFps()) {
            int factor = fp.factor();
            int inputPfPower = fp.power();
            
            /*
            In the 2nd for loop below, we want to iterate through all the PFs that are in factorPfs
            at this point, and not the ones that get added below. Use this variable for that.
             */
            int lastPfIndexToUse = factorPfs.size() - 1;
            
            for (var factorPfPower = 1; factorPfPower <= inputPfPower; factorPfPower++) {
                var singleton = List.of(new FactorAndPower(factor, factorPfPower));
                factorPfs.add(new PrimeFactorization(singleton));
                
                for (var i = 0; i <= lastPfIndexToUse; i++) {
                    var newFp = new FactorAndPower(factor, factorPfPower);
                    List<FactorAndPower> factorPfFps = new ArrayList<>(factorPfs.get(i).getFps());
                    
                    findIndexOfFactor(factorPfFps, factor)
                    .ifPresentOrElse(
                        indexToUpdate -> factorPfFps.set(indexToUpdate, newFp),
                        () -> factorPfFps.add(newFp)
                    );
                    
                    factorPfs.add(new PrimeFactorization(factorPfFps));
                }
            }
        }
        
        /*
        The last PF has all the factors that this PF has and each power is the same as the powers in
        this PF, so it's the same as this PF. We don't want to include that as part of the factors.
         */
        factorPfs.removeLast();
        factorPfs.sort(Comparator.comparingInt(PrimeFactorization::getCorrespondingInt));
        return factorPfs;
    }
    
    private static OptionalInt findIndexOfFactor(List<FactorAndPower> fps, int factor) {
        return
            IntStream.range(0, fps.size())
            .filter(i -> fps.get(i).factor() == factor)
            .findFirst();
    }
}