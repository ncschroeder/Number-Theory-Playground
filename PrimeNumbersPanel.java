import javax.swing.JLabel;
import java.awt.event.ActionEvent;

/**
 * JPanel that is in charge of all the content for the prime numbers section of the GUI.
 */
public class PrimeNumbersPanel extends NumberTheoryPlaygroundPanel {

    public PrimeNumbersPanel() {
        for (String infoLine : Primes.getPrimesSectionInfo()) {
            JLabel infoLabel = new JLabel(infoLine);
            infoLabel.setFont(contentFont);
            sectionInfoArea.add(infoLabel);
        }
        String[] instructions = new String[] {
                "Enter or generate a number to get the first 30 prime numbers after that number.",
                "This number should be less than or equal to 1 billion."
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
        for (String primesLine : Primes.getPrimesAfter(number)) {
            JLabel primesLabel = new JLabel(primesLine);
            primesLabel.setFont(contentFont);
            answerArea.add(primesLabel);
        }
        answerArea.revalidate();
        answerArea.repaint();
    }
}