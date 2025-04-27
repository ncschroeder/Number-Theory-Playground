package com.numbertheoryplayground;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.numbertheoryplayground.calculationsimpl.*;

@RestController
@RequestMapping("/calculate")
public class CalculationsController {
    public static final String PRIMES_ENDPOINT = "/primes";
    
    @GetMapping(PRIMES_ENDPOINT)
    public int[] getPrimes(@RequestParam int input) {
        return Calculations.getPrimes(input);
    }
    
    
    public static final String TWIN_PRIME_PAIR_STARTS_ENDPOINT = "/twinPrimePairStarts";
    
    @GetMapping(TWIN_PRIME_PAIR_STARTS_ENDPOINT)
    public int[] getTwinPrimePairStarts(@RequestParam int input) {
        return Calculations.getTwinPrimePairStarts(input);
    }
    
    
    public static final String PRIME_FACTORIZATION_ENDPOINT = "/primeFactorization";
    
    @GetMapping(PRIME_FACTORIZATION_ENDPOINT)
    public List<PrimeFactorization.FactorAndPower> getPrimeFactorization(@RequestParam int input) {
        return new PrimeFactorization(input).toList();
    }
    
    
    public static final String DIVISIBILITY_ANSWER_ENDPOINT = "/divisibilityAnswer";
    
    @GetMapping(DIVISIBILITY_ANSWER_ENDPOINT)
    public DivisibilityAnswer getDivisibilityAnswer(@RequestParam int input) {
        return new DivisibilityAnswer(input);
    }
    
    
    public static final String GCD_AND_LCM_ANSWER_ENDPOINT = "/gcdAndLcmAnswer";
    
    @GetMapping(GCD_AND_LCM_ANSWER_ENDPOINT)
    public GcdAndLcmAnswer getGcdAndLcmAnswer(@RequestParam int input1, @RequestParam int input2) {
        return new GcdAndLcmAnswer(input1, input2);
    }
    
    
    public static final String GOLDBACH_PRIME_PAIR_STARTS_ENDPOINT = "/goldbachPrimePairStarts";
    
    @GetMapping(GOLDBACH_PRIME_PAIR_STARTS_ENDPOINT)
    public int[] getGoldbachPrimePairStarts(@RequestParam int input) {
        return Calculations.getGoldbachPrimePairStarts(input);
    }
    
    
    public static final String PYTHAGOREAN_TRIPLES_ENDPOINT = "/pythagoreanTriples";
    
    @GetMapping(PYTHAGOREAN_TRIPLES_ENDPOINT)
    public List<Calculations.PythagoreanTriple> getPythagoreanTriples(@RequestParam int input) {
        return Calculations.getPythagoreanTriples(input);
    }
    
    
    public static final String TWO_SQUARE_THEOREM_ANSWER_ENDPOINT = "/twoSquareTheoremAnswer";
    
    @GetMapping(TWO_SQUARE_THEOREM_ANSWER_ENDPOINT)
    public TwoSquareTheoremAnswer getTwoSquareTheoremAnswer(@RequestParam int input) {
        return new TwoSquareTheoremAnswer(input);
    }
    
    
    public static final String FIBO_LIKE_SEQUENCES_ANSWER_ENDPOINT = "/fibonacciLikeSequencesAnswer";
    
    @GetMapping(FIBO_LIKE_SEQUENCES_ANSWER_ENDPOINT)
    public FibonacciLikeSequencesAnswer getFibonacciLikeSequencesAnswer(@RequestParam long input1, @RequestParam long input2) {
        return new FibonacciLikeSequencesAnswer(input1, input2);
    }
    
    
    public static final String ANCIENT_MULT_ANSWER_ENDPOINT = "/ancientMultiplicationAnswer";
    
    @GetMapping(ANCIENT_MULT_ANSWER_ENDPOINT)
    public AncientMultiplicationAnswer getAncientMultiplicationAnswer(@RequestParam long input1, @RequestParam long input2) {
        return new AncientMultiplicationAnswer(input1, input2);
    }
}
