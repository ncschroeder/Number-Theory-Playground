package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.sectionclasses.outer.GcdAndLcm.*;

/**
 * Has tests for code in the GcdAndLcm class.
 */
class GcdAndLcmTests {
    /*
    There are 4 situations I can think of when calculating the GCD and LCM for 2 ints:
    1. Both ints are the same so the GCD and LCM is that int.
    2. 1 of the ints is a multiple of the other int. The GCD will be the smaller of the ints and
       the LCM will be the other int.
    3. The 2 ints have some common prime factors in their prime factorizations so the GCD is > 1
       && < the smaller int and the LCM is > the bigger int && < the product of the 2 ints.
    4. The 2 ints have no common prime factors so the GCD is 1 and the LCM is the product of the
       2 ints. The term to describe 2 ints like this is coprime.
     */

    @ParameterizedTest
    @FieldSource("args")
    void testGetEuclideanIterations(int input1, int input2, List<EuclideanIteration> expectedIterations) {
        assertEquals(expectedIterations, getEuclideanIterations(input1, input2));
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
