package com.nicholasschroeder.numbertheoryplayground;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utility class with a little code related to the Number Theory Playground Graphical User Interface,
 * including the main method to launch the application and some static fields and methods used by other
 * GUI-related classes and by some Section classes for implementations of getGuiComponents.
 */
public class NTPGUI {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            var frame = new JFrame("Number Theory Playground");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 900);
            
            var sp = new JScrollPane(new MainPanel());
            sp.getVerticalScrollBar().setUnitIncrement(5);
            frame.getContentPane().add(sp);
            
            frame.setVisible(true);
        });
    }
    
    public static final Color backgroundColor = Color.CYAN;
    
    /**
     * Common font and size used.
     */
    public static final Font garamondFontSize25 = createGaramondFont(25);
    
    public static final Font answerContentFont = garamondFontSize25;
    public static final Font listHeadingFont = garamondFontSize25;
    public static final Font answerMainHeadingFont = createBoldGaramondFont(30);
    public static final Font answerSubHeadingFont = createBoldGaramondFont(25);
    public static final Font tableHeadingFont = createBoldGaramondFont(20);
    
    public static void centerComponent(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    
    /**
     * Returns a rigid square area that's as wide and tall as the size param. Adding this to a panel will
     * add a gap whether components in that panel are laid out horizontally or vertically.
     */
    public static Component createGap(int size) {
        return Box.createRigidArea(new Dimension(size, size));
    }
    
    protected static Font createGaramondFont(int size) {
        return new Font("Garamond", Font.PLAIN, size);
    }
    
    protected static Font createBoldGaramondFont(int size) {
        return new Font("Garamond", Font.BOLD, size);
    }
    
    protected static JLabel createLabel(String text, Font font) {
        var l = new JLabel(text);
        l.setFont(font);
        return l;
    }
    
    public static JLabel createCenteredLabel(String text, Font font) {
        JLabel l = createLabel(text, font);
        centerComponent(l);
        return l;
    }
    
    public static JButton createButton(String text, Font font) {
        var b = new JButton(text);
        b.setFont(font);
        b.setFocusPainted(false);
        return b;
    }
    
    public static List<Component> createStreamHeadingAndTextArea(String heading, Stream<String> strings) {
        return List.of(
            createCenteredLabel(heading, listHeadingFont),
            new NTPTextArea(strings)
        );
    }
}