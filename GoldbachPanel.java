import javax.swing.JLabel;
import java.awt.event.ActionEvent;

/**
 * JPanel that is in charge of the Goldbach conjecture section of the GUI.
 */
public class GoldbachPanel extends PanelWithAnswerScrollPane {

    public GoldbachPanel() {
        for (String infoLine : Goldbach.getSectionInfo()) {
            JLabel infoLabel = new JLabel(infoLine);
            infoLabel.setFont(contentFont);
            sectionInfoArea.add(infoLabel);
        }
        String[] instructions = new String[] {
                "Enter or generate a number to get all the pairs of prime numbers that sum to that number.",
                "This number should be even, greater than or equal to 4, and less than or equal to 100 thousand."
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
            // Generate a random number that is even, greater than or equal to 4, and less than or equal to 100 thousand
            int randomNumber = Math.max(4, random.nextInt(100_000));
            if (randomNumber % 2 != 0) {
                randomNumber++;
            }
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
        if (number % 2 == 0 && number >= 4 && number <= 100_000) {
            displayAnswer(number);
        } else {
            displayErrorMessage();
        }
    }

    private void displayAnswer(int number) {
        answerArea.removeAll();
        for (String pairsLine : Goldbach.getGoldbachPairs(number)) {
            JLabel pairsLabel = new JLabel(pairsLine);
            pairsLabel.setFont(contentFont);
            answerArea.add(pairsLabel);
        }
        answerArea.revalidate();
        answerArea.repaint();
    }
}