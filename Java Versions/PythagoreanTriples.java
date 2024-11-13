package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Divisibility.isDivisible;
import static com.nicholasschroeder.numbertheoryplayground.Misc.*;

/**
 * Utility class related to Pythagorean triples and the section for it.
 */
public class PythagoreanTriples {
    private static final String INFO = """
The Pythagorean Theorem says that for a right triangle, the sum of the squares of the lengths of the 2
short sides equals the square of the long side (hypotenuse) length, or a^2 + b^2 = c^2. This theorem was
named after the ancient Greek mathematician Pythagoras. A Pythagorean triple is a triple of integers that
a, b, and c can be. For example; 3, 4, and 5 is a Pythagorean triple since 3^2 (9) + 4^2 (16) = 5^2 (25)
and 11, 60, and 61 is another one since 11^2 (121) + 60^2 (3,600) = 61^2 (3,721).

Once we know a Pythagorean triple, we can form another one by multiplying a, b, and $ by the same positive
integer. Because of this, there are an infinite amount of Pythagorean triples. A Pythagorean triple is
considered to be primitive if the GCD of a, b, and c is 1. Therefore, a primitive triple can't be formed
by taking another triple and multiplying a, b, and c by something. The triples mentioned above; 3, 4, and 5,
and 11, 60, and 61; are primitive. 6 (3 x 2), 8 (4 x 2), and 10 (5 x 2) is another triple. 6^2 (36) +
8^2 (64) = 10^2 (100). 55 (11 x 5), 300 (60 x 5), and 305 (61 x 5) is another one. 55^2 (3,025) +
300^2 (90,000) = 305^2 (93,025).""";
    
    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = 10_000;
    private static final int NUM_TRIPLES_TO_FIND = 10;
    
    /**
     * Record for the 3 integers of a Pythagorean triple.
     */
    public record Triple(int a, int b, int c) {
        @Override
        public String toString() {
            return String.format(
                "%s + %s = %s%s",
                getLongAndSquareString(a),
                getLongAndSquareString(b),
                getLongAndSquareString(c),
                isPrimitive() ? " (primitive)" : ""
            );
        }
        
        public boolean isPrimitive() {
            /*
            See if there's a common factor other than 1. The iterating only needs to go up to a third of the min
            of a, b, and c. This is because if the min could be a common factor, then we would be able to divide
            a, b, and c by the min and get a Pythagorean triple and the min of a, b, and c in that triple would
            be 1. There's no Pythagorean Triple with 1. The first one is 3, 4, and 5. Because of that, half of
            the min also can't be a common factor. The next lowest possible common factor is a third of the min.
             */
            int maxI = Math.min(a, Math.min(b, c)) / 3;
            return IntStream.concat(
                IntStream.of(2),
                IntStream.iterate(3, i -> i <= maxI, i -> i + 2)
            )
            .noneMatch(i -> isDivisible(a, i) && isDivisible(b, i) && isDivisible(c, i));
        }
    }
    
    /**
     * Returns a list of triple objects for the first 10 Pythagorean triples where the lowest integer in the
     * triple is >= the input. For example, if the input is 3 then an object for the triple 3, 4, and 5 will
     * be the first one since the lowest number in that triple is 3. If the input number is 4, then an object
     * for the triple 5, 12, and 13 will be the first one.
     */
    public static List<Triple> getTriples(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);

        var triples = new ArrayList<Triple>(NUM_TRIPLES_TO_FIND);
        var a = (int) input;
        var b = a + 1;
        
        while (true) {
            var cDouble = Math.hypot(a, b);

            if (cDouble < b + 1) {
                /*
                b + 1 is the minimum possible integer value for c, so if c is less than that then
                the max value for b for the current value of a has been exceeded.
                 */
                b = ++a + 1;
            } else {
                var cInt = (int) cDouble;

                if (cDouble == cInt) {
                    triples.add(new Triple(a, b, cInt));
                    if (triples.size() == NUM_TRIPLES_TO_FIND) {
                        return triples;
                    }
                }
                
                b++;
            }
        }
    }
    
    /**
     * Returns a stream of strings that say the first 10 Pythagorean triples where the lowest integer in
     * the triple is >= the input. Each string contains the 1-based position of that triple followed by ") "
     * followed by the the string representation of that triple. Currently, NUM_TRIPLES_TO_FIND is 10 so
     * there'll be a 1-space indent for the strings that start with a single digit.
     */
    private static Stream<String> getNumberedTripleStrings(long input) {
        // Call getTriples first to see if it throws.
        List<Triple> triples = getTriples(input);
        var position = new AtomicInteger(1);
        return
            triples
            .stream()
            .map(t -> String.format("%s%d) %s", position.get() < 10 ? " " : "", position.getAndIncrement(), t));
    }
    
    private static String getTriplesHeading(long input) {
        return String.format(
            "The first %d Pythagorean triples >= %s are:",
            NUM_TRIPLES_TO_FIND,
            toStringWithCommas(input)
        );
    }
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Pythagorean Triples",
                MIN_INPUT,
                MAX_INPUT,
                String.format("get the first %d Pythagorean triples >= that integer", NUM_TRIPLES_TO_FIND),
                "Pythagorean triples",
                INFO
            );
        }
    
        /**
         * Returns a string that contains a heading and triple strings, each on their own line.
         */
        @Override
        public String getCliAnswer(long input) {
            return
                getNumberedTripleStrings(input)
                .map(s -> NTPCLI.insertNewLines(s, true))
                .collect(Collectors.joining("\n", getTriplesHeading(input) + '\n', ""));
        }
    
        /**
         * Returns a list with a heading label and an NTPTextArea with triple strings, each on their own line.
         */
        @Override
        public List<Component> getGuiComponents(long input) {
            return List.of(
                NTPGUI.createCenteredLabel(getTriplesHeading(input), NTPGUI.LIST_HEADING_FONT),
                new NTPTextArea(getNumberedTripleStrings(input), "\n")
            );
        }
    }
}