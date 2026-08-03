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
 * the section for it. The initials PF are used to refer to instances of this class and to prime
 * factorizations in general.
 */
public final class PrimeFactorization implements Iterable<PrimeFactorization.FactorAndPower> {
    private static final String INFO = """
The fundamental theorem of arithmetic says that every whole number > 1 is either prime or can be
expressed as the product of prime numbers in 1 way if you ignore the order of those prime numbers.
The prime factorization (PF) of a whole number > 1 is an expression of the prime numbers whose
product is that number. These prime numbers are factors of that number. For example, the PF of 3
is just 3, the PF of 25 is 5 × 5 or 5^2, and the PF of 12,250 is 2 × 5^3 × 7^2 if the factors are
in ascending order. 12,250 can also be expressed as 5^3 × 2 × 7^2 but that's the same expression
as the previous one if you ignore the order of the factors. The Number Theory Playground displays
PFs with the factors in ascending order. There are some interesting applications for PFs. See the
info for the "Divisibility" or "GCD and LCM" sections for some applications.

The input number with the most prime factors is 9,007,199,254,740,992 (2^53), the largest power
of 2 ≤ 10 quadrillion, the max input. An input number with the most unique prime factors is
304,250,263,527,210, which is the product of the first 13 prime numbers and has a PF of
2 × 3 × 5 × 7 × 11 × 13 × 17 × 19 × 23 × 29 × 31 × 37 × 41. You could also multiply that number
by a whole number ≤ 32 and the result would be ≤ the max input and have the same amount of unique
prime factors.""";

    // The calculation for this section is: find the PF of an input number.
    
    static final long MIN_INPUT = 2;
    static final long MAX_INPUT = TEN_QUADRILLION;
    
    /**
     * fp and its plural fps are used to refer to instances of this in variable names.
     */
    record FactorAndPower(long factor, int power) {}
    
    /**
     * The BigInteger that this PF is for.
     */
    private final BigInteger correspondingBigInt;
    
    /*
    Why use a BigInteger? Well, this class has 2 constructors, 1 of which has a list for a param.
    1 place where that one is used is in the constructor for the
    GcdAndLcmAnswer.PrimeFactorizationAnswer class. That constructor creates a list of the prime
    factors and powers of the LCM of 2 input longs, and then creates a PrimeFactorization using
    that list. That PrimeFactorization constructor will then set the correspondingBigInt field
    to the product of all factors raised to their powers. The LCM of 2 longs is at most the
    product of them. The GCD and LCM section has a max input of 5 quadrillion, so the largest
    possible LCM is 5 quadrillion × (5 quadrillion − 1), which is almost 25 nonillion, which is
    a number with 32 digits. The max value for a long is 9 quintillion something, which is a
    relatively small number with 19 digits.
     */
    
    /**
     * An immutable list of the factors and powers in this PF. This is sorted by factors, which
     * is appropriate for the string representation of this. As mentioned above, the PF of 12,250
     * is 2 × 5^3 × 7^2, so if a PF object was created for that number, then this list would
     * contain 3 FactorAndPowers and the fields of them would be 2 & 1, 5 & 3, and 7 & 2.
     */
    private final List<FactorAndPower> fps;
    
    /**
     * Constructs a PrimeFactorization for the prime factorization of the input.
     */
    PrimeFactorization(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        correspondingBigInt = BigInteger.valueOf(input);
        long remaining = input;
        // The max amount of unique prime factors is 13.
        var tempFps = new ArrayList<FactorAndPower>(13);
        
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
            var maxPossibleFactorToCheck = (long) Math.sqrt(input);
            for (var possibleFactor = 3L; possibleFactor <= maxPossibleFactorToCheck; possibleFactor += 2) {
                if (isDivisible(remaining, possibleFactor)) {
                    var power = 0;
                    do {
                        power++;
                        remaining /= possibleFactor;
                    } while (isDivisible(remaining, possibleFactor));
                    tempFps.add(new FactorAndPower(possibleFactor, power));
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
     * in the list provided, which should be sorted by factors.
     */
    PrimeFactorization(List<FactorAndPower> fps) {
        this.fps = List.copyOf(fps);
        
        var tempCorrespondingBigInt = BigInteger.ONE;
        for (FactorAndPower fp : fps) {
            var multiplicand = BigInteger.valueOf((long) Math.pow(fp.factor, fp.power));
            tempCorrespondingBigInt = tempCorrespondingBigInt.multiply(multiplicand);
        }
        correspondingBigInt = tempCorrespondingBigInt;
    }
    
    BigInteger getCorrespondingBigInt() {
        return correspondingBigInt;
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
    
    String getInfoSentence(String correspondingBigIntString) {
        return String.format("The PF of %s is %s.", correspondingBigIntString, this);
    }
    
    @Override
    public Iterator<FactorAndPower> iterator() {
        return fps.iterator();
    }
    
    boolean isForAPrimeNumber() {
        return fps.size() == 1 && fps.getFirst().power == 1;
    }
    
    Optional<Integer> getPowerOf(long possibleFactor) {
        return
            fps
            .stream()
            .filter(fp -> fp.factor == possibleFactor)
            .findFirst()
            .map(FactorAndPower::power);
    }
    
    boolean containsFactor(long possibleFactor) {
        return fps.stream().anyMatch(fp -> fp.factor == possibleFactor);
    }

    
    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Prime Factorization",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                "the PF of that number",
                "prime factorizations"
            );
        }
        
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            String info = new PrimeFactorization(inputLong).getInfoSentence(inputString);
            return NtpCli.putNewLineChars(info);
        }
        
        @Override
        public List<Component> getGuiComponents(long inputLong, String inputString) {
            String info = new PrimeFactorization(inputLong).getInfoSentence(inputString);
            return List.of(NtpGui.createCenteredAnswerContentLabel(info));
        }
    }
}
