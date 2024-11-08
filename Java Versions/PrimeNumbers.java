package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Divisibility.*;
import static com.nicholasschroeder.numbertheoryplayground.Misc.*;

/**
 * Utility class related to prime numbers and the section for it.
 */
public class PrimeNumbers {
    private static final String INFO = """
A prime number, or a prime, is an integer >= 2 whose only integer factors are 1 and itself. A composite number
is an integer >= 2 that has an integer factor other than 1 and itself. The first 10 primes are
2, 3, 5, 7, 11, 13, 17, 19, 23, and 29. There are an infinite amount of them. The largest known prime is
2^136,279,841 - 1. It has 41,024,320 digits! Primes are used in 7 of the 10 sections in this application.

With the exception of 2 and 3, all primes are either 1 above or 1 below a multiple of 6. To show why this is
the case, let's have a variable i and let it represent any integer >= 6 that's a multiple of 6. We know that
i is divisible by 2 and 3 so i + 2 and i + 4 are divisible by 2 and i + 3 is divisible by 3 but we don't have
any guarantees about what i + 1 and i + 5 are divisible by. Therefore, that's where primes can be.

An integer can be determined to be prime if it's not divisible by any primes <= the square root of that
integer. This is called trial division. Let's determine if 29 and 33 are prime. 5^2 = 25 and 6^2 = 36 so the
square roots of 29 and 33 are between 5 and 6. We check if the integers are divisible by 2, 3, or 5; which are
the primes <= 5. 29 isn't divisible by any of those and 33 is divisible by 3 so 29 is prime and 33 isn't.""";
    
    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = TEN_TRILLION;
    private static final int NUM_PRIMES_TO_FIND = 30;
    
    public static boolean isPrime(long input) {
        if (input < 2) return false;
        if (input <= 3) return true;
        if (isEven(input)) return false;

        /*
        We only need to check if the input is divisible by any primes <= the floor of the square root
        of the input. If it is, then the input is not prime. Odd numbers will be checked since all primes
        besides 2 are odd.
         */
        
        var highestLongToCheck = (long) Math.sqrt(input);
        return
            LongStream.iterate(3, l -> l <= highestLongToCheck, l -> l + 2)
            .noneMatch(l -> isDivisible(input, l));
    }
    
    public static boolean bothArePrime(long a, long b) {
        return isPrime(a) && isPrime(b);
    }
    
    /**
     * Returns a stream of the first 30 primes >= the input.
     */
    public static LongStream getPrimes(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        long iterationStart = isOdd(input) ? input : input + 1;
        LongStream oddPrimes =
            LongStream.iterate(iterationStart, l -> l + 2)
            .filter(PrimeNumbers::isPrime);
        
        // 2 is the only even prime.
        return
            (input <= 2 ? LongStream.concat(LongStream.of(2), oddPrimes) : oddPrimes)
            .limit(NUM_PRIMES_TO_FIND);
    }
    
    /**
     * Returns a stream of the string representations of the first 30 primes >= the input.
     */
    private static Stream<String> getPrimesStrings(long input) {
        return getPrimes(input).mapToObj(Misc::toStringWithCommas);
    }
    
    private static String getListHeading(long input) {
        return String.format(
            "The first %d prime numbers >= %s are:",
            NUM_PRIMES_TO_FIND,
            toStringWithCommas(input)
        );
    }
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Prime Numbers",
                MIN_INPUT,
                MAX_INPUT,
                String.format("get the first %d prime numbers >= that integer", NUM_PRIMES_TO_FIND),
                "prime numbers",
                INFO
            );
        }
        
        @Override
        public String getCliAnswer(long input) {
            return NTPCLI.streamToString(getListHeading(input), getPrimesStrings(input));
        }
    
        @Override
        public List<Component> getGuiComponents(long input) {
            return NTPGUI.createStreamHeadingAndTextArea(getListHeading(input), getPrimesStrings(input));
        }
    }
}