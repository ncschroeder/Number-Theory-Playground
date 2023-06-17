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
     * Find the pairs of prime numbers that sum to anInt and returns an array that contains the lower numbers
     * of each pair
     */
    public static int[] getGoldbachPrimePairStarts(int anInt) {
        assertIsInRange(anInt, minInputInt, maxInputInt);
        if (isOdd(anInt)) {
            throw new IllegalArgumentException("Arg can't be odd");
        }

        /*
        Check if a possible prime number and the difference between that possible prime number and
        anInt are both prime. The max possible prime number that needs to be checked is equal to the
        floor of half of anInt. This is because after that point, calculations for primality will be
        done on numbers that have already been determined to be pairs of prime numbers that sum to anInt.
        */
        
        if (anInt == 4) {
            return new int[] { 2 };
        }
        
        int maxI = anInt / 2;
        return
            IntStream.iterate(3, i -> i <= maxI, i -> i + 2)
            .filter(i -> bothArePrime(i, anInt - i))
            .toArray();
    }

    /**
     * Returns a Stream of the string representations of the pairs of prime numbers that sum to inputInt.
     * pairStarts should be an int array returned from calling getGoldbachPrimePairStarts and inputInt
     * should be the int used in that call to getGoldbachPrimePairStarts.
     */
    private static Stream<String> getGoldbachPrimePairStrings(int[] pairStarts, int inputInt) {
        return Arrays.stream(pairStarts).mapToObj(i -> intPairToString(i, inputInt - i));
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
            int[] pairStarts = getGoldbachPrimePairStarts(inputInt);
            return NTPCLI.stringifyList(
                getListHeading(pairStarts.length, inputInt),
                getGoldbachPrimePairStrings(pairStarts, inputInt)
            );
        }

        @Override
        public List<Component> getGuiComponents(int inputInt) {
            int[] pairStarts = getGoldbachPrimePairStarts(inputInt);
            return AnswerPanel.createListHeadingAndTextArea(
                getListHeading(pairStarts.length, inputInt),
                getGoldbachPrimePairStrings(pairStarts, inputInt)
            );
        }

        @Override
        public int getRandomValidInt() {
            int randomInt = super.getRandomValidInt();
            return isEven(randomInt) ? randomInt : randomInt + 1;
        }
    }
}
