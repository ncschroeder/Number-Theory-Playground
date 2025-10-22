package com.numbertheoryplayground.calculationsimpl.divisibility.alternatingsums;

/**
 * For the input number 5,544, expression would be "5 - 5 + 4 - 4" and sum would be 0.
 */
public final class DigitsAlternatingSumAndExpression extends AlternatingSumAndExpression {
    public DigitsAlternatingSumAndExpression(String inputString) {
        sum = 0;
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
        
        expression = expressionBuilder.toString();
    }
}