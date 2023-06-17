import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.nicholasschroeder.numbertheoryplayground.PrimeFactorization;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.nicholasschroeder.numbertheoryplayground.GcdAndLcm.*;

/**
 * Has tests for code in the GcdAndLcm class and the PrimeFactorization.GcdAndLcmInfo class.
 */
class GcdAndLcmTest {
    /*
    There are 4 situations I can think of when calculating the GCD and LCM for 2 ints
    1. Both ints are the same so the GCD and LCM is that int.
    2. 1 of the ints is a multiple of the other int. The GCD will be the smaller of the ints and the LCM
       will be the other int.
    3. The 2 ints have some common prime factors in their prime factorizations so the GCD is > 1 && < the
       smaller int and the LCM is > the bigger int && < the product of the 2 ints.
    4. The 2 ints have no common prime factors so the GCD is 1 and the LCM is the product of the 2 ints. The
       term to describe 2 ints like this is "coprime".
    */
    
    /**
     * Shortened constructor for a EuclideanIteration
     */
    static EuclideanIteration ei(int max, int min, int remainder) {
        return new EuclideanIteration(max, min, remainder);
    }
    
    @Test
    void euclideanIterationEquals() {
        assertTrue(ei(1, 2, 3).equals(ei(1, 2, 3)));
    }
    
    @ParameterizedTest
    @MethodSource("getEuclideanArgs")
    void testGetEuclideanIterations(int input1, int input2, List<EuclideanIteration> expectedIterations) {
        assertEquals(expectedIterations, getEuclideanIterations(input1, input2));
    }
    
    static Stream<Arguments> getEuclideanArgs() {
        return Stream.of(
            arguments(10, 10, List.of(ei(10, 10, 0))),
            arguments(10, 5, List.of(ei(10, 5, 0))),
            arguments(6, 35, List.of(ei(35, 6, 5), ei(6, 5, 1), ei(5, 1, 0))),
            arguments(4_410, 2_100, List.of(ei(4_410, 2_100, 210), ei(2_100, 210, 0))),
            arguments(120, 4_235, List.of(ei(4_235, 120, 35), ei(120, 35, 15), ei(35, 15, 5), ei(15, 5, 0)))
        );
    }
    

    @ParameterizedTest
    @MethodSource("getPfArgs")
    @DisplayName("PrimeFactorization.GcdAndLcmInfo class for non-coprime ints")
    void pfGcdAndLcmInfo(
        int input1,
        List<Map.Entry<Integer, Integer>> expectedInput1PfFactorsAndPowers,
        int input2,
        List<Map.Entry<Integer, Integer>> expectedInput2PfFactorsAndPowers,
        int expectedGcd,
        List<Map.Entry<Integer, Integer>> expectedGcdPfFactorsAndPowers,
        int expectedLcm,
        List<Map.Entry<Integer, Integer>> expectedLcmPfFactorsAndPowers
    ) {
        var info = new PrimeFactorization.GcdAndLcmInfo(input1, input2);
        
        // Assume these are true instead of asserting since PrimeFactorizationTest tests the creation
        // of PrimeFactorizations, which is what the GcdAndLcmInfo constructor will do with the input ints
        assumeTrue(info.int1Pf.getFactorsAndPowers().equals(expectedInput1PfFactorsAndPowers));
        assumeTrue(info.int2Pf.getFactorsAndPowers().equals(expectedInput2PfFactorsAndPowers));
    
        Consumer<PrimeFactorization> assertGcd = gcdPf ->
            assertAll(
                () -> {
                    assertEquals(
                        expectedGcdPfFactorsAndPowers,
                        gcdPf.getFactorsAndPowers()
                    );
                    assertEquals(expectedGcd, gcdPf.getCorrespondingInt());
                },
                () -> {
                    assertEquals(gcdPf.toString(), info.getGcdPfStringOrDefault());
                    assertEquals(gcdPf.getCorrespondingIntString(), info.getGcdString());
                }
            );
    
        assertAll(
            () -> info.gcdPf.ifPresentOrElse(
                assertGcd,
                () -> fail(String.format("GCD PF doesn't exist for %d and %d", input1, input2))
            ),
            () -> {
                assertEquals(
                    expectedLcmPfFactorsAndPowers,
                    info.lcmPf.getFactorsAndPowers()
                );
                assertEquals(expectedLcm, info.lcmPf.getCorrespondingInt());
            }
        );
    }
    
    /**
     * Shortened constructor for a Map.Entry.
     */
    static Map.Entry<Integer, Integer> e(int a, int b) {
        return new AbstractMap.SimpleImmutableEntry<>(a, b);
    }
    
    static Stream<Arguments> getPfArgs() {
        List<Map.Entry<Integer, Integer>> pfFactorsAndPowersFor10 = List.of(e(2, 1), e(5, 1));
        return Stream.of(
            arguments(10, pfFactorsAndPowersFor10, 10, pfFactorsAndPowersFor10, 10, pfFactorsAndPowersFor10, 10, pfFactorsAndPowersFor10),
            arguments(
                2_100,  List.of(e(2, 2), e(3, 1), e(5, 2), e(7, 1)),
                4_410,  List.of(e(2, 1), e(3, 2), e(5, 1), e(7, 2)),
                210,    List.of(e(2, 1), e(3, 1), e(5, 1), e(7, 1)),
                44_100, List.of(e(2, 2), e(3, 2), e(5, 2), e(7, 2))
            ),
            arguments(
                120,     List.of(e(2, 3), e(3, 1), e(5, 1)),
                4_235,   List.of(e(5, 1), e(7, 1), e(11, 2)),
                5,       List.of(e(5, 1)),
                101_640, List.of(e(2, 3), e(3, 1), e(5, 1), e(7, 1), e(11, 2))
            )
        );
    }
    

    /**
     * Situation 4 described at the top.
     */
    @Test
    @DisplayName("PrimeFactorization.GcdAndLcmInfo class for coprime ints")
    void coprimePfGcdAndLcmInfo() {
        int input1 = 6;
        List<Map.Entry<Integer, Integer>> expectedInput1PfFactorsAndPowers =
            List.of(e(2, 1), e(3, 1));
        
        int input2 = 35;
        List<Map.Entry<Integer, Integer>> expectedInput2PfFactorsAndPowers =
            List.of(e(5, 1), e(7, 1));
    
        List<Map.Entry<Integer, Integer>> expectedLcmPfFactorsAndPowers =
            List.of(e(2, 1), e(3, 1), e(5, 1), e(7, 1));
        int expectedLcm = 210;
        
        var info = new PrimeFactorization.GcdAndLcmInfo(input1, input2);

        // Assume these are true instead of asserting since PrimeFactorizationTest tests the creation
        // of PrimeFactorizations, which is what the GcdAndLcmInfo constructor will do with the input ints
        assumeTrue(info.int1Pf.getFactorsAndPowers().equals(expectedInput1PfFactorsAndPowers));
        assumeTrue(info.int2Pf.getFactorsAndPowers().equals(expectedInput2PfFactorsAndPowers));
    
        assertAll(
            () -> {
                assertTrue(info.gcdPf.isEmpty());
                assertEquals("N/A", info.getGcdPfStringOrDefault());
                assertEquals("1", info.getGcdString());
            },
            () -> {
                assertEquals(
                    expectedLcmPfFactorsAndPowers,
                    info.lcmPf.getFactorsAndPowers()
                );
                assertEquals(expectedLcm, info.lcmPf.getCorrespondingInt());
            }
        );
    }
}