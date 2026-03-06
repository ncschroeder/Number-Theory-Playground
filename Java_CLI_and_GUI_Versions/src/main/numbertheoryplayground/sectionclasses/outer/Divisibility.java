package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.gui.NtpGui.*;
import static numbertheoryplayground.sectionclasses.outer.PrimeFactorization.FactorAndPower;

/**
 * Utility class related to divisibility and the section for it.
 */
public class Divisibility {
    private static final String INTRO_INFO = """
Say we have 2 whole numbers that we'll represent with the variables a and b. If we divide a by
b and get no remainder, then a is said to be divisible by b and b is said to be a factor or
divisor of a. If you want to find some whole number factors of a whole number, you could
manually do some division but there are other ways to find them.""";
    
    /*
    Given an input number, the calculations for this section are:
    1. Use the divisibility rules to see if we can find some factors of the number and build a
       paragraph with info from this.
    2. Find the PF of the number. If we can determine from this PF that the input number is
       composite (not prime), then find the factors of the input number by finding the
       sub-factorizations of the PF.
     */
    
    // This section uses prime factorizations so the max input for those will be used.
    private static final long MIN_INPUT = 10;
    private static final long MAX_INPUT = PrimeFactorization.MAX_INPUT;
    
    static boolean isDivisible(long a, long b) {
        return a % b == 0;
    }
    
    public static boolean isEven(long l) {
        return isDivisible(l, 2);
    }
    
    static boolean isOdd(long l) {
        return !isEven(l);
    }
    
    
    private static final String RULES_INFO = """
Some rules can be used to determine if a whole number is divisible by another whole number.
I'll go over 1 rule for each number in the range of 3 to 12, excluding 5 and 10, though there
are rules for more numbers and many numbers have multiple rules. I'll go over an example of
using these rules to find the factors of a number in a few paragraphs. Let's have a variable n
and let it represent an whole number. If the number formed from the last 2 digits of n is
divisible by 4, then n is divisible by 4. If the number formed from the last 3 digits of n is
divisible by 8, then n is divisible by 8. If the sum of the digits of n is divisible by 3,
then n is divisible by 3. If the sum of the digits of n is divisible by 9, then n is divisible
by 9. If n is even and divisible by 3, then it's also divisible by 6. If n is divisible by
both 3 and 4, then it's also divisible by 12.

For 11, we do an alternating sum of the digits of n from left to right. We start with 0, add
the first digit, subtract the second digit, add the third digit, and so on for all digits. If
this sum is divisible by 11, then n is divisible by 11.

For 7, we split n into 3-digit blocks of 3 from right to left, though the leftmost block can
contain 1 or 2 digits. Coincidentally, these are the blocks separated by commas if we write n
with commas. We do an alternating sum of the blocks from right to left. We start with 0, add
the rightmost block, subtract the block to the left of that, add the block to the left of that,
and so on for all the blocks. If this sum is divisible by 7, then n is divisible by 7. These
alternating sums might involve negative integers or 0, so that makes them some of the few
calculations done by the Number Theory Playground that involve numbers other than natural numbers.

Here's an example. Let n be 4,695,768. The PF of n is 2^3 × 3^2 × 7^2 × 11^3. We can tell from
that PF that n is divisible by all the numbers that had rules mentioned about them above. Let's
check using the rules. The last 2 digits are 68, which is divisible by 4. The last 3 digits are
768, which is divisible by 8. The sum of the digits is 4 + 6 + 9 + 5 + 7 + 6 + 8 = 45, which is
divisible by both 3 and 9. Since n is even and divisible by 3, it's also divisible by 6. Since
n is divisible by both 3 and 4, it's also divisible by 12. The alternating sum of digits from
left to right is 4 - 6 + 9 - 5 + 7 - 6 + 8 = 11, which, of course, is divisible by 11. The
alternating sum of 3-digit blocks from right to left is 768 - 695 + 4 = 77, which is divisible
by 7.""";
    
    /**
     * Class with data related to rules that can be used to determine if the input long is
     * divisible by other whole numbers.
     */
    static final class RulesAnswer {
        /**
         * Contains divisibility info for the input found using the mentioned rules.
         */
        private final String infoParagraph;
        
        private final long last2Digits;
        
        private final long last3Digits;
        
        // 4,695,768 is the number used in the example above and the doc comments below.
        
        /**
         * Contains text for a mathematical expression of the sum of digits from left to right.
         * For 4,695,768, this would be "4 + 6 + 9 + 5 + 7 + 6 + 8 = 45".
         */
        private final StringBuilder sumOfDigitsExpressionBuilder;
        
        /**
         * Contains text for a mathematical expression of the alternating sum of digits from
         * left to right. For 4,695,768, this would be "4 - 6 + 9 - 5 + 7 - 6 + 8 = 11".
         */
        private final StringBuilder digitsAltSumExpressionBuilder;
        
        /**
         * Contains text for a mathematical expression of the alternating sum of 3-digit
         * blocks from right to left. For 4,695,768, this would be "768 - 695 + 4 = 77".
         */
        private final StringBuilder blocksAltSumExpressionBuilder;
        
        RulesAnswer(long inputLong, String inputStringWithCommas) {
            assertIsInRange(inputLong, MIN_INPUT, MAX_INPUT);
            
            /*
            For all the rules besides the ones for 6 and 12, we do a calculation with the input long
            and if the result of that calculation is divisible by a certain integer, then the input
            long is also divisible by that integer.
             */
            BiFunction<Integer, Boolean, String> getDivisSentenceEnd =
                (possibleFactor, isDivisible) ->
                    String.format(
                        ", which %1$s divisible by %2$s so %3$s %1$s divisible by %2$s",
                        isDivisible ? "is" : "isn't",
                        possibleFactor,
                        inputStringWithCommas
                    );
            
            var infoSentencesJoiner = new StringJoiner(". ", "", ".");
            last2Digits = inputLong % 100;
            last3Digits = inputLong % 1_000;
            boolean isDivisibleBy4 = isDivisible(last2Digits, 4);
            
            if (!isEven(inputLong)) {
                infoSentencesJoiner.add(
                    inputStringWithCommas + " isn't even so it isn't divisible by any even numbers"
                );
            } else if (inputLong >= 100) {
                infoSentencesJoiner.add(
                    "The last 2 digits form the number " + last2Digits +
                    getDivisSentenceEnd.apply(4, isDivisibleBy4)
                );
                
                if (isDivisibleBy4) {
                    if (inputLong >= 1_000) {
                        boolean isDivisibleBy8 = isDivisible(last3Digits, 8);
                        infoSentencesJoiner.add(
                            "The last 3 digits form the number " + last3Digits +
                            getDivisSentenceEnd.apply(8, isDivisibleBy8)
                        );
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
            
            var inputStringWithoutCommas = Long.toString(inputLong);
            sumOfDigitsExpressionBuilder = new StringBuilder();
            var sumOfDigits = 0;
            digitsAltSumExpressionBuilder = new StringBuilder();
            var digitsAltSum = 0;
            var addForAltSum = true;
            
            for (var i = 0; i < inputStringWithoutCommas.length(); i++) {
                if (!sumOfDigitsExpressionBuilder.isEmpty()) {
                    sumOfDigitsExpressionBuilder.append(" + ");
                    digitsAltSumExpressionBuilder.append(addForAltSum ? " + " : " − ");
                }
                
                var digitChar = inputStringWithoutCommas.charAt(i);
                sumOfDigitsExpressionBuilder.append(digitChar);
                digitsAltSumExpressionBuilder.append(digitChar);
                var digitInt = Character.getNumericValue(digitChar);
                sumOfDigits += digitInt;
                digitsAltSum += addForAltSum ? digitInt : -digitInt;
                addForAltSum = !addForAltSum;
            }
            
            sumOfDigitsExpressionBuilder.append(" = ").append(sumOfDigits);
            digitsAltSumExpressionBuilder.append(" = ").append(digitsAltSum);
            
            boolean isDivisibleBy3 = isDivisible(sumOfDigits, 3);
            infoSentencesJoiner.add(
                "The sum of the digits is " + sumOfDigitsExpressionBuilder +
                getDivisSentenceEnd.apply(3, isDivisibleBy3)
            );
            
            if (isDivisibleBy3) {
                boolean isDivisibleBy9 = isDivisible(sumOfDigits, 9);
                infoSentencesJoiner.add(
                    String.format(
                        "%1$d %2$s divisible by 9 so %3$s %2$s divisible by 9",
                        sumOfDigits,
                        isDivisibleBy9 ? "is" : "isn't",
                        inputStringWithCommas
                    )
                );
                
                if (isEven(inputLong)) {
                    infoSentencesJoiner.add(
                        inputStringWithCommas +
                        " is even and divisible by 3 so it's also divisible by 6"
                    );
                    
                    if (isDivisibleBy4) {
                        infoSentencesJoiner.add(
                            inputStringWithCommas +
                            " is divisible by both 3 and 4 so it's also divisible by 12"
                        );
                    } else if (inputLong < 100) {
                        infoSentencesJoiner.add(
                            inputStringWithCommas +
                            " isn't divisible by 4 so isn't divisible by 12"
                        );
                    }
                }
            } else {
                infoSentencesJoiner.add(
                    String.format(
                        "Since %s isn't divisible by 3, it's also not divisible by 6, 9, 12, and " +
                            "any other multiples of 3",
                        inputStringWithCommas
                    )
                );
            }
            
            boolean isDivisibleBy11 = isDivisible(digitsAltSum, 11);
            infoSentencesJoiner.add(
                "The alternating sum of digits from left to right is " +
                digitsAltSumExpressionBuilder + getDivisSentenceEnd.apply(11, isDivisibleBy11)
            );
            
            if (inputLong < 1_000) {
                blocksAltSumExpressionBuilder = null;
            } else {
                blocksAltSumExpressionBuilder = new StringBuilder();
                var blocksAltSum = 0;
                String[] threeDigitBlocks = inputStringWithCommas.split(",");
                addForAltSum = true;
                
                for (int i = threeDigitBlocks.length - 1; i >= 0; i--) {
                    if (!blocksAltSumExpressionBuilder.isEmpty()) {
                        blocksAltSumExpressionBuilder.append(addForAltSum ? " + " : " − ");
                    }
                    
                    var blockString = threeDigitBlocks[i];
                    blocksAltSumExpressionBuilder.append(blockString);
                    var blockInt = Integer.parseInt(blockString);
                    blocksAltSum += addForAltSum ? blockInt : -blockInt;
                    addForAltSum = !addForAltSum;
                }
                
                blocksAltSumExpressionBuilder
                .append(" = ")
                .append(createStringWithCommas(blocksAltSum));
                
                boolean isDivisibleBy7 = isDivisible(blocksAltSum, 7);
                infoSentencesJoiner.add(
                    "The alternating sum of 3-digit blocks from right to left is " +
                    blocksAltSumExpressionBuilder + getDivisSentenceEnd.apply(7, isDivisibleBy7)
                );
            }
            
            infoParagraph = infoSentencesJoiner.toString();
        }
        
        long getLast2Digits() {
            return last2Digits;
        }
        
        long getLast3Digits() {
            return last3Digits;
        }
        
        String getSumOfDigitsExpression() {
            return sumOfDigitsExpressionBuilder.toString();
        }
        
        String getDigitsAltSumExpression() {
            return digitsAltSumExpressionBuilder.toString();
        }
        
        /**
         * Throws NullPointerException if blocksAltSumExpressionBuilder is null.
         */
        String getBlocksAltSumExpression() {
            return blocksAltSumExpressionBuilder.toString();
        }
    }
    
    
    private static final String PF_INFO = """
The factors of a whole number > 1 can be found by looking at its prime factorization (PF). Let's
have a variable n and let it represent a whole number > 1. First, you can find how many factors
n has by looking at n's PF, taking all the powers of the factors, adding 1 to each, and then
multiplying all these together. For example, the PF of 36 is 2^2 × 3^2. The powers are 2 and 2,
so there are 3 × 3 = 9 factors. However, that count includes 1 and the number that the PF is for
(36 in this case). If you want to exclude those, then subtract 2. That would give us 7 factors.
You can find the factors of n by finding all the PFs within n's PF, or the "sub-factorizations,"
as I like to call them. For 2^2 × 3^2, the sub-factorizations are
2, 3, 2^2 (4), 2 × 3 (6), 3^2 (9), 2^2 × 3 (12), and 2 × 3^2 (18).

Whole numbers that are ≤ the max input of this section generally have a pretty small amount of
factors, like < 100. An example of an input number with a high number of factors is
9,736,008,432,870,720, or 9 quadrillion 736 trillion ... This number is the product of 2^6 and
the next 12 prime numbers so it has 13 unique prime factors and its PF is
2^6 x 3 x 5 x 7 x 11 x 13 x 17 x 19 x 23 x 29 x 31 x 37 x 41. It has 7 × 2^12 = 28,672 total
factors!""";
    
    static class NumberOfFactorsAndExpression {
        /**
         * The number of factors of the corresponding big int of the input PF, or the input
         * long for getCliAnswer or getGuiComponents since those are the methods that call
         * getNumFactorsInfo which then calls the constructor for this class.
         */
        private int numFactors;
        
        /**
         * A mathematical expression that shows how the number of factors was calculated.
         * For the input long 8,575, which has a PF of 5^2 × 7^3, this would be
         * "(2 + 1) × (3 + 1)" and numFactors would be 12.
         */
        private final String expression;
        
        NumberOfFactorsAndExpression(PrimeFactorization pf) {
            numFactors = 1;
            var powerStrings = new ArrayList<String>();
            
            for (FactorAndPower fp : pf.getFps()) {
                numFactors *= fp.power() + 1;
                powerStrings.add(String.format("(%d + 1)", fp.power()));
            }
            
            expression = String.join(" × ", powerStrings);
        }
        
        int getNumFactors() {
            return numFactors;
        }
        
        String getExpression() {
            return expression;
        }
    }
    
    private static String getNumFactorsInfo(PrimeFactorization pf) {
        var numFactorsAndExpression = new NumberOfFactorsAndExpression(pf);
        int numFactors = numFactorsAndExpression.numFactors;
        var infoEnd =
            "there" + (
                numFactors == 3
                ? "'s 1 factor"
                : String.format(" are %s factors", createStringWithCommas(numFactors - 2))
            );
        
        return String.format(
            "By looking at the power%s, we can see there are %s = %s factors. If 1 and %s are " +
                "excluded, then %s.",
            pf.getFps().size() == 1 ? "" : "s",
            numFactorsAndExpression.expression,
            createStringWithCommas(numFactors),
            pf.getCorrespondingBigIntString(),
            infoEnd
        );
    }
    
    /**
     * This method finds PFs of factors of the input PF's corresponding big int, excluding 1
     * and the corresponding big int, by finding sub-factorizations in the input PF and that's
     * done by finding combinations of factors and powers in that PF. The PFs in the list
     * returned are sorted by corresponding big ints.
     */
    static List<PrimeFactorization> getFactorPfs(PrimeFactorization pf) {
        var factorPfs = new ArrayList<PrimeFactorization>();
        
        for (FactorAndPower fp : pf.getFps()) {
            long factor = fp.factor();
            int inputPfPower = fp.power();
            
            /*
            In the 2nd for loop below, we want to iterate through all the PFs that are in factorPfs
            at this point, and not the ones that get added below. Use this variable for that.
             */
            int lastPfIndexToUse = factorPfs.size() - 1;
            
            for (var factorPfPower = 1; factorPfPower <= inputPfPower; factorPfPower++) {
                var singleton = List.of(new FactorAndPower(factor, factorPfPower));
                factorPfs.add(new PrimeFactorization(singleton));
                
                for (var i = 0; i <= lastPfIndexToUse; i++) {
                    var newFp = new FactorAndPower(factor, factorPfPower);
                    List<FactorAndPower> factorPfFps = new ArrayList<>(factorPfs.get(i).getFps());
                    
                    findIndexOfFactor(factorPfFps, factor)
                    .ifPresentOrElse(
                        indexToUpdate -> factorPfFps.set(indexToUpdate, newFp),
                        () -> factorPfFps.add(newFp)
                    );
                    
                    factorPfs.add(new PrimeFactorization(factorPfFps));
                }
            }
        }
        
        /*
        The last PF has all the factors that this PF has and each power is the same as the powers in
        this PF, so it's the same as this PF. We don't want to include that as part of the factors.
         */
        factorPfs.removeLast();
        factorPfs.sort(Comparator.comparing(PrimeFactorization::getCorrespondingBigInt));
        return factorPfs;
    }
    
    private static OptionalInt findIndexOfFactor(List<FactorAndPower> fps, long factor) {
        return
            IntStream.range(0, fps.size())
            .filter(i -> fps.get(i).factor() == factor)
            .findFirst();
    }
    
    private static Stream<String> getFactorPfStrings(PrimeFactorization pf) {
        return
            getFactorPfs(pf)
            .stream()
            .map(pf2 ->
                pf2.isForAPrimeNumber()
                ? pf2.getCorrespondingBigIntString()
                : String.format("%s (%s)", pf2, pf2.getCorrespondingBigIntString())
            );
    }
    
    
    private static String getAnswerMainHeading(String inputString) {
        return "Divisibility Info for " + inputString;
    }
    
    private static final String RULES_INFO_HEADING = "Rules Info";
    
    private static final String PF_INFO_HEADING = "Prime Factorization Info";
    
    private static final String FACTORS_AND_PFS_SENTENCE = "The factors and their PFs are:";
    
    private static String getPrimeNumberSentence(String inputString) {
        return
            inputString +
            " is prime and doesn't have any whole number factors other than 1 and itself.";
    }
    
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Divisibility",
                String.join("\n\n", INTRO_INFO, PF_INFO, RULES_INFO),
                MIN_INPUT,
                MAX_INPUT,
                "divisibility info for that number",
                "divisibility"
            );
        }
        
        /**
         * Returns a string with a main heading, divisibility rules info, and PF info.
         */
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            String rulesInfo = new RulesAnswer(inputLong, inputString).infoParagraph;
            var pf = new PrimeFactorization(inputLong, inputString);
            
            var linesJoiner =
                new StringJoiner("\n")
                .add(getAnswerMainHeading(inputString))
                .add("")
                .add(RULES_INFO_HEADING)
                .add(NtpCli.putNewLineChars(rulesInfo))
                .add("")
                .add(PF_INFO_HEADING);
            
            if (pf.isForAPrimeNumber()) {
                linesJoiner.add(
                    NtpCli.putNewLineChars(
                        pf.getInfoSentence() + ' ' + getPrimeNumberSentence(inputString)
                    )
                );
            } else {
                String textAboveFactorPfs =
                    NtpCli.putNewLineChars(
                        pf.getInfoSentence() + ' ' + getNumFactorsInfo(pf) +
                        ' ' + FACTORS_AND_PFS_SENTENCE
                    );
                linesJoiner.add(
                    NtpCli.buildStringWithStreamElementsOnLongLines(
                        textAboveFactorPfs,
                        getFactorPfStrings(pf)
                    )
                );
            }
            
            return linesJoiner.toString();
        }
        

        /*
        In getGuiComponents, when trying to create factorPfsArea, a string will get built that
        contains the factors of the input long and the PFs of those factors. For longs with a
        lot of factors, the string that gets built will be pretty long and displaying that string
        in a text area would take a long time, so for longs like this, an error message will be
        displayed instead of the factors and their PFS. See the documentation comment for
        NtpTextArea.StringTooLongException for more info. Longs that have so many factors that
        the resulting string takes a long time to display seem rare. An example of one that's
        below the max input is 9,736,008,432,870,720, which was mentioned in the PF_INFO string
        and has 28,672 total factors.
         */
        
        /**
         * Returns a list with a main heading label, divisibility rules info components,
         * PF info components, and gaps where appropriate.
         */
        @Override
        public List<Component> getGuiComponents(long inputLong, String inputString) {
            String rulesInfo = new RulesAnswer(inputLong, inputString).infoParagraph;
            var pf = new PrimeFactorization(inputLong, inputString);
            
            var comps = new ArrayList<Component>(9);
            comps.addAll(
                List.of(
                    createAnswerMainHeadingLabel(getAnswerMainHeading(inputString)),
                    createGapBetweenAnswerSections(),
                    createAnswerSubHeadingLabel(RULES_INFO_HEADING),
                    createGap(5),
                    new NtpTextArea(rulesInfo),
                    createGapBetweenAnswerSections(),
                    createAnswerSubHeadingLabel(PF_INFO_HEADING)
                )
            );
            
            if (pf.isForAPrimeNumber()) {
                String textToDisplay = pf.getInfoSentence() + ' ' + getPrimeNumberSentence(inputString);
                comps.add(new NtpTextArea(textToDisplay));
            } else {
                String numFactorsInfo = getNumFactorsInfo(pf);
                
                try {
                    var factorPfsArea =
                        NtpTextArea.createWideOneWithStreamElements(getFactorPfStrings(pf));
                    String textAboveFactorPfs =
                        pf.getInfoSentence() + ' ' + numFactorsInfo +
                        ' ' + FACTORS_AND_PFS_SENTENCE;
                    comps.add(new NtpTextArea(textAboveFactorPfs));
                    comps.add(factorPfsArea);
                } catch (NtpTextArea.StringTooLongException ex) {
                    String textToDisplay =
                        pf.getInfoSentence() + ' ' + numFactorsInfo +
                        ' ' + NtpTextArea.StringTooLongException.ERROR_MESSAGE;
                    comps.add(new NtpTextArea(textToDisplay));
                }
            }
            
            return comps;
        }
    }
}
