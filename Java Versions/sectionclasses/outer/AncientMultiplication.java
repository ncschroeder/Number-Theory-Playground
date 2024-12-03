package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.awt.GridLayout;
import java.math.BigInteger;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import numbertheoryplayground.NTPCLI;
import numbertheoryplayground.gui.NTPPanel;
import numbertheoryplayground.sectionclasses.abstract_.DoubleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.gui.NTPGUI.*;

/**
 * Utility class related to ancient Egyptian multiplication and the section for it.
 */
public class AncientMultiplication {
    private static final String INFO = """
The ancient Egyptians had an interesting algorithm for multiplication. My way of explaining the algorithm
goes like this:

1. Let variable a represent 1 of the numbers and variable b represent the other number.

2. Find all powers of 2 that are <= a. This could be done without modern multiplication by starting with 1,
the 1st power of 2 or 2^0, and finding the next power by adding the previous power to itself. This process
will look like: 1 + 1 = 2 (2^1), 2 + 2 = 4 (2^2), 4 + 4 = 8 (2^3), and so on until we find a power that's > a,
which we won't use.

3. Find the products of b and these powers of 2. Like with the powers of 2, this could be done by starting
with b and finding the next product by adding the previous product to itself. If we let b be 5, this process
will look like: 5 + 5 = 10 (5 x 2), 10 + 10 = 20 (5 x 4), 20 + 20 = 40 (5 x 8), and so on.

4. Find the powers of 2 that sum to a.

5. Add up the products of b and these powers.

This gives us the product of the 2 numbers.

Let's find the product of 5 and 12. Let's first use 5 for the number represented by a in the algorithm above
and 12 for b. The powers of 2 <= 5 are 1, 2, and 4. The products of 12 and these powers are 12, 24, and 48.
The powers of 2 that sum to 5 are 1 and 4. The products of 12 and these powers are 12 and 48. 12 + 48 =
(12 x 1) + (12 x 4) = 12 x (1 + 4) = 60. Now let's use 12 for a and 5 for b. The powers of 2 <= 12 are
1, 2, 4, and 8. The products of 5 and these powers are 5, 10, 20, and 40. The powers of 2 that sum to 12 are
4 and 8. The products of 5 and these powers are 20 and 40. 20 + 40 = (5 x 4) + (5 x 8) = 5 x (4 + 8) = 60.""";
    
    private static final long MIN_INPUT = 2;
    private static final long MAX_INPUT = NINE_QUINTILLION;
    
    /**
     * Has data that will be in rows of the tables displayed to the user when showing ancient Egyptian
     * multiplication info.
     */
    public record TableRow(long powerOf2, BigInteger correspondingMultiple) {
        private String powerOf2String() {
            return toStringWithCommas(powerOf2);
        }
        
        private String correspondingMultipleString() {
            return toStringWithCommas(correspondingMultiple);
        }
    }
    
    public static class Answer {
        private final String mainHeading;
        
        /**
         * Contains rows for all the powers of 2 <= input1 and the corresponding multiples of input2.
         */
        public final Stream<TableRow> table1Rows;
        
        /**
         * Contains rows for all the powers of 2 that sum to input1 and the corresponding multiples
         * of input2, which sum to the product of input1 and input2.
         */
        public final Stream<TableRow> table2Rows;
        
        private final String allPowersOf2ColumnHeading;
        private final String input2MultiplesColumnHeading;
        private final String powersOf2ThatSumToInput1ColumnHeading;
        private final String productSentence;
    
        public Answer(long input1, long input2) {
            assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
            assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
            
            var input1String = toStringWithCommas(input1);
            var input2String = toStringWithCommas(input2);
            
            mainHeading =
                String.format("Ancient Egyptian Multiplication Info for %s and %s", input1String, input2String);
            allPowersOf2ColumnHeading = "Powers of 2 <= " + input1String;
            input2MultiplesColumnHeading = "Corresponding Multiples of " + input2String;
            powersOf2ThatSumToInput1ColumnHeading = "Powers of 2 That Sum to " + input1String;
            BigInteger product = BigInteger.valueOf(input1).multiply(BigInteger.valueOf(input2));
            productSentence =
                String.format("The sum of the bottom right column is %s, which is the product.", toStringWithCommas(product));
            
            Stream.Builder<TableRow> table1RowsBuilder = Stream.builder();
            Stream.Builder<TableRow> table2RowsBuilder = Stream.builder();
            
            /*
            Iterate backwards through the binary string of input1 to find the powers of 2 that are <= input1
            and the powers of 2 that sum to input1.
             */
            var input1BinaryString = Long.toBinaryString(input1);
            var powerOf2 = 1L;
            var input2BigInt = BigInteger.valueOf(input2);
            
            for (int i = input1BinaryString.length() - 1; i >= 0; i--) {
                var row = new TableRow(powerOf2, input2BigInt.multiply(BigInteger.valueOf(powerOf2)));
                table1RowsBuilder.accept(row);
                if (input1BinaryString.charAt(i) == '1') {
                    // powerOf2 is one of the powers of 2 that sum to input1.
                    table2RowsBuilder.accept(row);
                }
                powerOf2 *= 2;
            }
            
            table1Rows = table1RowsBuilder.build();
            table2Rows = table2RowsBuilder.build();
        }
    }

    private static String createTableString(String headRow, Stream<TableRow> rows, int column1Width) {
        return
            rows
            .map(r ->
                NTPCLI.getRowFor2ColumnTable(r.powerOf2String(), column1Width, r.correspondingMultipleString())
            )
            .collect(Collectors.joining("\n", headRow + '\n', ""));
    }
    
    public static final class Section extends DoubleInputSection {
        public Section() {
            super(
                "Ancient Egyptian Multiplication",
                MIN_INPUT,
                MAX_INPUT,
                "get the ancient Egyptian multiplication info for those integers",
                "ancient Egyptian multiplication",
                INFO
            );
        }
        
        /**
         * Returns a string with a heading, 2 tables, and a sentence about what the product of input1 and input2 is.
         */
        @Override
        public String getCliAnswer(long input1, long input2) {
            var answer = new Answer(input1, input2);
            
            // Gap between the heading in the 1st column and the start of the 2nd column in each table.
            // The heading should always be the longest item in the 1st column.
            var columnGap = 3;
            
            String powersOf2ColumnHeading = answer.allPowersOf2ColumnHeading;
            int tableColumn1Width = powersOf2ColumnHeading.length() + columnGap;
            String headRow =
                NTPCLI.getRowFor2ColumnTable(powersOf2ColumnHeading, tableColumn1Width, answer.input2MultiplesColumnHeading);
            var table1 = createTableString(headRow, answer.table1Rows, tableColumn1Width);
            
            powersOf2ColumnHeading = answer.powersOf2ThatSumToInput1ColumnHeading;
            tableColumn1Width = powersOf2ColumnHeading.length() + columnGap;
            headRow =
                NTPCLI.getRowFor2ColumnTable(powersOf2ColumnHeading, tableColumn1Width, answer.input2MultiplesColumnHeading);
            var table2 = createTableString(headRow, answer.table2Rows, tableColumn1Width);
    
            return String.join(
                "\n\n",
                answer.mainHeading,
                table1,
                table2,
                answer.productSentence
            );
        }
    
        /**
         * Returns a list with a heading label, 2 table panels, a label with a sentence about what the
         * product of input1 and input2 is, and gaps in between these.
         */
        @Override
        public List<Component> getGuiComponents(long input1, long input2) {
            var answer = new Answer(input1, input2);
            var tableLayout = new GridLayout(0, 2);
    
            NTPPanel table1 =
                new NTPPanel()
                .chainedSetLayout(tableLayout)
                .addCenteredLabel(answer.allPowersOf2ColumnHeading, TABLE_HEADING_FONT)
                .addCenteredLabel(answer.input2MultiplesColumnHeading, TABLE_HEADING_FONT);
            
            NTPPanel table2 =
                new NTPPanel()
                .chainedSetLayout(tableLayout)
                .addCenteredLabel(answer.powersOf2ThatSumToInput1ColumnHeading, TABLE_HEADING_FONT)
                .addCenteredLabel(answer.input2MultiplesColumnHeading, TABLE_HEADING_FONT);
    
            BiConsumer<Stream<TableRow>, NTPPanel> addRows =
                (rows, table) ->
                    rows
                    .flatMap(r -> Stream.of(r.powerOf2String(), r.correspondingMultipleString()))
                    .forEachOrdered(s -> table.addCenteredLabel(s, ANSWER_CONTENT_FONT));
            
            addRows.accept(answer.table1Rows, table1);
            addRows.accept(answer.table2Rows, table2);
            
            return List.of(
                createCenteredLabel(answer.mainHeading, ANSWER_MAIN_HEADING_FONT),
                createGap(15),
                table1,
                createGap(15),
                table2,
                createGap(15),
                createCenteredLabel(answer.productSentence, ANSWER_CONTENT_FONT)
            );
        }
    }
}