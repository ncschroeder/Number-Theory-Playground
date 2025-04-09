package com.numbertheoryplayground.calculationsimpl;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.*;
public class DivisibilityAnswer {
    public final RulesData rulesData;
    public final PfAnswer pfAnswer;
    
    public DivisibilityAnswer(int input) {
        rulesData = input >= 10 ? new RulesData(input) : null;
        
        var pf = new PrimeFactorization(input);
        pfAnswer = pf.isForAPrimeNumber() ? null : new PfAnswer(pf);
    }
    
    public static class PfAnswer {
        @JsonProperty("inputPfArr")
        public final List<FactorAndPower> inputPfList;
        
        public int numFactors;
        
        public final String numFactorsExpression;
        
        @JsonProperty("factorPfArrsAndNums")
        public final List<PfListAndInt> factorPfListsAndInts;
        
        public PfAnswer(PrimeFactorization inputPf) {
            inputPfList = inputPf.toList();
            
            numFactors = 1;
            var numFactorsExpressionParts = new ArrayList<String>(inputPfList.size());
            for (var factorAndPower : inputPfList) {
                int power = factorAndPower.power();
                numFactors *= power + 1;
                numFactorsExpressionParts.add(String.format("(%d + 1)", power));
            }
            numFactorsExpression = String.join(" × ", numFactorsExpressionParts);
            
            factorPfListsAndInts = inputPf.getFactorPfListsAndInts();
        }
    }
    
    public static class RulesData {
        public final int last2Digits;
        public final int last3Digits;
        public final int sumOfDigits;
        public final AlternatingSumAndExpression blocksOf3AltSumAndExpression;
        public final AlternatingSumAndExpression digitsAltSumAndExpression;
        
        public RulesData(int input) {
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
