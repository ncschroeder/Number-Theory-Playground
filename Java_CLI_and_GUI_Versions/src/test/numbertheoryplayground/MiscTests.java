package numbertheoryplayground;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static numbertheoryplayground.Misc.*;

/**
 * Has tests for code in the Misc class.
 */
class MiscTests {
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
        INPUT,      EXPECTED_STRING
        1,            1
        10,           10
        100,          100
        1_000,        '1,000'
        10_000,       '10,000'
        100_000,      '100,000'
        1_000_000,    '1,000,000'
        """)
    void testCreateStringWithCommas(int input, String expectedString) {
        assertEquals(expectedString, createStringWithCommas(input));
    }

    @Test
    void testStripCommasAndParse() {
        assertAll(
            () -> {
                assertEquals(1, stripCommasAndParse("1"));
                assertEquals(1, stripCommasAndParse("1,"));
            },
            () -> assertThrows(IllegalArgumentException.class, () -> stripCommasAndParse("abc"))
        );
    }
}
