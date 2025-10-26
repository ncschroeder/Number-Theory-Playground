package com.numbertheoryplayground.calculationsimpl;

import java.util.*;
import java.util.stream.IntStream;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.numbertheoryplayground.InputValidation.*;
import static com.numbertheoryplayground.calculationsimpl.Calculations.isDivisible;

public final class PrimeFactorization {
    static final long MIN_INPUT = 2;
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
    PrimeFactorization(List<FactorAndPower> fps) {
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
    
    /**
     * This method finds PFs of factors of the corresponding int, excluding 1 and the
     * corresponding int, by finding sub-factorizations in this PF and that's done by finding
     * combinations of factors and powers in this PF. The PFs in list returned are sorted by
     * corresponding ints.
     */
    public List<PrimeFactorization> getFactorPfs() {
        var factorPfs = new ArrayList<PrimeFactorization>();
        
        for (FactorAndPower fp : fps) {
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
                    var newFp = new FactorAndPower(factor, factorPfPower);
                    List<FactorAndPower> factorPfFps = new ArrayList<>(factorPfs.get(i).fps);
                    
                    findIndexOfFactor(factorPfFps, factor)
                    .ifPresentOrElse(
                        indexToUpdate -> factorPfFps.set(indexToUpdate, newFp),
                        () -> factorPfFps.add(newFp)
                    );
                    
                    factorPfs.add(new PrimeFactorization(factorPfFps));
                }
            }
        }
        
        /*
        The last PF has all the factors that this PF has and each power is the same as the powers in
        this PF, so it's the same as this PF. We don't want to include that as part of the factors.
         */
        factorPfs.removeLast();
        factorPfs.sort(Comparator.comparingInt(PrimeFactorization::getCorrespondingInt));
        return factorPfs;
    }
    
    private static OptionalInt findIndexOfFactor(List<FactorAndPower> fps, int factor) {
        return
            IntStream.range(0, fps.size())
            .filter(i -> fps.get(i).factor == factor)
            .findFirst();
    }
    
    private OptionalInt findPowerOf(int factor) {
        return
            fps
            .stream()
            .filter(fp -> fp.factor == factor)
            .mapToInt(FactorAndPower::power)
            .findFirst();
    }
    
    private boolean containsFactor(int i) {
        return
            fps
            .stream()
            .anyMatch(fp -> fp.factor == i);
    }
    
    
    /**
     * This class uses prime factorizations to find the greatest common divisor (GCD) and
     * least common multiple (LCM) of 2 ints.
     */
    public static class GcdAndLcmAnswer {
        private final List<FactorAndPower> input1Fps;
        
        private final List<FactorAndPower> input2Fps;
        
        /**
         * If the GCD of the inputs is 1, this is null since only integers > 1 have a prime factorization.
         */
        private final PrimeFactorization gcdPf;
        
        private final PrimeFactorization lcmPf;
        
        GcdAndLcmAnswer(int input1, int input2) {
            final long minInput =
                com.numbertheoryplayground.calculationsimpl.GcdAndLcmAnswer.MIN_INPUT;
            final long maxInput =
                com.numbertheoryplayground.calculationsimpl.GcdAndLcmAnswer.MAX_INPUT;
            assertIsInRange(input1, minInput, maxInput);
            assertIsInRange(input2, minInput, maxInput);
            
            var input1Pf = new PrimeFactorization(input1);
            var input2Pf = new PrimeFactorization(input2);
            input1Fps = input1Pf.getFps();
            input2Fps = input2Pf.getFps();
            var gcdFps = new ArrayList<FactorAndPower>();
            var lcmFps = new ArrayList<FactorAndPower>();
            
            for (FactorAndPower fp : input1Pf.fps) {
                int factor = fp.factor;
                int power1 = fp.power;
                
                input2Pf
                .findPowerOf(factor)
                .ifPresentOrElse(
                    power2 -> {
                        gcdFps.add(new FactorAndPower(factor, Math.min(power1, power2)));
                        lcmFps.add(new FactorAndPower(factor, Math.max(power1, power2)));
                    },
                    () -> lcmFps.add(new FactorAndPower(factor, power1))
                );
            }
            
            for (FactorAndPower fp : input2Pf.fps) {
                if (!input1Pf.containsFactor(fp.factor)) {
                    lcmFps.add(new FactorAndPower(fp.factor, fp.power));
                }
            }
            
            gcdPf = gcdFps.isEmpty() ? null : new PrimeFactorization(gcdFps);
            lcmPf = new PrimeFactorization(lcmFps);
        }
        
        public List<FactorAndPower> getInput1Fps() {
            return input1Fps;
        }
        
        public List<FactorAndPower> getInput2Fps() {
            return input2Fps;
        }
        
        @JsonIgnore
        public Optional<PrimeFactorization> getGcdPf() {
            return Optional.ofNullable(gcdPf);
        }
        
        @JsonProperty("gcdPf")
        public Optional<PrimeFactorization.Dto> getGcdPfDto() {
            return getGcdPf().map(PrimeFactorization::toDto);
        }
        
        @JsonIgnore
        public PrimeFactorization getLcmPf() {
            return lcmPf;
        }
        
        @JsonProperty("lcmPf")
        public PrimeFactorization.Dto getLcmPfDto() {
            return lcmPf.toDto();
        }
    }
}