package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.util.List;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;
import static com.nicholasschroeder.numbertheoryplayground.Primes.isPrime;

/**
 * Utility class related to the Two Square Theorem and the section for it.
 */
public class TwoSquareTheorem {
    private static final String theoremInfo =
        "The Two Square Theorem says that every prime number that is 1 above a multiple " +
        "of 4 can be expressed as the sum of 2 squares.";
    
    private static final int minInputInt = 0;
    private static final int maxInputInt = oneBillion;
    
    public static class Info {
        /**
         * This is set to the first prime number >= an input int that is 1 above a multiple of 4.
         */
        private int primeNumber;
        
        /**
         * int1 and int2 are set to the ints whose squares sum to primeNumber.
         */
        private int int1;
        private final int int2;

        public Info(int input) {
            assertIsInRange(input, minInputInt, maxInputInt);

            primeNumber = input;
            while (primeNumber % 4 != 1) {
                primeNumber++;
            }
            while (!isPrime(primeNumber)) {
                primeNumber += 4;
            }

            for (int1 = 1; int1 < primeNumber; int1++) {
                int int1Square = int1 * int1;
                int number2Square = primeNumber - int1Square;
                double number2Double = Math.sqrt(number2Square);
                int number2Int = (int) number2Double;
                if (number2Int == number2Double) {
                    this.int2 = number2Int;
                    return;
                }
            }
            
            // This part shouldn't be reached.
            int2 = 0;
            printError("Numbers not found for Two Square Theorem algorithm with an input of " + input);
        }
    
        public int getPrimeNumber() {
            return primeNumber;
        }
    
        public int getInt1() {
            return int1;
        }
    
        public int getInt2() {
            return int2;
        }
    }
    
    private static String getInfoString(int input) {
        var info = new Info(input);
        return String.format(
            "The first integer >= %s that is prime and is 1 above a multiple of 4 is %s, which is equal to %s + %s.",
            stringifyWithCommas(input),
            stringifyWithCommas(info.primeNumber),
            getLongAndSquareString(info.int1),
            getLongAndSquareString(info.int2)
        );
    }
    
    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Two Square Theorem",
                List.of(theoremInfo),
                minInputInt,
                maxInputInt,
                "get the first prime number that is >= that integer and is 1 above a multiple " +
                    "of 4, as well as the squares that sum to that prime number",
                "the Two Square Theorem"
            );
        }
        
        @Override
        public String getCliAnswer(int input) {
            return NTPCLI.insertNewLines(getInfoString(input));
        }
    
        @Override
        public List<Component> getGuiComponents(int input) {
            return List.of(new NTPTextArea(getInfoString(input)));
        }
    }
}
