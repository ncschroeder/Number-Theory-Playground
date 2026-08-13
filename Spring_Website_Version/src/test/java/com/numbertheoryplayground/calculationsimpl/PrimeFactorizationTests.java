package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndExponent;

class PrimeFactorizationTests {
    @ParameterizedTest
    @FieldSource("intConstructorArgs")
    void intConstructor(int input, List<FactorAndExponent> expectedFes) {
        assertEquals(expectedFes, new PrimeFactorization(input).getFes());
    }
    
    static FactorAndExponent fe(int factor, int exponent) {
        return new FactorAndExponent(factor, exponent);
    }
    
    static int pow(int a, int b) {
        return (int) Math.pow(a, b);
    }
    
    static final List<Arguments> intConstructorArgs =
        List.of(
            arguments(2, List.of(fe(2, 1))),
            arguments(pow(2, 3), List.of(fe(2, 3))),
            arguments(3, List.of(fe(3, 1))),
            arguments(2 * 3 * 5 * 7, List.of(fe(2, 1), fe(3, 1), fe(5, 1), fe(7, 1))),
            arguments(pow(2, 4) * 7, List.of(fe(2, 4), fe(7, 1))),
            arguments(pow(3, 3) * pow(5, 2) * 7, List.of(fe(3, 3), fe(5, 2), fe(7, 1))),
            arguments(pow(5, 2) * pow(17, 2), List.of(fe(5, 2), fe(17, 2))),
            arguments(pow(13, 3), List.of(fe(13, 3))),
            arguments(1_213, List.of(fe(1_213, 1)))
        );
}