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
    @GetMapping("/primes")
    public int[] getPrimes(@RequestParam int input) {
        return Calculations.getPrimes(input);
    }
    
    @GetMapping("/twinPrimePairStarts")
    public int[] getTwinPrimePairStarts(@RequestParam int input) {
        return Calculations.getTwinPrimePairStarts(input);
    }
    
    @GetMapping("/primeFactorization")
    public List<PrimeFactorization.FactorAndPower> getPrimeFactorization(@RequestParam int input) {
        return new PrimeFactorization(input).toList();
    }
    
    @GetMapping("/divisibilityAnswer")
    public DivisibilityAnswer getDivisibilityAnswer(@RequestParam int input) {
        return new DivisibilityAnswer(input);
    }
    
    @GetMapping("/gcdAndLcmAnswer")
    public GcdAndLcmAnswer getGcdAndLcmAnswer(@RequestParam int input1, @RequestParam int input2) {
        return new GcdAndLcmAnswer(input1, input2);
    }
    
    @GetMapping("/goldbachPrimePairStarts")
    public int[] getGoldbachPrimePairStarts(@RequestParam int input) {
        return Calculations.getGoldbachPrimePairStarts(input);
    }
    
    @GetMapping("/pythagoreanTriples")
    public List<Calculations.PythagoreanTriple> getPythagoreanTriples(@RequestParam int input) {
        return Calculations.getPythagoreanTriples(input);
    }
    
    @GetMapping("/twoSquareTheoremAnswer")
    public TwoSquareTheoremAnswer getTwoSquareTheoremAnswer(@RequestParam int input) {
        return new TwoSquareTheoremAnswer(input);
    }
    
    @GetMapping("/fibonacciLikeSequencesAnswer")
    public FibonacciLikeSequencesAnswer getFibonacciLikeSequencesAnswer(@RequestParam long input1, @RequestParam long input2) {
        return new FibonacciLikeSequencesAnswer(input1, input2);
    }
    
    @GetMapping("/ancientMultiplicationAnswer")
    public AncientMultiplicationAnswer getAncientMultiplicationAnswer(@RequestParam long input1, @RequestParam long input2) {
        return new AncientMultiplicationAnswer(input1, input2);
    }
}
