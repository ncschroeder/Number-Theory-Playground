package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GoldbachConjectureTests {
    @BeforeAll
    static void setMaxInput() {
        GoldbachConjecture.setMaxInputForCli();
    }
    
    @ParameterizedTest
    @FieldSource("args")
    void getPrimePairStarts(long input, int[] expectedPairStarts) {
        assertArrayEquals(expectedPairStarts, GoldbachConjecture.getPrimePairStarts(input));
    }
    
    static final List<Arguments> args =
        List.of(
            arguments(6, new int[] { 3 }),
            arguments(10, new int[] { 3, 5 }),
            arguments(32, new int[] { 3, 13 }),
            arguments(58, new int[] { 5, 11, 17, 29 }),
            arguments(100, new int[] { 3, 11, 17, 29, 41, 47 })
        );
}
