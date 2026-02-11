package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.List;
import java.util.stream.LongStream;
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
    
    /**
     * Returns a stream of possible factors of the input ≤ the floor of the square root of
     * the input. These consist of 2 and odd numbers ≥ 3.
     *
     * The method is used by the isPrime and Semiprimes.checkIfSemiprime methods to find the
     * first int factor of an input int and that factor must be > 1 and < that input int. In
     * comments in those methods, I mention that we only need to check primes ≤ the square root
     * of an input int. 2 is the first prime and all other primes are odd numbers ≥ 3, so the
     * ints in the stream returned by this method are a superset of primes ≤ the square root of
     * the input.
     *
     * This method will always be called with an input arg that's ≥ 0, so the call to Math.sqrt
     * below will never return NaN.
     */
    static LongStream getPossibleFactors(long input) {
        var maxPossibleFactor = (long) Math.sqrt(input);
        return getStreamOf2AndOddNums(maxPossibleFactor);
    }
    
    static boolean isPrime(long input) {
        /*
        All ints ≤ 1 aren't prime. To determine if an int > 1 is prime, we only need to check
        if it's divisible by any primes ≤ the floor of the square root of that int. If it is,
        then that int isn't prime.
         */
        return
            input > 1 &&
            getPossibleFactors(input).noneMatch(l -> isDivisible(input, l));
    }
    
    static boolean bothArePrime(long a, long b) {
        return isPrime(a) && isPrime(b);
    }
    
    /**
     * Returns a stream of the first 30 primes ≥ the input.
     */
    static LongStream getPrimes(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        // Create a stream of odd primes since all primes besides 2 are odd.
        long iterationStart = isOdd(input) ? input : input + 1;
        LongStream oddPrimes =
            LongStream.iterate(iterationStart, l -> l + 2)
            .filter(PrimeNumbers::isPrime);
        
        return
            (input <= 2 ? LongStream.concat(LongStream.of(2), oddPrimes) : oddPrimes)
            .limit(NUM_PRIMES_TO_FIND);
    }
    
    private static Stream<String> getPrimesStrings(long input) {
        return getPrimes(input).mapToObj(Misc::createStringWithCommas);
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
