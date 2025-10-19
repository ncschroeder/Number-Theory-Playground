package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.*;

class PrimeFactorizationTests {
    static FactorAndPower fp(int factor, int power) {
        return new FactorAndPower(factor, power);
    }
    
    
    static int pow(int a, int b) {
        return (int) Math.pow(a, b);
    }
    
    
    @ParameterizedTest
    @FieldSource("intConstructorArgs")
    void intConstructor(int input, List<FactorAndPower> expectedFactorsAndPowers) {
        assertEquals(expectedFactorsAndPowers, new PrimeFactorization(input).getFactorsAndPowers());
    }
    
    static final List<Arguments> intConstructorArgs =
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
    @MethodSource("getArgsForGetFactorPfs")
    void getFactorPfs(
        List<FactorAndPower> input,
        List<List<FactorAndPower>> expectedFactorFpLists
    ) {
        List<List<FactorAndPower>> actualFactorFpLists =
            new PrimeFactorization(input)
            .getFactorPfs()
            .stream()
            .map(PrimeFactorization::getFactorsAndPowers)
            .toList();
        
        assertEquals(expectedFactorFpLists, actualFactorFpLists);
    }
    
    static Stream<Arguments> getArgsForGetFactorPfs() {
        List<FactorAndPower> input1 = List.of(fp(2, 2), fp(3, 1));
        List<List<FactorAndPower>> expectedFactorFpLists1 =
            List.of(
                List.of(fp(2, 1)),
                List.of(fp(3, 1)),
                List.of(fp(2, 2)),
                List.of(fp(2, 1), fp(3, 1))
            );
        
        List<FactorAndPower> input2 = List.of(fp(2, 1), fp(3, 2), fp(5, 2));
        List<List<FactorAndPower>> expectedFactorFpLists2 =
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
            arguments(input1, expectedFactorFpLists1),
            arguments(input2, expectedFactorFpLists2),
            arguments(List.of(fp(2, 1)), Collections.emptyList()),
            arguments(
                List.of(fp(2, 4)),
                List.of(List.of(fp(2, 1)), List.of(fp(2, 2)), List.of(fp(2, 3)))
            ),
            arguments(
                List.of(fp(2, 1), fp(3, 1)),
                List.of(List.of(fp(2, 1)), List.of(fp(3, 1)))
            )
        );
    }
    
    
    void assertFpListAndInt(
        FactorAndPowerListAndInt fpListAndInt,
        List<FactorAndPower> expectedFpList,
        int expectedCorrespondingInt
    ) {
        assertEquals(expectedFpList, fpListAndInt.fpList());
        assertEquals(expectedCorrespondingInt, fpListAndInt.correspondingInt());
    }
    
    /**
     * 2 integers are coprime if their GCD is 1 and they don't have any common prime factors.
     * As a result, if a PrimeFactorization.GcdAndLcmAnswer object is created with coprime
     * inputs, then the GCD PfListAndLongString of that object should be null.
     */
    @Test
    void gcdAndLcmAnswerForCoprimeInputs() {
        int input1 = 2 * 3;
        int input2 = 5 * 7;
        List<FactorAndPower> expectedLcmFpList =
            List.of(fp(2, 1), fp(3, 1), fp(5, 1), fp(7, 1));
        int expectedLcm = 2 * 3 * 5 * 7;
        var answer = new PrimeFactorization.GcdAndLcmAnswer(input1, input2);
        
        assertAll(
            () -> assertNull(answer.getGcdFpListAndInt()),
            () -> assertFpListAndInt(answer.getLcmFpListAndInt(), expectedLcmFpList, expectedLcm)
        );
    }
    
    @ParameterizedTest
    @MethodSource("getArgsForGcdAndLcmAnswerForNonCoprimeInputs")
    void gcdAndLcmAnswerForNonCoprimeInputs(
        int input1,
        int input2,
        List<FactorAndPower> expectedGcdFpList,
        int expectedGcd,
        List<FactorAndPower> expectedLcmFpList,
        int expectedLcm
    ) {
        var answer = new PrimeFactorization.GcdAndLcmAnswer(input1, input2);
        
        assertAll(
            () -> {
                var gcdFpListAndInt = answer.getGcdFpListAndInt();
                assertNotNull(gcdFpListAndInt);
                assertFpListAndInt(gcdFpListAndInt, expectedGcdFpList, expectedGcd);
            },
            () -> assertFpListAndInt(answer.getLcmFpListAndInt(), expectedLcmFpList, expectedLcm)
        );
    }
    
    static Stream<Arguments> getArgsForGcdAndLcmAnswerForNonCoprimeInputs() {
        List<FactorAndPower> factorsAndPowersFor10 = List.of(fp(2, 1), fp(5, 1));
        
        return Stream.of(
            arguments(10, 10, factorsAndPowersFor10, 10, factorsAndPowersFor10, 10),
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