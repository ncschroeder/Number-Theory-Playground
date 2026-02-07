package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static numbertheoryplayground.sectionclasses.outer.TwoSquareTheorem.Answer;

class TwoSquareTheoremTests {
    /**
     * At the start of the Answer constructor, there are 2 loops to determine the 1st number ≥ the
     * input that's prime and 1 above a multiple of 4. There are 4 situations that can happen with
     * those loops as follows:
     *
     * 1. The input is a prime number that's 1 above a multiple of 4, so none of the loops have to
     *    be used.
     * 2. The input is 1 above a multiple of 4 but isn't prime so only the 2nd loop is used.
     * 3. The input is 1, 2, or 3 less than a prime number that's 1 above a multiple of 4, so only
     *    the 1st loop is used.
     * 4. None of the above so both loops are used.
     *
     * I'm not certain that all of these situations need to be tested but it's not gonna do any harm
     * to test them all either so I'll do each one, in the order that they're listed.
     *
     * I got the expected prime nums from https://en.wikipedia.org/wiki/List_of_prime_numbers.
     * I got the expected a's and b's by running the NTP CLI and having it do a Two Square Theorem
     * section calculation but I verified that the sum of the squares of a and b equals the
     * expected prime num.
     */
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        INPUT,  EXPECTED_PRIME_NUM,  EXPECTED_A,  EXPECTED_B
        5,             5,                1,           2
        45,            53,               2,           7
        275,           277,              9,           14
        5_090,         5_101,            50,          51
        """)
    void answer(long input, long expectedPrimeNum, long expectedA, long expectedB) {
        var answer = new Answer(input, "");
        assertEquals(expectedPrimeNum, answer.getPrimeNum());
        assertEquals(expectedA, answer.getA());
        assertEquals(expectedB, answer.getB());
    }
}
