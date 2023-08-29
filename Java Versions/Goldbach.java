package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;
import static com.nicholasschroeder.numbertheoryplayground.Divisibility.isEven;
import static com.nicholasschroeder.numbertheoryplayground.Divisibility.isOdd;
import static com.nicholasschroeder.numbertheoryplayground.Primes.bothArePrime;

/**
 * Utility class related to the Goldbach conjecture and the section for it.
 */
public class Goldbach {
    private static final String info =
        "The Goldbach Conjecture says that every even number >= 4 can be expressed as the sum of 2 prime " +
        "numbers. A conjecture is a statement that is believed to be true but has not been proven to be " +
        "true. The Goldbach Conjecture has been verified to be true for all even numbers >= 4 && <= a very " +
        "high number. I don't know this number off the top of my head but it's way, way bigger than the " +
        "max number you're allowed to use for input.";

    public static final int minInputInt = 4;
    public static final int maxInputInt = oneHundredThousand;


    /**
     * Find the pairs of prime numbers that sum to the input and returns an array that contains the lower numbers
     * of each pair.
     */
    public static int[] getGoldbachPrimePairStarts(int input) {
        assertIsInRange(anInt, minInputInt, maxInputInt);
        if (isOdd(anInt)) {
            throw new IllegalArgumentException();
        }

        /*
        First, check if the input is 4 since 4 is the only even number >= 4 that has 2 in a pair of prime
        numbers that sum to it. If the input isn't 4, check pairs of odd ints that sum to the input for
        primality. The iterating only needs to go up to the floor of half of the input. This is because
        after that point, checks for primality will be done on pairs of ints that have already been checked
        for primality.
        */
        
        if (input == 4) {
            return new int[] { 2 };
        }
        
        int maxI = input / 2;
        return
            IntStream.iterate(3, i -> i <= maxI, i -> i + 2)
            .filter(i -> bothArePrime(i, input - i))
            .toArray();
    }

    /**
     * Returns a Stream of the string representations of the pairs of prime numbers that sum to the input.
     * pairStarts should be an int array returned from calling getGoldbachPrimePairStarts using the input.
     */
    private static Stream<String> getGoldbachPrimePairStrings(int[] pairStarts, int input) {
        return Arrays.stream(pairStarts).mapToObj(i -> intPairToString(i, input - i));
    }


    private static String getListHeading(int pairsCount, int input) {
        return String.format(
            "There are %s pairs of prime numbers that sum to %s. They are:",
            stringifyWithCommas(pairsCount),
            stringifyWithCommas(input)
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
        public String getCliAnswer(int input) {
            int[] pairStarts = getGoldbachPrimePairStarts(input);
            return NTPCLI.streamToString(
                getListHeading(pairStarts.length, input),
                getGoldbachPrimePairStrings(pairStarts, input)
            );
        }

        @Override
        public List<Component> getGuiComponents(int input) {
            int[] pairStarts = getGoldbachPrimePairStarts(input);
            return NTPGUI.createStreamHeadingAndTextArea(
                getListHeading(pairStarts.length, input),
                getGoldbachPrimePairStrings(pairStarts, input)
            );
        }

        /**
         * Returns an random, even int in the range of valid input ints for this Section.
         */
        @Override
        public int getRandomValidInt() {
            int randomInt = super.getRandomValidInt();
            return isEven(randomInt) ? randomInt : randomInt + 1;
        }
    }
}
