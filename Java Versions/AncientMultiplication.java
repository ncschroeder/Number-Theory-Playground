import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class related to Ancient Egyptian multiplication and the section for it.
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
    private static final int maxInputInt = oneThousand;

    /**
     * Contains data that will be in rows of the tables displayed to the user when showing Ancient Egyptian
     * multiplication info.
     */
    public static class Row {
        public final int powerOf2;
        public final int correspondingMultiple;

        public Row(int powerOf2, int correspondingMultiple) {
            this.powerOf2 = powerOf2;
            this.correspondingMultiple = correspondingMultiple;
        }

        public String getPowerOf2String() {
            return stringifyWithCommas(powerOf2);
        }

        public String getCorrespondingMultipleString() {
            return stringifyWithCommas(correspondingMultiple);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Row) {
                Row other = (Row) obj;
                return this.powerOf2 == other.powerOf2 && this.correspondingMultiple == other.correspondingMultiple;
            }
            return false;
        }
    }

    /**
     * Class with data and functionality for info about an Ancient Egyptian multiplication algorithm.
     */
    public static class Info {
        private final int input1;
        private final int input2;
    
        /**
         * Will contain rows for all the powers of 2 <= input1 and the corresponding multiples of input2
         */
        private final Stream<Row> rows1;

        /**
         * Will contain rows for all the powers of 2 that sum to input1 and the corresponding multiples
         * of input2, which will sum up to the product of input1 and input2
         */
        private final Stream<Row> rows2;

        /**
         * Constructs a new object for info about multiplication of int1 and int2.
         */
        public Info(int input1, int input2) {
            assertIsInRange(input1, minInputInt, maxInputInt);
            assertIsInRange(input2, minInputInt, maxInputInt);

            this.input1 = input1;
            this.input2 = input2;

            // Builders for IntStreams that will be the sources of the Streams for rows1 and rows2
            IntStream.Builder stream1Builder = IntStream.builder();
            IntStream.Builder stream2Builder = IntStream.builder();

            // Iterate backwards through the binary string of input1 to find the powers of 2 that are <= input1,
            // as well as the powers of 2 that sum to input1
            String minIntBinaryString = Integer.toBinaryString(input1);
            for (int index = minIntBinaryString.length() - 1, power = 0; index >= 0; index--, power++) {
                int powerOf2 = (int) Math.pow(2, power);
                stream1Builder.accept(powerOf2);
                if (minIntBinaryString.charAt(index) == '1') {
                    // powerOf2 is one of the powers of 2 that sum to input1
                    stream2Builder.accept(powerOf2);
                }
            }

            Function<IntStream.Builder, Stream<Row>> buildStream =
                builder -> builder.build().mapToObj(i -> new Row(i, i * input2));
            
            rows1 = buildStream.apply(stream1Builder);
            rows2 = buildStream.apply(stream2Builder);
        }

        public String getInput1String() {
            return stringifyWithCommas(input1);
        }
    
        public String getInput2String() {
            return stringifyWithCommas(input2);
        }
    
        public String getProductString() {
            return stringifyWithCommas(input1 * input2);
        }
        
        public Stream<Row> getRows1() {
            return rows1;
        }
    
        public Stream<Row> getRows2() {
            return rows2;
        }
        
    
        /**
         */
        public String getAllPowersOf2ColumnHeading() {
            return "Powers of 2 <= " + getInput1String();
        }
    
        public String getPowersOf2ThatSumToInput1ColumnHeading() {
            return "Powers of 2 that sum to " + getInput1String();
        }
    
        /**
         * Returns the column heading that will be used for the corresponding multiples column for both tables
         * that will be displayed.
         */
        public String getInput2MultiplesColumnHeading() {
            return "Corresponding multiples of " + getInput2String();
        }
    }
    
    private static String getAncientMultiplicationHeading(int input1, int input2) {
        return String.format(
            "Ancient Egyptian multiplication info for %s and %s",
            stringifyWithCommas(input1),
            stringifyWithCommas(input2)
        );
    }
    
    public static class Section extends DoubleInputSection {
        public Section() {
            super(
                "Ancient Egyptian Multiplication",
                infoStrings,
                minInputInt,
                maxInputInt,
                "get the Ancient Egyptian multiplication info for those integers",
                "Ancient Egyptian multiplication"
            );
        }
        
        /**
         * An instance of Info will be created and this method will return a string with a heading, 2 tables,
         * and a message about what the product of inputInt1 and inputInt2 is. Each table will have a column
         * for powers of 2 and a column for the corresponding multiples of the 2nd input. 1 table will contain
         * the data returned by calling getRows1() on the info object and the other table will contain the data
         * returned by calling getRows2() on the info object.
         */
        @Override
        public String getCliAnswer(int input1, int input2) {
            var info = new Info(input1, input2);
            
            // Gap between the heading in the 1st column and the start of the 2nd column in each table.
            // The heading should always be the longest item in the 1st column.
            var columnGap = 3;
            
            String input2MultiplesColumnHeading = info.getInput2MultiplesColumnHeading();
    
            String powersOf2ColumnHeading = info.getAllPowersOf2ColumnHeading();
            int table1Column1Width = powersOf2ColumnHeading.length() + columnGap;
            String headRow =
                NTPCLI.getRowFor2ColumnTable(powersOf2ColumnHeading, table1Column1Width, input2MultiplesColumnHeading);
    
            String table1 =
                info.getRows1()
                .map(r ->
                    NTPCLI.getRowFor2ColumnTable(r.getPowerOf2String(), table1Column1Width, r.getCorrespondingMultipleString())
                )
                .collect(Collectors.joining("\n", headRow + "\n", ""));

            powersOf2ColumnHeading = info.getPowersOf2ThatSumToInput1ColumnHeading();
            int table2Column1Width = powersOf2ColumnHeading.length() + columnGap;
            headRow = NTPCLI.getRowFor2ColumnTable(powersOf2ColumnHeading, table2Column1Width, input2MultiplesColumnHeading);
    
            String productMessage =
                String.format("The sum of the right column is %s, which is the product", info.getProductString());
    
            String table2 =
                info.getRows2()
                .map(r ->
                    NTPCLI.getRowFor2ColumnTable(r.getPowerOf2String(), table2Column1Width, r.getCorrespondingMultipleString())
                )
                .collect(Collectors.joining("\n", headRow + "\n", "\n" + productMessage));
    
            return String.join(
                "\n\n",
                getAncientMultiplicationHeading(input1, input2),
                table1,
                table2
            );
        }
    
        /**
         * An instance of Info will be created and this method will return a heading label, 2 table panels,
         * a label with a message about what the product of inputInt1 and inputInt2 is, and gaps in between
         * these. Each table will have a column for powers of 2 and a column for the corresponding multiples
         * of the 2nd input. 1 table will contain the data returned by calling getRows1() on the info object
         * and the other table will contain the data returned by calling getRows2() on the info object.
         */
        @Override
        public List<Component> getGuiComponents(int input1, int input2) {
            var info = new Info(input1, input2);
            String input2MultiplesColumnHeading = info.getInput2MultiplesColumnHeading();
            var tableLayout = new GridLayout(0, 2);
            Font tableHeadingFont = AnswerPanel.tableHeadingFont;
            Font contentFont = AnswerPanel.contentFont;
    
            var table1 = new NTPPanel();
            table1.setLayout(tableLayout);
            table1.addCenteredLabel(info.getAllPowersOf2ColumnHeading(), tableHeadingFont);
            table1.addCenteredLabel(input2MultiplesColumnHeading, tableHeadingFont);
    
            BiConsumer<Stream<Row>, NTPPanel> addRows =
                (rows, table) ->
                    rows
                    .flatMap(r -> Stream.of(r.getPowerOf2String(), r.getCorrespondingMultipleString()))
                    .forEachOrdered(s -> table.addCenteredLabel(s, contentFont));
            
            addRows.accept(info.getRows1(), table1);
    
            var table2 = new NTPPanel();
            table2.setLayout(tableLayout);
            table2.addCenteredLabel(info.getPowersOf2ThatSumToInput1ColumnHeading(), tableHeadingFont);
            table2.addCenteredLabel(input2MultiplesColumnHeading, tableHeadingFont);
            addRows.accept(info.getRows2(), table2);
            
            String productMessage =
                String.format("The sum of the bottom right column is %s, which is the product", info.getProductString());
            
            return List.of(
                NTPGUI.createCenteredLabel(getAncientMultiplicationHeading(input1, input2), AnswerPanel.mainHeadingFont),
                NTPGUI.createGap(15),
                table1,
                NTPGUI.createGap(15),
                table2,
                NTPGUI.createGap(15),
                NTPGUI.createCenteredLabel(productMessage, contentFont)
            );
        }
    }
}
