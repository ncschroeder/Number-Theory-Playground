package com.numbertheoryplayground.calculationsimpl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.Calculations.isDivisible;

public class PrimeFactorization {
    static final long MIN_INPUT = 2;
    private static final long MAX_INPUT = ONE_BILLION;
    
    public record FactorAndPower(int factor, int power) {}
    
    public record PfListAndLongString(
        @JsonProperty("pfArr") List<FactorAndPower> pfList,
        @JsonProperty("correspondingNumString") String correspondingLongString
    ) {}
    
    /**
     * The long that this prime factorization is for.
     *
     * Why use a long? Well, there are 2 constructors, 1 of which has a map for a param.
     * 1 place where that one is used is in the constructor for the
     * PrimeFactorization.GcdAndLcmAnswer class at the bottom of this class. The constructor
     * for that class creates a map for the prime factors and powers of the LCM of 2 input
     * longs, and then creates a PrimeFactorization using that map. That PrimeFactorization
     * constructor will then set this field to the product of all the factors raised to their
     * powers. The LCM of 2 longs is at most the product of them. The
     * calculationsimpl.GcdAndLcmAnswer class creates PrimeFactorization.GcdAndLcmAnswer
     * objects and the GCD and LCM section has a max input of 1 billion. The highest possible
     * LCM is 1 billion × (1 billion - 1), which is almost 1 quintillion, which is a number
     * with 19 digits. The max value for an int is 2 billion something and the max value for
     * a long is 9 quintillion something.
     */
    private final long correspondingLong;
    
    /**
     * This list is sorted by factors and immutable.
     */
    private final List<FactorAndPower> factorsAndPowers;
    
    public PrimeFactorization(int input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        correspondingLong = input;
        var factorsAndPowersArrList = new ArrayList<FactorAndPower>();
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
            factorsAndPowersArrList.add(new FactorAndPower(2, power));
        }
        
        if (remaining > 1) {
            for (var possiblePrimeFactor = 3; possiblePrimeFactor <= maxIntToCheck; possiblePrimeFactor += 2) {
                if (isDivisible(remaining, possiblePrimeFactor)) {
                    var power = 0;
                    do {
                        power++;
                        remaining /= possiblePrimeFactor;
                    } while (isDivisible(remaining, possiblePrimeFactor));
                    factorsAndPowersArrList.add(new FactorAndPower(possiblePrimeFactor, power));
                    if (remaining == 1) break;
                }
            }
        }
        
        if (remaining > 1) {
            factorsAndPowersArrList.add(new FactorAndPower(remaining, 1));
        }
        
        factorsAndPowers = List.copyOf(factorsAndPowersArrList);
    }
    
    PrimeFactorization(List<FactorAndPower> factorsAndPowers) {
        this.factorsAndPowers =
            factorsAndPowers
            .stream()
            .sorted(Comparator.comparingLong(FactorAndPower::factor))
            .toList();
        
        long tempCorrespondingLong = 1;
        for (FactorAndPower fp : factorsAndPowers) {
            tempCorrespondingLong *= (long) Math.pow(fp.factor, fp.power);
        }
        correspondingLong = tempCorrespondingLong;
    }
    
    public List<FactorAndPower> toList() {
        return factorsAndPowers;
    }
    
    /**
     * Prime numbers have a prime factorization that consists of a single factor with 1 as its power.
     */
    boolean isForAPrimeNumber() {
        return factorsAndPowers.size() == 1 && factorsAndPowers.getFirst().power == 1;
    }
    
    PfListAndLongString toPfListAndLongString() {
        List<FactorAndPower> pfList = isForAPrimeNumber() ? null : toList();
        return new PfListAndLongString(pfList, Long.toString(correspondingLong));
    }
    
    /**
     * This method finds the sub-factorizations by finding combinations of factors and powers.
     */
    List<PrimeFactorization> getFactorPfs() {
        var factorPfs = new ArrayList<PrimeFactorization>();
        for (FactorAndPower fp : factorsAndPowers) {
            int factor = fp.factor;
            int thisPfPower = fp.power;
            
            /*
            In the 2nd for loop below, we want to iterate through all the PFs that are in
            factorPfs at this point, and not the ones that get added below. Use this variable
            for that.
             */
            int lastPfIndexToUse = factorPfs.size() - 1;
            
            for (var factorPfPower = 1; factorPfPower <= thisPfPower; factorPfPower++) {
                var singleton = List.of(new FactorAndPower(factor, factorPfPower));
                factorPfs.add(new PrimeFactorization(singleton));
                
                for (var i = 0; i <= lastPfIndexToUse; i++) {
                    var newFactorAndPower = new FactorAndPower(factor, factorPfPower);
                    List<FactorAndPower> factorPfFactorsAndPowersList =
                        new ArrayList<>(factorPfs.get(i).factorsAndPowers);
                    
                    IntStream.range(0, factorPfFactorsAndPowersList.size())
                        .filter(j -> factorPfFactorsAndPowersList.get(j).factor == factor)
                        .findFirst()
                        .ifPresentOrElse(
                            indexToUpdate -> factorPfFactorsAndPowersList.set(indexToUpdate, newFactorAndPower),
                            () -> factorPfFactorsAndPowersList.add(newFactorAndPower)
                        );
                    
                    factorPfs.add(new PrimeFactorization(factorPfFactorsAndPowersList));
                }
            }

        }
        /*
        The last PF has all the factors that this PF has and each power is the same as the
        powers in this PF, so it's the same as this PF. We don't want to include that as part
        of the factors.
         */
        factorPfs.removeLast();
        factorPfs.sort(Comparator.comparingLong(pf -> pf.correspondingLong));
        return factorPfs;
    }
    
    List<PfListAndLongString> getFactorPfListsAndLongStrings() {
        return
            getFactorPfs()
            .stream()
            .map(PrimeFactorization::toPfListAndLongString)
            .toList();
    }
    
    private OptionalInt getPowerOf(int factor) {
        return
            factorsAndPowers
            .stream()
            .filter(fp -> fp.factor == factor)
            .mapToInt(FactorAndPower::power)
            .findFirst();
    }
    
    /**
     * This class uses prime factorizations to find the greatest common divisor (GCD) and
     * least common multiple (LCM) of 2 integers. An advantage to having this class be a
     * nested class within the PrimeFactorization class is that we can access the private
     * factorsAndPowers map of the PrimeFactorizations we create in the constructor for
     * this class.
     */
    public static class GcdAndLcmAnswer {

        
        private final List<FactorAndPower> input1PfList;
        private final List<FactorAndPower> input2PfList;
        /**
         * If the GCD of the inputs is 1, this is null since only integers >= 2 have a prime factorization.
         */
        private final PfListAndLongString gcdPfListAndLongString;
        private final PfListAndLongString lcmPfListAndLongString;
        
        GcdAndLcmAnswer(int input1, int input2) {
            assertIsInRange(input1, 0, 0);
            assertIsInRange(input2, 0, 0);
            
            var input1Pf = new PrimeFactorization(input1);
            var input2Pf = new PrimeFactorization(input2);
            
            input1PfList = input1Pf.toList();
            input2PfList = input2Pf.toList();
            var gcdPfFactorsAndPowers = new ArrayList<FactorAndPower>();
            var lcmPfFactorsAndPowers = new ArrayList<FactorAndPower>();
            
            for (FactorAndPower fp : input1Pf.factorsAndPowers) {
                int factor = fp.factor;
                int power1 = fp.power;
                
                input2Pf
                .getPowerOf(factor)
                .ifPresentOrElse(
                    power2 -> {
                        gcdPfFactorsAndPowers.add(new FactorAndPower(factor, Math.min(power1, power2)));
                        lcmPfFactorsAndPowers.add(new FactorAndPower(factor, Math.max(power1, power2)));
                    },
                    () -> lcmPfFactorsAndPowers.add(new FactorAndPower(factor, power1))
                );
            }
            
            // Find the unique prime factors of input2Pf.
            for (FactorAndPower fp : input2Pf.factorsAndPowers) {
                if (input1Pf.getPowerOf(fp.factor).isEmpty()) {
                    lcmPfFactorsAndPowers.add(new FactorAndPower(fp.factor, fp.power));
                }
            }
            
            gcdPfListAndLongString =
                gcdPfFactorsAndPowers.isEmpty()
                ? null
                : new PrimeFactorization(gcdPfFactorsAndPowers).toPfListAndLongString();
            
            lcmPfListAndLongString =
                new PrimeFactorization(lcmPfFactorsAndPowers).toPfListAndLongString();
        }
        
        @JsonProperty("input1PfArr")
        public List<FactorAndPower> getInput1PfList() {
            return input1PfList;
        }
        
        @JsonProperty("input2PfArr")
        public List<FactorAndPower> getInput2PfList() {
            return input2PfList;
        }
        
        @JsonProperty("gcdPfArrAndNumString")
        public PfListAndLongString getGcdPfListAndLongString() {
            return gcdPfListAndLongString;
        }
        
        @JsonProperty("lcmPfArrAndNumString")
        public PfListAndLongString getLcmPfListAndLongString() {
            return lcmPfListAndLongString;
        }
    }
}
