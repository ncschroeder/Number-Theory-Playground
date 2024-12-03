import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import numbertheoryplayground.sectionclasses.outer.PrimeFactorization;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Has tests for code in the PrimeFactorization class.
 */
class PrimeFactorizationTest {
    static Map.Entry<Long, Integer> me(long a, int b) {
        return Map.entry(a, b);
    }
    
    static int pow(int a, int b) {
        return (int) Math.pow(a, b);
    }
    
    @ParameterizedTest
    @MethodSource("getArgsForPfGetFactorsAndPowers")
    void pfGetFactorsAndPowers(int input, List<Map.Entry<Long, Integer>> expectedFactorsAndPowers) {
        assertEquals(expectedFactorsAndPowers, new PrimeFactorization(input).getFactorsAndPowers());
    }
    
    static Stream<Arguments> getArgsForPfGetFactorsAndPowers() {
        return Stream.of(
            arguments(2, List.of(me(2, 1))),
            arguments(pow(2, 3), List.of(me(2, 3))),
            arguments(3, List.of(me(3, 1))),
            arguments(2 * 3 * 5 * 7, List.of(me(2, 1), me(3, 1), me(5, 1), me(7, 1))),
            arguments(2 * pow(5, 3) * pow(7, 2), List.of(me(2, 1), me(5, 3), me(7, 2))),
            arguments(pow(2, 4) * 7, List.of(me(2, 4), me(7, 1))),
            arguments(pow(3, 3) * pow(5, 2) * 7, List.of(me(3, 3), me(5, 2), me(7, 1))),
            arguments(pow(5, 2) * pow(17, 2), List.of(me(5, 2), me(17, 2))),
            arguments(pow(13, 3), List.of(me(13, 3))),
            arguments(1_213, List.of(me(1_213, 1)))
        );
    }
    
    @ParameterizedTest
    @MethodSource("getArgsForPfToString")
    void pfToString(int input, String expectedString) {
        assertEquals(expectedString, new PrimeFactorization(input).toString());
    }
    
    static Stream<Arguments> getArgsForPfToString() {
        return Stream.of(
            arguments(1_213, "1,213"),
            arguments(pow(3, 3) * pow(5, 2) * 7, "3^3 x 5^2 x 7")
        );
    }
    
    @ParameterizedTest
    @MethodSource("getArgsForPfGetFactorPfs")
    void pfGetFactorPfs(
        Map<Long, Integer> input,
        List<List<Map.Entry<Long, Integer>>> expectedFactorsAndPowersForFactorPfs
    ) {
        List<List<Map.Entry<Long, Integer>>> actualFactorsAndPowersForFactorPfs =
            new PrimeFactorization(input)
            .getFactorPfs()
            .stream()
            .map(PrimeFactorization::getFactorsAndPowers)
            .toList();
        
        assertEquals(expectedFactorsAndPowersForFactorPfs, actualFactorsAndPowersForFactorPfs);
    }
    
    static Stream<Arguments> getArgsForPfGetFactorPfs() {
        Map<Long, Integer> input4 = Map.ofEntries(me(2, 2), me(3, 1));
        List<List<Map.Entry<Long, Integer>>> expectedFactorsAndPowersForFactorPfs4 =
            List.of(
                List.of(me(2, 1)),
                List.of(me(3, 1)),
                List.of(me(2, 2)),
                List.of(me(2, 1), me(3, 1))
            );
        
        Map<Long, Integer> input5 = Map.ofEntries(me(2, 1), me(3, 2), me(5, 2));
        List<List<Map.Entry<Long, Integer>>> expectedFactorsAndPowersForFactorPfs5 =
            List.of(
                // The comments show the corresponding factor.
                List.of(me(2, 1)), // 2
                List.of(me(3, 1)), // 3
                List.of(me(5, 1)), // 5
                List.of(me(2, 1), me(3, 1)), // 6
                List.of(me(3, 2)), // 9
                List.of(me(2, 1), me(5, 1)), // 10
                List.of(me(3, 1), me(5, 1)), // 15
                List.of(me(2, 1), me(3, 2)), // 18
                List.of(me(5, 2)), // 25
                List.of(me(2, 1), me(3, 1), me(5, 1)), // 30
                List.of(me(3, 2), me(5, 1)), // 45
                List.of(me(2, 1), me(5, 2)), // 50
                List.of(me(3, 1), me(5, 2)), // 75
                List.of(me(2, 1), me(3, 2), me(5, 1)), // 90
                List.of(me(2, 1), me(3, 1), me(5, 2)), // 150
                List.of(me(3, 2), me(5, 2)) // 225
            );
        
        return Stream.of(
            arguments(Map.of(2L, 1), Collections.emptyList()),
            arguments(
                Map.of(2L, 4),
                List.of(List.of(me(2, 1)), List.of(me(2, 2)), List.of(me(2, 3)))
            ),
            arguments(
                Map.ofEntries(me(2, 1), me(3, 1)),
                List.of(List.of(me(2, 1)), List.of(me(3, 1)))
            ),
            arguments(input4, expectedFactorsAndPowersForFactorPfs4),
            arguments(input5, expectedFactorsAndPowersForFactorPfs5)
        );
    }
    
    /**
     * 2 integers are coprime if their GCD is 1 and they don't have any common prime factors. If
     * input1 and input2 are coprime, then expectedGcdPfFactorsAndPowers and expectedGcd will be null.
     */
    @ParameterizedTest
    @MethodSource("getArgsForGcdAndLcmAnswer")
    void gcdAndLcmAnswer(
        int input1,
        int input2,
        List<Map.Entry<Long, Integer>> expectedGcdPfFactorsAndPowers,
        Integer expectedGcd,
        List<Map.Entry<Long, Integer>> expectedLcmPfFactorsAndPowers,
        int expectedLcm
    ) {
        var answer = new PrimeFactorization.GcdAndLcmAnswer(input1, input2);

        assertAll(
            () -> {
                if (expectedGcdPfFactorsAndPowers == null) {
                    assertNull(answer.gcdPf);
                } else {
                    assertNotNull(answer.gcdPf);
                    assertEquals(expectedGcdPfFactorsAndPowers, answer.gcdPf.getFactorsAndPowers());
                    assertEquals(expectedGcd, answer.gcdPf.getCorrespondingBigInt().intValueExact());
                }
            },
            () -> {
                assertEquals(expectedLcmPfFactorsAndPowers, answer.lcmPf.getFactorsAndPowers());
                assertEquals(expectedLcm, answer.lcmPf.getCorrespondingBigInt().intValueExact());
            }
        );
    }
    
    static Stream<Arguments> getArgsForGcdAndLcmAnswer() {
        List<Map.Entry<Long, Integer>> pfFactorsAndPowersFor10 = List.of(me(2, 1), me(5, 1));
        
        return Stream.of(
            arguments(10, 10, pfFactorsAndPowersFor10, 10, pfFactorsAndPowersFor10, 10),
            arguments(
                2 * 3,
                5 * 7,
                null,
                null,
                List.of(me(2, 1), me(3, 1), me(5, 1), me(7, 1)),
                2 * 3 * 5 * 7
            ),
            arguments(
                pow(2, 2) * 3 * pow(5, 2) * 7,
                2 * pow(3, 2) * 5 * pow(7, 2),
                List.of(me(2, 1), me(3, 1), me(5, 1), me(7, 1)),
                2 * 3 * 5 * 7,
                List.of(me(2, 2), me(3, 2), me(5, 2), me(7, 2)),
                pow(2, 2) * pow(3, 2) * pow(5, 2) * pow(7, 2)
            ),
            arguments(
                pow(2, 3) * 3 * 5,
                5 * 7 * pow(11, 2),
                List.of(me(5, 1)),
                5,
                List.of(me(2, 3), me(3, 1), me(5, 1), me(7, 1), me(11, 2)),
                pow(2, 3) * 3 * 5 * 7 * pow(11, 2)
            )
        );
    }
}