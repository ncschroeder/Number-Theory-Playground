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
    
    static boolean isPrime(int input) {
        if (input <= 1) return false;
        if (input <= 3) return true;
        if (isEven(input)) return false;
        
        var maxPossibleFactorToCheck = (int) Math.sqrt(input);
        for (var i = 3; i <= maxPossibleFactorToCheck; i += 2) {
            if (isDivisible(input, i)) return false;
        }
        return true;
    }
    
    /**
     * Returns a list of the first 30 primes ≥ the input.
     */
    public static List<Integer> getPrimes(int input) {
        assertIsInRange(input, 0, 100_000);
        
        final int numPrimesToFind = 30;
        var primes = new ArrayList<Integer>(numPrimesToFind);
        if (input < 2) primes.add(2);
        
        int possiblePrime = isOdd(input) ? input : input + 1;
        while (true) {
            if (isPrime(possiblePrime)) {
                primes.add(possiblePrime);
                if (primes.size() == numPrimesToFind) {
                    return primes;
                }
            }
            possiblePrime += 2;
        }
    }
    
    
    public record SemiprimeData(int semiprime, int primeFactor1, int primeFactor2) {}
    
    /**
     * If the input is a semiprime, then a SemiprimeData for it gets returned.
     * Otherwise, null gets returned.
     */
    private static SemiprimeData checkIfSemiprime(int input) {
        /*
        First, we need to find the first int factor of the input that's > 1 and < the input.
        Just like with the algorithm for determining if an int is prime, we only need to check
        primes ≤ the square root of the input. If we don't find a factor, then that means that
        the input is prime and not semiprime.
         */
        if (isEven(input)) {
            return isPrime(input / 2) ? new SemiprimeData(input, 2, input / 2) : null;
        }
        
        var maxPossibleFactorToCheck = (int) Math.sqrt(input);
        for (var possibleFactor1 = 3; possibleFactor1 <= maxPossibleFactorToCheck; possibleFactor1 += 2) {
            if (isDivisible(input, possibleFactor1)) {
                int factor2 = input / possibleFactor1;
                return
                    possibleFactor1 == factor2 || isPrime(factor2)
                    ? new SemiprimeData(input, possibleFactor1, factor2)
                    : null;
            }
        }
        
        return null;
    }
    
    /**
     * Returns a list of SemiprimeDatas for the first 20 semiprimes ≥ the input.
     */
    public static List<SemiprimeData> getSemiprimesData(int input) {
        assertIsInRange(input, 0, 100_000);
        
        final int numSemiprimesToFind = 20;
        var semiprimesData = new ArrayList<SemiprimeData>(numSemiprimesToFind);
        
        for (var i = input; ; i++) {
            var possibleSemiprimeData = checkIfSemiprime(i);
            if (possibleSemiprimeData != null) {
                semiprimesData.add(possibleSemiprimeData);
                if (semiprimesData.size() == numSemiprimesToFind) {
                    return semiprimesData;
                }
            }
        }
    }
    
    
    /**
     * Finds the first 20 twin prime pairs where the lowest number in the pair is ≥ the input.
     * For example, if the input is 3, then the pair 3 and 5 will be the first one found. If
     * the input is 4, then the pair 5 and 7 will be the first one found. A list that contains
     * the lowest numbers of those pairs gets returned.
     */
    public static List<Integer> getTwinPrimePairStarts(int input) {
        assertIsInRange(input, 0, 100_000);
        
        final int numPairsToFind = 20;
        var pairStarts = new ArrayList<Integer>(numPairsToFind);
        /*
        All twin prime pairs besides 3 and 5 consist of 1 number that's 1 below a multiple of 6
        and another number that's 1 above that same multiple of 6. Set possiblePairStart to the
        first int ≥ the input that's 1 below a multiple of 6 so that we'll be able to iterate
        through ints that are 1 below a multiple of 6.
         */
        if (input <= 3) pairStarts.add(3);
        int possiblePairStart = input;
        while (possiblePairStart % 6 != 5) possiblePairStart++;
        
        while (true) {
            if (isPrime(possiblePairStart) && isPrime(possiblePairStart + 2)) {
                pairStarts.add(possiblePairStart);
                if (pairStarts.size() == numPairsToFind) {
                    return pairStarts;
                }
            }
            possiblePairStart += 6;
        }
    }
    
    /**
     * lowest ints of those pairs.
     * Find the pairs of primes that sum to the input and returns a list that contains the
     */
    public static List<Integer> getGoldbachPrimePairStarts(int input) {
        assertIsInRange(input, 4, 10_000);
        if (isOdd(input)) {
            throw InvalidInputNumberException.getInstance();
        }
        
        /*
        Check if the input is 4 since 4 is the only even number ≥ 4 that has 2 in a pair of
        primes that sum to it.
         */
        if (input == 4) return List.of(2);
        
        /*
        Check pairs of odd numbers that sum to the input for primality. The iterating only
        needs to go up to the floor of half of the input. After that point, checks for
        primality will be done on pairs of numbers that have already been checked for primality.
         */
        var pairStarts = new ArrayList<Integer>();
        int maxPossiblePairStart = input / 2;
        
        for (var i = 3; i <= maxPossiblePairStart; i += 2) {
            if (isPrime(i) && isPrime(input - i)) {
                pairStarts.add(i);
            }
        }
        
        return pairStarts;
    }
    
    public record PythagoreanTriple(int a, int b, int c) {
        /*
        I want the JSON property to be "isPrimitive" but the Jackson JSON mapper
        removes "is" by default.
         */
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
     * Returns a list of triple objects for the first 10 Pythagorean triples where the short leg
     * length, the lowest number in the triple, is ≥ the input. For example, if the input is 3,
     * then an object for the triple 3, 4, and 5 will be the first one. If the input is 4, then
     * an object for the triple 5, 12, and 13 will be the first one. The algorithm I came up with
     * first tries to find triples where the short leg length equals the input and then tries to
     * find triples where the short leg equals the input + 1, and so on until 10 are found.
     */
    public static List<PythagoreanTriple> getPythagTriples(int input) {
        assertIsInRange(input, 0, 100);
        
        final int numTriplesToFind = 10;
        var triples = new ArrayList<PythagoreanTriple>(numTriplesToFind);
        // a and b and for the short and long leg lengths, respectively.
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