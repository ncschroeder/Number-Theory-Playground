import java.util.ArrayList;
import java.util.List;

public class FibonacciLikeSequenceInfo {
    public final static int sequenceLength = 20;
    private final List<Integer> intSequence;
    private final int lastElement;
    private final int secondToLastElement;

    public static String getLabel(String input1, String input2) {
        return String.format(
            "The first %d elements in the Fibonacci-like sequence that begins with %s and %s are:",
            sequenceLength,
            input1,
            input2
        );
    }

    /**
     *
     * @param int1
     * @param int2
     * @throws IllegalArgumentException if int1 or int2 is invalid input for the FIBONACCI_LIKE_SEQUENCES
     * section.
     */
    public FibonacciLikeSequenceInfo(int int1, int int2) {
        if (Section.FIBONACCI_LIKE_SEQUENCES.anyAreInvalidInput(int1, int2)) {
            throw new IllegalArgumentException();
        }

        intSequence = new ArrayList<>(sequenceLength);
        intSequence.add(int1);
        intSequence.add(int2);

        do {
            int lastIndex = intSequence.size() - 1;
            int nextInt = intSequence.get(lastIndex - 1) + intSequence.get(lastIndex);
            intSequence.add(nextInt);
        } while (intSequence.size() < sequenceLength);

        lastElement = intSequence.get(sequenceLength - 1);
        secondToLastElement = intSequence.get(sequenceLength - 2);
    }

    public String getEndRatioMessage() {
        return String.format(
            "%s / %s is approximately %f",
            getLongStringWithCommas(lastElement),
            getLongStringWithCommas(secondToLastElement),
            getEndRatio()
        );
    }

    /**
     *
     * @return An unmodifiable list of the ints in the sequence.
     */
    public List<Integer> getIntSequence() {
        return List.copyOf(intSequence);
    }

    /**
     *
     * @return An unmodifiable list of the elements in the sequence that are stringified with commas.
     */
    public List<String> getStringSequence() {
        return makeElementsStringsWithCommas(intSequence);
    }

    /**
     *
     * @return The last element of the sequence divided by the 2nd to last element of the sequence. Should be close
     * to the golden ratio (~1.618).
     */
    public double getEndRatio() {
        return (double) lastElement / secondToLastElement;
    }
}
