package com.numbertheoryplayground.calculationsimpl.divisibility;

import java.util.ArrayList;
import java.util.List;
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
            inputPf
            .getFactorPfs()
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
}