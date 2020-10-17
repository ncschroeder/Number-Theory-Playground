import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Launcher for the Number Theory Playground Graphical User Interface.
 */
public class NTPGUI implements ActionListener {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Number Theory Playground");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 900);
        NTPGUI ntpgui = new NTPGUI();
        ntpgui.addComponentsToPane(frame.getContentPane());
        frame.setVisible(true);
    }

    CardLayout cardsPanelLayout;
    JPanel cardsPanel;

    public void addComponentsToPane(Container pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        cardsPanelLayout = new CardLayout();
        cardsPanel = new JPanel(cardsPanelLayout);

        Color bgColor = new Color(0x5BD6F1);
        NumberTheoryPlaygroundPanel.setBgColor(bgColor);

        Font contentFont = new Font("Garamond", Font.PLAIN, 25);
        NumberTheoryPlaygroundPanel.setContentFont(contentFont);

        ButtonsPanel buttonsPanel = new ButtonsPanel(bgColor, this);

        String[] buttonTexts = new String[] {
                "Home",
                "Prime Numbers",
                "Twin Primes",
                "Prime Factorization",
                "Divisibility",
                "GCD and LCM",
                "Goldbach Conjecture",
                "Pythagorean Triples"
        };

        JPanel[] panels = {
                new HomePanel(bgColor, contentFont),
                new PrimeNumbersPanel(),
                new TwinPrimesPanel(),
                new PrimeFactorizationPanel(),
                new DivisibilityPanel(),
                new GcdLcmPanel(),
                new GoldbachPanel(),
                new PythagoreanTriplesPanel()
        };

        // Add buttons to buttonsPanel and place all panels in cardsPanel and have each one associated with the text
        // of their corresponding button.
        for (int i = 0; i < buttonTexts.length; i++) {
            buttonsPanel.addButton(buttonTexts[i]);
            cardsPanel.add(panels[i], buttonTexts[i]);
        }

        pane.add(buttonsPanel);
        pane.add(cardsPanel);
    }

    public void actionPerformed(ActionEvent event) {
        // The only sources are JButtons
        JButton sourceButton = (JButton) event.getSource();
        // Show the panel whose button was pressed.
        cardsPanelLayout.show(cardsPanel, sourceButton.getText());
    }
}