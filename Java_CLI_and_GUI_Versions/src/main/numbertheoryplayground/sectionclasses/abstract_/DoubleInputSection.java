package numbertheoryplayground.sectionclasses.abstract_;

import java.awt.Component;
import java.util.List;

import static numbertheoryplayground.Misc.createStringWithCommas;

/**
 * Superclass for Sections that require 2 input longs for their calculation(s).
 */
public abstract non-sealed class DoubleInputSection extends Section {
    protected DoubleInputSection(
        String heading,
        String info,
        long minInput,
        long maxInput,
        String actionSentencesEnding,
        String cliInfoOptionEnding
    ) {
        super(heading, info, minInput, maxInput, actionSentencesEnding, cliInfoOptionEnding);
    }
    
    /**
     * Does the calculation(s) for this section and returns a string with info about it to be
     * displayed in the CLI app.
     */
    public abstract String getCliAnswer(
        long input1Long,
        long input2Long,
        String input1String,
        String input2String
    );
    
    /**
     * Does the calculation(s) for this section and returns a list of GUI components with info about it.
     */
    public abstract List<Component> getGuiComponents(
        long input1Long,
        long input2Long,
        String input1String,
        String input2String
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