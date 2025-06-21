package com.numbertheoryplayground.calculationsimpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.numbertheoryplayground.InputValidation.*;

public final class AncientMultiplicationAnswer {
    private static final long MIN_INPUT = 2;
    private static final long MAX_INPUT = NINE_QUADRILLION;
    
    public record TableRow(String powerOf2String, String correspondingMultipleString) {}
    
    private final List<TableRow> table1Rows;
    private final List<TableRow> table2Rows;
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
            var row = new TableRow(powerOf2, correspondingMultipleString);
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
