package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.GcdAndLcmAnswer.EuclideanIteration;

class GcdAndLcmAnswerTests {
    @ParameterizedTest
    @FieldSource("args")
    void getEuclideanIterations(int input1, int input2, List<EuclideanIteration> expectedIterations) {
        assertEquals(expectedIterations, GcdAndLcmAnswer.getEuclideanIterations(input1, input2));
    }
    
    static EuclideanIteration ei(int max, int min, int remainder) {
        return new EuclideanIteration(max, min, remainder);
    }
    
    static final List<Arguments> args =
        List.of(
            arguments(10, 10, List.of(ei(10, 10, 0))),
            arguments(10, 5, List.of(ei(10, 5, 0))),
            arguments(6, 35, List.of(ei(35, 6, 5), ei(6, 5, 1), ei(5, 1, 0))),
            arguments(99, 54, List.of(ei(99, 54, 45), ei(54, 45, 9), ei(45, 9, 0))),
            arguments(4_410, 2_100, List.of(ei(4_410, 2_100, 210), ei(2_100, 210, 0)))
        );
}