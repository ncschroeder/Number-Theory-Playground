package com.numbertheoryplayground.calculationsimpl;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.Calculations.isDivisible;

public final class PrimeFactorization {
    public static final long MIN_INPUT = 2;
    private static final long MAX_INPUT = TEN_THOUSAND;
    
    public record FactorAndPower(int factor, int power) {}
    
    /**
     * The int that this prime factorization is for.
     */
    private final int correspondingInt;
    
    /**
     * An immutable list that's sorted by factors, which is appropriate when marshaling this
     * list to JSON and then sending that to the web page and then displaying the contents
     * of this list.
     */
    private final List<FactorAndPower> fps;
    
    /**
     * Constructs a PrimeFactorization for the prime factorization of the input.
     */
    public PrimeFactorization(int input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        correspondingInt = input;
        var tempFps = new ArrayList<FactorAndPower>();
        var maxIntToCheck = (int) Math.sqrt(input);
        int remaining = input;

        /*
        Find all the prime factors and their powers and put these in tempFps. Divide remaining
        by each factor that's found. When remaining becomes 1, the entire prime factorization
        has been found. First, 2 will be checked and then odd numbers will be checked since all
        prime numbers besides 2 are odd.
         */
        
        if (isDivisible(remaining, 2)) {
            var power = 0;
            do {
                power++;
                remaining /= 2;
            } while (isDivisible(remaining, 2));
            tempFps.add(new FactorAndPower(2, power));
        }
        
        if (remaining > 1) {
            for (var possiblePrimeFactor = 3; possiblePrimeFactor <= maxIntToCheck; possiblePrimeFactor += 2) {
                if (isDivisible(remaining, possiblePrimeFactor)) {
                    var power = 0;
                    do {
                        power++;
                        remaining /= possiblePrimeFactor;
                    } while (isDivisible(remaining, possiblePrimeFactor));
                    tempFps.add(new FactorAndPower(possiblePrimeFactor, power));
                    if (remaining == 1) break;
                }
            }
        }
        
        if (remaining > 1) {
            tempFps.add(new FactorAndPower(remaining, 1));
        }
        
        fps = List.copyOf(tempFps);
    }
    
    /**
     * Constructs a PrimeFactorization for the prime factorization whose factors and powers are
     * in the list provided.
     */
    public PrimeFactorization(List<FactorAndPower> fps) {
        this.fps =
            fps
            .stream()
            .sorted(Comparator.comparingInt(FactorAndPower::factor))
            .toList();
        
        var tempCorrespondingInt = 1;
        for (FactorAndPower fp : fps) {
            tempCorrespondingInt *= (int) Math.pow(fp.factor, fp.power);
        }
        correspondingInt = tempCorrespondingInt;
    }
    
    public int getCorrespondingInt() {
        return correspondingInt;
    }
    
    public List<FactorAndPower> getFps() {
        return fps;
    }
    
    public boolean isForAPrimeNumber() {
        return fps.size() == 1 && fps.getFirst().power == 1;
    }
    
    /**
     * Contains data that'll be marshaled to JSON and sent to the front end.
     *
     * In the places this'll be used on the front end, if the corresponding num is NOT prime,
     * then that number and the fps will be displayed, since they'll look different from each
     * other. If the corresponding number is prime, then we only need to display that number
     * since the PF just contains that number as its only factor and the power of it is 1.
     * For example, if a PF instance is created for 2, we only need to display 2. If a PF
     * instance is created for 6, we would display that number and its prime factors, which
     * are 2 and 3.
     */
    public final class Dto {
        @JsonProperty("correspondingNum")
        public int getCorrespondingInt() {
            return correspondingInt;
        }
        
        public List<FactorAndPower> getFps() {
            return isForAPrimeNumber() ? null : fps;
        }
    }
    
    public Dto toDto() {
        return new Dto();
    }
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