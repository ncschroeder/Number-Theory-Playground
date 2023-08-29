package com.nicholasschroeder.numbertheoryplayground;

import java.util.List;
import java.util.Random;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;

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
    
    
    private final String headingText;
    
    /**
     * List of paragraphs of information about this section. Each constructor creates this List using
     * List.of, which makes this List immutable.
     */
    private final List<String> info;
    
    private final int minInputInt;
    private final int maxInputInt;
    
    /**
     * There are a few "action" sentences used that say what the user is to do and what will happen in
     * response. For the GUI, there's 1 action sentence that starts with "Enter or generate an integer
     * and click the Calculate button to ". For the CLI, there's 1 action sentence for the custom input
     * option and 1 for the random input option. The custom input option starts with "An integer to ".
     * The random input option starts with "(r) to generate a random integer and ". This field is for
     * the endings of those sentences.
     */
    private final String actionSentenceEnding;
    
    /**
     * Mentions that the input int(s) should be >= what minInputInt is and <= what maxInputInt is.
     * For the Goldbach section, it's also mentioned that the input should be even.
     */
    private final String inputConstraintsSentence;
    
    /**
     * The beginning of the CLI info option is "(i) to get info about ". This field is what the ending
     * should be.
     */
    private final String cliInfoOptionEnding;
    
    
    /**
     * Creates a new Section with the info provided. inputConstraintsSentence is initialized using a
     * default input constraints sentence format.
     */
    protected Section(
        String headingText,
        List<String> info,
        int minInputInt,
        int maxInputInt,
        String actionSentenceEnding,
        String cliInfoOptionEnding
    ) {
        this(
            headingText,
            info,
            minInputInt,
            maxInputInt,
            actionSentenceEnding,
            cliInfoOptionEnding,
            "Have %s be >= %d && <= %s"
        );
    }
    
    /**
     * The inputConstraintsSentenceFormat is a format string consisting of an %s, %d, and %s in that order.
     */
    protected Section(
        String headingText,
        List<String> info,
        int minInputInt,
        int maxInputInt,
        String actionSentenceEnding,
        String cliInfoOptionEnding,
        String inputConstraintsSentenceFormat
    ) {
        this.headingText = headingText;
        this.info = info;
        this.minInputInt = minInputInt;
        this.maxInputInt = maxInputInt;
        this.actionSentenceEnding = actionSentenceEnding;
        this.cliInfoOptionEnding = cliInfoOptionEnding;
        
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
                maxInputString = stringifyWithCommas(maxInputInt);
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
     * Returns true if this is a SingleInputSection and false if this is a DoubleInputSection.
     */
    public final boolean isSingleInputSection() {
        return this instanceof SingleInputSection;
    }
    
    public final boolean isGoldbachSection() {
        return this instanceof Goldbach.Section;
    }
    
    
    private static final Random random = new Random();
    
    /**
     * Returns a random int in the range of valid input ints for this Section.
     */
    public int getRandomValidInt() {
        return Math.max(minInputInt, random.nextInt(maxInputInt));
    }
    
    /**
     * Used by the CLI to run the algorithm(s) for this section using random input and create a string
     * with info about the results of the algorithm(s).
     */
    public abstract String getRandomCliAnswer();
    
    
    public final String getHeadingText() {
        return headingText;
    }
    
    public final List<String> getInfo() {
        return info;
    }
    
    public final int getMinInputInt() {
        return minInputInt;
    }
    
    public final int getMaxInputInt() {
        return maxInputInt;
    }
    
    public final String getActionSentenceEnding() {
        return actionSentenceEnding;
    }
    
    public final String getInputConstraintsSentence() {
        return inputConstraintsSentence;
    }
    
    public final String getCliInfoOptionEnding() {
        return cliInfoOptionEnding;
    }
}