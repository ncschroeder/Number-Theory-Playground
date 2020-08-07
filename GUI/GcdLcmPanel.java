package com.numbertheoryplayground.GUI;

import com.numbertheoryplayground.CLI.CommandLineSection;
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

public class GcdLcmPanel extends NumberTheoryPlaygroundPanel implements ActionListener {
    private final JTextField inputField1;
    private final JTextField inputField2;
    private final JPanel answerArea;

    public GcdLcmPanel() {
        setBackground(bgColor);

        JLabel label1 = new JLabel("Enter 2 positive numbers to find their greatest common " +
                "divisor and least common multiple.");
        JLabel label2 = new JLabel("The prime factorization of these numbers can be used to find both of these.");
        JLabel label3 = new JLabel("The Euclidean algorithm can be used to find the greatest common divisor");

        label1.setFont(font);
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.setFont(font);
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label3.setFont(font);
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension inputFieldSize = new Dimension(100, 30);
        inputField1 = new JTextField();
        inputField1.setFont(font);
        inputField1.setMaximumSize(inputFieldSize);
        inputField1.setHorizontalAlignment(JTextField.CENTER);

        inputField2 = new JTextField();
        inputField2.setFont(font);
        inputField2.setMaximumSize(inputFieldSize);
        inputField2.setHorizontalAlignment(JTextField.CENTER);

        JPanel inputFieldPanel = new JPanel();
        inputFieldPanel.setBackground(bgColor);
        inputFieldPanel.setLayout(new BoxLayout(inputFieldPanel, BoxLayout.LINE_AXIS));
        inputFieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputFieldPanel.add(inputField1);
        inputFieldPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        inputFieldPanel.add(inputField2);

        JButton randomButton = new JButton("Generate Random Numbers");
        randomButton.setFont(font);
        randomButton.setActionCommand("random");
        randomButton.addActionListener(this);
        randomButton.setFocusPainted(false);
        randomButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(this);
        calculateButton.setFont(font);
        calculateButton.setFocusPainted(false);
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        answerArea = new JPanel();
        answerArea.setLayout(new BoxLayout(answerArea, BoxLayout.PAGE_AXIS));
        answerArea.setBackground(bgColor);
        answerArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        Dimension spacingDimension = new Dimension(0, 20);

        add(Box.createRigidArea(new Dimension(0, 50)));
        add(label1);
        add(label2);
        add(label3);
        add(Box.createRigidArea(spacingDimension));
        add(inputFieldPanel);
        add(Box.createRigidArea(spacingDimension));
        add(randomButton);
        add(Box.createRigidArea(spacingDimension));
        add(calculateButton);
        add(Box.createRigidArea(spacingDimension));
        add(answerArea);

//        setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//
//        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 5;
//        add(label1, gbc);
//
//        gbc.gridy = 1;
//        add(label2, gbc);
//
//        gbc.gridy = 2;
//        add(label3, gbc);
//
//        gbc.gridwidth = 1;
//        gbc.gridy = 3;
//        gbc.gridx = 3;
////        gbc.insets = new Insets(20, 0, 0, 0);
//        add(inputField1, gbc);
//
//        gbc.gridx = 4;
//        add(inputField2, gbc);
//
//        gbc.gridx = 2;
//        gbc.gridy = 4;
////        add(randomButton, gbc);
//
//        gbc.gridy = 5;
////        add(calculateButton, gbc);
//
//        gbc.gridy = 6;
//        gbc.gridx = 0;
//        gbc.gridwidth = 5;
//        gbc.insets = new Insets(20, 0, 0, 0);
//        add(answerArea, gbc);
    }

    public void actionPerformed(ActionEvent evt) {
        if ("random".equals(evt.getActionCommand())) {
            inputField1.setText(String.valueOf(random.nextInt(100)));
            inputField2.setText(String.valueOf(random.nextInt(100)));
        } else {
            validateInput();
        }
    }

    private void validateInput() {
        long firstNumber, secondNumber;
        try {
            firstNumber = Long.parseLong(inputField1.getText());
            secondNumber = Long.parseLong(inputField2.getText());
        } catch (NumberFormatException ex) {
            displayErrorMessage();
            return;
        }
        if (firstNumber >= 0 && secondNumber >= 0) displayAnswer(firstNumber, secondNumber);
        else displayErrorMessage();
    }

    private void displayErrorMessage() {

    }

    private void displayAnswer(long firstNumber, long secondNumber) {
        answerArea.removeAll();
        for (String line : Functions.getGcdAndLcmInfo(firstNumber, secondNumber)) {
            JLabel label = new JLabel(line);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setFont(font);
            answerArea.add(label);
        }
        answerArea.revalidate();
        answerArea.repaint();
    }
}