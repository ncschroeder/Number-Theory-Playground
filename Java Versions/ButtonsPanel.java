import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * JPanel that consists of buttons to change sections. The action listener provided as an argument for the constructor
 * is added to each button. The action command of each button are the string versions of the Section enum instances.
 */
public class ButtonsPanel extends JPanel {
    public ButtonsPanel(ActionListener al) {
        Font btnFont = new Font("Garamond", Font.PLAIN, 20);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JButton btn = new JButton("Home");
        btn.setFont(btnFont);
        btn.setFocusPainted(false);
        btn.setActionCommand("HOME");
        btn.addActionListener(al);
        add(btn);

        btn = new JButton("Prime Numbers");
        btn.setFont(btnFont);
        btn.setFocusPainted(false);
        btn.addActionListener(al);
        btn.setActionCommand("PRIMES");
        add(btn);

        btn = new JButton("Twin Primes");
        btn.setFont(btnFont);
        btn.setFocusPainted(false);
        btn.setActionCommand("TWIN_PRIMES");
        btn.addActionListener(al);
        add(btn);

        btn = new JButton("Prime Factorization");
        btn.setFont(btnFont);
        btn.setFocusPainted(false);
        btn.setActionCommand("PRIME_FACTORIZATION");
        btn.addActionListener(al);
        add(btn);

        btn = new JButton("Divisibility");
        btn.setFont(btnFont);
        btn.setFocusPainted(false);
        btn.setActionCommand("DIVISIBILITY");
        btn.addActionListener(al);
        add(btn);

        btn = new JButton("GCD and LCM");
        btn.setFont(btnFont);
        btn.setFocusPainted(false);
        btn.setActionCommand("GCD_LCM");
        btn.addActionListener(al);
        add(btn);

        btn = new JButton("Goldbach Conjecture");
        btn.setFont(btnFont);
        btn.setFocusPainted(false);
        btn.setActionCommand("GOLDBACH");
        btn.addActionListener(al);
        add(btn);

        btn = new JButton("Pythagorean Triples");
        btn.setFont(btnFont);
        btn.setFocusPainted(false);
        btn.setActionCommand("PYTHAG_TRIPLES");
        btn.addActionListener(al);
        add(btn);
    }
}
