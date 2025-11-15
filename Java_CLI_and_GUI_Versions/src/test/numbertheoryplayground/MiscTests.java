package numbertheoryplayground;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

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
    void createStringWithCommas(int input, String expectedString) {
        assertEquals(expectedString, Misc.createStringWithCommas(input));
    }

    @Test
    void stripCommasAndParse() {
        assertAll(
            () -> {
                assertEquals(1, Misc.stripCommasAndParse("1"));
                assertEquals(1, Misc.stripCommasAndParse("1,"));
            },
            () -> assertThrows(IllegalArgumentException.class, () -> Misc.stripCommasAndParse("abc"))
        );
    }
}
