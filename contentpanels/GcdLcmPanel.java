package contentpanels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Random;

public class GcdLcmPanel extends JPanel {
    JTextField userInput1, userInput2;
    JButton calculateButton;
    JButton randomButton;
    JTextArea answerArea;
    Random random;

    public GcdLcmPanel() {
//        System.out.println(PrimeNumbers.getPFString(PrimeNumbers.getPFMap(13)));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        userInput1 = new JTextField(4);
        userInput2 = new JTextField(4);
        calculateButton = new JButton("Calculate");
        randomButton = new JButton("Generate random numbers");
        answerArea = new JTextArea();
        random = new Random();

        add(new JLabel("Enter 2 positive numbers to find their greatest common " +
                "divisor and least common multiple."), gbc);

        gbc.gridy = 1;
        add(new JLabel("The prime factorization of these numbers can be used to find both of these."), gbc);

        gbc.gridy = 2;
        add(new JLabel("The Euclidean algorithm can be used to find the greatest common divisor"), gbc);

        gbc.gridy = 3;
        add(userInput1, gbc);
        gbc.gridx = 1;
        add(userInput2, gbc);
        gbc.gridx = 2;
        add(randomButton, gbc);
        gbc.gridx = 3;
        add(calculateButton, gbc);
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(answerArea, gbc);

        randomButton.addActionListener(e -> {
            userInput1.setText(String.valueOf(random.nextInt(10000)));
            userInput2.setText(String.valueOf(random.nextInt(10000)));
        });

        calculateButton.addActionListener(e -> {
            try {
                int firstNumber = Integer.parseInt(userInput1.getText());
                int secondNumber = Integer.parseInt(userInput2.getText());
                if (firstNumber > 0 && secondNumber > 0)
                    answerArea.setText(euclidean(firstNumber, secondNumber) +
                            PrimeNumbers.getPFGcdAndLcm(firstNumber, secondNumber));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });
    }

    String euclidean(int firstNumber, int secondNumber) {
        StringBuilder output = new StringBuilder();
        while (firstNumber != 0 && secondNumber != 0) {
            if (firstNumber < secondNumber) {
                output.append("The GCD of ").append(firstNumber).append(" and ").append(secondNumber)
                        .append(" is also the GCD of ").append(firstNumber).append(" and ")
                        .append(secondNumber % firstNumber).append(" (").append(secondNumber)
                        .append(" modulo ").append(firstNumber).append(")\n");
                secondNumber = secondNumber % firstNumber;
            } else {
                output.append("The GCD of ").append(firstNumber).append(" and ").append(secondNumber)
                        .append(" is also the GCD of ").append(secondNumber).append(" and ")
                        .append(firstNumber % secondNumber).append(" (").append(firstNumber)
                        .append(" modulo ").append(secondNumber).append(")\n");
                firstNumber = firstNumber % secondNumber;
            }
        }
        if (firstNumber == 0)
            output.append(secondNumber).append(" is the GCD.\n\n");
        else
            output.append(firstNumber).append(" is the GCD.\n\n");

        return output.toString();
    }
}