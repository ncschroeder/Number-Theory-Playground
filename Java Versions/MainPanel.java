package com.nicholasschroeder.numbertheoryplayground;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nicholasschroeder.numbertheoryplayground.NTPGUI.*;
import static com.nicholasschroeder.numbertheoryplayground.Divisibility.isEven;

/**
 * Panel that consists of all the content of the GUI version of the Number Theory Playground.
 */
public class MainPanel extends NTPPanel {
    private Section currentSection;
    private int minInputInt, maxInputInt;

    public MainPanel() {
        JLabel sectionHeading = createCenteredLabel("", NTPGUI.createBoldGaramondFont(35));
        var sectionInfoTextArea = new NTPTextArea();
        var directionsTextArea = new NTPTextArea();
        var answerPanel = new AnswerPanel();
        var inputPanel1 = new InputPanel();
        var inputPanel2 = new InputPanel();
        
        NTPPanel inputPanelPanel =
            new NTPPanel()
            .setToBoxLayoutWithLineAxis()
            .add(inputPanel1)
            .add(inputPanel2);
        
        JButton calcBtn = createButton("Calculate", garamondFontSize25);
        centerComponent(calcBtn);
        
        calcBtn.addActionListener(e -> {
            try {
                int inputInt1 = inputPanel1.getInputAsInt();
                List<Component> components;
                if (currentSection.isSingleInputSection()) {
                    components = ((SingleInputSection)currentSection).getGuiComponents(inputInt1);
                } else {
                    int inputInt2 = inputPanel2.getInputAsInt();
                    components = ((DoubleInputSection)currentSection).getGuiComponents(inputInt1, inputInt2);
                }
                answerPanel.displayComponents(components);
            } catch (IllegalArgumentException ex) {
                answerPanel.displayInvalidInputMessage();
            }
        });
        
        var sectionBtnsPanels = new ArrayList<NTPPanel>(2);
        NTPPanel sectionBtnsPanel = new NTPPanel().setToBoxLayoutWithLineAxis();
        Font sectionBtnFont = garamondFontSize25;
        JButton homeBtn = createButton("Home", sectionBtnFont);

        homeBtn.addActionListener(e -> {
            inputPanel1.setVisible(false);
            inputPanel2.setVisible(false);
            calcBtn.setVisible(false);
            answerPanel.clear();
            sectionHeading.setText(homeBtn.getText());
            sectionInfoTextArea.setText("Welcome to the Number Theory Playground.");
            directionsTextArea.setText("");
        });

        homeBtn.doClick();
        sectionBtnsPanel.add(homeBtn);
    
        /*
        Let btnsAndSections be a Map where the keys are section buttons and the values are the
        corresponding Sections. The keys will be Objects because event.getSource in the lambda
        below will be an Object representation of a button that was clicked.
        */
        var btnsAndSections = new HashMap<Object, Section>();
        
        ActionListener changeSection = event -> {
            inputPanel1.clearTextField();
            inputPanel2.clearTextField();
            answerPanel.clear();
            
            currentSection = btnsAndSections.get(event.getSource());
            minInputInt = currentSection.getMinInputInt();
            maxInputInt = currentSection.getMaxInputInt();
            
            sectionHeading.setText(currentSection.getHeadingText());
            sectionInfoTextArea.setText(String.join("\n\n", currentSection.getInfo()));
            
            boolean oneIntNeeded = currentSection.isSingleInputSection();
            String directions =
                String.format(
                    "Enter or generate %s and click the Calculate button to %s. %s.",
                    oneIntNeeded ? "an integer" : "2 integers",
                    currentSection.getActionSentenceEnding(),
                    currentSection.getInputConstraintsSentence()
                );
            directionsTextArea.setText(directions);
            inputPanel1.setVisible(true);
            inputPanel2.setVisible(!oneIntNeeded);
            calcBtn.setVisible(true);
        };
        
        // Let the first 7 section buttons be in 1 panel and have the rest of the buttons be in another panel
        for (Section section : Section.createInstances()) {
            if (section instanceof PythagoreanTriples.Section) {
                sectionBtnsPanels.add(sectionBtnsPanel);
                sectionBtnsPanel = new NTPPanel().setToBoxLayoutWithLineAxis();
            }
            
            JButton sectionBtn = createButton(section.getHeadingText(), sectionBtnFont);
            btnsAndSections.put(sectionBtn, section);
            sectionBtn.addActionListener(changeSection);
            sectionBtnsPanel.add(sectionBtn);
        }
        sectionBtnsPanels.add(sectionBtnsPanel);
        
        
        setToBoxLayoutWithPageAxis();
        
        addGap(15);
        addCenteredLabel("Number Theory Playground", createBoldGaramondFont(40));
        addGap(15);
        sectionBtnsPanels.forEach(this::add);
        addGap(15);
        add(sectionHeading);
        addGap(10);
        add(sectionInfoTextArea);
        addGap(15);
        add(directionsTextArea);
        addGap(15);
        add(inputPanelPanel);
        addGap(15);
        add(calcBtn);
        addGap(15);
        add(answerPanel);
    }
    
    
    /**
     * Panel that consists of a text field, a "Randomize" button, an increment ("+") button, and a
     * decrement ("-") button. This is an inner class so the on-click actions of the buttons can access
     * the current section and max and min inputs.
     */
    public class InputPanel extends NTPPanel {
        private final JTextField textField = new JTextField();
        
        public InputPanel() {
            textField.setMaximumSize(new Dimension(200, 30));
            textField.setFont(garamondFontSize25);
            
            JButton randomBtn = createButton("Randomize", garamondFontSize25);
            centerComponent(randomBtn);
            randomBtn.addActionListener(e -> setTextFieldText(currentSection.getRandomValidInt()));
    
            var btnSize = new Dimension(60, 50);
            
            JButton incrementBtn = createButton("+", garamondFontSize25);
            incrementBtn.setMinimumSize(btnSize);
            incrementBtn.setMaximumSize(btnSize);

            // For incrementing and decrementing, if the current section is the Goldbach section, make
            // the input int even.
            incrementBtn.addActionListener(e -> {
                try {
                    int inputInt = getInputAsInt();
                    inputInt =
                        inputInt >= maxInputInt || inputInt < minInputInt
                        ? minInputInt
                        : inputInt + (currentSection.isGoldbachSection() && isEven(inputInt) ? 2 : 1);
                    setTextFieldText(inputInt);
                } catch (NumberFormatException ex) {
                    if (hasBlankInput()) {
                        setTextFieldText(minInputInt);
                    }
                }
            });
    
            JButton decrementBtn = createButton("-", garamondFontSize25);
            decrementBtn.setMinimumSize(btnSize);
            decrementBtn.setMaximumSize(btnSize);
            
            decrementBtn.addActionListener(e -> {
                try {
                    int inputInt = getInputAsInt();
                    inputInt =
                        inputInt <= minInputInt || inputInt > maxInputInt
                        ? maxInputInt
                        : inputInt - (currentSection.isGoldbachSection() && isEven(inputInt) ? 2 : 1);
                    setTextFieldText(inputInt);
                } catch (NumberFormatException ex) {
                    if (hasBlankInput()) {
                        setTextFieldText(maxInputInt);
                    }
                }
            });
    
            NTPPanel btnsPanel =
                new NTPPanel()
                .setToBoxLayoutWithLineAxis()
                .center()
                .add(incrementBtn)
                .addGap(5)
                .add(decrementBtn);

            setToBoxLayoutWithPageAxis();
            setMaximumSize(new Dimension(200, 200));
            
            add(textField);
            addGap(5);
            add(randomBtn);
            addGap(5);
            add(btnsPanel);
        }
    
        private void setTextFieldText(int i) {
            textField.setText(String.valueOf(i));
        }
        
        public void clearTextField() {
            textField.setText("");
        }
    
        /**
         * Tries to parse the text field text as an int. If this is successful, that int gets returned.
         * Otherwise, a NumberFormatException gets thrown.
         */
        public int getInputAsInt() {
            return Integer.parseInt(textField.getText().trim());
        }
    
        /**
         * Returns true if the text field is empty or only has whitespace, false otherwise.
         */
        public boolean hasBlankInput() {
            return textField.getText().isBlank();
        }
    }
}