package com.numbertheoryplayground.calculationsimpl;

import java.util.ArrayList;
import java.util.List;

import static com.numbertheoryplayground.InputValidation.*;

public final class AncientMultiplicationAnswer {
    private static final int MIN_INPUT = 2;
    private static final int MAX_INPUT = ONE_BILLION;
    
    /**
     * This record has data that'll be in rows of tables shown on the webpage. Strings are
     * used for the numbers in this record since a corresponding multiple might be too big
     * for a safe JavaScript integer. A power of 2 will always be small enough for a safe
     * JavaScript integer but a string is still used for consistency.
     */
    public record TableRow(String powerOf2, String correspondingMultiple) {}
    
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
     * A string of the product of the 2 input ints. Just like with correspondingMultiple,
     * a string is used since this product might be too big for a safe JavaScript integer.
     */
    private final String product;
    
    public AncientMultiplicationAnswer(int input1, int input2) {
        assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
        assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
        
        /*
        Iterate backwards through the binary string of input1 to find the powers of 2 that are
        ≤ input1 and the powers of 2 that sum to input1.
         */
        var input1BinaryString = Integer.toBinaryString(input1);
        table1Rows = new ArrayList<>(input1BinaryString.length());
        table2Rows = new ArrayList<>(input1BinaryString.length());
        var powerOf2 = 1;
        
        /*
        The max possible corresponding multiple is input1 × input2. The max input is 1 billion
        and 1 billion × 1 billion = 1 quintillion, which is smaller than the max value for a
        long, which is 9 quintillion something. Therefore, longs can be used for calculating
        corresponding multiples and the product.
         */
        
        for (int i = input1BinaryString.length() - 1; i >= 0; i--) {
            var correspondingMultiple = Long.toString((long) input2 * powerOf2);
            var row = new TableRow(Long.toString(powerOf2), correspondingMultiple);
            table1Rows.add(row);
            if (input1BinaryString.charAt(i) == '1') {
                // powerOf2 is one of the powers of 2 that sum to input1.
                table2Rows.add(row);
            }
            powerOf2 *= 2;
        }
        
        product = Long.toString((long) input1 * input2);
    }
    
    public List<TableRow> getTable1Rows() {
        return table1Rows;
    }
    
    public List<TableRow> getTable2Rows() {
        return table2Rows;
    }
    
    public String getProduct() {
        return product;
    }
}