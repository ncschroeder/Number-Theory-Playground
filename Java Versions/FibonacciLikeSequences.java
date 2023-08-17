import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FibonacciLikeSequences {
    private static final List<String> infoParagraphs =
        List.of(
            "I consider a number sequence to be \"Fibonacci-like\" if it starts with 2 numbers and has " +
                "every following element be the sum of the 2 previous elements. The Fibonacci sequence " +
                "does this and has 1 and 1 as its first 2 elements. Fibonacci was a mathematician from " +
                "the 1100s to 1200s from modern-day Italy. Another Fibonacci-like sequence are the Lucas " +
                "numbers, which has 2 and 1 as its first 2 elements. This sequence was named after 1800s " +
                "French mathematician Francois Edouard Anatole Lucas. Lucas is pronounced like Lucaw.",
            "The Golden Ratio is an irrational number symbolized by the Greek letter Phi. " +
                "Phi = (1 + the square root of 5) / 2, which is approximately " + (1 + Math.sqrt(5)) / 2 +
                ". As we advance further and further into a Fibonacci-like sequence, the ratio between an " +
                "element and the element before it gets closer and closer to Phi. For example, the first 8 " +
                "elements of the Fibonacci sequence are 1, 1, 2, 3, 5, 8, 13, and 21. " +
                "2 / 1 = 2. 8 / 5 = 1.6. 21 / 13 is approximately " + (double) 21 / 13 + "."
        );

    private static final int sequenceLength = 20;
    private static final int minInputInt = 1;
    private static final int maxInputInt = oneThousand;

    public static class Info {
        private final List<Integer> intSequence;
        private final String endRatioMessage;

        /**
         * Constructs an object with a sequence that is as long as sequenceLength and with int1 and int2 as
         * the first 2 elements in the sequence.
         */
        public Info(int input1, int input2) {
            assertIsInRange(input1, minInputInt, maxInputInt);
            assertIsInRange(input2, minInputInt, maxInputInt);
        
            intSequence = new ArrayList<>(sequenceLength);
            intSequence.add(input1);
            intSequence.add(input2);
        
            do {
                int lastIndex = intSequence.size() - 1;
                int nextElement = intSequence.get(lastIndex - 1) + intSequence.get(lastIndex);
                intSequence.add(nextElement);
            } while (intSequence.size() < sequenceLength);
        
            int secondToLastElement = intSequence.get(sequenceLength - 2);
            int lastElement = intSequence.get(sequenceLength - 1);
            endRatioMessage =
                String.format(
                    "%s / %s is approximately %f",
                    stringifyWithCommas(lastElement),
                    stringifyWithCommas(secondToLastElement),
                    (double) lastElement / secondToLastElement
                );
        }
    
        /**
         * Returns an unmodifiable list of the ints in the sequence
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

        public String getEndRatioMessage() {
            return endRatioMessage;
        }
    }

    private static String getListHeading(int input1, int input2) {
        return String.format(
            "The first %d elements in the Fibonacci-like sequence that begins with %s and %s are:",
            sequenceLength,
            stringifyWithCommas(input1),
            stringifyWithCommas(input2)
        );
    }

    public static class Section extends DoubleInputSection {
        public Section() {
            super(
                "Fibonacci-Like Sequences",
                infoParagraphs,
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
