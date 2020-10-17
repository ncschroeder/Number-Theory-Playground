/**
 * Utility class consisting of methods related to Pythagorean triples.
 */
public class PythagoreanTriples {
    public static String[] getSectionInfo() {
        return new String[] {
                "The Pythagorean theorem says that for the side lengths of a right triangle,",
                "the sum of the squares of the 2 short sides equals the square of the long side (hypotenuse)",
                "or a^2 + b^2 = c^2. There are an infinite amount of trios of integers that a, b, and c",
                "can be. These trios are called Pythagorean triples."
        };
    }

    /**
     * @return An array of strings that consist of the first 10 pythagorean triples after the argument number.
     * Each pythagorean triple is in it's own string in this array.
     */
    public static String[] getTriples(int number) {
        String[] triples = new String[10];
        int insertionIndex = 0;

        // legLength represents one of the lengths of one of the two short sides of a right triangle.
        // 3 is the lowest number to be in a pythagorean triple so make legLength be at least that.
        int legLength = Math.max(number, 3);
        // otherLeglength represents the length of the other short side
        long otherLegLength = legLength + 1;

        while (true) {
            double hypotLength = Math.hypot(legLength, otherLegLength);
            if (hypotLength < otherLegLength + 1) {
                // otherLegLength + 1 is the minimum possible integer value for the hypotenuse length.
                // If the hypotenuse length is less than this, then the max value for otherLegLength for the current
                // value of legLength has been reached.
                legLength++;
                otherLegLength = legLength + 1;
            } else {
                if ((int) hypotLength == hypotLength) {
                    // A Pythagorean triple has been found. Add a string to the triples array. This string should
                    // consist of what the 3 numbers are and what their squares are in parentheses.
                    triples[insertionIndex] = legLength + "^2 (" + (legLength * legLength) + ") + " + otherLegLength +
                            "^2 (" + (otherLegLength * otherLegLength) + ") = " + (int) hypotLength + "^2 (" +
                            (long) (hypotLength * hypotLength) + ")";
                    insertionIndex++;
                    if (insertionIndex == 10) {
                        return triples;
                    }
                }
                otherLegLength++;
            }
        }
    }
}