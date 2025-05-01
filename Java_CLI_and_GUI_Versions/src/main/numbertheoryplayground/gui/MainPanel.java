package numbertheoryplayground.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.*;
import java.util.List;
import java.util.function.Function;
import numbertheoryplayground.sectionclasses.abstract_.*;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.gui.NtpGui.*;
import static numbertheoryplayground.sectionclasses.outer.Divisibility.isEven;

/**
 * Panel that consists of all the content of the GUI version of the Number Theory Playground.
 */
final class MainPanel extends NtpPanel {
    private Section curSection;
    private long minInput, maxInput;
    
    MainPanel() {
        JLabel mainHeadingLabel =
            createCenteredLabel("Number Theory Playground", createBoldGaramondFont(40));
        JLabel sectionHeadingLabel = createCenteredLabel("", createBoldGaramondFont(35));
        var sectionInfoTextArea = new NtpTextArea(900);
        var directionsTextArea = new NtpTextArea(750);
        var inputPanel1 = new InputPanel();
        var inputPanel2 = new InputPanel();
        NtpPanel inputPanelPanel =
            new NtpPanel()
            .setToBoxLayoutWithLineAxis()
            .addAll(inputPanel1, inputPanel2);
        var answerPanel = new AnswerPanel();
        
        JButton calcBtn = createButton("Calculate", GARAMOND_25);
        centerComponent(calcBtn);
        calcBtn.addActionListener(e -> {
            try {
                var input1Long = inputPanel1.getInputAsLong();
                var input1String = createStringWithCommas(input1Long);
                List<Component> components = null;
                
                if (curSection instanceof SingleInputSection sis) {
                    components = sis.getGuiComponents(input1Long, input1String);
                } else if (curSection instanceof DoubleInputSection dis) {
                    var input2Long = inputPanel2.getInputAsLong();
                    var input2String = createStringWithCommas(input2Long);
                    components =
                        dis.getGuiComponents(input1Long, input2Long, input1String, input2String);
                }
                
                answerPanel.displayComponents(components);
            } catch (NumberFormatException | InvalidInputNumberException ex) {
                answerPanel.displayInvalidInputMessage();
            }
        });
        
        Font sectionBtnFont = GARAMOND_25;
        
        var homeText = """
            Welcome to the Number Theory Playground.
            """;
        
        JButton homeBtn = createButton("Home", sectionBtnFont);
        homeBtn.addActionListener(e -> {
            inputPanel1.setVisible(false);
            inputPanel2.setVisible(false);
            calcBtn.setVisible(false);
            answerPanel.clear();
            sectionHeadingLabel.setText(homeBtn.getText());
            sectionInfoTextArea.setText(homeText);
            directionsTextArea.clear();
        });
        homeBtn.doClick();
        
        Function<Section, JButton> createSectionBtn = (section) -> {
            JButton btn = createButton(section.getHeading(), sectionBtnFont);
            
            btn.addActionListener(e -> {
                curSection = section;
                minInput = section.getMinInput();
                maxInput = section.getMaxInput();
                sectionHeadingLabel.setText(section.getHeading());
                sectionInfoTextArea.setText(String.join("\n\n", section.getInfoParagraphs()));
                boolean oneIntegerNeeded = section.isSingleInputSection();
                
                String actionSentence =
                    String.format(
                        "Enter or generate %s and click the Calculate button to %s",
                        oneIntegerNeeded ? "an integer" : "2 integers",
                        section.getActionSentenceEnding()
                    );
                directionsTextArea.setText(actionSentence + ' ' + section.getInputInfoSentences());
                
                inputPanel1.clearTextField();
                inputPanel1.setVisible(true);
                inputPanel2.clearTextField();
                inputPanel2.setVisible(!oneIntegerNeeded);
                calcBtn.setVisible(true);
                answerPanel.clear();
            });
            
            return btn;
        };
        
        List<Section> sections = Section.createInstances();
        
        NtpPanel sectionBtnsPanel1 =
            new NtpPanel()
            .setToBoxLayoutWithLineAxis()
            .add(homeBtn)
            .addAll(sections.stream().limit(6).map(createSectionBtn));
        
        NtpPanel sectionBtnsPanel2 =
            new NtpPanel()
            .setToBoxLayoutWithLineAxis()
            .addAll(sections.stream().skip(6).map(createSectionBtn));
        
        setToBoxLayoutWithPageAxis();
        addAll(
            createGap(15),
            mainHeadingLabel,
            createGap(15),
            sectionBtnsPanel1,
            sectionBtnsPanel2,
            createGap(15),
            sectionHeadingLabel,
            createGap(10),
            sectionInfoTextArea,
            createGap(15),
            directionsTextArea,
            createGap(15),
            inputPanelPanel,
            createGap(15),
            calcBtn,
            createGap(15),
            answerPanel
        );
    }
    
    
    /**
     * Panel that consists of a text field, a "Randomize" button, a "+" button, and a "−" button.
     * This is an inner class so the on-click actions of the buttons can access curSection,
     * minInput, and maxInput of this MainPanel.
     */
    private final class InputPanel extends NtpPanel {
        private final JTextField textField = new JTextField();
        
        private InputPanel() {
            setToBoxLayoutWithPageAxis();
            setMaximumSize(new Dimension(300, 200));
            
            textField.setMaximumSize(new Dimension(250, 30));
            textField.setFont(GARAMOND_25);
            textField.setHorizontalAlignment(JTextField.CENTER);
            
            JButton randomBtn = createButton("Randomize", GARAMOND_25);
            centerComponent(randomBtn);
            randomBtn.addActionListener(e -> setTextFieldText(curSection.getRandomInput()));
            
            var incrementAndDecrementBtnSize = new Dimension(60, 50);
            
            JButton incrementBtn = createButton("+", GARAMOND_25);
            incrementBtn.setMinimumSize(incrementAndDecrementBtnSize);
            incrementBtn.setMaximumSize(incrementAndDecrementBtnSize);
            
            incrementBtn.addActionListener(e -> {
                try {
                    var inputLong = getInputAsLong();
                    inputLong =
                        inputLong < minInput || inputLong >= maxInput
                        ? minInput
                        : inputLong + (curSection.needsEvenInput() && isEven(inputLong) ? 2 : 1);
                    setTextFieldText(inputLong);
                } catch (NumberFormatException ex) {
                    if (hasBlankInput()) {
                        setTextFieldText(minInput);
                    }
                }
            });
            
            JButton decrementBtn = createButton("−", GARAMOND_25);
            decrementBtn.setMinimumSize(incrementAndDecrementBtnSize);
            decrementBtn.setMaximumSize(incrementAndDecrementBtnSize);
            decrementBtn.addActionListener(e -> {
                try {
                    var inputLong = getInputAsLong();
                    inputLong =
                        inputLong <= minInput || inputLong > maxInput
                        ? maxInput
                        : inputLong - (curSection.needsEvenInput() && isEven(inputLong) ? 2 : 1);
                    setTextFieldText(inputLong);
                } catch (NumberFormatException ex) {
                    if (hasBlankInput()) {
                        setTextFieldText(maxInput);
                    }
                }
            });
            
            NtpPanel btnsPanel =
                new NtpPanel()
                .setToBoxLayoutWithLineAxis()
                .center()
                .addAll(incrementBtn, createGap(5), decrementBtn);
            
            addAll(textField, createGap(5), randomBtn, createGap(5), btnsPanel);
        }
        
        private void setTextFieldText(long l) {
            textField.setText(Long.toString(l));
        }
        
        private void clearTextField() {
            textField.setText("");
        }
        
        /**
         * If parsing is unsuccessful, a NumberFormatException gets thrown.
         */
        private long getInputAsLong() {
            return stripCommasAndParse(textField.getText().trim());
        }
        
        /**
         * Returns true if the text field is empty or only has whitespace, false otherwise.
         */
        private boolean hasBlankInput() {
            return textField.getText().isBlank();
        }
    }
}
