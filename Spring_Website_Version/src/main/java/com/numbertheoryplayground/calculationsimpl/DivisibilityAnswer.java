package com.numbertheoryplayground.calculationsimpl;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.*;

public final class DivisibilityAnswer {
    private final RulesData rulesData;
    private final PfAnswer pfAnswer;
    
    public DivisibilityAnswer(int input) {
        assertIsInRange(input, PrimeFactorization.MIN_INPUT, ONE_BILLION);
        
        rulesData = input >= 10 ? new RulesData(input) : null;
        
        var pf = new PrimeFactorization(input);
        pfAnswer = pf.isForAPrimeNumber() ? null : new PfAnswer(pf);
    }
    
    public RulesData getRulesData() {
        return rulesData;
    }
    
    public PfAnswer getPfAnswer() {
        return pfAnswer;
    }
    
    
    public static final class PfAnswer {
        private final List<FactorAndPower> inputFpList;
        private int numFactors;
        private final String numFactorsExpression;
        private final List<FactorAndPowerListAndLongString> factorFpListsAndLongStrings;
        
        PfAnswer(PrimeFactorization inputPf) {
            inputFpList = inputPf.getFactorsAndPowers();
            
            numFactors = 1;
            var numFactorsExpressionParts = new ArrayList<String>(inputFpList.size());
            for (var factorAndPower : inputFpList) {
                int power = factorAndPower.power();
                numFactors *= power + 1;
                numFactorsExpressionParts.add(String.format("(%d + 1)", power));
            }
            numFactorsExpression = String.join(" × ", numFactorsExpressionParts);
            
            factorFpListsAndLongStrings = inputPf.getFactorFpListsAndLongStrings();
        }
        
        @JsonProperty("inputFpArr")
        public List<FactorAndPower> getInputFpList() {
            return inputFpList;
        }
        
        public int getNumFactors() {
            return numFactors;
        }
        
        public String getNumFactorsExpression() {
            return numFactorsExpression;
        }
        
        @JsonProperty("factorFpArrsAndNumStrings")
        public List<FactorAndPowerListAndLongString> getFactorFpListsAndLongStrings() {
            return factorFpListsAndLongStrings;
        }
    }
    
    public static class RulesData {
        private final int last2Digits;
        private final int last3Digits;
        private final int sumOfDigits;
        private final AlternatingSumAndExpression blocksOf3AltSumAndExpression;
        private final AlternatingSumAndExpression digitsAltSumAndExpression;
        
        RulesData(int input) {
            last2Digits = input % 100;
            last3Digits = input % 1_000;
            
            var inputString = Integer.toString(input);
            
            sumOfDigits =
                inputString
                .chars()
                .map(Character::getNumericValue)
                .sum();
            
            blocksOf3AltSumAndExpression =
                input >= 1_000 ? new BlocksOf3AlternatingSumAndExpression(inputString) : null;
            
            digitsAltSumAndExpression = new DigitsAlternatingSumAndExpression(inputString);
        }
        
        public int getLast2Digits() {
            return last2Digits;
        }
        
        public int getLast3Digits() {
            return last3Digits;
        }
        
        public int getSumOfDigits() {
            return sumOfDigits;
        }
        
        public AlternatingSumAndExpression getBlocksOf3AltSumAndExpression() {
            return blocksOf3AltSumAndExpression;
        }
        
        public AlternatingSumAndExpression getDigitsAltSumAndExpression() {
            return digitsAltSumAndExpression;
        }
    }
    
    public static sealed class AlternatingSumAndExpression {
        protected int sum;
        protected String expression;
        
        public int getSum() {
            return sum;
        }
        
        public String getExpression() {
            return expression;
        }
    }
    
    private static final class BlocksOf3AlternatingSumAndExpression extends AlternatingSumAndExpression {
        private BlocksOf3AlternatingSumAndExpression(String inputString) {
            sum = 0;
            var expressionBuilder = new StringBuilder();
            var add = true;
            
            for (var i = inputString.length() - 3; i >= -2; i -= 3) {
                var blockString = inputString.substring(Math.max(0, i), i + 3);
                var blockInt = Integer.parseInt(blockString);
                if (!expressionBuilder.isEmpty()) {
                    expressionBuilder.append(add ? " + " : " − ");
                }
                expressionBuilder.append(blockString);
                sum += add ? blockInt : -blockInt;
                add = !add;
            }
            
            expression = expressionBuilder.toString();
        }
    }
    
    private static final class DigitsAlternatingSumAndExpression extends AlternatingSumAndExpression {
        private DigitsAlternatingSumAndExpression(String inputString) {
            sum = 0;
            var expressionBuilder = new StringBuilder();
            var add = true;
            
            for (var i = 0; i < inputString.length(); i++) {
                var digitChar = inputString.charAt(i);
                var digitInt = Character.getNumericValue(digitChar);
                if (!expressionBuilder.isEmpty()) {
                    expressionBuilder.append(add ? " + " : " − ");
                }
                expressionBuilder.append(digitChar);
                sum += add ? digitInt : -digitInt;
                add = !add;
            }
            
            expression = expressionBuilder.toString();
        }
    }
}
