package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.sectionclasses.outer.PythagoreanTriples.Triple;

class PythagoreanTriplesTests {
    static Triple t(int a, int b, int c) {
        return new Triple(a, b, c);
    }
    
    
    @ParameterizedTest
    @MethodSource("getArgsForGetTriples")
    void getTriples(int input, List<Triple> expectedTriples) {
        assertEquals(expectedTriples, PythagoreanTriples.getTriples(input));
    }
    
    static Stream<Arguments> getArgsForGetTriples() {
        /*
        For these triples, I got the values of a, or the short leg lengths, from OEIS sequence
        A009004 at https://oeis.org/A009004. I got the values of b and c by running the
        NTP CLI and having it do a Pythagorean Triples section calculation but I verified that
        a^2 + b^2 = c^2.
         */
        
        List<Triple> expectedTriples1 =
            List.of(
                t(3, 4, 5),
                t(5, 12, 13),
                t(6, 8, 10),
                t(7, 24, 25),
                t(8, 15, 17),
                t(9, 12, 15),
                t(9, 40, 41),
                t(10, 24, 26),
                t(11, 60, 61),
                t(12, 16, 20)
            );
        
        List<Triple> expectedTriples2 =
            List.of(
                t(30, 40, 50),
                t(30, 72, 78),
                t(30, 224, 226),
                t(31, 480, 481),
                t(32, 60, 68),
                t(32, 126, 130),
                t(32, 255, 257),
                t(33, 44, 55),
                t(33, 56, 65),
                t(33, 180, 183)
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
    void tripleIsPrimitive(int a, int b, int c, int multiplicand) {
        assertAll(
            () -> assertTrue(t(a, b, c).isPrimitive()),
            () -> assertFalse(
                t(a * multiplicand, b * multiplicand, c * multiplicand).isPrimitive()
            )
        );
    }
}
