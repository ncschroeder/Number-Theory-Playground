package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndPower;

class DivisibilityPrimeFactorizationAnswerTests {
    @ParameterizedTest
    @MethodSource("getArgsForGetFactorPfs")
    void getFactorPfs(
        List<FactorAndPower> input,
        List<List<FactorAndPower>> expectedFactorFpLists
    ) {
        List<List<FactorAndPower>> actualFactorFpLists =
            DivisibilityPrimeFactorizationAnswer.getFactorPfs(new PrimeFactorization(input))
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