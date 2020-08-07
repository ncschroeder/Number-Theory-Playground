package com.numbertheoryplayground.GUI;

import javax.swing.JLabel;

public class HomePanel extends NumberTheoryPlaygroundPanel {
    public HomePanel() {
        setBackground(bgColor);

        JLabel welcomeMessage = new JLabel("Welcome to the number theory playground");
        welcomeMessage.setFont(font);
        add(welcomeMessage);
    }
}
