package com.numbertheoryplayground.calculationsimpl;

import java.util.*;
import java.util.stream.IntStream;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;

/**
 * Utility class with static methods for doing calculations.
 */
public class Calculations {
    static boolean isDivisible(int a, int b) {
        return a % b == 0;
    }
    
    static boolean isEven(int i) {
        return isDivisible(i, 2);
    }
    
    static boolean isOdd(int i) {
        return !isEven(i);
    }
    
    /**
     * Returns a stream of possible factors of the input ≤ the floor of the square root of
     * the input. These consist of 2 and odd numbers ≥ 3.
     */
    private static IntStream getPossibleFactors(int input) {
        var maxPossibleFactor = (int) Math.sqrt(input);
        return
            IntStream.concat(
                IntStream.of(2),
                IntStream.iterate(3, i -> i + 2)
            )
            .takeWhile(i -> i <= maxPossibleFactor);
    }
    
    static boolean isPrime(int input) {
        return
            input > 1 &&
            getPossibleFactors(input).noneMatch(i -> isDivisible(input, i));
    }
    
    private static boolean bothArePrime(int a, int b) {
        return isPrime(a) && isPrime(b);
    }
    
    /**
     * Returns an array of the first 30 primes ≥ the input.
     */
    public static int[] getPrimes(int input) {
        assertIsInRange(input, 0, TEN_THOUSAND);
        
        // Create a stream of odd primes since all primes besides 2 are odd.
        int iterationStart = isOdd(input) ? input : input + 1;
        IntStream oddPrimes =
            IntStream.iterate(iterationStart, i -> i + 2)
            .filter(Calculations::isPrime);
        
        return
            (input <= 2 ? IntStream.concat(IntStream.of(2), oddPrimes) : oddPrimes)
            .limit(30)
            .toArray();
    }
    
    
    public record SemiprimeData(int semiprime, int primeFactor1, int primeFactor2) {}
    
    /**
     * If the input is a semiprime, a SemiprimeData for it gets returned.
     * Otherwise, null gets returned.
     */
    private static SemiprimeData checkIfSemiprime(int input) {
        /*
        Just like with the algorithm for determining if an int is prime, to find the first
        factor of the input, we only need to check primes ≤ the square root of the input.
        If we don't find a factor, then that means the input is prime and not semiprime.
         */
        OptionalInt optionalPrimeFactor1 =
            getPossibleFactors(input)
            .filter(i -> isDivisible(input, i))
            .findFirst();
        
        if (optionalPrimeFactor1.isPresent()) {
            int primeFactor1 = optionalPrimeFactor1.getAsInt();
            int factor2 = input / primeFactor1;
            if (primeFactor1 == factor2 || isPrime(factor2)) {
                return new SemiprimeData(input, primeFactor1, factor2);
            }
        }
        
        return null;
    }
    
    /**
     * Returns a list of SemiprimeDatas for the first 20 semiprimes ≥ the input.
     */
    public static List<SemiprimeData> getSemiprimesData(int input) {
        assertIsInRange(input, 0, TEN_THOUSAND);
        
        return
            IntStream.iterate(input, i -> i + 1)
            .mapToObj(Calculations::checkIfSemiprime)
            .filter(sd -> sd != null)
            .limit(20)
            .toList();
    }
    
    
    /**
     * Finds the first 20 twin prime pairs where the lowest number in the pair is ≥ the input.
     * For example, if the input is 3, then the pair 3 and 5 will be the first one found since
     * the lowest number in that pair is 3. If the input is 4, then the pair 5 and 7 will be the
     * first one found. An array that contains the lowest numbers of those pairs gets returned.
     */
    public static int[] getTwinPrimePairStarts(int input) {
        assertIsInRange(input, 0, TEN_THOUSAND);
        
        /*
        All twin prime pairs besides 3 and 5 consist of 1 number that's 1 below a multiple of 6
        and another number that's 1 above that same multiple of 6. Set iterationStart to the
        first int ≥ the input that's 1 below a multiple of 6 so that we'll be able to iterate
        through ints that are 1 below a multiple of 6.
         */
        int iterationStart = input;
        while (iterationStart % 6 != 5) iterationStart++;
        IntStream pairStarts =
            IntStream.iterate(iterationStart, i -> i + 6)
            .filter(i -> bothArePrime(i, i + 2));
        
        return
            (input <= 3 ? IntStream.concat(IntStream.of(3), pairStarts) : pairStarts)
            .limit(20)
            .toArray();
    }
    
    /**
     * Find the pairs of primes that sum to the input and returns an array that contains the
     * lowest ints of those pairs.
     */
    public static int[] getGoldbachPrimePairStarts(int input) {
        assertIsInRange(input, 4, 1_000);
        if (isOdd(input)) {
            throw InvalidInputNumberException.getInstance();
        }
        
        /*
        Check if the input is 4 since 4 is the only even number ≥ 4 that has 2 in a pair of
        primes that sum to it.
         */
        if (input == 4) {
            return new int[] { 2 };
        }
        
        /*
        Check pairs of odd numbers that sum to the input for primality. The iterating only
        needs to go up to the floor of half of the input. After that point, checks for
        primality will be done on pairs of numbers that have already been checked for primality.
         */
        int maxPossiblePairStart = input / 2;
        return
            IntStream.iterate(3, i -> i <= maxPossiblePairStart, i -> i + 2)
            .filter(i -> bothArePrime(i, input - i))
            .toArray();
    }
    
    public record PythagoreanTriple(int a, int b, int c) {
        @JsonProperty("isPrimitive")
        public boolean isPrimitive() {
            /*
            See if there's a common factor other than 1. The iterating only needs to go up to
            a third of the min of a, b, and c. This is because if the min could be a common
            factor, then we would be able to divide a, b, and c by the min and get a
            Pythagorean triple and the min of a, b, and c in that triple would be 1. There's
            no Pythagorean triple with 1. The first one is 3, 4, and 5. Because of that, half
            of the min also can't be a common factor. The next lowest possible common factor
            is a third of the min.
             */
            int maxPossibleCommonFactor = Math.min(a, Math.min(b, c)) / 3;
            return
                IntStream.concat(
                    IntStream.of(2),
                    IntStream.iterate(3, i -> i + 2)
                )
                .takeWhile(i -> i <= maxPossibleCommonFactor)
                .noneMatch(i -> isDivisible(a, i) && isDivisible(b, i) && isDivisible(c, i));
        }
    }
    
    /**
     * Returns a list of triple objects for the first 10 Pythagorean triples where the lowest
     * number in the triple is ≥ the input. For example, if the input is 3, then an object for
     * the triple 3, 4, and 5 will be the first one since the lowest number in that triple is 3.
     * If the input is 4, then an object for the triple 5, 12, and 13 will be the first one.
     */
    public static List<PythagoreanTriple> getPythagTriples(int input) {
        assertIsInRange(input, 0, 100);
        
        final int numTriplesToFind = 10;
        var triples = new ArrayList<PythagoreanTriple>(numTriplesToFind);
        int a = input;
        int b = a + 1;
        
        while (true) {
            var cDouble = Math.hypot(a, b);
            /*
            b + 1 is the min possible integer value for c, so if c is less than that then
            the max value for b for the current value of a has been exceeded.
             */
            if (cDouble < b + 1) {
                b = ++a + 1;
                continue;
            }
            
            var cInt = (int) cDouble;
            if (cDouble == cInt) {
                triples.add(new PythagoreanTriple(a, b, cInt));
                if (triples.size() == numTriplesToFind) {
                    return triples;
                }
            }
            
            b++;
        }
    }
}