package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpGui;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.sectionclasses.outer.PrimeNumbers.bothArePrime;

/**
 * Utility class related to twin prime pairs and the section for it.
 */
public class TwinPrimePairs {
    static final String CONJECTURE_DEFINITION =
        "A conjecture is a statement that's believed to be true but hasn't been proven to be";
    
    private static final String INFO = """
A twin prime pair is a pair of prime numbers that differ by 2. The first 5 twin prime pairs are
3 & 5, 5 & 7, 11 & 13, 17 & 19, and 29 & 31. The largest known twin prime pair is
(2,996,863,034,895 × 2^1,290,000) ± 1. They have 388,342 digits! The twin prime conjecture says
that there are an infinite amount of twin prime pairs. %s.

All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so this means
that all twin prime pairs besides 3 and 5 consist of 1 number that's 1 below a multiple of 6
and another number that's 1 above that same multiple of 6. 5 is the only number to be in 2
twin prime pairs, the first 2 mentioned above."""
        .formatted(CONJECTURE_DEFINITION);
    
    /*
    The calculation for this section is: find the first 20 twin prime pairs where the lowest
    number in the pair is ≥ an input number. For example, if the input number is 3, then the
    pair 3 and 5 will be the first one found since the lowest number in that pair is 3. If the
    input number is 4, then the pair 5 and 7 will be the first one found.
     */
    
    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = FIVE_HUNDRED_BILLION;
    private static final int NUM_PAIRS_TO_FIND = 20;
    
    /**
     * Returns a stream that can find the first 20 twin prime pairs where the lower of the 2 numbers in
     * the pair is ≥ the input. For example, if the input is 3, then the pair 3 and 5 will be the first
     * one found since the lower number in that pair is 3. If the input is 4, then the pair 5 and 7 will
     * be the first one found. The elements of this stream are the lower numbers in each pair.
     */
    static LongStream getPairStarts(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        /*
        As mentioned in the INFO string, all twin prime pairs besides 3 and 5 consist of 1 number
        that's 1 below a multiple of 6 and another number that's 1 above that same multiple of 6.
        Set iterationStart to the first long ≥ the input that's 1 below a multiple of 6 so that
        we'll be able to iterate through longs that are 1 below a multiple of 6.
         */
        long iterationStart = input;
        while (iterationStart % 6 != 5) iterationStart++;
        LongStream pairStarts =
            LongStream.iterate(iterationStart, l -> l + 6)
            .filter(l -> bothArePrime(l, l + 2));
        
        return
            (input <= 3 ? LongStream.concat(LongStream.of(3), pairStarts) : pairStarts)
            .limit(NUM_PAIRS_TO_FIND);
    }
    
    /**
     * Returns a stream of the string representations of the first 20 twin prime pairs where the
     * lower of the 2 numbers in the pair is ≥ the input.
     */
    private static Stream<String> getPairStrings(long input) {
        return
            getPairStarts(input)
            .mapToObj(l -> longPairToString(l, l + 2));
    }
    
    private static String getPairsHeading(String inputString) {
        return String.format(
            "The first %d twin prime pairs ≥ %s are:",
            NUM_PAIRS_TO_FIND, inputString
        );
    }
    
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Twin Prime Pairs",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                String.format("the first %d twin prime pairs ≥ that number", NUM_PAIRS_TO_FIND),
                "twin prime pairs"
            );
        }
        
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            Stream<String> pairStrings = getPairStrings(inputLong);
            return NtpCli.buildStringWithStreamElementsOnShortLines(
                getPairsHeading(inputString),
                pairStrings
            );
        }
        
        @Override
        public List<Component> getGuiComponents(long input, String inputString) {
            var textArea = NtpTextArea.createNarrowOneWithStreamElements(getPairStrings(input));
            return List.of(
                NtpGui.createListHeadingLabel(getPairsHeading(inputString)),
                textArea
            );
        }
    }
}
