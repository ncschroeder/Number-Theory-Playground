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
The Two Square Theorem says that every prime number that is 1 above a multiple of 4 can be expressed as the sum
of 2 square numbers. A square number is a number that can be formed by taking a number and multiplying it by
itself, or squaring it. The first few square numbers are 1 (1^2), 4 (2^2), and 9 (3^2). 29 is prime and is
1 above 28 (4 x 7) and can be expressed as 2^2 (4) + 5^2 (25).""";
    
    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = ONE_QUADRILLION;
    
    static final class Answer {
        /**
         * The first prime number >= the input that is 1 above a multiple of 4.
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
                    "The first integer >= %s that is prime and is 1 above a multiple of 4 is %s, which is %s + %s.",
                    inputString,
                    createStringWithCommas(primeNum),
                    createLongAndSquareString(a),
                    createLongAndSquareString(b)
                );

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
                MIN_INPUT,
                MAX_INPUT,
                "first prime number that's ≥ that integer and is 1 above a multiple of " +
                    "4, as well as the integers whose squares sum to that prime number",
                "the Two Square Theorem",
                INFO
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
