package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.List;
import numbertheoryplayground.NTPCLI;
import numbertheoryplayground.gui.NTPTextArea;
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
    
    public static final class Answer {
        /**
         * The first prime number >= the input that is 1 above a multiple of 4.
         */
        public long primeNum;
        
        /**
         * a and b are the longs whose squares sum to primeNum.
         */
        public long a;
        
        public long b;
        
        private final String infoSentence;
        
        public Answer(long input) {
            assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
            primeNum = input;
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
                    toStringWithCommas(input),
                    toStringWithCommas(primeNum),
                    getLongAndSquareString(a),
                    getLongAndSquareString(b)
                );
        }
    }
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Two Square Theorem",
                MIN_INPUT,
                MAX_INPUT,
                "get the first prime number that is >= that integer and is 1 above a multiple " +
                    "of 4, as well as the numbers whose squares sum to that prime number",
                "the Two Square Theorem",
                INFO
            );
        }
        
        @Override
        public String getCliAnswer(long input) {
            return NTPCLI.insertNewLines(new Answer(input).infoSentence);
        }
    
        @Override
        public List<Component> getGuiComponents(long input) {
            return List.of(new NTPTextArea(new Answer(input).infoSentence));
        }
    }
}