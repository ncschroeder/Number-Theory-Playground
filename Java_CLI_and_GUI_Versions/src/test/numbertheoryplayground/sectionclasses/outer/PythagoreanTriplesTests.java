package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.sectionclasses.outer.PythagoreanTriples.Triple;

/**
 * Has tests for code in the PythagoreanTriples class.
 */
class PythagoreanTriplesTests {
    @ParameterizedTest
    @MethodSource("getArgsForGetTriples")
    void getTriples(int input, List<Triple> expectedTriples) {
        assertEquals(expectedTriples, PythagoreanTriples.getTriples(input));
    }
    
    static Triple t(int a, int b, int c) {
        return new Triple(a, b, c);
    }

    static Stream<Arguments> getArgsForGetTriples() {
        /*
        Primitive Pythagorean triples are ones where the 3 numbers have a GCD of 1.
        Each non-primitive triple in the lists is followed by a comment of the primitive
        triple that it derives from.
         */

        List<Triple> first10Triples =
            List.of(
                t(3, 4, 5),
                t(5, 12, 13),
                t(6, 8, 10), // 3, 4, 5
                t(7, 24, 25),
                t(8, 15, 17),
                t(9, 12, 15), // 3, 4, 5
                t(9, 40, 41),
                t(10, 24, 26), // 5, 12, 13
                t(11, 60, 61),
                t(12, 16, 20) // 3, 4, 5
            );
        
        List<Triple> first10TriplesAfter100 =
            List.of(
                t(100, 105, 145), // 20, 21, 29
                t(100, 240, 260), // 5, 12, 13
                t(100, 495, 505), // 20, 99, 101
                t(100, 621, 629),
                t(100, 1_248, 1_252), // 25, 312, 313
                t(100, 2_499, 2_501),
                t(101, 5_100, 5_101),
                t(102, 136, 170), // 3, 4, 5
                t(102, 280, 298), // 51, 140, 149
                t(102, 864, 870) // 17, 144, 145
            );

        return Stream.of(
            arguments(0, first10Triples),
            arguments(100, first10TriplesAfter100)
        );
    }

    
    /**
     * a, b, and c will form a primitive triple but a triple formed by multiplying those by the
     * multiplicand won't be primitive.
     */
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        A,    B,    C,    MULTIPLICAND
        3,    4,    5,         2
        11,   60,   61,        5
        100,  621,  629,       11
        """)
    void tripleIsPrimitive(int a, int b, int c, int multiplicand) {
        assertAll(
            () -> assertTrue(t(a, b, c).isPrimitive()),
            () -> assertFalse(t(a * multiplicand, b * multiplicand, c * multiplicand).isPrimitive())
        );
    }
}
