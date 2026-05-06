package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GoldbachConjectureTests {
    @BeforeAll
    static void setMaxInput() {
        GoldbachConjecture.setMaxInputForCli();
    }
    
    @ParameterizedTest
    @FieldSource("args")
    void getPrimePairStarts(long input, List<Integer> expectedPairStarts) {
        assertEquals(expectedPairStarts, GoldbachConjecture.getPrimePairStarts(input));
    }
    
    static final List<Arguments> args =
        List.of(
            arguments(6, List.of(3)),
            arguments(10, List.of(3, 5)),
            arguments(32, List.of(3, 13)),
            arguments(58, List.of(5, 11, 17, 29)),
            arguments(100, List.of(3, 11, 17, 29, 41, 47))
        );
}
