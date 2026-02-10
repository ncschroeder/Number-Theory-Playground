package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.Calculations.*;

class CalculationsTests {
    @ParameterizedTest
    @MethodSource("getArgsForGetPrimes")
    void getPrimes(int input, int[] expectedPrimes) {
        assertArrayEquals(expectedPrimes, Calculations.getPrimes(input));
    }
    
    static Stream<Arguments> getArgsForGetPrimes() {
        // I got these primes from https://en.wikipedia.org/wiki/List_of_prime_numbers.
        
        var first30Primes =
            new int[] {
                2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53,
                59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113
            };
        
        var first30PrimesAfter1000 =
            new int[] {
                1_009, 1_013, 1_019, 1_021, 1_031, 1_033, 1_039, 1_049, 1_051, 1_061,
                1_063, 1_069, 1_087, 1_091, 1_093, 1_097, 1_103, 1_109, 1_117, 1_123,
                1_129, 1_151, 1_153, 1_163, 1_171, 1_181, 1_187, 1_193, 1_201, 1_213
            };
        
        return Stream.of(
            arguments(0, first30Primes),
            arguments(first30PrimesAfter1000[0], first30PrimesAfter1000)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("getArgsForGetSemiprimesData")
    void getSemiprimesData(int input, List<SemiprimeData> expectedSemiprimesData) {
        assertEquals(expectedSemiprimesData, Calculations.getSemiprimesData(input));
    }
    
    static SemiprimeData sd(int semiprime, int factor1, int factor2) {
        return new SemiprimeData(semiprime, factor1, factor2);
    }
    
    static Stream<Arguments> getArgsForGetSemiprimesData() {
        /*
        For this data, I got the semiprimes from OEIS sequence A001358 at
        https://oeis.org/A001358 and found the factors myself.
         */
        
        List<SemiprimeData> dataForFirst20Semiprimes =
            List.of(
                sd(4, 2, 2),
                sd(6, 2, 3),
                sd(9, 3, 3),
                sd(10, 2, 5),
                sd(14, 2, 7),
                sd(15, 3, 5),
                sd(21, 3, 7),
                sd(22, 2, 11),
                sd(25, 5, 5),
                sd(26, 2, 13),
                sd(33, 3, 11),
                sd(34, 2, 17),
                sd(35, 5, 7),
                sd(38, 2, 19),
                sd(39, 3, 13),
                sd(46, 2, 23),
                sd(49, 7, 7),
                sd(51, 3, 17),
                sd(55, 5, 11),
                sd(57, 3, 19)
            );
        
        List<SemiprimeData> dataForFirst20SemiprimesAfter100 =
            List.of(
                sd(106, 2, 53),
                sd(111, 3, 37),
                sd(115, 5, 23),
                sd(118, 2, 59),
                sd(119, 7, 17),
                sd(121, 11, 11),
                sd(122, 2, 61),
                sd(123, 3, 41),
                sd(129, 3, 43),
                sd(133, 7, 19),
                sd(134, 2, 67),
                sd(141, 3, 47),
                sd(142, 2, 71),
                sd(143, 11, 13),
                sd(145, 5, 29),
                sd(146, 2, 73),
                sd(155, 5, 31),
                sd(158, 2, 79),
                sd(159, 3, 53),
                sd(161, 7, 23)
            );
        
        return Stream.of(
            arguments(0, dataForFirst20Semiprimes),
            arguments(
                dataForFirst20SemiprimesAfter100.getFirst().semiprime(),
                dataForFirst20SemiprimesAfter100
            )
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("getArgsForGetTwinPrimePairStarts")
    void getTwinPrimePairStarts(int input, int[] expectedPairStarts) {
        assertArrayEquals(expectedPairStarts, Calculations.getTwinPrimePairStarts(input));
    }
    
    static Stream<Arguments> getArgsForGetTwinPrimePairStarts() {
        // I got these pair starts from OEIS sequence A001359 at https://oeis.org/A001359.
        
        var first20PairStarts =
            new int[] {
                3, 5, 11, 17, 29, 41, 59, 71, 101, 107, 137,
                149, 179, 191, 197, 227, 239, 269, 281, 311
            };
        
        var first20PairStartsAfter500 =
            new int[] {
                521, 569, 599, 617, 641, 659, 809, 821, 827, 857, 881, 1_019,
                1_031, 1_049, 1_061, 1_091, 1_151, 1_229, 1_277, 1_289
            };
        
        return Stream.of(
            arguments(0, first20PairStarts),
            arguments(first20PairStartsAfter500[0], first20PairStartsAfter500)
        );
    }
    
    
    @ParameterizedTest
    @FieldSource("argsForGetGoldbachPrimePairStarts")
    void getGoldbachPrimePairStarts(int input, int[] expectedPairStarts) {
        assertArrayEquals(expectedPairStarts, Calculations.getGoldbachPrimePairStarts(input));
    }
    
    static final List<Arguments> argsForGetGoldbachPrimePairStarts =
        List.of(
            arguments(6, new int[] { 3 }),
            arguments(10, new int[] { 3, 5 }),
            arguments(32, new int[] { 3, 13 }),
            arguments(58, new int[] { 5, 11, 17, 29 }),
            arguments(100, new int[] { 3, 11, 17, 29, 41, 47 })
        );
    
    
    static PythagoreanTriple pt(int a, int b, int c) {
        return new PythagoreanTriple(a, b, c);
    }
    
    @ParameterizedTest
    @MethodSource("getArgsForGetPythagTriples")
    void getPythagTriples(int input, List<PythagoreanTriple> expectedTriples) {
        assertEquals(expectedTriples, Calculations.getPythagTriples(input));
    }
    
    static Stream<Arguments> getArgsForGetPythagTriples() {
        /*
        For these triples, I got the values of a, or the short leg lengths, from OEIS sequence
        A009004 at https://oeis.org/A009004. I got the values of b and c by running the
        NTP CLI and having it do a Pythagorean Triples section calculation but I verified that
        a^2 + b^2 = c^2.
         */
        
        List<PythagoreanTriple> expectedTriples1 =
            List.of(
                pt(3, 4, 5),
                pt(5, 12, 13),
                pt(6, 8, 10),
                pt(7, 24, 25),
                pt(8, 15, 17),
                pt(9, 12, 15),
                pt(9, 40, 41),
                pt(10, 24, 26),
                pt(11, 60, 61),
                pt(12, 16, 20)
            );
        
        List<PythagoreanTriple> expectedTriples2 =
            List.of(
                pt(30, 40, 50),
                pt(30, 72, 78),
                pt(30, 224, 226),
                pt(31, 480, 481),
                pt(32, 60, 68),
                pt(32, 126, 130),
                pt(32, 255, 257),
                pt(33, 44, 55),
                pt(33, 56, 65),
                pt(33, 180, 183)
            );
        
        return Stream.of(
            arguments(0, expectedTriples1),
            arguments(expectedTriples2.getFirst().a(), expectedTriples2)
        );
    }
    
    /**
     * a, b, and c are a primitive triple but a triple formed by multiplying those by the
     * multiplicand won't be primitive.
     */
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        A,    B,    C,    MULTIPLICAND
        3,    4,    5,         2
        11,   60,   61,        5
        33,   56,   65,        11
        """)
    void pythagTripleIsPrimitive(int a, int b, int c, int multiplicand) {
        assertAll(
            () -> assertTrue(pt(a, b, c).isPrimitive()),
            () -> assertFalse(
                pt(a * multiplicand, b * multiplicand, c * multiplicand).isPrimitive()
            )
        );
    }
}