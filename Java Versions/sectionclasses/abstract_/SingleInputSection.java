package numbertheoryplayground.sectionclasses.abstract_;

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
        String cliInfoOptionEnding
    ) {
        super(
            headingText,
            info,
            minInputInt,
            maxInputInt,
            actionSentenceEnding,
            cliInfoOptionEnding
        );
    }
    
    /**
     * Constructor that the Goldbach section can use since it has a unique input constraints sentence format.
     */
    protected SingleInputSection(
        String headingText,
        List<String> info,
        int minInputInt,
        int maxInputInt,
        String actionSentenceEnding,
        String cliInfoOptionEnding,
        String inputConstraintsSentenceFormat
    ) {
        super(
            headingText,
            info,
            minInputInt,
            maxInputInt,
            actionSentenceEnding,
            cliInfoOptionEnding,
            inputConstraintsSentenceFormat
        );
    }

    /**
     * Used by the CLI to run the algorithm(s) for this section using the input and create a string with
     * info about the results of the algorithm(s).
     */
    public abstract String getCliAnswer(int input);
        
    /**
     * Used by the GUI to run the algorithm(s) for this section using the input and create GUI components
     * with info about the results of the algorithm(s).
     */
    public abstract List<Component> getGuiComponents(int input);
    
    @Override
    public final String getRandomCliAnswer() {
        return getCliAnswer(getRandomValidInt());
    }
}
