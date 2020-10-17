import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

/**
 * JPanel that is in charge of the buttons at the top.
 */
public class ButtonsPanel extends JPanel {
    final private ActionListener buttonClickListener;
    final private Font buttonFont;

    public ButtonsPanel(Color bgColor, ActionListener listener) {
        setBackground(bgColor);
        setPreferredSize(new Dimension(0, 60));
        buttonClickListener = listener;
        buttonFont = new Font("Garamond", Font.PLAIN, 20);
    }

    public void addButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setFont(buttonFont);
        button.addActionListener(buttonClickListener);
        button.setFocusPainted(false);
        add(button);
    }
}
