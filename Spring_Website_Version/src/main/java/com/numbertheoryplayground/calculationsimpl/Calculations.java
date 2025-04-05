package com.numbertheoryplayground.calculationsimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import com.fasterxml.jackson.annotation.JsonProperty;

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
        // All integers < 2 are not prime.
        if (input < 2) return false;
        
        /*
        We only need to check if the input is divisible by any primes <= the floor of the
        square root of the input. If it is, then the input is not prime. First, 2 will be
        checked and then odd numbers will be checked since all primes besides 2 are odd.
         */
        
        var maxPossibleFactor = (int) Math.sqrt(input);
        return
            IntStream.concat(
                IntStream.of(2),
                IntStream.iterate(3, l -> l + 2)
            )
            .takeWhile(i -> i <= maxPossibleFactor)
            .noneMatch(i -> isDivisible(input, i));
    }
    
    private static boolean bothArePrime(int a, int b) {
        return isPrime(a) && isPrime(b);
    }
    }
    
    public static int[] getPrimes(int input) {
        int iterationStart = isOdd(input) ? input : input + 1;
        IntStream oddPrimes =
            IntStream.iterate(iterationStart, l -> l + 2)
            .filter(Calculations::isPrime);
        
        // 2 is the only even prime number.
        return
            (input <= 2 ? IntStream.concat(IntStream.of(2), oddPrimes) : oddPrimes)
            .limit(30)
            .toArray();
    }
    
    public static int[] getTwinPrimePairStarts(int input) {
        /*
        Set iterationStart to the first int >= the input that is 1 below a multiple of 6 so
        that we'll be able to iterate through ints that are 1 below a multiple of 6.
         */
        int iterationStart = input;
        while (iterationStart % 6 != 5) iterationStart++;
        
        IntStream pairStarts =
            IntStream.iterate(iterationStart, l -> l + 6)
            .filter(l -> bothArePrime(l, l + 2));

        /*
        All twin prime pairs besides 3 and 5 consist of 1 number that is 1 below a
        multiple of 6 and another number that is 1 above that same multiple of 6.
         */
        return
            (input <= 3 ? IntStream.concat(IntStream.of(3), pairStarts) : pairStarts)
            .limit(20)
            .toArray();
    }
    
    public static List<int[]> getGoldbachPrimePairs(int input) {
        /*
        Check if the input is 4 since 4 is the only even number >= 4 that has 2 in a pair of
        primes that sum to it.
         */
        if (input == 4) {
            return List.of(new int[] { 2, 2 });
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
            .mapToObj(i -> new int[] { i, input - i })
            .toList();
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
    
    public static List<PythagoreanTriple> getPythagoreanTriples(int input) {
        final int NUM_TRIPLES_TO_FIND = 10;
        var triples = new ArrayList<PythagoreanTriple>(NUM_TRIPLES_TO_FIND);
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
                if (triples.size() == NUM_TRIPLES_TO_FIND) {
                    return triples;
                }
            }
            
            b++;
        }
    }
}
