import com.nicholasschroeder.numbertheoryplayground.TwoSquareTheorem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Has tests for code in the TwoSquareTheorem class.
 */
class TwoSquareTheoremTest {
    /**
     * At the start of the TwoSquareTheorem.Info constructor, there are 2 loops to determine the
     * 1st number >= the param number that is prime and 1 above a multiple of 4. There are 4 situations
     * that can happen with those loops as follows:
     * 1. The param is a prime number that is 1 above a multiple of 4, so no looping has to be done.
     * 2. The param is 1 above multiple of 4 but is not prime so only the 2nd loop is used.
     * 3. The 1st integer > the param that is 1 above a multiple of 4 is prime so only the 1st loop is used.
     * 4. None of the above so both loops are used.
     *
     * I'm not certain that all of these situations need to be tested but it's not gonna do any harm
     * to test all these situations either so I'll do each one twice.
     *
     * input1 and input2 will be 2 ints that end up finding the same prime number in the
     * TwoSquareTheorem.Info constructor.
     */
    @ParameterizedTest
    @CsvSource({
        // Situations tested are in comments
        "5,        2,        5,        1,     2",  // 1 and 3
        "249,      242,      257,      1,     16", // 2 and 4
        "5_101,    5_087,    5_101,    50,    51", // 1 and 4
        "340_033,  340_036,  340_037,  166,   559" // 2 and 3
    })
    @DisplayName("Info objects generate correct prime number and ints whose squares sum to that prime number")
    void infoObject(int input1, int input2, int expectedPrimeNumber, int expectedInt1, int expectedInt2) {
        var info1 = new TwoSquareTheorem.Info(input1);
        var info2 = new TwoSquareTheorem.Info(input2);
        
        assertAll(
            () -> {
                assertEquals(expectedPrimeNumber, info1.getPrimeNumber());
                assertEquals(expectedInt1, info1.getInt1());
                assertEquals(expectedInt2, info1.getInt2());
            },
            () -> assertEquals(expectedPrimeNumber, info2.getPrimeNumber())
        );
    }
}