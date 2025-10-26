package com.numbertheoryplayground;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.numbertheoryplayground.calculationsimpl.*;
import com.numbertheoryplayground.calculationsimpl.divisibility.DivisibilityAnswer;

import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndPower;

@RestController
@RequestMapping("/calculate")
public class CalculationsController {
    /*
    There are constants for the endpoints so that they can be accessed from the
    CalculationsControllerTests class.
     */
    
    static final String PRIMES_ENDPOINT = "/primes";
    
    @GetMapping(PRIMES_ENDPOINT)
    public int[] getPrimes(@RequestParam int input) {
        return Calculations.getPrimes(input);
    }
    
    
    static final String TWIN_PRIME_PAIR_STARTS_ENDPOINT = "/twin-prime-pair-starts";
    
    @GetMapping(TWIN_PRIME_PAIR_STARTS_ENDPOINT)
    public int[] getTwinPrimePairStarts(@RequestParam int input) {
        return Calculations.getTwinPrimePairStarts(input);
    }
    
    
    static final String PF_ENDPOINT = "/prime-factorization";
    
    @GetMapping(PF_ENDPOINT)
    public List<FactorAndPower> getPfFactorsAndPowers(@RequestParam int input) {
        return new PrimeFactorization(input).getFps();
    }
    
    
    static final String DIVIS_ANSWER_ENDPOINT = "/divisibility-answer";
    
    @GetMapping(DIVIS_ANSWER_ENDPOINT)
    public DivisibilityAnswer getDivisAnswer(@RequestParam int input) {
        return new DivisibilityAnswer(input);
    }
    
    
    static final String GCD_AND_LCM_ANSWER_ENDPOINT = "/gcd-and-lcm-answer";
    
    @GetMapping(GCD_AND_LCM_ANSWER_ENDPOINT)
    public GcdAndLcmAnswer getGcdAndLcmAnswer(
        @RequestParam int input1, @RequestParam int input2
    ) {
        return new GcdAndLcmAnswer(input1, input2);
    }
    
    
    static final String GOLDBACH_PRIME_PAIR_STARTS_ENDPOINT = "/goldbach-prime-pair-starts";
    
    @GetMapping(GOLDBACH_PRIME_PAIR_STARTS_ENDPOINT)
    public int[] getGoldbachPrimePairStarts(@RequestParam int input) {
        return Calculations.getGoldbachPrimePairStarts(input);
    }
    
    
    static final String PYTHAG_TRIPLES_ENDPOINT = "/pythagorean-triples";
    
    @GetMapping(PYTHAG_TRIPLES_ENDPOINT)
    public List<Calculations.PythagoreanTriple> getPythagTriples(@RequestParam int input) {
        return Calculations.getPythagTriples(input);
    }
    
    
    static final String TWO_SQUARE_THEOREM_ANSWER_ENDPOINT = "/two-square-theorem-answer";
    
    @GetMapping(TWO_SQUARE_THEOREM_ANSWER_ENDPOINT)
    public TwoSquareTheoremAnswer getTwoSquareTheoremAnswer(@RequestParam int input) {
        return new TwoSquareTheoremAnswer(input);
    }
    
    
    static final String FIBO_LIKE_SEQUENCES_ANSWER_ENDPOINT = "/fibonacci-like-sequences-answer";
    
    @GetMapping(FIBO_LIKE_SEQUENCES_ANSWER_ENDPOINT)
    public FibonacciLikeSequencesAnswer getFiboLikeSequencesAnswer(
        @RequestParam long input1, @RequestParam long input2
    ) {
        return new FibonacciLikeSequencesAnswer(input1, input2);
    }
    
    
    static final String ANCIENT_MULT_ANSWER_ENDPOINT = "/ancient-multiplication-answer";
    
    @GetMapping(ANCIENT_MULT_ANSWER_ENDPOINT)
    public AncientMultiplicationAnswer getAncientMultAnswer(
        @RequestParam long input1, @RequestParam long input2
    ) {
        return new AncientMultiplicationAnswer(input1, input2);
    }
}