package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.sectionclasses.outer.GcdAndLcm.*;
import static numbertheoryplayground.sectionclasses.outer.PrimeFactorization.FactorAndPower;

/**
 * Has tests for code in the GcdAndLcm class.
 */
class GcdAndLcmTests {
    /*
    There are 4 situations I can think of when calculating the GCD and LCM for 2 ints:
    1. Both ints are the same so the GCD and LCM is that int.
    2. 1 of the ints is a multiple of the other int. The GCD will be the smaller of the ints and
       the LCM will be the other int.
    3. The 2 ints have some common prime factors in their prime factorizations so the GCD is > 1
       && < the smaller int and the LCM is > the bigger int && < the product of the 2 ints.
    4. The 2 ints have no common prime factors so the GCD is 1 and the LCM is the product of the
       2 ints. The term to describe 2 ints like this is coprime.
     */

    @ParameterizedTest
    @FieldSource("argsForGetEuclideanIterations")
    void getEuclideanIterations(int input1, int input2, List<EuclideanIteration> expectedIterations) {
        assertEquals(expectedIterations, GcdAndLcm.getEuclideanIterations(input1, input2));
    }
    
    static EuclideanIteration ei(int max, int min, int remainder) {
        return new EuclideanIteration(max, min, remainder);
    }

    static final List<Arguments> argsForGetEuclideanIterations =
        List.of(
            arguments(10, 10, List.of(ei(10, 10, 0))),
            arguments(10, 5, List.of(ei(10, 5, 0))),
            arguments(6, 35, List.of(ei(35, 6, 5), ei(6, 5, 1), ei(5, 1, 0))),
            arguments(99, 54, List.of(ei(99, 54, 45), ei(54, 45, 9), ei(45, 9, 0))),
            arguments(4_410, 2_100, List.of(ei(4_410, 2_100, 210), ei(2_100, 210, 0)))
        );
    
    
    static FactorAndPower fp(long factor, int power) {
        return new FactorAndPower(factor, power);
    }
    
    static int pow(int a, int b) {
        return (int) Math.pow(a, b);
    }
    
    void assertPf(
        PrimeFactorization pf,
        List<FactorAndPower> expectedFps,
        int expectedCorrespondingInt
    ) {
        assertEquals(expectedFps, pf.getFps());
        assertEquals(expectedCorrespondingInt, pf.getCorrespondingBigInt().intValueExact());
    }
    
    /**
     * 2 integers are coprime if their GCD is 1, so if a PrimeFactorizationAnswer object is
     * created with coprime inputs, then the getGcdPf method of that object should return an
     * empty Optional.
     */
    @Test
    void pfAnswerForCoprimeInputs() {
        int input1 = 2 * 3;
        int input2 = 5 * 7;
        List<FactorAndPower> expectedLcmFps =
            List.of(fp(2, 1), fp(3, 1), fp(5, 1), fp(7, 1));
        int expectedLcm = 2 * 3 * 5 * 7;
        var answer = new PrimeFactorizationAnswer(input1, input2, "", "");
        
        assertAll(
            () -> assertTrue(answer.getGcdPf().isEmpty(), "GCD PF is empty."),
            () -> assertPf(answer.getLcmPf(), expectedLcmFps, expectedLcm)
        );
    }
    
    @ParameterizedTest
    @MethodSource("getArgsForGcdAndLcmAnswerForNonCoprimeInputs")
    void pfAnswerForNonCoprimeInputs(
        int input1,
        int input2,
        List<FactorAndPower> expectedGcdFps,
        int expectedGcd,
        List<FactorAndPower> expectedLcmFps,
        int expectedLcm
    ) {
        var answer = new PrimeFactorizationAnswer(input1, input2, "", "");
        
        assertAll(
            () -> {
                answer.getGcdPf().ifPresentOrElse(
                    pf -> assertPf(pf, expectedGcdFps, expectedGcd),
                    () -> fail("GCD PF is empty but shouldn't have been.")
                );
            },
            () -> assertPf(answer.getLcmPf(), expectedLcmFps, expectedLcm)
        );
    }
    
    static Stream<Arguments> getArgsForGcdAndLcmAnswerForNonCoprimeInputs() {
        List<PrimeFactorization.FactorAndPower> fpsFor10 = List.of(fp(2, 1), fp(5, 1));
        
        return Stream.of(
            arguments(10, 10, fpsFor10, 10, fpsFor10, 10),
            arguments(
                2 * pow(3, 3),
                pow(3, 2) * 11,
                List.of(fp(3, 2)),
                9,
                List.of(fp(2, 1), fp(3, 3), fp(11, 1)),
                594
            ),
            arguments(
                pow(2, 2) * 3 * pow(5, 2) * 7,
                2 * pow(3, 2) * 5 * pow(7, 2),
                List.of(fp(2, 1), fp(3, 1), fp(5, 1), fp(7, 1)),
                2 * 3 * 5 * 7,
                List.of(fp(2, 2), fp(3, 2), fp(5, 2), fp(7, 2)),
                pow(2, 2) * pow(3, 2) * pow(5, 2) * pow(7, 2)
            )
        );
    }
}
