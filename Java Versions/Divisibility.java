import java.awt.Component;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;
import static com.nicholasschroeder.numbertheoryplayground.NTPGUI.*;

/**
 * Utility class related to divisibility and the section for it.
 */
public class Divisibility {
    private static final String introParagraph =
        "Say we have 2 integers that we'll represent with the variables a and b. If we divide a by b and " +
        "get no remainder, then a is said to be divisible by b and b is said to be a factor or divisor " +
        "of a. If you want to find all the factors of an integer, you could manually find all of them but " +
        "there are some other ways to find them.";

    // This section uses prime factorizations so the input constraints for those will be used.
    private static final int minInputInt = PrimeFactorization.minInputInt;
    private static final int maxInputInt = PrimeFactorization.maxInputInt;

    /**
     * Determines if a is divisible by b without using any special tricks.
     */
    public static boolean isDivisible(int a, int b) {
        return a % b == 0;
    }

    public static boolean isEven(int i) {
        return isDivisible(i, 2);
    }

    public static boolean isOdd(int i) {
        return !isEven(i);
    }


    public static class TricksInfo {
        private static final String paragraph =
            "Some special tricks can be used to find some of the factors of a number. Let's have a variable i " +
            "and let it represent an integer. If the sum of the digits of i is divisible by 3, then i is " +
            "divisible by 3. If the sum of the digits of i is divisible by 9, then i is divisible by 9. If i " +
            "is even and divisible by 3, then i is also divisible by 6. If the last 2 digits of i is divisible " +
            "by 4, then i is divisible by 4. If the last 3 digits of i is divisible by 8, then i is divisible " +
            "by 8. If i is divisible by both 3 and 4 then i is also divisible by 12.";
        
        
        private final String info;
        
        private final int sumOfDigits;
        private final int last2Digits;
        private final int last3Digits;
        
        private final boolean isDivisibleBy3;
        private boolean isDivisibleBy4 = false;
        private boolean isDivisibleBy6 = false;
        private boolean isDivisibleBy8 = false;
        private boolean isDivisibleBy9 = false;
        private boolean isDivisibleBy12 = false;
    

        public TricksInfo(int input) {
            assertIsInRange(input, minInputInt, maxInputInt);
            
            String intString = stringifyWithCommas(input);
            var sentencesJoiner = new StringJoiner(". ", "", ".");
            boolean isEven = isEven(input);
            if (!isEven) {
                sentencesJoiner.add(intString + " is not even so it cannot be divisible by any even numbers");
            }
            
            sumOfDigits =
                String.valueOf(input)
                .chars()
                .map(Character::getNumericValue)
                .sum();
            
            isDivisibleBy3 = isDivisible(sumOfDigits, 3);
            sentencesJoiner
            .add("The sum of the digits is " + sumOfDigits)
            .add(
                // This and a few more string creations below insert negations conditionally.
                String.format(
                    "%1$d is %2$sdivisible by 3 so %3$s is %2$sdivisible by 3",
                    sumOfDigits,
                    isDivisibleBy3 ? "" : "not ",
                    intString
                )
            );
            
            if (isDivisibleBy3) {
                isDivisibleBy9 = isDivisible(sumOfDigits, 9);
                sentencesJoiner.add(
                    String.format(
                        "%1$d is %2$sdivisible by 9 so %3$s is %2$sdivisible by 9",
                        sumOfDigits,
                        isDivisibleBy9 ? "" : "not ",
                        intString
                    )
                );
            } else {
                sentencesJoiner.add(
                    String.format(
                        "Since %s is not divisible by 3, it can't be divisible by 6, 9, 12, and any other multiples of 3",
                        intString
                    )
                );
            }
    
            last2Digits = input % 100;
            last3Digits = input % 1000;

            if (isEven) {
                if (isDivisibleBy3) {
                    isDivisibleBy6 = true;
                    sentencesJoiner.add(intString + " is even and divisible by 3 so it's also divisible by 6");
                }
                
                isDivisibleBy4 = isDivisible(last2Digits, 4);
                sentencesJoiner
                .add("The last 2 digits form the number " + last2Digits)
                .add(
                    String.format(
                        "%1$d is %2$sdivisible by 4 so %3$s is %2$sdivisible by 4",
                        last2Digits,
                        isDivisibleBy4 ? "" : "not ",
                        intString
                    )
                );
        
                if (isDivisibleBy4) {
                    isDivisibleBy8 = isDivisible(last3Digits, 8);
                    sentencesJoiner
                    .add("The last 3 digits form the number " + last3Digits)
                    .add(
                        String.format(
                            "%1$d is %2$sdivisible by 8 so %3$s is %2$sdivisible by 8",
                            last3Digits,
                            isDivisibleBy8 ? "" : "not ",
                            intString
                        )
                    );
                    
                    if (isDivisibleBy3) {
                        isDivisibleBy12 = true;
                        sentencesJoiner.add(intString + " is divisible by both 3 and 4 so it's also divisible by 12");
                    }
                } else {
                    sentencesJoiner.add(
                        String.format(
                            "Since %1$s is not divisible by 4, %1$s is also not divisible by 8, 12, and any other multiples of 4",
                            intString
                        )
                    );
                }
            }
            
            info = sentencesJoiner.toString();
        }
        
        @Override
        public String toString() {
            return info;
        }
        
        public int getSumOfDigits() {
            return sumOfDigits;
        }
    
        public int getLast2Digits() {
            return last2Digits;
        }
    
        public int getLast3Digits() {
            return last3Digits;
        }
        
        public boolean isDivisibleBy3() {
            return isDivisibleBy3;
        }
    
        public boolean isDivisibleBy4() {
            return isDivisibleBy4;
        }
        
        public boolean isDivisibleBy6() {
            return isDivisibleBy6;
        }
    
        public boolean isDivisibleBy8() {
            return isDivisibleBy8;
        }
        
        public boolean isDivisibleBy9() {
            return isDivisibleBy9;
        }
        
        public boolean isDivisibleBy12() {
            return isDivisibleBy12;
        }
    }


    private static final String pfInfoParagraph =
        "Another way you can tell what factors a number has and how many factors it has is by looking at " +
        "its prime factorization, or PF. Let's say we have an integer > 1 and we'll represent it with the " +
        "variable i. You can find how many factors i has by looking at i's PF, taking all the powers of the " +
        "prime factors, adding 1 to each, and then multiplying all these together. For example, the prime " +
        "factorization of 294 is 2 x 3 x 7^2. The powers are 1, 1, and 2; so there are 2 x 2 x 3 = 12 factors. " +
        "However, that count includes 1 and the number that the PF is for (294 in this case). If you want to " +
        "exclude those, then subtract 2. That would give us 10 factors. You can find the factors of i by " +
        "finding all the PFs within i's PF, or the \"sub-factorizations\". For 2 x 3 x 7^2, some " +
        "sub-factorizations include 2, 2 x 7, and 3 x 7^2. This means that 2, 14, and 147 are factors of 294.";


    /*
    In order to find sub-factorizations programmatically, you would have to use the factorsAndPowers
    map of a PF object and find all possible combinations of factors and powers. I don't know how to do
    that so I'll just do some iteration to find the factors and then create PF objects from those factors.
    */

    /**
     * Returns an IntStream of the factors of the input besides 1 and the input. This method is called by
     * getFactorPfStrings below and by a unit test.
     */
    public static IntStream getFactors(int input) {
        assertIsInRange(input, minInputInt, maxInputInt);
        return
            IntStream.rangeClosed(2, input / 2)
            .filter(i -> isDivisible(input, i));
    }
    
    /**
     * Returns a Stream of prime factorization strings for all factors of the input besides 1 and the input.
     */
    public static Stream<String> getFactorPfStrings(int input) {
        return
            getFactors(input)
            .mapToObj(i -> new PrimeFactorization(i).toStringWithCorrespondingInt());
    }
    
    private static String getDivisibilityInfoHeading(int input) {
        return "Divisibility Info for " + stringifyWithCommas(input);
    }
    
    private static final String tricksInfoHeading = "Info Acquired Using Special Tricks";
    private static final String pfInfoHeading = "Prime Factorization Info";
    private static final String factorsAndPfsSentence = "The factors and their prime factorizations are:";
    
    private static String getPrimeNumberSentence(int input) {
        return stringifyWithCommas(input) + " is prime and doesn't have any factors other than 1 and itself.";
    }

    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Divisibility",
                List.of(introParagraph, TricksInfo.paragraph, pfInfoParagraph),
                minInputInt,
                maxInputInt,
                "get divisibility info about it",
                "divisibility"
            );
        }
        
        /**
         * Returns a string that shows divisibility info acquired using special tricks followed by info
         * related to prime factorizations.
         */
        @Override
        public String getCliAnswer(int input) {
            var pf = new PrimeFactorization(input);
            String pfInfo;
    
            if (pf.isForAPrimeNumber()) {
                pfInfo = NTPCLI.insertNewLines(pf.getInfoMessage() + ". " + getPrimeNumberSentence(input));
            } else {
                String factorsListPrefix =
                    pf.getInfoMessage() + ". " + pf.getNumberOfFactorsInfo() + " " + factorsAndPfsSentence;
                factorsListPrefix = NTPCLI.insertNewLines(factorsListPrefix);
                pfInfo = NTPCLI.streamToString(factorsListPrefix, getFactorPfStrings(input));
            }
    
            return String.join(
                "\n",
                getDivisibilityInfoHeading(input),
                "",
                tricksInfoHeading,
                NTPCLI.insertNewLines(new TricksInfo(input).toString()),
                "",
                pfInfoHeading,
                pfInfo
            );
        }
    
        /**
         * Returns a list of GUI components which includes a main heading label, components for divisibility
         * info acquired using special tricks, components for info relating to prime factorizations, and gaps
         * where appropriate.
         */
        @Override
        public List<Component> getGuiComponents(int input) {    
            var pf = new PrimeFactorization(input);
            String pfInfo;
            Component emptyComponentOrFactorsTextArea;
            
            if (pf.isForAPrimeNumber()) {
                pfInfo = pf.getInfoMessage() + ". " + getPrimeNumberSentence(input);
                // No factors to display so set this to an empty component.
                emptyComponentOrFactorsTextArea = createGap(0);
            } else {
                pfInfo =
                    pf.getInfoMessage() + ". " + pf.getNumberOfFactorsInfo() + " " + factorsAndPfsSentence;
                emptyComponentOrFactorsTextArea = new NTPTextArea(getFactorPfStrings(input));
            }
            
            return List.of(
                createCenteredLabel(getDivisibilityInfoHeading(input), answerMainHeadingFont),
                createGap(15),
                createCenteredLabel(tricksInfoHeading, answerSubHeadingFont),
                createGap(5),
                new NTPTextArea(new TricksInfo(input).toString()),
                createGap(10),
                createCenteredLabel(pfInfoHeading, answerSubHeadingFont),
                createGap(5),
                new NTPTextArea(pfInfo),
                emptyComponentOrFactorsTextArea
            );
        }
    }
}
