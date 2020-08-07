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

public class TwinPrimesPanel extends NumberTheoryPlaygroundPanel implements ActionListener {

    private final JTextField inputField;
    private final JPanel answerArea;

    public TwinPrimesPanel() {
        setBackground(bgColor);

        JLabel descriptionLabel1 = new JLabel("Twin primes are primes that have a difference of 2");
        descriptionLabel1.setFont(font);
        descriptionLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel2 = new JLabel("It is conjectured that there are infinitely many of them");
        descriptionLabel2.setFont(font);
        descriptionLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel3 =
                new JLabel("Enter a number to find out the first 20 pairs of twin primes after that number");
        descriptionLabel3.setFont(font);
        descriptionLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(150, 30));
        inputField.setFont(font);
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton randomButton = new JButton("Generate Random Number");
        randomButton.setFocusPainted(false);
        randomButton.setFont(font);
        randomButton.setActionCommand("random");
        randomButton.addActionListener(this);
        randomButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setFocusPainted(false);
        calculateButton.setFont(font);
        calculateButton.addActionListener(this);
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        answerArea = new JPanel();
        answerArea.setBackground(bgColor);

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
//        setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();

//        add(descriptionArea, gbc);
//        gbc.gridy = 1;
//        add(inputField, gbc);
//        gbc.gridx = 1;
//        add(randomButton, gbc);
//        gbc.gridx = 2;
//        add(calculateButton, gbc);
//        gbc.gridy = 2;
//        gbc.gridx = 0;
//        add(answerArea, gbc);

    public void actionPerformed(ActionEvent evt) {
        if ("random".equals(evt.getActionCommand())) {
            inputField.setText(String.valueOf(random.nextInt(1000)));
        } else {
            validateInput();
        }
    }

    private void validateInput() {
        long number;
        try {
            number = Long.parseLong(inputField.getText());
        } catch (NumberFormatException ex) {
            displayErrorMessage();
            return;
        }
        displayAnswer(number);
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
        for (String line : Functions.getTwinPrimes(number)) {
            JLabel label = new JLabel(line);
            label.setFont(font);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            answerArea.add(label);
            answerArea.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        answerArea.revalidate();
        answerArea.repaint();
    }
}