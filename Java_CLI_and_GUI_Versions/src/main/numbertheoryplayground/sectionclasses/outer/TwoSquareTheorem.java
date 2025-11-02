package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.List;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.sectionclasses.outer.PrimeNumbers.isPrime;

/**
 * Utility class related to the Two Square Theorem and the section for it.
 */
public class TwoSquareTheorem {
    private static final String INFO = """
The Two Square Theorem says that every prime number that's 1 above a multiple of 4 can be
expressed as the sum of 2 square numbers. A square number, also known as a perfect square, is
a number that can be formed by taking an integer and multiplying it by itself, or squaring it.
The first 4 square numbers are 0 (0^2), 1 (1^2 or (-1)^2), 4 (2^2 or (-2)^2), and
9 (3^2 or (-3)^2). An example of a number that's prime and is 1 above a multiple of 4 is 29
and it can be expressed as 2^2 (4) + 5^2 (25).""";
    
    /*
    The calculations for this section are: find the first prime number ≥ an input number that's
    1 above a multiple of 4, as well as the whole numbers whose squares sum to that prime number.
     */

    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = ONE_QUADRILLION;
    
    static final class Answer {
        /**
         * The first prime number ≥ the input that's 1 above a multiple of 4.
         */
        private long primeNum;
        
        /**
         * a and b are the longs whose squares sum to primeNum.
         */
        private long a;
        
        private long b;
        
        private final String infoSentence;
        
        Answer(long inputLong, String inputString) {
            assertIsInRange(inputLong, MIN_INPUT, MAX_INPUT);
            
            primeNum = inputLong;
            while (primeNum % 4 != 1) primeNum++;
            while (!isPrime(primeNum)) primeNum += 4;
            
            b = 0;
            for (a = 1; a < primeNum; a++) {
                long aSquared = a * a;
                long bSquared = primeNum - aSquared;
                var bDouble = Math.sqrt(bSquared);
                var bLong = (long) bDouble;
                if (bDouble == bLong) {
                    b = bLong;
                    break;
                }
            }
            
            infoSentence =
                String.format(
                    "The first number ≥ %s that's prime and is 1 above a multiple of 4 is %s, " +
                        "which is %s + %s.",
                    inputString,
                    createStringWithCommas(primeNum),
                    createLongAndSquareString(a),
                    createLongAndSquareString(b)
                );
        }

        long getPrimeNum() {
            return primeNum;
        }
        
        long getA() {
            return a;
        }
        
        long getB() {
            return b;
        }
    }
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Two Square Theorem",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                "the first prime number that's ≥ that number and is 1 above a multiple " +
                    "of 4, as well as the whole numbers whose squares sum to that prime number",
                "the Two Square Theorem"
            );
        }
        
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            String info = new Answer(inputLong, inputString).infoSentence;
            return NtpCli.putNewLineChars(info);
        }
        
        @Override
        public List<Component> getGuiComponents(long input, String inputString) {
            String info = new Answer(input, inputString).infoSentence;
            return List.of(new NtpTextArea(info));
        }
    }
}