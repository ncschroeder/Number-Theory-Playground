package numbertheoryplayground.sectionclasses.abstract_;

import java.awt.Component;
import java.util.List;

import static numbertheoryplayground.Misc.createStringWithCommas;

/**
 * Superclass for Sections that require 2 input ints for their algorithm(s).
 */
public abstract non-sealed class DoubleInputSection extends Section {
    protected DoubleInputSection(
        String heading,
        List<String> infoParagraphs,
        long minInput,
        long maxInput,
        String actionSentencesEnding,
        String cliInfoOptionEnding
    ) {
        super(heading, infoParagraphs, minInput, maxInput, actionSentencesEnding, cliInfoOptionEnding);
    }
    
    /**
     * Used by the CLI to run the algorithm(s) for this section using input1Long and input2Long
     * and create a string with info about the results of the algorithm(s).
     */
    public abstract String getCliAnswer(
        long input1Long, long input2Long,
        String input1String, String input2String
    );
    
    /**
     * Used by the GUI to run the algorithm(s) for this section using input1Long and input2Long
     * and create GUI components with info about the results of the algorithm(s).
     */
    public abstract List<Component> getGuiComponents(
        long input1Long, long input2Long,
        String input1String, String input2String
    );
    
    @Override
    public final String getRandomCliAnswer() {
        var input1Long = getRandomInput();
        var input2Long = getRandomInput();
        var input1String = createStringWithCommas(input1Long);
        var input2String = createStringWithCommas(input2Long);
        return getCliAnswer(input1Long, input2Long, input1String, input2String);
    }
}
