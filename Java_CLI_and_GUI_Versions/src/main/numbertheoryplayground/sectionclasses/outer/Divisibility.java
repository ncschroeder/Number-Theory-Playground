package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.*;
import java.util.function.BiFunction;
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
    The calculations for this section need 1 input number and are explained below the info
    strings below.
     */
    
    /**
     * This is 10 since none of the divisibility rules mentioned below can be used on
     * single-digit numbers.
     */
    private static final long MIN_INPUT = 10;
    
    /**
     * This section creates PrimeFactorization objects so the max input for those will be used.
     */
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
    
    private static String getAnswerMainHeading(String inputString) {
        return "Divisibility Info for " + inputString;
    }
    
    
    private static final String RULES_INFO = """
Some rules can be used to determine if a whole number is divisible by another whole number.
I'll go over 1 rule for each number in the range of 3 to 15, excluding 5 and 10, though there
are rules for more numbers and many numbers have multiple rules. I'll go over an example of
using these rules to find the factors of a number in a few paragraphs. Let's have a variable n
and let it represent a whole number. If the number formed from the last 2 digits of n is
divisible by 4, then n is divisible by 4. If the number formed from the last 3 digits is
divisible by 8, then n is divisible by 8. If the sum of the digits of n is divisible by 3, then
n is divisible by 3. If the sum of the digits is divisible by 9, then n is divisible by 9. If n
is even and divisible by 3, then it's also divisible by 6. If n is divisible by both 3 and 4,
then it's also divisible by 12. If n is even and divisible by 7 (see rule below), then it's
also divisible by 14. If n is divisible by both 3 and 5, then it's also divisible by 15.

For 11, we do an alternating sum of the digits of n from left to right. We start with 0, add
the first digit, subtract the second digit, add the third digit, and so on for all digits. If
this sum is divisible by 11, then n is divisible by 11.

For 7 and 13, we split n into 3-digit blocks of 3 from right to left, though the leftmost block
can contain 1 or 2 digits. Coincidentally, these are the blocks separated by commas if we write
n with commas. We do an alternating sum of the blocks from right to left. We start with 0, add
the rightmost block, subtract the block to the left of that, add the block to the left of that,
and so on for all the blocks. If this sum is divisible by 7, then n is divisible by 7. If this
sum is divisible by 13, then n is divisible by 13. These alternating sums might involve negative
integers or 0, so that makes them some of the few calculations done by the Number Theory
Playground that involve numbers other than natural numbers.

Here's an example. Let n be 720,720. The PF of n is 2^4 × 3^2 × 5 × 7 × 11 × 13. We can tell
from that PF that n is divisible by all the numbers that had rules mentioned about them above.
Let's check using those rules. The last 2 digits form the number 20, which is divisible by 4.
The last 3 digits form the number 720, which is divisible by 8. The sum of the digits is
7 + 2 + 0 + 7 + 2 + 0 = 18, which is divisible by both 3 and 9. Since n is even and divisible
by 3, it's also divisible by 6. Since n is divisible by both 3 and 4, it's also divisible by 12.
Since n is divisible by both 3 and 5, it's also divisible by 15. The alternating sum of the
digits from left to right is 7 − 2 + 0 − 7 + 2 − 0 = 0, which is divisible by 11. The
alternating sum of 3-digit blocks from right to left is 720 − 720 = 0, which is divisible by
both 7 and 13. Since n is even and divisible by 7, it's also divisible by 14.""";
    
    /*
    Some calculations for this section are: use the divisibility rules to see if we can find
    some factors of the input number and display a paragraph with info from this.
     */
    
    /**
     * Class with data related to rules that can be used to determine if the input long is
     * divisible by other whole numbers.
     */
    static final class RulesAnswer {
        private static final String HEADING = "Rules Info";
        private final String infoParagraph;
        
        private final long last2Digits;
        
        private final long last3Digits;
        
        // 720,720 is the number used in the example above and the doc comments below.
        
        /**
         * A mathematical expression of the sum of the digits from left to right.
         * For 720,720, this would be "7 + 2 + 0 + 7 + 2 + 0 = 18".
         */
        private final String sumOfDigitsExpression;
        
        /**
         * A mathematical expression of the alternating sum of the digits from left to right.
         * For 720,720, this would be "7 − 2 + 0 − 7 + 2 − 0 = 0".
         */
        private final String altSumOfDigitsExpression;
        
        /**
         * A mathematical expression of the alternating sum of 3-digit blocks from right to left.
         * For 720,720, this would be "720 − 720 = 0".
         */
        private final String altSumOfBlocksExpression;
        
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
                        "%1$s divisible by %2$s so %3$s %1$s divisible by %2$s",
                        isDivisible ? "is" : "isn't",
                        possibleFactor,
                        inputStringWithCommas
                    );
            
            var infoSentencesJoiner = new StringJoiner(". ", "", ".");
            boolean isEven = isEven(inputLong);
            last2Digits = inputLong % 100;
            last3Digits = inputLong % 1_000;
            boolean isDivisibleBy4 = isDivisible(last2Digits, 4);
            
            if (!isEven) {
                infoSentencesJoiner.add(
                    inputStringWithCommas + " isn't even so it isn't divisible by any even numbers"
                );
            } else if (inputLong >= 100) {
                infoSentencesJoiner.add(
                    String.format(
                        "The last 2 digits form the number %d, which %s",
                        last2Digits, getDivisSentenceEnd.apply(4, isDivisibleBy4)
                    )
                );
                
                if (!isDivisibleBy4) {
                    infoSentencesJoiner.add(
                        String.format(
                            "Since %s isn't divisible by 4, it's also not divisible by 8, 12, and " +
                                "any other multiples of 4",
                            inputStringWithCommas
                        )
                    );
                } else if (inputLong >= 1_000) {
                    boolean isDivisibleBy8 = isDivisible(last3Digits, 8);
                    infoSentencesJoiner.add(
                        String.format(
                            "The last 3 digits form the number %d, which %s",
                            last3Digits, getDivisSentenceEnd.apply(8, isDivisibleBy8)
                        )
                    );
                }
            }
            
            var inputStringWithoutCommas = Long.toString(inputLong);
            var sumOfDigitsExpressionBuilder = new StringBuilder();
            var sumOfDigits = 0;
            var altSumOfDigitsExpressionBuilder = new StringBuilder();
            var altSumOfDigits = 0;
            var addForAltSum = true;
            
            for (var i = 0; i < inputStringWithoutCommas.length(); i++) {
                if (!sumOfDigitsExpressionBuilder.isEmpty()) {
                    sumOfDigitsExpressionBuilder.append(" + ");
                    altSumOfDigitsExpressionBuilder.append(addForAltSum ? " + " : " − ");
                }
                var digitChar = inputStringWithoutCommas.charAt(i);
                sumOfDigitsExpressionBuilder.append(digitChar);
                altSumOfDigitsExpressionBuilder.append(digitChar);
                var digitInt = Character.getNumericValue(digitChar);
                sumOfDigits += digitInt;
                altSumOfDigits += addForAltSum ? digitInt : -digitInt;
                addForAltSum = !addForAltSum;
            }
            
            sumOfDigitsExpression =
                sumOfDigitsExpressionBuilder
                .append(" = ")
                .append(sumOfDigits)
                .toString();
            
            altSumOfDigitsExpression =
                altSumOfDigitsExpressionBuilder
                .append(" = ")
                .append(altSumOfDigits)
                .toString();
            
            boolean isDivisibleBy3 = isDivisible(sumOfDigits, 3);
            infoSentencesJoiner.add(
                String.format(
                    "The sum of the digits is %s, which %s",
                    sumOfDigitsExpression, getDivisSentenceEnd.apply(3, isDivisibleBy3)
                )
            );
            
            if (!isDivisibleBy3) {
                infoSentencesJoiner.add(
                    String.format(
                        "Since %s isn't divisible by 3, it's also not divisible by 6, 9, 12, and " +
                            "any other multiples of 3",
                        inputStringWithCommas
                    )
                );
            } else {
                boolean isDivisibleBy9 = isDivisible(sumOfDigits, 9);
                infoSentencesJoiner.add(
                    sumOfDigits + ' ' + getDivisSentenceEnd.apply(sumOfDigits, isDivisibleBy9)
                );
                
                if (isEven) {
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
                
                if (isDivisible(inputLong, 5)) {
                    infoSentencesJoiner.add(
                        inputStringWithCommas +
                        " is divisible by both 3 and 5 so it's also divisible by 15"
                    );
                }
            }
            
            boolean isDivisibleBy11 = isDivisible(altSumOfDigits, 11);
            infoSentencesJoiner.add(
                String.format(
                    "The alternating sum of the digits from left to right is %s, which %s",
                    altSumOfDigitsExpression, getDivisSentenceEnd.apply(11, isDivisibleBy11)
                )
            );
            
            if (inputLong < 1_000) {
                altSumOfBlocksExpression = null;
            } else {
                var altSumOfBlocksExpressionBuilder = new StringBuilder();
                var altSumOfBlocks = 0;
                String[] blocksOf3 = inputStringWithCommas.split(",");
                addForAltSum = true;
                
                for (int i = blocksOf3.length - 1; i >= 0; i--) {
                    if (!altSumOfBlocksExpressionBuilder.isEmpty()) {
                        altSumOfBlocksExpressionBuilder.append(addForAltSum ? " + " : " − ");
                    }
                    var blockString = blocksOf3[i];
                    altSumOfBlocksExpressionBuilder.append(blockString);
                    var blockInt = Integer.parseInt(blockString);
                    altSumOfBlocks += addForAltSum ? blockInt : -blockInt;
                    addForAltSum = !addForAltSum;
                }
                
                var altSumOfBlocksString = createStringWithCommas(altSumOfBlocks);
                altSumOfBlocksExpression =
                    altSumOfBlocksExpressionBuilder
                    .append(" = ")
                    .append(altSumOfBlocksString)
                    .toString();
                
                boolean isDivisibleBy7 = isDivisible(altSumOfBlocks, 7);
                infoSentencesJoiner.add(
                    String.format(
                        "The alternating sum of 3-digit blocks from right to left is %s, which %s",
                        altSumOfBlocksExpression, getDivisSentenceEnd.apply(7, isDivisibleBy7)
                    )
                );
                
                boolean isDivisibleBy13 = isDivisible(altSumOfBlocks, 13);
                infoSentencesJoiner.add(
                    altSumOfBlocksString + ' ' + getDivisSentenceEnd.apply(13, isDivisibleBy13)
                );
                
                if (isEven && isDivisibleBy7) {
                    infoSentencesJoiner.add(
                        inputStringWithCommas +
                        " is even and divisible by 7 so it's also divisible by 14"
                    );
                }
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
            return sumOfDigitsExpression;
        }
        
        String getAltSumOfDigitsExpression() {
            return altSumOfDigitsExpression;
        }
        
        String getAltSumOfBlocksExpression() {
            return altSumOfBlocksExpression;
        }
    }
    
    
    private static final String PF_INFO = """
The factors of a whole number > 1 can be found by looking at its prime factorization (PF). Let's
have a variable n and let it represent a whole number > 1. First, you can find how many factors
n has by looking at n's PF, taking the powers of the factors, adding 1 to each, and multiplying
them. For example, the PF of 36 is 2^2 × 3^2. The powers are 2 and 2, so there are 3 × 3 = 9
factors. This amount includes 1 and the number that the PF is for (36 in this case). You can
find the factors of n by finding the PFs within n's PF, or the subfactorizations, as I like to
call them. For 2^2 × 3^2, the subfactorizations are
2, 3, 2^2 (4), 2 × 3 (6), 3^2 (9), 2^2 × 3 (12), and 2 × 3^2 (18).

Whole numbers that are ≤ 10 quadrillion, the max input of this section, generally have a small
amount of factors, like < 100. An example of an input number with a high number of factors is
9,736,008,432,870,720, or 9 quadrillion 736 trillion ... This number is the product of 2^6 and
the next 12 prime numbers so it has 13 unique prime factors and its PF is
2^6 x 3 x 5 x 7 x 11 x 13 x 17 x 19 x 23 x 29 x 31 x 37 x 41. It has 7 × 2^12 = 28,672 factors!""";
    
    /*
    The other calculations for this section are: find the PF of the input number and if we can
    determine from this PF that the input number is composite (not prime), then find its factors
    by finding the subfactorizations of its PF.
     */
    
    private static final String PF_ANSWER_HEADING = "Prime Factorization Info";
    
    private static String getInputIsPrimeSentence(String inputString) {
        return
            inputString +
            " is prime and doesn't have any whole number factors other than 1 and itself.";
    }
    
    private static String getFactorsAndPfsSentence(String inputString) {
        return String.format("The factors, excluding 1 and %s, and their PFs are:", inputString);
    }
    
    /**
     * Class with data related to using a number's PF to find the number of factors it has.
     */
    static class NumberOfFactorsData {
        private int numFactors;
        
        /**
         * A mathematical expression that shows how the number of factors was calculated.
         */
        private final String expression;
        
        /*
        12 has a PF of 2^2 × 3 so if a PF object for this was the input, then expression would
        be "(2 + 1) × (1 + 1)" and numFactors would be 6.
         */
        
        private final String infoSentence;
        
        NumberOfFactorsData(PrimeFactorization pf) {
            numFactors = 1;
            var powerStrings = new ArrayList<String>(pf.getFps().size());
            for (FactorAndPower fp : pf.getFps()) {
                numFactors *= fp.power() + 1;
                powerStrings.add(String.format("(%d + 1)", fp.power()));
            }
            expression = String.join(" × ", powerStrings);
            
            infoSentence =
                String.format(
                    "By looking at the power%s, we can see that there are %s = %s factors.",
                    pf.getFps().size() == 1 ? "" : "s",
                    expression,
                    createStringWithCommas(numFactors)
                );
        }
        
        int getNumFactors() {
            return numFactors;
        }
        
        String getExpression() {
            return expression;
        }
    }
    
    /**
     * This method finds PFs of factors of the input PF's corresponding big int, excluding 1
     * and the corresponding big int, by finding subfactorizations in the input PF and that's
     * done by finding combinations of factors and powers in that PF. The PFs in the list
     * returned are sorted by corresponding big ints.
     */
    static List<PrimeFactorization> getFactorPfs(PrimeFactorization pf, int numFactors) {
        /*
        numFactors is the calculated number of factors that the input PF's corresponding big int
        has, including 1 and the corresponding big int. The algorithm below will add a PF to
        factorPfs that's the same as the input PF but then remove it, so the capacity for
        factorPfs will be set to numFactors - 1 and its size at the end will be numFactors - 2.
         */
        
        for (FactorAndPower fp : pf.getFps()) {
            long primeFactor = fp.factor();
            int maxPower = fp.power();
            /*
            In the 2nd for loop below, we want to iterate through all the PFs that are in factorPfs
            at this point, and not the ones that get added below. Use this variable for that.
             */
            int lastPfIndexToUse = factorPfs.size() - 1;
            
            for (var power = 1; power <= maxPower; power++) {
                var singleton = List.of(new FactorAndPower(primeFactor, power));
                factorPfs.add(new PrimeFactorization(singleton));
                
                for (var i = 0; i <= lastPfIndexToUse; i++) {
                    List<FactorAndPower> listFactorFps = factorPfs.get(i).getFps();
                    var newFactorFps = new ArrayList<FactorAndPower>(listFactorFps.size() + 1);
                    newFactorFps.addAll(listFactorFps);
                    newFactorFps.add(new FactorAndPower(primeFactor, power));
                    factorPfs.add(new PrimeFactorization(newFactorFps));
                }
            }
        }
        
        factorPfs.removeLast();
        factorPfs.sort(Comparator.comparing(PrimeFactorization::getCorrespondingBigInt));
        return factorPfs;
    }
    
    private static Stream<String> getFactorPfStrings(PrimeFactorization pf, int numFactors) {
        return
            getFactorPfs(pf, numFactors)
            .stream()
            .map(pf2 ->
                pf2.isForAPrimeNumber()
                ? pf2.getCorrespondingBigIntString()
                : String.format("%s (%s)", pf2, pf2.getCorrespondingBigIntString())
            );
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
                .add(RulesAnswer.HEADING)
                .add(NtpCli.putNewLineChars(rulesInfo))
                .add("")
                .add(PF_ANSWER_HEADING);
            
            if (pf.isForAPrimeNumber()) {
                linesJoiner.add(
                    NtpCli.putNewLineChars(
                        pf.getInfoSentence() + ' ' + getInputIsPrimeSentence(inputString)
                    )
                );
            } else {
                var numFactorsData = new NumberOfFactorsData(pf);
                String textAboveFactorPfs =
                    NtpCli.putNewLineChars(
                        pf.getInfoSentence() + ' ' + numFactorsData.infoSentence +
                        ' ' + getFactorsAndPfsSentence(inputString)
                    );
                linesJoiner.add(
                    NtpCli.buildStringWithStreamElementsOnLongLines(
                        textAboveFactorPfs,
                        getFactorPfStrings(pf, numFactorsData.numFactors)
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
                    createAnswerSubHeadingLabel(RulesAnswer.HEADING),
                    createGap(5),
                    new NtpTextArea(rulesInfo),
                    createGapBetweenAnswerSections(),
                    createAnswerSubHeadingLabel(PF_ANSWER_HEADING)
                )
            );
            
            if (pf.isForAPrimeNumber()) {
                String textToDisplay = pf.getInfoSentence() + ' ' + getInputIsPrimeSentence(inputString);
                comps.add(new NtpTextArea(textToDisplay));
            } else {
                var numFactorsData = new NumberOfFactorsData(pf);
                
                try {
                    var factorPfsArea =
                        NtpTextArea.createWideOneWithStreamElements(
                            getFactorPfStrings(pf, numFactorsData.numFactors)
                        );
                    String textAboveFactorPfs =
                        pf.getInfoSentence() + ' ' + numFactorsData.infoSentence +
                        ' ' + getFactorsAndPfsSentence(inputString);
                    comps.add(new NtpTextArea(textAboveFactorPfs));
                    comps.add(factorPfsArea);
                } catch (NtpTextArea.StringTooLongException ex) {
                    String textToDisplay =
                        pf.getInfoSentence() + ' ' + numFactorsData.infoSentence +
                        ' ' + NtpTextArea.StringTooLongException.ERROR_MESSAGE;
                    comps.add(new NtpTextArea(textToDisplay));
                }
            }
            
            return comps;
        }
    }
}
