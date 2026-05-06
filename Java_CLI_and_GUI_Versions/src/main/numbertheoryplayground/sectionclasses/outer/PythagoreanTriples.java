package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpGui;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.sectionclasses.outer.Divisibility.isDivisible;

/**
 * Utility class related to Pythagorean triples and the section for it.
 */
public class PythagoreanTriples {
    private static final String INFO = """
The Pythagorean theorem says that for a right triangle, the sum of the squares of the lengths
of the 2 shortest sides (legs) equals the square of the longest side (hypotenuse) length, or
a^2 + b^2 = c^2. This was named after the ancient Greek mathematician Pythagoras. A Pythagorean
triple is a triple of whole numbers that a, b, and c can be. For example; 3, 4, and 5 is a
Pythagorean triple since 3^2 (9) + 4^2 (16) = 5^2 (25) and 11, 60, and 61 is another one since
11^2 (121) + 60^2 (3,600) = 61^2 (3,721).

Once we know a Pythagorean triple, we can form another one by multiplying a, b, and c by the
same whole number > 1. Because of this, there are an infinite amount of Pythagorean triples.
A Pythagorean triple is considered to be primitive if the GCD of a, b, and c is 1. Therefore,
a primitive triple can't be formed by taking another triple and multiplying a, b, and c by the
same whole number. The triples mentioned above; 3, 4, and 5, and 11, 60, and 61; are primitive.
6 (3 × 2), 8 (4 × 2), and 10 (5 × 2) is another triple. 6^2 (36) + 8^2 (64) = 10^2 (100).
55 (11 × 5), 300 (60 × 5), and 305 (61 × 5) is another one.
55^2 (3,025) + 300^2 (90,000) = 305^2 (93,025).

The algorithm I came up with for calculating triples first tries to find triples where the short
leg length equals the input number and then tries to find triples where the short leg equals the
input number + 1, and so on until 10 are found.""";

    /*
    The calculation for this section is: find the first 10 Pythagorean triples where the lowest
    number in the triple is ≥ an input number. For example, if the input number is 3, then the
    triple 3, 4, and 5 will be the first one found. If the input number is 4, then the triple
    5, 12, and 13 will be the first one found. These triples will be displayed like the examples
    at the end of the paragraphs in the text block above are displayed. If a triple is primitive,
    then it'll be followed by "(primitive)".
     */
    
    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = 10_000;
    private static final int NUM_TRIPLES_TO_FIND = 10;
    
    /**
     * a is for the short leg, b is for the long leg, and c is for the hypotenuse.
     */
    record Triple(int a, int b, int c) {
        @Override
        public String toString() {
            return String.format(
                "%s + %s = %s%s",
                createLongAndSquareString(a),
                createLongAndSquareString(b),
                createLongAndSquareString(c),
                isPrimitive() ? " (primitive)" : ""
            );
        }
        
        boolean isPrimitive() {
            /*
            See if there's a common factor other than 1. The iterating only needs to go up to a third of the min
            of a, b, and c. This is because if the min could be a common factor, then we would be able to divide
            a, b, and c by the min and get a Pythagorean triple and the min of a, b, and c in that triple would
            be 1. There's no Pythagorean Triple with 1. The first one is 3, 4, and 5. Because of that, half of
            the min also can't be a common factor. The next lowest possible common factor is a third of the min.
             */
            int maxPossibleCommonFactor = Math.min(a, Math.min(b, c)) / 3;
            return
                IntStream.concat(
                    IntStream.of(2),
                    IntStream.iterate(3, i -> i + 2)
                )
                .takeWhile(i -> i <= maxPossibleCommonFactor)
                .noneMatch(i -> isDivisible(a, i) && isDivisible(b, i) && isDivisible(c, i));
        }
    }
    
    /**
     * Returns a list of Triples for the first 10 Pythagorean triples where the short leg length,
     * the lowest number in the triple, is ≥ the input. For example, if the input is 3, then a
     * Triple for the triple 3, 4, and 5 will be the first one. If the input is 4, then a Triple
     * for the triple 5, 12, and 13 will be the first one.
     */
    static List<Triple> getTriples(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        var triples = new ArrayList<Triple>(NUM_TRIPLES_TO_FIND);
        // a and b are for the short and long leg lengths, respectively.
        var a = (int) input;
        var b = a + 1;
        
        while (true) {
            var cDouble = Math.hypot(a, b);
            if (cDouble < b + 1) {
                /*
                b + 1 is the minimum possible integer value for c, so if c is less than that,
                then the max value for b for the current value of a has been exceeded.
                 */
                b = ++a + 1;
                continue;
            }
            
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
    
    /**
     * Returns a stream of strings that say the first 10 Pythagorean triples where the lowest integer in
     * the triple is ≥ the input. Each string contains the 1-based position of that triple followed by ") "
     * followed by the string representation of that triple. Currently, NUM_TRIPLES_TO_FIND is 10 so
     * there'll be a 1-space indent for the strings that start with a single digit.
     */
    private static Stream<String> getNumberedTripleStrings(long input, int indentLength) {
        List<Triple> triples = getTriples(input);
        String indent = getSpace(indentLength);
        var position = new AtomicInteger(1);
        return
            triples
            .stream()
            .map(t -> {
                String maybeIndent = position.get() < 10 ? indent : "";
                return String.format("%s%d) %s", maybeIndent, position.getAndIncrement(), t);
            });
    }
    
    private static final String TRIPLES_HEADING_START =
        String.format(
            "The first %d Pythagorean triples where the lowest number in the triple is ≥",
            NUM_TRIPLES_TO_FIND
        );
    
    private static final String ACTION_SENTENCES_ENDING =
        't' + TRIPLES_HEADING_START.substring(1) + " that number";
    
    private static String getTriplesHeading(String inputString) {
        return String.format("%s %s are:", TRIPLES_HEADING_START, inputString);
    }
    
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Pythagorean Triples",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                ACTION_SENTENCES_ENDING,
                "Pythagorean triples"
            );
        }
        
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            Stream<String> tripleStrings = getNumberedTripleStrings(inputLong, 1);
            return NtpCli.buildStringWithStreamElementsOnSeparateLines(
                getTriplesHeading(inputString),
                tripleStrings
            );
        }
        
        @Override
        public List<Component> getGuiComponents(long inputLong, String inputString) {
            Stream<String> tripleStrings = getNumberedTripleStrings(inputLong, 2);
            return List.of(
                NtpGui.createListHeadingLabel(getTriplesHeading(inputString)),
                NtpTextArea.createWithStreamElementsOnSeparateLines(tripleStrings)
            );
        }
    }
}
