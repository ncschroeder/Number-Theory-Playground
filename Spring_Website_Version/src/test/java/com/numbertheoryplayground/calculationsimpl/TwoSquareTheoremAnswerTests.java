package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class TwoSquareTheoremAnswerTests {
    /**
     * At the start of the TwoSquareTheoremAnswer constructor, there are 2 loops to determine
     * the 1st number >= the input that's prime and is 1 above a multiple of 4. There are 4
     * situations that can happen with those loops as follows:
     *
     * 1. The input is a prime number that's 1 above a multiple of 4, so none of the loops
     *    have to be used.
     * 2. The input is 1 above a multiple of 4 but isn't prime so only the 2nd loop is used.
     * 3. The input is 1, 2, or 3 less than a prime number that's 1 above a multiple of 4, so
     *    only the 1st loop is used.
     * 4. None of the above so both loops are used.
     *
     * I'm not certain that all of these situations need to be tested but it's not gonna do
     * any harm to test them all either so I'll do each one, in the order that they're listed.
     */
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        INPUT,    EXPECTED_PRIME_NUM,  EXPECTED_A,  EXPECTED_B
        5,             5,                  1,           2
        273,           277,                9,           14
        5_098,         5_101,              50,          51
        340_000,       340_037,            166,         559
        """)
    void twoSquareTheoremAnswer(int input, int expectedPrimeNum, int expectedA, int expectedB) {
        var answer = new TwoSquareTheoremAnswer(input);
        assertEquals(expectedPrimeNum, answer.primeNum);
        assertEquals(expectedA, answer.a);
        assertEquals(expectedB, answer.b);
    }
}