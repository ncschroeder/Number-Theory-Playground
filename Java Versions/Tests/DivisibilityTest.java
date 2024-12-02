import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static com.nicholasschroeder.numbertheoryplayground.Divisibility.RulesAnswer;

/**
 * Has tests for code in the Divisibility class.
 */
class DivisibilityTest {
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
INPUT,      EXPECTED_SUM_OF_DIGITS,  EXPECTED_LAST_2_DIGITS,  EXPECTED_LAST_3_DIGITS,  EXPECTED_ALTERNATING_SUM_OF_BLOCKS_STRING,  EXPECTED_ALTERNATING_SUM_OF_DIGITS_STRING
1_000,      1,                       0,                       0,                       0 - 1 = -1,                                 1 - 0 + 0 - 0 = 1
2_008,      10,                      8,                       8,                       8 - 2 = 6,                                  2 - 0 + 0 - 8 = -6
60_060,     12,                      60,                      60,                      60 - 60 = 0,                                6 - 0 + 0 - 6 + 0 = 0
4_695_768,  45,                      68,                      768,                     768 - 695 + 4 = 77,                         4 - 6 + 9 - 5 + 7 - 6 + 8 = 11
    """)
    void rulesAnswer(
        int input,
        int expectedSumOfDigits,
        int expectedLast2Digits,
        int expectedLast3Digits,
        String expectedAlternatingSumOfBlocksString,
        String expectedAlternatingSumOfDigitsString
    ) {
        var answer = new RulesAnswer(input);

        assertAll(
            () -> assertEquals(expectedSumOfDigits, answer.sumOfDigits),
            () -> assertEquals(expectedLast2Digits, answer.last2Digits),
            () -> assertEquals(expectedLast3Digits, answer.last3Digits),
            () -> assertEquals(
                expectedAlternatingSumOfBlocksString, answer.alternatingSumOfBlocksSb.toString()
            ),
            () -> assertEquals(
                expectedAlternatingSumOfDigitsString, answer.alternatingSumOfDigitsSb.toString()
            )
        );
    }
}