package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TwoSquareTheoremAnswerTests {
    /**
     * At the start of the TwoSquareTheoremAnswer constructor, there are 2 loops to determine
     * the first Pythagorean prime ≥ the input. There are 4 situations that can happen with
     * those loops as follows:
     *
     * 1. The input is a Pythagorean prime, so none of the loops have to be used.
     * 2. The input is 1 above a multiple of 4 but isn't prime so only the second loop is used.
     * 3. The input is 1, 2, or 3 less than a prime number that's 1 above a multiple of 4, so
     *    only the first loop is used.
     * 4. None of the above so both loops are used.
     *
     * I'm not certain that all of these situations need to be tested but it's not gonna do
     * any harm to test them all either so I'll do each one, in the order that they're listed.
     *
     * I got the expected Pythagorean primes from https://en.wikipedia.org/wiki/List_of_prime_numbers.
     * I got the expected a's and b's by running the NTP CLI and having it do a Two Square
     * Theorem section calculation but I verified that the sum of the squares of a and b
     * equals the expected Pythagorean prime.
     */
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        INPUT,  EXPECTED_PYTHAG_PRIME,  EXPECTED_A,  EXPECTED_B
        5,               5,                 1,           2
        45,              53,                2,           7
        275,             277,               9,           14
        5_090,           5_101,             50,          51
        """)
    void allMethods(int input, int expectedPythagPrime, int expectedA, int expectedB) {
        var answer = new TwoSquareTheoremAnswer(input);
        assertEquals(expectedPythagPrime, answer.getPythagPrime());
        assertEquals(expectedA, answer.getA());
        assertEquals(expectedB, answer.getB());
    }
}