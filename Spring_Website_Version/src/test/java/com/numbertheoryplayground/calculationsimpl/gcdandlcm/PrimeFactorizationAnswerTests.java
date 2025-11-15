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
    /*
    There are 4 situations I can think of for calculating the GCD and LCM of 2 ints:
    1. Both ints are the same so their GCD and LCM is that int.
    2. One of the ints is a multiple of the other. Their GCD is the smaller int and their LCM is
       the other.
    3. The 2 ints have some common factors in their PFs so their GCD is > 1 and < the smaller int
       and their LCM is > the bigger int and < the product of the 2 ints.
    4. The 2 ints have no common prime factors so their GCD is 1 and their LCM is the product of
       the 2 ints. The term for 2 ints like this is coprime.
     */
    
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
     * As mentioned in the comment above, 2 ints are coprime if their GCD is 1, so if a
     * PrimeFactorizationAnswer object is created with coprime inputs, then the getGcdPf
     * method of that object should return an empty Optional.
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