import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AncientEgyptianMultiplicationInfo {
    public final int minInt;
    public final int maxInt;
    public final int product;

    /**
     * Will contain rows for all the powers of 2 less than or equal to minInt and the corresponding multiples of
     * maxInt.
     */
    private final List<Row> rows1;

    /**
     * Will be a sublist of rows1 and contain rows for all the powers of 2 that sum to minInt and the corresponding
     * multiples of maxInt, which will sum up to the product of minInt and maxInt.
     */
    private final List<Row> rows2;

    public static String getMainLabel(String inputString1, String inputString2) {
        return String.format("Ancient Egyptian multiplication info for %s and %s", inputString1, inputString2);
    }

    /**
     * Contains data that will be in rows of the tables displayed to the user when showing ancient Egyptian
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
    }

    public AncientEgyptianMultiplicationInfo(int int1, int int2) {
        if (Section.ANCIENT_EGYPTIAN_MULTIPLICATION.anyAreInvalidInput(int1, int2)) {
            throw new IllegalArgumentException();
        }

        minInt = Math.min(int1, int2);
        maxInt = Math.max(int1, int2);

        rows1 = new ArrayList<>();
        rows2 = new ArrayList<>();

        // Iterate backwards through the binary string of minInt to find the powers of 2 that are less than or equal to
        // minInt, as well as the powers of 2 that sum to minInt, as well as maxInt multiplied by those powers of 2.
        String minIntBinaryString = Integer.toBinaryString(minInt);
        for (int index = minIntBinaryString.length() - 1, power = 0; index >= 0; index--, power++) {
            int powerOf2 = (int) Math.pow(2, power);
            Row row = new Row(powerOf2, maxInt * powerOf2);
            rows1.add(row);
            if (minIntBinaryString.charAt(index) == '1') {
                rows2.add(row);
            }
        }

        product = rows2.stream().mapToInt(r -> r.correspondingMultiple).sum();
        if (product != int1 * int2) {

        }
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
     *
     * @return An unmodifiable list of rows for all the powers of 2 less than or equal to minInt and the
     * corresponding multiples of maxInt.
     */
    public List<Row> getRows1() {
        return List.copyOf(rows1);
    }

    /**
     *
     * @return An unmodifiable list of rows for all the powers of 2 that sum to minInt and the corresponding
     * multiples of maxInt, which will sum up to the product of minInt and maxInt. This will be a sublist of
     * the list returned by getRows1().
     */
    public List<Row> getRows2() {
        return List.copyOf(rows2);
    }

    /**
     *
     * @return
     */
    public String getAllPowersOf2ColumnHeading() {
        return String.format("Powers of 2 <= %s", getMinIntString());
    }

    /**
     *
     * @return
     */
    public String getPowersOf2ThatSumToMinIntColumnHeading() {
        return String.format("Powers of 2 that sum to %s", getMinIntString());
    }

    /**
     *
     * @return The column heading that will be used for the corresponding multiples column for both tables
     * that will be displayed.
     */
    public String getMaxIntMultiplesColumnHeading() {
        return String.format("Corresponding multiples of %s", getMaxIntString());
    }
}
