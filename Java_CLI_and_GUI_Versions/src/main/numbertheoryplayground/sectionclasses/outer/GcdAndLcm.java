package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpPanel;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.DoubleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.gui.NtpGui.*;
import static numbertheoryplayground.sectionclasses.outer.PrimeFactorization.FactorAndPower;

/**
 * Utility class related to GCDs and LCMs and the section for it.
 */
public class GcdAndLcm {
    private static final String INTRO_AND_EUCLIDEAN_INFO = """
GCD stands for greatest common divisor and is also known as greatest common factor, or GCF.
LCM stands for least common multiple. To find the GCD and LCM of 2 whole numbers, you could
manually do some division and multiplication but there are other ways to find them.

The Euclidean algorithm can be used to find just the GCD of 2 whole numbers. This was named
after the ancient Greek mathematician Euclid.

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
iterations until we get a remainder of 0.""";

    private static final String OTHER_INFO = """
2 whole numbers are said to be coprime if their GCD is 1. Therefore, coprime numbers don't have
any common factors in their PFs. the input numbers whose LCM is the highest are
5 quadrillion (5,000,000,000,000,000), the max input, and
4,999,999,999,999,999, the max input - 1. Their LCM is
24,999,999,999,999,995,000,000,000,000,000, or
24 nonillion 999 octillion 999 septillion 999 sextillion 999 quintillion 995 quadrillion!
It has 32 digits. Trillion is before quadrillion. A pair of input numbers whose LCM has the
highest amount of prime factors is 4,503,599,627,370,496 (2^52) and
1,853,020,188,851,841 (3^32). Their LCM is 8,345,261,032,023,157,253,752,158,683,136, or
8 nonillion ... A pair of input numbers whose LCM might have the highest amount of unique prime
factors is 304,250,263,527,210, the product of the first 13 prime numbers, and
133,869,006,807,307, the product of the next 8 prime numbers. Their LCM is
40,729,680,599,249,024,150,621,323,470, or 40 octillion ... It has 29 digits and 21 unique
prime factors and its PF is
2 × 3 × 5 × 7 × 11 × 13 × 17 × 19 × 23 × 29 × 31 × 37 × 41 × 47 × 53 × 59 × 61 × 67 × 71 × 73!""";
    
    
    // This section uses prime factorizations so the min input for those will be used.
    private static final long MIN_INPUT = PrimeFactorization.MIN_INPUT;
    private static final long MAX_INPUT = FIVE_QUADRILLION;
    
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
        final int columnGap = 4;
        
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
     * Returns an NtpPanel with a heading label, table, and label with a message about what the GCD of
     * input1Long and input2Long is. The table has columns for the max number, min number, and remainder for each
     * iteration of the Euclidean algorithm performed on input1Long and input2Long.
     */
    private static NtpPanel getEuclideanPanel(
        long input1Long, long input2Long,
        String input1String, String input2String
    ) {
        List<EuclideanIteration> iterations = getEuclideanIterations(input1Long, input2Long);
        
        Function<EuclideanIteration, Stream<String>> getIterationRowStrings =
            ei -> Stream.of(ei.maxString(), ei.minString(), ei.remainderString());
        
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
    
    
    private static final String PF_INFO = """
The GCD and LCM of 2 whole numbers > 1 can be found by looking at their prime factorizations (PFs).
If those numbers don't have any common prime factors, then the GCD is 1. If they do have common
prime factors, then the GCD PF consists of all the common prime factors and the power of each
factor is the min of the powers of that factor in the 2 PFs. The LCM PF consists of all factors
that are in either of the PFs of the 2 numbers. If a factor is in both PFs, then the power of
that factor in the LCM PF is the max of the powers of that factor in the 2 PFs. If a factor is
unique to one of the PFs, then that factor and its power are in the LCM PF.

Let's find the GCD and LCM of 6 and 35 using their PFs. The PF of 6 is 2 × 3 and the PF of 35 is 5 × 7.
There are no common prime factors so the GCD is 1. The LCM PF is 2 × 3 × 5 × 7, which is 210.

Let's find the GCD and LCM of 54 and 99. The PF of 54 is 2 × 3^3 and the PF of 99 is 3^2 × 11.
3 is the only common prime factor and the min power of it is 2 so the GCD PF is 3^2, which is 9.
The max power of 3 is 3 so 3^3 is in the LCM PF. The LCM PF is 2 × 3^3 × 11, which is 594.

The input numbers whose LCM is the highest are 5 quadrillion (5,000,000,000,000,000),
the max input; and 5 quadrillion - 1. The LCM is 24,999,999,999,999,995,000,000,000,000,000,
or 24 nonillion 999 octillion 999 septillion 999 sextillion 999 quintillion 995 quadrillion!
It has 32 digits. Trillion is before quadrillion.

A pair of input numbers whose LCM might have the highest amount of unique prime factors is
304,250,263,527,210, the product of the first 13 prime numbers; and 133,869,006,807,307,
the product of the next 8 prime numbers. The LCM is 40,729,680,599,249,024,150,621,323,470,
or 40 octillion ... It has 29 digits and 21 unique prime factors and its PF is
2 × 3 × 5 × 7 × 11 × 13 × 17 × 19 × 23 × 29 × 31 × 37 × 41 × 47 × 53 × 59 × 61 × 67 × 71 × 73!
Other pairs of input numbers have the same LCM, such as that first input number divided by 2 and
the second input number multiplied by 2.""";
    
    /**
     * This class uses PFs to find the GCD and LCM of 2 integers.
     */
    static class PrimeFactorizationAnswer {
        private final PrimeFactorization input1Pf;
        
        private final PrimeFactorization input2Pf;
        
        /**
         * If the GCD of the inputs is 1, this is null since only integers ≥ 2 have a prime factorization.
         */
        private final PrimeFactorization gcdPf;
        
        private final PrimeFactorization lcmPf;
        
        PrimeFactorizationAnswer(
            long input1Long, long input2Long,
            String input1String, String input2String
        ) {
            assertIsInRange(input1Long, MIN_INPUT, MAX_INPUT);
            assertIsInRange(input2Long, MIN_INPUT, MAX_INPUT);
            
            input1Pf = new PrimeFactorization(input1Long, input1String);
            input2Pf = new PrimeFactorization(input2Long, input2String);
            var gcdPfFps = new ArrayList<FactorAndPower>();
            var lcmPfFps = new ArrayList<FactorAndPower>();
            
            for (FactorAndPower fp : input1Pf.getFps()) {
                long factor = fp.factor();
                int power1 = fp.power();
                
                input2Pf
                .getPowerOf(factor)
                .ifPresentOrElse(
                    (power2) -> {
                        gcdPfFps.add(new FactorAndPower(factor, Math.min(power1, power2)));
                        lcmPfFps.add(new FactorAndPower(factor, Math.max(power1, power2)));
                    },
                    () -> lcmPfFps.add(new FactorAndPower(factor, power1))
                );
            }
            
            for (FactorAndPower fp : input2Pf.getFps()) {
                if (!input1Pf.containsFactor(fp.factor())) {
                    lcmPfFps.add(new FactorAndPower(fp.factor(), fp.power()));
                }
            }
            
            gcdPf =
                gcdPfFps.isEmpty()
                ? null
                : new PrimeFactorization(gcdPfFps);
            
            lcmPf = new PrimeFactorization(lcmPfFps);
        }
        
        Optional<PrimeFactorization> getGcdPf() {
            return Optional.ofNullable(gcdPf);
        }
        
        PrimeFactorization getLcmPf() {
            return lcmPf;
        }
        
        private Stream<String> getInfoSentences() {
            String gcdSentence =
                getGcdPf()
                .map(pf -> getGcdOrLcmPfSentence("GCD", gcdPf))
                .orElseGet(() -> "There are no common prime factors so the GCD is 1.");
            
            return Stream.of(
                input1Pf.getInfoSentence(),
                input2Pf.getInfoSentence(),
                gcdSentence,
                getGcdOrLcmPfSentence("LCM", lcmPf)
            );
        }
        
        private static String getGcdOrLcmPfSentence(String gcdOrLcmText, PrimeFactorization pf) {
            var textAfterIs = pf.isForAPrimeNumber() ? "" : pf + ", which is ";
            return String.format(
                "The PF of the %s is %s%s.",
                gcdOrLcmText, textAfterIs, pf.getCorrespondingBigIntString()
            );
        }
    }
    
    
    private static String getAnswerMainHeading(String input1String, String input2String) {
        return String.format("GCD and LCM Info for %s and %s", input1String, input2String);
    }
    
    private static final String PF_INFO_HEADING = "Prime Factorization Info";
    
    
    public static final class Section extends DoubleInputSection {
        public Section() {
            super(
                "GCD and LCM",
                String.join("\n\n", INTRO_AND_EUCLIDEAN_INFO, PF_INFO, OTHER_INFO),
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
                new PrimeFactorizationAnswer(input1Long, input2Long, input1String, input2String)
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
            // Call getEuclideanPanel first to see if it throws.
            NtpPanel euclideanPanel =
                getEuclideanPanel(input1Long, input2Long, input1String, input2String);
            Stream<String> pfInfoSentences =
                new PrimeFactorizationAnswer(input1Long, input2Long, input1String, input2String)
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
