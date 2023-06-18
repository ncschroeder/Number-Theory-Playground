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
     * Used by the CLI to run the algorithm(s) for this section using input1 and input2 and create a
     * string with info about the results of the algorithm(s)
     */
    public abstract String getCliAnswer(int input1, int input2);
    
    /**
     * Used by the GUI to run the algorithm(s) for this section using input1 and input2 and create
     * GUI components with info about the results of the algorithm(s)
     */
    public abstract List<Component> getGuiComponents(int input1, int input2);
    
    @Override
    public final String getRandomCliAnswer() {
        return getCliAnswer(getRandomValidInt(), getRandomValidInt());
    }
}
