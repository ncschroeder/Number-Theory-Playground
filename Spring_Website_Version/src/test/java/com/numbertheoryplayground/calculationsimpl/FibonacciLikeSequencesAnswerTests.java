package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciLikeSequencesAnswerTests {
    @ParameterizedTest
    @MethodSource("getSequences")
    void fibonacciLikeSequencesAnswerIntSequence(List<Integer> expectedSequence) {
        List<Integer> actualSequence =
            new FibonacciLikeSequencesAnswer(expectedSequence.get(0), expectedSequence.get(1))
            .getIntSequence();
        assertEquals(expectedSequence, actualSequence);
    }
    
    static Stream<List<Integer>> getSequences() {
        List<Integer> fibonacciSequenceStart =
            List.of(
                1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1_597, 2_584,
                4_181, 6_765
            );
        
        List<Integer> anotherSequence =
            List.of(
                304, 5, 309, 314, 623, 937, 1_560, 2_497, 4_057, 6_554, 10_611, 17_165,
                27_776, 44_941, 72_717, 117_658, 190_375, 308_033, 498_408, 806_441
            );
        
        return Stream.of(fibonacciSequenceStart, anotherSequence);
    }
}