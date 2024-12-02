package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;
import static com.nicholasschroeder.numbertheoryplayground.Divisibility.*;

/**
 * Class that can be instantiated and also has static members related to prime factorizations and the
 * section for it.
 */
public final class PrimeFactorization {
    private static final Supplier<String> infoSupplier = () -> """
The Fundamental Theorem of Arithmetic says that every integer > 1 can be expressed as the product of
prime numbers. The prime factorization (PF) of an integer is an expression of the prime numbers whose
product is that integer. For example; the PF of 5 is just 5, the PF of 25 is 5^2, and the PF of 12,250
is 2 x 5^3 x 7^2. There are some interesting applications for this. Visit the "Divisibility" or
"GCD and LCM" sections for some applications.

The input number with the highest amount of prime factors is 2^53, or 9,007,199,254,740,992; the
largest power of 2 <= 10 quadrillion. An input number with the highest amount of unique prime factors
is 304,250,263,527,210. This number is the product of the first 13 prime numbers so it has 13 unique
prime factors and its PF is 2 x 3 x 5 x 7 x 11 x 13 x 17 x 19 x 23 x 29 x 31 x 37 x 41. You could also
multiply that number by 2 or 3 and those numbers are <= the max input and have the same amount of
unique prime factors.""";
    
    public static final long MIN_INPUT = 2;
    public static final long MAX_INPUT = TEN_QUADRILLION;
    
    /**
     * The BigInteger that this prime factorization is for.
     *
     * Why use a BigInteger? Well, there are 2 constructors, 1 of which has a map for a param. 1 place
     * where that one is used is in constructor for the GcdAndLcmAnswer class at the bottom of this
     * class. The GcdAndLcmAnswer constructor creates a map for the prime factors and powers of the
     * LCM of 2 input longs, and then creates a PrimeFactorization using that map. That PrimeFactorization
     * constructor will then set this field to the product of all the factors raised to their powers.
     * The LCM of 2 longs is at most the product of them. The GCD and LCM class creates GcdAndLcmAnswer
     * objects and the GCD and LCM section has a max input of 5 quadrillion. The highest possible LCM
     * is described in the string returned by gcdAndLcmInfoSupplier below and is 5 quadrillion times
     * (5 quadrillion - 1), which is almost 25 nonillion, which is a number with 32 digits. The max
     * value for a long is 9 quintillion something, which is a relatively small number with 19 digits.
     */
    private BigInteger correspondingBigInt;
    
    /**
     * A map whose keys are prime factors and values are the corresponding powers for this prime
     * factorization. A SortedMap is used so that the entries are ordered by the factors, which is
     * appropriate for the string representation of this object.
     */
    private final SortedMap<Long, Integer> factorsAndPowers;

    /**
     * Constructs a new object to represent the prime factorization of the input.
     */
    public PrimeFactorization(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        correspondingBigInt = BigInteger.valueOf(input);
        factorsAndPowers = new TreeMap<>();
        var maxLongToCheck = (long) Math.sqrt(input);
        long remaining = input;

        /*
        Find all the prime factors and their powers and put these in factorsAndPowers. Divide remaining
        by each prime factor that is found. When remaining becomes 1, the entire prime factorization has
        been found. First 2 will be checked and then odd numbers will be checked since all prime numbers
        besides 2 are odd.
         */
        
        if (isDivisible(remaining, 2)) {
            var power = 0;
            do {
                power++;
                remaining /= 2;
            } while (isDivisible(remaining, 2));
            factorsAndPowers.put(2L, power);
            if (remaining == 1) return;
        }
        
        for (var possiblePrimeFactor = 3L; possiblePrimeFactor <= maxLongToCheck; possiblePrimeFactor += 2) {
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
     * Constructs a new PrimeFactorization to represent the prime factorization whose factors and powers
     * are keys and values, respectively, in the map provided.
     */
    public PrimeFactorization(Map<Long, Integer> factorsAndPowers) {
        this.factorsAndPowers = new TreeMap<>(factorsAndPowers);
        correspondingBigInt = BigInteger.ONE;
        
        for (Map.Entry<Long, Integer> e : factorsAndPowers.entrySet()) {
            BigInteger multiplicand = BigInteger.valueOf((long) Math.pow(e.getKey(), e.getValue()));
            correspondingBigInt = correspondingBigInt.multiply(multiplicand);
        }
    }
    
    public BigInteger getCorrespondingBigInt() {
        return correspondingBigInt;
    }
    
    public String getCorrespondingBigIntString() {
        return toStringWithCommas(correspondingBigInt);
    }
    
    /**
     * Returns a list of map entries where each key is a factor and each value is the corresponding
     * power in this prime factorization.
     *
     * According to the documentation, the entrySet method "returns a Set view of the mappings
     * contained in this map. The set is backed by the map, so changes to the map are reflected in
     * the set, and vice-versa." That's just kinda scary to me, so I'll have this method return an
     * unmodifiable list of immutable copies of entries in the entry set.
     */
    public List<Map.Entry<Long, Integer>> getFactorsAndPowers() {
        return
            factorsAndPowers
            .entrySet()
            .stream()
            .map(e -> Map.entry(e.getKey(), e.getValue()))
            .toList();
    }

    /**
     * Returns a string that represents this object the same way that the first info paragraph at the
     * top represents PFs. That paragraph says "the PF of 5 is just 5, the PF of 25 is 5^2, and the PF
     * of 12,250 is 2 x 5^3 x 7^2".
     */
    @Override
    public String toString() {
        return
            factorsAndPowers
            .entrySet()
            .stream()
            .map(e -> {
                var factorString = toStringWithCommas(e.getKey());
                int power = e.getValue();
                return power == 1 ? factorString : String.format("%s^%d", factorString, power);
            })
            .collect(Collectors.joining(" x "));
    }
    
    /**
     * Prime numbers have a prime factorization that consists of a single factor with 1 as its power.
     */
    public boolean isForAPrimeNumber() {
        return factorsAndPowers.size() == 1 && factorsAndPowers.containsValue(1);
    }

    public String toStringWithCorrespondingBigInt() {
        return
            isForAPrimeNumber()
            ? getCorrespondingBigIntString()
            : String.format("%s (%s)", this, getCorrespondingBigIntString());
    }
    
    public String getInfoSentence() {
        return String.format("The PF of %s is %s.", getCorrespondingBigIntString(), this);
    }
    
    public static final Supplier<String> factorsInfoSupplier = () -> """
The factors of an integer can be found by looking at its prime factorization (PF). Let's have a
variable i and let it represent an integer > 1. First, you can find how many factors i has by looking
at i's PF, taking all the powers of the factors, adding 1 to each, and then multiplying all these
together. For example, the PF of 36 is 2^2 x 3^2. The powers are 2 and 2, so there are 3 x 3 = 9
factors. However, that count includes 1 and the number that the PF is for (36 in this case). If you
want to exclude those, then subtract 2. That would give us 7 factors. You can find the factors of i by
finding all the PFs within i's PF, or the "sub-factorizations", as I like to call them. For 2^2 x 3^2,
the sub-factorizations are 2, 3, 2^2 (4), 2 x 3 (6), 3^2 (9), 2^2 x 3 (12), and 2 x 3^2 (18).""";

    /**
     * This method calculates the number of factors and returns a string that says the number
     * of factors and how it was calculated.
     */
        var powerStrings = new ArrayList<String>(factorsAndPowers.size());
    public String getNumFactorsInfo() {
        var numFactors = 1;
        
        for (int power : factorsAndPowers.values()) {
            powerStrings.add(String.format("(%d + 1)", power));
            numFactors *= power + 1;
        }
                
        return String.format(
            "By looking at the power%s, we can see there are %s = %s factors. If 1 and %s are " +
                "excluded then there are %s factors.",
            factorsAndPowers.size() == 1 ? "" : "s",
            String.join(" x ", powerStrings),
            toStringWithCommas(numFactors),
            getCorrespondingBigIntString(),
            toStringWithCommas(numFactors - 2)
        );
    }
    
    /**
     * This method finds the sub-factorizations by finding combinations of factors and powers.
     */
    public List<PrimeFactorization> getFactorPfs() {
        var factorPfs = new ArrayList<PrimeFactorization>();
        
        factorsAndPowers.forEach((factor, thisPfPower) -> {
            /*
            In the 2nd for loop below, we want to iterate through all the PFs that are in factorPfs
            at this point, and not the ones that get added below. Use this variable for that.
             */
            int lastPfIndexToUse = factorPfs.size() - 1;
            
            for (var factorPfPower = 1; factorPfPower <= thisPfPower; factorPfPower++) {
                factorPfs.add(new PrimeFactorization(Map.of(factor, factorPfPower)));
                
                for (var i = 0; i <= lastPfIndexToUse; i++) {
                    var factorPfFactorsAndPowers =
                        new HashMap<Long, Integer>(factorPfs.get(i).factorsAndPowers);
                    factorPfFactorsAndPowers.put(factor, factorPfPower);
                    factorPfs.add(new PrimeFactorization(factorPfFactorsAndPowers));
                }
            }
        });
        
        /*
        The last PF has all the factors that this PF has and each power is the same as the powers in
        this PF, so it's the same as this PF. We don't want to include that as part of the factors.
         */
        factorPfs.removeLast();
        factorPfs.sort(Comparator.comparing(PrimeFactorization::getCorrespondingBigInt));
        return factorPfs;
    }
    
    public Stream<String> getFactorPfStrings() {
        return getFactorPfs().stream().map(PrimeFactorization::toStringWithCorrespondingBigInt);
    }
    
    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Prime Factorization",
                MIN_INPUT,
                MAX_INPUT,
                "get its prime factorization",
                "prime factorizations",
                infoSupplier.get()
            );
        }
        
        @Override
        public String getCliAnswer(long input) {
            return NTPCLI.insertNewLines(new PrimeFactorization(input).getInfoSentence());
        }
        
        @Override
        public List<Component> getGuiComponents(long input) {
            return List.of(
                NTPGUI.createCenteredLabel(
                    new PrimeFactorization(input).getInfoSentence(),
                    NTPGUI.ANSWER_CONTENT_FONT
                )
            );
        }
    }
    
    public static final Supplier<String> gcdAndLcmInfoSupplier = () -> """
The GCD and LCM of 2 integers can be found by looking at their prime factorizations (PFs). If those
integers don't have any common prime factors, then the GCD is 1. If they do have common prime factors,
then the GCD PF consists of all the common prime factors and the power of each factor is the min of
the powers of that factor in the 2 PFs. The LCM PF consists of all factors that are in either of the
PFs of the 2 integers. If a factor is in both PFs then the power of that factor in the LCM PF is the
max of the powers of that factor in the 2 PFs. If a factor is unique to one of the PFs then that
factor and its power are in the LCM PF.

Let's find the GCD and LCM of 6 and 35 using their PFs. The PF of 6 is 2 x 3 and the PF of 35 is 5 x 7.
There are no common prime factors so the GCD is 1. The LCM PF is 2 x 3 x 5 x 7, which is 210.

Let's find the GCD and LCM of 54 and 99. The PF of 54 is 2 x 3^3 and the PF of 99 is 3^2 x 11. 3 is
the only common prime factor and the min power of it is 2 so the GCD PF is 3^2, which is 9. The max
power of 3 is 3 so the LCM PF consists of 3^3. The LCM PF is 2 x 3^3 x 11, which is 594.

The input integers whose LCM is the highest are 5,000,000,000,000,000, the max input, and
4,999,999,999,999,999, the max input - 1. The LCM is 24,999,999,999,999,995,000,000,000,000,000, or
24 nonillion 999 octillion 999 septillion 999 sextillion 999 quintillion 995 quadrillion! It has 32
digits. Trillion is before quadrillion. 

A pair of input integers whose LCM might have the highest amount of unique prime factors is
304,250,263,527,210, the product of the first 13 prime numbers, and 133,869,006,807,307, the product
of the next 8 prime numbers. The LCM is 40,729,680,599,249,024,150,621,323,470, or 40 octillion ...
It has 29 digits and 21 unique prime factors and its PF is
2 x 3 x 5 x 7 x 11 x 13 x 17 x 19 x 23 x 29 x 31 x 37 x 41 x 47 x 53 x 59 x 61 x 67 x 71 x 73!
Other pairs of input integers have the same LCM, such as that first input integer divided by 2 and the
second input integer multiplied by 2.""";
    
    /**
     * This class uses prime factorizations to find the greatest common divisor (GCD) and least common
     * multiple (LCM) of 2 integers. An advantage to having this class be a nested class within the
     * PrimeFactorization class is that we can access the private factorsAndPowers map of the
     * PrimeFactorizations we create in the constructor for this class.
     */
    public static class GcdAndLcmAnswer {
        /**
         * If the GCD of the inputs is 1, this is null since only integers >= 2 have a prime factorization.
         */
        public final PrimeFactorization gcdPf;
        
        public final PrimeFactorization lcmPf;
        
        public final Stream<String> infoSentences;
        
        public GcdAndLcmAnswer(long input1, long input2) {
            assertIsInRange(input1, GcdAndLcm.MIN_INPUT, GcdAndLcm.MAX_INPUT);
            assertIsInRange(input2, GcdAndLcm.MIN_INPUT, GcdAndLcm.MAX_INPUT);
            
            var input1Pf = new PrimeFactorization(input1);
            var input2Pf = new PrimeFactorization(input2);
            var gcdPfFactorsAndPowers = new HashMap<Long, Integer>();
            var lcmPfFactorsAndPowers = new HashMap<Long, Integer>();
            
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
            
            String gcdInfo;

            if (gcdPfFactorsAndPowers.isEmpty()) {
                gcdPf = null;
                gcdInfo = "There are no common prime factors so the GCD is 1.";
            } else {
                gcdPf = new PrimeFactorization(gcdPfFactorsAndPowers);
                var infoEnding =
                    gcdPf.isForAPrimeNumber() ? "" : ", which is " + gcdPf.getCorrespondingBigIntString();
                gcdInfo = String.format("The GCD PF is %s%s.", gcdPf, infoEnding);
            }
            
            lcmPf = new PrimeFactorization(lcmPfFactorsAndPowers);
            String lcmInfo =
                String.format("The LCM PF is %s, which is %s.", lcmPf, lcmPf.getCorrespondingBigIntString());
            
            infoSentences =
                Stream.of(input1Pf.getInfoSentence(), input2Pf.getInfoSentence(), gcdInfo, lcmInfo);
        }
    }
}