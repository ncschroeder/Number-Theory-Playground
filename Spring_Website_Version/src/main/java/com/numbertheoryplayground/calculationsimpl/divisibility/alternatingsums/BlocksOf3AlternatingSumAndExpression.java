package com.numbertheoryplayground.calculationsimpl.divisibility.alternatingsums;

/**
 * For the input number 5,544, expression would be "544 - 5" and sum would be 539.
 */
public final class BlocksOf3AlternatingSumAndExpression extends AlternatingSumAndExpression {
    private BlocksOf3AlternatingSumAndExpression(String inputString) {
        sum = 0;
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
        
        expression = expressionBuilder.toString();
    }
    
    public static BlocksOf3AlternatingSumAndExpression createIfLongEnough(String inputString) {
        return
            inputString.length() >= 4
            ? new BlocksOf3AlternatingSumAndExpression(inputString)
            : null;
    }
}