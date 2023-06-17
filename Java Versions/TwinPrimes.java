import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class related to twin prime numbers and the section for it.
 */
public class TwinPrimes {
    private static final String info =
        "Twin primes are prime numbers that differ by 2. It is conjectured that there " +
        "are infinitely many of them. A conjecture is a statement that is believed to be " +
        "true but has not been proven to be.";

    private final static int numberOfPairsToFind = 20;
    public static final int minInputInt = 0;
    public static final int maxInputInt = oneBillion;

    /**
     * Returns an IntStream that can find the first 20 pairs of twin prime numbers where the lower of the 2
     * numbers is >= anInt. This Stream will yield the first numbers in each pair.
     */
    public static IntStream getTwinPrimePairStarts(int anInt) {
        assertIsInRange(anInt, minInputInt, maxInputInt);
        
        /*
        With the exception of 2 and 3, all prime numbers are either 1 above or 1 below a multiple of 6. This means
        that all prime number pairs besides 3 and 5 consist of 1 number that is 1 below a multiple of 6 and the
        other number is 1 above that same multiple of 6. This algorithm takes advantage of this.
        */
        
        int seed = anInt;
        while (seed % 6 != 5) {
            seed++;
        }
    
        return
            IntStream.concat(
                anInt <= 3 ? IntStream.of(3) : IntStream.empty(),
                IntStream.iterate(seed, i -> i + 6)
            )
            .filter(i -> bothArePrime(i, i + 2))
            .limit(numberOfPairsToFind);
    }

    /**
     * Returns a Stream of the string representations of the first 20 pairs of twin prime numbers where the
     * lower of the 2 numbers is >= anInt
     */
    private static Stream<String> getTwinPrimePairStrings(int anInt) {
        return getTwinPrimePairStarts(anInt).mapToObj(i -> intPairToString(i, i + 2));
    }

    private static String getListHeading(int inputInt) {
        return String.format(
            "The first %d pairs of twin prime numbers >= %s are:",
            numberOfPairsToFind,
            getLongStringWithCommas(inputInt)
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
        public String getCliAnswer(int inputInt) {
            return NTPCLI.stringifyList(getTwinPrimePairStrings(inputInt), getListHeading(inputInt));
        }

        @Override
        public List<Component> getGuiComponents(int inputInt) {
            return AnswerPanel.createListHeadingAndPanel(getListHeading(inputInt), getTwinPrimePairStrings(inputInt));
        }
    }
}
