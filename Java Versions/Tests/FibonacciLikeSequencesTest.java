import com.nicholasschroeder.numbertheoryplayground.FibonacciLikeSequences;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Has tests for code in the FibonacciLikeSequences class.
 */
class FibonacciLikeSequencesTest {
    @ParameterizedTest
    @MethodSource("getArgs")
    @DisplayName("Info objects generate correct sequences")
    void infoObjectSequences(int input1, int input2, List<Integer> expectedSequence) {
        List<Integer> actualSequence =
            new FibonacciLikeSequences.Info(input1, input2).getIntSequence();
        assertEquals(expectedSequence, actualSequence);
    }
    
    static Stream<Arguments> getArgs() {
        List<Integer> fibonacciSequenceStart =
            List.of(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1_597, 2_584, 4_181, 6_765);
    
        List<Integer> anotherSequence =
            List.of(
                304, 5, 309, 314, 623, 937, 1_560, 2_497, 4_057, 6_554, 10_611, 17_165,
                27_776, 44_941, 72_717, 117_658, 190_375, 308_033, 498_408, 806_441
            );
        
        return Stream.of(
            arguments(1, 1, fibonacciSequenceStart),
            arguments(304, 5, anotherSequence)
        );
    }
}