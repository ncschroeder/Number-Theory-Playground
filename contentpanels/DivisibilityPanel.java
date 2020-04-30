package contentpanels;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

public class DivisibilityPanel extends JPanel {

    JLabel questionLabel;
    JTextField inputField;
    JButton calculateButton;
    JTextArea answerArea;

    public DivisibilityPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints GBC = new GridBagConstraints();

        questionLabel = new JLabel("Enter a number to find out what it is divisible by");
        inputField = new JTextField(8);
        calculateButton = new JButton("Calculate");
        answerArea = new JTextArea("\t\t\t\t\n\t\t\t\t\n\t\t\t\t\n");

        GBC.gridx = 0;
        GBC.gridy = 0;
        GBC.gridwidth = 3;
        add(questionLabel, GBC);

        GBC.gridy = 1;
        GBC.gridwidth = 1;
        add(inputField, GBC);

        GBC.gridx = 1;
        add(calculateButton, GBC);

        GBC.gridy = 2;
        GBC.gridx = 0;
        GBC.gridwidth = 3;
        add(answerArea, GBC);

        int numberInt;
        String numberString;
        calculateButton.addActionListener(e -> {
            try {
//                numberString = inputField.getText();
//                numberInt = Integer.parseInt(numberString);
//                answerArea.setText(getDivisibilityInfo(numberString, numberInt));
            } catch (Exception ex) {
                answerArea.setText("Must enter a number");
            }
        });
    }

    String getDivisibilityInfo(String numberString, int numberInt) {
//        Some parts of this method will need the string version of the number and some need the int version

        boolean isEven = (numberInt % 2 == 0);
        int sumOfDigits = sumOfDigits(numberString);
        int last2Digits, last3Digits;

        StringBuilder output = new StringBuilder();
        if (!isEven)
            output.append(numberString).append(" is not even so it cannot be divisible by any even numbers\n");

        output.append("The sum of the digits is ").append(sumOfDigits).append("\n");
        if (sumOfDigits % 3 == 0) {
            output.append("This is divisible by 3 so ").append(numberString).append(" is divisible by 3\n");
            if (sumOfDigits % 9 == 0)
                output.append("This is divisible by 9 so ").append(numberString).append(" is divisible by 9\n");
            else
                output.append("This is not divisible by 9 so ").append(numberString).append(" is not divisible by 9\n");
        } else {
            output.append("This is not divisible by 3 so ").append(numberString).append(" is not divisible by 3\n");
            output.append("Since ").append(numberString).append(" is not divisible by 3, it cannot be")
                    .append(" divisible by 6, 9, nor 12 either\n");
        }

        if (isEven) {
            output.append(divisibleBy6(numberInt));

            last2Digits = lastNDigits(numberString, 2, numberInt);
            output.append("The last 2 digits are ").append(last2Digits).append("\n");
            output.append(divisibleBy4(numberInt, last2Digits));

            last3Digits = lastNDigits(numberString, 3, numberInt);
            output.append("The last 3 digits are ").append(last3Digits).append("\n");
            output.append(divisibleBy8(numberInt, last3Digits));

            output.append(divisibleBy12(numberInt));
        }

        return output.toString();
    }

    int sumOfDigits(String numberString) {
        int sum = 0;
        for (int i = 0; i < numberString.length(); i++)
            sum += Character.getNumericValue(numberString.charAt(i));
        return sum;
    }

    int lastNDigits(String numberString, int n, int origNumber) {
        String lastNDigits;
        try {
            lastNDigits = numberString.substring(numberString.length() - n);
        } catch (Exception e) {
            return origNumber;
        }
        return Integer.parseInt(lastNDigits);
    }

    int removeLastDigit(int number) {
        String numberString = String.valueOf(number);
        String newNumber = numberString.substring(0, numberString.length() - 1);
        return Integer.parseInt(newNumber);
    }

    String divisibleBy3(int number, int sumOfDigits) {
        if (sumOfDigits % 3 == 0)
            return "This is divisible by 3 so " + number + " is divisible by 3\n";
        else
            return "This is not divisible by 3 so " + number + " is not divisible by 3\n";
    }

    String divisibleBy9(int number, int sumOfDigits) {
        if (sumOfDigits % 9 == 0)
            return "This is divisible by 9 so " + number + " is divisible by 9\n";
        else
            return "This is not divisible by 9 so " + number + " is not divisible by 9\n";
    }

    String divisibleBy6(int number) {
        if (number % 2 == 0) {
            if (number % 3 == 0)
                return number + " is even and divisible by 3 so it's divisible by 6\n";
            else
                return number + " is not divisible by 3 so it cannot be divisible by 6\n";
        } else
            return number + " is not even so it cannot be divisible by 6\n";
    }

    String divisibleBy4(int number, int last2Digits) {
        if (last2Digits % 4 == 0)
            return "This is divisible by 4 so " + number + " is divisible by 4\n";
        else
            return "This is not divisible by 4 so " + number + " is not divisible by 4\n";
    }

//    static void divisibleBy7(String numberString) {
//        int lastDigit = lastNDigits(numberString, 1);
//        int numberWithoutLastDigit = removeLastDigit(number);
//
//    }

    String divisibleBy8(int number, int last3Digits) {
        if (last3Digits % 8 == 0)
            return "This is divisible by 8 so " + number + " is divisible by 8\n";
        else
            return "This is not divisible by 8 so " + number + " is not divisible by 8\n";
    }

    String divisibleBy12(int number) {
        if (number % 3 == 0) {
            if (number % 4 == 0)
                return number + " is divisible by both 3 and 4 so it's divisible by 12\n";
            else
                return number + " is not divisible by 4 so it cannot be divisible by 12\n";
        } else
            return number + " is not divisible by 3 so it cannot be divisible by 12\n";
    }
}
