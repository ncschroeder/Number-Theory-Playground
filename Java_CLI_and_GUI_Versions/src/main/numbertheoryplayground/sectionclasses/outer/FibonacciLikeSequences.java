package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import numbertheoryplayground.Misc;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.DoubleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.gui.NtpGui.*;

/**
 * Utility class related to Fibonacci-like sequences and the section for it.
 */
public class FibonacciLikeSequences {
    /*
    BigIntegers are used to create sequences and BigDecimals must be used to find the ratio
    between BigIntegers. The MathContexts below are used as part of the division of BigDecimals.
    For consistency, MATH_CONTEXT_WITH_ROUNDING will also be used to calculate Phi and 21 / 13.
     */
    
    private static final MathContext MATH_CONTEXT_WITH_ROUNDING = MathContext.DECIMAL64;
    
    private static final MathContext MATH_CONTEXT_WITHOUT_ROUNDING =
        new MathContext(MATH_CONTEXT_WITH_ROUNDING.getPrecision(), RoundingMode.UNNECESSARY);
    
    /**
     * Phi = (1 + the square root of 5) / 2 ≈ 1.618.
     */
    public static final String PHI_STRING =
        BigDecimal.valueOf(5)
        .sqrt(MATH_CONTEXT_WITH_ROUNDING)
        .add(BigDecimal.ONE, MATH_CONTEXT_WITH_ROUNDING)
        .divide(BigDecimal.TWO, MATH_CONTEXT_WITH_ROUNDING)
        .toPlainString();
    
    /*
    I would use the Unicode char 𝚽 for Phi in INFO and the ratioAndPhiExpressions field in the
    Answer class, but Swing doesn't recognize that char.
     */
    
    private static final String INFO = """
I consider a number sequence to be "Fibonacci-like" if it starts with 2 numbers and has every
following number be the sum of the 2 previous numbers. The Fibonacci sequence does this and the
first 8 numbers of it are 1, 1, 2, 3, 5, 8, 13, and 21. Fibonacci was a mathematician from the
1100s to 1200s from modern-day Italy. Another Fibonacci-like sequence is the Lucas sequence and
the first 8 numbers of it are 2, 1, 3, 4, 7, 11, 18, and 29. This sequence was named after
1800s French mathematician Francois Edouard Anatole Lucas.

The Golden Ratio is an irrational number symbolized by the Greek letter Phi.
Phi = (1 + the square root of 5) / 2 ≈ %s. As we advance further and further into a
Fibonacci-like sequence, the ratio between a number and the number before it gets closer and
closer to Phi. For example, recall that the first 8 numbers of the Fibonacci sequence are
1, 1, 2, 3, 5, 8, 13, and 21. 2 / 1 = 2, 8 / 5 = 1.6, and 21 / 13 ≈ %s.

The ratios that get calculated by this section are floating-point numbers most of the time, so
these calculations are some of the few calculations done by the Number Theory Playground that
involve numbers other than natural numbers."""
        .formatted(
            PHI_STRING,
            BigDecimal.valueOf(21).divide(BigDecimal.valueOf(13), MATH_CONTEXT_WITH_ROUNDING) /* ~1.615 */
        );
    
    /*
    The calculations for this section are: find the first 20 numbers of the Fibonacci-like
    sequence that starts with 2 input numbers, as well as the ratios between the 5th and 4th,
    10th and 9th, 15th and 14th, and 20th and 19th numbers.
     */
    
    private static final long MIN_INPUT = 1;
    private static final long MAX_INPUT = NINE_QUINTILLION;
    private static final int SEQUENCE_LENGTH = 20;
    
    private static final class Answer {
        private final String sequenceHeading;

        private final Stream<String> stringSequence;

        /**
         * The first 4 elements are expressions about the ratios between the 5th and 4th,
         * 10th and 9th, 15th and 14th, and 20th and 19th numbers in bigIntSequence.
         * The 5th element is an expression about what Phi approximately is.
         */
        private final Stream<String> ratioAndPhiExpressions;

        private Answer(long input1Long, long input2Long, String input1String, String input2String) {
            List<BigInteger> bigIntSequence = getBigIntFiboLikeSequence(input1Long, input2Long);
            
            sequenceHeading =
                String.format(
                    "The first %d numbers in the Fibonacci-like sequence that starts with %s and %s are:",
                    SEQUENCE_LENGTH, input1String, input2String
                );

            stringSequence = bigIntSequence.stream().map(Misc::createStringWithCommas);

            Stream<String> ratioExpressions =
                IntStream.of(3, 8, 13, 18)
                .mapToObj(i -> getRatioExpression(bigIntSequence.get(i), bigIntSequence.get(i + 1)));
            
            ratioAndPhiExpressions =
                Stream.concat(
                    ratioExpressions,
                    Stream.of("Phi ≈ " + PHI_STRING)
                );
        }
    }
    
    /**
     * Returns a list of BigIntegers of the first 20 numbers in the Fibonacci-like sequence
     * that starts with input1 and input2. BigIntegers are used since some numbers in the
     * list might be too big for a long.
     */
    static List<BigInteger> getBigIntFiboLikeSequence(long input1, long input2) {
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
    
    static String getRatioExpression(BigInteger bigInt1, BigInteger bigInt2) {
        var bigDecimal1 = new BigDecimal(bigInt1);
        var bigDecimal2 = new BigDecimal(bigInt2);
        BigDecimal ratio;
        char equalityChar;
        
        try {
            ratio = bigDecimal2.divide(bigDecimal1, MATH_CONTEXT_WITHOUT_ROUNDING);
            equalityChar = '=';
        } catch (ArithmeticException ex) {
            ratio = bigDecimal2.divide(bigDecimal1, MATH_CONTEXT_WITH_ROUNDING);
            equalityChar = '≈';
        }
        
        return String.format(
            "%s / %s %s %s",
            createStringWithCommas(bigInt2),
            createStringWithCommas(bigInt1),
            equalityChar,
            ratio
        );
    }
    
    private static final String RATIOS_HEADING = """
The ratios between the 5th and 4th, 10th and 9th, 15th and 14th, and 20th and 19th numbers are:""";
    
    
    public static final class Section extends DoubleInputSection {
        public Section() {
            super(
                "Fibonacci-Like Sequences",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                String.format(
                    "the first %d numbers in the Fibonacci-like sequence that starts with those numbers, " +
                        "as well as the ratios between some consecutive numbers in that sequence",
                    SEQUENCE_LENGTH
                ),
                "Fibonacci-like sequences"
            );
        }
        
        @Override
        public String getCliAnswer(
            long input1Long, long input2Long,
            String input1String, String input2String
        ) {
            var answer = new Answer(input1Long, input2Long, input1String, input2String);
            
            String sequenceHeading = NtpCli.putNewLineChars(answer.sequenceHeading);
            var sequenceString =
                NtpCli.buildStringWithStreamElementsOnShortLines(sequenceHeading, answer.stringSequence);
            
            var ratioAndPhiExpressionsString =
                NtpCli.buildStringWithStreamElementsOnSeparateLines(RATIOS_HEADING, answer.ratioAndPhiExpressions);
            
            return sequenceString + "\n\n" + ratioAndPhiExpressionsString;
        }
        
        @Override
        public List<Component> getGuiComponents(
            long input1Long, long input2Long,
            String input1String, String input2String
        ) {
            var answer = new Answer(input1Long, input2Long, input1String, input2String);
            
            return List.of(
                createListHeadingLabel(answer.sequenceHeading),
                NtpTextArea.createNarrowOneWithStreamElements(answer.stringSequence),
                createGapBetweenAnswerSections(),
                createListHeadingLabel(RATIOS_HEADING),
                NtpTextArea.createWithStreamElementsOnSeparateLines(answer.ratioAndPhiExpressions)
            );
        }
    }
}