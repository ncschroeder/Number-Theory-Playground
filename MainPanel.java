import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * JPanel that consists of all the content of the GUI version of the Number Theory Playground.
 */
public class MainPanel extends NumberTheoryPlaygroundPanel {
    JLabel sectionHeading = new JLabel();
    JTextArea sectionInfoTextArea = new JTextArea();
    InputPanel inputPanel1 = new InputPanel();
    InputPanel inputPanel2 = new InputPanel();
    JButton calcBtn = new JButton("Calculate");
    AnswerPanel answerPanel = new AnswerPanel();
    Section currentSection = Section.HOME;

    public MainPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel mainHeading = new JLabel("Number Theory Playground");
        mainHeading.setFont(new Font("Garamond", Font.PLAIN, 35));
        mainHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(mainHeading);

        add(Box.createRigidArea(new Dimension(0, 10)));

        add(new ButtonsPanel(new SectionButtonListener()));

        add(Box.createRigidArea(new Dimension(0, 10)));

        sectionHeading.setFont(new Font("Garamond", Font.PLAIN, 30));
        sectionHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(sectionHeading);

        sectionInfoTextArea.setLineWrap(true);
        sectionInfoTextArea.setWrapStyleWord(true);
        sectionInfoTextArea.setEditable(false);
        sectionInfoTextArea.setFont(contentFont);
        sectionInfoTextArea.setBackground(bgColor);
        add(sectionInfoTextArea);

        InputPanelListener ipl = new InputPanelListener();

        // Add ipl to every button on both input panels. Have the action command be the action followed by a space
        // followed by the number of the input panel to perform that action on.
        JButton randomBtn1 = inputPanel1.getRandomBtn();
        randomBtn1.addActionListener(ipl);
        randomBtn1.setActionCommand("randomize 1");

        JButton incrementBtn1 = inputPanel1.getIncrementBtn();
        incrementBtn1.addActionListener(ipl);
        incrementBtn1.setActionCommand("increment 1");

        JButton decrementBtn1 = inputPanel1.getDecrementBtn();
        decrementBtn1.addActionListener(ipl);
        decrementBtn1.setActionCommand("decrement 1");

        JButton randomBtn2 = inputPanel2.getRandomBtn();
        randomBtn2.addActionListener(ipl);
        randomBtn2.setActionCommand("randomize 2");

        JButton incrementBtn2 = inputPanel2.getIncrementBtn();
        incrementBtn2.addActionListener(ipl);
        incrementBtn2.setActionCommand("increment 2");

        JButton decrementBtn2 = inputPanel2.getDecrementBtn();
        decrementBtn2.addActionListener(ipl);
        decrementBtn2.setActionCommand("decrement 2");

        add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel inputPanelPanel = new NumberTheoryPlaygroundPanel();
        inputPanelPanel.setLayout(new BoxLayout(inputPanelPanel, BoxLayout.LINE_AXIS));
        inputPanelPanel.add(inputPanel1);
        inputPanelPanel.add(inputPanel2);
        add(inputPanelPanel);

        add(Box.createRigidArea(new Dimension(0, 10)));

        calcBtn.addActionListener(new CalculateButtonListener());
        calcBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        calcBtn.setFocusPainted(false);
        calcBtn.setFont(contentFont);
        add(calcBtn);

        add(Box.createRigidArea(new Dimension(0, 10)));

        add(answerPanel);

        showHomeSection();
        final int SECTION_INFO_WIDTH = 800;
        sectionInfoTextArea.setMaximumSize(new Dimension(SECTION_INFO_WIDTH, sectionInfoTextArea.getPreferredSize().height));
    }

    void showHomeSection() {
        sectionHeading.setText("Home");
        sectionInfoTextArea.setText(Section.HOME.getInfo());
        inputPanel1.setVisible(false);
        inputPanel2.setVisible(false);
        calcBtn.setVisible(false);
    }

    void showSingleInputPanelSection() {
        inputPanel1.setVisible(true);
        inputPanel2.setVisible(false);
        calcBtn.setVisible(true);
    }

    void showDoubleInputPanelSection() {
        inputPanel1.setVisible(true);
        inputPanel2.setVisible(true);
        calcBtn.setVisible(true);
    }

    /**
     * Responsible for changing sections.
     */
    class SectionButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            inputPanel1.clearTextField();
            inputPanel2.clearTextField();

            answerPanel.removeAll();
            answerPanel.revalidate();
            answerPanel.repaint();

            currentSection = Section.valueOf(e.getActionCommand());
            if (currentSection == Section.HOME) {
                showHomeSection();
            } else {
                sectionInfoTextArea.setText(currentSection.getInfo() + "\n\n");
                String directions = "nothing";

                switch (currentSection) {
                    case PRIMES:
                        sectionHeading.setText("Prime Numbers");
                        directions = "Enter a number to get the first 30 primes that appear after that number. Have this " +
                                "number be greater than or equal to 0 and less than or equal to 1 billion.";
                        showSingleInputPanelSection();
                        break;

                    case TWIN_PRIMES:
                        sectionHeading.setText("Twin Prime Numbers");
                        directions = "Enter a number to get the first 20 pairs of twin prime numbers that appear after " +
                                "that number. Have this number be greater than or equal to 0 and less than or equal to 1 billion.";
                        showSingleInputPanelSection();
                        break;

                    case PRIME_FACTORIZATION:
                        sectionHeading.setText("Prime Factorization");
                        directions = "Enter a number to get it's prime factorization. Have this number be greater than or " +
                                "equal to 2 and less than or equal to 10 thousand.";
                        showSingleInputPanelSection();
                        break;

                    case DIVISIBILITY:
                        sectionHeading.setText("Divisibility");
                        directions = "Enter a number to get it's divisibility info. Have this number be greater than or " +
                                "equal to 2 and less than or equal to 10 thousand.";
                        showSingleInputPanelSection();
                        break;

                    case GCD_LCM:
                        sectionHeading.setText("GCD and LCM");
                        directions = "Enter 2 numbers to get their GCD and LCM info. Have these numbers be greater than or " +
                                "equal to 2 and less than or equal to 10 thousand.";
                        showDoubleInputPanelSection();
                        break;

                    case GOLDBACH:
                        sectionHeading.setText("Goldbach Conjecture");
                        directions = "Enter a number to get the pairs of prime numbers that sum to that number. Have this " +
                                "number be even, greater than or equal to 4, and less than or equal to 100 thousand.";
                        showSingleInputPanelSection();
                        break;

                    case PYTHAG_TRIPLES:
                        sectionHeading.setText("Pythagorean Triples");
                        directions = "Enter a number to get the first 10 Pythagorean triples that appear after that number. " +
                                "Have this number be greater than or equal to 0 and less than or equal to 1 thousand.";
                        showSingleInputPanelSection();
                        break;
                }

                sectionInfoTextArea.append(directions);
            }

            // resize sectionInfoTextArea so that it only takes up the space it needs
            sectionInfoTextArea.setMaximumSize(sectionInfoTextArea.getPreferredSize());
        }
    }

    /**
     * Responsible for events that occur after the calculate button is hit.
     */
    class CalculateButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean inputError = false;
            int firstNumber = 0, secondNumber = 0;

            // Validate the user's input and if there are any errors, set inputError to true. Goldbach section must
            // have even number.
            try {
                firstNumber = Integer.parseInt(inputPanel1.getTextField().getText());
                if (firstNumber < currentSection.getMinInputInt() || firstNumber > currentSection.getMaxInputInt() ||
                        (currentSection == Section.GOLDBACH && firstNumber % 2 != 0)) {
                    inputError = true;
                } else if (currentSection == Section.GCD_LCM) {
                    // GCD and LCM section is the only one that uses a second input number.
                    secondNumber = Integer.parseInt(inputPanel2.getTextField().getText());
                    if (secondNumber < currentSection.getMinInputInt() || secondNumber > currentSection.getMaxInputInt()) {
                        inputError = true;
                    }
                }
            } catch (NumberFormatException ex) {
                // This block is reached if the input panel texts were not able to be parsed as an int
                inputError = true;
            }

            if (inputError) {
                answerPanel.displayInvalidInputMessage();
            } else if (currentSection == Section.GCD_LCM) {
                answerPanel.displayDoubleInputAnswer(currentSection, firstNumber, secondNumber);
            } else {
                answerPanel.displaySingleInputAnswer(currentSection, firstNumber);
            }
        }
    }

    /**
     * Responsible for changing the input panel text field contents when a user clicks the randomize, increment, and
     * decrement buttons for one of the input panels.
     */
    class InputPanelListener implements ActionListener {
        private final Random random = new Random();

        @Override
        public void actionPerformed(ActionEvent e) {
            // All action commands are a string with the action first followed by a space followed by the number
            // of the input panel to perform that action on.
            String[] actionCommandContents = e.getActionCommand().split(" ");
            String actionToPerform = actionCommandContents[0];
            String inputPanelNumber = actionCommandContents[1];

            InputPanel inputPanelToModify;
            if (inputPanelNumber.equals("1")) {
                inputPanelToModify = inputPanel1;
            } else if (inputPanelNumber.equals("2")) {
                inputPanelToModify = inputPanel2;
            } else {
                throw new IllegalArgumentException("invalid inputPanelNumber: " + inputPanelNumber);
            }

            int currentInputNumber;
            int newInputNumber;

            switch (actionToPerform) {
                case "randomize":
                    // generate random number in valid range for current section
                    int randomNumber = Math.max(random.nextInt(currentSection.getMaxInputInt()), currentSection.getMinInputInt());
                    // Goldbach section requires even number
                    if (currentSection == Section.GOLDBACH && randomNumber % 2 != 0) {
                        randomNumber++;
                    }
                    inputPanelToModify.getTextField().setText(String.valueOf(randomNumber));
                    return;

                case "increment":
                    try {
                        currentInputNumber = Integer.parseInt(inputPanelToModify.getTextField().getText());
                    } catch (NumberFormatException ex) {
                        // Do nothing if there was not an int in the input panel text field
                        return;
                    }

                    if (currentInputNumber >= currentSection.getMaxInputInt() || currentInputNumber < currentSection.getMinInputInt()) {
                        inputPanelToModify.getTextField().setText(String.valueOf(currentSection.getMinInputInt()));
                    } else {
                        newInputNumber = currentInputNumber + 1;
                        // Make even number displayed for Goldbach section
                        if (currentSection == Section.GOLDBACH && newInputNumber % 2 != 0) {
                            newInputNumber++;
                        }
                        inputPanelToModify.getTextField().setText(String.valueOf(newInputNumber));
                    }
                    return;

                case "decrement":
                    try {
                        currentInputNumber = Integer.parseInt(inputPanelToModify.getTextField().getText());
                    } catch (NumberFormatException ex) {
                        // Do nothing if there was not an int in the input panel text field
                        return;
                    }

                    if (currentInputNumber <= currentSection.getMinInputInt() || currentInputNumber > currentSection.getMaxInputInt()) {
                        inputPanelToModify.getTextField().setText(String.valueOf(currentSection.getMaxInputInt()));
                    } else {
                        newInputNumber = currentInputNumber - 1;
                        // Make even number displayed for Goldbach section
                        if (currentSection == Section.GOLDBACH && newInputNumber % 2 != 0) {
                            newInputNumber--;
                        }
                        inputPanelToModify.getTextField().setText(String.valueOf(newInputNumber));
                    }
                    return;

                default:
                    throw new IllegalArgumentException("invalid action: " + actionToPerform);
            }
        }
    }
}
