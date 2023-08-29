package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;
import static com.nicholasschroeder.numbertheoryplayground.NTPGUI.*;

/**
 * Utility class related to ancient Egyptian multiplication and the section for it.
 */
public class AncientMultiplication {
    private static final List<String> infoStrings =
        List.of(
            "The ancient Egyptians had an interesting algorithm for multiplication. My way of explaining " +
                "the algorithm goes like this:",
            "1. Let variable a represent 1 of the numbers and variable b represent the other number.",
            "2. Find all powers of 2 that are <= a. This could be done without modern multiplication by " +
                "starting with 1, the 1st power of 2 or 2^0, and finding the next power by adding the previous " +
                "power to itself. This process will look like: 1 + 1 = 2 (2^1), 2 + 2 = 4 (2^2), 4 + 4 = 8 (2^3), " +
                "and so on until we find a power that's > a, which we won't use.",
            "3. Find the products of b and these powers of 2. Like with the powers of 2, this could be done " +
                "by starting with b and finding the next product by adding the previous product to itself. " +
                "If we let b be 5, this process will look like: 5 + 5 = 10 (5 x 2), 10 + 10 = 20 (5 x 4), " +
                "20 + 20 = 40 (5 x 8), and so on.",
            "4. Find the powers of 2 that add up to a.",
            "5. Add up the products of b and these powers.",
            "This gives us the product of the 2 numbers.",
            "Let's find the product of 5 and 12. Let's first use 5 for the number represented by a in the " +
                "algorithm above and 12 for b. The powers of 2 <= 5 are 1, 2, and 4. The products of 12 and " +
                "these powers are 12, 24, and 48. The powers of 2 that add up to 5 are 1 and 4. The products " +
                "of 12 and these powers are 12 and 48. 12 + 48 = (12 x 1) + (12 x 4) = 12 x (1 + 4) = 60. " +
                "Now let's use 12 for a and 5 for b. The powers of 2 <= 12 are 1, 2, 4, and 8. The products " +
                "of 5 and these powers are 5, 10, 20, and 40. The powers of 2 that add up to 12 are 4 and 8. " +
                "The products of 5 and these powers are 20 and 40. 20 + 40 = (5 x 4) + (5 x 8) = 5 x (4 + 8) = 60."
        );
    
    private static final int minInputInt = 2;
    private static final int maxInputInt = tenThousand;
    
    /**
     * Has data that will be in rows of the tables displayed to the user when showing ancient Egyptian
     * multiplication info.
     */
    public static class Row {
        private final int powerOf2;
        private final int correspondingMultiple;
        
        public Row(int powerOf2, int correspondingMultiple) {
            this.powerOf2 = powerOf2;
            this.correspondingMultiple = correspondingMultiple;
        }
        
        private String getPowerOf2String() {
            return stringifyWithCommas(powerOf2);
        }
        
        private String getCorrespondingMultipleString() {
            return stringifyWithCommas(correspondingMultiple);
        }
    
        @Override
        public boolean equals(Object o) {
            if (o instanceof Row) {
                var row = (Row) o;
                return this.powerOf2 == row.powerOf2 && this.correspondingMultiple == row.correspondingMultiple;
            }
            return false;
        }
    }
    
    /**
     * Has data and functionality related to the ancient Egyptian multiplication algorithm performed on 2 ints.
     */
    public static class Info {
        private final int input1;
        private final int input2;
    
        /**
         * Will contain rows for all the powers of 2 <= input1 and the corresponding multiples of input2.
         */
        private final Stream<Row> rows1;
    
        /**
         * Will contain rows for all the powers of 2 that add up to input1 and the corresponding multiples
         * of input2, which will add up to the product of input1 and input2.
         */
        private final Stream<Row> rows2;
        
        public Info(int input1, int input2) {
            assertIsInRange(input1, minInputInt, maxInputInt);
            assertIsInRange(input2, minInputInt, maxInputInt);
            
            this.input1 = input1;
            this.input2 = input2;
            
            Stream.Builder<Row> rows1Builder = Stream.builder();
            Stream.Builder<Row> rows2Builder = Stream.builder();
        
            // Iterate backwards through the binary string of input1 to find the powers of 2 that are <= input1
            // and the powers of 2 that add up to input1.
            String input1BinaryString = Integer.toBinaryString(input1);
            for (int index = input1BinaryString.length() - 1, power = 0; index >= 0; index--, power++) {
                int powerOf2 = (int) Math.pow(2, power);
                var row = new Row(powerOf2, input2 * powerOf2);
                rows1Builder.accept(row);
                if (input1BinaryString.charAt(index) == '1') {
                    // powerOf2 is one of the powers of 2 that add up to input1.
                    rows2Builder.accept(row);
                }
            }
            
            rows1 = rows1Builder.build();
            rows2 = rows2Builder.build();
        }

        private String getInput1String() {
            return stringifyWithCommas(input1);
        }
        
        private String getInput2String() {
            return stringifyWithCommas(input2);
        }
        
        public Stream<Row> getRows1() {
            return rows1;
        }
        
        public Stream<Row> getRows2() {
            return rows2;
        }
        
        private String getAnswerHeading() {
            return String.format(
                "Ancient Egyptian Multiplication Info for %s and %s",
                stringifyWithCommas(input1),
                stringifyWithCommas(input2)
            );
        }
    
        private String getAllPowersOf2ColumnHeading() {
            return "Powers of 2 <= " + getInput1String();
        }
    
        private String getPowersOf2ThatAddUpToInput1ColumnHeading() {
            return "Powers of 2 That Add Up to " + getInput1String();
        }
    
        /**
         * Returns the column heading that will be used for the corresponding multiples column for both tables
         * that will be displayed.
         */
        private String getInput2MultiplesColumnHeading() {
            return "Corresponding Multiples of " + getInput2String();
        }
        
        /**
         * Returns the message that will be displayed below both tables.
         */
        private String getProductMessage() {
            return String.format(
                "The sum of the bottom right column is %s, which is the product.",
                stringifyWithCommas(input1 * input2)
            );
        }
    }
    
    
    public static class Section extends DoubleInputSection {
        public Section() {
            super(
                "Ancient Egyptian Multiplication",
                infoStrings,
                minInputInt,
                maxInputInt,
                "get the ancient Egyptian multiplication info for those integers",
                "ancient Egyptian multiplication"
            );
        }
        
        private String createTableString(String headRow, Stream<Row> rows, int column1Width) {
            return
                rows
                .map(r ->
                    NTPCLI.getRowFor2ColumnTable(r.getPowerOf2String(), column1Width, r.getCorrespondingMultipleString())
                )
                .collect(Collectors.joining("\n", headRow + "\n", ""));
        }
        
        /**
         * An instance of Info will be created and this method will return a string with a heading, 2 tables,
         * and a message about what the product of input1 and input2 is. Each table will have a column for
         * powers of 2 and a column for the corresponding multiples of the 2nd input. 1 table will contain
         * the data returned by calling getRows1() on the Info object and the other table will contain the
         * data returned by calling getRows2() on the Info object.
         */
        @Override
        public String getCliAnswer(int input1, int input2) {
            var info = new Info(input1, input2);
            
            // Gap between the heading in the 1st column and the start of the 2nd column in each table.
            // The heading should always be the longest item in the 1st column.
            var columnGap = 3;
            
            String powersOf2ColumnHeading = info.getAllPowersOf2ColumnHeading();
            int tableColumn1Width = powersOf2ColumnHeading.length() + columnGap;
            String input2MultiplesColumnHeading = info.getInput2MultiplesColumnHeading();
            String headRow =
                NTPCLI.getRowFor2ColumnTable(powersOf2ColumnHeading, tableColumn1Width, input2MultiplesColumnHeading);
            String table1 = createTableString(headRow, info.rows1, tableColumn1Width);
            
            powersOf2ColumnHeading = info.getPowersOf2ThatAddUpToInput1ColumnHeading();
            tableColumn1Width = powersOf2ColumnHeading.length() + columnGap;
            headRow =
                NTPCLI.getRowFor2ColumnTable(powersOf2ColumnHeading, tableColumn1Width, input2MultiplesColumnHeading);
            String table2 = createTableString(headRow, info.rows2, tableColumn1Width);
    
            return String.join(
                "\n\n",
                info.getAnswerHeading(),
                table1,
                table2,
                info.getProductMessage()
            );
        }
    
        /**
         * An instance of Info will be created and this method will return a list with a heading label, 2 table
         * panels, a label with a message about what the product of input1 and input2 is, and gaps in between
         * these. Each table will have a column for powers of 2 and a column for the corresponding multiples of
         * the 2nd input. 1 table will contain the data returned by calling getRows1() on the Info object and
         * the other table will contain the data returned by calling getRows2() on the Info object.
         */
        @Override
        public List<Component> getGuiComponents(int input1, int input2) {
            var info = new Info(input1, input2);
            var tableLayout = new GridLayout(0, 2);
            String input2MultiplesColumnHeading = info.getInput2MultiplesColumnHeading();
    
            NTPPanel table1 =
                new NTPPanel()
                .chainedSetLayout(tableLayout)
                .addCenteredLabel(info.getAllPowersOf2ColumnHeading(), tableHeadingFont)
                .addCenteredLabel(input2MultiplesColumnHeading, tableHeadingFont);
            
            NTPPanel table2 =
                new NTPPanel()
                .chainedSetLayout(tableLayout)
                .addCenteredLabel(info.getPowersOf2ThatAddUpToInput1ColumnHeading(), tableHeadingFont)
                .addCenteredLabel(input2MultiplesColumnHeading, tableHeadingFont);
    
            BiConsumer<Stream<Row>, NTPPanel> addRows =
                (rows, table) ->
                    rows
                    .flatMap(r -> Stream.of(r.getPowerOf2String(), r.getCorrespondingMultipleString()))
                    .forEachOrdered(s -> table.addCenteredLabel(s, answerContentFont));
            
            addRows.accept(info.getRows1(), table1);
            addRows.accept(info.getRows2(), table2);
            
            return List.of(
                createCenteredLabel(info.getAnswerHeading(), answerMainHeadingFont),
                createGap(15),
                table1,
                createGap(15),
                table2,
                createGap(15),
                createCenteredLabel(info.getProductMessage(), answerContentFont)
            );
        }
    }
}