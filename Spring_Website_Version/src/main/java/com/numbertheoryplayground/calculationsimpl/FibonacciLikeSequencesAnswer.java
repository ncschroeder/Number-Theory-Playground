package com.numbertheoryplayground.calculationsimpl;

import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;

public final class FibonacciLikeSequencesAnswer {
    private static final int MIN_INPUT = 1;
    private static final int MAX_INPUT = ONE_BILLION;
    
    /**
     * Contains strings of the numbers in the Fibonacci-like sequence that gets created.
     * Strings are used since these numbers might be too big for a JavaScript safe integer.
     */
    private final List<Long> fiboLikeSequence;
    
    /**
     * Contains RatioDatas for the 4th and 5th, 9th and 10th, 14th and 15th, and
     * 19th and 20th numbers in the Fibonacci-like sequence that gets created.
     */
    private final List<RatioData> ratiosData;
    
    public FibonacciLikeSequencesAnswer(int input1, int input2) {
        assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
        assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
        
        final int sequenceLength = 20;
        fiboLikeSequence = new ArrayList<>(sequenceLength);
        long long1 = input1;
        long long2 = input2;
        fiboLikeSequence.add(long1);
        fiboLikeSequence.add(long2);
        
        while (fiboLikeSequence.size() < sequenceLength) {
            var nextLong = long1 + long2;
            fiboLikeSequence.add(nextLong);
            long1 = long2;
            long2 = nextLong;
        }
        
        ratiosData =
            IntStream.of(3, 8, 13, 18)
            .mapToObj(i ->
                new RatioData(fiboLikeSequence.get(i), fiboLikeSequence.get(i + 1))
            )
            .toList();
    }
    
    public List<Long> getFiboLikeSequence() {
        return fiboLikeSequence;
    }
    
    public List<RatioData> getRatiosData() {
        return ratiosData;
    }
    
    
    /**
     */
    public static final class RatioData {
        private static final MathContext MATH_CONTEXT_WITH_ROUNDING = MathContext.DECIMAL64;
        
        private static final MathContext MATH_CONTEXT_WITHOUT_ROUNDING =
            new MathContext(MATH_CONTEXT_WITH_ROUNDING.getPrecision(), RoundingMode.UNNECESSARY);
        
        private final long long1;
        
        private final long long2;
        
        private BigDecimal ratio;
        
        private boolean isRounded;
        
        RatioData(long long1, long long2) {
            this.long1 = long1;
            this.long2 = long2;
            var bigDecimal1 = new BigDecimal(long1);
            var bigDecimal2 = new BigDecimal(long2);
            
            try {
                ratio = bigDecimal2.divide(bigDecimal1, MATH_CONTEXT_WITHOUT_ROUNDING);
                isRounded = false;
            } catch (ArithmeticException ex) {
                ratio = bigDecimal2.divide(bigDecimal1, MATH_CONTEXT_WITH_ROUNDING);
                isRounded = true;
            }
        }
        
        @JsonProperty("num1")
        public long getLong1() {
            return long1;
        }
        
        @JsonProperty("num2")
        public long getLong2() {
            return long2;
        }
        
        public BigDecimal getRatio() {
            return ratio;
        }
        
        /*
        I want the JSON property to be "isRounded" but the Jackson JSON mapper
        removes "is" by default.
         */
        @JsonProperty("isRounded")
        public boolean isRounded() {
            return isRounded;
        }
    }
}