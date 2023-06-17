import com.nicholasschroeder.numbertheoryplayground.PrimeFactorization;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Has tests for code in the PrimeFactorization class.
 */
class PrimeFactorizationTest {
    @ParameterizedTest
    @MethodSource("getFactorsAndPowersArgs")
    void getFactorsAndPowers(int input, List<Map.Entry<Integer, Integer>> expectedFactorsAndPowers) {
        assertEquals(expectedFactorsAndPowers, new PrimeFactorization(input).getFactorsAndPowers());
    }
    
    /**
     * Shortened constructor for a Map.Entry
     */
    static Map.Entry<Integer, Integer> e(int a, int b) {
        return new AbstractMap.SimpleImmutableEntry<>(a, b);
    }
    
    static Stream<Arguments> getFactorsAndPowersArgs() {
        return Stream.of(
            arguments(2, List.of(e(2, 1))),
            arguments(3, List.of(e(3, 1))),
            arguments(6, List.of(e(2, 1), e(3, 1))),
            arguments(8, List.of(e(2, 3))),
            arguments(27, List.of(e(3, 3))),
            arguments(12, List.of(e(2, 2), e(3, 1))),
            arguments(210, List.of(e(2, 1), e(3, 1), e(5, 1), e(7, 1))),
            arguments(110, List.of(e(2, 1), e(5, 1), e(11, 1))),
            arguments(1_155, List.of(e(3, 1), e(5, 1), e(7, 1), e(11, 1))),
            arguments(56, List.of(e(2, 3), e(7, 1))),
            arguments(686, List.of(e(2, 1), e(7, 3))),
            arguments(360, List.of(e(2, 3), e(3, 2), e(5, 1))),
            arguments(2_250, List.of(e(2, 1), e(3, 2), e(5, 3))),
            arguments(4_725, List.of(e(3, 3), e(5, 2), e(7, 1))),
            arguments(2_704, List.of(e(2, 4), e(13, 2))),
            arguments(7_225, List.of(e(5, 2), e(17, 2))),
            arguments(107, List.of(e(107, 1))),
            arguments(1_213, List.of(e(1_213, 1)))
        );
    }
    
    
    @ParameterizedTest
    @CsvSource({
        "2_520, '2^3 x 3^2 x 5 x 7 (2,520)'",
        "1_213, '1,213'"
    })
    void toStringWithCorrespondingInt(int input, String expectedString) {
        assertEquals(expectedString, new PrimeFactorization(input).toStringWithCorrespondingInt());
    }
}