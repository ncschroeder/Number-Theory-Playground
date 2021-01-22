import javax.swing.*;
import java.awt.*;

/**
 * JPanel consisting of common data for the JPanels used that extend this class.
 */
public class NumberTheoryPlaygroundPanel extends JPanel {
    protected static Color bgColor = Color.CYAN;
    protected static Font contentFont = new Font("Garamond", Font.PLAIN, 25);

    public NumberTheoryPlaygroundPanel() {
        setBackground(bgColor);
    }
}
