package com.numbertheoryplayground.calculationsimpl;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.*;
public class DivisibilityAnswer {
    public final PfAnswer pfAnswer;
    
    public DivisibilityAnswer(int input) {
        var pf = new PrimeFactorization(input);
        pfAnswer = pf.isForAPrimeNumber() ? null : new PfAnswer(pf);
    }
    
    public static class PfAnswer {
        @JsonProperty("inputPfArr")
        public final List<FactorAndPower> inputPfList;
        
        @JsonProperty("factorPfArrsAndNums")
        public final List<PfListAndInt> factorPfListsAndInts;
        
        private PfAnswer(PrimeFactorization inputPf) {
            inputPfList = inputPf.toList();
            factorPfListsAndInts = inputPf.getFactorPfListsAndInts();
        }
    }
}
