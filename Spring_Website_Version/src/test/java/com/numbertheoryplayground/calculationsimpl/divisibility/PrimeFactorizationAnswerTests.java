package com.numbertheoryplayground.calculationsimpl.divisibility;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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
}