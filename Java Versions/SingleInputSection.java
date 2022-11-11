import java.awt.Component;
import java.util.List;

/**
 * Superclass for Sections that require 1 input int for their algorithm(s).
 */
public abstract class SingleInputSection extends Section {
    protected SingleInputSection(
        String headingText,
        List<String> info,
        int minInputInt,
        int maxInputInt,
        String actionSentenceEnding,
        String infoOptionEndingForCli,
        String inputConstraintsSentenceFormat
    ) {
        super(
            headingText,
            info,
            minInputInt,
            maxInputInt,
            actionSentenceEnding,
            infoOptionEndingForCli,
            inputConstraintsSentenceFormat
        );
    }

    protected SingleInputSection(
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
        return getCliAnswer(getRandomValidInt());
    }

    /**
     * Runs the algorithm(s) for this section using inputInt and creates a string with info about the results
     * of the algorithm(s).
     * @throws IllegalArgumentException if inputInt is invalid input for this section.
     */
    public abstract String getCliAnswer(int inputInt);

    /**
     * Runs the algorithm(s) for this section using inputInt and creates GUI components with info about the
     * results of the algorithm(s).
     * @throws IllegalArgumentException if inputInt is invalid input for this section.
     */
    public abstract List<Component> getGuiComponents(int inputInt);
}
