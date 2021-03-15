import javax.swing.*;
import java.awt.*;

/**
 * JPanel that displays either nothing, an invalid input message, or an answer for the current section.
 */
public class AnswerPanel extends NumberTheoryPlaygroundPanel {

    public AnswerPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void displayInvalidInputMessage() {
        removeAll();
        JLabel label = new JLabel("Invalid input");
        label.setFont(contentFont);
        add(label);
        revalidate();
        repaint();
    }

    /**
     * Displays the answer for sections that require the user to provide 1 input number.
     * @throws IllegalArgumentException if the section argument is not for a section that requires 1 input number.
     */
    public void displaySingleInputAnswer(Section section, int number) {
        removeAll();

        switch (section) {
            case PRIMES: {
                JLabel label = new JLabel("The first 30 primes after " + number + " are");
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                label.setFont(contentFont);
                add(label);

                JPanel primesGrid = new NumberTheoryPlaygroundPanel();
                primesGrid.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.PAGE_START;
                gbc.insets = new Insets(5, 15, 5, 15);

                for (int prime : Primes.getPrimesAfter(number)) {
                    label = new JLabel(String.valueOf(prime));
                    label.setFont(contentFont);
                    primesGrid.add(label, gbc);
                    gbc.gridx++;
                    // Make 6 primes be in each row
                    if (gbc.gridx == 6) {
                        gbc.gridx = 0;
                        gbc.gridy++;
                    }
                }
                add(primesGrid);
                break;
            }

            case TWIN_PRIMES: {
                JLabel label = new JLabel("The first 20 pairs of twin primes after " + number + " are");
                label.setFont(contentFont);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(label);

                JPanel twinPrimesGrid = new NumberTheoryPlaygroundPanel();
                twinPrimesGrid.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(5, 15, 5, 15);
                // Make 4 pairs of twin primes be in each row
                for (String primesPair : TwinPrimes.getTwinPrimesAfter(number)) {
                    label = new JLabel(primesPair);
                    label.setFont(contentFont);
                    twinPrimesGrid.add(label, gbc);
                    gbc.gridx++;
                    if (gbc.gridx == 4) {
                        gbc.gridx = 0;
                        gbc.gridy++;
                    }
                }
                add(twinPrimesGrid);
                break;
            }

            case PRIME_FACTORIZATION: {
                JLabel label = new JLabel("The prime factorization of " + number + " is");
                label.setFont(contentFont);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(label);

                label = new JLabel(PrimeFactorization.getPfString(number));
                label.setFont(contentFont);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(label);
                break;
            }

            case DIVISIBILITY: {
                JLabel label = new JLabel("Info acquired by special tricks");
                label.setFont(contentFont);
                add(label);
                for (String infoLine : Divisibility.getDivisInfoViaTricks(number)) {
                    label = new JLabel(infoLine);
                    label.setFont(contentFont);
                    add(label);
                }

                add(Box.createRigidArea(new Dimension(0, 10)));

                label = new JLabel("Info acquired from the prime factorization");
                label.setFont(contentFont);
                add(label);
                for (String infoLine : Divisibility.getDivisInfoViaPf(number)) {
                    label = new JLabel(infoLine);
                    label.setFont(contentFont);
                    add(label);
                }
                break;
            }

            case GOLDBACH: {
                JLabel label = new JLabel("Prime number pairs that sum to " + number);
                label.setFont(contentFont);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(label);

                JPanel pairsGrid = new NumberTheoryPlaygroundPanel();
                pairsGrid.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(5, 10, 5, 10);
                for (String pair : Goldbach.getGoldbachPrimePairs(number)) {
                    label = new JLabel(pair);
                    label.setFont(contentFont);
                    pairsGrid.add(label, gbc);
                    gbc.gridx++;
                    // Add 5 pairs to each line
                    if (gbc.gridx == 5) {
                        gbc.gridx = 0;
                        gbc.gridy++;
                    }
                }
                add(pairsGrid);
                break;
            }

            case PYTHAG_TRIPLES: {
                JLabel label = new JLabel("The first 10 Pythagorean triples after " + number + " are");
                label.setFont(contentFont);
                add(label);

                for (String triple : PythagoreanTriples.getPythagTriplesAfter(number)) {
                    label = new JLabel(triple);
                    label.setFont(contentFont);
                    add(label);
                }
                break;
            }

            default:
                throw new IllegalArgumentException("invalid section: " + section);
        }

        revalidate();
        repaint();
    }

    /**
     * Displays the answer for sections that require the user to provide 2 input numbers.
     * @throws IllegalArgumentException if the section argument is not for a section that requires 2 input numbers.
     */
    public void displayDoubleInputAnswer(Section section, int firstNumber, int secondNumber) {
        // Only valid section is GCD and LCM
        if (section != Section.GCD_LCM) {
            throw new IllegalArgumentException("invalid section: " + section);
        }

        removeAll();

        JLabel label = new JLabel("GCD and LCM info acquired from the prime factorizations");
        label.setFont(contentFont);
        add(label);
        for (String infoLine : GcdAndLcm.getGcdAndLcmInfoViaPf(firstNumber, secondNumber)) {
            label = new JLabel(infoLine);
            label.setFont(contentFont);
            add(label);
        }

        add(Box.createRigidArea(new Dimension(0, 10)));

        label = new JLabel("GCD info acquired from the Euclidean algorithm");
        label.setFont(contentFont);
        add(label);
        for (String infoLine : GcdAndLcm.getEuclideanInfo(firstNumber, secondNumber)) {
            label = new JLabel(infoLine);
            label.setFont(contentFont);
            add(label);
        }

        revalidate();
        repaint();
    }
}