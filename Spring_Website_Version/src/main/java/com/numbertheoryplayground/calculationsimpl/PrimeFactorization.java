package com.numbertheoryplayground.calculationsimpl;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.Calculations.isDivisible;

public class PrimeFactorization {
    public static final long MIN_INPUT = 2;
    private static final long MAX_INPUT = ONE_BILLION;
    
    public record FactorAndPower(int factor, int power) {}
    
    public record PfListAndInt(
        @JsonProperty("pfArr") List<FactorAndPower> pfList,
        @JsonProperty("correspondingNum") int correspondingInt
    ) {}
    
    private int correspondingInt;
    
    private final SortedMap<Integer, Integer> factorsAndPowers;
    public PrimeFactorization(int input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        correspondingInt = input;
        factorsAndPowers = new TreeMap<>();
        var maxIntToCheck = (int) Math.sqrt(input);
        int remaining = input;

        /*
        Find all the prime factors and their powers and put these in factorsAndPowers. Divide
        remaining by each factor that is found. When remaining becomes 1, the entire prime
        factorization has been found. First 2 will be checked and then odd numbers will be
        checked since all prime numbers besides 2 are odd.
         */
        
        if (isDivisible(remaining, 2)) {
            var power = 0;
            do {
                power++;
                remaining /= 2;
            } while (isDivisible(remaining, 2));
            factorsAndPowers.put(2, power);
            if (remaining == 1) return;
        }
        
        for (var possiblePrimeFactor = 3; possiblePrimeFactor <= maxIntToCheck; possiblePrimeFactor += 2) {
            if (isDivisible(remaining, possiblePrimeFactor)) {
                var power = 0;
                do {
                    power++;
                    remaining /= possiblePrimeFactor;
                } while (isDivisible(remaining, possiblePrimeFactor));
                factorsAndPowers.put(possiblePrimeFactor, power);
                if (remaining == 1) return;
            }
        }
        
        factorsAndPowers.put(remaining, 1);
    }
    
    /**
     * Constructs a new PrimeFactorization to represent the prime factorization whose factors
     * and powers are keys and values, respectively, in the map provided.
     */
    public PrimeFactorization(Map<Integer, Integer> factorsAndPowers) {
        this.factorsAndPowers = new TreeMap<>(factorsAndPowers);
        correspondingInt = 1;
        
        for (Map.Entry<Integer, Integer> e : factorsAndPowers.entrySet()) {
            correspondingInt *= (int) Math.pow(e.getKey(), e.getValue());
        }
    }
    
    public List<FactorAndPower> toList() {
        return
            factorsAndPowers
            .entrySet()
            .stream()
            .map(e -> new FactorAndPower(e.getKey(), e.getValue()))
            .toList();
    }
    
    /**
     * Prime numbers have a prime factorization that consists of a single factor with 1 as its power.
     */
    boolean isForAPrimeNumber() {
        return factorsAndPowers.size() == 1 && factorsAndPowers.containsValue(1);
    }
    
    PfListAndInt toPfListAndInt() {
        return new PfListAndInt(isForAPrimeNumber() ? null : toList(), correspondingInt);
    }
    
    /**
     * This method finds the sub-factorizations by finding combinations of factors and powers.
     */
    public List<PrimeFactorization> getFactorPfs() {
        var factorPfs = new ArrayList<PrimeFactorization>();
        
        factorsAndPowers.forEach((factor, thisPfPower) -> {
            /*
            In the 2nd for loop below, we want to iterate through all the PFs that are in
            factorPfs at this point, and not the ones that get added below. Use this variable
            for that.
             */
            int lastPfIndexToUse = factorPfs.size() - 1;
            
            for (var factorPfPower = 1; factorPfPower <= thisPfPower; factorPfPower++) {
                factorPfs.add(new PrimeFactorization(Map.of(factor, factorPfPower)));
                
                for (var i = 0; i <= lastPfIndexToUse; i++) {
                    var factorPfFactorsAndPowers =
                        new HashMap<Integer, Integer>(factorPfs.get(i).factorsAndPowers);
                    factorPfFactorsAndPowers.put(factor, factorPfPower);
                    factorPfs.add(new PrimeFactorization(factorPfFactorsAndPowers));
                }
            }
        });

        /*
        The last PF has all the factors that this PF has and each power is the same as the
        powers in this PF, so it's the same as this PF. We don't want to include that as part
        of the factors.
         */
        factorPfs.removeLast();
        factorPfs.sort(Comparator.comparingInt(pf -> pf.correspondingInt));
        return factorPfs;
    }
    
    List<PfListAndInt> getFactorPfListsAndInts() {
        return
            getFactorPfs()
            .stream()
            .map(PrimeFactorization::toPfListAndInt)
            .toList();
    }
    
    /**
     * This class uses prime factorizations to find the greatest common divisor (GCD) and
     * least common multiple (LCM) of 2 integers. An advantage to having this class be a
     * nested class within the PrimeFactorization class is that we can access the private
     * factorsAndPowers map of the PrimeFactorizations we create in the constructor for
     * this class.
     */
    public static class GcdAndLcmAnswer {
        @JsonProperty("input1PfArr")
        public final List<FactorAndPower> input1PfList;

        @JsonProperty("input2PfArr")
        public final List<FactorAndPower> input2PfList;
        
        /**
         * If the GCD of the inputs is 1, this is null since only integers >= 2 have a prime factorization.
         */
        @JsonProperty("gcdPfArrAndNum")
        public final PfListAndInt gcdPfListAndInt;
        
        @JsonProperty("lcmPfArrAndNum")
        public final PfListAndInt lcmPfListAndInt;
        
        GcdAndLcmAnswer(int input1, int input2) {
            assertIsInRange(input1, 0, 0);
            assertIsInRange(input2, 0, 0);
            
            var input1Pf = new PrimeFactorization(input1);
            var input2Pf = new PrimeFactorization(input2);
            var gcdPfFactorsAndPowers = new HashMap<Integer, Integer>();
            var lcmPfFactorsAndPowers = new HashMap<Integer, Integer>();
            
            input1PfList = input1Pf.toList();
            input2PfList = input2Pf.toList();
            
            input1Pf.factorsAndPowers.forEach((factor, power1) -> {
                Integer power2 = input2Pf.factorsAndPowers.get(factor);
                if (power2 != null) {
                    gcdPfFactorsAndPowers.put(factor, Math.min(power1, power2));
                    lcmPfFactorsAndPowers.put(factor, Math.max(power1, power2));
                } else {
                    lcmPfFactorsAndPowers.put(factor, power1);
                }
            });
            
            input2Pf.factorsAndPowers.forEach((factor, power) -> {
                if (!input1Pf.factorsAndPowers.containsKey(factor)) {
                    lcmPfFactorsAndPowers.put(factor, power);
                }
            });
            
            gcdPfListAndInt =
                gcdPfFactorsAndPowers.isEmpty()
                ? null
                : new PrimeFactorization(gcdPfFactorsAndPowers).toPfListAndInt();
            
            lcmPfListAndInt =
                new PrimeFactorization(lcmPfFactorsAndPowers).toPfListAndInt();
        }
    }
}
