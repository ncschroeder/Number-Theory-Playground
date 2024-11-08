import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import com.nicholasschroeder.numbertheoryplayground.GoldbachConjecture;
import com.nicholasschroeder.numbertheoryplayground.Section;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.nicholasschroeder.numbertheoryplayground.Divisibility.isEven;
import static com.nicholasschroeder.numbertheoryplayground.GoldbachConjecture.getPrimePairStarts;

/**
 * Has tests for code in the GoldbachConjecture class.
 */
class GoldbachConjectureTest {
    @ParameterizedTest
    @MethodSource("getArgs")
    void testGetPrimePairStarts(long input, int[] expectedPairStarts) {
        assertArrayEquals(expectedPairStarts, getPrimePairStarts(input));
    }

    static Stream<Arguments> getArgs() {
        return Stream.of(
            arguments(6, new int[] { 3 }),
            arguments(10, new int[] { 3, 5 }),
            arguments(32, new int[] { 3, 13 }),
            arguments(58, new int[] { 5, 11, 17, 29 }),
            arguments(100, new int[] { 3, 11, 17, 29, 41, 47 })
        );
    }
    
    Section section = new GoldbachConjecture.Section();
    
    @RepeatedTest(10)
    void sectionRandomInput() {
        assertTrue(isEven(section.getRandomInput()));
    }
}