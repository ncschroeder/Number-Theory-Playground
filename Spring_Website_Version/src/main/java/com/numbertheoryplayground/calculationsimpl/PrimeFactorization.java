package com.numbertheoryplayground.calculationsimpl;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.Calculations.isDivisible;

/**
 * The initials PF are used to refer to instances of this class or to prime factorizations in general.
 *
 * Sometimes, instances of this class get marshaled to JSON as part of a response for an HTTP
 * request and sometimes, just the factors and powers list of an instance gets marshaled.
 */
public final class PrimeFactorization {
    public static final int MIN_INPUT = 2;
    public static final int MAX_INPUT = ONE_MILLION;
    
    /**
     * fe and its plural fes are used to refer to instances of this in variable names.
     */
    public record FactorAndExponent(int factor, int exponent) {}
    
    /**
     * The long that this prime factorization is for.
     */
    private final long correspondingLong;
    
    /**
     * An immutable list that's sorted by factors, which is appropriate when marshaling this
     * list to JSON and then sending that to the web page and then displaying the contents
     * of this list.
     */
    private final List<FactorAndExponent> fes;
    
    /**
     * Constructs a PrimeFactorization for the prime factorization of the input.
     */
    public PrimeFactorization(int input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        correspondingLong = input;
        var maxIntToCheck = (int) Math.sqrt(input);
        int remaining = input;

        var tempFes = new ArrayList<FactorAndExponent>();
        /*
        Find all the prime factors and their powers and put these in tempFps. Divide remaining
        by each factor that's found. When remaining becomes 1, the entire prime factorization
        has been found. First, 2 will be checked and then odd numbers will be checked since all
        prime numbers besides 2 are odd.
         */
        
        if (isDivisible(remaining, 2)) {
            var exponent = 0;
            do {
                exponent++;
                remaining /= 2;
            } while (isDivisible(remaining, 2));
            tempFes.add(new FactorAndExponent(2, exponent));
        }
        
        if (remaining > 1) {
            for (var possiblePrimeFactor = 3; possiblePrimeFactor <= maxIntToCheck; possiblePrimeFactor += 2) {
                if (isDivisible(remaining, possiblePrimeFactor)) {
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
     * Constructs a PrimeFactorization for the prime factorization whose factors and powers are
     * in the list provided.
     */
        this.fps =
            fps
            .stream()
            .sorted(Comparator.comparingInt(FactorAndPower::factor))
            .toList();
    public PrimeFactorization(List<FactorAndExponent> fes) {
        
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
     * In the places that marshaled PFs will be used on the front end, if the corresponding
     * number is NOT prime, then that number and the fps will be displayed, since they'll look
     * different from each other. If the corresponding number is prime, then only that number
     * will be displayed since the PF just contains that number as its only factor and the power
     * of it is 1. For example, for a PF with a corresponding number of 2, only 2 would be
     * displayed. For a PF with a corresponding number of 6, 6 and its prime factors, 2 and 3,
     * would be displayed. If a marshaled PF has an fps property of null, then that means that
     * only the corresponding number needs to be displayed.
     */
    @JsonProperty("fes")
    public List<FactorAndExponent> getFesOrNull() {
        return isForAPrimeNumber() ? null : fes;
    }
    
    /**
     * If the factor is in this PF, then an Optional with that factor's power will be returned.
     * Otherwise, an empty Optional will be returned.
     */
    public OptionalInt findPowerOf(int factor) {
        return
            fps
            .stream()
            .filter(fp -> fp.factor == factor)
            .mapToInt(FactorAndPower::power)
            .findFirst();
    }
    
    public boolean containsFactor(int i) {
        return
            fps
            .stream()
            .anyMatch(fp -> fp.factor == i);
    }
}