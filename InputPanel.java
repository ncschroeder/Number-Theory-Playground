import javax.swing.*;
import java.awt.*;

/**
 * JPanel that consists of a text field, a "Randomize" button, an increment ("+") button, and a decrement ("-") button.
 */
public class InputPanel extends NumberTheoryPlaygroundPanel {
    private final JTextField textField = new JTextField();
    private final JButton randomBtn = new JButton("Randomize");
    private final JButton incrementBtn = new JButton("+");
    private final JButton decrementBtn = new JButton("-");

    public InputPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setMaximumSize(new Dimension(200, 125));
        textField.setMaximumSize(new Dimension(200, 30));
        textField.setFont(contentFont);
        add(textField);

        add(Box.createRigidArea(new Dimension(0, 2)));

        randomBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        randomBtn.setFocusPainted(false);
        randomBtn.setFont(contentFont);
        add(randomBtn);

        add(Box.createRigidArea(new Dimension(0, 2)));

        incrementBtn.setFocusPainted(false);
        incrementBtn.setFont(contentFont);

        decrementBtn.setFocusPainted(false);
        decrementBtn.setFont(contentFont);

        JPanel btnsPanel = new JPanel();
        btnsPanel.setLayout(new BoxLayout(btnsPanel, BoxLayout.LINE_AXIS));
        btnsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnsPanel.add(incrementBtn);
        btnsPanel.add(decrementBtn);
        add(btnsPanel);
    }

    public JTextField getTextField() {
        return textField;
    }

    public void clearTextField() {
        textField.setText("");
    }

    public JButton getRandomBtn() {
        return randomBtn;
    }

    public JButton getIncrementBtn() {
        return incrementBtn;
    }

    public JButton getDecrementBtn() {
        return decrementBtn;
    }
}
