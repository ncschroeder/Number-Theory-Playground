package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.DivisibilityAnswer.*;

class DivisibilityAnswerTests {
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
INPUT,      LAST_2_DIGITS, LAST_3_DIGITS,  SUM_OF_DIGITS,  BLOCKS_OF_3_ALT_SUM_EXPRESSION,  BLOCKS_OF_3_ALT_SUM,  DIGITS_ALT_SUM_EXPRESSION,  DIGITS_ALT_SUM
1_000,           0,             0,              1,                 000 − 1,                         -1,            1 − 0 + 0 − 0,                   1
60_060,          60,            60,             12,                060 − 60,                        0,             6 − 0 + 0 − 6 + 0,               0
200_008,         8,             8,              10,                008 − 200,                       -192,          2 − 0 + 0 − 0 + 0 − 8,           -6
4_695_768,       68,            768,            45,                768 − 695 + 4,                   77,            4 − 6 + 9 − 5 + 7 − 6 + 8,       11
""")
    void rulesData(
        int input,
        int expectedLast2Digits, int expectedLast3Digits, int expectedSumOfDigits,
        String expectedBlocksOf3AltSumExpression, int expectedBlocksOf3AltSum,
        String expectedDigitsAltSumExpression, int expectedDigitsAltSum
    ) {
        var data = new RulesData(input);
        AlternatingSumAndExpression blocksOf3AltSumAndExpression =
            data.getBlocksOf3AltSumAndExpression();
        AlternatingSumAndExpression digitsAltSumAndExpression =
            data.getDigitsAltSumAndExpression();
        
        assertAll(
            () -> assertEquals(expectedLast2Digits, data.getLast2Digits()),
            () -> assertEquals(expectedLast3Digits, data.getLast3Digits()),
            () -> assertEquals(expectedSumOfDigits, data.getSumOfDigits()),
            () -> assertEquals(expectedBlocksOf3AltSumExpression, blocksOf3AltSumAndExpression.getExpression()),
            () -> assertEquals(expectedBlocksOf3AltSum, blocksOf3AltSumAndExpression.getSum()),
            () -> assertEquals(expectedDigitsAltSumExpression, digitsAltSumAndExpression.getExpression()),
            () -> assertEquals(expectedDigitsAltSum, digitsAltSumAndExpression.getSum())
        );
    }
    
    
    @ParameterizedTest
    @FieldSource("argsForPfAnswerNumFactorsFields")
    void pfAnswerNumFactorsFields(int input, String expectedNumFactorsExpression, int expectedNumFactors) {
        var answer = new PrimeFactorizationAnswer(new PrimeFactorization(input));
        assertAll(
            () -> assertEquals(expectedNumFactorsExpression, answer.getNumFactorsExpression()),
            () -> assertEquals(expectedNumFactors, answer.getNumFactors())
        );
    }
    
    static final List<Arguments> argsForPfAnswerNumFactorsFields =
        List.of(
            arguments(2 * 2, "(2 + 1)", 3),
            arguments(2 * 3, "(1 + 1) × (1 + 1)", 2 * 2),
            arguments(2 * 3 * 3 * 5 * 5 * 5, "(1 + 1) × (2 + 1) × (3 + 1)", 2 * 3 * 4)
        );
}