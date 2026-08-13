package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndExponent;

class DivisibilityPrimeFactorizationAnswerTests {
    @ParameterizedTest
    @MethodSource("getArgsForGetFactorPfs")
    void getFactorPfs(
        List<FactorAndExponent> input,
        List<List<FactorAndExponent>> expectedFactorFeLists
    ) {
        List<List<FactorAndExponent>> actualFactorFeLists =
            DivisibilityPrimeFactorizationAnswer.getFactorPfs(new PrimeFactorization(input))
            .stream()
            .map(PrimeFactorization::getFes)
            .toList();
        
        assertEquals(expectedFactorFeLists, actualFactorFeLists);
    }
    
    static FactorAndExponent fe(int factor, int exponent) {
        return new FactorAndExponent(factor, exponent);
    }
    
    static Stream<Arguments> getArgsForGetFactorPfs() {
        List<FactorAndExponent> input1 = List.of(fe(2, 2), fe(3, 1));
        List<List<FactorAndExponent>> expectedFactorFeLists1 =
            List.of(
                List.of(fe(2, 1)),
                List.of(fe(3, 1)),
                List.of(fe(2, 2)),
                List.of(fe(2, 1), fe(3, 1))
            );
        
        List<FactorAndExponent> input2 = List.of(fe(2, 1), fe(3, 2), fe(5, 2));
        List<List<FactorAndExponent>> expectedFactorFeLists2 =
            List.of(
                // The comments say the corresponding factor.
                List.of(fe(2, 1)), // 2
                List.of(fe(3, 1)), // 3
                List.of(fe(5, 1)), // 5
                List.of(fe(2, 1), fe(3, 1)), // 6
                List.of(fe(3, 2)), // 9
                List.of(fe(2, 1), fe(5, 1)), // 10
                List.of(fe(3, 1), fe(5, 1)), // 15
                List.of(fe(2, 1), fe(3, 2)), // 18
                List.of(fe(5, 2)), // 25
                List.of(fe(2, 1), fe(3, 1), fe(5, 1)), // 30
                List.of(fe(3, 2), fe(5, 1)), // 45
                List.of(fe(2, 1), fe(5, 2)), // 50
                List.of(fe(3, 1), fe(5, 2)), // 75
                List.of(fe(2, 1), fe(3, 2), fe(5, 1)), // 90
                List.of(fe(2, 1), fe(3, 1), fe(5, 2)), // 150
                List.of(fe(3, 2), fe(5, 2)) // 225
            );
        
        return Stream.of(
            arguments(input1, expectedFactorFeLists1),
            arguments(input2, expectedFactorFeLists2),
            arguments(List.of(fe(2, 1)), Collections.emptyList()),
            arguments(
                List.of(fe(2, 4)),
                List.of(List.of(fe(2, 1)), List.of(fe(2, 2)), List.of(fe(2, 3)))
            ),
            arguments(
                List.of(fe(2, 1), fe(3, 1)),
                List.of(List.of(fe(2, 1)), List.of(fe(3, 1)))
            )
        );
    }
}