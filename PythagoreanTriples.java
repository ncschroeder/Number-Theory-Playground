import java.util.ArrayList;
import java.util.List;

/**
 * Utility class consisting of methods related to Pythagorean triples.
 */
public class PythagoreanTriples {
    public static String getSectionInfo() {
        return "The Pythagorean theorem says that for the side lengths of a right triangle, " +
                "the sum of the squares of the 2 short sides equals the square of the long side (hypotenuse) " +
                "or a^2 + b^2 = c^2. There are an infinite amount of trios of integers that a, b, and c " +
                "can be. These trios are called Pythagorean triples.";
    }

    /**
     * @return A list of strings that say the first 10 Pythagorean triples that appear after the number passed in as
     * an argument. Each string consists of what the 3 numbers are along with their squares in parentheses.
     */
    public static List<String> getPythagTriplesAfter(int number) {
        List<String> triples = new ArrayList<>(10);

        // sideLength represents one of the lengths of one of the two short sides of a right triangle.
        // 3 is the lowest number to be in a Pythagorean triple so make sideLength be at least that.
        int sideLength = Math.max(number, 3);
        // otherSideLength represents the length of the other short side. This needs to be a long to prevent overflow.
        long otherSideLength = sideLength + 1;

        while (true) {
            double hypotLength = Math.hypot(sideLength, otherSideLength);
            if (hypotLength < otherSideLength + 1) {
                // otherSideLength + 1 is the minimum possible integer value for the hypotenuse length.
                // If the hypotenuse length is less than this, then the max value for otherSideLength for the current
                // value of sideLength has been exceeded.
                sideLength++;
                otherSideLength = sideLength + 1;
            } else {
                if ((int) hypotLength == hypotLength) {
                    // A Pythagorean triple has been found. Add a string to the triples list. This string
                    // consists of what the 3 numbers are and what their squares are in parentheses.
                    triples.add(
                            sideLength + "^2 (" + (sideLength * sideLength) + ") + " + otherSideLength +
                            "^2 (" + (otherSideLength * otherSideLength) + ") = " + (int) hypotLength + "^2 (" +
                            (long) (hypotLength * hypotLength) + ")"
                    );
                    if (triples.size() == 10) {
                        return triples;
                    }
                }
                otherSideLength++;
            }
        }
    }
}