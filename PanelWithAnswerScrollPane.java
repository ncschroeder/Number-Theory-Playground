import javax.swing.Box;
import javax.swing.JScrollPane;

/**
 * Contains a JScrollPane that answerArea is placed in for the panels where the answers might require more space than
 * the size of each panel.
 */
public abstract class PanelWithAnswerScrollPane extends NumberTheoryPlaygroundPanel {
    protected JScrollPane answerScrollPane;

    public PanelWithAnswerScrollPane() {
        answerScrollPane = new JScrollPane(answerArea);
        answerScrollPane.getVerticalScrollBar().setUnitIncrement(5);
        answerScrollPane.setAlignmentX(CENTER_ALIGNMENT);
    }

    /**
     * Adds sectionInfoArea, inputField1, randomButton, calculateButton, and answerScrollPane. sectionInfoArea is
     * placed at the top and every other object is placed below the object that was previously placed.
     */
    @Override
    protected void addComponents() {
        add(sectionInfoArea);
        add(Box.createRigidArea(spacingDimension));
        add(inputField1);
        add(Box.createRigidArea(spacingDimension));
        add(randomButton);
        add(Box.createRigidArea(spacingDimension));
        add(calculateButton);
        add(Box.createRigidArea(spacingDimension));
        add(answerScrollPane);
    }
}
