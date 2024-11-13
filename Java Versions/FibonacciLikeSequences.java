package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;

/**
 * Utility class related to Fibonacci-like sequences and the section for it.
 */
public class FibonacciLikeSequences {
    /**
     * Approximately 1.618.
     */
    private static final double PHI = (1 + Math.sqrt(5)) / 2;
    
    public static final String INFO = """
I consider a number sequence to be "Fibonacci-like" if it starts with 2 numbers and has every following number
be the sum of the 2 previous numbers. The Fibonacci sequence does this and has 1 and 1 as its first 2 numbers.
Fibonacci was a mathematician from the 1100s to 1200s from modern-day Italy. Another Fibonacci-like sequence
is the Lucas sequence, which has 2 and 1 as its first 2 numbers. This sequence was named after 1800s French
mathematician Francois Edouard Anatole Lucas.

The Golden Ratio is an irrational number symbolized by the Greek letter Phi. Phi = (1 + the square root of 5) / 2,
which is approximately %s. As we advance further and further into a Fibonacci-like sequence, the ratio between
a number and the number before it gets closer and closer to Phi. For example, the first 8 numbers of the
Fibonacci sequence are 1, 1, 2, 3, 5, 8, 13, and 21. 2 / 1 = 2. 8 / 5 = 1.6. 21 / 13 is approximately %s."""
        .formatted(PHI, (double) 21 / 13 /* ~1.615 */);
    
    private static final long MIN_INPUT = 1;
    private static final long MAX_INPUT = NINE_QUINTILLION;
    private static final int SEQUENCE_LENGTH = 20;
    
    public static final class Answer {
        private final String sequenceHeading;
        
        /**
         * Is as long as SEQUENCE_LENGTH and contains BigIntegers for numbers in a Fibonacci-like sequence.
         * The first 2 BigIntegers in this sequence are from 2 input longs.
         */
        public final List<BigInteger> bigIntSequence;
        
        private final Stream<String> stringSequence;
        
        /**
         * Contains a sentence about what Phi approximately is and sentences about the ratios between the 5th and 4th,
         * 10th and 9th, 15th and 14th, and 20th and 19th numbers in bigIntSequence.
         */
        private final Stream<String> phiAndRatioSentences;

        public Answer(long input1, long input2) {
            assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
            assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
            
            sequenceHeading =
                String.format(
                    "The first %d numbers in the Fibonacci-like sequence that begins with %s and %s are:",
                    SEQUENCE_LENGTH,
                    toStringWithCommas(input1),
                    toStringWithCommas(input2)
                );
        
            bigIntSequence = new ArrayList<>(SEQUENCE_LENGTH);
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
            
            stringSequence = bigIntSequence.stream().map(Misc::toStringWithCommas);
            
            Stream<String> ratioSentences =
                IntStream.iterate(3, i -> i <= SEQUENCE_LENGTH - 2, i -> i + 5)
                .mapToObj(i -> {
                    BigInteger bi1 = bigIntSequence.get(i);
                    BigInteger bi2 = bigIntSequence.get(i + 1);
                    
                    return String.format(
                        "%s / %s is approximately %s.",
                        toStringWithCommas(bi2),
                        toStringWithCommas(bi1),
                        new BigDecimal(bi2).divide(new BigDecimal(bi1), MathContext.DECIMAL64)
                    );
                });
            
            phiAndRatioSentences =
                Stream.concat(
                    Stream.of(String.format("Phi is approximately %s.", PHI)),
                    ratioSentences
                );
        }
    }
    
    public static final class Section extends DoubleInputSection {
        public Section() {
            super(
                "Fibonacci-Like Sequences",
                MIN_INPUT,
                MAX_INPUT,
                String.format(
                    "get the first %d numbers in the Fibonacci-like sequence that begins with those integers, " +
                        "as well as the ratios between some consecutive numbers in the sequence",
                    SEQUENCE_LENGTH
                ),
                "Fibonacci-like sequences",
                INFO
            );
        }
        
        @Override
        public String getCliAnswer(long input1, long input2) {
            var answer = new Answer(input1, input2);
            
            var phiAndRatioSentencesString =
                answer
                .phiAndRatioSentences
                .map(s -> NTPCLI.insertNewLines(s, true))
                .collect(Collectors.joining("\n"));
            
            return
                NTPCLI.streamToString(NTPCLI.insertNewLines(answer.sequenceHeading), answer.stringSequence) +
                "\n\n" + phiAndRatioSentencesString;
        }
    
        @Override
        public List<Component> getGuiComponents(long input1, long input2) {
            var answer = new Answer(input1, input2);
            List<Component> headingAndTextArea =
                NTPGUI.createStreamHeadingAndTextArea(answer.sequenceHeading, answer.stringSequence);
            
            return List.of(
                headingAndTextArea.get(0),
                headingAndTextArea.get(1),
                new NTPTextArea(answer.phiAndRatioSentences, "\n")
            );
        }
    }
}