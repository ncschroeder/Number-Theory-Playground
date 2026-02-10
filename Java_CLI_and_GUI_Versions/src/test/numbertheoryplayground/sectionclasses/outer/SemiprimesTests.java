package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.sectionclasses.outer.Semiprimes.SemiprimeData;

public class SemiprimesTests {
    @ParameterizedTest
    @MethodSource("getArgsForGetSemiprimesData")
    void getSemiprimesData(long input, List<SemiprimeData> expectedSemiprimesData) {
        assertEquals(expectedSemiprimesData, Semiprimes.getSemiprimesData(input).toList());
    }
    
    static SemiprimeData sd(int semiprime, int factor1, int factor2) {
        return new SemiprimeData(semiprime, factor1, factor2);
    }
    
    static Stream<Arguments> getArgsForGetSemiprimesData() {
        /*
        For this data, I got the semiprimes from OEIS sequence A001358 at
        https://oeis.org/A001358 and found the factors myself.
         */
        
        List<SemiprimeData> dataForFirst20Semiprimes =
            List.of(
                sd(4, 2, 2),
                sd(6, 2, 3),
                sd(9, 3, 3),
                sd(10, 2, 5),
                sd(14, 2, 7),
                sd(15, 3, 5),
                sd(21, 3, 7),
                sd(22, 2, 11),
                sd(25, 5, 5),
                sd(26, 2, 13),
                sd(33, 3, 11),
                sd(34, 2, 17),
                sd(35, 5, 7),
                sd(38, 2, 19),
                sd(39, 3, 13),
                sd(46, 2, 23),
                sd(49, 7, 7),
                sd(51, 3, 17),
                sd(55, 5, 11),
                sd(57, 3, 19)
            );
        
        List<SemiprimeData> dataForFirst20SemiprimesAfter100 =
            List.of(
                sd(106, 2, 53),
                sd(111, 3, 37),
                sd(115, 5, 23),
                sd(118, 2, 59),
                sd(119, 7, 17),
                sd(121, 11, 11),
                sd(122, 2, 61),
                sd(123, 3, 41),
                sd(129, 3, 43),
                sd(133, 7, 19),
                sd(134, 2, 67),
                sd(141, 3, 47),
                sd(142, 2, 71),
                sd(143, 11, 13),
                sd(145, 5, 29),
                sd(146, 2, 73),
                sd(155, 5, 31),
                sd(158, 2, 79),
                sd(159, 3, 53),
                sd(161, 7, 23)
            );
        
        return Stream.of(
            arguments(0, dataForFirst20Semiprimes),
            arguments(
                dataForFirst20SemiprimesAfter100.getFirst().semiprime(),
                dataForFirst20SemiprimesAfter100
            )
        );
    }
}