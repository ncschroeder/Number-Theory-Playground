import contentpanels.ContentPanels;
import java.io.IOException;

public class NumberTheoryPlayground {

    public static void main(String[] args) throws IOException {

        /*SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            }
        });*/

        ButtonsPanel buttonsPanel = new ButtonsPanel();
        ContentPanels contentPanels = new ContentPanels();
        MainFrame mainFrame = new MainFrame(buttonsPanel, contentPanels.homePanel);
//        System.out.println(PrimeNumbers.getGoldbachPairs(100));

        buttonsPanel.homeButton.addActionListener(e -> {
            mainFrame.setCurrentPanel(contentPanels.homePanel);
        });

        buttonsPanel.gcdLcmButton.addActionListener(e -> {
            mainFrame.setCurrentPanel(contentPanels.gcdLcmPanel);
        });

        buttonsPanel.divisibilityButton.addActionListener(e -> {
            mainFrame.setCurrentPanel(contentPanels.divisPanel);
        });

        buttonsPanel.primeFactorizationButton.addActionListener(e -> {
            mainFrame.setCurrentPanel(contentPanels.primeFactorizationPanel);
        });

        buttonsPanel.goldbachButton.addActionListener(e -> {
            mainFrame.setCurrentPanel(contentPanels.goldbachPanel);
        });

        buttonsPanel.twinPrimesButton.addActionListener(e -> {
            mainFrame.setCurrentPanel(contentPanels.twinPrimesPanel);
        });
    }
}