package com.numbertheoryplayground.calculationsimpl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.numbertheoryplayground.calculationsimpl.AncientMultiplicationAnswer.TableRow;

class AncientMultiplicationAnswerTests {
    /*
    I've identified 3 possible outcomes when creating an AncientMultiplicationAnswer object:

    1. input1 is a power of 2 so its binary string consists of a 1 followed by either nothing
       or some 0's. There, of course, is only 1 power of 2 that sums to input1. table2Rows
       will only have a single row.
    2. input1 is 1 less than a power of 2 so its binary string consists only of 1's and the
       powers of 2 that sum to input1 are all the powers of 2 less than it. table1Rows and
       table2Rows will have the same rows.
    3. None of the above. The binary string of input1 will consist of at least two 1's and at
       least one 0. The amount of powers of 2 that sum to input1 will be > 1 and < the amount
       of powers of 2 < input1. table2Rows will have more than 1 row but won't have as many
       rows as table1Rows.

    I'll test out each of these in the order that they're listed.
     */
    
    @ParameterizedTest
    @MethodSource("getArgs")
    void ancientMultiplicationAnswerTableRows(
        int input1,
        int input2,
        List<TableRow> expectedTable1Rows,
        List<TableRow> expectedTable2Rows
    ) {
        var answer = new AncientMultiplicationAnswer(input1, input2);
        assertEquals(expectedTable1Rows, answer.getTable1Rows());
        assertEquals(expectedTable2Rows, answer.getTable2Rows());
    }
    
    static TableRow tr(int powerOf2, int correspondingMultiple) {
        return new TableRow(Integer.toString(powerOf2), Integer.toString(correspondingMultiple));
    }
    
    static Stream<Arguments> getArgs() {
        List<TableRow> rowsFor255And300 =
            List.of(
                tr(1, 300), tr(2, 600), tr(4, 1_200), tr(8, 2_400), tr(16, 4_800),
                tr(32, 9_600), tr(64, 19_200), tr(128, 38_400)
            );
        
        return Stream.of(
            // 32 is a power of 2 so expectedTable2Rows has a single row.
            arguments(
                32, 33,
                List.of(tr(1, 33), tr(2, 66), tr(4, 132), tr(8, 264), tr(16, 528), tr(32, 1_056)),
                List.of(tr(32, 1_056))
            ),
            // 255 is 1 below a power of 2 so expectedTable1Rows and expectedTable2Rows are the same.
            arguments(255, 300, rowsFor255And300, rowsFor255And300),
            arguments(
                800, 971,
                List.of(
                    tr(1, 971), tr(2, 1_942), tr(4, 3_884), tr(8, 7_768), tr(16, 15_536),
                    tr(32, 31_072), tr(64, 62_144), tr(128, 124_288), tr(256, 248_576),
                    tr(512, 497_152)
                ),
                List.of(tr(32, 31_072), tr(256, 248_576), tr(512, 497_152))
            )
        );
    }
}
