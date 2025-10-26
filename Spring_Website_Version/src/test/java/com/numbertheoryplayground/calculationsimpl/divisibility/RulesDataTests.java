package com.numbertheoryplayground.calculationsimpl.divisibility;

import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class RulesDataTests {
    /**
     * The minus sign used in the alt sum expressions is a special math char that's the
     * same as MINUS_SIGN in the AlternatingSumAndExpression class.
     *
     * 5,544 is the number used as an example in the section info and in the RulesData
     * class. It's divisible by all numbers that had rules mentioned about them;
     * 3, 4, 6, 7, 8, 9, and 11.
     */
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
INPUT,  LAST_2_DIGITS,  LAST_3_DIGITS,  SUM_OF_DIGITS,  BLOCKS_OF_3_ALT_SUM_EXPRESSION,  BLOCKS_OF_3_ALT_SUM,  DIGITS_ALT_SUM_EXPRESSION,  DIGITS_ALT_SUM
1_000,        0,             0,              1,                    000 − 1,                      -1,                 1 − 0 + 0 − 0,              1
2_008,        8,             8,              10,                   008 − 2,                      6,                  2 − 0 + 0 − 8,              -6
5_544,        44,            544,            18,                   544 − 5,                      539,                5 − 5 + 4 − 4,              0
6_060,        60,            60,             12,                   060 − 6,                      54,                 6 − 0 + 6 − 0,              12
""")
    void allMethods(
        int input,
        int expectedLast2Digits,
        int expectedLast3Digits,
        int expectedSumOfDigits,
        String expectedBlocksOf3AltSumExpression,
        int expectedBlocksOf3AltSum,
        String expectedDigitsAltSumExpression,
        int expectedDigitsAltSum
    ) {
        var data = new RulesData(input);
        
        Executable assertBlocksOf3AltSumAndExpression = () -> {
            var blocksOf3AltSumAndExpression = data.getBlocksOf3AltSumAndExpression();
            // All the inputs are ≥ 1,000 so this shouldn't be null.
            assertNotNull(blocksOf3AltSumAndExpression);
            
            assertAll(
                () -> assertEquals(
                    expectedBlocksOf3AltSumExpression,
                    blocksOf3AltSumAndExpression.getExpression()
                ),
                () -> assertEquals(
                    expectedBlocksOf3AltSum,
                    blocksOf3AltSumAndExpression.getSum()
                )
            );
        };
        
        var digitsAltSumAndExpression = data.getDigitsAltSumAndExpression();
        
        assertAll(
            () -> assertEquals(expectedLast2Digits, data.getLast2Digits()),
            () -> assertEquals(expectedLast3Digits, data.getLast3Digits()),
            () -> assertEquals(expectedSumOfDigits, data.getSumOfDigits()),
            assertBlocksOf3AltSumAndExpression,
            () -> assertEquals(expectedDigitsAltSumExpression, digitsAltSumAndExpression.getExpression()),
            () -> assertEquals(expectedDigitsAltSum, digitsAltSumAndExpression.getSum())
        );
    }
}