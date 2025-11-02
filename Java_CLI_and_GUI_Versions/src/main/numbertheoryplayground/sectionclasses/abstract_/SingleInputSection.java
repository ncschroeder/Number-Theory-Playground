package numbertheoryplayground.sectionclasses.abstract_;

import java.awt.Component;
import java.util.List;

import static numbertheoryplayground.Misc.createStringWithCommas;

/**
 * Superclass for Sections that require 1 input long for their algorithm(s).
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
     * Used by the CLI to run the algorithm(s) for this section using inputLong and create a string
     * with info about the results of the algorithm(s).
     */
    public abstract String getCliAnswer(long inputLong, String inputString);
    
    /**
     * Used by the GUI to run the algorithm(s) for this section using inputLong and create GUI
     * components with info about the results of the algorithm(s).
     */
    public abstract List<Component> getGuiComponents(long inputLong, String inputString);
    
    @Override
    public final String getRandomCliAnswer() {
        var inputLong = getRandomInput();
        return getCliAnswer(inputLong, createStringWithCommas(inputLong));
    }
}