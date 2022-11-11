import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class related to divisibility and the section for it.
 */
public class Divisibility {
    private static final String introParagraph =
        "The factors of a number are all the numbers that can evenly divide that number.";

    /**
     * Determines if a is divisible by b without using any special tricks
     * @return <code>a % b == 0</code>, which is true if a is divisible by b and false otherwise.
     */
    public static boolean isDivisible(long a, long b) {
        return a % b == 0;
    }

    public static boolean isEven(int i) {
        return isDivisible(i, 2);
    }

    public static boolean isOdd(int i) {
        return !isEven(i);
    }

    // This section uses prime factorizations so the input constraints for those will be used
    public static final int minInputInt = PrimeFactorization.minInputInt;
    public static final int maxInputInt = PrimeFactorization.maxInputInt;
        }
        }

            if (isDivisibleBy3) {
            }

                if (isDivisibleBy3) {
                }
            }
        }
    }

    private static final String tricksInfoParagraph =
        "Some special tricks can be used to find some of the factors of a number. If the " +
        "sum of the digits of a number is divisible by 3, then that number is divisible " +
        "by 3. If the sum of the digits of a number is divisible by 9, then that number " +
        "is divisible by 9. If a number is even and divisible by 3, then it is also " +
        "divisible by 6. If the last 2 digits of a number is divisible by 4, then that " +
        "number is divisible by 4. If the last 3 digits of a number is divisible by 8, " +
        "then that number is divisible by 8. If a number is divisible by both 3 and 4 " +
        "then it is also divisible by 12.";

    private static final String pfInfoParagraph =
        "Another way you can tell what factors a number has and how many factors it has is " +
        "by looking at it's prime factorization. To find the number of factors, you take " +
        "all the powers of the prime factors, add 1 to each and then multiply them all " +
        "together. All the \"sub-factorizations\" of this prime factorization are the prime " +
        "factorizations of all the factors. Some examples of \"sub-factorizations\" are 2 and " +
        "2 x 3 in the prime factorization 2 x 3 x 5.";

    /**
     * Creates a sequential stream containing the factors of intParam besides 1 and intParam. The tests
     * will collect this stream to a list and the applications will do some mapping on this before collecting
     * it to a list, as seen in the getFactorPfStrings method below.
     * @param intParam
     */
    public static Stream<Integer> getFactorsStream(int intParam) {
        return
            IntStream.rangeClosed(2, intParam / 2)
            .filter(i -> isDivisible(intParam, i))
            .boxed();
    }

    /**
     * PFs can be used to find the factors of an integer. This can be done by finding what I consider
     * to be "sub-factorizations" of a PF. These are PFs within a PF. For the PF 2 x 3 x 5 x 7, some
     * examples of sub-factorizations are 2, 2 x 3, and 2 x 3 x 5. In order to find sub-factorizations
     * programmatically, you would have to use the factorsAndPowers map of a PF object and find all possible combinations
     * of factors and powers. I don't know how to do that so I'll just do some iteration to find the factors
     * and then create PF objects from those factors.
     * @return A list of prime factorization strings for all factors of intParam.
     * @throws IllegalArgumentException
     */
    public static List<String> getFactorPfStrings(int intParam) {
        assertIsInRange(intParam, minInputInt, maxInputInt);
        return
            getFactorsStream(intParam)
            .map(i -> new PrimeFactorization(i).toStringWithCorrespondingLong())
            .collect(Collectors.toList());
    }

    private static String getDivisibilityInfoHeading(int inputInt) {
        return String.format("Divisibility info for %s", getLongStringWithCommas(inputInt));
    }

    private static final String tricksInfoHeading = "Info acquired by using special tricks";
    private static final String pfInfoHeading = "Prime factorization info";
    private static final String subfactorizationsSentence =
        "By looking at the \"sub-factorizations\", we can see the factors are:";

    private static String getPrimeNumberSentence(int inputInt) {
        return String.format("%s is prime and doesn't have any factors other than itself and 1.", getLongStringWithCommas(inputInt));
    }

    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Divisibility",
                List.of(introParagraph, tricksInfoParagraph, pfInfoParagraph),
                minInputInt,
                maxInputInt,
                "get divisibility info about it",
                "divisibility"
            );
        }

        /**
         * @return A string that shows a paragraph of divisibility info acquired by using special tricks followed by a
         * section of information about divisibility info relating to prime factorizations.
         * @throws IllegalArgumentException if inputInt is invalid input for the DIVISIBILITY section.
         */
        @Override
        public String getCliAnswer(int inputInt) throws IllegalArgumentException {
            String tricksInfo = getDivisibilityInfoViaTricks(inputInt);
            tricksInfo = NTPCLI.insertNewLines(tricksInfo);

            StringJoiner linesJoiner =
                new StringJoiner("\n")
                .add(getDivisibilityInfoHeading(inputInt))
                .add("")
                .add(tricksInfoHeading)
                .add(tricksInfo)
                .add("")
                .add(pfInfoHeading);

            PrimeFactorization pf = new PrimeFactorization(inputInt);
            if (pf.isForAPrimeNumber()) {
                linesJoiner.add(
                    NTPCLI.insertNewLines(pf.getInfoMessage() + ". " + getPrimeNumberSentence(inputInt))
                );
            } else {
                String factorsListPrefix =
                    pf.getInfoMessage() + ". " + pf.getNumberOfFactorsInfo() + " " + subfactorizationsSentence;
                factorsListPrefix = NTPCLI.insertNewLines(factorsListPrefix);
                String factorsListWithPrefix =
                    NTPCLI.stringifyList(getFactorPfStrings(inputInt), factorsListPrefix);
                linesJoiner.add(factorsListWithPrefix);
            }

            return linesJoiner.toString();
        }

        /**
         * @return A list of GUI components which includes a main heading label, components for a tricks info section,
         * components for a prime factorization info section, and gaps where appropriate.
         * @throws IllegalArgumentException
         */
        @Override
        public List<Component> getGuiComponents(int inputInt) throws IllegalArgumentException {
            String tricksInfo = getDivisibilityInfoViaTricks(inputInt);
            NTPTextArea tricksInfoArea = new NTPTextArea(AnswerPanel.contentFont);
            tricksInfoArea.setText(tricksInfo);

            PrimeFactorization pf = new PrimeFactorization(inputInt);
            NTPTextArea pfInfoArea = new NTPTextArea(AnswerPanel.contentFont);
            NTPPanel factorsPanel = null;
            if (pf.isForAPrimeNumber()) {
                pfInfoArea.setText(pf.getInfoMessage() + ". " + getPrimeNumberSentence(inputInt));
            } else {
                pfInfoArea.setText(
                    pf.getInfoMessage() + ". " + pf.getNumberOfFactorsInfo() + " " + subfactorizationsSentence
                );
            }

            return List.of(
                NTPPanel.createCenteredLabel(getDivisibilityInfoHeading(inputInt), AnswerPanel.mainHeadingFont),
                NTPGUI.createGap(15),
                NTPPanel.createCenteredLabel(tricksInfoHeading, AnswerPanel.subHeadingFont),
                NTPGUI.createGap(5),
                tricksInfoArea,
                NTPGUI.createGap(10),
                NTPPanel.createCenteredLabel(pfInfoHeading, AnswerPanel.subHeadingFont),
                NTPGUI.createGap(5),
                pfInfoArea,
                factorsPanel != null ? factorsPanel : NTPGUI.createGap(0)
            );
        }
    }
}
