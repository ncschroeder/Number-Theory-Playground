import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class ButtonsPanel extends JPanel {
    public JButton homeButton;
    public JButton gcdLcmButton;
    public JButton divisibilityButton;
    public JButton primeFactorizationButton;
    public JButton goldbachButton;
    public JButton twinPrimesButton;

    public ButtonsPanel() {
//        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        homeButton = new JButton("Home");
        gcdLcmButton = new JButton("GCD and LCM");
        divisibilityButton = new JButton("Divisibility");
        primeFactorizationButton = new JButton("Prime Factorization");
        goldbachButton = new JButton("Goldbach Conjecture");
        twinPrimesButton = new JButton("Twin Primes");

        add(homeButton);
        add(gcdLcmButton);
        add(divisibilityButton);
        add(primeFactorizationButton);
        add(goldbachButton);
        add(twinPrimesButton);
    }

//    public JButton getGCDButton() { return GCDButton; }
//    public JButton getDivisibilityButton() { return divisibilityButton; }
}
