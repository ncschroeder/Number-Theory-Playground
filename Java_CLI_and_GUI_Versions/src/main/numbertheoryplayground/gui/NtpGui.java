package numbertheoryplayground.gui;

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
import numbertheoryplayground.sectionclasses.outer.GoldbachConjecture;

/**
 * Utility class with a little code related to the Number Theory Playground graphical user interface,
 * including the main method to launch the application and some static fields and methods used by
 * other GUI-related classes and by nested section classes for implementing getGuiComponents.
 */
public class NtpGui {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GoldbachConjecture.setMaxInputForGui();
            
            var frame = new JFrame("Number Theory Playground");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 900);
            
            var sp = new JScrollPane(new MainPanel());
            sp.getVerticalScrollBar().setUnitIncrement(5);
            frame.getContentPane().add(sp);
            
            frame.setVisible(true);
        });
    }
    
    static final Color BACKGROUND_COLOR = Color.CYAN;
    
    static void centerComponent(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    
    /**
     * Returns a rigid square area that's as wide and tall as the size param. Adding this to a panel will
     * add a gap whether components in that panel are laid out horizontally or vertically.
     */
    public static Component createGap(int size) {
        return Box.createRigidArea(new Dimension(size, size));
    }
    
    public static Component createGapBetweenAnswerSections() {
        return createGap(20);
    }
    
    static JButton createButton(String text, Font font) {
        var b = new JButton(text);
        b.setFont(font);
        b.setFocusPainted(false);
        return b;
    }
    
    static Font createGaramondFont(int size) {
        return new Font("Garamond", Font.PLAIN, size);
    }
    
    static Font createBoldGaramondFont(int size) {
        return new Font("Garamond", Font.BOLD, size);
    }
    
    /**
     * Common font and size used.
     */
    static final Font GARAMOND_25 = createGaramondFont(25);
    static final Font BOLD_GARAMOND_25 = createBoldGaramondFont(25);
    private static final Font ANSWER_MAIN_HEADING_FONT = createBoldGaramondFont(30);
    private static final Font ANSWER_SUB_HEADING_FONT = BOLD_GARAMOND_25;
    private static final Font LIST_HEADING_FONT = GARAMOND_25;
    private static final Font TABLE_HEADING_FONT = BOLD_GARAMOND_25;
    private static final Font ANSWER_CONTENT_FONT = GARAMOND_25;
    
    static JLabel createLabel(String text, Font font) {
        var l = new JLabel(text);
        l.setFont(font);
        return l;
    }
    
    static JLabel createCenteredLabel(String text, Font font) {
        JLabel l = createLabel(text, font);
        centerComponent(l);
        return l;
    }
    
    public static JLabel createAnswerMainHeadingLabel(String heading) {
        return createCenteredLabel(heading, ANSWER_MAIN_HEADING_FONT);
    }
    
    public static JLabel createAnswerSubHeadingLabel(String heading) {
        return createCenteredLabel(heading, ANSWER_SUB_HEADING_FONT);
    }
    
    public static JLabel createListHeadingLabel(String heading) {
        return createCenteredLabel(heading, LIST_HEADING_FONT);
    }
    
    public static JLabel createAnswerTableColumnHeadingLabel(String heading) {
        return createCenteredLabel(heading, TABLE_HEADING_FONT);
    }
    
    public static JLabel createUncenteredAnswerContentLabel(String heading) {
        return createLabel(heading, ANSWER_CONTENT_FONT);
    }
    
    public static JLabel createCenteredAnswerContentLabel(String heading) {
        return createCenteredLabel(heading, ANSWER_CONTENT_FONT);
    }
}
