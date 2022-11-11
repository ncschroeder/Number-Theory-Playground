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
     * @return A list of strings that say the first 20 pairs of twin prime numbers where the lower of the 2
     * numbers is >= anInt.
     */
    public static List<IntPair> getTwinPrimePairs(int anInt) {
        assertIsInRange(anInt, minInputInt, maxInputInt);

        /*
        With the exception of 2 and 3, all prime numbers are either 1 above or 1 below a multiple of 6. This means
        that all prime number pairs besides 3 and 5 consist of 1 number that is 1 below a multiple of 6 and the
        other number is 1 above that same multiple of 6. This algorithm takes advantage of this.
        */

        List<IntPair> pairs = new ArrayList<>(numberOfPairsToFind);
        int possiblePrime1;
        if (anInt <= 3) {
            pairs.add(new IntPair(3, 5));
            // Set possiblePrime1 to the first positive int that is 1 below a multiple of 6
            possiblePrime1 = 5;
        } else {
            // Set possiblePrime1 to the first int >= anInt that is 1 below a multiple of 6
            possiblePrime1 = anInt;
            while (possiblePrime1 % 6 != 5) {
                possiblePrime1++;
            }
        }

        // Check pairs of numbers that are possibly twin prime pairs until 20 twin prime pairs are found
        for (;; possiblePrime1 += 6) {
            int possiblePrime2 = possiblePrime1 + 2;
            if (Primes.bothArePrime(possiblePrime1, possiblePrime2)) {
                pairs.add(new IntPair(possiblePrime1, possiblePrime2));
                if (pairs.size() == numberOfPairsToFind) {
                    return pairs;
                }
            }
        }
    }

    /**
     */
    private static List<String> getTwinPrimePairStrings(int anInt) {
        return stringifyElements(getTwinPrimePairs(anInt));
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
