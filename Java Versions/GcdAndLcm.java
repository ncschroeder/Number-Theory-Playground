import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class related to GCDs and LCMs and the section for it.
 */
public class GcdAndLcm {
    /**
     * A list of paragraphs with info about this section. These will be displayed in the application and it also can help
     * explain the code in this class. The 2nd paragraph is relevant to some code in the PrimeFactorization class
     * so that's where that string will be.
     */
    private static final List<String> infoParagraphs =
        List.of(
            "GCD stands for greatest common divisor and LCM stands for least common multiple.",
            PrimeFactorization.GcdAndLcmInfo.infoString,
            "The Euclidean algorithm can be used to find the GCD of 2 numbers, usually faster than " +
                "calculating the prime factorizations. For the Euclidean algorithm, first take 2 " +
                "numbers. If the bigger number is divisible by the smaller number, then the smaller " +
                "number is the GCD. Otherwise, the GCD of the 2 numbers is the same as the GCD of the " +
                "smaller number and the remainder when the bigger number is divided by the smaller " +
                "number. Repeat."
        );

    /**
     * Class with data for an iteration of the Euclidean algorithm.
     */
    public static class EuclideanIteration {
        public final int max;
        public final int min;
        public final int remainder;

        public EuclideanIteration(int max, int min, int remainder) {
            this.max = max;
            this.min = min;
            this.remainder = remainder;
        }

        public String getMaxString() {
            return getLongStringWithCommas(max);
        }

        public String getMinString() {
            return getLongStringWithCommas(min);
        }

        public String getRemainderString() {
            return getLongStringWithCommas(remainder);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof EuclideanIteration) {
                EuclideanIteration other = (EuclideanIteration) obj;
                return this.max == other.max && this.min == other.min && this.remainder == other.remainder;
            }
            return false;
        }
    }

    // This section uses prime factorizations so the input constraints for those will be used
    public static final int minInputInt = PrimeFactorization.minInputInt;
    public static final int maxInputInt = PrimeFactorization.maxInputInt;

    /**
     * @return A list of <code>EuclideanIteration</code> objects to represent all iterations of the Euclidean
     * algorithm performed on int1 and int2.
     */
    public static List<EuclideanIteration> getEuclideanIterations(int int1, int int2) {
        assertIsInRange(int1, minInputInt, maxInputInt);
        assertIsInRange(int2, minInputInt, maxInputInt);

        ArrayList<EuclideanIteration> iterations = new ArrayList<>();
        int max = Math.max(int1, int2);
        int min = Math.min(int1, int2);
        int remainder = max % min;
        iterations.add(new EuclideanIteration(max, min, remainder));
        while (remainder != 0) {
            max = min;
            min = remainder;
            remainder = max % min;
            iterations.add(new EuclideanIteration(max, min, remainder));
        }
        return iterations;
    }

    private static String getGcdAndLcmInfoHeading(int inputInt1, int inputInt2) {
        return String.format("GCD and LCM info for %s and %s", getLongStringWithCommas(inputInt1), getLongStringWithCommas(inputInt2));
    }

    private static final String euclideanHeading = "Euclidean Algorithm Info";
    private static final String euclideanMaxColumnHeading = "Max";
    private static final String euclideanMinColumnHeading = "Min";
    private static final String euclideanRemainderColumnHeading = "Remainder";

    private static String getEuclideanGcdMessage(int inputInt1, int inputInt2, List<EuclideanIteration> euclideanIterations) {
        return String.format(
            "The GCD of %s and %s is %s",
            getLongStringWithCommas(inputInt1),
            getLongStringWithCommas(inputInt2),
            getLastElement(euclideanIterations).getMinString()
        );
    }

    /**
     * @return A string with a heading, a table, and a message about what the GCD of inputInt1 and inputInt2 is.
     * The table has columns for the max number, min number, and remainder for each iteration of the Euclidean
     * algorithm performed on inputInt1 and inputInt2.
     */
    private static String getEuclideanCliAnswer(int inputInt1, int inputInt2) {
        List<EuclideanIteration> euclideanIterations = getEuclideanIterations(inputInt1, inputInt2);

        // The gap between the end of the longest item in a column and the item in the next column
        final int columnGap = 4;

        // Make column widths equal to the length of the longest element in the column + the column gap.
        // The 1st iteration will have the longest elements of all iterations.
        EuclideanIteration iteration1 = euclideanIterations.get(0);
        int maxColumnWidth =
            Math.max(euclideanMaxColumnHeading.length(), iteration1.getMaxString().length()) +
            columnGap;
        int minColumnWidth =
            Math.max(euclideanMinColumnHeading.length(), iteration1.getMinString().length()) +
            columnGap;

        String headRow =
            NTPCLI.getRowFor3ColumnTable(euclideanMaxColumnHeading, maxColumnWidth, euclideanMinColumnHeading, minColumnWidth, euclideanRemainderColumnHeading);
        String collectingPrefix = euclideanHeading + "\n" + headRow + "\n";
        String collectingSuffix = "\n" + getEuclideanGcdMessage(inputInt1, inputInt2, euclideanIterations);

        return
            euclideanIterations
            .stream()
            .map(ei ->
                NTPCLI.getRowFor3ColumnTable(ei.getMaxString(), maxColumnWidth, ei.getMinString(), minColumnWidth, ei.getRemainderString())
            )
            .collect(Collectors.joining("\n", collectingPrefix, collectingSuffix));
    }

    /**
     * @return An NTPPanel with a heading label, a table, and a label with a message about what the GCD of inputInt1
     * and inputInt2 is. The table has columns for the max number, min number, and remainder for each iteration of
     * the Euclidean algorithm performed on inputInt1 and inputInt2.
     */
    private static NTPPanel getEuclideanPanel(int inputInt1, int inputInt2) {
        List<EuclideanIteration> euclideanIterations = getEuclideanIterations(inputInt1, inputInt2);
        NTPPanel euclideanAlgoPanel = new NTPPanel();
        euclideanAlgoPanel.setLayout(new BoxLayout(euclideanAlgoPanel, BoxLayout.PAGE_AXIS));
        euclideanAlgoPanel.addCenteredLabel(euclideanHeading, AnswerPanel.subHeadingFont);

        NTPPanel iterationsTable = new NTPPanel();
        iterationsTable.setLayout(new GridLayout(0, 3, 4, 4));
        NTPPanel.centerComponent(iterationsTable);
        List.of(euclideanMaxColumnHeading, euclideanMinColumnHeading, euclideanRemainderColumnHeading)
        .forEach(h -> iterationsTable.addLabel(h, AnswerPanel.tableHeadingFont));

        for (EuclideanIteration ei : euclideanIterations) {
            List.of(ei.getMaxString(), ei.getMinString(), ei.getRemainderString())
            .forEach(s -> iterationsTable.addLabel(s, AnswerPanel.contentFont));
        }

        euclideanAlgoPanel.add(iterationsTable);
        euclideanAlgoPanel.addCenteredLabel(
            getEuclideanGcdMessage(inputInt1, inputInt2, euclideanIterations),
            AnswerPanel.contentFont
        );
        euclideanAlgoPanel.setMaximumSize(euclideanAlgoPanel.getPreferredSize());
        return euclideanAlgoPanel;
    }

    // Text used for prime factorization info table
    private static final String pfInfoHeading = "Prime factorization info";
    private static final String numberColumnHeading = "Number";
    private static final String pfColumnHeading = "Prime Factorization";
    private static final String gcdRowHeading = "GCD";
    private static final String lcmRowHeading = "LCM";

    /**
     * @return A string with a heading and a table where 1 column is for an integer and another column is for the
     * corresponding prime factorization string. There are rows for inputInt1, inputInt2, the GCD of these, and the
     * LCM of these. For the GCD and LCM rows, there's also a column that says "GCD" and "LCM", respectively.
     */
    private static String getGcdAndLcmViaPfCliAnswer(int inputInt1, int inputInt2) {
        PrimeFactorization.GcdAndLcmInfo info = new PrimeFactorization.GcdAndLcmInfo(inputInt1, inputInt2);
        String inputInt1String = getLongStringWithCommas(inputInt1);
        String inputInt2String = getLongStringWithCommas(inputInt2);
        String gcdString = info.getGcdString();
        String lcmString = info.lcmPf.getCorrespondingLongString();

        int column1Width = 6; // Only contents of this column are 1 row that says "GCD" and another row that says "LCM"

        // Make column 2 as wide as the longest element in that column + 3
        int column2Width =
            Stream.of(numberColumnHeading, inputInt1String, inputInt2String, gcdString, lcmString)
            .mapToInt(String::length)
            .max()
            .orElseThrow()
            + 3;

        return
            Stream.of(
                List.of("", numberColumnHeading, pfColumnHeading),
                List.of("", inputInt1String, info.int1Pf.toString()),
                List.of("", inputInt2String, info.int2Pf.toString()),
                List.of(gcdRowHeading, gcdString, info.getGcdPfStringOrDefault()),
                List.of(lcmRowHeading, lcmString, info.lcmPf.toString())
            )
            .map(l -> NTPCLI.getRowFor3ColumnTable(l.get(0), column1Width, l.get(1), column2Width, l.get(2)))
            .collect(Collectors.joining("\n", pfInfoHeading + "\n", ""));
    }

    /**
     * @return An NTPPanel with a heading label and a table where 1 column is for an integer and another column is
     * for the corresponding prime factorization string. There are rows for inputInt1, inputInt2, the GCD of these,
     * and the LCM of these. For the GCD and LCM rows, there's also a column that says "GCD" and "LCM", respectively.
     */
    private static NTPPanel getGcdAndLcmViaPfPanel(int inputInt1, int inputInt2) {
        PrimeFactorization.GcdAndLcmInfo info = new PrimeFactorization.GcdAndLcmInfo(inputInt1, inputInt2);
        NTPPanel pfPanel = new NTPPanel();
        pfPanel.setLayout(new BoxLayout(pfPanel, BoxLayout.PAGE_AXIS));
        pfPanel.addCenteredLabel(pfInfoHeading, AnswerPanel.subHeadingFont);
        NTPPanel table = new NTPPanel();
        table.setLayout(new GridLayout(5, 3));

        Consumer<String> addHeading = s -> table.addLabel(s, AnswerPanel.tableHeadingFont);
        Consumer<String> addNormalEntry = s -> table.addLabel(s, AnswerPanel.contentFont);

        List.of("", numberColumnHeading, pfColumnHeading).forEach(addHeading);
        List.of(
            "", getLongStringWithCommas(inputInt1), info.int1Pf.toString(),
            "", getLongStringWithCommas(inputInt2), info.int2Pf.toString()
        )
        .forEach(addNormalEntry);

        addHeading.accept(gcdRowHeading);
        List.of(info.getGcdString(), info.getGcdPfStringOrDefault()).forEach(addNormalEntry);
        addHeading.accept(lcmRowHeading);
        List.of(info.lcmPf.getCorrespondingLongString(), info.lcmPf.toString()).forEach(addNormalEntry);
        pfPanel.add(table);
        pfPanel.setMaximumSize(pfPanel.getPreferredSize());

        return pfPanel;
    }

    public static class Section extends DoubleInputSection {
        public Section() {
            super(
                "GCD and LCM",
                infoParagraphs,
                minInputInt,
                maxInputInt,
                "get GCD and LCM info about them",
                "GCDs and LCMs"
            );
        }

        @Override
        public String getCliAnswer(int inputInt1, int inputInt2) {
            return String.join(
                "\n\n",
                getGcdAndLcmInfoHeading(inputInt1, inputInt2),
                getEuclideanCliAnswer(inputInt1, inputInt2),
                getGcdAndLcmViaPfCliAnswer(inputInt1, inputInt2)
            );
        }

        @Override
        public List<Component> getGuiComponents(int inputInt1, int inputInt2) {
            return List.of(
                NTPPanel.createCenteredLabel(getGcdAndLcmInfoHeading(inputInt1, inputInt2), AnswerPanel.mainHeadingFont),
                NTPGUI.createGap(10),
                getEuclideanPanel(inputInt1, inputInt2),
                NTPGUI.createGap(10),
                getGcdAndLcmViaPfPanel(inputInt1, inputInt2)
            );
        }
    }
}
