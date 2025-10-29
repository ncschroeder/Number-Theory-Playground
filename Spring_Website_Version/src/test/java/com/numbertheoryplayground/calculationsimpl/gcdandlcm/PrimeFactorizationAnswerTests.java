package com.numbertheoryplayground.calculationsimpl.gcdandlcm;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.numbertheoryplayground.calculationsimpl.PrimeFactorization;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndPower;

class PrimeFactorizationAnswerTests {
    static FactorAndPower fp(int factor, int power) {
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
        assertEquals(expectedCorrespondingInt, pf.getCorrespondingInt());
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
        var answer = new PrimeFactorizationAnswer(input1, input2);
        
        assertAll(
            () -> assertNull(answer.getGcdPf()),
            () -> assertPf(answer.getLcmPf(), expectedLcmFps, expectedLcm)
        );
    }
    
    @ParameterizedTest
    @MethodSource("getArgsForPfAnswerForNonCoprimeInputs")
    void pfAnswerForNonCoprimeInputs(
        int input1,
        int input2,
        List<FactorAndPower> expectedGcdFps,
        int expectedGcd,
        List<FactorAndPower> expectedLcmFps,
        int expectedLcm
    ) {
        var answer = new PrimeFactorizationAnswer(input1, input2);
        
        assertAll(
            () -> {
                assertNotNull(answer.getGcdPf());
                assertPf(answer.getGcdPf(), expectedGcdFps, expectedGcd);
            },
            () -> assertPf(answer.getLcmPf(), expectedLcmFps, expectedLcm)
        );
    }
    
    static Stream<Arguments> getArgsForPfAnswerForNonCoprimeInputs() {
        List<FactorAndPower> fpsFor10 = List.of(fp(2, 1), fp(5, 1));
        
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