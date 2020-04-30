package contentpanels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class GoldbachPanel extends JPanel {
    JTextField userInput;
    JButton calculateButton;
    JLabel answerLabel;

    GoldbachPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(new GridBagLayout());

//        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.gridwidth = 2;
        add(new JLabel("In mathematics, a conjecture is a statement that is believed to " +
                "be true but has not been proven to be true"), gbc);
        gbc.gridy = 1;
        add(new JLabel("The Goldbach Conjecture says that every even number can be expressed " +
                "as the sum of 2 prime numbers"), gbc);
        gbc.gridy = 2;
        add(new JLabel("Enter an even positive number to find out which 2 prime numbers"), gbc);

        userInput = new JTextField(8);
        calculateButton = new JButton("Calculate");
        answerLabel = new JLabel();

        gbc.gridwidth = 1;
        gbc.gridy = 3;
//        gbc.anchor = GridBagConstraints.LINE_END;
        add(userInput, gbc);

        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_START;
        add(calculateButton, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        add(answerLabel, gbc);

        calculateButton.addActionListener(e -> {
            try {
                int number = Integer.parseInt(userInput.getText());
                if (number % 2 == 0 && number > 0)
                    answerLabel.setText(PrimeNumbers.getGoldbachPairs(number));
                else {
                    if (!answerLabel.getText().isEmpty())
                        answerLabel.setText("");
                }
            } catch (Exception ignored) {
                if (!answerLabel.getText().isEmpty())
                    answerLabel.setText("");
            }
        });
    }
}
