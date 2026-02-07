package com.numbertheoryplayground.calculationsimpl.gcdandlcm;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.gcdandlcm.GcdAndLcmAnswer.EuclideanIteration;

class GcdAndLcmAnswerTests {
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
    //arguments(
//                120, 4_235,
//                List.of(ei(4_235, 120, 35), ei(120, 35, 15), ei(35, 15, 5), ei(15, 5, 0))
//)
}