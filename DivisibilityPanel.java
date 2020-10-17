import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

/**
 * JPanel that is in charge of the content for the divisibility section of the GUI.
 */
public class DivisibilityPanel extends PanelWithAnswerScrollPane {

    public DivisibilityPanel() {
        for (String infoLine : Divisibility.getSectionInfo()) {
            JLabel infoLabel = new JLabel(infoLine);
            infoLabel.setFont(contentFont);
            sectionInfoArea.add(infoLabel);
        }
        String[] instructions = new String[] {
                "Enter or generate a number to get the divisibility info of it.",
                "This number should be greater than or equal to 2 and less than or equal to 10 million."
        };
        for (String instruction : instructions) {
            JLabel instructionLabel = new JLabel(instruction);
            instructionLabel.setFont(contentFont);
            sectionInfoArea.add(instructionLabel);
        }

        addComponents();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == randomButton) {
            // Generate random number that is greater than or equal to 2 and less than or equal to 10 million
            int randomNumber = Math.max(2, random.nextInt(10_000_000));
            inputField1.setText(String.valueOf(randomNumber));
        } else {
            // The only other possible source of event is calculateButton
            validateInput();
        }
    }

    private void validateInput() {
        int number;
        try {
            number = Integer.parseInt(inputField1.getText());
        } catch (NumberFormatException ex) {
            displayErrorMessage();
            return;
        }
        if (number >= 2 && number <= 10_000_000) {
            displayAnswer(number);
        } else {
            displayErrorMessage();
        }
    }

    private void displayAnswer(int number) {
        answerArea.removeAll();
        for (String infoLine : Divisibility.getDivisInfoViaTricks(number)) {
            JLabel infoLabel = new JLabel(infoLine);
            infoLabel.setFont(contentFont);
            answerArea.add(infoLabel);
        }
        answerArea.add(Box.createRigidArea(new Dimension(0, 10)));
        for (String infoLine : PrimeFactorization.getDivisInfo(number)) {
            JLabel infoLabel = new JLabel(infoLine);
            infoLabel.setFont(contentFont);
            answerArea.add(infoLabel);
        }
        answerArea.revalidate();
        answerArea.repaint();
    }
}