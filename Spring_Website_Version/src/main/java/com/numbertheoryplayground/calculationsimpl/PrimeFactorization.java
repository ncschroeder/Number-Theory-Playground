package com.numbertheoryplayground.calculationsimpl;

import java.util.*;
import java.util.stream.IntStream;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.Calculations.isDivisible;

public final class PrimeFactorization {
    static final long MIN_INPUT = 2;
    private static final long MAX_INPUT = TEN_THOUSAND;
    
    public record FactorAndPower(int factor, int power) {}
    
    public record FactorAndPowerListAndInt(
        @JsonProperty("fpArr") List<FactorAndPower> fpList,
        @JsonProperty("correspondingNum") int correspondingInt
    ) {}
    
    /**
     * The int that this prime factorization is for.
     */
    private final int correspondingInt;
    
    /**
     * An immutable list that's sorted by factors, which is appropriate when marshaling this
     * list to JSON and then sending that to the web page and then displaying the contents
     * of this list.
     */
    private final List<FactorAndPower> factorsAndPowers;
    
    /**
     * Constructs a PrimeFactorization for the prime factorization of the input.
     */
    public PrimeFactorization(int input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        correspondingInt = input;
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
        
        var tempCorrespondingInt = 1;
        for (FactorAndPower fp : factorsAndPowers) {
            tempCorrespondingInt *= (int) Math.pow(fp.factor, fp.power);
        }
        correspondingInt = tempCorrespondingInt;
    }
    
    public List<FactorAndPower> getFactorsAndPowers() {
        return factorsAndPowers;
    }
    
    boolean isForAPrimeNumber() {
        return factorsAndPowers.size() == 1 && factorsAndPowers.getFirst().power == 1;
    }
    
    FactorAndPowerListAndInt toFpListAndInt() {
        List<FactorAndPower> fpList = isForAPrimeNumber() ? null : factorsAndPowers;
        return new FactorAndPowerListAndInt(fpList, correspondingInt);
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
            In the 2nd for loop below, we want to iterate through all the PFs that are in factorPfs
            at this point, and not the ones that get added below. Use this variable for that.
             */
            int lastPfIndexToUse = factorPfs.size() - 1;
            
            for (var factorPfPower = 1; factorPfPower <= thisPfPower; factorPfPower++) {
                var singleton = List.of(new FactorAndPower(factor, factorPfPower));
                factorPfs.add(new PrimeFactorization(singleton));
                
                for (var i = 0; i <= lastPfIndexToUse; i++) {
                    var newFactorAndPower = new FactorAndPower(factor, factorPfPower);
                    List<FactorAndPower> factorPfFactorsAndPowers =
                        new ArrayList<>(factorPfs.get(i).factorsAndPowers);
                    
                    findIndexOfFactor(factorPfFactorsAndPowers, factor)
                    .ifPresentOrElse(
                        indexToUpdate -> factorPfFactorsAndPowers.set(indexToUpdate, newFactorAndPower),
                        () -> factorPfFactorsAndPowers.add(newFactorAndPower)
                    );
                    
                    factorPfs.add(new PrimeFactorization(factorPfFactorsAndPowers));
                }
            }
        }
        
        /*
        The last PF has all the factors that this PF has and each power is the same as the powers in
        this PF, so it's the same as this PF. We don't want to include that as part of the factors.
         */
        factorPfs.removeLast();
        factorPfs.sort(Comparator.comparingInt(pf -> pf.correspondingInt));
        return factorPfs;
    }
    
    private static OptionalInt findIndexOfFactor(List<FactorAndPower> fps, int factor) {
        return
            IntStream.range(0, fps.size())
            .filter(i -> fps.get(i).factor == factor)
            .findFirst();
    }
    
    List<FactorAndPowerListAndInt> getFactorFpListsAndInts() {
        return
            getFactorPfs()
            .stream()
            .map(PrimeFactorization::toFpListAndInt)
            .toList();
    }
    
    private OptionalInt findPowerOf(int factor) {
        return
            factorsAndPowers
            .stream()
            .filter(fp -> fp.factor == factor)
            .mapToInt(FactorAndPower::power)
            .findFirst();
    }
    
    private boolean containsFactor(int i) {
        return
            factorsAndPowers
            .stream()
            .anyMatch(fp -> fp.factor == i);
    }
    
    
    /**
     * This class uses prime factorizations to find the greatest common divisor (GCD) and
     * least common multiple (LCM) of 2 integers. An advantage to having this class be a
     * nested class within the PrimeFactorization class is that we can access the private
     * factorsAndPowers list of the PrimeFactorizations we create in the constructor for
     * this class.
     */
    public static class GcdAndLcmAnswer {
        private final List<FactorAndPower> input1FpList;
        private final List<FactorAndPower> input2FpList;
        /**
         * If the GCD of the inputs is 1, this is null since only integers > 1 have a prime factorization.
         */
        private final FactorAndPowerListAndInt gcdFpListAndInt;
        private final FactorAndPowerListAndInt lcmFpListAndInt;
        
        GcdAndLcmAnswer(int input1, int input2) {
            assertIsInRange(input1, 0, 0);
            assertIsInRange(input2, 0, 0);
            
            var input1Pf = new PrimeFactorization(input1);
            var input2Pf = new PrimeFactorization(input2);
            input1FpList = input1Pf.getFactorsAndPowers();
            input2FpList = input2Pf.getFactorsAndPowers();
            var gcdPfFactorsAndPowers = new ArrayList<FactorAndPower>();
            var lcmPfFactorsAndPowers = new ArrayList<FactorAndPower>();
            
            for (FactorAndPower fp : input1Pf.factorsAndPowers) {
                int factor = fp.factor;
                int power1 = fp.power;
                
                input2Pf
                .findPowerOf(factor)
                .ifPresentOrElse(
                    power2 -> {
                        gcdPfFactorsAndPowers.add(new FactorAndPower(factor, Math.min(power1, power2)));
                        lcmPfFactorsAndPowers.add(new FactorAndPower(factor, Math.max(power1, power2)));
                    },
                    () -> lcmPfFactorsAndPowers.add(new FactorAndPower(factor, power1))
                );
            }
            
            for (FactorAndPower fp : input2Pf.factorsAndPowers) {
                if (!input1Pf.containsFactor(fp.factor)) {
                    lcmPfFactorsAndPowers.add(new FactorAndPower(fp.factor, fp.power));
                }
            }
            
            gcdFpListAndInt =
                gcdPfFactorsAndPowers.isEmpty()
                ? null
                : new PrimeFactorization(gcdPfFactorsAndPowers).toFpListAndInt();
            
            lcmFpListAndInt =
                new PrimeFactorization(lcmPfFactorsAndPowers).toFpListAndInt();
        }
        
        @JsonProperty("input1FpArr")
        public List<FactorAndPower> getInput1FpList() {
            return input1FpList;
        }
        
        @JsonProperty("input2FpArr")
        public List<FactorAndPower> getInput2FpList() {
            return input2FpList;
        }
        
        @JsonProperty("gcdFpArrAndNum")
        public FactorAndPowerListAndInt getGcdFpListAndInt() {
            return gcdFpListAndInt;
        }
        
        @JsonProperty("lcmFpArrAndNum")
        public FactorAndPowerListAndInt getLcmFpListAndInt() {
            return lcmFpListAndInt;
        }
    }
}
