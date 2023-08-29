package com.nicholasschroeder.numbertheoryplayground;

import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

/**
 * Panel that displays either nothing, an invalid input message, or an answer for the current section.
 */
public class AnswerPanel extends NTPPanel {
    private final JLabel invalidInputLabel =
        NTPGUI.createCenteredLabel("Invalid input", NTPGUI.garamondFontSize25);
    
    public AnswerPanel() {
        setToBoxLayoutWithPageAxis();
        center();
        setMaximumSize(new Dimension(1_000, 1_000));
    }
    
    public void displayComponents(List<Component> components) {
        removeAll();
        components.forEach(this::add);
        revalidate();
        repaint();
    }
    
    public void clear() {
        displayComponents(List.of());
    }
    
    public void displayInvalidInputMessage() {
        displayComponents(List.of(invalidInputLabel));
    }
}