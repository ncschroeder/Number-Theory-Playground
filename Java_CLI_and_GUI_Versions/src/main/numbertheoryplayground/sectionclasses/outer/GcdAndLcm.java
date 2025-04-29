package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.*;
import numbertheoryplayground.Misc;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpPanel;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.DoubleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.gui.NtpGui.*;

/**
 * Utility class related to GCDs and LCMs and the section for it.
 */
public class GcdAndLcm {
    private static final Supplier<String> infoStartSupplier = () -> """
GCD stands for greatest common divisor and LCM stands for least common multiple. Greatest common
divisor is also known as greatest common factor, or GCF. To find the GCD and LCM of 2 numbers, you
could manually do some division and multiplication but there are other ways to find them.

The Euclidean algorithm can be used to find the GCD of 2 numbers. This algorithm was named after the
ancient Greek mathematician Euclid. For this algorithm, first take 2 numbers. If the bigger number
is divisible by the smaller number, then the smaller number is the GCD. Otherwise, the GCD of the 2
numbers is the same as the GCD of the smaller number and the remainder when the bigger number is
divided by the smaller number. Repeat.""";
    
    // This section uses prime factorizations so the input constraints for those will be used.
    
    
    static final long MIN_INPUT = PrimeFactorization.MIN_INPUT;
    static final long MAX_INPUT = PrimeFactorization.MAX_INPUT / 2;
    /**
     * Record with data for an iteration of the Euclidean algorithm.
     */
    record EuclideanIteration(long max, long min, long remainder) {
        private String maxString() {
            return toStringWithCommas(max);
        }
        
        private String minString() {
            return toStringWithCommas(min);
        }
        
        private String remainderString() {
            return toStringWithCommas(remainder);
        }
    }
    
    /**
     * Returns a list of iteration objects to represent all iterations of the Euclidean algorithm
     * performed on input1 and input2.
     */
    static List<EuclideanIteration> getEuclideanIterations(long input1, long input2) {
        assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
        assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
        
        long max = Math.max(input1, input2);
        long min = Math.min(input1, input2);
        long remainder = max % min;
        
        var iterations = new ArrayList<EuclideanIteration>();
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
    private static final String EUCLIDEAN_TABLE_HEADING = "Euclidean Algorithm Info";
    private static final String EUCLIDEAN_MAX_COLUMN_HEADING = "Max";
    private static final String EUCLIDEAN_MIN_COLUMN_HEADING = "Min";
    private static final String EUCLIDEAN_REMAINDER_COLUMN_HEADING = "Remainder";
    
    /**
     * Returns a message about what the GCD of input1 and input2 is. iterations should be a list
     * returned from making a call to getEuclideanIterations using input1 and input2. The min of
     * the last element in that list is the GCD.
     */
    private static String getEuclideanGcdMessage(long input1, long input2, List<EuclideanIteration> iterations) {
        return String.format(
            "The GCD of %s and %s is %s.",
            toStringWithCommas(input1),
            toStringWithCommas(input2),
            iterations.getLast().minString()
        );
    }
    
    /**
     * Returns a string with a heading, table, and message about what the GCD of input1 and input2 is.
     * The table has columns for the max number, min number, and remainder for each iteration of the
     * Euclidean algorithm performed on input1 and input2.
     */
    private static String getEuclideanCliAnswer(long input1, long input2) {
        List<EuclideanIteration> iterations = getEuclideanIterations(input1, input2);
        
        // The gap between the end of the longest item in a column and the item in the next column.
        var columnGap = 4;
    
        /*
        Make column widths equal to the length of the longest element in the column + the column gap.
        The first iteration will have the longest elements of all iterations.
         */
        EuclideanIteration iteration1 = iterations.getFirst();
        
        int maxColumnWidth =
            Math.max(EUCLIDEAN_MAX_COLUMN_HEADING.length(), iteration1.maxString().length()) +
            columnGap;
        
        int minColumnWidth =
            Math.max(EUCLIDEAN_MIN_COLUMN_HEADING.length(), iteration1.minString().length()) +
            columnGap;
        
        String headRow =
            NtpCli.getRowFor3ColumnTable(
                EUCLIDEAN_MAX_COLUMN_HEADING, maxColumnWidth,
                EUCLIDEAN_MIN_COLUMN_HEADING, minColumnWidth,
                EUCLIDEAN_REMAINDER_COLUMN_HEADING
            );
        
        String collectingPrefix = EUCLIDEAN_TABLE_HEADING + '\n' + headRow + '\n';
        String collectingSuffix = '\n' + getEuclideanGcdMessage(input1, input2, iterations);
        
        return
            iterations
            .stream()
            .map(i ->
                NtpCli.getRowFor3ColumnTable(
                    i.maxString(), maxColumnWidth, i.minString(), minColumnWidth, i.remainderString()
                )
            )
            .collect(Collectors.joining("\n", collectingPrefix, collectingSuffix));
    }
    
    /**
     * Returns an NTPPanel with a heading label, table, and label with a message about what the GCD of
     * input1 and input2 is. The table has columns for the max number, min number, and remainder for each
     * iteration of the Euclidean algorithm performed on input1 and input2.
     */
    private static NtpPanel getEuclideanPanel(long input1, long input2) {
        List<EuclideanIteration> iterations = getEuclideanIterations(input1, input2);
        
        Function<EuclideanIteration, Stream<String>> getIterationRowStrings =
            ei ->
                LongStream.of(ei.max, ei.min, ei.remainder)
                .mapToObj(Misc::createStringWithCommas);
        
        NtpPanel iterationsTable =
            NtpPanel.createTablePanel(
                EUCLIDEAN_COLUMN_HEADINGS,
                iterations.stream(),
                getIterationRowStrings
            );
        
        String gcdMessage = getEuclideanGcdMessage(input1, input2, iterations);
        
        return
            new NtpPanel()
            .setToBoxLayoutWithPageAxis()
            .add(createAnswerSubHeadingLabel(EUCLIDEAN_TABLE_HEADING))
            .add(iterationsTable)
            .add(createCenteredAnswerContentLabel(gcdMessage))
            .setMaxSizeToPreferredSize();
    }
    
    private static String getAnswerMainHeading(long input1, long input2) {
        return String.format(
            "GCD and LCM Info for %s and %s",
            toStringWithCommas(input1),
            toStringWithCommas(input2)
        );
    }

    private static final String PF_INFO_HEADING = "Prime Factorization Info";

    
    public static final class Section extends DoubleInputSection {
        public Section() {
            super(
                "GCD and LCM",
                MIN_INPUT,
                MAX_INPUT,
                "get GCD and LCM info about them",
                "GCDs and LCMs",
                infoStartSupplier.get() + "\n\n" + PrimeFactorization.gcdAndLcmInfoSupplier.get()
            );
        }
        
        @Override
        public String getCliAnswer(long input1, long input2) {
            // Call getEuclideanCliAnswer first to see if it throws.
            String euclideanAnswer = getEuclideanCliAnswer(input1, input2);
            Stream<String> pfInfoSentences =
                new PrimeFactorization.GcdAndLcmAnswer(input1, input2).getInfoSentences();
            String pfAnswer =
                NtpCli.buildStringWithStreamElementsOnSeparateLines(PF_INFO_HEADING, pfInfoSentences);
            return String.join(
                "\n\n",
                getAnswerMainHeading(input1, input2),
                euclideanAnswer,
                pfAnswer
            );
        }
        
        @Override
        public List<Component> getGuiComponents(long input1, long input2) {
            Stream<String> pfInfoSentences =
                new PrimeFactorization.GcdAndLcmAnswer(input1, input2).getInfoSentences();
            
            NtpPanel pfInfoPanel =
                new NtpPanel()
                .setToBoxLayoutWithPageAxis()
                .add(createAnswerSubHeadingLabel(PF_INFO_HEADING))
                .add(NtpTextArea.createWithStreamElementsOnSeparateLines(pfInfoSentences));
            
            return List.of(
                createAnswerMainHeadingLabel(getAnswerMainHeading(input1, input2)),
                createGapBetweenAnswerSections(),
                getEuclideanPanel(input1, input2),
                createGapBetweenAnswerSections(),
                pfInfoPanel
            );
        }
    }
}
