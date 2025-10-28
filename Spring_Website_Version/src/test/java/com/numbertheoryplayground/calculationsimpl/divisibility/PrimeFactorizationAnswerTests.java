package com.numbertheoryplayground.calculationsimpl.divisibility;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import com.numbertheoryplayground.calculationsimpl.PrimeFactorization;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndPower;

public class PrimeFactorizationAnswerTests {
    @ParameterizedTest
    @FieldSource("argsForNumFactorsMethods")
    void numFactorsMethods(int input, String expectedNumFactorsExpression, int expectedNumFactors) {
        var answer = PrimeFactorizationAnswer.createIfNotPrime(input);
        // None of the inputs are prime.
        assertNotNull(answer);
        assertAll(
            () -> assertEquals(expectedNumFactorsExpression, answer.getNumFactorsExpression()),
            () -> assertEquals(expectedNumFactors, answer.getNumFactors())
        );
    }
    
    static final List<Arguments> argsForNumFactorsMethods =
        List.of(
            arguments(2 * 2, "(2 + 1)", 3),
            arguments(2 * 3, "(1 + 1) × (1 + 1)", 2 * 2),
            arguments(2 * 3 * 3 * 5 * 5 * 5, "(1 + 1) × (2 + 1) × (3 + 1)", 2 * 3 * 4)
        );
    
    
    @ParameterizedTest
    @MethodSource("getArgsForGetFactorPfs")
    void getFactorPfs(
        List<FactorAndPower> input,
        List<List<FactorAndPower>> expectedFactorFpLists
    ) {
        List<List<FactorAndPower>> actualFactorFpLists =
            PrimeFactorizationAnswer.getFactorPfs(new PrimeFactorization(input))
            .stream()
            .map(PrimeFactorization::getFps)
            .toList();
        
        assertEquals(expectedFactorFpLists, actualFactorFpLists);
    }
    
    static FactorAndPower fp(int factor, int power) {
        return new FactorAndPower(factor, power);
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
                // The comments say the corresponding factor.
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
}