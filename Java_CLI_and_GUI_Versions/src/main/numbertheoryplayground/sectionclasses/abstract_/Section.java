package numbertheoryplayground.sectionclasses.abstract_;

import java.util.*;
import numbertheoryplayground.sectionclasses.outer.*;

import static numbertheoryplayground.Misc.*;

/**
 * Class for data and functionality of each section of the applications.
 */
public abstract sealed class Section
    permits SingleInputSection, DoubleInputSection {
    
    public static List<Section> createInstances() {
        return List.of(
            new PrimeNumbers.Section(),
            new TwinPrimePairs.Section(),
            new PrimeFactorization.Section(),
            new Divisibility.Section(),
            new GcdAndLcm.Section(),
            new GoldbachConjecture.Section(),
            new PythagoreanTriples.Section(),
            new TwoSquareTheorem.Section(),
            new FibonacciLikeSequences.Section(),
            new AncientMultiplication.Section()
        );
    }
    
    private static final Random random = new Random();
    
    
    private final String heading;
    
    /**
     * This list is immutable since Steam.toList returns an immutable list and that method is used
     * to create this list.
     */
    private final List<String> infoParagraphs;
    
    private final long minInput;
    
    private final long maxInput;
    
    /**
     * There are a few "action" sentences used that say what the user is to do and what will happen
     * in response. For the GUI, there's 1 action sentence that starts with "Enter or generate an
     * integer and click the Calculate button to ". For the CLI, there's 1 action sentence for the
     * custom input option and 1 for the random input option. The custom input option starts with
     * "An integer to ". The random input option starts with "(r) to generate a random integer
     * and ". This field is for the ending of those sentences. For example, for PrimeNumbers.Section,
     * this field is "get the first 30 prime numbers ≥ that integer."
     */
    private final String actionSentencesEnding;
    
    /**
     * The 1st input info sentence mentions that the input integer(s) should be ≥ what the min
     * input is and ≤ what the max input is. For the Goldbach Conjecture section, it's also
     * mentioned that the input should be even. The 2nd input info sentence is "Commas are optional."
     */
    private final String inputInfoSentences;
    
    /**
     * The beginning of the CLI info option is "(i) to get info about ".
     */
    private final String cliInfoOptionEnding;
    
    
    protected Section(
        String heading,
        long minInput,
        long maxInput,
        String actionSentencesEnding,
        String cliInfoOptionEnding,
        String info
    ) {
        this.heading = heading;
        this.minInput = minInput;
        this.maxInput = maxInput;
        this.actionSentencesEnding = String.format("get the %s.", actionSentencesEnding);
        this.cliInfoOptionEnding = cliInfoOptionEnding;
        
        infoParagraphs =
            Arrays.stream(info.split("\n\n"))
            .map(s -> s.replace('\n', ' '))
            .toList();
        
        var maxInputString = createStringWithCommas(maxInput);

        /*
        If the max input is one of the longs that's a key in the map below, have the 1st input
        info sentence say that the input integer(s) should be ≤ the corresponding string value
        in the map followed by the long with commas in parentheses. If the max input isn't one of
        the longs that's a key in the map, then just say that input integer(s) should be less than
        that long with commas. The max inputs that aren't keys are 10,000 and 1.5 million, the
        max inputs for the Pythagorean triples and Goldbach Conjecture sections, respectively.
        
        Longs can't be used as the selector for switch statements and expressions, which seems
        pathetic. Using a map seems to be the next best option.
         */
        
        Map<Long, String> maxInputsAndStringsWithWords =
            Map.of(
                FIVE_HUNDRED_BILLION, "500 billion",
                TEN_TRILLION, "10 trillion",
                ONE_QUADRILLION, "1 quadrillion",
                FIVE_QUADRILLION, "5 quadrillion",
                TEN_QUADRILLION, "10 quadrillion",
                NINE_QUINTILLION, "9 quintillion"
            );
        
        String maxInputSentencePart =
            Optional.ofNullable(maxInputsAndStringsWithWords.get(maxInput))
            .map(maxInputStringWithWord ->
                String.format("%s (%s)", maxInputStringWithWord, maxInputString)
            )
            .orElse(maxInputString);

        inputInfoSentences =
            String.format(
                "Have %s be %s≥ %d & ≤ %s. Commas are optional.",
                isSingleInputSection() ? "this integer" : "these integers",
                needsEvenInput() ? "even && " : "",
                minInput,
                maxInputSentencePart
            );
    }
    
    
    /**
     * Returns true if this is a SingleInputSection and false if this is a DoubleInputSection.
     */
    public final boolean isSingleInputSection() {
        return this instanceof SingleInputSection;
    }

    /**
     * Used in the constructor above and also by the InputPanel class nested within the MainPanel class.
     */
    public final boolean needsEvenInput() {
        return this instanceof GoldbachConjecture.Section;
    }
    
    /**
     * Returns a random int in the range of valid input ints for this Section.
     */
    public long getRandomInput() {
        return random.nextLong(minInput, maxInput + 1);
    }
    
    /**
     * Used by the CLI to run the algorithm(s) for this section using random input and create a string
     * with info about the results of the algorithm(s).
     */
    public abstract String getRandomCliAnswer();
    
    public final String getHeading() {
        return heading;
    }
    
    public final List<String> getInfoParagraphs() {
        return infoParagraphs;
    }
    
    public final long getMinInput() {
        return minInput;
    }
    
    public final long getMaxInput() {
        return maxInput;
    }
    
    public final String getActionSentencesEnding() {
        return actionSentencesEnding;
    }
    
    public final String getInputInfoSentences() {
        return inputInfoSentences;
    }
    
    public final String getCliInfoOptionEnding() {
        return cliInfoOptionEnding;
    }
}
