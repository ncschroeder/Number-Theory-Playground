package com.numbertheoryplayground.calculationsimpl;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.Calculations.*;

/**
 * The initials PF are used to refer to instances of this class and to prime factorizations
 * in general. Sometimes, instances of this class get marshaled to JSON as part of a response
 * for an HTTP request and sometimes, just the fes list of an instance gets marshaled.
 */
public final class PrimeFactorization implements Iterable<PrimeFactorization.FactorAndExponent> {
    public static final int MIN_INPUT = 2;
    public static final int MAX_INPUT = ONE_MILLION;
    
    /**
     * fe and its plural fes are used to refer to instances of this in variable names.
     */
    public record FactorAndExponent(int factor, int exponent) {}
    
    /**
     * The long that this PF is for.
     */
    private final long correspondingLong;
    
    /*
    Why use a long? Well, this class has 2 constructors, 1 of which has a list for a param.
    1 place where that one is used is in the constructor for the
    gcdandlcm.PrimeFactorizationAnswer class. That constructor creates a list of the prime
    factors and powers of the LCM of 2 input ints, and then creates a PrimeFactorization using
    that list. That PrimeFactorization constructor will then set the correspondingLong field
    to the product of all factors raised to their powers. The LCM of 2 ints is at most the
    product of them. The GCD and LCM section has a max input of 1 million. The highest possible
    LCM is 1 million × (1 million − 1), which is almost 1 trillion. The max value for an int is
    2 billion something.
     */
    
    /**
     * An immutable list of the factors and exponents in this PF. This is sorted by factors,
     * which is appropriate for marshaling this to JSON, sending that to the webpage, and
     * displaying the contents of this list.
     */
    private final List<FactorAndExponent> fes;
    
    /**
     * Constructs a PrimeFactorization for the prime factorization of the input.
     */
    public PrimeFactorization(int input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        correspondingLong = input;
        int remaining = input;
        /*
        Use a mutable ArrayList and create an immutable copy at the end.
        The max amount of unique prime factors is 7.
         */
        var tempFes = new ArrayList<FactorAndExponent>(7);
        
        /*
        Find all the prime factors and their powers and put these in tempFes. Divide remaining
        by each factor that's found. If remaining becomes 1, then the entire PF has been found.
        First, we'll check if 2 is a factor then check odd numbers since all prime numbers
        besides 2 are odd. We only need to check odd numbers up to the square root of the input.
         */
        
        if (isEven(remaining)) {
            var exponent = 0;
            do {
                exponent++;
                remaining /= 2;
            } while (isDivisible(remaining, 2));
            tempFes.add(new FactorAndExponent(2, exponent));
        }
        
        if (remaining > 1) {
            var maxPossibleFactorToCheck = (int) Math.sqrt(input);
            for (var possibleFactor = 3; possibleFactor <= maxPossibleFactorToCheck; possibleFactor += 2) {
                if (isDivisible(remaining, possibleFactor)) {
                    var exponent = 0;
                    do {
                        exponent++;
                        remaining /= possibleFactor;
                    } while (isDivisible(remaining, possibleFactor));
                    tempFes.add(new FactorAndExponent(possibleFactor, exponent));
                    if (remaining == 1) break;
                }
            }
        }
        
        if (remaining > 1) {
            tempFes.add(new FactorAndExponent(remaining, 1));
        }
        
        fes = List.copyOf(tempFes);
    }
    
    /**
     * Constructs a PrimeFactorization for the prime factorization whose factors and
     * exponents are in the list provided, which should be sorted by factors.
     */
    public PrimeFactorization(List<FactorAndExponent> fes) {
        this.fes = List.copyOf(fes);
        
        // Use a temp variable to allow correspondingLong to be final.
        var tempCorrespondingLong = 1L;
        for (FactorAndExponent fp : fes) {
            tempCorrespondingLong *= (long) Math.pow(fp.factor, fp.exponent);
        }
        correspondingLong = tempCorrespondingLong;
    }
    
    @JsonProperty("correspondingNum")
    public long getCorrespondingLong() {
        return correspondingLong;
    }
    
    @JsonIgnore
    public List<FactorAndExponent> getFes() {
        return fes;
    }
    
    @JsonIgnore
    public boolean isForAPrimeNumber() {
        return fes.size() == 1 && fes.getFirst().exponent == 1;
    }
    
    /**
     * If this PF is for a prime number, then the corresponding long and the representation of
     * this PF would look the same, so the webpage doesn't need to display both.
     */
    @JsonProperty("fes")
    public List<FactorAndExponent> getFesOrNull() {
        return isForAPrimeNumber() ? null : fes;
    }
    
    @Override
    public Iterator<FactorAndExponent> iterator() {
        return fes.iterator();
    }
    
    public Optional<Integer> getExponentOf(int possibleFactor) {
        return
            fes
            .stream()
            .filter(fe -> fe.factor == possibleFactor)
            .findFirst()
            .map(FactorAndExponent::exponent);
    }
    
    public boolean containsFactor(int possibleFactor) {
        return fes.stream().anyMatch(fe -> fe.factor == possibleFactor);
    }
}