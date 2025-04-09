package com.numbertheoryplayground.calculationsimpl;

import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FibonacciLikeSequencesAnswer {
    private static final int SEQUENCE_LENGTH = 20;
    
    private final List<BigInteger> bigIntSequence = new ArrayList<>(SEQUENCE_LENGTH);
    
    public final List<String> stringSequence;
    
    @JsonProperty("ratioDataArray")
    public final List<RatioData> ratioDataList;
    
    public FibonacciLikeSequencesAnswer(long input1, long input2) {
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
        public final String num1String;
        public final String num2String;
        public final double ratio;
        
        private RatioData(BigInteger bigInt1, BigInteger bigInt2) {
            num1String = bigInt1.toString();
            num2String = bigInt2.toString();
            ratio =
                new BigDecimal(bigInt2)
                .divide(new BigDecimal(bigInt1), MathContext.DECIMAL64)
                .doubleValue();
        }
    }
}
