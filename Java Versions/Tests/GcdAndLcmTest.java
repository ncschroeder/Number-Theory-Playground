import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.sectionclasses.outer.GcdAndLcm.*;

/**
 * Has tests for code in the GcdAndLcm class.
 */
class GcdAndLcmTest {
    static EuclideanIteration ei(int max, int min, int remainder) {
        return new EuclideanIteration(max, min, remainder);
    }
    
    @ParameterizedTest
    @MethodSource("getArgs")
    void testGetEuclideanIterations(int input1, int input2, List<EuclideanIteration> expectedIterations) {
        assertEquals(expectedIterations, getEuclideanIterations(input1, input2));
    }

    static Stream<Arguments> getArgs() {
        return Stream.of(
            arguments(10, 10, List.of(ei(10, 10, 0))),
            arguments(10, 5, List.of(ei(10, 5, 0))),
            arguments(6, 35, List.of(ei(35, 6, 5), ei(6, 5, 1), ei(5, 1, 0))),
            arguments(4_410, 2_100, List.of(ei(4_410, 2_100, 210), ei(2_100, 210, 0))),
            arguments(120, 4_235, List.of(ei(4_235, 120, 35), ei(120, 35, 15), ei(35, 15, 5), ei(15, 5, 0)))
        );
    }
}