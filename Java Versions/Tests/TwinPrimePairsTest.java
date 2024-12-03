import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.sectionclasses.outer.TwinPrimePairs.getPairStarts;

/**
 * Has tests for code in the TwinPrimePairs class.
 */
class TwinPrimePairsTest {
    @ParameterizedTest
    @MethodSource("getArgs")
    void testGetPairStarts(long input, long[] expectedPairStarts) {
        assertArrayEquals(expectedPairStarts, getPairStarts(input).toArray());
    }
    
    static Stream<Arguments> getArgs() {
        var first20PairStarts =
            new long[] {
                3, 5, 11, 17, 29, 41, 59, 71, 101, 107, 137, 149, 179, 191, 197, 227, 239, 269, 281, 311
            };
        
        var first20PairStartsAfter1000 =
            new long[] {
                1_019, 1_031, 1_049, 1_061, 1_091, 1_151, 1_229, 1_277, 1_289, 1_301,
                1_319, 1_427, 1_451, 1_481, 1_487, 1_607, 1_619, 1_667, 1_697, 1_721
            };
        
        return Stream.of(
            arguments(0, first20PairStarts),
            arguments(1_000, first20PairStartsAfter1000)
        );
    }
}