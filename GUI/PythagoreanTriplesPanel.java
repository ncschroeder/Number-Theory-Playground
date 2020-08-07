package com.numbertheoryplayground.GUI;

import com.numbertheoryplayground.CLI.CommandLineSection;
import com.numbertheoryplayground.Functions;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PythagoreanTriplesPanel extends NumberTheoryPlaygroundPanel implements ActionListener {
    private final JTextField inputField;
    private final JPanel answerArea;

    public PythagoreanTriplesPanel() {
        setBackground(bgColor);

        JLabel descriptionLabel1 =
                new JLabel("The Pythagorean theorem says that for the side lengths of a right triangle,");
        descriptionLabel1.setFont(font);
        descriptionLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel2 =
                new JLabel("the sum of the squares of the 2 short sides equals the square of the long side");
        descriptionLabel2.setFont(font);
        descriptionLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel3 = new JLabel("or a^2 + b^2 = c^2. Enter a number to find some integers for " +
                "which this theorem is true");
        descriptionLabel3.setFont(font);
        descriptionLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(100, 30));
        inputField.setFont(font);
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton randomButton = new JButton("Generate Random Number");
        randomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        randomButton.setFont(font);
        randomButton.setFocusPainted(false);
        randomButton.addActionListener(this);
        randomButton.setActionCommand("random");

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calculateButton.setFont(font);
        calculateButton.setFocusPainted(false);
        calculateButton.addActionListener(this);

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

//        setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//
//        add(descriptionLabel, gbc);
//        gbc.gridy = 1;
//        add(inputField, gbc);
//        gbc.gridx = 1;
//        add(calculateButton, gbc);
//        gbc.gridx = 2;
//        add(randomButton, gbc);
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        add(answerArea, gbc);
    }

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
        for (String line : Functions.getPythagTriples(number)) {
            JLabel label = new JLabel(line);
            label.setFont(font);
//            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            answerArea.add(label);
        }
        answerArea.revalidate();
        answerArea.repaint();
    }
}