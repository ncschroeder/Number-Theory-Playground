import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Contains objects that are common to all the panels.
 */
public abstract class NumberTheoryPlaygroundPanel extends JPanel implements ActionListener {
    protected static Color bgColor;
    public static void setBgColor(Color color) {
        bgColor = color;
    }

    protected static Font contentFont;
    public static void setContentFont(Font font) {
        contentFont = font;
    }

    final protected static Random random = new Random();

    /**
     * Consists of information about the panel and instructions.
     */
    protected JPanel sectionInfoArea;

    /**
     * Where the user can type some input. All panels have at least 1 input field but some may have 2.
     */
    protected JTextField inputField1;

    /**
     * Takes the input based on content in inputField1 and displays info in answerArea.
     */
    protected JButton calculateButton;

    /**
     * Used to generate a random number or random numbers.
     */
    protected JButton randomButton;

    /**
     * Either contains nothing, some info depending on user input and the panel, or "Invalid input".
     */
    protected JPanel answerArea;

    /**
     * The spacing between components in each panel.
     */
    protected static Dimension spacingDimension = new Dimension(0, 20);

    public NumberTheoryPlaygroundPanel() {
        setBackground(bgColor);

        sectionInfoArea = new JPanel();
        sectionInfoArea.setBackground(bgColor);
        sectionInfoArea.setLayout(new BoxLayout(sectionInfoArea, BoxLayout.PAGE_AXIS));
        sectionInfoArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputField1 = new JTextField();
        inputField1.setFont(contentFont);
        Dimension inputFieldSize = new Dimension(175, 30);
        inputField1.setMaximumSize(inputFieldSize);
        inputField1.setHorizontalAlignment(JTextField.CENTER);

        randomButton = new JButton("Generate Random Number");
        randomButton.setFont(contentFont);
        randomButton.addActionListener(this);
        randomButton.setFocusPainted(false);
        randomButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(this);
        calculateButton.setFont(contentFont);
        calculateButton.setFocusPainted(false);
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        answerArea = new JPanel();
        answerArea.setLayout(new BoxLayout(answerArea, BoxLayout.PAGE_AXIS));
        answerArea.setBackground(bgColor);
        answerArea.setFont(contentFont);
        answerArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    /**
     * Adds sectionInfoArea, inputField1, randomButton, calculateButton, and answerArea. sectionInfoArea is placed at
     * the top and every other object is placed below the object that was previously placed.
     */
    protected void addComponents() {
        add(sectionInfoArea);
        add(Box.createRigidArea(spacingDimension));
        add(inputField1);
        add(Box.createRigidArea(spacingDimension));
        add(randomButton);
        add(Box.createRigidArea(spacingDimension));
        add(calculateButton);
        add(Box.createRigidArea(spacingDimension));
        add(answerArea);
    }

    /**
     * Displays "Invalid input" in answerArea.
     */
    protected void displayErrorMessage() {
        answerArea.removeAll();
        JLabel errorLabel = new JLabel("Invalid input");
        errorLabel.setFont(contentFont);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerArea.add(errorLabel);
        answerArea.revalidate();
        answerArea.repaint();
    }
}
