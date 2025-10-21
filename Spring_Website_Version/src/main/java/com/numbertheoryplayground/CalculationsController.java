package com.numbertheoryplayground;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.numbertheoryplayground.calculationsimpl.*;

@RestController
@RequestMapping("/calculate")
public class CalculationsController {
    /*
    There are constants for the endpoints so that they can be accessed from the
    CalculationsControllerTests class.
     */
    
    static final String PRIMES_ENDPOINT = "/primes";
    
    @GetMapping(PRIMES_ENDPOINT)
    public List<String> getPrimesStrings(@RequestParam int input) {
        return
            Calculations.getPrimes(input)
            .mapToObj(Integer::toString)
            .toList();
    }
    
    
    static final String TWIN_PRIME_PAIR_STARTS_ENDPOINT = "/twinPrimePairStarts";
    
    @GetMapping(TWIN_PRIME_PAIR_STARTS_ENDPOINT)
    public int[] getTwinPrimePairStarts(@RequestParam int input) {
        return Calculations.getTwinPrimePairStarts(input);
    }
    
    
    static final String PF_ENDPOINT = "/primeFactorization";
    
    @GetMapping(PF_ENDPOINT)
    public List<PrimeFactorization.FactorAndPower> getPfFactorsAndPowers(@RequestParam int input) {
        return new PrimeFactorization(input).getFps();
    }
    
    
    static final String DIVIS_ANSWER_ENDPOINT = "/divisibilityAnswer";
    
    @GetMapping(DIVIS_ANSWER_ENDPOINT)
    public DivisibilityAnswer getDivisAnswer(@RequestParam int input) {
        return new DivisibilityAnswer(input);
    }
    
    
    static final String GCD_AND_LCM_ANSWER_ENDPOINT = "/gcdAndLcmAnswer";
    
    @GetMapping(GCD_AND_LCM_ANSWER_ENDPOINT)
    public GcdAndLcmAnswer getGcdAndLcmAnswer(@RequestParam int input1, @RequestParam int input2) {
        return new GcdAndLcmAnswer(input1, input2);
    }
    
    
    static final String GOLDBACH_PRIME_PAIR_STARTS_ENDPOINT = "/goldbachPrimePairStarts";
    
    @GetMapping(GOLDBACH_PRIME_PAIR_STARTS_ENDPOINT)
    public int[] getGoldbachPrimePairStarts(@RequestParam int input) {
        return Calculations.getGoldbachPrimePairStarts(input);
    }
    
    
    static final String PYTHAG_TRIPLES_ENDPOINT = "/pythagoreanTriples";
    
    @GetMapping(PYTHAG_TRIPLES_ENDPOINT)
    public List<Calculations.PythagoreanTriple> getPythagTriples(@RequestParam int input) {
        return Calculations.getPythagTriples(input);
    }
    
    
    static final String TWO_SQUARE_THEOREM_ANSWER_ENDPOINT = "/twoSquareTheoremAnswer";
    
    @GetMapping(TWO_SQUARE_THEOREM_ANSWER_ENDPOINT)
    public TwoSquareTheoremAnswer getTwoSquareTheoremAnswer(@RequestParam int input) {
        return new TwoSquareTheoremAnswer(input);
    }
    
    
    static final String FIBO_LIKE_SEQUENCES_ANSWER_ENDPOINT = "/fibonacciLikeSequencesAnswer";
    
    @GetMapping(FIBO_LIKE_SEQUENCES_ANSWER_ENDPOINT)
    public FibonacciLikeSequencesAnswer getFiboLikeSequencesAnswer(
        @RequestParam long input1, @RequestParam long input2
    ) {
        return new FibonacciLikeSequencesAnswer(input1, input2);
    }
    
    
    static final String ANCIENT_MULT_ANSWER_ENDPOINT = "/ancientMultiplicationAnswer";
    
    @GetMapping(ANCIENT_MULT_ANSWER_ENDPOINT)
    public AncientMultiplicationAnswer getAncientMultAnswer(
        @RequestParam long input1, @RequestParam long input2
    ) {
        return new AncientMultiplicationAnswer(input1, input2);
    }
}
