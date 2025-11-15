package com.numbertheoryplayground.calculationsimpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.numbertheoryplayground.InputValidation.*;

public final class AncientMultiplicationAnswer {
    private static final long MIN_INPUT = 2;
    private static final long MAX_INPUT = ONE_QUADRILLION;
    
    /**
     * This record has data that'll be in rows of tables shown on the web page.
     *
     * Strings are used for numbers in this record since a number in a
     * correspondingMultipleString might be too big for a safe JavaScript integer. A number
     * in a powerOf2String will always be small enough for a safe JavaScript integer but a
     * string is still used for consistency.
     */
    public record TableRow(String powerOf2String, String correspondingMultipleString) {}
    
    /**
     * Contains rows for all the powers of 2 ≤ input1 and the corresponding multiples of input2.
     */
    private final List<TableRow> table1Rows;
    
    /**
     * Contains rows for all the powers of 2 that sum to input1 and the corresponding multiples
     * of input2, which sum to the product of input1 and input2.
     */
    private final List<TableRow> table2Rows;
    
    /**
     * A string of the product of the 2 input longs. Just like with correspondingMultipleString,
     * a string is used since this product might be too big for a safe JavaScript integer.
     */
    private final String productString;
    
    public AncientMultiplicationAnswer(long input1, long input2) {
        assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
        assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
        
        /*
        Iterate backwards through the binary string of input1 to find the powers of 2 that are
        ≤ input1 and the powers of 2 that sum to input1.
         */
        var input1BinaryString = Long.toBinaryString(input1);
        table1Rows = new ArrayList<>(input1BinaryString.length());
        table2Rows = new ArrayList<>(input1BinaryString.length());
        var powerOf2 = 1L;
        var input2BigInt = BigInteger.valueOf(input2);
        
        for (int i = input1BinaryString.length() - 1; i >= 0; i--) {
            var correspondingMultipleString =
                input2BigInt
                .multiply(BigInteger.valueOf(powerOf2))
                .toString();
            var row = new TableRow(Long.toString(powerOf2), correspondingMultipleString);
            table1Rows.add(row);
            
            if (input1BinaryString.charAt(i) == '1') {
                // powerOf2 is one of the powers of 2 that sum to input1.
                table2Rows.add(row);
            }

            powerOf2 *= 2;
        }
        
        productString =
            BigInteger.valueOf(input1)
            .multiply(BigInteger.valueOf(input2))
            .toString();
    }
    
    public List<TableRow> getTable1Rows() {
        return table1Rows;
    }
    
    public List<TableRow> getTable2Rows() {
        return table2Rows;
    }
    
    public String getProductString() {
        return productString;
    }
}