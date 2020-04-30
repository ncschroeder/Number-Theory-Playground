import contentpanels.HomePanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    JPanel currentPanel;

    public MainFrame(ButtonsPanel buttonsPanel, HomePanel homePanel) {
        super("Number Theory Playground");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);

        add(buttonsPanel, BorderLayout.PAGE_START);
        add(homePanel, BorderLayout.CENTER);
        currentPanel = homePanel;
        setVisible(true);
    }

    public void setCurrentPanel(JPanel panel) {
        remove(currentPanel);
        currentPanel = panel;
        add(currentPanel, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(this);
    }
}
