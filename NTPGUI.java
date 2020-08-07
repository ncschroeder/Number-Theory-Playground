package com.numbertheoryplayground;

import com.numbertheoryplayground.CLI.CommandLineSection;
import com.numbertheoryplayground.GUI.*;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Number Theory Playground Graphical User Interface
public class NTPGUI implements ActionListener {
    JPanel cardsPanel;

    public void addComponentsToPane(Container pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        cardsPanel = new JPanel(new CardLayout());

        JPanel buttonsPanel = new NumberTheoryPlaygroundPanel();
//        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setPreferredSize(new Dimension(20, 80));
        Font buttonPanelFont = new Font("Garamond", Font.PLAIN, 20);
//        try {
//            buttonPanelFont = new Font("Garamond", Font.PLAIN, 20);
//        } catch (Exception ex) {
//            System.out.println(ex);
//            System.out.println("font error");
//            buttonPanelFont = new Font(Font.DIALOG, Font.PLAIN, 20);
//        }
        Color btnColor = new Color(0xD5D5D5);

//        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
//        for (Font font : fonts) {
//            System.out.println(font.getName());
//        }
        // All panels will be placed in cardsPanel and be mapped to the text of their corresponding button
        JButton[] buttons = new JButton[] {
                new JButton("Home"),
                new JButton("Prime Numbers"),
                new JButton("Twin Primes"),
                new JButton("Prime Factorization"),
                new JButton("Divisibility"),
                new JButton("GCD and LCM"),
                new JButton("Goldbach Conjecture"),
                new JButton("Pythagorean Triples")
        };

        JPanel[] panels = {
                new HomePanel(),
                new PrimeNumbersPanel(),
                new TwinPrimesPanel(),
                new PrimeFactorizationPanel(),
                new DivisibilityPanel(),
                new GcdLcmPanel(),
                new GoldbachPanel(),
                new PythagoreanTriplesPanel()
        };

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setFocusPainted(false);
            buttons[i].setFont(buttonPanelFont);
            buttons[i].addActionListener(this);
            buttonsPanel.add(buttons[i]);
            cardsPanel.add(panels[i], buttons[i].getText());
        }

        pane.add(buttonsPanel);
        pane.add(cardsPanel);
    }

    public void actionPerformed(ActionEvent evt) {
        // The only sources are JButtons
        JButton sourceButton = (JButton) evt.getSource();
        CardLayout cardLayout = (CardLayout) cardsPanel.getLayout();
        cardLayout.show(cardsPanel, sourceButton.getText());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Number Theory Playground");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        NTPGUI ntpgui = new NTPGUI();
        ntpgui.addComponentsToPane(frame.getContentPane());
//        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        JButton button1 = new JButton("button 1");
//        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
//        panel.add(button1);
////        panel.add(new JButton("button 2"));
//        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}