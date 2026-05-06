package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import numbertheoryplayground.Misc;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpGui;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.sectionclasses.outer.Divisibility.*;

/**
 * Utility class related to prime numbers and the section for it.
 */
public class PrimeNumbers {
    private static final String INFO = """
A prime number, or a prime, is a whole number > 1 that isn't divisible by any whole numbers
other than 1 and itself. A composite number is a whole number > 1 that is divisible by a whole
number other than 1 and itself. The first 10 primes are 2, 3, 5, 7, 11, 13, 17, 19, 23, and 29.
There are an infinite amount of primes. The largest known prime is (2^136,279,841) − 1. It has
41,024,320 digits! Primes are used in 8 of the 11 sections in the Number Theory Playground.

With the exception of 2 and 3, all primes are either 1 above or 1 below a multiple of 6. To
show why this is the case, let's have a variable n and let it represent a whole number ≥ 6
that's a multiple of 6. We know that n is divisible by 2 and 3 so n + 2 and n + 4 are divisible
by 2 and n + 3 is divisible by 3 but we don't have any guarantees about what n + 1 and n + 5
are divisible by. Therefore, that's where primes can be.

A whole number can be determined to be prime if it's not divisible by any primes ≤ the square
root of that number. This is called trial division. Let's determine if 29 and 33 are prime.
5^2 = 25 and 6^2 = 36 so the square roots of 29 and 33 are between 5 and 6. We check if 29 and
33 are divisible by 2, 3, or 5; which are the primes ≤ 5. 29 isn't divisible by any of those
and 33 is divisible by 3 so 29 is prime and 33 isn't.""";
    
    // The calculation for this section is: find the first 30 primes that are ≥ an input number.
    
    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = TEN_TRILLION;
    private static final int NUM_PRIMES_TO_FIND = 30;
    
    static boolean isPrime(long input) {
        if (input <= 1) return false;
        if (input <= 3) return true;
        if (isEven(input)) return false;
        
        var maxPossibleFactorToCheck = (long) Math.sqrt(input);
        for (var l = 3L; l <= maxPossibleFactorToCheck; l += 2) {
            if (isDivisible(input, l)) return false;
        }
        
        return true;
    }
    
    /**
     * Returns a list of the first 30 primes ≥ the input.
     */
    static List<Long> getPrimes(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        var primes = new ArrayList<Long>(NUM_PRIMES_TO_FIND);
        if (input < 2) primes.add(2L);
        
        long possiblePrime = isOdd(input) ? input : input + 1;
        while (true) {
            if (isPrime(possiblePrime)) {
                primes.add(possiblePrime);
                if (primes.size() == NUM_PRIMES_TO_FIND) {
                    return primes;
                }
            }
            possiblePrime += 2;
        }
    }
    
    private static Stream<String> getPrimesStrings(long input) {
        return getPrimes(input).stream().map(Misc::createStringWithCommas);
    }
    
    private static String getPrimesHeading(String inputString) {
        return String.format(
            "The first %d primes ≥ %s are:",
            NUM_PRIMES_TO_FIND, inputString
        );
    }
    
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Prime Numbers",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                String.format("the first %d primes ≥ that number", NUM_PRIMES_TO_FIND),
                "prime numbers"
            );
        }
        
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            Stream<String> primesStrings = getPrimesStrings(inputLong);
            return NtpCli.buildStringWithStreamElementsOnShortLines(
                getPrimesHeading(inputString),
                primesStrings
            );
        }
        
        @Override
        public List<Component> getGuiComponents(long input, String inputString) {
            var textArea = NtpTextArea.createNarrowOneWithStreamElements(getPrimesStrings(input));
            return List.of(
                NtpGui.createListHeadingLabel(getPrimesHeading(inputString)),
                textArea
            );
        }
    }
}
