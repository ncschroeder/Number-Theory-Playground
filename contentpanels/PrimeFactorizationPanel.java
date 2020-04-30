package contentpanels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class PrimeFactorizationPanel extends JPanel {
    JTextField userInput;
    JButton calculateButton;
    JLabel outputLabel;
    GridBagConstraints gbc;

    PrimeFactorizationPanel() {
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        userInput = new JTextField(8);
        calculateButton = new JButton("Calculate");
        outputLabel = new JLabel();

        add(new JLabel("Enter a number to find it's prime factorization"), gbc);

        gbc.gridy = 1;
        add(userInput, gbc);

        gbc.gridx = 1;
        add(calculateButton, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(outputLabel, gbc);

        calculateButton.addActionListener(e -> {
            if (!userInput.getText().isEmpty()) {
                try {
                    int number = Integer.parseInt(userInput.getText());
                    if (number <= 1)
                        outputLabel.setText("Number must be greater than 1 and less than " +
                                Integer.MAX_VALUE);
                    else
                        outputLabel.setText(PrimeNumbers.getPFString(PrimeNumbers.getPFMap(number)));
                } catch (Exception ex) {
                    outputLabel.setText("Must enter a number greater than 1 and less than " +
                            Integer.MAX_VALUE);
                }
            }
        });
    }
}