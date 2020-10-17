import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

/**
 * JPanel that is in charge of the content for the twin primes section of the GUI.
 */
public class TwinPrimesPanel extends NumberTheoryPlaygroundPanel {

    public TwinPrimesPanel() {
        for (String infoLine : Primes.getTwinPrimesSectionInfo()) {
            JLabel infoLabel = new JLabel(infoLine);
            infoLabel.setFont(contentFont);
            sectionInfoArea.add(infoLabel);
        }
        String[] instructions = new String[] {
                "Enter or generate a number to get the first 20 pairs of twin primes after that number.",
                "This number should be less than 1 billion."
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
            int randomNumber = random.nextInt(1_000_000_000);
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
        if (number <= 1_000_000_000) {
            displayAnswer(number);
        } else {
            displayErrorMessage();
        }
    }

    private void displayAnswer(int number) {
        answerArea.removeAll();
        for (String twinPrimesLine : Primes.getTwinPrimesAfter(number)) {
            JLabel twinPrimesLabel = new JLabel(twinPrimesLine);
            twinPrimesLabel.setFont(contentFont);
            answerArea.add(twinPrimesLabel);
            answerArea.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        answerArea.revalidate();
        answerArea.repaint();
    }
}