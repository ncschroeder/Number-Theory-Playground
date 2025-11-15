package com.numbertheoryplayground.calculationsimpl.divisibility;

import static com.numbertheoryplayground.InputValidation.assertIsInRange;

/**
 * Contains data that'll be used by the webpage to determine what factors an input number has
 * by using divisibility rules.
 */
public final class RulesData {
    private final String inputString;
    
    private final int last2Digits;
    
    private final int last3Digits;
    
    private final int sumOfDigits;
    
    RulesData(int input) {
        assertIsInRange(input, DivisibilityAnswer.MIN_INPUT, DivisibilityAnswer.MAX_INPUT);
        
        inputString = Integer.toString(input);
        last2Digits = input % 100;
        last3Digits = input % 1_000;
        
        sumOfDigits =
            inputString
            .chars()
            .map(Character::getNumericValue)
            .sum();
    }
    
    public int getLast2Digits() {
        return last2Digits;
    }
    
    public int getLast3Digits() {
        return last3Digits;
    }
    
    public int getSumOfDigits() {
        return sumOfDigits;
    }
    
    /**
     * expression is for a mathematical expression.
     */
    public record AlternatingSumAndExpression(int sum, String expression) {}
    
    /**
     * This is a special minus sign in the Mathematical Operators Unicode block and has a
     * Unicode value of U+2212. Depending on the font, it might look like a hyphen.
     */
    private static final char MINUS_SIGN = '−';
    
    /**
     * The input number must have at least 4 digits for this calculation to be useful, so this
     * returns null if it doesn't. Otherwise, this returns an AlternatingSumAndExpression for the
     * alternating sum of 3-digit blocks from right to left. For the input number 5,544, the
     * expression would be "544 - 5" and the sum would be 539.
     */
    public AlternatingSumAndExpression getBlocksAltSumAndExpression() {
        if (inputString.length() <= 3) return null;
        
        var sum = 0;
        var expressionBuilder = new StringBuilder();
        var add = true;
        
        for (var i = inputString.length() - 3; i >= -2; i -= 3) {
            if (!expressionBuilder.isEmpty()) {
                expressionBuilder
                .append(' ')
                .append(add ? '+' : MINUS_SIGN)
                .append(' ');
            }
            
            var blockString = inputString.substring(Math.max(0, i), i + 3);
            expressionBuilder.append(blockString);
            var blockInt = Integer.parseInt(blockString);
            sum += add ? blockInt : -blockInt;
            add = !add;
        }
        
        return new AlternatingSumAndExpression(sum, expressionBuilder.toString());
    }
    
    /**
     * Returns an AlternatingSumAndExpression for the alternating sum of digits from left to
     * right. For the input number 5,544, the expression would be "5 - 5 + 4 - 4" and the sum
     * would be 0.
     */
    public AlternatingSumAndExpression getDigitsAltSumAndExpression() {
        var sum = 0;
        var expressionBuilder = new StringBuilder();
        var add = true;
        
        for (var i = 0; i < inputString.length(); i++) {
            if (!expressionBuilder.isEmpty()) {
                expressionBuilder
                .append(' ')
                .append(add ? '+' : MINUS_SIGN)
                .append(' ');
            }
            
            var digitChar = inputString.charAt(i);
            expressionBuilder.append(digitChar);
            var digitInt = Character.getNumericValue(digitChar);
            sum += add ? digitInt : -digitInt;
            add = !add;
        }
        
        return new AlternatingSumAndExpression(sum, expressionBuilder.toString());
    }
}