package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static numbertheoryplayground.sectionclasses.outer.Divisibility.RulesAnswer;

/**
 * Has tests for code in the Divisibility class.
 */
class DivisibilityTests {
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
INPUT,      LAST_2_DIGITS,  LAST_3_DIGITS,  SUM_OF_DIGITS,  BLOCKS_OF_3_ALT_SUM_EXPRESSION,  DIGITS_ALT_SUM_EXPRESSION
1_000,           0,              0,              1,               000 − 1 = -1,              1 − 0 + 0 − 0 = 1
60_060,          60,             60,             12,              060 − 60 = 0,              6 − 0 + 0 − 6 + 0 = 0
200_008,         8,              8,              10,              008 − 200 = -192,          2 − 0 + 0 − 0 + 0 − 8 = -6
4_695_768,       68,             768,            45,              768 − 695 + 4 = 77,        4 − 6 + 9 − 5 + 7 − 6 + 8 = 11
""")
    void rulesAnswer(
        int input,
        int expectedLast2Digits,
        int expectedLast3Digits,
        int expectedSumOfDigits,
        String expectedBlocksOf3AltSumExpression,
        String expectedDigitsAltSumExpression
    ) {
        var answer = new RulesAnswer(input);
        assertAll(
            () -> assertEquals(expectedLast2Digits, answer.getLast2Digits()),
            () -> assertEquals(expectedLast3Digits, answer.getLast3Digits()),
            () -> assertEquals(expectedSumOfDigits, answer.getSumOfDigits()),
            () -> assertEquals(expectedBlocksOf3AltSumExpression, answer.getBlocksOf3AltSumExpression()),
            () -> assertEquals(expectedDigitsAltSumExpression, answer.getDigitsAltSumExpression())
        );
    }
}
