import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Utility class related to prime numbers and the section for it.
 */
public class Primes {
    private static final String info =
        "Prime numbers are numbers that are only divisible by 1 and themself. There are " +
        "an infinite amount of them. A number can be determined to be prime if it is not " +
        "divisible by any prime numbers less than or equal to the square root of that " +
        "number. Fun fact: with the exception of 2 and 3, all prime numbers are either " +
        "1 above or 1 below a multiple of 6.";

    public static boolean isPrime(int anInt) {
        // All integers < 2 are not prime
        if (anInt < 2) {
            return false;
        }

        // 2 and 3 are prime
        if (anInt <= 3) {
            return true;
        }

        if (isEven(anInt)) {
            return false;
        }

        // Only need to check if anInt is divisible by any prime numbers <= the floor of the square root of anInt.
        // If it is, then anInt is not prime. Odd numbers will be checked since all prime numbers besides 2 are odd.
        int highestIntToCheck = (int) Math.sqrt(anInt);
        return
            IntStream.iterate(3, i -> i <= highestIntToCheck, i -> i + 2)
            .noneMatch(i -> isDivisible(anInt, i));
    }

    public static boolean bothArePrime(int a, int b) {
        return isPrime(a) && isPrime(b);
    }

    private final static int numberOfPrimesToFind = 30;
    public static final int minInputInt = 0;
    public static final int maxInputInt = oneBillion;

    /**
     * Returns an IntStream of the first 30 prime numbers that are >= anInt
     */
    public static IntStream getPrimesInts(int anInt) {
        assertIsInRange(anInt, minInputInt, maxInputInt);

        return
            IntStream.concat(
                anInt <= 2 ? IntStream.of(2) : IntStream.empty(), // 2 is the only even prime number
                IntStream.iterate(isOdd(anInt) ? anInt : anInt + 1, i -> i + 2) // Iterate through odd numbers
            )
            .filter(Primes::isPrime)
            .limit(numberOfPrimesToFind);
    }

    /**
     * Returns a Stream of the string representations of the first 30 prime numbers >= anInt
     */
    private static Stream<String> getPrimesStrings(int anInt) {
        return getPrimes(anInt).mapToObj(Misc::stringifyWithCommas);
    }

    private static String getListHeading(int inputInt) {
        return String.format(
            "The first %d prime numbers >= %s are:",
            numberOfPrimesToFind,
            getLongStringWithCommas(inputInt)
        );
    }

    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Prime Numbers",
                List.of(info),
                minInputInt,
                maxInputInt,
                String.format("get the first %d prime numbers >= that integer", numberOfPrimesToFind),
                "prime numbers"
            );
        }

        @Override
        public String getCliAnswer(int inputInt) {
            return NTPCLI.stringifyList(getPrimesStrings(inputInt), getListHeading(inputInt));
        }

        @Override
        public List<Component> getGuiComponents(int inputInt) {
            return AnswerPanel.createListHeadingAndPanel(getListHeading(inputInt), getPrimesStrings(inputInt));
        }
    }
}
