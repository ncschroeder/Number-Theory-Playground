package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.Misc.createStringWithCommas;
import static numbertheoryplayground.sectionclasses.outer.Divisibility.*;
import static numbertheoryplayground.sectionclasses.outer.PrimeFactorization.FactorAndExponent;

class DivisibilityTests {
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
INPUT,      LAST_2_DIGITS,  LAST_3_DIGITS,  SUM_OF_DIGITS_EXPRESSION,        ALT_SUM_OF_DIGITS_EXPRESSION,    ALT_SUM_OF_BLOCKS_EXPRESSION
1_000,           0,              0,         1 + 0 + 0 + 0 = 1,               1 − 0 + 0 − 0 = 1,                    000 − 1 = -1
60_060,          60,             60,        6 + 0 + 0 + 6 + 0 = 12,          6 − 0 + 0 − 6 + 0 = 0,                060 − 60 = 0
200_008,         8,              8,         2 + 0 + 0 + 0 + 0 + 8 = 10,      2 − 0 + 0 − 0 + 0 − 8 = -6,           008 − 200 = -192
4_695_768,       68,             768,       4 + 6 + 9 + 5 + 7 + 6 + 8 = 45,  4 − 6 + 9 − 5 + 7 − 6 + 8 = 11,       768 − 695 + 4 = 77
""")
    void rulesAnswer(
        int input,
        int expectedLast2Digits,
        int expectedLast3Digits,
        String expectedSumOfDigitsExpression,
        String expectedAltSumOfDigitsExpression,
        String expectedAltSumOfBlocksExpression
    ) {
        var answer = new RulesAnswer(input, createStringWithCommas(input));
        assertAll(
            () -> assertEquals(expectedLast2Digits, answer.getLast2Digits()),
            () -> assertEquals(expectedLast3Digits, answer.getLast3Digits()),
            () -> assertEquals(expectedSumOfDigitsExpression, answer.getSumOfDigitsExpression()),
            () -> assertEquals(expectedAltSumOfDigitsExpression, answer.getAltSumOfDigitsExpression()),
            () -> assertEquals(expectedAltSumOfBlocksExpression, answer.getAltSumOfBlocksExpression())
        );
    }
    
    
    @ParameterizedTest
    @FieldSource("argsForNumberOfFactorsData")
    void numberOfFactorsData(int input, String expectedExpression, int expectedNumFactors) {
        var data = new NumberOfFactorsData(new PrimeFactorization(input));
        assertAll(
            () -> assertEquals(expectedExpression, data.getExpression()),
            () -> assertEquals(expectedNumFactors, data.getNumFactors())
        );
    }
    
    static final List<Arguments> argsForNumberOfFactorsData =
        List.of(
            arguments(2 * 2, "(2 + 1)", 3),
            arguments(2 * 3, "(1 + 1) × (1 + 1)", 2 * 2),
            arguments(2 * 3 * 3 * 5 * 5 * 5, "(1 + 1) × (2 + 1) × (3 + 1)", 2 * 3 * 4)
        );
    
    
    @ParameterizedTest
    @MethodSource("getArgsForGetFactorPfs")
    void getFactorPfs(
        List<FactorAndExponent> input,
        List<List<FactorAndExponent>> expectedFactorFeLists
    ) {
        List<List<FactorAndExponent>> actualFactorFeLists =
            Divisibility.getFactorPfs(new PrimeFactorization(input), 2)
            .stream()
            .map(PrimeFactorization::getFes)
            .toList();
        
        assertEquals(expectedFactorFeLists, actualFactorFeLists);
    }
    
    static FactorAndExponent fe(long factor, int exponent) {
        return new FactorAndExponent(factor, exponent);
    }
    
    static Stream<Arguments> getArgsForGetFactorPfs() {
        List<FactorAndExponent> input1 = List.of(fe(2, 2), fe(3, 1));
        List<List<FactorAndExponent>> expectedFactorFeLists1 =
            List.of(
                List.of(fe(2, 1)),
                List.of(fe(3, 1)),
                List.of(fe(2, 2)),
                List.of(fe(2, 1), fe(3, 1))
            );

        List<FactorAndExponent> input2 = List.of(fe(2, 1), fe(3, 2), fe(5, 2));
        List<List<FactorAndExponent>> expectedFactorFeLists2 =
            List.of(
                // The comments say the corresponding factor.
                List.of(fe(2, 1)), // 2
                List.of(fe(3, 1)), // 3
                List.of(fe(5, 1)), // 5
                List.of(fe(2, 1), fe(3, 1)), // 6
                List.of(fe(3, 2)), // 9
                List.of(fe(2, 1), fe(5, 1)), // 10
                List.of(fe(3, 1), fe(5, 1)), // 15
                List.of(fe(2, 1), fe(3, 2)), // 18
                List.of(fe(5, 2)), // 25
                List.of(fe(2, 1), fe(3, 1), fe(5, 1)), // 30
                List.of(fe(3, 2), fe(5, 1)), // 45
                List.of(fe(2, 1), fe(5, 2)), // 50
                List.of(fe(3, 1), fe(5, 2)), // 75
                List.of(fe(2, 1), fe(3, 2), fe(5, 1)), // 90
                List.of(fe(2, 1), fe(3, 1), fe(5, 2)), // 150
                List.of(fe(3, 2), fe(5, 2)) // 225
            );
        
        return Stream.of(
            arguments(input1, expectedFactorFeLists1),
            arguments(input2, expectedFactorFeLists2),
            arguments(List.of(fe(2, 1)), Collections.emptyList()),
            arguments(
                List.of(fe(2, 4)),
                List.of(List.of(fe(2, 1)), List.of(fe(2, 2)), List.of(fe(2, 3)))
            ),
            arguments(
                List.of(fe(2, 1), fe(3, 1)),
                List.of(List.of(fe(2, 1)), List.of(fe(3, 1)))
            )
        );
    }
}
