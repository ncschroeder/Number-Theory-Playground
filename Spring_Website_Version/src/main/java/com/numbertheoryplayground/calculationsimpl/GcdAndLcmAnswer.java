package com.numbertheoryplayground.calculationsimpl;

import java.util.ArrayList;
import java.util.List;

public class GcdAndLcmAnswer {
    public final List<EuclideanIteration> euclideanIterations;
    public final PrimeFactorization.GcdAndLcmAnswer pfAnswer;
    
    public GcdAndLcmAnswer(int input1, int input2) {
        euclideanIterations = getEuclideanIterations(input1, input2);
        pfAnswer = new PrimeFactorization.GcdAndLcmAnswer(input1, input2);
    }
    
    public record EuclideanIteration(int max, int min, int remainder) {}
    
    private static List<EuclideanIteration> getEuclideanIterations(int input1, int input2) {
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
