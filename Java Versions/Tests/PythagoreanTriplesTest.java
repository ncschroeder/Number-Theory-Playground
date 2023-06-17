import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.PythagoreanTriples.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Has tests for code in the PythagoreanTriples class.
 */
class PythagoreanTriplesTest {
    /**
     * Shortened constructor for a PythagoreanTriple
     */
    static PythagoreanTriple pt(int side1, int side2, int hypotenuse) {
        return new PythagoreanTriple(side1, side2, hypotenuse);
    }
    
    
    @Test
    @DisplayName("PythagoreanTriple string conversion and equality, Misc.getLongAndSquareString method")
    void pythagTripleObject() {
        assertAll(
            () -> assertEquals(
                "3^2 (9) + 1,000^2 (1,000,000) = 100,005^2 (10,001,000,025)",
                pt(3, 1_000, 100_005).toString()
            ),
            () -> assertTrue(pt(1, 2, 3).equals(pt(1, 2, 3)))
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("getPythagTriplesArgs")
    void testGetPythagTriples(int input, List<PythagoreanTriple> expectedTriples) {
        assertEquals(expectedTriples, getPythagTriples(input));
    }
    
    static Stream<Arguments> getPythagTriplesArgs() {
        List<PythagoreanTriple> first10PythagTriples =
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

        List<PythagoreanTriple> first10PythagTriplesAfter100 =
            List.of(
                pt(100, 105, 145),
                pt(100, 240, 260),
                pt(100, 495, 505),
                pt(100, 621, 629),
                pt(100, 1_248, 1_252),
                pt(100, 2_499, 2_501),
                pt(101, 5_100, 5_101),
                pt(102, 136, 170),
                pt(102, 280, 298),
                pt(102, 864, 870)
            );
        
        return Stream.of(
            arguments(0, first10PythagTriples),
            arguments(100, first10PythagTriplesAfter100)
        );
    }
}