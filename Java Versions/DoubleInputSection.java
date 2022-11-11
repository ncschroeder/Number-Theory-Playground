import java.awt.Component;
import java.util.List;

/**
 * Superclass for Sections that require 2 input ints for their algorithm(s).
 */
public abstract class DoubleInputSection extends Section {
    public DoubleInputSection(
        String headingText,
        List<String> info,
        int minInputInt,
        int maxInputInt,
        String actionSentenceEnding,
        String infoOptionEndingForCli
    ) {
        super(
            headingText,
            info,
            minInputInt,
            maxInputInt,
            actionSentenceEnding,
            infoOptionEndingForCli
        );
    }

    @Override
    public final String getRandomCliAnswer() {
        return getCliAnswer(getRandomValidInt(), getRandomValidInt());
    }

    /**
     * Runs the algorithm(s) for this section using inputInt1 and inputInt2 and creates a string with info about
     * the results of the algorithm(s).
     * @throws IllegalArgumentException if inputInt1 or inputInt2 are invalid input for this section.
     */
    public abstract String getCliAnswer(int inputInt1, int inputInt2);

    /**
     * Runs the algorithm(s) for this section using inputInt1 and inputInt2 and creates GUI components with info
     * about the results of the algorithm(s).
     * @throws IllegalArgumentException if inputInt1 or inputInt2 are invalid input for this section.
     */
    public abstract List<Component> getGuiComponents(int inputInt1, int inputInt2);
}
