import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.sectionclasses.outer.PrimeNumbers.getPrimes;

/**
 * Has tests for code in the PrimeNumbers class.
 */
class PrimeNumbersTest {
    @ParameterizedTest
    @MethodSource("getArgs")
    void testGetPrimes(long input, long[] expectedPrimes) {
        assertArrayEquals(expectedPrimes, getPrimes(input).toArray());
    }
    
    static Stream<Arguments> getArgs() {
        var first30Primes =
            new long[] {
                2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53,
                59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113
            };
        
        var first30PrimesAfter1000 =
            new long[] {
                1_009, 1_013, 1_019, 1_021, 1_031, 1_033, 1_039, 1_049, 1_051, 1_061, 1_063, 1_069,
                1_087, 1_091, 1_093, 1_097, 1_103, 1_109, 1_117, 1_123, 1_129, 1_151, 1_153, 1_163,
                1_171, 1_181, 1_187, 1_193, 1_201, 1_213
            };
        
        return Stream.of(
            arguments(0, first30Primes),
            arguments(first30PrimesAfter1000[0], first30PrimesAfter1000)
        );
    }
}