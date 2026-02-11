package com.numbertheoryplayground.calculationsimpl;

import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;

public final class FibonacciLikeSequencesAnswer {
    private static final long MIN_INPUT = 1;
    private static final long MAX_INPUT = ONE_QUADRILLION;
    
    /**
     * Contains strings of the numbers in the Fibonacci-like sequence that gets created.
     * Strings are used since these numbers might be too big for a JavaScript safe integer.
     */
    private final List<String> stringFiboLikeSequence;
    
    /**
     * Contains RatioDatas for the 4th and 5th, 9th and 10th, 14th and 15th, and
     * 19th and 20th numbers in the Fibonacci-like sequence that gets created.
     */
    private final List<RatioData> ratiosData;
    
    public FibonacciLikeSequencesAnswer(long input1, long input2) {
        List<BigInteger> bigIntFiboLikeSequence = getBigIntFiboLikeSequence(input1, input2);
        
        stringFiboLikeSequence =
            bigIntFiboLikeSequence
            .stream()
            .map(BigInteger::toString)
            .toList();
        
        ratiosData =
            IntStream.of(3, 8, 13, 18)
            .mapToObj(i ->
                new RatioData(bigIntFiboLikeSequence.get(i), bigIntFiboLikeSequence.get(i + 1))
            )
            .toList();
    }
    
    @JsonProperty("fiboLikeSequence")
    public List<String> getStringFiboLikeSequence() {
        return stringFiboLikeSequence;
    }
    
    public List<RatioData> getRatiosData() {
        return ratiosData;
    }
    
    
    /**
     * Returns a list of BigIntegers of the first 20 numbers in the Fibonacci-like sequence
     * that starts with input1 and input2. BigIntegers are used since some numbers in the
     * list might be too big for a long.
     */
    static List<BigInteger> getBigIntFiboLikeSequence(long input1, long input2) {
        assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
        assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
        
        final int sequenceLength = 20;
        var sequence = new ArrayList<BigInteger>(sequenceLength);
        var bigInt1 = BigInteger.valueOf(input1);
        var bigInt2 = BigInteger.valueOf(input2);
        sequence.add(bigInt1);
        sequence.add(bigInt2);
        
        while (sequence.size() < sequenceLength) {
            var nextBigInt = bigInt1.add(bigInt2);
            sequence.add(nextBigInt);
            bigInt1 = bigInt2;
            bigInt2 = nextBigInt;
        }
        
        return sequence;
    }
    
    public static final class RatioData {
        private static final MathContext MATH_CONTEXT_WITH_ROUNDING = MathContext.DECIMAL64;
        
        private static final MathContext MATH_CONTEXT_WITHOUT_ROUNDING =
            new MathContext(MATH_CONTEXT_WITH_ROUNDING.getPrecision(), RoundingMode.UNNECESSARY);
        
        /*
        Strings are used for the 2 big ints since they might be too big for
        JavaScript safe integers.
         */
        
        private final String bigInt1String;
        
        private final String bigInt2String;
        
        private BigDecimal ratio;
        
        private boolean isRounded;
        
        RatioData(BigInteger bigInt1, BigInteger bigInt2) {
            bigInt1String = bigInt1.toString();
            bigInt2String = bigInt2.toString();
            var bigDecimal1 = new BigDecimal(bigInt1);
            var bigDecimal2 = new BigDecimal(bigInt2);
            
            try {
                ratio = bigDecimal2.divide(bigDecimal1, MATH_CONTEXT_WITHOUT_ROUNDING);
                isRounded = false;
            } catch (ArithmeticException ex) {
                ratio = bigDecimal2.divide(bigDecimal1, MATH_CONTEXT_WITH_ROUNDING);
                isRounded = true;
            }
        }
        
        @JsonProperty("num1String")
        public String getBigInt1String() {
            return bigInt1String;
        }
        
        @JsonProperty("num2String")
        public String getBigInt2String() {
            return bigInt2String;
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