package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.sectionclasses.outer.PrimeFactorization.*;

/**
 * Has tests for code in the PrimeFactorization class.
 */
class PrimeFactorizationTests {
    static FactorAndPower fp(long factor, int power) {
        return new FactorAndPower(factor, power);
    }

    static int pow(int a, int b) {
        return (int) Math.pow(a, b);
    }

    
    @ParameterizedTest
    @FieldSource("longConstructorArgs")
    void longConstructor(long input, List<FactorAndPower> expectedFactorsAndPowers) {
        assertEquals(expectedFactorsAndPowers, new PrimeFactorization(input).toList());
    }

    static final List<Arguments> longConstructorArgs =
        List.of(
            arguments(2, List.of(fp(2, 1))),
            arguments(pow(2, 3), List.of(fp(2, 3))),
            arguments(3, List.of(fp(3, 1))),
            arguments(2 * 3 * 5 * 7, List.of(fp(2, 1), fp(3, 1), fp(5, 1), fp(7, 1))),
            arguments(2 * pow(5, 3) * pow(7, 2), List.of(fp(2, 1), fp(5, 3), fp(7, 2))),
            arguments(pow(2, 4) * 7, List.of(fp(2, 4), fp(7, 1))),
            arguments(pow(3, 3) * pow(5, 2) * 7, List.of(fp(3, 3), fp(5, 2), fp(7, 1))),
            arguments(pow(5, 2) * pow(17, 2), List.of(fp(5, 2), fp(17, 2))),
            arguments(pow(13, 3), List.of(fp(13, 3))),
            arguments(1_213, List.of(fp(1_213, 1)))
        );

    
    @ParameterizedTest
    @FieldSource("toStringArgs")
    void toString(int input, String expectedString) {
        assertEquals(expectedString, new PrimeFactorization(input).toString());
    }

    static final List<Arguments> toStringArgs =
        List.of(
            arguments(1_213, "1,213"),
            arguments(pow(3, 3) * pow(5, 2) * 7, "3^3 × 5^2 × 7")
        );
    
    
    @ParameterizedTest
    @MethodSource("getArgsForGetFactorPfs")
    void getFactorPfs(
        List<FactorAndPower> input,
        List<List<FactorAndPower>> expectedPfListsForFactorPfs
    ) {
        List<List<FactorAndPower>> actualPfListsForFactorPfs =
            new PrimeFactorization(input)
            .getFactorPfs()
            .stream()
            .map(PrimeFactorization::toList)
            .toList();
        
        assertEquals(expectedPfListsForFactorPfs, actualPfListsForFactorPfs);
    }
    
    static Stream<Arguments> getArgsForGetFactorPfs() {
        List<FactorAndPower> input4 = List.of(fp(2, 2), fp(3, 1));
        List<List<FactorAndPower>> expectedPfListsForFactorPfs4 =
            List.of(
                List.of(fp(2, 1)),
                List.of(fp(3, 1)),
                List.of(fp(2, 2)),
                List.of(fp(2, 1), fp(3, 1))
            );

        List<FactorAndPower> input5 = List.of(fp(2, 1), fp(3, 2), fp(5, 2));
        List<List<FactorAndPower>> expectedPfListsForFactorPfs5 =
            List.of(
                // The comments show the corresponding factor.
                List.of(fp(2, 1)), // 2
                List.of(fp(3, 1)), // 3
                List.of(fp(5, 1)), // 5
                List.of(fp(2, 1), fp(3, 1)), // 6
                List.of(fp(3, 2)), // 9
                List.of(fp(2, 1), fp(5, 1)), // 10
                List.of(fp(3, 1), fp(5, 1)), // 15
                List.of(fp(2, 1), fp(3, 2)), // 18
                List.of(fp(5, 2)), // 25
                List.of(fp(2, 1), fp(3, 1), fp(5, 1)), // 30
                List.of(fp(3, 2), fp(5, 1)), // 45
                List.of(fp(2, 1), fp(5, 2)), // 50
                List.of(fp(3, 1), fp(5, 2)), // 75
                List.of(fp(2, 1), fp(3, 2), fp(5, 1)), // 90
                List.of(fp(2, 1), fp(3, 1), fp(5, 2)), // 150
                List.of(fp(3, 2), fp(5, 2)) // 225
            );

        return Stream.of(
            arguments(List.of(fp(2, 1)), Collections.emptyList()),
            arguments(
                List.of(fp(2, 4)),
                List.of(List.of(fp(2, 1)), List.of(fp(2, 2)), List.of(fp(2, 3)))
            ),
            arguments(
                List.of(fp(2, 1), fp(3, 1)),
                List.of(List.of(fp(2, 1)), List.of(fp(3, 1)))
            ),
            arguments(input4, expectedPfListsForFactorPfs4),
            arguments(input5, expectedPfListsForFactorPfs5)
        );
    }
    
    
    void assertPf(
        PrimeFactorization pf,
        List<FactorAndPower> expectedPfList,
        int expectedCorrespondingInt
    ) {
        assertEquals(expectedPfList, pf.toList());
        assertEquals(expectedCorrespondingInt, pf.getCorrespondingBigInt().intValueExact());
    }
    
    /**
     * 2 integers are coprime if their GCD is 1 and they don't have any common prime factors.
     * As a result, if a GcdAndLcmAnswer object is created with coprime inputs, then the GCD PF
     * of that object should be null.
     */
    @Test
    void gcdAndLcmAnswerForCoprimeInputs() {
        int input1 = 2 * 3;
        int input2 = 5 * 7;
        List<FactorAndPower> expectedLcmPfList =
            List.of(fp(2, 1), fp(3, 1), fp(5, 1), fp(7, 1));
        int expectedLcm = 2 * 3 * 5 * 7;
        var answer = new GcdAndLcmAnswer(input1, input2);
        
        assertAll(
            () -> assertNull(answer.getGcdPf()),
            () -> assertPf(answer.getLcmPf(), expectedLcmPfList, expectedLcm)
        );
    }
    
    @ParameterizedTest
    @MethodSource("getArgsForGcdAndLcmAnswerForNonCoprimeInputs")
    void gcdAndLcmAnswerForNonCoprimeInputs(
        int input1,
        int input2,
        List<FactorAndPower> expectedGcdPfList,
        int expectedGcd,
        List<FactorAndPower> expectedLcmPfList,
        int expectedLcm
    ) {
        var answer = new GcdAndLcmAnswer(input1, input2);

        assertAll(
            () -> {
                assertNotNull(answer.getGcdPf());
                assertPf(answer.getGcdPf(), expectedGcdPfList, expectedGcd);
            },
            () -> assertPf(answer.getLcmPf(), expectedLcmPfList, expectedLcm)
        );
    }

    static Stream<Arguments> getArgsForGcdAndLcmAnswerForNonCoprimeInputs() {
        List<FactorAndPower> pfFactorsAndPowersFor10 =
            List.of(fp(2, 1), fp(5, 1));

        return Stream.of(
            arguments(10, 10, pfFactorsAndPowersFor10, 10, pfFactorsAndPowersFor10, 10),
            arguments(
                pow(2, 2) * 3 * pow(5, 2) * 7,
                2 * pow(3, 2) * 5 * pow(7, 2),
                List.of(fp(2, 1), fp(3, 1), fp(5, 1), fp(7, 1)),
                2 * 3 * 5 * 7,
                List.of(fp(2, 2), fp(3, 2), fp(5, 2), fp(7, 2)),
                pow(2, 2) * pow(3, 2) * pow(5, 2) * pow(7, 2)
            ),
            arguments(
                pow(2, 3) * 3 * 5,
                5 * 7 * pow(11, 2),
                List.of(fp(5, 1)),
                5,
                List.of(fp(2, 3), fp(3, 1), fp(5, 1), fp(7, 1), fp(11, 2)),
                pow(2, 3) * 3 * 5 * 7 * pow(11, 2)
            )
        );
    }
}
