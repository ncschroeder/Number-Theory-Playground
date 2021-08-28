package com.nicholasschroeder.numbertheoryplayground.singlepanel;

import com.nicholasschroeder.numbertheoryplayground.Calculations;
import com.nicholasschroeder.numbertheoryplayground.PrimeFactorization;
import com.nicholasschroeder.numbertheoryplayground.Section;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;


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
    public void displaySingleInputAnswer(Section section, int anInt) {
        removeAll();
        String intString = Calculations.getNumberStringWithCommas(anInt);

        switch (section) {
            case PRIMES: {
                List<String> primes = Calculations.getPrimesAfter(anInt);
                JLabel label =
                        new JLabel(
                                "The first " + primes.size() +
                                        "primes greater than or equal to " + intString + " are:"
                        );
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

                for (String prime : primes) {
                    label = new JLabel(prime);
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
                List<String> twinPrimePairs = Calculations.getTwinPrimesAfter(anInt);
                JLabel label =
                        new JLabel(
                                "The first " + twinPrimePairs.size() + " pairs of twin primes after " +
                                        intString + " are"
                        );
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
                for (String pair : twinPrimePairs) {
                    label = new JLabel(pair);
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
                JLabel label = new JLabel("The prime factorization of " + intString + " is");
                label.setFont(contentFont);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(label);

                label = new JLabel(PrimeFactorization.getPfString(anInt));
                label.setFont(contentFont);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(label);
                break;
            }

            case DIVISIBILITY: {
                JLabel label =
                        new JLabel("Divisibility info for " + intString + " acquired by special tricks:");
                label.setFont(contentFont);
                add(label);

                label = new JLabel(Calculations.getDivisInfoViaTricks(anInt));
                label.setFont(contentFont);
                add(label);

                add(Box.createRigidArea(new Dimension(0, 10)));

                label = new JLabel("Info acquired from the prime factorization:");
                label.setFont(contentFont);
                add(label);
                for (String infoLine : Calculations.getDivisInfoViaPf(anInt)) {
                    label = new JLabel(infoLine);
                    label.setFont(contentFont);
                    add(label);
                }
                break;
            }

            case GOLDBACH: {
                List<String> goldbachPrimePairs = Calculations.getGoldbachPrimePairs(anInt);
                JLabel label =
                        new JLabel(
                                "There are " + goldbachPrimePairs.size() +
                                        " pairs of prime numbers that sum to " + intString + ". They are:"
                        );
                label.setFont(contentFont);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(label);

                JPanel pairsGrid = new NumberTheoryPlaygroundPanel();
                pairsGrid.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(5, 10, 5, 10);
                for (String pair : goldbachPrimePairs) {
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
                List<String> triplesEquations = Calculations.getPythagTriplesAfter(anInt);
                JLabel label =
                        new JLabel(
                                "The first " + triplesEquations.size() + " Pythagorean triples after " +
                                intString + " are:"
                        );
                label.setFont(contentFont);
                add(label);

                for (String equation : triplesEquations) {
                    label = new JLabel(equation);
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
    public void displayDoubleInputAnswer(Section section, int int1, int int2) {
        // Only valid section is GCD and LCM
        if (section != Section.GCD_LCM) {
            throw new IllegalArgumentException("invalid section: " + section);
        }

        removeAll();
        String intString1 = Calculations.getNumberStringWithCommas(int1);
        String intString2 = Calculations.getNumberStringWithCommas(int2);
        JLabel label =
                new JLabel(
                        "GCD and LCM info for " + intString1 + " and " + intString2 +
                                " acquired from the prime factorizations"
                );
        label.setFont(contentFont);
        add(label);
        for (String infoLine : Calculations.getGcdAndLcmInfoViaPf(int1, int2)) {
            label = new JLabel(infoLine);
            label.setFont(contentFont);
            add(label);
        }

        add(Box.createRigidArea(new Dimension(0, 10)));

        label = new JLabel("GCD info acquired from the Euclidean algorithm");
        label.setFont(contentFont);
        add(label);

        for (String infoLine : Calculations.getEuclideanInfo(int1, int2)) {
            label = new JLabel(infoLine);
            label.setFont(contentFont);
            add(label);
        }

        revalidate();
        repaint();
    }
}
