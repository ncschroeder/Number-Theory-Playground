package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;
import static com.nicholasschroeder.numbertheoryplayground.PrimeNumbers.bothArePrime;

/**
 * Utility class related to twin prime pairs and the section for it.
 */
public class TwinPrimePairs {
    private static final String INFO = """
A twin prime pair is a pair of prime numbers that differ by 2. The first 5 twin prime pairs are
3 & 5, 5 & 7, 11 & 13, 17 & 19, and 29 & 31. The largest known twin prime pair is
2,996,863,034,895 x 2^1,290,000 + and - 1. They have 388,342 digits! It is conjectured that there are an
infinite amount of twin prime pairs. A conjecture is a statement that is believed to be true but has
not been proven to be.

All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so this means that all
twin prime pairs besides 3 and 5 consist of 1 number that is 1 below a multiple of 6 and another
number that is 1 above that same multiple of 6. 5 is the only number to be in 2 twin prime pairs.""";
    
    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = FIVE_HUNDRED_BILLION;
    private static final int NUM_PAIRS_TO_FIND = 20;
    
    /**
     * Returns a stream that can find the first 20 twin prime pairs where the lower of the 2 numbers in
     * the pair is >= the input. For example, if the input is 3, then the pair 3 and 5 will be the first
     * one found since the lower number in that pair is 3. If the input is 4, then the pair 5 and 7 will
     * be the first one found. The elements of this stream are the lower numbers in each pair.
     */
    public static LongStream getPairStarts(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        /*
        Set iterationStart to the first int >= the input that is 1 below a multiple of 6 so that we'll be
        able to iterate through ints that are 1 below a multiple of 6.
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
     * lower of the 2 numbers in the pair is >= the input.
     */
    private static Stream<String> getPairStrings(long input) {
        return getPairStarts(input).mapToObj(l -> longPairToString(l, l + 2));
    }
    
    private static String getListHeading(long input) {
        return String.format(
            "The first %d twin prime pairs >= %s are:",
            NUM_PAIRS_TO_FIND,
            toStringWithCommas(input)
        );
    }
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Twin Prime Pairs",
                MIN_INPUT,
                MAX_INPUT,
                String.format("get the first %d twin prime pairs >= that integer", NUM_PAIRS_TO_FIND),
                "twin prime pairs",
                INFO
            );
        }
        
        @Override
        public String getCliAnswer(long input) {
            return NTPCLI.streamToString(getListHeading(input), getPairStrings(input));
        }
    
        @Override
        public List<Component> getGuiComponents(long input) {
            return NTPGUI.createStreamHeadingAndTextArea(getListHeading(input), getPairStrings(input));
        }
    }
}