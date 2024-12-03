package numbertheoryplayground;

import java.text.DecimalFormat;

/**
 * Utility class with members that are used by multiple classes and don't really fit in any other class.
 */
public class Misc {
    public static final int oneThousand = 1_000;
    public static final int tenThousand = 10_000;
    public static final int oneHundredThousand = 100_000;
    public static final int oneBillion = 1_000_000_000;

    private static final DecimalFormat commaAdder = new DecimalFormat("#,###");
    
    public static String stringifyWithCommas(long l) {
        return commaAdder.format(l);
    }
    
    public static String intPairToString(int a, int b) {
        return String.format("%s & %s", stringifyWithCommas(a), stringifyWithCommas(b));
    }
    
    public static String getLongAndSquareString(long l) {
        return String.format("%s^2 (%s)", stringifyWithCommas(l), stringifyWithCommas(l * l));
    }
    
    /**
     * Returns a chunk of whitespace that's as long as the length specified.
     * Throws IllegalArgumentException if the length is negative.
     */
    public static String getWhiteSpace(int length) {
        return " ".repeat(length);
    }
    
    /**
     * Used to assert that an int argument is in a valid range for an algorithm.
     */
    public static void assertIsInRange(int intToCheck, int rangeMin, int rangeMax) {
        if (intToCheck < rangeMin || intToCheck > rangeMax) {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Used when there's an error that's not supposed to happen and we can handle it but we want to know that
     * that error happened.
     */
    public static void printError(String error) {
        System.out.println("=\n".repeat(10) + error + "\n=".repeat(10));
    }
}
