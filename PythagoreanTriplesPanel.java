import javax.swing.JLabel;
import java.awt.event.ActionEvent;

/**
 * JPanel that is in charge of the content for the Pythagorean triples section of the GUI.
 */
public class PythagoreanTriplesPanel extends NumberTheoryPlaygroundPanel {

    public PythagoreanTriplesPanel() {
        for (String infoLine : PythagoreanTriples.getSectionInfo()) {
            JLabel infoLabel = new JLabel(infoLine);
            infoLabel.setFont(contentFont);
            sectionInfoArea.add(infoLabel);
        }
        String[] instructions = new String[] {
                "Enter a number to get the first 10 Pythagorean triples after that number.",
                "This number should be less than or equal to 1 thousand."
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
            int randomNumber = random.nextInt(1_000);
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
        if (number <= 1_000) {
            displayAnswer(number);
        } else {
            displayErrorMessage();
        }
    }

    private void displayAnswer(int number) {
        answerArea.removeAll();
        for (String line : PythagoreanTriples.getTriples(number)) {
            JLabel label = new JLabel(line);
            label.setFont(contentFont);
            answerArea.add(label);
        }
        answerArea.revalidate();
        answerArea.repaint();
    }
}