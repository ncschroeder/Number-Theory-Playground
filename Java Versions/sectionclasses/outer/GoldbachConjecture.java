package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import numbertheoryplayground.NTPCLI;
import numbertheoryplayground.gui.NTPGUI;
import numbertheoryplayground.gui.NTPTextArea;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.sectionclasses.outer.Divisibility.*;
import static numbertheoryplayground.sectionclasses.outer.PrimeNumbers.bothArePrime;

/**
 * Utility class related to the Goldbach Conjecture and the section for it.
 */
public class GoldbachConjecture {
    private static final String INFO = """
The Goldbach Conjecture says that every even number >= 4 can be expressed as the sum of 2 prime numbers.
This was named after 1700s Prussian mathematician Christian Goldbach. A conjecture is a statement that is
believed to be true but has not been proven to be true. The Goldbach Conjecture has been verified to be
true for all even numbers >= 4 && <= 4 x 10^18.""";
    
    private static final long MIN_INPUT = 4;
    private static final long MAX_INPUT = 1_500_000;

    /**
     * Find the pairs of primes that sum to the input and returns an array that contains the lower numbers
     * of each pair.
     */
    public static int[] getPrimePairStarts(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        if (isOdd(input)) throw invalidInputException;
    
        /*
        Check if the input is 4 since 4 is the only even number >= 4 that has 2 in a pair of primes that
        sum to it. If the input isn't 4, check pairs of odd ints that sum to the input for primality.
        The iterating only needs to go up to the floor of half of the input. After that point, checks
        for primality will be done on pairs of ints that have already been checked for primality.
         */

        if (input == 4) {
            return new int[] { 2 };
        }
        
        var maxI = (int) input / 2;
        return
            IntStream.iterate(3, i -> i <= maxI, i -> i + 2)
            .filter(i -> bothArePrime(i, input - i))
            .toArray();
    }
    
    /**
     * Returns a stream of the string representations of the pairs of primes that sum to the input.
     */
    private static Stream<String> getPrimePairStrings(int[] pairStarts, long input) {
        return Arrays.stream(pairStarts).mapToObj(i -> longPairToString(i, input - i));
    }
    
    private static String getListHeading(int numPairs, long input) {
        return String.format(
            "There are %s pairs of prime numbers that sum to %s. They are:",
            toStringWithCommas(numPairs),
            toStringWithCommas(input)
        );
    }
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Goldbach Conjecture",
                MIN_INPUT,
                MAX_INPUT,
                "get the pairs of prime numbers that sum to it",
                "the Goldbach Conjecture",
                INFO
            );
        }
        
        @Override
        public String getCliAnswer(long input) {
            int[] pairStarts = getPrimePairStarts(input);
            return NTPCLI.streamToString(
                getListHeading(pairStarts.length, input),
                getPrimePairStrings(pairStarts, input)
            );
        }
    
        @Override
        public List<Component> getGuiComponents(long input) {
            int[] pairStarts = getPrimePairStarts(input);
            return NTPGUI.createStreamHeadingAndTextArea(
                getListHeading(pairStarts.length, input),
                getPrimePairStrings(pairStarts, input)
            );
        }
    
        @Override
        public long getRandomInput() {
            long randomInput = super.getRandomInput();
            return isEven(randomInput) ? randomInput : randomInput + 1;
        }
    }
}