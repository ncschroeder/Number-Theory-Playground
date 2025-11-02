package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.*;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpGui;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.sectionclasses.outer.Divisibility.*;

/**
 * Class that can be instantiated and also has static members related to prime factorizations and
 * the section for it. The initials PF are used to refer to instances of this class or to prime
 * factorizations in general.
 */
public final class PrimeFactorization {
    private static final String INFO = """
The Fundamental Theorem of Arithmetic says that every whole number > 1 can be expressed as the
product of prime numbers in 1 way if you ignore the order of those prime numbers. The prime
factorization (PF) of a whole number > 1 is an expression of the prime numbers whose product is
that number. For example; the PF of 5 is just 5, the PF of 25 is 5^2, and the PF of 12,250 is
2 × 5^3 × 7^2 if the prime numbers are in ascending order. 12,250 could also be expressed as
5^3 × 2 × 7^2 but that's the same expression as the previous one if you ignore the order of the
prime numbers. The Number Theory Playground displays PFs with the prime numbers in ascending
order. There are some interesting applications for PFs. See the info for the "Divisibility" or
"GCD and LCM" sections for some applications.

The input number with the highest amount of prime factors is 2^53 (9,007,199,254,740,992), the
largest power of 2 ≤ 10 quadrillion, the max input. An input number with the highest amount of
unique prime factors is 304,250,263,527,210. This number is the product of the first 13 prime
numbers so it has 13 unique prime factors and its PF is
2 × 3 × 5 × 7 × 11 × 13 × 17 × 19 × 23 × 29 × 31 × 37 × 41. You could also multiply that number
by 2 or 3 and those numbers are ≤ the max input and have the same amount of unique prime factors.""";

    // The calculation for this section is: find the PF of an input number.
    
    static final long MIN_INPUT = 2;
    static final long MAX_INPUT = TEN_QUADRILLION;
    
    /**
     * Instances of this class are shortened to fp or its plural fps.
     */
    record FactorAndPower(long factor, int power) {}
    /**
     * The BigInteger that this prime factorization is for.
     */
    private final BigInteger correspondingBigInt;
    
    /*
    Why use a BigInteger? Well, this class has 2 constructors, 1 of which has a list for a param.
    1 place where that one is used is in the constructor for the
    GcdAndLcmAnswer.PrimeFactorizationAnswer class. That constructor creates a list of the prime
    factors and powers of the LCM of 2 input longs, and then creates a PrimeFactorization using
    that list. That PrimeFactorization constructor will then set this field to the product of all
    the factors raised to their powers. The LCM of 2 longs is at most the product of them. The
    GCD and LCM section has a max input of 5 quadrillion. The highest possible LCM is described in
    the PF_INFO string above the GcdAndLcmAnswer.PrimeFactorizationAnswer class. That LCM is
    5 quadrillion × (5 quadrillion − 1), which is almost 25 nonillion, which is a number with 32
    digits. The max value for a long is 9 quintillion something, which is a relatively small
    number with 19 digits.
     */
    
    private final String correspondingBigIntString;
    
    /**
     * An immutable list that's sorted by factors, which is appropriate for the string
     * representation of this.
     */
    private final List<FactorAndPower> fps;
    
    /**
     * Constructs a PrimeFactorization for the prime factorization of the input long.
     */
    PrimeFactorization(long inputLong, String inputString) {
        assertIsInRange(inputLong, MIN_INPUT, MAX_INPUT);
        
        correspondingBigInt = BigInteger.valueOf(inputLong);
        correspondingBigIntString = inputString;
        var tempFps = new ArrayList<FactorAndPower>();
        var maxLongToCheck = (long) Math.sqrt(inputLong);
        long remaining = inputLong;

        /*
        Find all the prime factors and their powers and put these in tempFps. Divide remaining
        by each factor that's found. When remaining becomes 1, the entire prime factorization has
        been found. First 2 will be checked and then odd numbers will be checked since all prime
        numbers besides 2 are odd.
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
            for (var possiblePrimeFactor = 3L; possiblePrimeFactor <= maxLongToCheck; possiblePrimeFactor += 2) {
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
     * Constructs a PrimeFactorization for the prime factorization whose factors and powers
     * are in the list provided.
     */
    PrimeFactorization(List<FactorAndPower> fps) {
        this.fps =
            fps
            .stream()
            .sorted(Comparator.comparingLong(FactorAndPower::factor))
            .toList();
        
        BigInteger tempCorrespondingBigInt = BigInteger.ONE;
        for (FactorAndPower fp : fps) {
            BigInteger multiplicand = BigInteger.valueOf((long) Math.pow(fp.factor, fp.power));
            tempCorrespondingBigInt = tempCorrespondingBigInt.multiply(multiplicand);
        }
        correspondingBigInt = tempCorrespondingBigInt;
        correspondingBigIntString = createStringWithCommas(correspondingBigInt);
    }
    
    BigInteger getCorrespondingBigInt() {
        return correspondingBigInt;
    }
    
    public String getCorrespondingBigIntString() {
        return correspondingBigIntString;
    }
    
    List<FactorAndPower> getFps() {
        return fps;
    }
    
    /**
     * Returns a string that represents this PF the same way that the first info paragraph at the
     * top represents PFs. That paragraph says "the PF of 5 is just 5, the PF of 25 is 5^2, and
     * the PF of 12,250 is 2 × 5^3 × 7^2".
     */
    @Override
    public String toString() {
        return
            fps
            .stream()
            .map(fp -> {
                var factorString = createStringWithCommas(fp.factor);
                return fp.power == 1 ? factorString : String.format("%s^%d", factorString, fp.power);
            })
            .collect(Collectors.joining(" × "));
    }
    
    boolean isForAPrimeNumber() {
        return fps.size() == 1 && fps.getFirst().power == 1;
    }
    
    String getInfoSentence() {
        return String.format("The PF of %s is %s.", correspondingBigIntString, this);
    }
    
    /**
     * If the factor is in this PF, then an Optional with that factor's power will be returned.
     * Otherwise, an empty Optional will be returned.
     */
    OptionalInt getPowerOf(long factor) {
        return
            fps
            .stream()
            .filter(fp -> fp.factor == factor)
            .mapToInt(FactorAndPower::power)
            .findFirst();
    }
    
    boolean containsFactor(long l) {
        return
            fps
            .stream()
            .anyMatch(fp -> fp.factor == l);
    }

    
    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Prime Factorization",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                "the prime factorization of that number",
                "prime factorizations"
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
}
