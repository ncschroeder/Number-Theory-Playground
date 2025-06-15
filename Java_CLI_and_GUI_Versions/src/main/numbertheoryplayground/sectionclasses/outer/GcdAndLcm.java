package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.*;
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
    private static final List<String> INFO_PARAGRAPHS = """
GCD stands for greatest common divisor and is also known as greatest common factor, or GCF.
LCM stands for least common multiple. To find the GCD and LCM of 2 whole numbers, you could
manually do some division and multiplication but there are other ways to find them.

The Euclidean algorithm can be used to find just the GCD of 2 integers. This was named after
the ancient Greek mathematician Euclid.

A simple way of explaining this algorithm is that it starts with 2 whole numbers that we want
to find the GCD of and if the max of those numbers is divisible by the min, then that min is
the GCD. Otherwise, the GCD of the 2 numbers is the same as the GCD of the min and the
remainder when the max is divided by the min. Repeat.

Another way of explaining this algorithm is that it consists of iterations and each one
consists of a max number, min number, and remainder when the max number is divided by the min
number. These'll be referred to as just the max, min, and remainder. The first iteration has a
max of the max of 2 whole numbers that you want to find the GCD of. The min of this iteration
is the min of those 2 numbers. If the remainder is 0, then the algorithm is done and the GCD of
the 2 numbers that we wanted to find the GCD of is the min of this iteration. Otherwise, we do
another iteration and the max of the new iteration is the min of the last iteration and the min
of the new iteration is the remainder of the last iteration. Again, we check if the remainder
is 0 and if it is, then the min of this iteration is the GCD. Otherwise, we keep doing
iterations until we get a remainder of 0.

%s"""
        .formatted(PrimeFactorization.gcdAndLcmInfoSupplier.get())
        .transform(getParagraphList);
    
    // This section uses prime factorizations so the input constraints for those will be used.
    
    
    static final long MIN_INPUT = PrimeFactorization.MIN_INPUT;
    static final long MAX_INPUT = PrimeFactorization.MAX_INPUT / 2;
    /**
     * Record with data for an iteration of the Euclidean algorithm.
     */
    record EuclideanIteration(long max, long min, long remainder) {
        private String maxString() {
            return createStringWithCommas(max);
        }
        
        private String minString() {
            return createStringWithCommas(min);
        }
        
        private String remainderString() {
            return createStringWithCommas(remainder);
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
    private static String getEuclideanGcdMessage(
        String input1String,
        String input2String,
        List<EuclideanIteration> iterations
    ) {
        return String.format(
            "The GCD of %s and %s is %s.",
            input1String, input2String, iterations.getLast().minString()
        );
    }
    
    /**
     * Returns a string with a heading, table, and message about what the GCD of input1Long and input2Long is.
     * The table has columns for the max number, min number, and remainder for each iteration of the
     * Euclidean algorithm performed on input1Long and input2Long.
     */
    private static String getEuclideanCliAnswer(
        long input1Long, long input2Long,
        String input1String, String input2String
    ) {
        List<EuclideanIteration> iterations = getEuclideanIterations(input1Long, input2Long);
        
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
        String collectingSuffix = '\n' + getEuclideanGcdMessage(input1String, input2String, iterations);
        
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
     * input1Long and input2Long is. The table has columns for the max number, min number, and remainder for each
     * iteration of the Euclidean algorithm performed on input1Long and input2Long.
     */
    private static NtpPanel getEuclideanPanel(
        long input1Long, long input2Long,
        String input1String, String input2String
    ) {
        List<EuclideanIteration> iterations = getEuclideanIterations(input1Long, input2Long);
        
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
        
        String gcdMessage = getEuclideanGcdMessage(input1String, input2String, iterations);
        
        return
            new NtpPanel()
            .setToBoxLayoutWithPageAxis()
            .add(createAnswerSubHeadingLabel(EUCLIDEAN_TABLE_HEADING))
            .add(iterationsTable)
            .add(createCenteredAnswerContentLabel(gcdMessage))
            .setMaxSizeToPreferredSize();
    }
    
    private static String getAnswerMainHeading(String input1String, String input2String) {
        return String.format("GCD and LCM Info for %s and %s", input1String, input2String);
    }

    private static final String PF_INFO_HEADING = "Prime Factorization Info";

    
    public static final class Section extends DoubleInputSection {
        public Section() {
            super(
                "GCD and LCM",
                INFO_PARAGRAPHS,
                MIN_INPUT,
                MAX_INPUT,
                "GCD and LCM info for those numbers",
                "GCDs and LCMs"
            );
        }
        
        @Override
        public String getCliAnswer(
            long input1Long, long input2Long,
            String input1String, String input2String
        ) {
            // Call getEuclideanCliAnswer first to see if it throws.
            String euclideanAnswer =
                getEuclideanCliAnswer(input1Long, input2Long, input1String, input2String);
            Stream<String> pfInfoSentences =
                new PrimeFactorization.GcdAndLcmAnswer(input1Long, input2Long, input1String, input2String)
                .getInfoSentences();
            String pfAnswer =
                NtpCli.buildStringWithStreamElementsOnSeparateLines(PF_INFO_HEADING, pfInfoSentences);
            return String.join(
                "\n\n",
                getAnswerMainHeading(input1String, input2String),
                euclideanAnswer,
                pfAnswer
            );
        }
        
        @Override
        public List<Component> getGuiComponents(
            long input1Long, long input2Long,
            String input1String, String input2String
        ) {
            NtpPanel euclideanPanel =
                getEuclideanPanel(input1Long, input2Long, input1String, input2String);
            Stream<String> pfInfoSentences =
                new PrimeFactorization.GcdAndLcmAnswer(input1Long, input2Long, input1String, input2String)
                .getInfoSentences();
            
            NtpPanel pfInfoPanel =
                new NtpPanel()
                .setToBoxLayoutWithPageAxis()
                .add(createAnswerSubHeadingLabel(PF_INFO_HEADING))
                .add(NtpTextArea.createWithStreamElementsOnSeparateLines(pfInfoSentences));
            
            return List.of(
                createAnswerMainHeadingLabel(getAnswerMainHeading(input1String, input2String)),
                createGapBetweenAnswerSections(),
                euclideanPanel,
                createGapBetweenAnswerSections(),
                pfInfoPanel
            );
        }
    }
}
