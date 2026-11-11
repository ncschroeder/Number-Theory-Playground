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
    Answer class; but Swing, the GUI library I'm using, doesn't recognize that char. A black
    square gets displayed instead of that char.
     */
    
    private static final String INFO = """
I consider a number sequence to be "Fibonacci-like" if it starts with 2 numbers and has every
following number be the sum of the 2 previous numbers. The Fibonacci sequence does this and the
first 8 numbers of it are 1, 1, 2, 3, 5, 8, 13, and 21. Fibonacci was a mathematician from the
1100s to 1200s from modern-day Italy. Another Fibonacci-like sequence is the Lucas sequence and
the first 8 numbers of it are 2, 1, 3, 4, 7, 11, 18, and 29. This sequence was named after
1800s French mathematician François Édouard Anatole Lucas.

The Golden Ratio is an irrational number symbolized by the Greek letter Phi.
Phi = (1 + the square root of 5) / 2 ≈ %s. As we advance further and further into a Fibonacci-like
sequence, the result of dividing a number by the number before it gets closer and closer to Phi.
For example, recall that the first 8 numbers of the Fibonacci sequence are
1, 1, 2, 3, 5, 8, 13, and 21. 2 / 1 = 2, 8 / 5 = 1.6, and 21 / 13 ≈ %s.

The division results of the calculations for this section usually can't be expressed as integers,
so those calculations are the only ones done by the Number Theory Playground that involve numbers
other than integers."""
        .formatted(
            PHI_STRING,
            BigDecimal.valueOf(21).divide(BigDecimal.valueOf(13), MATH_CONTEXT_WITH_ROUNDING) /* ~1.615 */
        );
    
    /*
    The calculations for this section are: find the first 20 numbers of the Fibonacci-like sequence
    that starts with 2 input numbers, as well as the results of dividing the 5th number in that
    sequence by the 4th, the 10th by the 9th, the 15th by the 14th, and the 20th by the 19th.
     */
    
    private static final long MIN_INPUT = 1;
    private static final long MAX_INPUT = NINE_QUINTILLION;
    private static final int SEQUENCE_LENGTH = 20;
    
    private static final class Answer {
        private final String sequenceHeading;

        private final Stream<String> stringSequence;

        /**
         * The first 4 elements in this stream are expressions of the division of consecutive numbers
         * in the Fibonacci-like sequence that gets created, namely the 5th number divided by the 4th,
         * the 10th by the 9th, the 15th by the 14th, and the 20th by the 19th. The 5th element in
         * this stream is an expression about what Phi approximately is.
         */
        private final Stream<String> divisionAndPhiExpressions;

        private Answer(long input1Long, long input2Long, String input1String, String input2String) {
            List<BigInteger> bigIntSequence = getBigIntFiboLikeSequence(input1Long, input2Long);
            stringSequence = bigIntSequence.stream().map(Misc::createStringWithCommas);
            
            sequenceHeading =
                String.format(
                    "The first %d numbers in the Fibonacci-like sequence that starts with %s and %s are:",
                    SEQUENCE_LENGTH, input1String, input2String
                );
            
            Stream<String> divisionExpressions =
                IntStream.of(4, 9, 14, 19)
                .mapToObj(i -> getDivisionExpression(bigIntSequence.get(i), bigIntSequence.get(i - 1)));
            
            divisionAndPhiExpressions =
                Stream.concat(divisionExpressions, Stream.of("Phi ≈ " + PHI_STRING));
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
    
    /**
     * Returns an expression of the division of 2 BigIntegers.
     */
    static String getDivisionExpression(BigInteger dividend, BigInteger divisor) {
        var dividendBd = new BigDecimal(dividend);
        var divisorBd = new BigDecimal(divisor);
        BigDecimal quotient;
        char equalityChar;
        
        try {
            quotient = dividendBd.divide(divisorBd, MATH_CONTEXT_WITHOUT_ROUNDING);
            equalityChar = '=';
        } catch (ArithmeticException ex) {
            quotient = dividendBd.divide(divisorBd, MATH_CONTEXT_WITH_ROUNDING);
            equalityChar = '≈';
        }
        
        return String.format(
            "%s / %s %c %s",
            createStringWithCommas(dividend),
            createStringWithCommas(divisor),
            equalityChar,
            quotient
        );
    }
    
    private static final String DIVISION_RESULTS_HEADING = "The division results are:";
    
    
    public static final class Section extends DoubleInputSection {
        public Section() {
            super(
                "Fibonacci-Like Sequences",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                String.format(
                    "the first %d numbers in the Fibonacci-like sequence that starts with those numbers, " +
                        "as well as the results of dividing the 5th number in that sequence by the 4th, " +
                        "the 10th by the 9th, the 15th by the 14th, and the 20th by the 19th.",
                    SEQUENCE_LENGTH
                ),
                "Fibonacci-like sequences"
            );
        }
        
        @Override
        public String getCliAnswer(
            long input1Long,
            long input2Long,
            String input1String,
            String input2String
        ) {
            var answer = new Answer(input1Long, input2Long, input1String, input2String);
            String sequenceHeading = NtpCli.putNewLineChars(answer.sequenceHeading);
            var sequenceString =
                NtpCli.buildStringWithStreamElementsOnShortLines(sequenceHeading, answer.stringSequence);
            var divisionAndPhiExpressionsString =
                NtpCli.buildStringWithStreamElementsOnSeparateLines(DIVISION_RESULTS_HEADING, answer.divisionAndPhiExpressions);
            return sequenceString + "\n\n" + divisionAndPhiExpressionsString;
        }
        
        @Override
        public List<Component> getGuiComponents(
            long input1Long,
            long input2Long,
            String input1String,
            String input2String
        ) {
            var answer = new Answer(input1Long, input2Long, input1String, input2String);
            return List.of(
                createListHeadingLabel(answer.sequenceHeading),
                NtpTextArea.createNarrowOneWithStreamElements(answer.stringSequence),
                createGapBetweenAnswerSections(),
                createListHeadingLabel(DIVISION_RESULTS_HEADING),
                NtpTextArea.createWithStreamElementsOnSeparateLines(answer.divisionAndPhiExpressions)
            );
        }
    }
}