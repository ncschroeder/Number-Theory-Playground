import java.awt.Component;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class related to twin prime numbers and the section for it.
 */
public class TwinPrimes {
    private static final String info =
        "Twin primes are prime numbers that differ by 2. It is conjectured that there " +
        "are infinitely many of them. A conjecture is a statement that is believed to be " +
        "true but has not been proven to be.";

    private static final int numberOfPairsToFind = 20;
    private static final int minInputInt = 0;
    private static final int maxInputInt = oneBillion;

    /**
     * Returns an IntStream that can find the first 20 pairs of twin prime numbers where the lower of the 2
     * numbers is >= the input. This Stream will yield the first numbers in each pair.
     */
    public static IntStream getTwinPrimePairStarts(int input) {
        assertIsInRange(input, minInputInt, maxInputInt);
        
        /*
        With the exception of 2 and 3, all prime numbers are either 1 above or 1 below a multiple of 6.
        This means that all twin prime number pairs besides 3 and 5 consist of 1 number that is 1 below a
        multiple of 6 and another number is 1 above that same multiple of 6. This algorithm takes
        advantage of this.
        
        First, set the iteration seed to the first int >= the input that is 1 below a multiple of 6. Since
        minInputInt is 0, we don't have to handle situations where the input is negative. For those situations,
        we would have the seed be 5. The Stream that gets created will be able to iterate through ints
        that are 1 below a multiple of 6.
        */
        
        int seed = input;
        while (seed % 6 != 5) {
            seed++;
        }
    
        return
            IntStream.concat(
                input <= 3 ? IntStream.of(3) : IntStream.empty(),
                IntStream.iterate(seed, i -> i + 6)
            )
            .filter(i -> bothArePrime(i, i + 2))
            .limit(numberOfPairsToFind);
    }

    /**
     * Returns a Stream of the string representations of the first 20 pairs of twin prime numbers where the
     * lower of the 2 numbers is >= the input
     */
    private static Stream<String> getTwinPrimePairStrings(int input) {
        return getTwinPrimePairStarts(input).mapToObj(i -> intPairToString(i, i + 2));
    }

    private static String getListHeading(int input) {
        return String.format(
            "The first %d pairs of twin prime numbers >= %s are:",
            numberOfPairsToFind,
            stringifyWithCommas(input)
        );
    }

    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Twin Prime Numbers",
                List.of(info),
                minInputInt,
                maxInputInt,
                String.format("get the first %d pairs of twin prime numbers >= that integer", numberOfPairsToFind),
                "twin prime numbers"
            );
        }

        @Override
        public String getCliAnswer(int input) {
            return NTPCLI.stringifyList(getListHeading(input), getTwinPrimePairStrings(input));
        }

        @Override
        public List<Component> getGuiComponents(int input) {
            return AnswerPanel.createListHeadingAndPanel(getListHeading(input), getTwinPrimePairStrings(input));
        }
    }
}
