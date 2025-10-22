package com.numbertheoryplayground.calculationsimpl.divisibility;

import static com.numbertheoryplayground.InputValidation.*;

public final class DivisibilityAnswer {
    static final long MIN_INPUT = 10;
    static final long MAX_INPUT = TEN_THOUSAND;
    
    private final RulesData rulesData;
    
    private final PrimeFactorizationAnswer pfAnswer;
    
    public DivisibilityAnswer(int input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        rulesData = new RulesData(input);
        pfAnswer = PrimeFactorizationAnswer.createIfNotPrime(input);
//        var pf = new PrimeFactorization(input);
//        pfAnswer = pf.isForAPrimeNumber() ? null : new PrimeFactorizationAnswer(pf);
    }
    
    public RulesData getRulesData() {
        return rulesData;
    }
    
    public PrimeFactorizationAnswer getPfAnswer() {
        return pfAnswer;
    }
}