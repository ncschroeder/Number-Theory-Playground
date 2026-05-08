import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class NTPTests {
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        n,              expected
        0,              0
        1,              1
        999,            999
        1000,           "1,000"
        1000000,        "1,000,000"
        1234567,        "1,234,567"
        -1000,          "-1,000"
        9223372036854775807, "9,223,372,036,854,775,807"
        """)
    void formatWithCommasLong(long n, String expected) {
        assertEquals(expected, NTP.formatWithCommas(n));
    }

    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        n,                        expected
        0,                        0
        999,                      999
        1000,                     "1,000"
        1234567,                  "1,234,567"
        # larger than Long.MAX_VALUE
        99999999999999999999,     "99,999,999,999,999,999,999"
        12345678901234567890,     "12,345,678,901,234,567,890"
        """)
    void formatWithCommasBigInteger(String n, String expected) {
        assertEquals(expected, NTP.formatWithCommas(new BigInteger(n)));
    }
    
    
    @ParameterizedTest
    @MethodSource("wrappedStringCases")
    void wrappedString(String text, String expected) {
        assertEquals(expected, NTP.wrapped(text));
    }
    
    static Stream<Arguments> wrappedStringCases() {
        return Stream.of(
            arguments("", ""),
            arguments("hello", "hello"),
            arguments(
                "The quick brown fox jumps over the lazy dog",
                "The quick brown fox jumps over the lazy dog"
            ),
            arguments(
                "The quick brown fox jumps over the lazy dog and then some more words are added here to test wrapping",
                "The quick brown fox jumps over the lazy dog and then some more words are added here to\ntest wrapping"
            ),
            arguments(
                "The quick brown fox jumps over the lazy dog and then some more words are added here to test wrapping behavior when the line gets really long and needs to be wrapped more than once for the test",
                "The quick brown fox jumps over the lazy dog and then some more words are added here to\ntest wrapping behavior when the line gets really long and needs to be wrapped more than\nonce for the test"
            )
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("wrappedListCases")
    void wrappedList(List<String> items, String expected) {
        assertEquals(expected, NTP.wrapped(items, Function.identity()));
    }

    static Stream<Arguments> wrappedListCases() {
        String piece = "abcdefghij";
        String line = String.join("    ", piece, piece, piece, piece, piece);
        return Stream.of(
            arguments(List.of(), ""),
            arguments(List.of("abc"), "abc"),
            arguments(List.of("1", "2", "3"), "1    2    3"),
            arguments(List.of(piece, piece, piece, piece, piece), line),
            arguments(
                List.of(piece, piece, piece, piece, piece, piece, piece, piece, piece, piece),
                line + "\n" + line
            )
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("findPrimesFromCases")
    void findPrimesFrom(long start, List<Long> expected) {
        assertEquals(expected, NTP.findPrimesFrom(start));
    }
    
    private static List<Long> longListOf(long... values) {
        return Arrays.stream(values).boxed().toList();
    }

    static Stream<Arguments> findPrimesFromCases() {
        return Stream.of(
            arguments(
                0,
                longListOf(
                    2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47,
                    53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113
                )
            ),
            arguments(
                1_000,
                longListOf(
                    1_009, 1_013, 1_019, 1_021, 1_031, 1_033, 1_039, 1_049, 1_051, 1_061,
                    1_063, 1_069, 1_087, 1_091, 1_093, 1_097, 1_103, 1_109, 1_117, 1_123,
                    1_129, 1_151, 1_153, 1_163, 1_171, 1_181, 1_187, 1_193, 1_201, 1_213
                )
            )
        );
    }

    
    @ParameterizedTest
    @MethodSource("findSemiprimesFromCases")
    void findSemiprimesFrom(long start, List<NTP.SemiprimeData> expected) {
        assertEquals(expected, NTP.findSemiprimesFrom(start));
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
    void findTwinPrimesFrom(long start, List<Long> expected) {
        assertEquals(expected, NTP.findTwinPrimesFrom(start));
    }
    
    static Stream<Arguments> findTwinPrimesFromCases() {
        return Stream.of(
            arguments(
                0,
                longListOf(
                    3, 5, 11, 17, 29, 41, 59, 71, 101, 107,
                    137, 149, 179, 191, 197, 227, 239, 269, 281, 311
                )
            ),
            arguments(
                1_000,
                longListOf(
                    1_019, 1_031, 1_049, 1_061, 1_091, 1_151, 1_229, 1_277, 1_289, 1_301,
                    1_319, 1_427, 1_451, 1_481, 1_487, 1_607, 1_619, 1_667, 1_697, 1_721
                )
            )
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("euclideanAlgorithmCases")
    void euclideanAlgorithm(long a, long b, List<NTP.EuclideanStep> expected) {
        assertEquals(expected, NTP.euclideanAlgorithm(a, b));
    }

    private static NTP.EuclideanStep es(long max, long min, long remainder) {
        return new NTP.EuclideanStep(max, min, remainder);
    }

    static Stream<Arguments> euclideanAlgorithmCases() {
        return Stream.of(
            arguments(48, 18, List.of(es(48, 18, 12), es(18, 12, 6), es(12, 6, 0))),
            arguments(17, 13, List.of(es(17, 13, 4), es(13, 4, 1), es(4, 1, 0))),
            arguments(100, 25, List.of(es(100, 25, 0)))
        );
    }

    @Test
    void euclideanAlgorithmSwappedInputs() {
        assertEquals(NTP.euclideanAlgorithm(48, 18), NTP.euclideanAlgorithm(18, 48));
    }
    
    
    @ParameterizedTest
    @MethodSource("gcdAndLcmFactorsCases")
    void gcdAndLcmFactorsParameterized(long a, long b, List<NTP.FactorAndPower> expectedGcd, List<NTP.FactorAndPower> expectedLcm) {
        var result = new NTP.GcdAndLcmPrimeFactorizationData(NTP.primeFactorsAndPowersOf(a), NTP.primeFactorsAndPowersOf(b));
        assertEquals(expectedGcd, result.gcdFps());
        assertEquals(expectedLcm, result.lcmFps());
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
            arguments(17, 13, List.of(), List.of(fp(13, 1), fp(17, 1)))
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("goldbachPairsCases")
    void goldbachPairs(long n, List<Long> expected) {
        assertEquals(expected, NTP.goldbachPairs(n));
    }
    
    static Stream<Arguments> goldbachPairsCases() {
        return Stream.of(
            arguments(4, List.of(2L)),
            arguments(10, List.of(3L, 5L)),
            arguments(100, List.of(3L, 11L, 17L, 29L, 41L, 47L))
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
                0,
                List.of(
                    pt(3, 4, 5),    pt(5, 12, 13),  pt(6, 8, 10),
                    pt(7, 24, 25),  pt(8, 15, 17),  pt(9, 12, 15),
                    pt(9, 40, 41),  pt(10, 24, 26), pt(11, 60, 61),
                    pt(12, 16, 20)
                )
            ),
            arguments(
                30,
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
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        a,  b,   c,   expected
        3,  4,   5,   true
        5,  12,  13,  true
        8,  15,  17,  true
        7,  24,  25,  true
        20, 21,  29,  true
        # 2 × (3, 4, 5)
        6,  8,   10,  false
        # 3 × (3, 4, 5)
        9,  12,  15,  false
        # 2 × (5, 12, 13)
        10, 24,  26,  false
        """)
    void pythagTripleIsPrimitive(long a, long b, long c, boolean expected) {
        assertEquals(expected, pt(a, b, c).isPrimitive());
    }
    
    
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        start, prime, a, b
        0,     5,     1, 2
        13,    13,    2, 3
        14,    17,    1, 4
        100,   101,   1, 10
        50,    53,    2, 7
        """)
    void twoSquareData(long start, long prime, long a, long b) {
        var ts = new NTP.TwoSquareData(start);
        assertEquals(prime, ts.prime);
        assertEquals(a, ts.a);
        assertEquals(b, ts.b);
    }

    
    @ParameterizedTest
    @MethodSource("fibonacciLikeSequenceCases")
    void fibonacciLikeSequence(long a, long b, List<BigInteger> expected) {
        assertEquals(expected, NTP.fibonacciLikeSequence(a, b));
    }
    
    private static List<BigInteger> bigIntListOf(int... values) {
        return Arrays.stream(values).mapToObj(BigInteger::valueOf).toList();
    }

    static Stream<Arguments> fibonacciLikeSequenceCases() {
        return Stream.of(
            arguments(
                1,
                1,
                bigIntListOf(
                    1, 1, 2, 3, 5, 8, 13, 21, 34, 55,
                    89, 144, 233, 377, 610, 987, 1_597, 2_584, 4_181, 6_765
                )
            ),
            arguments(
                304,
                5,
                bigIntListOf(
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
    void powersOfTwoSumming(long a, long b, List<NTP.PowerAndMultiple> expected) {
        assertEquals(expected, NTP.powersOfTwoSumming(a, b));
    }
    
    private static NTP.PowerAndMultiple pm(long power, long multiple) {
        return new NTP.PowerAndMultiple(power, BigInteger.valueOf(multiple));
    }
    
    static Stream<Arguments> powersOfTwoSummingCases() {
        return Stream.of(
            arguments(13, 12, List.of(pm(1, 12), pm(4, 48), pm(8, 96))),
            arguments(2, 2, List.of(pm(2, 4))),
            arguments(16, 3, List.of(pm(16, 48))),
            arguments(7, 5, List.of(pm(1, 5), pm(2, 10), pm(4, 20))),
            arguments(15, 1, List.of(pm(1, 1), pm(2, 2), pm(4, 4), pm(8, 8)))
        );
    }
}