package com.numbertheoryplayground.calculationsimpl.gcdandlcm;

import java.util.ArrayList;
import java.util.List;
import com.numbertheoryplayground.calculationsimpl.PrimeFactorization;

import static com.numbertheoryplayground.InputValidation.*;

public final class GcdAndLcmAnswer {
    static final long MIN_INPUT = PrimeFactorization.MIN_INPUT;
    static final long MAX_INPUT = TEN_THOUSAND;
    
    private final List<EuclideanIteration> euclideanIterations;
    
    private final PrimeFactorizationAnswer pfAnswer;
    
    public GcdAndLcmAnswer(int input1, int input2) {
        euclideanIterations = getEuclideanIterations(input1, input2);
        pfAnswer = new PrimeFactorizationAnswer(input1, input2);
    }
    
    public List<EuclideanIteration> getEuclideanIterations() {
        return euclideanIterations;
    }
    
    public PrimeFactorizationAnswer getPfAnswer() {
        return pfAnswer;
    }
    
    
    public record EuclideanIteration(int max, int min, int remainder) {}
    
    static List<EuclideanIteration> getEuclideanIterations(int input1, int input2) {
        assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
        assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
        
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