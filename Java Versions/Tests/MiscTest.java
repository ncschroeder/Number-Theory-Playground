import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static numbertheoryplayground.Misc.*;

/**
 * Has tests for code in the Misc class, except for getLongAndSquareString. That method will be tested
 * in the PythagoreanTriplesTest class since the string conversion of PythagoreanTriple objects use
 * that method.
 */
class MiscTest {
    @ParameterizedTest
    @CsvSource({
        "1,         '1'",
        "10,        '10'",
        "100,       '100'",
        "1_000,     '1,000'",
        "10_000,    '10,000'",
        "100_000,   '100,000'",
        "1_000_000, '1,000,000'"
    })
    void testStringifyWithCommas(int input, String expectedOutput) {
        assertEquals(expectedOutput, stringifyWithCommas(input));
    }
    
    @Test
    void testGetWhiteSpace() {
        assertEquals("     ", getWhiteSpace(5));
    }
    
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    @DisplayName("assertIsInRange doesn't throw when given an int in the range specified")
    void assertIsInRangeDoesntThrow(int i) {
        assertDoesNotThrow(() -> assertIsInRange(i, 1, 3));
    }
    
    @ParameterizedTest
    @ValueSource(ints = { 0, 4 })
    @DisplayName("assertIsInRange throws when given an int out of the range specified")
    void assertIsInRangeThrows(int i) {
        assertThrows(IllegalArgumentException.class, () -> assertIsInRange(i, 1, 3));
    }
}