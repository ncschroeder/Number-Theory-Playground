package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.List;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.sectionclasses.outer.PrimeNumbers.isPrime;

/**
 * Utility class related to the two square theorem and the section for it.
 */
public class TwoSquareTheorem {
    private static final String INFO = """
Fermat's two square theorem says that every prime number that's 1 above a multiple of 4 can be
expressed as the sum of 2 squares in 1 way. This was named after 1600s French mathematician
Pierre de Fermat. In the context of this theorem, square is a shortening of square number or
perfect square and is a number that can be formed by taking an integer and multiplying it by
itself, or squaring it. The first 4 squares are 0 (0^2), 1 (1^2 or (-1)^2), 4 (2^2 or (-2)^2),
and 9 (3^2 or (-3)^2). Because of this theorem, a prime number that's 1 above a multiple of 4
is known as a Pythagorean prime. An example of a Pythagorean prime is 29 and it can be expressed
as 2^2 (4) + 5^2 (25).""";
    
    /*
    The calculations for this section are: find the first Pythagorean prime ≥ an input number,
    as well as the whole numbers whose squares sum to that prime.
     */

    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = ONE_QUADRILLION;
    
    static final class Answer {
        /**
         * The first Pythagorean prime ≥ the input.
         */
        private long pythagPrime;
        
        /**
         * a and b are the longs whose squares sum to pythagPrime.
         */
        private long a;
        
        private long b;
        
        private final String infoSentence;
        
        Answer(long inputLong, String inputString) {
            assertIsInRange(inputLong, MIN_INPUT, MAX_INPUT);
            
            pythagPrime = inputLong;
            while (pythagPrime % 4 != 1) pythagPrime++;
            while (!isPrime(pythagPrime)) pythagPrime += 4;
            
            b = 0;
            for (a = 1; a < pythagPrime; a++) {
                long aSquared = a * a;
                long bSquared = pythagPrime - aSquared;
                var bDouble = Math.sqrt(bSquared);
                var bLong = (long) bDouble;
                if (bDouble == bLong) {
                    b = bLong;
                    break;
                }
            }
            
            infoSentence =
                String.format(
                    "The first Pythagorean prime ≥ %s is %s, which is %s + %s.",
                    inputString,
                    createStringWithCommas(pythagPrime),
                    createLongAndSquareString(a),
                    createLongAndSquareString(b)
                );
        }

        long getPythagPrime() {
            return pythagPrime;
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
                "the first Pythagorean prime ≥ that number, as well as the " +
                    "whole numbers whose squares sum to that prime",
                "the two square theorem"
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