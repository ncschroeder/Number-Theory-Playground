package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndPower;

class PrimeFactorizationTests {
    @ParameterizedTest
    @FieldSource("intConstructorArgs")
    void intConstructor(int input, List<FactorAndPower> expectedFps) {
        assertEquals(expectedFps, new PrimeFactorization(input).getFps());
    }
    
    static FactorAndPower fp(int factor, int power) {
        return new FactorAndPower(factor, power);
    }
    
    static int pow(int a, int b) {
        return (int) Math.pow(a, b);
    }
    
    static final List<Arguments> intConstructorArgs =
        List.of(
            arguments(2, List.of(fp(2, 1))),
            arguments(pow(2, 3), List.of(fp(2, 3))),
            arguments(3, List.of(fp(3, 1))),
            arguments(2 * 3 * 5 * 7, List.of(fp(2, 1), fp(3, 1), fp(5, 1), fp(7, 1))),
            arguments(pow(2, 4) * 7, List.of(fp(2, 4), fp(7, 1))),
            arguments(pow(3, 3) * pow(5, 2) * 7, List.of(fp(3, 3), fp(5, 2), fp(7, 1))),
            arguments(pow(5, 2) * pow(17, 2), List.of(fp(5, 2), fp(17, 2))),
            arguments(pow(13, 3), List.of(fp(13, 3))),
            arguments(1_213, List.of(fp(1_213, 1)))
        );
}