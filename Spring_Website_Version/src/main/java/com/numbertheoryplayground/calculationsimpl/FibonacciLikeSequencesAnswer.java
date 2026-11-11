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
     * This can be a list of longs since if 1 billion is used as both inputs, then the last
     * number in the sequence is 6 trillion something, which is small enough to be a long
     * and JavaScript safe integer.
     */
    private final List<Long> fiboLikeSequence;
    
    /**
     * Contains Divisions for the 5th number in fiboLikeSequence divided by the 4th,
     * the 10th by the 9th, the 15th by the 14th, and the 20th by the 19th.
     */
    private final List<Division> divisions;
    
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
        
        divisions =
            IntStream.of(4, 9, 14, 19)
            .mapToObj(i -> new Division(fiboLikeSequence.get(i), fiboLikeSequence.get(i - 1)))
            .toList();
    }
    
    public List<Long> getFiboLikeSequence() {
        return fiboLikeSequence;
    }
    
    public List<Division> getDivisions() {
        return divisions;
    }
    
    
    /**
     * Contains data for the division of 2 longs.
     */
    public static final class Division {
        private static final MathContext MATH_CONTEXT_WITH_ROUNDING = MathContext.DECIMAL64;
        
        private static final MathContext MATH_CONTEXT_WITHOUT_ROUNDING =
            new MathContext(MATH_CONTEXT_WITH_ROUNDING.getPrecision(), RoundingMode.UNNECESSARY);
        
        private final long dividend;
        
        private final long divisor;
        
        /**
         * A string is used since the quotient can have up to 16 significant digits
         * and JavaScript numbers can have a max of 15 to 17 significant digits.
         */
        private final String quotientString;
        
        private boolean quotientIsRounded;
        
        Division(long dividend, long divisor) {
            this.dividend = dividend;
            this.divisor = divisor;
            var dividendBd = new BigDecimal(dividend);
            var divisorBd = new BigDecimal(divisor);
            BigDecimal quotient;
            
            try {
                quotient = dividendBd.divide(divisorBd, MATH_CONTEXT_WITHOUT_ROUNDING);
                quotientIsRounded = false;
            } catch (ArithmeticException ex) {
                quotient = dividendBd.divide(divisorBd, MATH_CONTEXT_WITH_ROUNDING);
                quotientIsRounded = true;
            }
            
            quotientString = quotient.toPlainString();
        }
        
        public long getDividend() {
            return dividend;
        }
        
        public long getDivisor() {
            return divisor;
        }
        
        @JsonProperty("quotient")
        public String getQuotientString() {
            return quotientString;
        }
        
        public boolean getQuotientIsRounded() {
            return quotientIsRounded;
        }
    }
}