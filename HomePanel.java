import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;

/**
 * JPanel in charge of the content for the home section of the GUI.
 */
public class HomePanel extends JPanel {
    /**
     * @param bgColor Should be the same as all other panels.
     * @param font Should be the same as all other panels.
     */
    public HomePanel(Color bgColor, Font font) {
        setBackground(bgColor);
        JLabel welcomeMessage = new JLabel("Welcome to the number theory playground");
        welcomeMessage.setFont(font);
        add(welcomeMessage);
    }
}
