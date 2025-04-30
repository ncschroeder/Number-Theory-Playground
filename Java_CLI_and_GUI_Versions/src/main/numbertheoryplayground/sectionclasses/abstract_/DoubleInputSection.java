package numbertheoryplayground.sectionclasses.abstract_;

import java.awt.Component;
import java.util.List;

/**
 * Superclass for Sections that require 2 input ints for their algorithm(s).
 */
public abstract non-sealed class DoubleInputSection extends Section {
    protected DoubleInputSection(
        String heading,
        long minInput,
        long maxInput,
        String actionSentencesEnding,
        String cliInfoOptionEnding,
        String info
    ) {
        super(heading, minInput, maxInput, actionSentencesEnding, cliInfoOptionEnding, info);
    }
    
    /**
     * Used by the CLI to run the algorithm(s) for this section using input1 and input2 and create a
     * string with info about the results of the algorithm(s).
     */
    public abstract String getCliAnswer(long input1, long input2);
    
    /**
     * Used by the GUI to run the algorithm(s) for this section using input1 and input2 and create
     * GUI components with info about the results of the algorithm(s).
     */
    public abstract List<Component> getGuiComponents(long input1, long input2);
    
    @Override
    public final String getRandomCliAnswer() {
        return getCliAnswer(getRandomInput(), getRandomInput());
    }
}
