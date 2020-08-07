package com.numbertheoryplayground.GUI;

import com.numbertheoryplayground.Functions;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GoldbachPanel extends NumberTheoryPlaygroundPanel implements ActionListener {
    private final JTextField inputField;
    private final JPanel answerArea;

    public GoldbachPanel() {
        setBackground(bgColor);

        JLabel descriptionLabel1 = new JLabel("In mathematics, a conjecture is a statement that is believed to be " +
                "true but has not been proven to be true");
        descriptionLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel1.setFont(font);

        JLabel descriptionLabel2 = new JLabel("The Goldbach Conjecture says that every even number can be expressed " +
                "as the sum of 2 prime numbers");
        descriptionLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel2.setFont(font);

        JLabel descriptionLabel3 = new JLabel("Enter an even positive number greater than or equal to 4 to find " +
                "out the pairs of prime numbers");
        descriptionLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel3.setFont(font);

        inputField = new JTextField();
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputField.setFont(font);
        inputField.setMaximumSize(new Dimension(100, 30));

        JButton randomButton = new JButton("Generate Random number");
        randomButton.setFont(font);
        randomButton.addActionListener(this);
        randomButton.setActionCommand("random");
        randomButton.setFocusPainted(false);
        randomButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setFont(font);
        calculateButton.addActionListener(this);
        calculateButton.setFocusPainted(false);
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        answerArea = new JPanel();
        answerArea.setBackground(bgColor);
        answerArea.setMaximumSize(new Dimension(800, 300));
        answerArea.setLayout(new BoxLayout(answerArea, BoxLayout.PAGE_AXIS));
        answerArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        Dimension spacingDimension = new Dimension(0, 20);

        add(Box.createRigidArea(new Dimension(0, 50)));
        add(descriptionLabel1);
        add(descriptionLabel2);
        add(descriptionLabel3);
        add(Box.createRigidArea(spacingDimension));
        add(inputField);
        add(Box.createRigidArea(spacingDimension));
        add(randomButton);
        add(Box.createRigidArea(spacingDimension));
        add(calculateButton);
        add(Box.createRigidArea(spacingDimension));
        add(answerArea);
    }

//        GridBagConstraints gbc = new GridBagConstraints();
//        setLayout(new GridBagLayout());

//        gbc.anchor = GridBagConstraints.PAGE_START;
//        gbc.gridwidth = 2;
//        add(new JLabel("In mathematics, a conjecture is a statement that is believed to " +
//                "be true but has not been proven to be true"), gbc);
//        gbc.gridy = 1;
//        add(new JLabel("The Goldbach Conjecture says that every even number can be expressed " +
//                "as the sum of 2 prime numbers"), gbc);
//        gbc.gridy = 2;
//        add(new JLabel("Enter an even positive number to find out which 2 prime numbers"), gbc);

//        gbc.gridwidth = 1;
//        gbc.gridy = 3;
////        gbc.anchor = GridBagConstraints.LINE_END;
//        add(userInput, gbc);



//        gbc.gridx = 1;
////        gbc.anchor = GridBagConstraints.LINE_START;
//        add(calculateButton, gbc);
//
//        gbc.gridy = 4;
//        gbc.gridx = 0;
//        add(resultArea, gbc);

    public void actionPerformed(ActionEvent e) {
        if ("random".equals(e.getActionCommand())) {
            generateRandomNumber();
        } else {
            validateInput();
        }
    }

    /**
     * Sets the text of the input field to a random even number greater than 4 and less than or equal to 1000
     */
    private void generateRandomNumber() {
        int randomNumber;
        do {
            randomNumber = random.nextInt(1000);
        } while (randomNumber % 2 != 0 || randomNumber < 4);
        inputField.setText(String.valueOf(randomNumber));
    }

    private void validateInput() {
        long number;
        try {
            number = Long.parseLong(inputField.getText());
        } catch (NumberFormatException ex) {
            displayErrorMessage();
            return;
        }
        if (number % 2 == 0 && number >= 4) displayAnswer(number);
        else displayErrorMessage();
    }

    private void displayErrorMessage() {
        answerArea.removeAll();
        JLabel errorLabel = new JLabel("Invalid input");
        errorLabel.setFont(font);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerArea.add(errorLabel);
        answerArea.revalidate();
        answerArea.repaint();
    }

    private void displayAnswer(long number) {
        answerArea.removeAll();
        for (String line : Functions.getGoldbachPairs(number)) {
            JLabel label = new JLabel(line);
            label.setFont(font);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            answerArea.add(label);
        }
        answerArea.revalidate();
        answerArea.repaint();
    }
}
