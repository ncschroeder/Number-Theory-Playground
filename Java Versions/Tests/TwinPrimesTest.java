import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.nicholasschroeder.numbertheoryplayground.TwinPrimes.getTwinPrimePairStarts;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Has tests for code in the TwinPrimes class.
 */
class TwinPrimesTest {
    @ParameterizedTest
    @MethodSource("getArgs")
    void testGetTwinPrimePairStarts(int input, int[] expectedStarts) {
        assertArrayEquals(expectedStarts, getTwinPrimePairStarts(input).toArray());
    }
    
    static Stream<Arguments> getArgs() {
        int[] first20Starts =
            new int[] {
                3, 5, 11, 17, 29, 41, 59, 71, 101, 107, 137, 149, 179, 191, 197, 227, 239, 269, 281, 311
            };
        
        int[] first20StartsAfter1000 =
            new int[] {
                1_019, 1_031, 1_049, 1_061, 1_091, 1_151, 1_229, 1_277, 1_289, 1_301,
                1_319, 1_427, 1_451, 1_481, 1_487, 1_607, 1_619, 1_667, 1_697, 1_721
            };
        
        return Stream.of(
            arguments(0, first20Starts),
            arguments(1_000, first20StartsAfter1000)
        );
    }
}