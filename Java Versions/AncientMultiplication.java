import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class related to Ancient Egyptian multiplication and the section for it.
 */
public class AncientMultiplication {
    /**
     * A list of strings that have info about the algorithm. This will be displayed to the end user and it also can help
     * explain the code in this class.
     */
    private static final List<String> infoStrings =
        List.of(
            "The Ancient Egyptians had an interesting algorithm for multiplication. My way of explaining " +
                "the algorithm goes like this:",
            "1. Find all the powers of 2 that are less than or equal to the min of the 2 numbers. This could " +
                "be done without modern multiplication by starting with 1 and having each following term be " +
                "the previous term added to itself.",
            "2. Find the multiples of the max number that correspond to all these powers of 2. Like with the " +
                "powers of 2, this could be done by starting with the max number and have each following term " +
                "be the previous term added to itself.",
            "3. Find the powers of 2 that sum up to the min number.",
            "4. Sum up the max number multiples that correspond to these powers of 2.",
            "This gives us the product of the 2 numbers."
        );

    public static final int minInputInt = 2;
    public static final int maxInputInt = oneThousand;

    /**
     * Contains data that will be in rows of the tables displayed to the user when showing Ancient Egyptian
     * multiplication info.
     */
    public static class Row {
        public int powerOf2;
        public int correspondingMultiple;

        public Row(int powerOf2, int correspondingMultiple) {
            this.powerOf2 = powerOf2;
            this.correspondingMultiple = correspondingMultiple;
        }

        public String getPowerOf2String() {
            return getLongStringWithCommas(powerOf2);
        }

        public String getCorrespondingMultipleString() {
            return getLongStringWithCommas(correspondingMultiple);
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
        private final int minInt;
        private final int maxInt;
        private final int product;

        /**
         * Will contain rows for all the powers of 2 <= minInt and the corresponding multiples of maxInt.
         */
        private final List<Row> rows1;

        /**
         * Will be a sublist of rows1 and contain rows for all the powers of 2 that sum to minInt and the corresponding
         * multiples of maxInt, which will sum up to the product of minInt and maxInt.
         */
        private final List<Row> rows2;

        /**
         * Constructs a new object for info about multiplication of int1 and int2.
         */
        public Info(int int1, int int2) {
            assertIsInRange(int1, minInputInt, maxInputInt);
            assertIsInRange(int2, minInputInt, maxInputInt);

            minInt = Math.min(int1, int2);
            maxInt = Math.max(int1, int2);
            rows1 = new ArrayList<>();
            rows2 = new ArrayList<>();

            // Iterate backwards through the binary string of minInt to find the powers of 2 that are <= minInt,
            // as well as the powers of 2 that sum to minInt, as well as maxInt multiplied by those powers of 2
            String minIntBinaryString = Integer.toBinaryString(minInt);
            for (int index = minIntBinaryString.length() - 1, power = 0; index >= 0; index--, power++) {
                int powerOf2 = (int) Math.pow(2, power);
                Row row = new Row(powerOf2, maxInt * powerOf2);
                rows1.add(row);
                if (minIntBinaryString.charAt(index) == '1') {
                    rows2.add(row);
                }
            }

            product =
                rows2.stream()
                .mapToInt(r -> r.correspondingMultiple)
                .sum();
        }

        public String getMinIntString() {
            return getLongStringWithCommas(minInt);
        }

        public String getMaxIntString() {
            return getLongStringWithCommas(maxInt);
        }

        public String getProductString() {
            return getLongStringWithCommas(product);
        }

        /**
         * @return An unmodifiable copy of rows1
         */
        public List<Row> getRows1() {
            return List.copyOf(rows1);
        }

        /**
         * @return An unmodifiable copy of rows2
         */
        public List<Row> getRows2() {
            return List.copyOf(rows2);
        }

        public String getAllPowersOf2ColumnHeading() {
            return String.format("Powers of 2 <= %s", getMinIntString());
        }

        public String getPowersOf2ThatSumToMinIntColumnHeading() {
            return String.format("Powers of 2 that sum to %s", getMinIntString());
        }

        /**
         * @return The column heading that will be used for the corresponding multiples column for both tables
         * that will be displayed.
         */
        public String getMaxIntMultiplesColumnHeading() {
            return String.format("Corresponding multiples of %s", getMaxIntString());
        }
    }

    private static String getAncientMultiplicationHeading(int inputInt1, int inputInt2) {
        return String.format(
            "Ancient Egyptian multiplication info for %s and %s",
            getLongStringWithCommas(inputInt1),
            getLongStringWithCommas(inputInt2)
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
         * @return An instance of Info will be created and this method will return a string with a heading,
         * 2 tables, and a message about what the product of inputInt1 and inputInt2 is. Each table will have a
         * column for powers of 2 and a column for the corresponding max int multiples. 1 table will contain
         * the data returned by calling getRows1() on the info object and the other table will contain the data
         * returned by calling getRows2() on the info object.
         */
        @Override
        public String getCliAnswer(int inputInt1, int inputInt2) {
            Info info = new Info(inputInt1, inputInt2);

            // Gap between the heading in the 1st column and the start of the 2nd column in each table.
            // The heading should always be the longest item in the 1st column.
            int columnGap = 3;

            String maxIntMultiplesColumnHeading = info.getMaxIntMultiplesColumnHeading();

            String powersOf2ColumnHeading = info.getAllPowersOf2ColumnHeading();
            int table1Column1Width = powersOf2ColumnHeading.length() + columnGap;
            String headRow =
                NTPCLI.getRowFor2ColumnTable(powersOf2ColumnHeading, table1Column1Width, maxIntMultiplesColumnHeading);

            String table1 =
                info.getRows1()
                .stream()
                .map(r ->
                    NTPCLI.getRowFor2ColumnTable(r.getPowerOf2String(), table1Column1Width, r.getCorrespondingMultipleString())
                )
                .collect(Collectors.joining("\n", headRow + "\n", ""));

            powersOf2ColumnHeading = info.getPowersOf2ThatSumToMinIntColumnHeading();
            int table2Column1Width = powersOf2ColumnHeading.length() + columnGap;
            headRow = NTPCLI.getRowFor2ColumnTable(powersOf2ColumnHeading, table2Column1Width, maxIntMultiplesColumnHeading);

            String productMessage = String.format("The sum of the right column is %s, which is the product", info.getProductString());

            String table2 =
                info.getRows2()
                .stream()
                .map(r -> NTPCLI.getRowFor2ColumnTable(r.getPowerOf2String(), table2Column1Width, r.getCorrespondingMultipleString()))
                .collect(Collectors.joining("\n", headRow + "\n", "\n" + productMessage));

            return String.join(
                "\n\n",
                getAncientMultiplicationHeading(inputInt1, inputInt2),
                table1,
                table2
            );
        }

        /**
         * @return An instance of Info will be created and this method will return a heading label, 2 table
         * panels, a label with a message about what the product of inputInt1 and inputInt2 is, and gaps in
         * between these. Each table will have a column for powers of 2 and a column for the corresponding
         * max int multiples. 1 table will contain the data returned by calling getRows1() on the info object
         * and the other table will contain the data returned by calling getRows2() on the info object.
         */
        @Override
        public List<Component> getGuiComponents(int inputInt1, int inputInt2) {
            Info info = new Info(inputInt1, inputInt2);
            String maxIntMultiplesColumnHeading = info.getMaxIntMultiplesColumnHeading();
            GridLayout tableLayout = new GridLayout(0, 2);
            Font tableHeadingFont = AnswerPanel.tableHeadingFont;
            Font contentFont = AnswerPanel.contentFont;

            NTPPanel table1 = new NTPPanel();
            table1.setLayout(tableLayout);
            table1.addCenteredLabel(info.getAllPowersOf2ColumnHeading(), tableHeadingFont);
            table1.addCenteredLabel(maxIntMultiplesColumnHeading, tableHeadingFont);

            for (Row row : info.getRows1()) {
                table1.addCenteredLabel(row.getPowerOf2String(), contentFont);
                table1.addCenteredLabel(row.getCorrespondingMultipleString(), contentFont);
            }

            NTPPanel table2 = new NTPPanel();
            table2.setLayout(tableLayout);
            table2.addCenteredLabel(info.getPowersOf2ThatSumToMinIntColumnHeading(), tableHeadingFont);
            table2.addCenteredLabel(maxIntMultiplesColumnHeading, tableHeadingFont);

            for (Row row : info.getRows2()) {
                table2.addCenteredLabel(row.getPowerOf2String(), contentFont);
                table2.addCenteredLabel(row.getCorrespondingMultipleString(), contentFont);
            }

            return List.of(
                NTPPanel.createCenteredLabel(getAncientMultiplicationHeading(inputInt1, inputInt2), AnswerPanel.mainHeadingFont),
                NTPGUI.createGap(15),
                table1,
                NTPGUI.createGap(15),
                table2
            );
        }
    }
}
