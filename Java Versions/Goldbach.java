import java.awt.Component;
import java.util.ArrayList;
import java.util.List;


/**
 * Utility class related to the Goldbach conjecture and the section for it.
 */
public class Goldbach {
    private static final String info =
        "The Goldbach conjecture states that every even number >= 4 can be expressed as the sum of " +
        "2 prime numbers. A conjecture is a statement that is believed to be true but has not been " +
        "proven to be true. The Goldbach conjecture has been verified to be true for all even numbers " +
        ">= 4 && <= a very high number. I don't know this number off the top of my head but it's way, " +
        "way bigger than the maximum number you're allowed to use for input.";

    public static final int minInputInt = 4;
    public static final int maxInputInt = oneHundredThousand;

    /**
     * @return A list of all the pairs of prime numbers that sum up to anInt.
     */
    public static List<IntPair> getGoldbachPrimePairs(int anInt) {
        assertIsInRange(anInt, minInputInt, maxInputInt);
        if (isOdd(anInt)) {
            throw new IllegalArgumentException("Arg can't be odd");
        }

        List<IntPair> pairs = new ArrayList<>();
        // 1st, check if anInt is 4 since 4 is the only even number >= 4 that
        // has 2 in a pair of prime numbers that sum to it.
        if (anInt == 4) {
            pairs.add(new IntPair(2, 2));
            return pairs;
        }

        /*
        Check if a possible prime number and the difference between that possible prime number and
        anInt are both prime. The max possible prime number that needs to be checked is equal to the
        floor of half of anInt. This is because after that point, calculations for primality will be
        done on numbers that have already been determined to be pairs of prime numbers that sum to anInt.
        */
        int maxPossiblePrime = anInt / 2;
        for (int possiblePrime1 = 3; possiblePrime1 <= maxPossiblePrime; possiblePrime1 += 2) {
            int possiblePrime2 = anInt - possiblePrime1;
            if (Primes.bothArePrime(possiblePrime1, possiblePrime2)) {
                pairs.add(new IntPair(possiblePrime1, possiblePrime2));
            }
        }
        return pairs;
    }

    private static List<String> getGoldbachPrimePairStrings(int anInt) {
        return stringifyElements(getGoldbachPrimePairs(anInt));
    }

    private static String getListHeading(int pairsCount, int inputInt) {
        return String.format(
            "There are %s pairs of prime numbers that sum to %s. They are:",
            getLongStringWithCommas(pairsCount),
            getLongStringWithCommas(inputInt)
        );
    }

    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Goldbach Conjecture",
                List.of(info),
                minInputInt,
                maxInputInt,
                "get the pairs of prime numbers that sum to it",
                "the Goldbach Conjecture",
                "Have %s be even && >= %d && <= %s"
            );
        }

        @Override
        public String getCliAnswer(int inputInt) {
            List<String> pairStrings = getGoldbachPrimePairStrings(inputInt);
            return NTPCLI.stringifyList(pairStrings, getListHeading(pairStrings.size(), inputInt));
        }

        @Override
        public List<Component> getGuiComponents(int inputInt) {
            List<String> pairStrings = getGoldbachPrimePairStrings(inputInt);
            return AnswerPanel.createListHeadingAndPanel(getListHeading(pairStrings.size(), inputInt), pairStrings);
        }

        @Override
        public int getRandomValidInt() {
            int randomInt = super.getRandomValidInt();
            return isEven(randomInt) ? randomInt : randomInt + 1;
        }
    }
}
