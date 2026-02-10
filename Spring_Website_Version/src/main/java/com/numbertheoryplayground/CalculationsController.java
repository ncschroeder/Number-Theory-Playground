package com.numbertheoryplayground;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.numbertheoryplayground.calculationsimpl.*;
import com.numbertheoryplayground.calculationsimpl.divisibility.DivisibilityAnswer;
import com.numbertheoryplayground.calculationsimpl.gcdandlcm.GcdAndLcmAnswer;

import static com.numbertheoryplayground.calculationsimpl.Calculations.*;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndPower;

@RestController
@RequestMapping("/calculate/")
public class CalculationsController {
    /*
    There are constants for the endpoint ends so that they can be accessed from the
    CalculationsControllerTests class.
     */
    
    static final String PRIMES_ENDPOINT_END = "primes";
    
    @GetMapping(PRIMES_ENDPOINT_END)
    public int[] getPrimes(@RequestParam int input) {
        return Calculations.getPrimes(input);
    }
    
    
    static final String SEMIPRIMES_DATA_ENDPOINT_END = "semiprimes-data";
    
    @GetMapping(SEMIPRIMES_DATA_ENDPOINT_END)
    public List<SemiprimeData> getSemiprimesData(@RequestParam int input) {
        return Calculations.getSemiprimesData(input);
    }
    
    
    static final String TWIN_PRIME_PAIR_STARTS_ENDPOINT_END = "twin-prime-pair-starts";
    
    @GetMapping(TWIN_PRIME_PAIR_STARTS_ENDPOINT_END)
    public int[] getTwinPrimePairStarts(@RequestParam int input) {
        return Calculations.getTwinPrimePairStarts(input);
    }
    
    
    static final String PF_ENDPOINT_END = "prime-factorization";
    
    @GetMapping(PF_ENDPOINT_END)
    public List<FactorAndPower> getPfFactorsAndPowers(@RequestParam int input) {
        return new PrimeFactorization(input).getFps();
    }
    
    
    static final String DIVIS_ANSWER_ENDPOINT_END = "divisibility-answer";
    
    @GetMapping(DIVIS_ANSWER_ENDPOINT_END)
    public DivisibilityAnswer getDivisAnswer(@RequestParam int input) {
        return new DivisibilityAnswer(input);
    }
    
    
    static final String GCD_AND_LCM_ANSWER_ENDPOINT_END = "gcd-and-lcm-answer";
    
    @GetMapping(GCD_AND_LCM_ANSWER_ENDPOINT_END)
    public GcdAndLcmAnswer getGcdAndLcmAnswer(
        @RequestParam int input1,
        @RequestParam int input2
    ) {
        return new GcdAndLcmAnswer(input1, input2);
    }
    
    
    static final String GOLDBACH_PRIME_PAIR_STARTS_ENDPOINT_END = "goldbach-prime-pair-starts";
    
    @GetMapping(GOLDBACH_PRIME_PAIR_STARTS_ENDPOINT_END)
    public int[] getGoldbachPrimePairStarts(@RequestParam int input) {
        return Calculations.getGoldbachPrimePairStarts(input);
    }
    
    
    static final String PYTHAG_TRIPLES_ENDPOINT_END = "pythagorean-triples";
    
    @GetMapping(PYTHAG_TRIPLES_ENDPOINT_END)
    public List<Calculations.PythagoreanTriple> getPythagTriples(@RequestParam int input) {
        return Calculations.getPythagTriples(input);
    }
    
    
    static final String TWO_SQUARE_THEOREM_ANSWER_ENDPOINT_END = "two-square-theorem-answer";
    
    @GetMapping(TWO_SQUARE_THEOREM_ANSWER_ENDPOINT_END)
    public TwoSquareTheoremAnswer getTwoSquareTheoremAnswer(@RequestParam int input) {
        return new TwoSquareTheoremAnswer(input);
    }
    
    
    static final String FIBO_LIKE_SEQUENCES_ANSWER_ENDPOINT_END = "fibonacci-like-sequences-answer";
    
    @GetMapping(FIBO_LIKE_SEQUENCES_ANSWER_ENDPOINT_END)
    public FibonacciLikeSequencesAnswer getFiboLikeSequencesAnswer(
        @RequestParam long input1,
        @RequestParam long input2
    ) {
        return new FibonacciLikeSequencesAnswer(input1, input2);
    }
    
    
    static final String ANCIENT_MULT_ANSWER_ENDPOINT_END = "ancient-multiplication-answer";
    
    @GetMapping(ANCIENT_MULT_ANSWER_ENDPOINT_END)
    public AncientMultiplicationAnswer getAncientMultAnswer(
        @RequestParam long input1,
        @RequestParam long input2
    ) {
        return new AncientMultiplicationAnswer(input1, input2);
    }
}