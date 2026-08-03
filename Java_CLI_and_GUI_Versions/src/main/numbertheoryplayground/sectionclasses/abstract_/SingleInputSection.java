package numbertheoryplayground.sectionclasses.abstract_;

import java.awt.Component;
import java.util.List;

import static numbertheoryplayground.Misc.createStringWithCommas;

/**
 * Superclass for Sections that require 1 input longs for their calculation(s).
 */
public abstract non-sealed class SingleInputSection extends Section {
    protected SingleInputSection(
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
    public abstract String getCliAnswer(long inputLong, String inputString);
    
    /**
     * Does the calculation(s) for this section and returns a list of GUI components with info about it.
     */
    public abstract List<Component> getGuiComponents(long inputLong, String inputString);
    
    @Override
    public final String getRandomCliAnswer() {
        var inputLong = getRandomInput();
        return getCliAnswer(inputLong, createStringWithCommas(inputLong));
    }
}