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
import static numbertheoryplayground.sectionclasses.outer.PrimeFactorization.FactorAndExponent;

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
    
    /*
    Some calculations for this section are: perform the Euclidean algorithm on 2 input numbers
    and display a table with info about all iterations. The other calculations are explained
    below the OTHER_INFO string.
     */
    
    /**
     * This section creates PrimeFactorization objects so the min input for those is used.
     */
    private static final long MIN_INPUT = PrimeFactorization.MIN_INPUT;
    private static final long MAX_INPUT = FIVE_QUADRILLION;
    
    private static String getAnswerMainHeading(String input1String, String input2String) {
        return String.format("GCD and LCM Info for %s and %s", input1String, input2String);
    }
    
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
    
    // Text used for the Euclidean algorithm table
    private static final String EUCLIDEAN_TABLE_HEADING = "Euclidean Algorithm Iterations";
    private static final String EUCLIDEAN_MAX_COLUMN_HEADING = "Max";
    private static final String EUCLIDEAN_MIN_COLUMN_HEADING = "Min";
    private static final String EUCLIDEAN_REMAINDER_COLUMN_HEADING = "Remainder";
    
    /**
     * Returns a message about what the GCD of 2 input numbers is. iterations should be a list
     * returned from making a call to getEuclideanIterations. The min of the last object in that
     * list is the GCD.
     */
    private static String getEuclideanGcdMessage(List<EuclideanIteration> iterations) {
        return String.format("The GCD is %s.", iterations.getLast().minString());
    }
    
    /**
     * Returns a string with a heading, table, and message about what the GCD of input1Long and input2Long is.
     * The table has columns for the max number, min number, and remainder for each iteration of the
     * Euclidean algorithm performed on input1Long and input2Long.
     */
    private static String getEuclideanCliAnswer(long input1Long, long input2Long) {
        List<EuclideanIteration> iterations = getEuclideanIterations(input1Long, input2Long);
        
        /*
        Make column widths the length of the longest element in the column + the column gap.
        The first iteration has the longest elements of all iterations.
         */
        final int columnGap = 4;
        EuclideanIteration iteration1 = iterations.getFirst();
        
        int maxColumnWidth =
            Math.max(EUCLIDEAN_MAX_COLUMN_HEADING.length(), iteration1.maxString().length()) +
            columnGap;
        
        int minColumnWidth =
            Math.max(EUCLIDEAN_MIN_COLUMN_HEADING.length(), iteration1.minString().length()) +
            columnGap;
        
        String headRow =
            NtpCli.getRowFor3ColumnTable(
                EUCLIDEAN_MAX_COLUMN_HEADING,
                maxColumnWidth,
                EUCLIDEAN_MIN_COLUMN_HEADING,
                minColumnWidth,
                EUCLIDEAN_REMAINDER_COLUMN_HEADING
            );
        
        String collectingPrefix = EUCLIDEAN_TABLE_HEADING + '\n' + headRow + '\n';
        String collectingSuffix = '\n' + getEuclideanGcdMessage(iterations);
        
        return
            iterations
            .stream()
            .map(i ->
                NtpCli.getRowFor3ColumnTable(
                    i.maxString(),
                    maxColumnWidth,
                    i.minString(),
                    minColumnWidth,
                    i.remainderString()
                )
            )
            .collect(Collectors.joining("\n", collectingPrefix, collectingSuffix));
    }
    
    /**
     * Returns an NtpPanel with a heading label, table, and label with a message about what the GCD of
     * input1Long and input2Long is. The table has columns for the max number, min number, and remainder
     * for each iteration of the Euclidean algorithm performed on input1Long and input2Long.
     */
    private static NtpPanel getEuclideanPanel(long input1Long, long input2Long) {
        List<EuclideanIteration> iterations = getEuclideanIterations(input1Long, input2Long);
        
        List<String> columnHeadings =
            List.of(
                EUCLIDEAN_MAX_COLUMN_HEADING,
                EUCLIDEAN_MIN_COLUMN_HEADING,
                EUCLIDEAN_REMAINDER_COLUMN_HEADING
            );
        
        Function<EuclideanIteration, Stream<String>> getIterationRowStrings =
            ei -> Stream.of(ei.maxString(), ei.minString(), ei.remainderString());
        
        NtpPanel iterationsTable =
            NtpPanel.createTablePanel(columnHeadings, iterations.stream(), getIterationRowStrings);
        
        String gcdMessage = getEuclideanGcdMessage(iterations);
        
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
prime factors, then the GCD PF consists of all the common prime factors and the exponent of each
factor is the min of the exponents of that factor in the 2 PFs. The LCM PF consists of all the
prime factors that are in either of the PFs of the 2 numbers. If a factor is in both PFs, then the
exponent of that factor in the LCM PF is the max of the exponents of that factor in the 2 PFs.
If a factor is unique to one of the PFs, then that factor and its exponent are in the LCM PF.

Let's find the GCD and LCM of 6 and 35 using their PFs. The PF of 6 is 2 × 3 and the PF of 35 is 5 × 7.
There are no common prime factors so the GCD is 1. The LCM PF is 2 × 3 × 5 × 7, which is 210.

Let's find the GCD and LCM of 54 and 99. The PF of 54 is 2 × 3^3 and the PF of 99 is 3^2 × 11.
3 is the only common prime factor and the min exponent of it is 2 so the GCD PF is 3^2, which is 9.
The max exponent of 3 is 3 so 3^3 is in the LCM PF. The LCM PF is 2 × 3^3 × 11, which is 594.""";
    
    private static final String OTHER_INFO = """
2 whole numbers are said to be coprime if their GCD is 1. Therefore, coprime numbers don't have
any common prime factors in their PFs. The input numbers that have the largest LCM are
5 quadrillion (5,000,000,000,000,000), the max input; and 5 quadrillion − 1. Their LCM is
24,999,999,999,999,995,000,000,000,000,000
(24 nonillion 999 octillion 999 septillion 999 sextillion 999 quintillion 995 quadrillion)!
It has 32 digits. Trillion is before quadrillion. A pair of input numbers whose LCM has the
most prime factors is 4,503,599,627,370,496 (2^52) and 1,853,020,188,851,841 (3^32). Their LCM
has a PF of 2^52 × 3^32, has 84 prime factors, and is 8,345,261,032,023,157,253,752,158,683,136
(8 nonillion ...). A pair of input numbers whose LCM might have the most unique prime factors is
304,250,263,527,210, the product of the first 13 prime numbers; and 133,869,006,807,307, the
product of the next 8 prime numbers. Their LCM is the product of the first 21 prime numbers, or
40,729,680,599,249,024,150,621,323,470 (40 octillion ...). Its PF is
2 × 3 × 5 × 7 × 11 × 13 × 17 × 19 × 23 × 29 × 31 × 37 × 41 × 47 × 53 × 59 × 61 × 67 × 71 × 73!""";
    
    /*
    The other calculations for this section are: find the PFs of 2 input numbers and use these
    to find the PFs of the GCD and LCM.
     */
    
    static final class PrimeFactorizationAnswer {
        private static final String HEADING = "Prime Factorizations Info";
        
        /**
         * If the GCD of the inputs is 1, then this is null since only whole numbers > 1 have a PF.
         */
        private final PrimeFactorization gcdPf;
        
        private final PrimeFactorization lcmPf;
        
        private final Stream<String> infoSentences;
        
        PrimeFactorizationAnswer(
            long input1Long,
            long input2Long,
            String input1String,
            String input2String
        ) {
            assertIsInRange(input1Long, MIN_INPUT, MAX_INPUT);
            assertIsInRange(input2Long, MIN_INPUT, MAX_INPUT);
            
            var input1Pf = new PrimeFactorization(input1Long);
            PrimeFactorization input2Pf;
            
            if (input1Long == input2Long) {
                lcmPf = gcdPf = input2Pf = input1Pf;
            } else {
                input2Pf = new PrimeFactorization(input2Long);
                var gcdPfFes = new ArrayList<FactorAndExponent>();
                var lcmPfFes = new ArrayList<FactorAndExponent>();
                
                for (FactorAndExponent fe : input1Pf) {
                    long factor = fe.factor();
                    int exponent1 = fe.exponent();
                    
                    input2Pf
                    .getExponentOf(factor)
                    .ifPresentOrElse(
                        exponent2 -> {
                            gcdPfFes.add(new FactorAndExponent(factor, Math.min(exponent1, exponent2)));
                            lcmPfFes.add(new FactorAndExponent(factor, Math.max(exponent1, exponent2)));
                        },
                        () -> lcmPfFes.add(fe)
                    );
                }
                
                for (FactorAndExponent fe : input2Pf) {
                    if (!input1Pf.containsFactor(fe.factor())) {
                        lcmPfFes.add(fe);
                    }
                }
                
                gcdPf = gcdPfFes.isEmpty() ? null : new PrimeFactorization(gcdPfFes);
                lcmPfFes.sort(Comparator.comparingLong(FactorAndExponent::factor));
                lcmPf = new PrimeFactorization(lcmPfFes);
            }
            
            String gcdSentence =
                gcdPf != null
                ? getGcdOrLcmPfSentence("GCD", gcdPf)
                : "There are no common prime factors so the GCD is 1.";
            
            infoSentences =
                Stream.of(
                    input1Pf.getInfoSentence(input1String),
                    input2Pf.getInfoSentence(input2String),
                    gcdSentence,
                    getGcdOrLcmPfSentence("LCM", lcmPf)
                );
        }
    
        private static String getGcdOrLcmPfSentence(String gcdOrLcmText, PrimeFactorization pf) {
            var corBigIntString = createStringWithCommas(pf.getCorrespondingBigInt());
            var end =
                pf.isForAPrimeNumber()
                ? corBigIntString
                : String.format("%s, which is %s", pf, corBigIntString);
            
            return String.format("The PF of the %s is %s.", gcdOrLcmText, end);
        }
        
        PrimeFactorization getGcdPf() {
            return gcdPf;
        }
        
        PrimeFactorization getLcmPf() {
            return lcmPf;
        }
    }
    
    
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
            long input1Long,
            long input2Long,
            String input1String,
            String input2String
        ) {
            String euclideanAnswer = getEuclideanCliAnswer(input1Long, input2Long);
            
            Stream<String> pfInfoSentences =
                new PrimeFactorizationAnswer(input1Long, input2Long, input1String, input2String)
                .infoSentences;
            String pfAnswer =
                NtpCli.buildStringWithStreamElementsOnSeparateLines(
                    PrimeFactorizationAnswer.HEADING,
                    pfInfoSentences
                );
            
            return String.join(
                "\n\n",
                getAnswerMainHeading(input1String, input2String),
                euclideanAnswer,
                pfAnswer
            );
        }
        
        @Override
        public List<Component> getGuiComponents(
            long input1Long,
            long input2Long,
            String input1String,
            String input2String
        ) {
            NtpPanel euclideanPanel = getEuclideanPanel(input1Long, input2Long);
            
            Stream<String> pfInfoSentences =
                new PrimeFactorizationAnswer(input1Long, input2Long, input1String, input2String)
                .infoSentences;
            NtpPanel pfInfoPanel =
                new NtpPanel()
                .setToBoxLayoutWithPageAxis()
                .add(createAnswerSubHeadingLabel(PrimeFactorizationAnswer.HEADING))
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
