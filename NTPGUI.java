import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * Launcher for the Number Theory Playground Graphical User Interface.
 */
public class NTPGUI {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    static void createAndShowGUI() {
        JFrame frame = new JFrame("Number Theory Playground");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 900);
        JScrollPane sp = new JScrollPane(new MainPanel());
        sp.getVerticalScrollBar().setUnitIncrement(5);
        frame.getContentPane().add(sp);
        frame.setVisible(true);
    }
}