package numbertheoryplayground;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Utility class with members that are used by multiple classes and don't fit in any other class.
 */
public class Misc {
    public static final String NTP_INFO = """
Number theory is a branch of math and is the study of integers. Let's go over a few types of
numbers. Natural numbers are 1, 2, 3, and so on. Whole numbers are the natural numbers along
with 0. Integers are the whole numbers along with negative natural numbers; so ...,
-2, -1, 0, 1, 2, ...

The Number Theory Playground is an application where a user can visit sections that show
info and do calculations for number theory concepts. These calculations are done based on input
numbers provided by the user. Some sections require 1 input number and some require 2. An example
of a section is the prime numbers section and it has 17 sentences of info and can find the first
30 prime numbers that are ≥ an input number.

Almost all of these calculations involve only natural numbers. There are 2 exceptions to this:

1. The divisibility section does calculations that might involve negative integers or 0.

2. The Fibonacci-like sequences section does calculations where the result is a floating-point
number.

The term "whole number" is often used in this app since I find it to be the more self-explanatory
than "natural number.\"""";
    
    
    // Max input constants
    public static final long ONE_POINT_FIVE_MILLION = 1_500_000;
    public static final long FIVE_HUNDRED_BILLION = 500_000_000_000L;
    public static final long TEN_TRILLION = 10_000_000_000_000L;
    public static final long ONE_QUADRILLION = 1_000_000_000_000_000L;
    public static final long FIVE_QUADRILLION = ONE_QUADRILLION * 5;
    public static final long TEN_QUADRILLION = ONE_QUADRILLION * 10;
    public static final long NINE_QUINTILLION = 9_000_000_000_000_000_000L;
    
    public static Stream<String> getParagraphs(String info) {
        return
            Arrays.stream(info.split("\n\n"))
            .map(s -> s.replace('\n', ' '));
    }
    
    public static long stripCommasAndParse(String s) {
        return Long.parseLong(s.replace(",", ""));
    }
    
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
    
    public static String getSpace(int length) {
        return " ".repeat(length);
    }
    
    public static final class InvalidInputNumberException extends IllegalArgumentException {
        private InvalidInputNumberException() {}
        
        /**
         * This is thrown by assertIsInRange below and by GoldbachConjecture.getPrimePairStarts.
         */
        private static final InvalidInputNumberException instance = new InvalidInputNumberException();
        
        public static InvalidInputNumberException getInstance() {
            return instance;
        }
    }
    
    /**
     * Used to assert that a long argument is in a valid range for an algorithm.
     */
    public static void assertIsInRange(long input, long minInput, long maxInput) {
        if (input < minInput || input > maxInput) {
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
