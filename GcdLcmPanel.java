import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

/**
 * JPanel that is in charge of the content for the GCD and LCM section of the GUI.
 */
public class GcdLcmPanel extends PanelWithAnswerScrollPane {
    private final JTextField inputField2;
    private final JPanel inputFieldPanel;

    public GcdLcmPanel() {
        for (String infoLine : GcdAndLcm.getSectionInfo()) {
            JLabel infoLabel = new JLabel(infoLine);
            infoLabel.setFont(contentFont);
            sectionInfoArea.add(infoLabel);
        }
        String[] instructions = new String[] {
                "Enter or generate 2 numbers to get GCD and LCM info for those numbers.",
                "These numbers should be greater than or equal to 2 and less than or equal to 10 million."
        };
        for (String instruction : instructions) {
            JLabel instructionLabel = new JLabel(instruction);
            instructionLabel.setFont(contentFont);
            sectionInfoArea.add(instructionLabel);
        }

        inputField2 = new JTextField();
        inputField2.setFont(contentFont);
        inputField2.setMaximumSize(inputField1.getMaximumSize());
        inputField2.setHorizontalAlignment(JTextField.CENTER);

        inputFieldPanel = new JPanel();
        inputFieldPanel.setBackground(bgColor);
        inputFieldPanel.setLayout(new BoxLayout(inputFieldPanel, BoxLayout.LINE_AXIS));
        inputFieldPanel.setAlignmentX(CENTER_ALIGNMENT);
        inputFieldPanel.add(inputField1);
        inputFieldPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        inputFieldPanel.add(inputField2);

        // Add "s" since there are 2 input fields to generate random numbers for.
        randomButton.setText(randomButton.getText() + "s");

        addComponents();
    }

    /**
     * Adds sectionInfoArea, inputFieldPanel, randomButton, calculateButton, and answerScrollPane. sectionInfoArea
     * is placed at the top and every other object is placed below the object that was previously placed.
     */
    @Override
    protected void addComponents() {
        add(sectionInfoArea);
        add(Box.createRigidArea(spacingDimension));
        add(inputFieldPanel);
        add(Box.createRigidArea(spacingDimension));
        add(randomButton);
        add(Box.createRigidArea(spacingDimension));
        add(calculateButton);
        add(Box.createRigidArea(spacingDimension));
        add(answerScrollPane);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == randomButton) {
            // Generate random numbers that are greater than or equal to 2 and less than or equal to 10 million
            int randomNumber = Math.max(2, random.nextInt(10_000_000));
            inputField1.setText(String.valueOf(randomNumber));

            randomNumber = Math.max(2, random.nextInt(10_000_000));
            inputField2.setText(String.valueOf(randomNumber));
        } else {
            // The only other possible source of event is calculateButton
            validateInput();
        }
    }

    private void validateInput() {
        int firstNumber, secondNumber;
        try {
            firstNumber = Integer.parseInt(inputField1.getText());
            secondNumber = Integer.parseInt(inputField2.getText());
        } catch (NumberFormatException ex) {
            displayErrorMessage();
            return;
        }

        if (firstNumber >= 2 && firstNumber <= 10_000_000 && secondNumber >= 2 && secondNumber <= 10_000_000) {
            displayAnswer(firstNumber, secondNumber);
        } else {
            displayErrorMessage();
        }
    }

    private void displayAnswer(int firstNumber, int secondNumber) {
        answerArea.removeAll();
        for (String infoLine : PrimeFactorization.getGcdAndLcmInfo(firstNumber, secondNumber)) {
            JLabel infoLabel = new JLabel(infoLine);
            infoLabel.setAlignmentX(LEFT_ALIGNMENT);
            infoLabel.setFont(contentFont);
            answerArea.add(infoLabel);
        }
        answerArea.add(Box.createRigidArea(spacingDimension));
        for (String infoLine : GcdAndLcm.getEuclideanInfo(firstNumber, secondNumber)) {
            JLabel infoLabel = new JLabel(infoLine);
            infoLabel.setAlignmentX(LEFT_ALIGNMENT);
            infoLabel.setFont(contentFont);
            answerArea.add(infoLabel);
        }
        answerArea.revalidate();
        answerArea.repaint();
    }
}