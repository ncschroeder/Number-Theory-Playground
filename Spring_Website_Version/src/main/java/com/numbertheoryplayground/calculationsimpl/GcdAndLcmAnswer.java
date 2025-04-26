package com.numbertheoryplayground.calculationsimpl;

import java.util.ArrayList;
import java.util.List;

import static com.numbertheoryplayground.InputValidation.*;

public class GcdAndLcmAnswer {
    private static final long MAX_INPUT = ONE_BILLION;
    
    private final List<EuclideanIteration> euclideanIterations;
    private final PrimeFactorization.GcdAndLcmAnswer pfAnswer;
    
    public GcdAndLcmAnswer(int input1, int input2) {
        assertIsInRange(input1, PrimeFactorization.MIN_INPUT, MAX_INPUT);
        assertIsInRange(input2, PrimeFactorization.MIN_INPUT, MAX_INPUT);
        
        euclideanIterations = getEuclideanIterations(input1, input2);
        pfAnswer = new PrimeFactorization.GcdAndLcmAnswer(input1, input2);
    }
    
    public List<EuclideanIteration> getEuclideanIterations() {
        return euclideanIterations;
    }
    
    public PrimeFactorization.GcdAndLcmAnswer getPfAnswer() {
        return pfAnswer;
    }
    
    
    public record EuclideanIteration(int max, int min, int remainder) {}
    
    static List<EuclideanIteration> getEuclideanIterations(int input1, int input2) {
        int max = Math.max(input1, input2);
        int min = Math.min(input1, input2);
        int remainder = max % min;
        
        var iterations = new ArrayList<EuclideanIteration>();
        iterations.add(new EuclideanIteration(max, min, remainder));

        while (remainder != 0) {
            max = min;
            min = remainder;
            remainder = max % min;
            iterations.add(new EuclideanIteration(max, min, remainder));
        }
        
        return iterations;
    }
}
