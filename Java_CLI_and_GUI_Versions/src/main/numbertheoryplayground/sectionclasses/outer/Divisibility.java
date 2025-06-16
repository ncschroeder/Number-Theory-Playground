package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.gui.NtpGui.*;

/**
 * Utility class related to divisibility and the section for it.
 */
public class Divisibility {
    private static final String INFO = """
Say we have 2 whole numbers that we'll represent with the variables a and b. If we divide a by
b and get no remainder, then a is said to be divisible by b and b is said to be a factor or
divisor of a. If you want to find some whole number factors of a whole number, you could
manually do some division but there are other ways to find them.

%s

Some rules can be used to determine if a whole number is divisible by another whole number.
I'll go over 1 rule for each whole number in the range of 3 to 12, excluding 5 and 10, though
there are rules for more whole numbers and many whole numbers have multiple rules. I'll go over
an example of using these rules to find the factors of a whole number in the next paragraph.
Let's have a variable n and let it represent an whole number. If the last 2 digits of n is
divisible by 4, then n is divisible by 4. If the last 3 digits of n is divisible by 8, then n
is divisible by 8. If the sum of the digits of n is divisible by 3, then n is divisible by 3.
If the sum of the digits of n is divisible by 9, then n is divisible by 9. If n is even and
divisible by 3, then it's also divisible by 6. If n is divisible by both 3 and 4, then it's
also divisible by 12. For 7, we split n into blocks of 3 from right to left. Coincidentally,
these are the blocks separated by commas if we write n with commas. We do an alternating sum of
the blocks from right to left. We start with 0, add the last block, subtract the second to last
block, add the third to last block, and so on for all the blocks. If this alternating sum is
divisible by 7, then n is divisible by 7. For 11, we do an alternating sum of digits of n from
left to right. We start with 0, add the first digit, subtract the second digit, add the third
digit, and so on for all digits. If this alternating sum is divisible by 11, then n is
divisible by 11.

Here's an example. Let n be 4,695,768. The PF of n is 2^3 × 3^2 × 7^2 × 11^3. We can tell from
that PF that n is divisible by all the numbers that had rules mentioned about them above. Let's
check using the rules. The last 2 digits are 68, which is divisible by 4. The last 3 digits are
768, which is divisible by 8. The sum of the digits is 45, which is divisible by 3 and 9. Since
n is even and divisible by 3, it's also divisible by 6. Since n is divisible by both 3 and 4,
it's also divisible by 12. The alternating sum of blocks of 3 from right to left is
768 - 695 + 4 = 77, which is divisible by 7. The alternating sum of digits from left to right
is 4 - 6 + 9 - 5 + 7 - 6 + 8 = 11, which, of course, is divisible by 11."""
        .formatted(PrimeFactorization.FACTORS_INFO);
    
    static boolean isDivisible(long a, long b) {
        return a % b == 0;
    }
    
    public static boolean isEven(long l) {
        return isDivisible(l, 2);
    }
    
    static boolean isOdd(long l) {
        return !isEven(l);
    }
    
    // This section uses prime factorizations so the input constraints for those will be used.
    private static final long MIN_INPUT = PrimeFactorization.MIN_INPUT;
    private static final long MAX_INPUT = PrimeFactorization.MAX_INPUT;
    static final class RulesAnswer {
        /**
         * Contains divisibility info for the input long found using the mentioned rules.
         */
        private final String infoParagraph;
        private final String inputStringWithCommas;
        private final long last2Digits;
        private final long last3Digits;
        private final int sumOfDigits;
        /**
         * Contains text for the alternating sum of blocks of 3 from right to left.
         */
        private final StringBuilder blocksOf3AltSumExpressionBuilder;
        /**
         * Contains text for the alternating sum of digits from left to right.
         */
        
        private final StringBuilder digitsAltSumExpressionBuilder;
        RulesAnswer(long inputLong, String inputStringWithCommas) {
            assertIsInRange(inputLong, MIN_INPUT, MAX_INPUT);
            
            var inputStringWithoutCommas = Long.toString(inputLong);
            var infoSentencesJoiner = new StringJoiner(". ", "", ".");
            last2Digits = inputLong % 100;
            last3Digits = inputLong % 1_000;
            boolean isDivisibleBy4 = isDivisible(last2Digits, 4);
            boolean isEven = isEven(inputLong);
            
            if (!isEven) {
                infoSentencesJoiner.add(
                    inputStringWithCommas + " isn't even so it isn't divisible by any even numbers"
                );
            } else if (inputLong >= 100) {
                infoSentencesJoiner
                .add("The last 2 digits form the integer " + last2Digits)
                .add(getDivisibilitySentence(4, last2Digits, isDivisibleBy4));
                
                if (isDivisibleBy4) {
                    if (inputLong >= 1_000) {
                        boolean isDivisibleBy8 = isDivisible(last3Digits, 8);
                        
                        infoSentencesJoiner
                        .add("The last 3 digits form the integer " + last3Digits)
                        .add(getDivisibilitySentence(8, last3Digits, isDivisibleBy8));
                    }
                } else {
                    infoSentencesJoiner.add(
                        String.format(
                            "Since %s isn't divisible by 4, it's also not divisible by 8, 12, and " +
                                "any other multiples of 4",
                            inputStringWithCommas
                        )
                    );
                }
            }
            
            sumOfDigits =
                inputStringWithoutCommas
                .chars()
                .map(Character::getNumericValue)
                .sum();
            
            boolean isDivisibleBy3 = isDivisible(sumOfDigits, 3);
            
            infoSentencesJoiner
            .add("The sum of the digits is " + sumOfDigits)
            .add(getDivisibilitySentence(3, sumOfDigits, isDivisibleBy3));
            
            if (isDivisibleBy3) {
                if (isEven) {
                    infoSentencesJoiner.add(
                        inputStringWithCommas + " is even and divisible by 3 so it's also " +
                        "divisible by 6"
                    );
                    
                    if (isDivisibleBy4) {
                        infoSentencesJoiner.add(
                            inputStringWithCommas + " is divisible by both 3 and 4 so it's also " +
                            "divisible by 12"
                        );
                    } else if (inputLong < 100) {
                        infoSentencesJoiner.add(
                            inputStringWithCommas + " isn't divisible by 4 so it's not divisible by 12"
                        );
                    }
                }
                
                boolean isDivisibleBy9 = isDivisible(sumOfDigits, 9);
                infoSentencesJoiner.add(getDivisibilitySentence(9, sumOfDigits, isDivisibleBy9));
            } else {
                infoSentencesJoiner.add(
                    String.format(
                        "Since %s isn't divisible by 3, it's also not divisible by 6, 9, 12, and " +
                            "any other multiples of 3",
                        inputStringWithCommas
                    )
                );
            }
            
            if (input >= 1_000) {
                String[] blocksOf3 = inputStringWithCommas.split(",");
                var alternatingSumOfBlocks = 0;
                alternatingSumOfBlocksSb = new StringBuilder();
                var add = true;
                
                for (var i = blocksOf3.length - 1; i >= 0; i--) {
                    var blockInt = Integer.parseInt(blocksOf3[i]);
                    alternatingSumOfBlocks += add ? blockInt : -blockInt;
                    if (!alternatingSumOfBlocksSb.isEmpty()) {
                        alternatingSumOfBlocksSb.append(add ? " + " : " - ");
                    }
                    alternatingSumOfBlocksSb.append(blockInt);
                    add = !add;
                }
                
                alternatingSumOfBlocksSb.append(" = ").append(toStringWithCommas(alternatingSumOfBlocks));
                boolean isDivisibleBy7 = isDivisible(alternatingSumOfBlocks, 7);
                
                infoSentencesJoiner
                .add("The alternating sum of blocks of 3 from right to left is " + alternatingSumOfBlocksSb)
                .add(getDivisibilitySentence(7, alternatingSumOfBlocks, isDivisibleBy7));
            }
            
            var alternatingSumOfDigits = 0;
            alternatingSumOfDigitsSb = new StringBuilder();
            var add = true;
            
            for (var i = 0; i < inputStringWithoutCommas.length(); i++) {
                int digit = Character.getNumericValue(inputStringWithoutCommas.charAt(i));
                alternatingSumOfDigits += add ? digit : -digit;
                if (!alternatingSumOfDigitsSb.isEmpty()) {
                    alternatingSumOfDigitsSb.append(add ? " + " : " - ");
                }
                alternatingSumOfDigitsSb.append(digit);
                add = !add;
            }
            
            alternatingSumOfDigitsSb.append(" = ").append(alternatingSumOfDigits);
            boolean isDivisibleBy11 = isDivisible(alternatingSumOfDigits, 11);
            
            infoSentencesJoiner
            .add("The alternating sum of digits from left to right is " + alternatingSumOfDigitsSb)
            .add(getDivisibilitySentence(11, alternatingSumOfDigits, isDivisibleBy11));
            
            infoParagraph = infoSentencesJoiner.toString();
        }
        
        /**
         * For all the rules besides the ones for 6 and 12, we do a calculation with the input long
         * and if the result of that calculation is divisible by a certain integer, then the input
         * long is also divisible by that integer.
         */
        private String getDivisibilitySentence(
            int possibleFactor,
            long longFromCalculation,
            boolean isDivisible
        ) {
            return String.format(
                "%1$s %2$s divisible by %3$s so %4$s %2$s divisible by %3$s",
                toStringWithCommas(longFromCalculation),
                isDivisible ? "is" : "isn't",
                possibleFactor,
                inputStringWithCommas
            );
        }
        
        int getSumOfDigits() {
            return sumOfDigits;
        }
        
        long getLast2Digits() {
            return last2Digits;
        }
        
        long getLast3Digits() {
            return last3Digits;
        }
        
        /**
         * Throws NullPointerException if blocksOf3AltSumExpressionBuilder is null.
         */
        String getBlocksOf3AltSumExpression() {
            return blocksOf3AltSumExpressionBuilder.toString();
        }
        
        String getDigitsAltSumExpression() {
            return digitsAltSumExpressionBuilder.toString();
        }
    }
    
    private static String getAnswerMainHeading(String inputString) {
        return "Divisibility Info for " + inputString;
    }
    
    private static final String RULES_INFO_HEADING = "Rules Info";
    
    private static final String PF_INFO_HEADING = "Prime Factorization Info";
    
    private static final String FACTORS_AND_PFS_SENTENCE =
        "The factors and their prime factorizations are:";
    
    private static String getPrimeNumberSentence(String inputString) {
        return inputString + " is prime and doesn't have any factors other than 1 and itself.";
    }
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Divisibility",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                "divisibility info for that number",
                "divisibility"
            );
        }
        
        /**
         * Returns a string that shows divisibility info acquired using special tricks followed by info
         * related to prime factorizations.
         */
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            // Call PF constructor first to see if it throws.
            var pf = new PrimeFactorization(inputLong, inputString);
            
            StringJoiner linesJoiner =
                new StringJoiner("\n")
                .add(getAnswerMainHeading(inputString))
                .add("");
            
            if (inputLong >= 10) {
                String rulesInfo = new RulesAnswer(inputLong, inputString).infoParagraph;
                
                linesJoiner
                .add(RULES_INFO_HEADING)
                .add(NtpCli.putNewLineChars(rulesInfo))
                .add("");
            }
            
            linesJoiner.add(PF_INFO_HEADING);
            
            if (pf.isForAPrimeNumber()) {
                linesJoiner.add(
                    NtpCli.putNewLineChars(pf.getInfoSentence() + ' ' + getPrimeNumberSentence(inputString))
                );
            } else {
                String textAboveFactorPfs =
                    NtpCli.putNewLineChars(
                        pf.getInfoSentence() + ' ' + pf.getNumFactorsInfo() + ' ' + FACTORS_AND_PFS_SENTENCE
                    );
                linesJoiner.add(
                    NtpCli.buildStringWithStreamElementsOnLongLines(textAboveFactorPfs, pf.getFactorPfStrings())
                );
            }
            
            return linesJoiner.toString();
        }
        
        /**
         * Returns a list of GUI components which includes a main heading label, components for divisibility
         * info acquired using special tricks, components for info relating to prime factorizations, and gaps
         * where appropriate.
         */
        @Override
        public List<Component> getGuiComponents(long inputLong, String inputString) {
            // Call PF constructor first to see if it throws.
            var pf = new PrimeFactorization(inputLong, inputString);
            
            var comps = new ArrayList<Component>(10);
            comps.add(createAnswerMainHeadingLabel(getAnswerMainHeading(inputString)));
            comps.add(createGapBetweenAnswerSections());
            
            if (inputLong >= 10) {
                String rulesInfo = new RulesAnswer(inputLong, inputString).infoParagraph;
                comps.addAll(
                    List.of(
                        createAnswerSubHeadingLabel(RULES_INFO_HEADING),
                        createGap(5),
                        new NtpTextArea(rulesInfo),
                        createGapBetweenAnswerSections()
                    )
                );
            }
            
            comps.add(createAnswerSubHeadingLabel(PF_INFO_HEADING));
            
            if (pf.isForAPrimeNumber()) {
                String textToDisplay = pf.getInfoSentence() + ' ' + getPrimeNumberSentence(inputString);
                comps.add(new NtpTextArea(textToDisplay));
            } else {
                try {
                    var factorPfsArea =
                        NtpTextArea.createWideOneWithStreamElements(pf.getFactorPfStrings());
                    String textAboveFactorPfs =
                        pf.getInfoSentence() + ' ' + pf.getNumFactorsInfo() + ' ' +
                        FACTORS_AND_PFS_SENTENCE;
                    comps.add(new NtpTextArea(textAboveFactorPfs));
                    comps.add(factorPfsArea);
                } catch (NtpTextArea.StringTooLongException ex) {
                    String textToDisplay =
                        pf.getInfoSentence() + ' ' + pf.getNumFactorsInfo() + ' ' +
                        NtpTextArea.StringTooLongException.ERROR_MESSAGE;
                    comps.add(new NtpTextArea(textToDisplay));
                }
            }
            
            return comps;
        }
    }
}
