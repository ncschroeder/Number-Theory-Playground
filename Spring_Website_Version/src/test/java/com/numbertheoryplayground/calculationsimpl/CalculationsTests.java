package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.Calculations.PythagoreanTriple;

class CalculationsTests {
    @ParameterizedTest
    @MethodSource("getArgsForGetPrimes")
    void getPrimes(int input, int[] expectedPrimes) {
        assertArrayEquals(expectedPrimes, Calculations.getPrimes(input));
    }
    
    static Stream<Arguments> getArgsForGetPrimes() {
        var first30Primes =
            new int[] {
                2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53,
                59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113
            };
        
        var first30PrimesAfter1000 =
            new int[] {
                1_009, 1_013, 1_019, 1_021, 1_031, 1_033, 1_039, 1_049, 1_051, 1_061, 1_063,
                1_069, 1_087, 1_091, 1_093, 1_097, 1_103, 1_109, 1_117, 1_123, 1_129, 1_151,
                1_153, 1_163, 1_171, 1_181, 1_187, 1_193, 1_201, 1_213
            };
        
        return Stream.of(
            arguments(0, first30Primes),
            arguments(first30PrimesAfter1000[0], first30PrimesAfter1000)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("getArgsForGetTwinPrimePairStarts")
    void getTwinPrimePairStarts(int input, int[] expectedPairStarts) {
        assertArrayEquals(expectedPairStarts, Calculations.getTwinPrimePairStarts(input));
    }
    
    static Stream<Arguments> getArgsForGetTwinPrimePairStarts() {
        var first20PairStarts =
            new int[] {
                3, 5, 11, 17, 29, 41, 59, 71, 101, 107, 137,
                149, 179, 191, 197, 227, 239, 269, 281, 311
            };
        
        var first20PairStartsAfter1000 =
            new int[] {
                1_019, 1_031, 1_049, 1_061, 1_091, 1_151, 1_229, 1_277, 1_289, 1_301,
                1_319, 1_427, 1_451, 1_481, 1_487, 1_607, 1_619, 1_667, 1_697, 1_721
            };
        
        return Stream.of(
            arguments(0, first20PairStarts),
            arguments(1_000, first20PairStartsAfter1000)
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
        Primitive Pythagorean triples are ones where the 3 numbers have a GCD of 1.
        Each non-primitive triple in the lists is followed by a comment of the primitive
        triple that it derives from.
         */
        
        List<PythagoreanTriple> first10Triples =
            List.of(
                pt(3, 4, 5),
                pt(5, 12, 13),
                pt(6, 8, 10), // 3, 4, 5
                pt(7, 24, 25),
                pt(8, 15, 17),
                pt(9, 12, 15), // 3, 4, 5
                pt(9, 40, 41),
                pt(10, 24, 26), // 5, 12, 13
                pt(11, 60, 61),
                pt(12, 16, 20) // 3, 4, 5
            );

        List<PythagoreanTriple> first10TriplesAfter100 =
            List.of(
                pt(100, 105, 145), // 20, 21, 29
                pt(100, 240, 260), // 5, 12, 13
                pt(100, 495, 505), // 20, 99, 101
                pt(100, 621, 629),
                pt(100, 1_248, 1_252), // 25, 312, 313
                pt(100, 2_499, 2_501),
                pt(101, 5_100, 5_101),
                pt(102, 136, 170), // 3, 4, 5
                pt(102, 280, 298), // 51, 140, 149
                pt(102, 864, 870) // 17, 144, 145
            );
        
        return Stream.of(
            arguments(0, first10Triples),
            arguments(100, first10TriplesAfter100)
        );
    }
    
    /**
     * a, b, and c will form a primitive triple but a triple formed by multiplying those by
     * the multiplicand won't be primitive.
     */
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        A,    B,    C,    MULTIPLICAND
        3,    4,    5,    2
        11,   60,   61,   5
        100,  621,  629,  11
        """)
    void pythagTripleIsPrimitive(int a, int b, int c, int multiplicand) {
        assertAll(
            () -> assertTrue(pt(a, b, c).isPrimitive()),
            () -> assertFalse(pt(a * multiplicand, b * multiplicand, c * multiplicand).isPrimitive())
        );
    }
}