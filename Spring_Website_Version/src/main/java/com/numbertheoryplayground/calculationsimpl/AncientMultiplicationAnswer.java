package com.numbertheoryplayground.calculationsimpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AncientMultiplicationAnswer {
    public record TableRow(long powerOf2, String correspondingMultipleString) {}
    
    public final List<TableRow> table1Rows;
    public final List<TableRow> table2Rows;
    public final String productString;
    
    public AncientMultiplicationAnswer(long input1, long input2) {
        /*
        Iterate backwards through the binary string of input1 to find the powers of 2 that are
        <= input1 and the powers of 2 that sum to input1.
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
}
