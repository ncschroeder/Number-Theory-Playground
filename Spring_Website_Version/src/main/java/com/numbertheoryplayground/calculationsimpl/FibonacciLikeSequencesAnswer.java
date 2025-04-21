package com.numbertheoryplayground.calculationsimpl;

import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;

public class FibonacciLikeSequencesAnswer {
    private static final long MIN_INPUT = 1;
    private static final long MAX_INPUT = NINE_QUADRILLION;
    
    private static final int SEQUENCE_LENGTH = 20;
    
    private final List<BigInteger> bigIntSequence = new ArrayList<>(SEQUENCE_LENGTH);
    
    public final List<String> stringSequence;
    
    @JsonProperty("ratioDataArray")
    public final List<RatioData> ratioDataList;
    
    public FibonacciLikeSequencesAnswer(long input1, long input2) {
        assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
        assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
        
        var bigInt1 = BigInteger.valueOf(input1);
        var bigInt2 = BigInteger.valueOf(input2);
        bigIntSequence.add(bigInt1);
        bigIntSequence.add(bigInt2);
        
        while (bigIntSequence.size() < SEQUENCE_LENGTH) {
            var nextBigInt = bigInt1.add(bigInt2);
            bigIntSequence.add(nextBigInt);
            bigInt1 = bigInt2;
            bigInt2 = nextBigInt;
        }
        
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
    
    @JsonIgnore
    public List<Integer> getIntSequence() {
        return
            bigIntSequence
            .stream()
            .map(BigInteger::intValueExact)
            .toList();
    }
    
    public static class RatioData {
        private static final MathContext noRoundingMathContext =
            new MathContext(MathContext.DECIMAL64.getPrecision(), RoundingMode.UNNECESSARY);
        
        @JsonProperty("num1String")
        public final String bigInt1String;
        
        @JsonProperty("num2String")
        public final String bigInt2String;
        
        public BigDecimal ratio;
        
        public boolean isRounded;
        
        public RatioData(BigInteger bigInt1, BigInteger bigInt2) {
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
    }
}
