package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.*;

class PrimeFactorizationTests {
    static FactorAndPower fp(int factor, int power) {
        return new FactorAndPower(factor, power);
    }
    
    static Map.Entry<Integer, Integer> me(int key, int value) {
        return Map.entry(key, value);
    }
    
    static int pow(int a, int b) {
        return (int) Math.pow(a, b);
    }
    
    
    @ParameterizedTest
    @FieldSource("argsForIntConstructor")
    void intConstructor(int input, List<FactorAndPower> expectedFactorsAndPowers) {
        assertEquals(expectedFactorsAndPowers, new PrimeFactorization(input).toList());
    }
    
    static final List<Arguments> argsForIntConstructor =
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
        Map<Integer, Integer> input,
        List<List<FactorAndPower>> expectedFactorsAndPowersForFactorPfs
    ) {
        List<List<FactorAndPower>> actualFactorsAndPowersForFactorPfs =
            new PrimeFactorization(input)
            .getFactorPfs()
            .stream()
            .map(PrimeFactorization::toList)
            .toList();
        
        assertEquals(expectedFactorsAndPowersForFactorPfs, actualFactorsAndPowersForFactorPfs);
    }
    
    static Stream<Arguments> getArgsForGetFactorPfs() {
        Map<Integer, Integer> input4 = Map.ofEntries(me(2, 2), me(3, 1));
        List<List<FactorAndPower>> expectedFactorsAndPowersForFactorPfs4 =
            List.of(
                List.of(fp(2, 1)),
                List.of(fp(3, 1)),
                List.of(fp(2, 2)),
                List.of(fp(2, 1), fp(3, 1))
            );
        
        Map<Integer, Integer> input5 = Map.ofEntries(me(2, 1), me(3, 2), me(5, 2));
        List<List<FactorAndPower>> expectedFactorsAndPowersForFactorPfs5 =
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
            arguments(Map.of(2, 1), Collections.emptyList()),
            arguments(
                Map.of(2, 4),
                List.of(List.of(fp(2, 1)), List.of(fp(2, 2)), List.of(fp(2, 3)))
            ),
            arguments(
                Map.ofEntries(me(2, 1), me(3, 1)),
                List.of(List.of(fp(2, 1)), List.of(fp(3, 1)))
            ),
            arguments(input4, expectedFactorsAndPowersForFactorPfs4),
            arguments(input5, expectedFactorsAndPowersForFactorPfs5)
        );
    }
    
    
    /**
     * 2 integers are coprime if their GCD is 1 and they don't have any common prime factors.
     * If input1 and input2 are coprime then expectedGcdPfList and expectedGcd will be null.
     * If the GCD of input1 and input2 is a prime number then expectedGcdPfList will be null
     * but expectedGcd won't be.
     */
    @ParameterizedTest
    @MethodSource("getArgsForGcdAndLcmAnswer")
    void gcdAndLcmAnswer(
        int input1,
        int input2,
        List<FactorAndPower> expectedGcdPfList,
        Integer expectedGcd,
        List<FactorAndPower> expectedLcmPfList,
        int expectedLcm
    ) {
        var answer = new PrimeFactorization.GcdAndLcmAnswer(input1, input2);
        
        assertAll(
            () -> {
                if (expectedGcd == null) {
                    assertNull(answer.gcdPfListAndLongString);
                } else {
                    assertNotNull(answer.gcdPfListAndLongString);
                    assertEquals(expectedGcdPfList, answer.gcdPfListAndLongString.pfList());
                    assertEquals(
                        expectedGcd.toString(),
                        answer.gcdPfListAndLongString.correspondingLongString()
                    );
                }
            },
            () -> {
                assertEquals(expectedLcmPfList, answer.lcmPfListAndLongString.pfList());
                assertEquals(
                    Integer.toString(expectedLcm),
                    answer.lcmPfListAndLongString.correspondingLongString()
                );
            }
        );
    }
    
    static Stream<Arguments> getArgsForGcdAndLcmAnswer() {
        List<FactorAndPower> pfFactorsAndPowersFor10 = List.of(fp(2, 1), fp(5, 1));
        
        return Stream.of(
            arguments(10, 10, pfFactorsAndPowersFor10, 10, pfFactorsAndPowersFor10, 10),
            arguments(
                2 * 3,
                5 * 7,
                null,
                null,
                List.of(fp(2, 1), fp(3, 1), fp(5, 1), fp(7, 1)),
                2 * 3 * 5 * 7
            ),
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
                null,
                5,
                List.of(fp(2, 3), fp(3, 1), fp(5, 1), fp(7, 1), fp(11, 2)),
                pow(2, 3) * 3 * 5 * 7 * pow(11, 2)
            )
        );
    }
}