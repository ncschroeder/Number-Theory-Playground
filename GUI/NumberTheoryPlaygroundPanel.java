package com.numbertheoryplayground.GUI;

import com.numbertheoryplayground.PrimeNumberMethods;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class NumberTheoryPlaygroundPanel extends JPanel {
    // The first 3 variables are used by all panels, the last one is used by most panels
    final protected static Color bgColor = new Color(0x5BD6F1);
    final protected static Font font = new Font("Garamond", Font.PLAIN, 25);
    final protected static Random random = new Random();

    public NumberTheoryPlaygroundPanel() {
        setBackground(bgColor);
    }
}
