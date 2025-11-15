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
import static numbertheoryplayground.sectionclasses.outer.PrimeFactorization.FactorAndPower;

class DivisibilityTests {
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
INPUT,      LAST_2_DIGITS,  LAST_3_DIGITS,  SUM_OF_DIGITS,  BLOCKS_ALT_SUM_EXPRESSION,  DIGITS_ALT_SUM_EXPRESSION
1_000,           0,              0,              1,             000 − 1 = -1,             1 − 0 + 0 − 0 = 1
60_060,          60,             60,             12,            060 − 60 = 0,             6 − 0 + 0 − 6 + 0 = 0
200_008,         8,              8,              10,            008 − 200 = -192,         2 − 0 + 0 − 0 + 0 − 8 = -6
4_695_768,       68,             768,            45,            768 − 695 + 4 = 77,       4 − 6 + 9 − 5 + 7 − 6 + 8 = 11
""")
    void rulesAnswer(
        int input,
        int expectedLast2Digits,
        int expectedLast3Digits,
        int expectedSumOfDigits,
        String expectedBlocksAltSumExpression,
        String expectedDigitsAltSumExpression
    ) {
        var answer = new RulesAnswer(input, createStringWithCommas(input));
        
        assertAll(
            () -> assertEquals(expectedLast2Digits, answer.getLast2Digits()),
            () -> assertEquals(expectedLast3Digits, answer.getLast3Digits()),
            () -> assertEquals(expectedSumOfDigits, answer.getSumOfDigits()),
            () -> assertEquals(expectedBlocksAltSumExpression, answer.getBlocksAltSumExpression()),
            () -> assertEquals(expectedDigitsAltSumExpression, answer.getDigitsAltSumExpression())
        );
    }
    
    
    @ParameterizedTest
    @FieldSource("argsForNumberOfFactorsAndExpression")
    void numberOfFactorsAndExpression(int input, String expectedExpression, int expectedNumFactors) {
        var numFactorsAndExpression =
            new NumberOfFactorsAndExpression(new PrimeFactorization(input, ""));
        
        assertAll(
            () -> assertEquals(expectedExpression, numFactorsAndExpression.getExpression()),
            () -> assertEquals(expectedNumFactors, numFactorsAndExpression.getNumFactors())
        );
    }
    
    static final List<Arguments> argsForNumberOfFactorsAndExpression =
        List.of(
            arguments(2 * 2, "(2 + 1)", 3),
            arguments(2 * 3, "(1 + 1) × (1 + 1)", 2 * 2),
            arguments(2 * 3 * 3 * 5 * 5 * 5, "(1 + 1) × (2 + 1) × (3 + 1)", 2 * 3 * 4)
        );
    
    
    @ParameterizedTest
    @MethodSource("getArgsForGetFactorPfs")
    void getFactorPfs(
        List<FactorAndPower> input,
        List<List<FactorAndPower>> expectedFactorFpLists
    ) {
        List<List<FactorAndPower>> actualFactorFpLists =
            Divisibility.getFactorPfs(new PrimeFactorization(input))
            .stream()
            .map(PrimeFactorization::getFps)
            .toList();
        
        assertEquals(expectedFactorFpLists, actualFactorFpLists);
    }
    
    static FactorAndPower fp(long factor, int power) {
        return new FactorAndPower(factor, power);
    }
    
    static Stream<Arguments> getArgsForGetFactorPfs() {
        List<FactorAndPower> input1 = List.of(fp(2, 2), fp(3, 1));
        List<List<FactorAndPower>> expectedFactorFpLists1 =
        List.of(
            List.of(fp(2, 1)),
            List.of(fp(3, 1)),
            List.of(fp(2, 2)),
            List.of(fp(2, 1), fp(3, 1))
        );

        List<FactorAndPower> input2 = List.of(fp(2, 1), fp(3, 2), fp(5, 2));
        List<List<FactorAndPower>> expectedFactorFpLists2 =
        List.of(
            // The comments say the corresponding factor.
            List.of(fp(2, 1)), // 2
            List.of(fp(3, 1)), // 3
            List.of(fp(5, 1)), // 5
            List.of(fp(2, 1), fp(3, 1)), // 6
            List.of(fp(3, 2)), // 9
            List.of(fp(2, 1), fp(5, 1)), // 10
            List.of(fp(3, 1), fp(5, 1)), // 15
            List.of(fp(2, 1), fp(3, 2)), // 18
            List.of(fp(5, 2)), // 25
            List.of(fp(2, 1), fp(3, 1), fp(5, 1)), // 30
            List.of(fp(3, 2), fp(5, 1)), // 45
            List.of(fp(2, 1), fp(5, 2)), // 50
            List.of(fp(3, 1), fp(5, 2)), // 75
            List.of(fp(2, 1), fp(3, 2), fp(5, 1)), // 90
            List.of(fp(2, 1), fp(3, 1), fp(5, 2)), // 150
            List.of(fp(3, 2), fp(5, 2)) // 225
        );
        
        return Stream.of(
            arguments(input1, expectedFactorFpLists1),
            arguments(input2, expectedFactorFpLists2),
            arguments(List.of(fp(2, 1)), Collections.emptyList()),
            arguments(
                List.of(fp(2, 4)),
                List.of(List.of(fp(2, 1)), List.of(fp(2, 2)), List.of(fp(2, 3)))
            ),
            arguments(
                List.of(fp(2, 1), fp(3, 1)),
                List.of(List.of(fp(2, 1)), List.of(fp(3, 1)))
            )
        );
    }
}
