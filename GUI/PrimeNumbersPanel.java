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

public class PrimeNumbersPanel extends NumberTheoryPlaygroundPanel implements ActionListener {

    private final JTextField inputField;
    private final JPanel answerArea;

    public PrimeNumbersPanel() {
        setBackground(bgColor);

        JLabel descriptionLabel1 = new JLabel("Prime numbers are numbers that are divisible only by 1 and themself");
        descriptionLabel1.setFont(font);
        descriptionLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel2 = new JLabel("Enter a number to find the first 30 prime numbers after that number");
        descriptionLabel2.setFont(font);
        descriptionLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(200, 30));
        inputField.setFont(font);
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton randomButton = new JButton("Generate Random Number");
        randomButton.setFocusPainted(false);
        randomButton.setFont(font);
        randomButton.addActionListener(this);
        randomButton.setActionCommand("random");
        randomButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setFocusPainted(false);
        calculateButton.setFont(font);
        calculateButton.addActionListener(this);
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        answerArea = new JPanel();
        answerArea.setMaximumSize(new Dimension(1000, 300));
        answerArea.setLayout(new BoxLayout(answerArea, BoxLayout.PAGE_AXIS));
        answerArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerArea.setBackground(bgColor);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        Dimension spacingDimension = new Dimension(0, 20);

        add(Box.createRigidArea(new Dimension(0, 50)));
        add(descriptionLabel1);
        add(descriptionLabel2);
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
//
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
        for (String line : Functions.getPrimes(number)) {
            JLabel answerLabel = new JLabel(line);
            answerLabel.setFont(font);
            answerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            answerArea.add(answerLabel);
            answerArea.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        answerArea.revalidate();
        answerArea.repaint();
    }
}
