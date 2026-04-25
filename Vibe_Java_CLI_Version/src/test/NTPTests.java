import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class NTPTests {
    @ParameterizedTest
    @MethodSource("wrappedCases")
    void wrapped(Stream<String> pieces, String expected) {
        assertEquals(expected, NTP.wrapped(pieces));
    }
    
    static Stream<Arguments> wrappedCases() {
        String piece = "abcdefghij";
        String line = String.join("    ", piece, piece, piece, piece, piece);
        return Stream.of(
            arguments(Stream.of(), ""),
            arguments(Stream.of("abc"), "abc"),
            arguments(Stream.of("1", "2", "3"), "1    2    3"),
            arguments(Stream.of(piece, piece, piece, piece, piece), line),
            arguments(
                Stream.of(piece, piece, piece, piece, piece, piece, piece, piece, piece, piece),
                line + "\n" + line
            )
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("findPrimesFromCases")
    void findPrimesFrom(long start, long[] expected) {
        assertArrayEquals(expected, NTP.findPrimesFrom(start).toArray());
    }

    static Stream<Arguments> findPrimesFromCases() {
        return Stream.of(
            arguments(
                0,
                new long[]{
                    2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47,
                    53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113
                }
            ),
            arguments(
                1_000,
                new long[]{
                    1_009, 1_013, 1_019, 1_021, 1_031, 1_033, 1_039, 1_049, 1_051, 1_061,
                    1_063, 1_069, 1_087, 1_091, 1_093, 1_097, 1_103, 1_109, 1_117, 1_123,
                    1_129, 1_151, 1_153, 1_163, 1_171, 1_181, 1_187, 1_193, 1_201, 1_213
                }
            )
        );
    }

    
    @ParameterizedTest
    @MethodSource("findSemiprimesFromCases")
    void findSemiprimesFrom(long start, List<NTP.SemiprimeData> expected) {
        assertEquals(expected, NTP.findSemiprimesFrom(start).toList());
    }
    
    private static NTP.SemiprimeData sd(long semiprime, long factor1, long factor2) {
        return new NTP.SemiprimeData(semiprime, factor1, factor2);
    }

    static Stream<Arguments> findSemiprimesFromCases() {
        return Stream.of(
            arguments(
                0,
                List.of(
                    sd(4, 2, 2),  sd(6, 2, 3),  sd(9, 3, 3),  sd(10, 2, 5),
                    sd(14, 2, 7), sd(15, 3, 5), sd(21, 3, 7), sd(22, 2, 11),
                    sd(25, 5, 5), sd(26, 2, 13), sd(33, 3, 11), sd(34, 2, 17),
                    sd(35, 5, 7), sd(38, 2, 19), sd(39, 3, 13), sd(46, 2, 23),
                    sd(49, 7, 7), sd(51, 3, 17), sd(55, 5, 11), sd(57, 3, 19)
                )
            ),
            arguments(
                1_000,
                List.of(
                    sd(1_003, 17, 59), sd(1_006, 2, 503), sd(1_007, 19, 53),
                    sd(1_011, 3, 337), sd(1_018, 2, 509), sd(1_027, 13, 79),
                    sd(1_037, 17, 61), sd(1_041, 3, 347), sd(1_042, 2, 521),
                    sd(1_043, 7, 149), sd(1_046, 2, 523), sd(1_047, 3, 349),
                    sd(1_055, 5, 211), sd(1_057, 7, 151), sd(1_059, 3, 353),
                    sd(1_067, 11, 97), sd(1_073, 29, 37), sd(1_077, 3, 359),
                    sd(1_079, 13, 83), sd(1_081, 23, 47)
                )
            )
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("findTwinPrimesFromCases")
    void findTwinPrimesFrom(long start, long[] expected) {
        assertArrayEquals(expected, NTP.findTwinPrimesFrom(start).toArray());
    }
    
    static Stream<Arguments> findTwinPrimesFromCases() {
        return Stream.of(
            arguments(
                0,
                new long[] {
                    3, 5, 11, 17, 29, 41, 59, 71, 101, 107,
                    137, 149, 179, 191, 197, 227, 239, 269, 281, 311
                }
            ),
            arguments(
                1_000,
                new long[] {
                    1_019, 1_031, 1_049, 1_061, 1_091, 1_151, 1_229, 1_277, 1_289, 1_301,
                    1_319, 1_427, 1_451, 1_481, 1_487, 1_607, 1_619, 1_667, 1_697, 1_721
                }
            )
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("euclideanAlgorithmCases")
    void euclideanAlgorithm(long a, long b, List<NTP.EuclideanStep> expected) {
        assertEquals(expected, NTP.euclideanAlgorithm(a, b));
    }

    static Stream<Arguments> euclideanAlgorithmCases() {
        return Stream.of(
            arguments(
                48,
                18,
                List.of(
                    new NTP.EuclideanStep(48, 18, 12),
                    new NTP.EuclideanStep(18, 12, 6),
                    new NTP.EuclideanStep(12, 6, 0)
                )
            ),
            arguments(
                17,
                13,
                List.of(
                    new NTP.EuclideanStep(17, 13, 4),
                    new NTP.EuclideanStep(13, 4, 1),
                    new NTP.EuclideanStep(4, 1, 0)
                )
            ),
            arguments(
                100,
                25,
                List.of(new NTP.EuclideanStep(100, 25, 0))
            )
        );
    }

    @Test
    void euclideanAlgorithmSwappedInputs() {
        assertEquals(NTP.euclideanAlgorithm(48, 18), NTP.euclideanAlgorithm(18, 48));
    }
    
    
    @ParameterizedTest
    @MethodSource("gcdAndLcmFactorsCases")
    void gcdAndLcmFactorsParameterized(long a, long b, List<NTP.FactorAndPower> expectedGcd, List<NTP.FactorAndPower> expectedLcm) {
        NTP.GcdLcm result = NTP.gcdAndLcmFactors(NTP.primeFactorsAndPowersOf(a), NTP.primeFactorsAndPowersOf(b));
        assertEquals(expectedGcd, result.gcd());
        assertEquals(expectedLcm, result.lcm());
    }
    
    private static NTP.FactorAndPower fp(long factor, int power) {
        return new NTP.FactorAndPower(factor, power);
    }
    
    static Stream<Arguments> gcdAndLcmFactorsCases() {
        return Stream.of(
            arguments(
                48,
                18,
                List.of(fp(2, 1), fp(3, 1)),
                List.of(fp(2, 4), fp(3, 2))
            ),
            arguments(
                60,
                126,
                List.of(fp(2, 1), fp(3, 1)),
                List.of(fp(2, 2), fp(3, 2), fp(5, 1), fp(7, 1))),
            arguments(17L, 13L, List.of(), List.of(fp(13, 1), fp(17, 1)))
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("goldbachPairsCases")
    void goldbachPairs(long n, List<Long> expected) {
        assertEquals(expected, NTP.goldbachPairs(n));
    }
    
    static Stream<Arguments> goldbachPairsCases() {
        return Stream.of(
            arguments(4L, List.of(2L)),
            arguments(10L, List.of(3L, 5L)),
            arguments(100L, List.of(3L, 11L, 17L, 29L, 41L, 47L))
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("firstPythagoreanTriplesCases")
    void firstPythagoreanTriples(long minA, List<NTP.PythagoreanTriple> expected) {
        assertEquals(expected, NTP.firstPythagoreanTriples(minA));
    }
    
    private static NTP.PythagoreanTriple pt(long a, long b, long c) {
        return new NTP.PythagoreanTriple(a, b, c);
    }

    static Stream<Arguments> firstPythagoreanTriplesCases() {
        return Stream.of(
            arguments(
                0L,
                List.of(
                    pt(3, 4, 5),    pt(5, 12, 13),  pt(6, 8, 10),
                    pt(7, 24, 25),  pt(8, 15, 17),  pt(9, 12, 15),
                    pt(9, 40, 41),  pt(10, 24, 26), pt(11, 60, 61),
                    pt(12, 16, 20)
                )
            ),
            arguments(
                30L,
                List.of(
                    pt(30, 40, 50),   pt(30, 72, 78),   pt(30, 224, 226),
                    pt(31, 480, 481), pt(32, 60, 68),   pt(32, 126, 130),
                    pt(32, 255, 257), pt(33, 44, 55),   pt(33, 56, 65),
                    pt(33, 180, 183)
                )
            )
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("firstPythagoreanPrimeFromCases")
    void firstPythagoreanPrimeFromParameterized(long start, NTP.TwoSquareData expected) {
        assertEquals(expected, NTP.firstPythagoreanPrimeFrom(start));
    }

    static Stream<Arguments> firstPythagoreanPrimeFromCases() {
        return Stream.of(
            arguments(0L,   new NTP.TwoSquareData(5, 1, 2)),
            arguments(13L,  new NTP.TwoSquareData(13, 2, 3)),
            arguments(14L,  new NTP.TwoSquareData(17, 1, 4)),
            arguments(100L, new NTP.TwoSquareData(101, 1, 10)),
            arguments(50L,  new NTP.TwoSquareData(53, 2, 7))
        );
    }

    
    @ParameterizedTest
    @MethodSource("fibonacciLikeSequenceCases")
    void fibonacciLikeSequenceParameterized(long a, long b, List<BigInteger> expected) {
        assertEquals(expected, NTP.fibonacciLikeSequence(a, b));
    }
    
    private static List<BigInteger> bigs(int... values) {
        return Arrays.stream(values).mapToObj(BigInteger::valueOf).toList();
    }

    static Stream<Arguments> fibonacciLikeSequenceCases() {
        return Stream.of(
            arguments(
                1,
                1,
                bigs(
                    1, 1, 2, 3, 5, 8, 13, 21, 34, 55,
                    89, 144, 233, 377, 610, 987, 1_597, 2_584, 4_181, 6_765
                )
            ),
            arguments(
                304,
                5,
                bigs(
                    304, 5, 309, 314, 623, 937, 1_560, 2_497, 4_057, 6_554,
                    10_611, 17_165, 27_776, 44_941, 72_717, 117_658, 190_375, 308_033, 498_408, 806_441
                )
            )
        );
    }

    
    @ParameterizedTest
    @MethodSource("computeRatioExactnessCases")
    void computeRatioExactness(long numerator, long denominator, boolean expectedExact) {
        NTP.Ratio ratio = new NTP.Ratio(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
        assertEquals(expectedExact, ratio.exact());
    }

    static Stream<Arguments> computeRatioExactnessCases() {
        return Stream.of(
            arguments(16L, 10L, true),   // 1.6
            arguments(10L, 4L, true),    // 2.5
            arguments(10L, 5L, true),    // 2
            arguments(5L, 3L, false),    // non-terminating
            arguments(55L, 34L, false),  // denominator has factor 17
            arguments(623L, 314L, false) // denominator has factor 157
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("powersOfTwoSummingCases")
    void powersOfTwoSummingParameterized(long a, long b, List<NTP.PowerAndMultiple> expected) {
        assertEquals(expected, NTP.powersOfTwoSumming(a, b));
    }
    
    private static NTP.PowerAndMultiple pm(long power, long multiple) {
        return new NTP.PowerAndMultiple(power, BigInteger.valueOf(multiple));
    }
    
    static Stream<Arguments> powersOfTwoSummingCases() {
        return Stream.of(
            arguments(13L, 12L, List.of(pm(1, 12), pm(4, 48), pm(8, 96))),
            arguments(2L,   2L, List.of(pm(2, 4))),
            arguments(16L,  3L, List.of(pm(16, 48))),
            arguments(7L,   5L, List.of(pm(1, 5), pm(2, 10), pm(4, 20))),
            arguments(15L,  1L, List.of(pm(1, 1), pm(2, 2), pm(4, 4), pm(8, 8)))
        );
    }
}