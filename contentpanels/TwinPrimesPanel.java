package contentpanels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class TwinPrimesPanel extends JPanel {
    TwinPrimesPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JTextField userInput = new JTextField(6);
        JButton findButton = new JButton("Find");
        JLabel[] outputLabels = new JLabel[10];

        add(new JLabel("Twin primes are primes that have a difference of 2"), gbc);
        gbc.gridy = 1;
        add(new JLabel("It is conjectured that there are infinitely many of them"), gbc);
        gbc.gridy = 2;
        add(new JLabel("Enter a number to find out the first 100 pairs of twin primes" +
                " after that number"), gbc);

        gbc.gridy = 3;
        add(userInput, gbc);
        gbc.gridx = 1;
        add(findButton, gbc);

        findButton.addActionListener(e -> {
            try {
                int number = Integer.parseInt(userInput.getText());
                String[] twinPrimes = PrimeNumbers.getTwinPrimes(number);
            } catch (Exception ignored) {}

        });
    }
}
