package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Supplier;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;
import static com.nicholasschroeder.numbertheoryplayground.NTPGUI.*;

/**
 * Utility class related to divisibility and the section for it.
 */
public class Divisibility {
    private static final Supplier<String> introParagraphSupplier = () -> """
Say we have 2 integers that we'll represent with the variables a and b. If we divide a by b and get
no remainder, then a is said to be divisible by b and b is said to be a factor or divisor of a. If
you want to find some factors of an integer, you could manually do some division but there are other
ways to find them.""";
    
    // This section uses prime factorizations so the input constraints for those will be used.
    private static final long MIN_INPUT = PrimeFactorization.MIN_INPUT;
    private static final long MAX_INPUT = PrimeFactorization.MAX_INPUT;
    
    public static boolean isDivisible(long a, long b) {
        return a % b == 0;
    }
    
    public static boolean isEven(long i) {
        return isDivisible(i, 2);
    }
    
    public static boolean isOdd(long i) {
        return !isEven(i);
    }
    
    private static final Supplier<String> rulesInfoSupplier = () -> """
Some rules can be used to find some of the factors of an integer. I'll go over 1 rule for each
integer in the range of 3 to 12, excluding 5 and 10, though there are rules for more integers and
many integers have multiple rules. I'll go over an example of using these rules to find the factors
of an integer in the next paragraph. Let's have a variable i and let it represent an integer. If the
last 2 digits of i is divisible by 4, then i is divisible by 4. If the last 3 digits of i is
divisible by 8, then i is divisible by 8. If the sum of the digits of i is divisible by 3, then i is
divisible by 3. If the sum of the digits of i is divisible by 9, then i is divisible by 9. If i is
even and divisible by 3, then it's also divisible by 6. If i is divisible by both 3 and 4, then it's
also divisible by 12. For 7, we split the integer into blocks of 3 from right to left. Coincidentally,
these are the blocks separated by commas if we write the integer with commas. We do an alternating
sum of the blocks from right to left. We start with 0, add the last block, subtract the second to
last block, add the third to last block, and so on for all the blocks. If this alternating sum is
divisible by 7, then i is divisible by 7. For 11, we do an alternating sum of digits from left to
right. We start with 0, add the first digit, subtract the second digit, add the third digit, and so
on for all digits. If this alternating sum is divisible by 11, then i is divisible by 11.

Here's an example. Let i be 4,695,768. The PF of i is 2^3 x 3^2 x 7^2 x 11^3. We can tell from that
PF that i is divisible by all the numbers mentioned above. Let's check using the rules. The last 2
digits are 68, which is divisible by 4. The last 3 digits are 768, which is divisible by 8. The sum
of the digits is 45, which is divisible by 3 and 9. Since i is even and divisible by 3, it's also
divisible by 6. Since i is divisible by both 3 and 4, it's also divisible by 12. The alternating sum
of blocks of 3 from right to left is 768 - 695 + 4 = 77, which is divisible by 7. The alternating
sum of digits from left to right is 4 - 6 + 9 - 5 + 7 - 6 + 8 = 11, which, of course, is divisible
by 11.""";
    
    public static final class RulesAnswer {
        /**
         * Contains divisibility info for the input long found using the mentioned rules.
         */
        private final String infoParagraph;
        
        private final String inputStringWithCommas;
        
        public final int sumOfDigits;
        
        public final long last2Digits;
        
        public final long last3Digits;
        
        /**
         * Contains text for the alternating sum of blocks of 3 from right to left.
         */
        public StringBuilder alternatingSumOfBlocksSb;
        
        /**
         * Contains text for the alternating sum of digits from left to right.
         */
        public final StringBuilder alternatingSumOfDigitsSb;
        
        public RulesAnswer(long input) {
            assertIsInRange(input, MIN_INPUT, MAX_INPUT);
            
            inputStringWithCommas = toStringWithCommas(input);
            var inputStringWithoutCommas = Long.toString(input);
            var infoSentencesJoiner = new StringJoiner(". ", "", ".");
            last2Digits = input % 100;
            last3Digits = input % 1_000;
            boolean isDivisibleBy4 = isDivisible(last2Digits, 4);
            boolean isEven = isEven(input);
            
            if (!isEven) {
                infoSentencesJoiner.add(
                    inputStringWithCommas + " isn't even so it isn't divisible by any even numbers"
                );
            } else if (input >= 100) {
                infoSentencesJoiner
                .add("The last 2 digits form the integer " + last2Digits)
                .add(getDivisibilitySentence(4, last2Digits, isDivisibleBy4));
                
                if (isDivisibleBy4) {
                    if (input >= 1_000) {
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
                    } else if (input < 100) {
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
    }
    
    private static String getAnswerMainHeading(long input) {
        return "Divisibility Info for " + toStringWithCommas(input);
    }
    
    private static final String RULES_INFO_HEADING = "Rules Info";
    
    private static final String PF_INFO_HEADING = "Prime Factorization Info";

    private static final String FACTORS_AND_PFS_SENTENCE =
        "The factors and their prime factorizations are:";
    
    private static String getPrimeNumberSentence(long input) {
        return
            toStringWithCommas(input) +
            " is prime and doesn't have any factors other than 1 and itself.";
    }
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Divisibility",
                MIN_INPUT,
                MAX_INPUT,
                "get divisibility info about it",
                "divisibility",
                String.join(
                    "\n\n",
                    introParagraphSupplier.get(),
                    PrimeFactorization.factorsInfoSupplier.get(),
                    rulesInfoSupplier.get()
                )
            );
        }
        
        /**
         * Returns a string that shows divisibility info acquired using special tricks followed by info
         * related to prime factorizations.
         */
        @Override
        public String getCliAnswer(long input) {
            // Call PF constructor first to see if it throws.
            var pf = new PrimeFactorization(input);
            
            StringJoiner linesJoiner =
                new StringJoiner("\n")
                .add(getAnswerMainHeading(input))
                .add("");
            
            if (input >= 10) {
                linesJoiner
                .add(RULES_INFO_HEADING)
                .add(NTPCLI.insertNewLines(new RulesAnswer(input).infoParagraph))
                .add("");
            }
            
            linesJoiner.add(PF_INFO_HEADING);
    
            if (pf.isForAPrimeNumber()) {
                linesJoiner.add(
                    NTPCLI.insertNewLines(pf.getInfoSentence() + ' ' + getPrimeNumberSentence(input))
                );
            } else {
                String textAboveFactorPfs =
                    NTPCLI.insertNewLines(
                        pf.getInfoSentence() + ' ' + pf.getNumFactorsInfo() + ' ' + FACTORS_AND_PFS_SENTENCE
                    );
                linesJoiner.add(NTPCLI.streamToString(textAboveFactorPfs, pf.getFactorPfStrings()));
            }
            
            return linesJoiner.toString();
        }
    
        /**
         * Returns a list of GUI components which includes a main heading label, components for divisibility
         * info acquired using special tricks, components for info relating to prime factorizations, and gaps
         * where appropriate.
         */
        @Override
        public List<Component> getGuiComponents(long input) {
            // Call PF constructor first to see if it throws.
            var pf = new PrimeFactorization(input);
            
            var comps = new ArrayList<Component>(10);
            comps.add(createCenteredLabel(getAnswerMainHeading(input), ANSWER_MAIN_HEADING_FONT));
            comps.add(createGap(15));
            
            if (input >= 10) {
                comps.addAll(
                    List.of(
                        createCenteredLabel(RULES_INFO_HEADING, ANSWER_SUB_HEADING_FONT),
                        createGap(5),
                        new NTPTextArea(new RulesAnswer(input).infoParagraph),
                        createGap(10)
                    )
                );
            }
            
            comps.add(createCenteredLabel(PF_INFO_HEADING, ANSWER_SUB_HEADING_FONT));
            
            if (pf.isForAPrimeNumber()) {
                String textToDisplay = pf.getInfoSentence() + ' ' + getPrimeNumberSentence(input);
                comps.add(new NTPTextArea(textToDisplay));
            } else {
                var factorPfsArea = new NTPTextArea(pf.getFactorPfStrings());
                String textAboveFactorPfs =
                    pf.getInfoSentence() + ' ' + pf.getNumFactorsInfo() + ' ' + FACTORS_AND_PFS_SENTENCE;
                comps.add(new NTPTextArea(textAboveFactorPfs));
                comps.add(factorPfsArea);
            }
            
            return comps;
        }
    }
}