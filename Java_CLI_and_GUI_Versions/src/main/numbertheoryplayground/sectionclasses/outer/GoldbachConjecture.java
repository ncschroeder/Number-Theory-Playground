package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import numbertheoryplayground.Misc;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpGui;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.sectionclasses.outer.Divisibility.isOdd;
import static numbertheoryplayground.sectionclasses.outer.PrimeNumbers.bothArePrime;

/**
 * Utility class related to the Goldbach Conjecture and the section for it.
 */
public class GoldbachConjecture {
    private static final String INFO = """
The Goldbach Conjecture says that every even number ≥ 4 can be expressed as the sum of 2 prime numbers.
This was named after 1700s Prussian mathematician Christian Goldbach. A conjecture is a statement that is
believed to be true but has not been proven to be true. The Goldbach Conjecture has been verified to be
true for all even numbers ≥ 4 & ≤ 4 × 10^18."""
        .transform(Misc::replaceNewLineCharsWithSpaces);
    
    private static final long MIN_INPUT = 4;
    private static final long MAX_INPUT = 1_500_000;
    
    /**
     * Find the pairs of primes that sum to the input and returns an array that contains the lower numbers
     * of each pair.
     */
    static int[] getPrimePairStarts(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        if (isOdd(input)) {
            throw InvalidInputNumberException.instance;
        }
        
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
    
        return String.format(
        );
    }
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Goldbach Conjecture",
                List.of(INFO),
                MIN_INPUT,
                MAX_INPUT,
                "pairs of prime numbers that sum to that integer",
                "the Goldbach Conjecture"
            );
        }
        
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            int[] pairStarts = getPrimePairStarts(inputLong);
            return NtpCli.buildStringWithStreamElementsOnLongLines(
                getPrimePairStrings(pairStarts, inputLong)
            );
        }
        
        @Override
        public List<Component> getGuiComponents(long inputLong, String inputString) {
            int[] pairStarts = getPrimePairStarts(inputLong);
            
            try {
                var pairsTextArea =
                    NtpTextArea.createWideOneWithStreamElements(getPrimePairStrings(pairStarts, inputLong));
                return List.of(
                    NtpGui.createListHeadingLabel(getPairsHeading(pairStarts.length, inputString)),
                    pairsTextArea
                );
            } catch (NtpTextArea.StringTooLongException ex) {
                String textToDisplay =
                    getNumPairsSentence(pairStarts.length, inputString) + ' ' +
                    NtpTextArea.StringTooLongException.ERROR_MESSAGE;
                return List.of(new NtpTextArea(textToDisplay));
            }
        }
        
        @Override
        public long getRandomInput() {
            long randomInput;
            do {
                randomInput = super.getRandomInput();
            } while (isOdd(randomInput));
            return randomInput;
        }
    }
}
