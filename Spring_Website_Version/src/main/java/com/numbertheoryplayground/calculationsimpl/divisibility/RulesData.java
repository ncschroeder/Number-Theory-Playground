package com.numbertheoryplayground.calculationsimpl.divisibility;

import com.numbertheoryplayground.calculationsimpl.divisibility.alternatingsums.*;
import static com.numbertheoryplayground.InputValidation.assertIsInRange;

/**
 * Contains data that'll be used by the web page to determine what factors an input number has
 * by using divisibility rules.
 */
public final class RulesData {
    private final int last2Digits;
    
    private final int last3Digits;
    
    private final int sumOfDigits;
    
    private final AlternatingSumAndExpression blocksAltSumAndExpression;
    
    private final AlternatingSumAndExpression digitsAltSumAndExpression;
    
    RulesData(int input) {
        assertIsInRange(input, DivisibilityAnswer.MIN_INPUT, DivisibilityAnswer.MAX_INPUT);
        
        last2Digits = input % 100;
        last3Digits = input % 1_000;
        var inputString = Integer.toString(input);
        
        sumOfDigits =
            inputString
            .chars()
            .map(Character::getNumericValue)
            .sum();
        
        blocksAltSumAndExpression =
            BlocksAlternatingSumAndExpression.createIfLongEnough(inputString);
        digitsAltSumAndExpression = new DigitsAlternatingSumAndExpression(inputString);
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
    
    public AlternatingSumAndExpression getBlocksAltSumAndExpression() {
        return blocksAltSumAndExpression;
    }
    
    public AlternatingSumAndExpression getDigitsAltSumAndExpression() {
        return digitsAltSumAndExpression;
    }
}