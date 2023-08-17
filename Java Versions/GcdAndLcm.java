import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;
import static com.nicholasschroeder.numbertheoryplayground.NTPGUI.*;

/**
 * Utility class related to GCDs and LCMs and the section for it.
 */
public class GcdAndLcm {
    private static final List<String> infoParagraphs =
        List.of(
            "GCD stands for greatest common divisor and LCM stands for least common multiple.",
            PrimeFactorization.GcdAndLcmInfo.infoParagraph,
            PrimeFactorization.GcdAndLcmInfo.examplesParagraph,
            "The Euclidean algorithm can be used to find the GCD of 2 numbers, usually faster than " +
                "calculating the prime factorizations. This algorithm was named after the ancient Greek " +
                "mathematician Euclid. For this algorithm, first take 2 numbers. If the bigger number is " +
                "divisible by the smaller number, then the smaller number is the GCD. Otherwise, the GCD " +
                "of the 2 numbers is the same as the GCD of the smaller number and the remainder when the " +
                "bigger number is divided by the smaller number. Repeat."
        );

    // This section uses prime factorizations so the input constraints for those will be used.
    private static final int minInputInt = PrimeFactorization.minInputInt;
    private static final int maxInputInt = PrimeFactorization.maxInputInt;


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
            return stringifyWithCommas(max);
        }

        public String getMinString() {
            return stringifyWithCommas(min);
        }

        public String getRemainderString() {
            return stringifyWithCommas(remainder);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof EuclideanIteration) {
                var other = (EuclideanIteration) obj;
                return this.max == other.max && this.min == other.min && this.remainder == other.remainder;
            }
            return false;
        }
    }

    /**
     * Returns a list of iteration objects to represent all iterations of the Euclidean algorithm
     * performed on input1 and input2.
     */
    public static List<EuclideanIteration> getEuclideanIterations(int input1, int input2) {
        assertIsInRange(input1, minInputInt, maxInputInt);
        assertIsInRange(input2, minInputInt, maxInputInt);
        
        var iterations = new ArrayList<EuclideanIteration>();
        int max = Math.max(input1, input2);
        int min = Math.min(input1, input2);
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
    
    // Text used for the Euclidean algorithm info table.
    private static final String euclideanHeading = "Euclidean Algorithm Info";
    private static final String euclideanMaxColumnHeading = "Max";
    private static final String euclideanMinColumnHeading = "Min";
    private static final String euclideanRemainderColumnHeading = "Remainder";

    /**
     * Returns a message about what the GCD of input1 and input2 is. euclideanIterations should be a list
     * returned from making a call to getEuclideanIterations using input1 and input2. The min field of the
     * last element in that list is the GCD.
     */
    private static String getEuclideanGcdMessage(int input1, int input2, List<EuclideanIteration> euclideanIterations) {
        return String.format(
            "The GCD of %s and %s is %s.",
            stringifyWithCommas(input1),
            stringifyWithCommas(input2),
            euclideanIterations.get(euclideanIterations.size() - 1).getMinString()
        );
    }

    /**
     * Returns a string with a heading, table, and message about what the GCD of input1 and input2 is.
     * The table has columns for the max number, min number, and remainder for each iteration of the
     * Euclidean algorithm performed on input1 and input2.
     */
    private static String getEuclideanCliAnswer(int input1, int input2) {
        List<EuclideanIteration> euclideanIterations = getEuclideanIterations(input1, input2);
    
        // The gap between the end of the longest item in a column and the item in the next column.
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
        String collectingSuffix = "\n" + getEuclideanGcdMessage(input1, input2, euclideanIterations);
    
        return
            euclideanIterations
            .stream()
            .map(ei ->
                NTPCLI.getRowFor3ColumnTable(ei.getMaxString(), maxColumnWidth, ei.getMinString(), minColumnWidth, ei.getRemainderString())
            )
            .collect(Collectors.joining("\n", collectingPrefix, collectingSuffix));
    }

    /**
     * Returns an NTPPanel with a heading label, table, and label with a message about what the GCD of
     * input1 and input2 is. The table has columns for the max number, min number, and remainder for each
     * iteration of the Euclidean algorithm performed on input1 and input2.
     */
    private static NTPPanel getEuclideanPanel(int input1, int input2) {
        List<EuclideanIteration> euclideanIterations = getEuclideanIterations(input1, input2);
    
        NTPPanel iterationsTable =
            new NTPPanel()
            .chainedSetLayout(new GridLayout(0, 3, 4, 4))
            .center();
        
        List.of(euclideanMaxColumnHeading, euclideanMinColumnHeading, euclideanRemainderColumnHeading)
        .forEach(h -> iterationsTable.addLabel(h, tableHeadingFont));
        
        euclideanIterations.stream()
        .flatMap(ei -> Stream.of(ei.getMaxString(), ei.getMinString(), ei.getRemainderString()))
        .forEachOrdered(s -> iterationsTable.addLabel(s, answerContentFont));
        
        return
            new NTPPanel()
            .setToBoxLayoutWithPageAxis()
            .addCenteredLabel(euclideanHeading, answerSubHeadingFont)
            .add(iterationsTable)
            .addCenteredLabel(
                getEuclideanGcdMessage(input1, input2, euclideanIterations),
                answerContentFont
            )
            .setMaxSizeToPreferredSize();
    }
    
    
    // Text used for prime factorization info table.
    private static final String pfInfoHeading = "Prime Factorization Info";
    private static final String numberColumnHeading = "Number";
    private static final String pfColumnHeading = "Prime Factorization";
    private static final String gcdRowHeading = "GCD";
    private static final String lcmRowHeading = "LCM";

    /**
     * Returns a string with a heading and a table where 1 column is for an integer and another column is for the
     * corresponding prime factorization string. There are rows for input1, input2, the GCD of these, and the
     * LCM of these. For the GCD and LCM rows, there's also a column that says "GCD" and "LCM", respectively.
     */
    private static String getGcdAndLcmViaPfCliAnswer(int input1, int input2) {
        var info = new PrimeFactorization.GcdAndLcmInfo(input1, input2);
        
        String input1String = stringifyWithCommas(input1);
        String input2String = stringifyWithCommas(input2);
        String gcdString = info.getGcdString();
        String lcmString = info.lcmPf.getCorrespondingIntString();
        
        // Only contents of column 1 are a row that says "GCD" and another row that says "LCM".
        var column1Width = 6;
        
        // Make column 2 as wide as the longest element in that column + 3.
        int column2Width =
            Stream.of(numberColumnHeading, input1String, input2String, gcdString, lcmString)
            .mapToInt(String::length)
            .max()
            .orElseThrow()
            + 3;
        
        return
            Stream.of(
                List.of("", numberColumnHeading, pfColumnHeading),
                List.of("", input1String, info.int1Pf.toString()),
                List.of("", input2String, info.int2Pf.toString()),
                List.of(gcdRowHeading, gcdString, info.getGcdPfStringOrDefault()),
                List.of(lcmRowHeading, lcmString, info.lcmPf.toString())
            )
            .map(l -> NTPCLI.getRowFor3ColumnTable(l.get(0), column1Width, l.get(1), column2Width, l.get(2)))
            .collect(Collectors.joining("\n", pfInfoHeading + "\n", ""));
    }

    /**
     * Returns an NTPPanel with a heading label and a table where 1 column is for an integer and another
     * column is for the corresponding prime factorization string. There are rows for input1, input2, the
     * GCD of these, and the LCM of these. For the GCD and LCM rows, there's also a column that says "GCD"
     * and "LCM", respectively.
     */
    private static NTPPanel getGcdAndLcmViaPfPanel(int input1, int input2) {
        var info = new PrimeFactorization.GcdAndLcmInfo(input1, input2);
        
        NTPPanel table =
            new NTPPanel()
            .chainedSetLayout(new GridLayout(5, 3));

        Consumer<String> addHeading = s -> table.addLabel(s, tableHeadingFont);
        Consumer<String> addNormalEntry = s -> table.addLabel(s, answerContentFont);

        List.of("", numberColumnHeading, pfColumnHeading)
        .forEach(addHeading);

        List.of(
            "", stringifyWithCommas(input1), info.int1Pf.toString(),
            "", stringifyWithCommas(input2), info.int2Pf.toString()
        )
        .forEach(addNormalEntry);
    
        addHeading.accept(gcdRowHeading);
        addNormalEntry.accept(info.getGcdString());
        addNormalEntry.accept(info.getGcdPfStringOrDefault());

        addHeading.accept(lcmRowHeading);
        addNormalEntry.accept(info.lcmPf.getCorrespondingIntString());
        addNormalEntry.accept(info.lcmPf.toString());

        return
            new NTPPanel()
            .setToBoxLayoutWithPageAxis()
            .addCenteredLabel(pfInfoHeading, answerSubHeadingFont)
            .add(table)
            .setMaxSizeToPreferredSize();
    }
    
    private static String getGcdAndLcmInfoHeading(int input1, int input2) {
        return String.format("GCD and LCM Info for %s and %s", stringifyWithCommas(input1), stringifyWithCommas(input2));
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
        public String getCliAnswer(int input1, int input2) {
            return String.join(
                "\n\n",
                getGcdAndLcmInfoHeading(input1, input2),
                getEuclideanCliAnswer(input1, input2),
                getGcdAndLcmViaPfCliAnswer(input1, input2)
            );
        }
    
        @Override
        public List<Component> getGuiComponents(int input1, int input2) {
            return List.of(
                createCenteredLabel(getGcdAndLcmInfoHeading(input1, input2), answerMainHeadingFont),
                createGap(10),
                getEuclideanPanel(input1, input2),
                createGap(10),
                getGcdAndLcmViaPfPanel(input1, input2)
            );
        }
    }
}
