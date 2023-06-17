import com.nicholasschroeder.numbertheoryplayground.PrimeFactorization;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Primes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Has tests for code in the Primes class, as well as the isForAPrimeNumber method for
 * PrimeFactorizations.
 */
class PrimesTest {
    static int[] first30Primes =
        new int[] {
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53,
            59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113
        };
    
    static int[] first30PrimesAfter1000 =
        new int[] {
            1_009, 1_013, 1_019, 1_021, 1_031, 1_033, 1_039, 1_049, 1_051, 1_061, 1_063, 1_069,
            1_087, 1_091, 1_093, 1_097, 1_103, 1_109, 1_117, 1_123, 1_129, 1_151, 1_153, 1_163,
            1_171, 1_181, 1_187, 1_193, 1_201, 1_213
        };
    
    
    @ParameterizedTest
    @MethodSource("getPrimesArgs")
    void testGetPrimes(int input, int[] primes) {
        assertArrayEquals(primes, getPrimes(input).toArray());
    }
    
    static Stream<Arguments> getPrimesArgs() {
        return Stream.of(
            arguments(0, first30Primes),
            // Test even and odd int to test both code paths. 1,009 will be used as the odd one
            // since it's the 1st prime after 1,000.
            arguments(1_000, first30PrimesAfter1000),
            arguments(1_009, first30PrimesAfter1000)
        );
    }
    

    /**
     * The PrimeFactorizationTest class does a couple of tests with prime numbers but this test method is
     * an easy way to add more rigor to that since we have 2 lists of primes in this class that we can use.
     */
    @ParameterizedTest
    @MethodSource("getPrimesArrays")
    void primeFactorizationIsForAPrimeNumber(int[] primes) {
        int[] primesFoundUsingPfs =
            IntStream.rangeClosed(primes[0], primes[primes.length - 1])
            .filter(i -> new PrimeFactorization(i).isForAPrimeNumber())
            .toArray();
        
        assertArrayEquals(primes, primesFoundUsingPfs);
    }
    
    static Stream<int[]> getPrimesArrays() {
        return Stream.of(first30Primes, first30PrimesAfter1000);
    }
}