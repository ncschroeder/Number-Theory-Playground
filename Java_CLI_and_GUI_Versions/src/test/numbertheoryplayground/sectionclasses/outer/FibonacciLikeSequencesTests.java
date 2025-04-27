package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static numbertheoryplayground.sectionclasses.outer.FibonacciLikeSequences.*;

/**
 * Has tests for code in the FibonacciLikeSequences class.
 */
class FibonacciLikeSequencesTests {
    @ParameterizedTest
    @MethodSource("getSequenceStreams")
    void testGetBigIntSequence(IntStream expectedSequenceStream) {
        List<BigInteger> expectedSequence =
            expectedSequenceStream
            .mapToObj(BigInteger::valueOf)
            .toList();
        
        List<BigInteger> actualSequence =
            getBigIntSequence(
                expectedSequence.get(0).longValueExact(),
                expectedSequence.get(1).longValueExact()
            );

        assertEquals(expectedSequence, actualSequence);
    }

    static Stream<IntStream> getSequenceStreams() {
        var fibonacciSequenceStart =
            IntStream.of(
                1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233,
                377, 610,987, 1_597, 2_584, 4_181, 6_765
            );

        var anotherSequence =
            IntStream.of(
                304, 5, 309, 314, 623, 937, 1_560, 2_497, 4_057, 6_554, 10_611, 17_165,
                27_776, 44_941, 72_717, 117_658, 190_375, 308_033, 498_408, 806_441
            );

        return Stream.of(fibonacciSequenceStart, anotherSequence);
    }
    
    
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        INPUT_1,  INPUT_2,  EXPECTED_EXPRESSION
           3,        4,     4 / 3 ≈ 1.333333333333333
           4,        5,     5 / 4 = 1.25
        """)
    void testGetRatioExpression(BigInteger input1, BigInteger input2, String expectedExpression) {
        assertEquals(expectedExpression, getRatioExpression(input1, input2));
    }
}