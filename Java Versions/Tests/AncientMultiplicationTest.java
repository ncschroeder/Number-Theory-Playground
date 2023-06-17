import com.nicholasschroeder.numbertheoryplayground.AncientMultiplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Has tests for code in the AncientMultiplication class.
 */
class AncientMultiplicationTest {
    /*
    There are 3 situations I can think of when performing the Ancient Egyptian multiplication algorithm.
    int1, rows1, and rows2 refer to the corresponding fields on an Info object created.
    1. int1 is a power of 2 so the binary string consists of a 1 followed by either nothing or some 0s.
       There's only 1 power of 2 that sums to int1. rows2 is only going to contain a single row.
    2. int1 is 1 less than a power of 2 and the binary string consists only of 1s and all the powers of 2
       < int1 sum to the int1. rows1 and rows2 will be the same.
    3. None of the above. The binary string will be a 1 followed by 1s and 0s. The powers of 2 that sum to
       int1 will have more than 1 but not all of the powers of 2 <= int1. The length of rows2 will be > 1
       && < the length of rows1.
    */
    
    /**
     * Shortened constructor for an AncientMultiplication.Row
     */
    static AncientMultiplication.Row r(int powerOf2, int correspondingMultiple) {
        return new AncientMultiplication.Row(powerOf2, correspondingMultiple);
    }
    
    @Test
    void rowEquals() {
        assertTrue(r(1, 2).equals(r(1, 2)));
    }
    
    @ParameterizedTest
    @MethodSource("getArgs")
    @DisplayName("Info objects generate correct rows")
    void infoObjectRows(int input1, int input2, List<AncientMultiplication.Row> expectedRows1, List<AncientMultiplication.Row> expectedRows2) {
        var info = new AncientMultiplication.Info(input1, input2);
        assertEquals(expectedRows1, info.getRows1().collect(Collectors.toList()));
        assertEquals(expectedRows2, info.getRows2().collect(Collectors.toList()));
    }
    
    static Stream<Arguments> getArgs() {
        List<AncientMultiplication.Row> rowsFor255And300 =
            List.of(
                r(1, 300), r(2, 600), r(4, 1_200), r(8, 2_400), r(16, 4_800), r(32, 9_600),
                r(64, 19_200), r(128, 38_400)
            );
        
        return Stream.of(
            // Situation from above that gets tested is in the comment
            arguments( // 1
                32, 33,
                List.of(r(1, 33), r(2, 66), r(4, 132), r(8, 264), r(16, 528), r(32, 1_056)),
                List.of(r(32, 1_056))
            ),
            arguments(255, 300, rowsFor255And300, rowsFor255And300), // 2
            arguments( // 3
                800, 971,
                List.of(
                    r(1, 971), r(2, 1_942), r(4, 3_884), r(8, 7_768), r(16, 15_536), r(32, 31_072),
                    r(64, 62_144), r(128, 124_288), r(256, 248_576), r(512, 497_152)
                ),
                List.of(r(32, 31_072), r(256, 248_576), r(512, 497_152))
            )
        );
    }
}