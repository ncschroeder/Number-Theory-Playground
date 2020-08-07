package com.numbertheoryplayground.GUI;

import com.numbertheoryplayground.Functions;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrimeFactorizationPanel extends NumberTheoryPlaygroundPanel implements ActionListener {
    private final JTextField inputField;
    private final JLabel answerLabel;

    public PrimeFactorizationPanel() {
        setBackground(bgColor);

        JLabel descriptionLabel = new JLabel("Enter a number to find it's prime factorization");
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setFont(font);

        inputField = new JTextField();
        Dimension size = new Dimension(100, 30);
        inputField.setMaximumSize(size);
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputField.setFont(font);
        inputField.setHorizontalAlignment(JTextField.CENTER);

        JButton randomButton = new JButton("Generate Random Number");
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

        answerLabel = new JLabel();
        answerLabel.setFont(font);
        answerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        Dimension spacingDimension = new Dimension(0, 20);

        add(Box.createRigidArea(new Dimension(0, 50)));
        add(descriptionLabel);
        add(Box.createRigidArea(spacingDimension));
        add(inputField);
        add(Box.createRigidArea(spacingDimension));
        add(randomButton);
        add(Box.createRigidArea(spacingDimension));
        add(calculateButton);
        add(Box.createRigidArea(spacingDimension));
        add(answerLabel);

//        JButton button1 = new JButton("button 1");
//        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
//        add(button1);
//        JTextField textField = new JTextField(10);
//        textField.setPreferredSize(new Dimension(20, 20));
//        textField.setMaximumSize(new Dimension(20, 20));
//        add(textField);
//        setAlignmentX(Component.RIGHT_ALIGNMENT);

//        setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//
//        gbc.anchor = GridBagConstraints.CENTER;
//        add(descriptionLabel, gbc);
//
//        gbc.gridy = 1;
//        gbc.insets = new Insets(20, 0, 0, 0);
//        add(inputField, gbc);
//
//        gbc.gridy = 2;
//        add(randomButton, gbc);
//
//        gbc.gridy = 3;
//        add(calculateButton, gbc);
//
//        gbc.gridy = 4;
//        gbc.gridwidth = 3;
//        add(answerLabel, gbc);
    }

    public void actionPerformed(ActionEvent evt) {
        if ("random".equals(evt.getActionCommand())) {
            inputField.setText(String.valueOf(random.nextInt(10000)));
        } else {
            validateInput();
        }
    }

    private void validateInput() {
        long number;
        try {
            number = Long.parseLong(inputField.getText());
        } catch (NumberFormatException ex) {
            answerLabel.setText("Must enter a number greater than 1 and less than " + Long.MAX_VALUE);
            return;
        }
        if (number <= 1) {
            answerLabel.setText("Number must be greater than 1 and less than " + Long.MAX_VALUE);
        } else {
            displayAnswer(number);
        }
    }

    private void displayAnswer(long number) {
        answerLabel.setText(Functions.getPrimeFactorizationString(number));
    }
}