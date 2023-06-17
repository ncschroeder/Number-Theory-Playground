import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.nicholasschroeder.numbertheoryplayground.Divisibility.*;

/**
 * Has tests for code in the Divisibility class.
 */
class DivisibilityTest {
    @ParameterizedTest
    @ValueSource(ints = { 10, 12 })
    void testIsEven(int i) {
        assertTrue(isEven(i));
    }
    
    @ParameterizedTest
    @ValueSource(ints = { 11, 13 })
    void testIsOdd(int i) {
        assertTrue(isOdd(i));
    }
    

    @ParameterizedTest
    @CsvSource({
        "2,      2,   2,   2",
        "14,     5,   14,  14",
        "330,    6,   30,  330",
        "6_742,  19,  42,  742",
        "1_000,  1,   0,   0",
        "2_009,  11,  9,   9",
        "5_012,  8,   12,  12"
    })
    @DisplayName("TricksInfo methods that return ints")
    void tricksInfoIntMethods(int input, int expectedSumOfDigits, int expectedLast2Digits, int expectedLast3Digits) {
        var info = new TricksInfo(input);
        assertAll(
            () -> assertEquals(expectedSumOfDigits, info.getSumOfDigits()),
            () -> assertEquals(expectedLast2Digits, info.getLast2Digits()),
            () -> assertEquals(expectedLast3Digits, info.getLast3Digits())
        );
    }
    

    @ParameterizedTest
    @MethodSource("getTricksInfoDivisibilityArgs")
    @DisplayName("TricksInfo methods that start with \"isDivisibleBy\"")
    void tricksInfoDivisibility(int input) {
        var info = new TricksInfo(input);
        assertAll(
            () -> assertEquals(isDivisible(input, 3), info.isDivisibleBy3()),
            () -> assertEquals(isDivisible(input, 4), info.isDivisibleBy4()),
            () -> assertEquals(isDivisible(input, 6), info.isDivisibleBy6()),
            () -> assertEquals(isDivisible(input, 8), info.isDivisibleBy8()),
            () -> assertEquals(isDivisible(input, 9), info.isDivisibleBy9()),
            () -> assertEquals(isDivisible(input, 12), info.isDivisibleBy12())
        );
    }
    
    /**
     * Returns an IntStream of 3 ranges of ints. Each range starts with the ints passed to IntStream.of
     * and contains 12 ints. 12 is chosen as the range length since it will guarantee that at least 1
     * int in that range is divisible by 12.
     */
    static IntStream getTricksInfoDivisibilityArgs() {
        return
            IntStream.of(2, 173, 6_050)
            .flatMap(i -> IntStream.range(i, i + 12));
    }
    

    /**
     * The getFactors method uses the isDivisible method so getFactors will be used to determine if
     * both of the methods work.
     */
    @ParameterizedTest
    @MethodSource("getFactorsArgs")
    @DisplayName("getFactors and isDivisible methods")
    void testGetFactors(int input, int[] expectedFactors) {
        assertArrayEquals(expectedFactors, getFactors(input).toArray());
    }
    
    static Stream<Arguments> getFactorsArgs() {
        return Stream.of(
            arguments(10, new int[] { 2, 5 }),
            arguments(12, new int[] { 2, 3, 4, 6 }),
            arguments(81, new int[] { 3, 9, 27 }),
            arguments(103, new int[] {}),
            arguments(121, new int[] { 11 }),
            arguments(210, new int[] { 2, 3, 5, 6, 7, 10, 14, 15, 21, 30, 35, 42, 70, 105 }),
            arguments(500, new int[] { 2, 4, 5, 10, 20, 25, 50, 100, 125, 250 }),
            arguments(1_093, new int[] {})
        );
    }
}