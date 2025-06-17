package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpGui;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.sectionclasses.outer.Divisibility.isOdd;
import static numbertheoryplayground.sectionclasses.outer.PrimeNumbers.bothArePrime;

/**
 * Utility class related to the Goldbach Conjecture and the section for it.
 *
 * The Goldbach Conjecture section needs even input so that's why getPrimePairStarts throws an
 * exception for odd inputs and the getRandomInput method is overridden.
 */
public class GoldbachConjecture {
    private static final String INFO = """
The Goldbach Conjecture says that every even number ≥ 4 can be expressed as the sum of 2 prime
numbers. This was named after 1700s Prussian mathematician Christian Goldbach. %s. The Goldbach
Conjecture has been verified to be true for all even numbers ≥ 4 & ≤ 4 × 10^18."""
        .formatted(CONJECTURE_DEFINITION);
    
    private static final long MIN_INPUT = 4;
    
    private static long MAX_INPUT;
    
    public static void setMaxInputForCli() {
        MAX_INPUT = 1_500_000;
    }
    
    public static void setMaxInputForGui() {
        MAX_INPUT = 250_000;
    }
    
    /*
    Why does this section have separate max inputs for the CLI and GUI? Well, this section can
    find the pairs of prime numbers that sum to an input long. After finding them, a string gets
    built that contains them. This string can be pretty long. There doesn't seem to be any
    problem displaying long strings in the CLI, but it can take a long time to do that in the GUI.
    See the documentation comment for NtpTextArea.StringTooLongException for more info.
    
    The max input is set to 250,000 for the GUI since the resulting string from that input isn't
    deemed too long but the string for 300,000 is. There are still some inputs below 250,000 that
    result in a string that's deemed too long. 228,342 is one of them and it has 2,498 pairs of
    prime numbers that sum to it.
     */
    
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
        Check if the input is 4 since 4 is the only even number ≥ 4 that has 2 in a pair of
        primes that sum to it. If the input isn't 4, then check pairs of odd numbers that sum to
        the input for primality. The iterating only needs to go up to the floor of half of the
        input. After that point, checks for primality will be done on pairs of numbers that have
        already been checked for primality.
         */
        
        if (input == 4) {
            return new int[] { 2 };
        }
        
        var maxPossiblePairStart = (int) input / 2;
        return
            IntStream.iterate(3, i -> i <= maxPossiblePairStart, i -> i + 2)
            .filter(i -> bothArePrime(i, input - i))
            .toArray();
    }
    
    /**
     * Returns a stream of the string representations of the pairs of primes that sum to the input.
     * pairStarts should be an int array returned from calling getPrimePairStarts using the input.
     */
    private static Stream<String> getPrimePairStrings(int[] pairStarts, long input) {
        return Arrays.stream(pairStarts).mapToObj(i -> longPairToString(i, input - i));
    }
    
    private static String getNumPairsSentence(int numPairs, String inputString) {
        String textAfterThere =
            numPairs == 1
            ? "'s 1 pair"
            : String.format(" are %s pairs", createStringWithCommas(numPairs));
        
        return String.format(
            "There%s of prime numbers that sum to %s.",
            textAfterThere, inputString
        );
    }
    
    private static String getPairsHeading(int numPairs, String inputString) {
        return
            getNumPairsSentence(numPairs, inputString) + ' ' +
            (numPairs == 1 ? "It is" : "They are") + ':';
    }
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Goldbach Conjecture",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                "the pairs of prime numbers that sum to that number",
                "the Goldbach Conjecture"
            );
        }
        
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            int[] pairStarts = getPrimePairStarts(inputLong);
            return NtpCli.buildStringWithStreamElementsOnLongLines(
                getPairsHeading(pairStarts.length, inputString),
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
