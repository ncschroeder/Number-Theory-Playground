import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class FibonacciLikeSequences {
    private static final String infoString =
        "For any sequence of numbers that starts with 2 numbers and has every following " +
        "term be the sum of the 2 previous terms, as this sequence goes on and on, the " +
        "ratio between consecutive terms gets closer and closer to the Golden Ratio, " +
        "which is approximately " + (1 + Math.sqrt(5)) / 2 + ". The most notable sequence " +
        "of this type is the Fibonacci sequence, whose first 2 numbers are 1 and 1. " +
        "Another notable sequence are the Lucas numbers, whose first 2 numbers are 2 and 1.";

    private final static int sequenceLength = 20;
    public static final int minInputInt = 1;
    public static final int maxInputInt = oneThousand;

    public static class Info {
        private final List<Integer> intSequence;
        private final int secondToLastElement;
        private final int lastElement;

        /**
         * Constructs an object with a sequence that is as long as sequenceLength and with int1 and int2 as
         * the first 2 elements in the sequence.
         */
        public Info(int int1, int int2) {
            assertIsInRange(int1, minInputInt, maxInputInt);
            assertIsInRange(int2, minInputInt, maxInputInt);

            intSequence = new ArrayList<>(sequenceLength);
            intSequence.add(int1);
            intSequence.add(int2);

            do {
                int lastIndex = intSequence.size() - 1;
                int nextElement = intSequence.get(lastIndex - 1) + intSequence.get(lastIndex);
                intSequence.add(nextElement);
            } while (intSequence.size() < sequenceLength);

            secondToLastElement = intSequence.get(sequenceLength - 2);
            lastElement = intSequence.get(sequenceLength - 1);
        }

        /**
         * @return An unmodifiable list of the ints in the sequence.
         */
        public List<Integer> getIntSequence() {
            return List.copyOf(intSequence);
        }

        /**
         * Returns a Stream of the string representations of the numbers in the sequence
         */
        public Stream<String> getStringSequence() {
            return intSequence.stream().map(Misc::stringifyWithCommas);
        }

        /**
         * @return The last element of the sequence divided by the 2nd to last element of the sequence. Should be close
         * to the golden ratio (~1.618).
         */
        public double getEndRatio() {
            return (double) lastElement / secondToLastElement;
        }

        public String getEndRatioMessage() {
            return String.format(
                "%s / %s is approximately %f",
                getLongStringWithCommas(lastElement),
                getLongStringWithCommas(secondToLastElement),
                getEndRatio()
            );
        }
    }

    private static String getListHeading(int inputInt1, int inputInt2) {
        return String.format(
            "The first %d elements in the Fibonacci-like sequence that begins with %s and %s are:",
            sequenceLength,
            getLongStringWithCommas(inputInt1),
            getLongStringWithCommas(inputInt2)
        );
    }

    public static class Section extends DoubleInputSection {
        public Section() {
            super(
                "Fibonacci-Like Sequences",
                List.of(infoString),
                minInputInt,
                maxInputInt,
                String.format(
                    "get the first %d integers in the Fibonacci-like sequence that begins with those integers, " +
                        "as well as the ratio between the last 2 integers in the sequence",
                    sequenceLength
                ),
                "Fibonacci-like sequences"
            );
        }

        @Override
        public String getCliAnswer(int inputInt1, int inputInt2) {
            Info info = new Info(inputInt1, inputInt2);
            String prefix = getListHeading(inputInt1, inputInt2);
            return NTPCLI.stringifyList(info.getStringSequence(), prefix) + "\n\n" + info.getEndRatioMessage();
        }

        @Override
        public List<Component> getGuiComponents(int inputInt1, int inputInt2) {
            Info info = new Info(inputInt1, inputInt2);
            List<Component> listHeadingAndPanel =
                AnswerPanel.createListHeadingAndPanel(getListHeading(inputInt1, inputInt2), info.getStringSequence());
        }
    }
}
