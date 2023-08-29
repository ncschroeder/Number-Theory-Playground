package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;
import static com.nicholasschroeder.numbertheoryplayground.methods.Divisibility.*;

/**
 * Utility class related to prime numbers and the section for it.
 */
public class Primes {
    private static final List<String> infoParagraphs =
        List.of(
            "A prime number is an integer >= 2 whose only factors are 1 and itself. There are an infinite " +
                "amount of them. A composite number is an integer >= 2 that has a factor other than 1 and " +
                "itself. Prime numbers are used in 7 of the 10 sections in this application.",
            "An integer can be determined to be prime if it is not divisible by any prime numbers <= " +
                "the square root of that integer. For example, let's determine if 29 and 33 are prime. " +
                "5^2 = 25 and 6^2 = 36 so the square roots of 29 and 33 are between 5 and 6. We check if " +
                "the integers are divisible by 2, 3, or 5, which are the prime numbers <= 5. 29 isn't " +
                "divisible by any of those and 33 is divisible by 3 so 29 is prime and 33 isn't.",
            "Fun fact: with the exception of 2 and 3, all prime numbers are either 1 above or 1 below " +
                "a multiple of 6. To show why this is the case, let's have a variable i and let it represent " +
                "any integer >= 6 that is a multiple of 6. We know that i is divisible by 2 and 3 so i + 2 " +
                "and i + 4 are divisible by 2 and i + 3 is divisible by 3 but we don't have any guarantees " +
                "about what i + 1 and i + 5 are divisible by. Therefore, that is where prime numbers can be."
        );

    public static boolean isPrime(int input) {
        // All integers < 2 are not prime
        if (input < 2) {
            return false;
        }

        // 2 and 3 are prime
        if (input <= 3) {
            return true;
        }

        if (isEven(input)) {
            return false;
        }

        /*
        Only need to check if the input is divisible by any prime numbers <= the floor of the square root
        of the input. If it is, then the input is not prime. Odd numbers will be checked since all prime
        numbers besides 2 are odd.
        */

        int highestIntToCheck = (int) Math.sqrt(input);
        return
            IntStream.iterate(3, i -> i <= highestIntToCheck, i -> i + 2)
            .noneMatch(i -> isDivisible(input, i));
    }

    public static boolean bothArePrime(int a, int b) {
        return isPrime(a) && isPrime(b);
    }

    private static final int numberOfPrimesToFind = 30;
    private static final int minInputInt = 0;
    private static final int maxInputInt = oneBillion;

    /**
     * Returns an IntStream of the first 30 prime numbers >= the input.
     */
    public static IntStream getPrimes(int input) {
        assertIsInRange(input, minInputInt, maxInputInt);

        return
            IntStream.concat(
                input <= 2 ? IntStream.of(2) : IntStream.empty(), // 2 is the only even prime number
                IntStream.iterate(isOdd(input) ? input : input + 1, i -> i + 2) // Iterate through odd numbers
            )
            .filter(Primes::isPrime)
            .limit(numberOfPrimesToFind);
    }

    /**
     * Returns a Stream of the string representations of the first 30 prime numbers >= the input.
     */
    private static Stream<String> getPrimesStrings(int input) {
        return getPrimes(input).mapToObj(Misc::stringifyWithCommas);
    }

    private static String getListHeading(int input) {
        return String.format(
            "The first %d prime numbers >= %s are:",
            numberOfPrimesToFind,
            stringifyWithCommas(input)
        );
    }

    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Prime Numbers",
                infoParagraphs,
                minInputInt,
                maxInputInt,
                String.format("get the first %d prime numbers >= that integer", numberOfPrimesToFind),
                "prime numbers"
            );
        }

        @Override
        public String getCliAnswer(int input) {
            return NTPCLI.streamToString(getListHeading(input), getPrimesStrings(input));
        }

        @Override
        public List<Component> getGuiComponents(int input) {
            return NTPGUI.createStreamHeadingAndTextArea(getListHeading(input), getPrimesStrings(input));
        }
    }
}
