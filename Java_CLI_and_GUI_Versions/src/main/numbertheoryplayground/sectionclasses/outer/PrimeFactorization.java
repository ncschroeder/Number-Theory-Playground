package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpGui;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.sectionclasses.outer.Divisibility.*;

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
    static final long MIN_INPUT = 2;
    static final long MAX_INPUT = TEN_QUADRILLION;
    
    
    record FactorAndPower(long factor, int power) {}

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
    private final BigInteger correspondingBigInt;
    
    private final String correspondingBigIntString;
    
    /**
     * This list is sorted by factors and immutable.
     */
    private final List<FactorAndPower> factorsAndPowers;
    
    /**
     * Constructs a new object to represent the prime factorization of the input.
     */
    PrimeFactorization(long inputLong, String inputString) {
        assertIsInRange(inputLong, MIN_INPUT, MAX_INPUT);
        
        correspondingBigInt = BigInteger.valueOf(inputLong);
        correspondingBigIntString = inputString;
        var factorsAndPowersArrList = new ArrayList<FactorAndPower>();
        var maxLongToCheck = (long) Math.sqrt(inputLong);
        long remaining = inputLong;

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
            factorsAndPowersArrList.add(new FactorAndPower(2, power));
        }
        
        if (remaining > 1) {
            for (var possiblePrimeFactor = 3L; possiblePrimeFactor <= maxLongToCheck; possiblePrimeFactor += 2) {
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
    
    /**
     * Constructs a new PrimeFactorization to represent the prime factorization whose factors and powers
     * are keys and values, respectively, in the map provided.
     */
    
    PrimeFactorization(List<FactorAndPower> factorsAndPowers) {
        this.factorsAndPowers =
            factorsAndPowers
            .stream()
            .sorted(Comparator.comparingLong(FactorAndPower::factor))
            .toList();
        
        BigInteger tempCorrespondingBigInt = BigInteger.ONE;
        for (FactorAndPower fp : factorsAndPowers) {
            BigInteger multiplicand = BigInteger.valueOf((long) Math.pow(fp.factor, fp.power));
            tempCorrespondingBigInt = tempCorrespondingBigInt.multiply(multiplicand);
        }
        correspondingBigInt = tempCorrespondingBigInt;
        correspondingBigIntString = createStringWithCommas(correspondingBigInt);
    }
    
    
    List<FactorAndPower> getFactorsAndPowers() {
        return factorsAndPowers;
    }
    
    BigInteger getCorrespondingBigInt() {
        return correspondingBigInt;
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
            .stream()
            .map(fp -> {
                var factorString = createStringWithCommas(fp.factor);
                return fp.power == 1 ? factorString : String.format("%s^%d", factorString, fp.power);
            })
            .collect(Collectors.joining(" × "));
    }
    
    /**
     * Prime numbers have a prime factorization that consists of a single factor with 1 as its power.
     */
    boolean isForAPrimeNumber() {
    }
    
    String toStringWithCorrespondingBigInt() {
        return
            isForAPrimeNumber()
            ? getCorrespondingBigIntString()
            : String.format("%s (%s)", this, getCorrespondingBigIntString());
        return factorsAndPowers.size() == 1 && factorsAndPowers.getFirst().power == 1;
    }
    
    String getInfoSentence() {
        return String.format("The PF of %s is %s.", correspondingBigIntString, this);
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
    String getNumFactorsInfo() {
        var numFactors = 1;
        
        for (FactorAndPower fp : factorsAndPowers) {
            numFactors *= fp.power + 1;
            powerStrings.add(String.format("(%d + 1)", fp.power));
        }
        
        return String.format(
            "By looking at the power%s, we can see there are %s = %s factors. If 1 and %s are " +
                "excluded then there are %s factors.",
            factorsAndPowers.size() == 1 ? "" : "s",
            String.join(" x ", powerStrings),
            createStringWithCommas(numFactors),
            correspondingBigIntString,
            createStringWithCommas(numFactors - 2)
        );
    }
    
    /**
     * This method finds the sub-factorizations by finding combinations of factors and powers.
     */
    List<PrimeFactorization> getFactorPfs() {
        var factorPfs = new ArrayList<PrimeFactorization>();
        
        for (FactorAndPower fp : factorsAndPowers) {
            long factor = fp.factor;
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

                    IntStream.range(0, factorPfFactorsAndPowers.size())
                    .filter(j -> factorPfFactorsAndPowers.get(j).factor == factor)
                    .findFirst()
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
        factorPfs.sort(Comparator.comparing(PrimeFactorization::getCorrespondingBigInt));
        return factorPfs;
    }
    
    Stream<String> getFactorPfStrings() {
        return getFactorPfs().stream().map(PrimeFactorization::toStringWithCorrespondingBigInt);
    private OptionalInt getPowerOf(long factor) {
        return
            factorsAndPowers
            .stream()
            .filter(fp -> fp.factor == factor)
            .mapToInt(FactorAndPower::power)
            .findFirst();
    }
    
    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Prime Factorization",
                MIN_INPUT,
                MAX_INPUT,
                "prime factorization of that integer",
                "prime factorizations",
                infoSupplier.get()
            );
        }
        
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            String info = new PrimeFactorization(inputLong, inputString).getInfoSentence();
            return NtpCli.putNewLineChars(info);
        }
        
        @Override
        public List<Component> getGuiComponents(long inputLong, String inputString) {
            String info = new PrimeFactorization(inputLong, inputString).getInfoSentence();
            return List.of(NtpGui.createCenteredAnswerContentLabel(info));
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
    static class GcdAndLcmAnswer {
        /**
         * If the GCD of the inputs is 1, this is null since only integers >= 2 have a prime factorization.
         */
        private final PrimeFactorization gcdPf;
        private final PrimeFactorization lcmPf;
        GcdAndLcmAnswer(long input1Long, long input2Long, String input1String, String input2String) {
            input1Pf = new PrimeFactorization(input1Long, input1String);
            input2Pf = new PrimeFactorization(input2Long, input2String);
            var gcdPfFactorsAndPowers = new ArrayList<FactorAndPower>();
            var lcmPfFactorsAndPowers = new ArrayList<FactorAndPower>();
            
            for (FactorAndPower fp : input1Pf.factorsAndPowers) {
                long factor = fp.factor;
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
            
            gcdPf =
                gcdPfFactorsAndPowers.isEmpty()
                ? null
                : new PrimeFactorization(gcdPfFactorsAndPowers);

            lcmPf = new PrimeFactorization(lcmPfFactorsAndPowers);
        }
        
        Stream<String> getInfoSentences() {
            String gcdSentence =
                gcdPf != null
                ? getGcdOrLcmPfSentence("GCD", gcdPf)
                : "There are no common prime factors so the GCD is 1.";
            
            String lcmSentence = getGcdOrLcmPfSentence("LCM", lcmPf);
            
            return Stream.of(
                input1Pf.getInfoSentence(), input2Pf.getInfoSentence(), gcdSentence, lcmSentence
            );
        }
        
        PrimeFactorization getGcdPf() {
            return gcdPf;
        }
        
        PrimeFactorization getLcmPf() {
            return lcmPf;
        }
    }
}
