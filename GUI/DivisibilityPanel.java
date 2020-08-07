package com.numbertheoryplayground.GUI;

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

public class DivisibilityPanel extends NumberTheoryPlaygroundPanel implements ActionListener {
    private final JTextField inputField;
    private final JPanel answerArea;

    public DivisibilityPanel() {
        setBackground(bgColor);

        JTextArea descriptionArea = new JTextArea("There are various ways to tell which numbers are divisible by other" +
                " numbers\nOne way is to look at the \"sub-factorizations\" of the prime factorization. These form the " +
                "prime factorizations of the factors\nEnter a number to find out which numbers it is divisible by, " +
                "or the factors of it");
        descriptionArea.setFont(font);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(bgColor);

        JLabel descriptionLabel1 =
                new JLabel("There are various ways to tell which numbers are divisible by other numbers");
        descriptionLabel1.setFont(font);
        descriptionLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel2 =
                new JLabel("One way is to look at the \"sub-factorizations\" of the prime factorization.");
        descriptionLabel2.setFont(font);
        descriptionLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel3 =
                new JLabel("These form the prime factorizations of the factors");
        descriptionLabel3.setFont(font);
        descriptionLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel4 =
                new JLabel("Enter a number to find out which numbers it is divisible by, or the factors of it");
        descriptionLabel4.setFont(font);
        descriptionLabel4.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputField = new JTextField();
        inputField.setFont(font);
        inputField.setMaximumSize(new Dimension(150, 30));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton randomButton = new JButton("Generate random number");
        randomButton.setFont(font);
        randomButton.setFocusPainted(false);
        randomButton.addActionListener(this);
        randomButton.setActionCommand("random");
        randomButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setFont(font);
        calculateButton.setFocusPainted(false);
        calculateButton.addActionListener(this);
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        answerArea = new JPanel();
        answerArea.setLayout(new BoxLayout(answerArea, BoxLayout.PAGE_AXIS));
        answerArea.setBackground(bgColor);
        answerArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        Dimension spacingDimension = new Dimension(0, 20);

        add(Box.createRigidArea(new Dimension(0, 50)));
        add(descriptionLabel1);
        add(descriptionLabel2);
        add(descriptionLabel3);
        add(descriptionLabel4);
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
//
//        gbc.gridwidth = 3;
//        add(descriptionArea, gbc);
//
//        gbc.gridy = 1;
//        gbc.gridwidth = 1;
//        add(userInput, gbc);
//
//        gbc.gridx = 1;
//        add(randomButton, gbc);
//        gbc.gridx = 2;
//        add(calculateButton, gbc);
//
//        gbc.gridy = 2;
//        gbc.gridx = 0;
//        gbc.gridwidth = 3;
//        add(answerArea, gbc);

    public void actionPerformed(ActionEvent evt) {
        // The 2 buttons on this panel are the only way this function can be called
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

        for (String line : Functions.getDivisInfoViaTricks(number)) {
            JLabel label = new JLabel(line);
            label.setFont(font);
//            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            answerArea.add(label);
        }

        answerArea.add(Box.createRigidArea(new Dimension(0, 10)));

        for (String line : Functions.getDivisInfoViaPF(number)) {
            JLabel label = new JLabel(line);
            label.setFont(font);
//            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            answerArea.add(label);
        }

        answerArea.revalidate();
        answerArea.repaint();
    }
}