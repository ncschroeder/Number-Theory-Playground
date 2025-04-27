package com.numbertheoryplayground.calculationsimpl;

import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;

public class FibonacciLikeSequencesAnswer {
    private static final long MIN_INPUT = 1;
    private static final long MAX_INPUT = NINE_QUADRILLION;
    
    private static final int SEQUENCE_LENGTH = 20;
    
    private final List<String> stringSequence;
    private final List<RatioData> ratioDataList;
    
    public FibonacciLikeSequencesAnswer(long input1, long input2) {
        List<BigInteger> bigIntSequence = getBigIntSequence(input1, input2);
        
        stringSequence =
            bigIntSequence
            .stream()
            .map(BigInteger::toString)
            .toList();
        
        ratioDataList =
            IntStream.of(3, 8, 13, 18)
            .mapToObj(i -> new RatioData(bigIntSequence.get(i), bigIntSequence.get(i + 1)))
            .toList();
    }
    
    @JsonProperty("sequence")
    public List<String> getStringSequence() {
        return stringSequence;
    }
    
    @JsonProperty("ratioDataArray")
    public List<RatioData> getRatioDataList() {
        return ratioDataList;
    }
    
    
    static List<BigInteger> getBigIntSequence(long input1, long input2) {
        assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
        assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
        
        var sequence = new ArrayList<BigInteger>(SEQUENCE_LENGTH);
        var bigInt1 = BigInteger.valueOf(input1);
        var bigInt2 = BigInteger.valueOf(input2);
        sequence.add(bigInt1);
        sequence.add(bigInt2);
        
        while (sequence.size() < SEQUENCE_LENGTH) {
            var nextBigInt = bigInt1.add(bigInt2);
            sequence.add(nextBigInt);
            bigInt1 = bigInt2;
            bigInt2 = nextBigInt;
        }
        
        return sequence;
    }
    
    public static final class RatioData {
        private static final MathContext noRoundingMathContext =
            new MathContext(MathContext.DECIMAL64.getPrecision(), RoundingMode.UNNECESSARY);
        
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
                ratio = bigDecimal2.divide(bigDecimal1, noRoundingMathContext);
                isRounded = false;
            } catch (ArithmeticException ex) {
                ratio = bigDecimal2.divide(bigDecimal1, MathContext.DECIMAL64);
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
        
        @JsonProperty("isRounded")
        public boolean isRounded() {
            return isRounded;
        }
    }
}
