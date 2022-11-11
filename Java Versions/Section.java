import java.util.List;
import java.util.Random;


/**
 * Class for data and functionality of each section of the applications.
 */
public abstract class Section {
    public static List<Section> createInstances() {
        return List.of(
            new Primes.Section(),
            new TwinPrimes.Section(),
            new PrimeFactorization.Section(),
            new Divisibility.Section(),
            new GcdAndLcm.Section(),
            new Goldbach.Section(),
            new PythagoreanTriples.Section(),
            new TwoSquareTheorem.Section(),
            new FibonacciLikeSequences.Section(),
            new AncientMultiplication.Section()
        );
    }

    private static final Random random = new Random();

    /**
     * Creates a new Section with the info provided. inputConstraintsSentence is initialized using a default input constraints
     * sentence format.
     */
    protected Section(
        String headingText,
        List<String> info,
        int minInputInt,
        int maxInputInt,
        String actionSentenceEnding,
        String infoOptionEndingForCli
    ) {
        this(
            headingText,
            info,
            minInputInt,
            maxInputInt,
            actionSentenceEnding,
            infoOptionEndingForCli,
            "Have %s be >= %d && <= %s"
        );
    }

    /**
     *
     * @param minInputInt
     * @param maxInputInt
     * @param inputConstraintsSentenceFormat A format string consisting of an %s, %d, and %s in that order. The 1st
     *                                       one will be replaced with either "this integer" or "these integers",
     *                                       depending on the value of needs1Int. The 2nd is for minInputInt
     *                                       and the 3rd is for an English version of maxInputInt (e.g.
     *                                       if maxInputInt is 10000, then the %s will be replaced with "10 thousand").
     */
    protected Section(
        String headingText,
        List<String> info,
        int minInputInt,
        int maxInputInt,
        String actionSentenceEnding,
        String infoOptionEndingForCli,
        String inputConstraintsSentenceFormat
    ) {
        this.headingText = headingText;
        this.info = info;
        this.minInputInt = minInputInt;
        this.maxInputInt = maxInputInt;
        this.actionSentenceEnding = actionSentenceEnding;
        this.infoOptionEndingForCli = infoOptionEndingForCli;

        String maxInputString;
        switch (maxInputInt) {
            case oneThousand:
                maxInputString = "1 thousand";
                break;

            case tenThousand:
                maxInputString = "10 thousand";
                break;

            case oneHundredThousand:
                maxInputString = "100 thousand";
                break;

            case oneBillion:
                maxInputString = "1 billion";
                break;

            default:
                logError("maxInputString not properly initialized");
                maxInputString = getLongStringWithCommas(maxInputInt);
        }

        inputConstraintsSentence =
            String.format(
                inputConstraintsSentenceFormat,
                isSingleInputSection() ? "this integer" : "these integers",
                minInputInt,
                maxInputString
            );
    }

    /**
     * Whether or not this section needs 1 int for input. If this is false then 2 ints are needed.
     */
    public final boolean isSingleInputSection() {
        return this instanceof SingleInputSection;
    }

    /**
     * Goldbach section needs even ints for input so this is used by the GUI when an increment or
     * decrement button is clicked to determine whether or not the number to increment or decrement
     * to needs to be even.
     */
    public final boolean isGoldbachSection() {
        return this instanceof Goldbach.Section;
    }

    public abstract String getRandomCliAnswer();

    private final String headingText;
    public final String getHeadingText() {
        return headingText;
    }

    /**
     * List of paragraphs of information about this section. Each class creates this list using
     * <code>List.of</code>, which makes this list immutable.
     */
    private final List<String> info;
    public final List<String> getInfo() {
        return info;
    }

    private final int minInputInt;
    public final int getMinInputInt() {
        return minInputInt;
    }

    private final int maxInputInt;
    public final int getMaxInputInt() {
        return maxInputInt;
    }

    /**
     * There are a few "action" sentences used that say what the user is to do and what will happen in
     * response. For the GUI, there's 1 action sentence that starts with something along the lines
     * of "Enter or generate an integer and hit the Calculate button to ".
     * For the CLI, there's 1 action sentence for the custom input option and 1 for the random input
     * option. The custom input option starts with "An integer to ". The random input option starts with
     * "(r) to generate a random integer and ".
     * This attribute is for the endings of those sentences.
     */
    private final String actionSentenceEnding;
    public final String getActionSentenceEnding() {
        return actionSentenceEnding;
    }

    /**
     * Mentions that the input int(s) should be >= what minInputInt is and <= what maxInputInt is.
     * For the Goldbach section, it's also mentioned that the input should be even.
     */
    private final String inputConstraintsSentence;
    public final String getInputConstraintsSentence() {
        return inputConstraintsSentence;
    }

    /**
     * The beginning of the CLI info option is something along the lines of
     * "(i) to get info about ". This attribute is what the ending should be.
     */
    private final String infoOptionEndingForCli;
    public final String getInfoOptionEndingForCli() {
        return infoOptionEndingForCli;
    }

    public int getRandomValidInt() {
        return Math.max(minInputInt, random.nextInt(maxInputInt));
    }
}
