package com.numbertheoryplayground.calculationsimpl.divisibility.alternatingsums;

public abstract class AlternatingSumAndExpression  {
    /**
     * This is a special minus sign in the Mathematical Operators Unicode block and has a
     * Unicode value of U+2212. Depending on the font, it might look like a hyphen.
     */
    protected static final char MINUS_SIGN = '−';
    
    protected int sum;
    
    protected String expression;
    
    public int getSum() {
        return sum;
    }
    
    public String getExpression() {
        return expression;
    }
}
