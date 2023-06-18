import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class related to Pythagorean triples and the section for it.
 */
public class PythagoreanTriples {
    private static final String info =
        "The Pythagorean theorem says that for the side lengths of a right triangle, " +
        "the sum of the squares of the 2 short sides equals the square of the long " +
        "side (hypotenuse) or a^2 + b^2 = c^2. There are an infinite amount of trios " +
        "of integers that a, b, and c can be. These trios are called Pythagorean triples.";

    private static final int numberOfTriplesToFind = 10;
    private static final int minInputInt = 0;
    private static final int maxInputInt = oneThousand;

    /**
     * Class with data for the 3 elements of a Pythagorean triple
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
     * Returns a List of PythagoreanTriple objects to represent the first 10 Pythagorean triples where
     * the lowest integer in the triple is >= the input
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
                    // A Pythagorean triple has been found
                    triples.add(new PythagoreanTriple(sideLength1, sideLength2, hypotLengthInt));
                    if (triples.size() == numberOfTriplesToFind) {
                        return triples;
                    }
                }
                sideLength2++;
            }
        }
    }

    private static List<String> getPythagTripleStrings(int anInt) {
        return stringifyElements(getPythagTriples(anInt));
    }

    private static String getListHeading(int input) {
        return String.format(
            "The first %d Pythagorean triples >= %s are:",
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
                String.format("get the first %d Pythagorean triples >= that integer", numberOfTriplesToFind),
                "Pythagorean triples"
            );
        }

        @Override
        public String getCliAnswer(int inputInt) {
            StringJoiner lines =
                new StringJoiner("\n")
                .add(getListHeading(inputInt));

            int i = 1;
            for (String triple : getPythagTripleStrings(inputInt)) {
                lines.add(i + ") " + triple);
                i++;
            }

            return lines.toString();
        }

        @Override
        public List<Component> getGuiComponents(int inputInt) {
            return List.of();
        }
    }
}
