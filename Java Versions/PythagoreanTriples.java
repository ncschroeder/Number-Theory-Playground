package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;

/**
 * Utility class related to Pythagorean Triples and the section for it.
 */
public class PythagoreanTriples {
    private static final String info =
        "The Pythagorean Theorem says that for the side lengths of a right triangle, the sum of the " +
        "squares of the 2 short sides equals the square of the long side (hypotenuse) or " +
        "a^2 + b^2 = c^2. This theorem was named after the ancient Greek mathematician Pythagoras. There " +
        "are an infinite amount of trios of integers that a, b, and c can be. These trios are called " +
        "Pythagorean Triples. For example, 3^2 (9) + 4^2 (16) = 5^2 (25) and " +
        "11^2 (121) + 60^2 (3,600) = 61^2 (3,721).";
    
    private static final int numberOfTriplesToFind = 10;
    private static final int minInputInt = 0;
    private static final int maxInputInt = oneThousand;
    
    /**
     * Class with data for the 3 elements of a Pythagorean Triple.
     */
    public static class PythagoreanTriple {
        public final int side1;
        public final int side2;
        public final int hypotenuse;
        
        public PythagoreanTriple(int side1, int side2, int hypotenuse) {
            this.side1 = side1;
            this.side2 = side2;
            this.hypotenuse = hypotenuse;
        }
        
        @Override
        public String toString() {
            return String.format(
                "%s + %s = %s",
                getLongAndSquareString(side1),
                getLongAndSquareString(side2),
                getLongAndSquareString(hypotenuse)
            );
        }
    
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PythagoreanTriple) {
                PythagoreanTriple other = (PythagoreanTriple) obj;
                return this.side1 == other.side1 && this.side2 == other.side2 && this.hypotenuse == other.hypotenuse;
            }
            return false;
        }
    }
    
    /**
     * Returns a list of triple objects to represent the first 10 Pythagorean Triples where the lowest integer
     * in the triple is >= the input. For example, if the input is 3, then the triple 3, 4, and 5 will be the
     * first one found since the lowest number in that triple is 3. If the input number is 4, then the triple
     * 5, 12, and 13 will be the first one found.
     */
    public static List<PythagoreanTriple> getPythagTriples(int input) {
        assertIsInRange(input, minInputInt, maxInputInt);

        var triples = new ArrayList<PythagoreanTriple>(numberOfTriplesToFind);
        
        // sideLength1 represents one of the lengths of one of the two short sides of a right triangle.
        // 3 is the lowest number to be in a Pythagorean triple so make sideLength1 be at least that.
        int sideLength1 = Math.max(input, 3);
        // sideLength2 represents the length of the other short side
        int sideLength2 = sideLength1 + 1;
        
        while (true) {
            double hypotLengthDouble = Math.hypot(sideLength1, sideLength2);
            if (hypotLengthDouble < sideLength2 + 1) {
                // sideLength2 + 1 is the minimum possible integer value for the hypotenuse length.
                // If the hypotenuse length is less than this, then the max value for sideLength2 for the
                // current value of sideLength1 has been exceeded.
                sideLength1++;
                sideLength2 = sideLength1 + 1;
            } else {
                int hypotLengthInt = (int) hypotLengthDouble;
                if (hypotLengthInt == hypotLengthDouble) {
                    // A Pythagorean Triple has been found.
                    triples.add(new PythagoreanTriple(sideLength1, sideLength2, hypotLengthInt));
                    if (triples.size() == numberOfTriplesToFind) {
                        return triples;
                    }
                }
                sideLength2++;
            }
        }
    }
    
    /**
     * Returns a Stream of strings that say the first 10 Pythagorean Triples where the lowest integer in
     * the triple is >= the input. Each string contains the 1-based position of that triple followed by ") "
     * followed by the the string representation of the object for that triple. Currently, numberOfTriplesToFind
     * is 10 so there'll be a 1 space indent for the strings that start with a single digit.
     */
    private static Stream<String> getNumberedPythagTriplesStrings(int input) {
        var ai = new AtomicInteger(1);
        return
            getPythagTriples(input)
            .stream()
            .map(pt -> String.format("%s%d) %s", ai.get() < 10 ? " " : "", ai.getAndIncrement(), pt));
    }
    
    private static String getListHeading(int input) {
        return String.format(
            "The first %d Pythagorean Triples >= %s are:",
            numberOfTriplesToFind,
            stringifyWithCommas(input)
        );
    }
    
    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Pythagorean Triples",
                List.of(info),
                minInputInt,
                maxInputInt,
                String.format("get the first %d Pythagorean Triples >= that integer", numberOfTriplesToFind),
                "Pythagorean Triples"
            );
        }
    
        /**
         * Returns a string that contains a heading and numbered Pythagorean Triple strings, each on their
         * own line.
         */
        @Override
        public String getCliAnswer(int input) {
            return
                getNumberedPythagTriplesStrings(input)
                .collect(Collectors.joining("\n", getListHeading(input) + "\n", ""));
        }
    
        /**
         * Returns a list with a heading label and an NTPTextArea that contains numbered
         * Pythagorean Triple strings, each on their own line.
         */
        @Override
        public List<Component> getGuiComponents(int input) {
            return List.of(
                NTPGUI.createCenteredLabel(getListHeading(input), NTPGUI.listHeadingFont),
                new NTPTextArea(getNumberedPythagTriplesStrings(input), "\n")
            );
        }
    }
}