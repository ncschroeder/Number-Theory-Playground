package numbertheoryplayground.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Collections;
import java.util.List;

/**
 * Panel that displays either nothing, an invalid input message, or an answer for the current section.
 */
final class AnswerPanel extends NtpPanel {
    private final List<Component> invalidInputLabelList =
        List.of(NtpGui.createCenteredLabel("Invalid input", NtpGui.GARAMOND_25));
    
    AnswerPanel() {
        setToBoxLayoutWithPageAxis();
        center();
        setMaximumSize(new Dimension(1_250, Integer.MAX_VALUE));
    }
    
    void displayComponents(List<Component> components) {
        removeAll();
        components.forEach(this::add);
        revalidate();
        repaint();
    }
    
    void clear() {
        displayComponents(Collections.emptyList());
    }
    
    void displayInvalidInputMessage() {
        displayComponents(invalidInputLabelList);
    }
}
