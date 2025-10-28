package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.sectionclasses.outer.PrimeFactorization.FactorAndPower;

/**
 * Has tests for code in the PrimeFactorization class.
 */
class PrimeFactorizationTests {
    static FactorAndPower fp(long factor, int power) {
        return new FactorAndPower(factor, power);
    }

    static int pow(int a, int b) {
        return (int) Math.pow(a, b);
    }

    
    @ParameterizedTest
    @FieldSource("longConstructorArgs")
    void longConstructor(long input, List<FactorAndPower> expectedFactorsAndPowers) {
        assertEquals(expectedFactorsAndPowers, new PrimeFactorization(input, "").getFactorsAndPowers());
    }

    static final List<Arguments> longConstructorArgs =
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
    @FieldSource("toStringArgs")
    void toString(int input, String expectedString) {
        assertEquals(expectedString, new PrimeFactorization(input, "").toString());
    }

    static final List<Arguments> toStringArgs =
        List.of(
            arguments(1_213, "1,213"),
            arguments(pow(3, 3) * pow(5, 2) * 7, "3^3 × 5^2 × 7")
        );
}
