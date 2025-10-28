package com.numbertheoryplayground.calculationsimpl.divisibility;

import java.util.*;
import java.util.stream.IntStream;
import com.numbertheoryplayground.calculationsimpl.PrimeFactorization;

import static com.numbertheoryplayground.InputValidation.assertIsInRange;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndPower;

/**
 * For the input number with a prime factorization (PF) of 5^2 × 7^3, numFactorsExpression
 * would be "(2 + 1) × (3 + 1)" and numFactors would be 12.
 */
public final class PrimeFactorizationAnswer {
    /**
     * Contains the factors and powers of the PF of the input number.
     */
    private final List<FactorAndPower> inputFps;
    
    private final String numFactorsExpression;
    
    private int numFactors;
    
    /**
     * Contains DTOs for the PFs of the factors of the input number, excluding 1 and the
     * input number.
     */
    private final List<PrimeFactorization.Dto> factorPfs;
    
    private PrimeFactorizationAnswer(PrimeFactorization inputPf) {
        inputFps = inputPf.getFps();
        
        var numFactorsExpressionParts = new ArrayList<String>(inputFps.size());
        numFactors = 1;
        for (FactorAndPower fp : inputFps) {
            int power = fp.power();
            numFactors *= power + 1;
            numFactorsExpressionParts.add(String.format("(%d + 1)", power));
        }
        numFactorsExpression = String.join(" × ", numFactorsExpressionParts);
        
        factorPfs =
            getFactorPfs(inputPf)
            .stream()
            .map(PrimeFactorization::toDto)
            .toList();
    }
    
    /**
     * None of the fields matter if the input is prime.
     */
    static PrimeFactorizationAnswer createIfNotPrime(int input) {
        assertIsInRange(input, DivisibilityAnswer.MIN_INPUT, DivisibilityAnswer.MAX_INPUT);
        var pf = new PrimeFactorization(input);
        return pf.isForAPrimeNumber() ? null : new PrimeFactorizationAnswer(pf);
    }
    
    public List<PrimeFactorization.FactorAndPower> getInputFps() {
        return inputFps;
    }
    
    public int getNumFactors() {
        return numFactors;
    }
    
    public String getNumFactorsExpression() {
        return numFactorsExpression;
    }
    
    public List<PrimeFactorization.Dto> getFactorPfs() {
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
                    
                    // If the factor is already...
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