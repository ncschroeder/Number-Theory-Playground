package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;

/**
 * Utility class related to Fibonacci-like sequences and the section for it.
 */
public class FibonacciLikeSequences {
    private static final List<String> infoParagraphs =
        List.of(
            "I consider a number sequence to be \"Fibonacci-like\" if it starts with 2 numbers and has " +
                "every following number be the sum of the 2 previous numbers. The Fibonacci Sequence " +
                "does this and has 1 and 1 as its first 2 numbers. Fibonacci was a mathematician from " +
                "the 1100s to 1200s from modern-day Italy. Another Fibonacci-like sequence are the Lucas " +
                "Numbers, which has 2 and 1 as its first 2 numbers. This sequence was named after 1800s " +
                "French mathematician Francois Edouard Anatole Lucas. Lucas is pronounced like Lucaw.",
            "The Golden Ratio is an irrational number symbolized by the Greek letter Phi. " +
                "Phi = (1 + the square root of 5) / 2, which is approximately " + (1 + Math.sqrt(5)) / 2 + // ~1.618
                ". As we advance further and further into a Fibonacci-like sequence, the ratio between a " +
                "number and the number before it gets closer and closer to Phi. For example, the first 8 " +
                "numbers of the Fibonacci Sequence are 1, 1, 2, 3, 5, 8, 13, and 21. " +
                "2 / 1 = 2. 8 / 5 = 1.6. 21 / 13 is approximately " + (double) 21 / 13 /* ~1.615 */ + "."
        );

    private static final int sequenceLength = 20;
    private static final int minInputInt = 1;
    private static final int maxInputInt = oneThousand;

    public static class Info {
        /**
         * Will be as long as sequenceLength and contain ints in a Fibonacci-like sequence. The first 2
         * ints in this sequence are 2 input ints.
         */
        private final List<Integer> intSequence;

        /**
         * Message about the ratio between the last element and the second to last element in the sequence.
         */
        private final String endRatioMessage;

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
                    "%s / %s is approximately %s.",
                    stringifyWithCommas(lastElement),
                    stringifyWithCommas(secondToLastElement),
                    (double) lastElement / secondToLastElement
                );
        }
    
        /**
         * Returns an unmodifiable list of the ints in the sequence.
         */
        public List<Integer> getIntSequence() {
            return List.copyOf(intSequence);
        }

        /**
         * Returns a Stream of the string representations of the ints in the sequence.
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
            "The first %d numbers in the Fibonacci-like sequence that begins with %s and %s are:",
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
                    "get the first %d numbers in the Fibonacci-like sequence that begins with those integers, " +
                        "as well as the ratio between the last 2 numbers in the sequence",
                    sequenceLength
                ),
                "Fibonacci-like sequences"
            );
        }

        @Override
        public String getCliAnswer(int input1, int input2) {
            var info = new Info(input1, input2);
            return
                NTPCLI.streamToString(getListHeading(input1, input2), info.getStringSequence()) +
                "\n\n" + info.getEndRatioMessage();
        }
    
        @Override
        public List<Component> getGuiComponents(int input1, int input2) {
            var info = new Info(input1, input2);
            List<Component> headingAndTextArea =
                NTPGUI.createStreamHeadingAndTextArea(getListHeading(input1, input2), info.getStringSequence());
                
            return List.of(
                headingAndTextArea.get(0),
                headingAndTextArea.get(1),
                NTPGUI.createCenteredLabel(info.getEndRatioMessage(), NTPGUI.garamondFontSize25)
            );
        }
    }
}
