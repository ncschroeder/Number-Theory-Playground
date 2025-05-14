package numbertheoryplayground;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Utility class with members that are used by multiple classes and don't fit in any other class.
 */
public class Misc {
    public static final Supplier<Stream<String>> ntpInfoParagraphStreamSupplier = () -> """
"""
        .transform(Misc::getParagraphStream);
    
    public static String replaceNewLineCharsWithSpaces(String s) {
        return s.replace('\n', ' ');
    }
    
    public static Stream<String> getParagraphStream(String info) {
        return
            Arrays.stream(info.split("\n\n"))
            .map(Misc::replaceNewLineCharsWithSpaces);
    }
    
    public static List<String> getParagraphList(String info) {
        return getParagraphStream(info).toList();
    }
    
    // Max input constants
    public static final long FIVE_HUNDRED_BILLION = 500_000_000_000L;
    public static final long TEN_TRILLION = 10_000_000_000_000L;
    public static final long ONE_QUADRILLION = 1_000_000_000_000_000L;
    public static final long FIVE_QUADRILLION = ONE_QUADRILLION * 5;
    public static final long TEN_QUADRILLION = ONE_QUADRILLION * 10;
    public static final long NINE_QUINTILLION = 9_000_000_000_000_000_000L;
    
    private static final DecimalFormat commaAdder = new DecimalFormat("#,###");
    
    public static String createStringWithCommas(long l) {
        return commaAdder.format(l);
    }
    
    public static String createStringWithCommas(BigInteger bi) {
        return commaAdder.format(bi);
    }
    
    public static String longPairToString(long a, long b) {
        return String.format("%s & %s", createStringWithCommas(a), createStringWithCommas(b));
    }
    
    public static String createLongAndSquareString(long l) {
        return String.format("%s^2 (%s)", createStringWithCommas(l), createStringWithCommas(l * l));
    }
    
    public static long stripCommasAndParse(String s) {
        return Long.parseLong(s.replace(",", ""));
    }
    public static String getSpace(int length) {
        return " ".repeat(length);
    }
    
    public static final class InvalidInputNumberException extends IllegalArgumentException {
        private InvalidInputNumberException() {}
        
        public static final InvalidInputNumberException instance =
            new InvalidInputNumberException();
    }
    
    /**
     * Used to assert that a long argument is in a valid range for an algorithm.
     */
    public static void assertIsInRange(long longToCheck, long rangeMin, long rangeMax) {
        if (longToCheck < rangeMin || longToCheck > rangeMax) {
            throw InvalidInputNumberException.instance;
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
