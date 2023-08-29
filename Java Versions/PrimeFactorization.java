package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.nicholasschroeder.numbertheoryplayground.Misc.*;
import static com.nicholasschroeder.numbertheoryplayground.Divisibility.*;

/**
 * Class that can be instantiated to form an abstraction of a prime factorization. Also has static members related
 * to prime factorizations and the section for it.
 */
public class PrimeFactorization {
    private static final String info =
        "The Fundamental Theorem of Arithmetic says that every integer > 1 can be expressed as the product " +
        "of prime numbers. The prime factorization (PF) of an integer is an expression of the prime numbers " +
        "whose product is that integer. For example, the PF of 5 is just 5, the PF of 25 is 5^2, and the PF " +
        "of 12,250 is 2 x 5^3 x 7^2. There are some interesting applications for this. Visit the GCD and LCM " +
        "or the Divisibility sections for some applications.";

    public static final int minInputInt = 2;
    public static final int maxInputInt = tenThousand;

    /**
     * A map whose keys are prime factors and values are the corresponding powers for this prime
     * factorization. A SortedMap is used so that the entries are ordered by the prime factors, which
     * is mathematically appropriate when converting this to a string.
     */
    private final SortedMap<Integer, Integer> factorsAndPowers;

    /**
     * The int that this prime factorization is for.
     *
     * Note: the GcdAndLcmInfo constructor below creates 2 PrimeFactorizations, and then creates a Map
     * to represent the prime factorization of the LCM of the 2 input ints, and then passes that Map to
     * the constructor with a SortedMap param, which will then set this field to the product of all the
     * factors raised to their powers. The LCM of 2 ints is at most the product of those 2 ints. For a
     * max input of 10,000, the highest possible LCM will be < 10,000^2 (100,000,000), which is < the max
     * value for an int. If the max input was to be raised, then this might need to be changed to a long.
     */
    private int correspondingInt;

    /**
     * Constructs a new object to represent the prime factorization of the input.
     */
    public PrimeFactorization(int input) {
        assertIsInRange(input, minInputInt, maxInputInt);

        factorsAndPowers = new TreeMap<>();
        correspondingInt = input;
        var intRemaining = new AtomicInteger(input);

        /*
        Find all the prime factors and their powers and put these in factorsAndPowers. Divide intRemaining
        by each prime factor that is found. When intRemaining becomes 1, the entire prime factorization has
        been found. First 2 will be checked and then odd numbers will be checked since all prime numbers
        besides 2 are odd.
        */
        
        Consumer<Integer> addPrimeFactor = factor -> {
            int power = 0;
            do {
                power++;
                intRemaining.updateAndGet(i -> i / factor);
            } while (isDivisible(intRemaining.get(), factor));
            factorsAndPowers.put(factor, power);
        };
        
        if (isEven(input)) {
            addPrimeFactor.accept(2);
            if (intRemaining.get() == 1) {
                return;
            }
        }
    
        for (int possiblePrimeFactor = 3; ; possiblePrimeFactor += 2) {
            if (isDivisible(intRemaining.get(), possiblePrimeFactor)) {
                addPrimeFactor.accept(possiblePrimeFactor);
                if (intRemaining.get() == 1) {
                    return;
                }
            }
        }
    }

    /**
     * Constructs a new object to represent the prime factorization whose factors and powers are keys and
     * values, respectively, in the Map provided.
     * 
     * We're setting the factorsAndPowers Map in this PrimeFactorization to the Map provided, meaning that
     * any changes to that Map after using it to create this PrimeFactorization will cause unwanted behavior.
     * However, I don't consider this much of a problem since this constructor is private and the only place
     * it's used is at the bottom of the GcdAndLcmInfo constructor below.
     */
    private PrimeFactorization(SortedMap<Integer, Integer> factorsAndPowers) {
        this.factorsAndPowers = factorsAndPowers;
        correspondingInt = 1;
        for (Map.Entry<Integer, Integer> entry : factorsAndPowers.entrySet()) {
            correspondingInt *= Math.pow(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns a List of Map Entries where each key is a prime factor and each value is the corresponding
     * power in this prime factorization.
     */
    public List<Map.Entry<Integer, Integer>> getFactorsAndPowers() {
        return List.copyOf(factorsAndPowers.entrySet());
    }
    
    public int getCorrespondingInt() {
        return correspondingInt;
    }
    
    public String getCorrespondingIntString() {
        return stringifyWithCommas(correspondingInt);
    }

    /**
     * Returns a string representation of a prime factorization. " x " is used to represent multiplication
     * among all the prime factors and is placed between all the factors. "^" is used to represent the
     * powers of factors and is placed after factors that have a power other than 1.
     */
    @Override
    public String toString() {
        return
            factorsAndPowers.entrySet().stream()
            .map(entry -> {
                String factorString = stringifyWithCommas(entry.getKey());
                int power = entry.getValue();
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
    
    public String toStringWithCorrespondingInt() {
        return
            isForAPrimeNumber()
            ? getCorrespondingIntString()
            : String.format("%s (%s)", this, getCorrespondingIntString());
    }
    
    public String getInfoMessage() {
        return String.format("The prime factorization of %s is %s", getCorrespondingIntString(), this);
    }

    /**
     * The number of factors can be found by taking the powers of all the prime factors in the prime
     * factorization, adding 1 to each, and multiplying these all together. For example, the prime
     * factorization of 294 is 2 x 3 x 7^2. The powers are 1, 1, and 2; so there are 2 x 2 x 3 = 12
     * factors. However, that count includes 1 and the number that the prime factorization is for
     * (294 in this case). If you want to exclude those, then subtract 2. That would give us 10 factors.
     *
     * This method calculates the number of factors and returns a string that says the number
     * of factors and how it was calculated.
     */
    public String getNumberOfFactorsInfo() {
        var numberOfFactors = 1;
        var powerStrings = new ArrayList<String>(factorsAndPowers.size());
        for (int power : factorsAndPowers.values()) {
            numberOfFactors *= (power + 1);
            powerStrings.add(String.format("(%d + 1)", power));
        }
        
        return String.format(
            "By looking at the powers, we can see there are %s = %d total factors. If 1 and %s are " +
                "excluded then there are %d factors.",
            String.join(" x ", powerStrings),
            numberOfFactors,
            getCorrespondingIntString(),
            numberOfFactors - 2
        );
    }


    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Prime Factorization",
                List.of(info),
                minInputInt,
                maxInputInt,
                "get its prime factorization",
                "prime factorizations"
            );
        }

        @Override
        public String getCliAnswer(int input) {
            return new PrimeFactorization(input).getInfoMessage();
        }
        
        @Override
        public List<Component> getGuiComponents(int input) {
            return List.of(
                NTPGUI.createCenteredLabel(new PrimeFactorization(input).getInfoMessage(), NTPGUI.answerContentFont)
            );
        }
    }


    /**
     * This class uses prime factorizations to find the greatest common divisor (GCD) and least common multiple (LCM)
     * of 2 integers. An advantage to having this class be a nested class within the PrimeFactorization class is that we
     * can access the private factorsAndPowers map of the PrimeFactorizations we create in the constructor for this class.
     */
    public static class GcdAndLcmInfo {
        public static final String infoParagraph =
            "One way to find the GCD and LCM of 2 numbers is to look at the prime factorizations (PFs) " +
            "of those numbers. If those numbers do not have any common prime factors, then the GCD is 1. " +
            "If they do have common prime factors, then the PF of the GCD consists of all the common prime " +
            "factors and the power of each factor is the minimum power of that factor in the 2 PFs. The PF " +
            "of the LCM consists of all factors that are in either of the PFs of the 2 numbers. If a factor " +
            "is in both PFs then the power of that factor in the LCM PF is the max of the powers of that " +
            "factor the 2 PFs. If a factor is unique to one of the PFs then that factor and its power are " +
            "in the LCM PF.";
            
        public static final String examplesParagraph =
            "Let's find the GCD and LCM of 6 and 35 using their PFs. 6 has a PF of 2 x 3 and 35 has a " +
            "PF of 5 x 7. There are no common factors so the GCD is 1. The LCM has a PF of 2 x 3 x 5 x 7 " +
            "and this equals 210. Let's find the GCD and LCM of 54 and 99. 54 has a PF of 2 x 3^3 and " +
            "99 has a PF of 3^2 x 11. 3 is the only common factor and the minimum power of that factor is " +
            "2 so the GCD has a PF of 3^2 and this equals 9. The max power of that factor is 3 so the LCM " +
            "has a PF that consists of 3^3. The PF of the LCM is 2 x 3^3 x 11 and this equals 594.";


        // Since this class has a unique and specific use, I'll use public final fields instead of getter methods.

        public final PrimeFactorization input1Pf;

        public final PrimeFactorization input2Pf;

        /**
         * If the GCD of the ints provided is 1, then this is an empty Optional. Otherwise, this is an
         * Optional with the prime factorization of the GCD. The reason for this behavior is that
         * mathematically, only integers >= 2 have a prime factorization.
         */
        public final Optional<PrimeFactorization> gcdPf;

        public final PrimeFactorization lcmPf;

        public GcdAndLcmInfo(int input1, int input2) {
            input1Pf = new PrimeFactorization(input1);
            input2Pf = new PrimeFactorization(input2);
            
            SortedMap<Integer, Integer> gcdPfMap = new TreeMap<>();
            SortedMap<Integer, Integer> lcmPfMap = new TreeMap<>();

            for (Map.Entry<Integer, Integer> entry : input1Pf.factorsAndPowers.entrySet()) {
                int factor = entry.getKey();
                int power1 = entry.getValue();
                Integer power2 = input2Pf.factorsAndPowers.get(factor);
                if (power2 == null) {
                    // Prime factor is unique to the first prime factorization.
                    lcmPfMap.put(factor, power1);
                } else {
                    // Common prime factors.
                    gcdPfMap.put(factor, Math.min(power1, power2));
                    lcmPfMap.put(factor, Math.max(power1, power2));
                }
            }
            
            // Find the unique prime factors of the second prime factorization.
            for (Map.Entry<Integer, Integer> entry : input2Pf.factorsAndPowers.entrySet()) {
                int factor = entry.getKey();
                if (!input1Pf.factorsAndPowers.containsKey(factor)) {
                    int power = entry.getValue();
                    lcmPfMap.put(factor, power);
                }
            }
            
            gcdPf = gcdPfMap.isEmpty() ? Optional.empty() : Optional.of(new PrimeFactorization(gcdPfMap));
            lcmPf = new PrimeFactorization(lcmPfMap);
        }

        public String getGcdPfStringOrDefault() {
            return
                gcdPf
                .map(PrimeFactorization::toString)
                .orElse("N/A");
        }

        public String getGcdString() {
            return
                gcdPf
                .map(PrimeFactorization::getCorrespondingIntString)
                .orElse("1");
        }
    }
}