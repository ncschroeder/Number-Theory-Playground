package com.nicholasschroeder.numbertheoryplayground;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Class that can be instantiated to form an abstraction of a prime factorization. Also has static members related
 * to prime factorizations and the section for it.
 */
public class PrimeFactorization {
    /**
     *
     */
    private static final String info =
        "The fundamental theorem of arithmetic states that every integer > 1 can be expressed as the product " +
        "of prime numbers. This product is called its prime factorization. There are some interesting " +
        "applications for this. Visit the GCD and LCM or the divisibility sections for some applications.";

    public final static int minInputInt = 2;
    public final static int maxInputInt = tenThousand;

    /**
     * A map whose keys are the prime factors of a number and the values are the powers those factors are
     * raised to to form the prime factorization of a number. A TreeMap is used so that the prime factors
     * are in order.
     */
    private final TreeMap<Integer, Integer> factorsAndPowers;

    /**
     * The long that this prime factorization is for.
     */
    private long correspondingLong;

    /**
     * @throws IllegalArgumentException
     */
    public PrimeFactorization(int anInt) {
        assertIsInRange(anInt, minInputInt, maxInputInt);

        factorsAndPowers = new TreeMap<>();
        correspondingLong = anInt;
        int intRemaining = anInt;

        /*
        Find all the prime factors and their powers and put these in factorsAndPowers. Divide intRemaining by each
        prime factor that is found. When intRemaining becomes 1, the entire prime factorization has been found.
        First 2 will be checked and then odd numbers will be checked since all prime numbers besides 2 are odd.
        */

        int possiblePrimeFactor = 2;
        if (isDivisible(intRemaining, possiblePrimeFactor)) {
            int power = 0;
            do {
                power++;
                intRemaining /= possiblePrimeFactor;
            } while (isDivisible(intRemaining, possiblePrimeFactor));
            factorsAndPowers.put(possiblePrimeFactor, power);
            if (intRemaining == 1) {
                return;
            }
        }

        for (possiblePrimeFactor = 3; ; possiblePrimeFactor += 2) {
            if (isDivisible(intRemaining, possiblePrimeFactor)) {
                int power = 0;
                do {
                    power++;
                    intRemaining /= possiblePrimeFactor;
                } while (isDivisible(intRemaining, possiblePrimeFactor));
                factorsAndPowers.put(possiblePrimeFactor, power);
                if (intRemaining == 1) {
                    return;
                }
            }
        }
    }

    /**
     * @param factorsAndPowers A map representing the prime factorization of a number. The keys are the prime
     *                         factors and the values are the powers of those prime factors.
     * @throws IllegalArgumentException if factorsAndPowers is empty or contains any non-prime keys
     * or non-positive values.
     */
    private PrimeFactorization(TreeMap<Integer, Integer> factorsAndPowers) {
        Set<Map.Entry<Integer, Integer>> entries = factorsAndPowers.entrySet();
        if (factorsAndPowers.isEmpty() ||
            entries.stream().anyMatch(
                entry -> !Primes.isPrime(entry.getKey()) || entry.getValue() < 1
            )
        ) {
            throw new IllegalArgumentException("Invalid map provided: " + factorsAndPowers);
        }


        this.factorsAndPowers = factorsAndPowers;
        correspondingLong = 1;
        for (Map.Entry<Integer, Integer> entry : entries) {
            correspondingLong *= Math.pow(entry.getKey(), entry.getValue());
        }
    }

    /**
     * @return A string representation of a prime factorization. " x " is used to represent multiplication among all
     * the prime factors and is placed between all the prime factors. "^" is used to represent the powers of prime
     * factors and is used for prime factors that have a power other than 1.
     */
    @Override
    public String toString() {
        return
            factorsAndPowers
            .entrySet()
            .stream()
            .map(entry -> {
                String factorString = getLongStringWithCommas(entry.getKey());
                int power = entry.getValue();
                return power == 1 ? factorString : String.format("%s^%d", factorString, power);
            })
            .collect(Collectors.joining(" x "));
    }

    /**
     * @return The long that this prime factorization is for, that is, the product of all the prime factors
     * raised to their respective powers. This method returns a long since it's possible that an int will be
     * too small when a PrimeFactorization is created using the constructor that takes a TreeMap as an
     * argument.
     */
    public long getCorrespondingLong() {
        return correspondingLong;
    }

    public String getCorrespondingLongString() {
        return getLongStringWithCommas(getCorrespondingLong());
    }

    public String toStringWithCorrespondingLong() {
        return
            isForAPrimeNumber()
            ? toString()
            : String.format("%s (%s)", this, getCorrespondingLongString());
    }

    public String getInfoMessage() {
        return String.format("The prime factorization of %s is %s", getCorrespondingLongString(), this);
    }

    /**
     * @return true if the number that this prime factorization is representing is prime. For this case, the prime
     * factorization consists of a single number that has 1 as its power. Returns false otherwise.
     */
    public boolean isForAPrimeNumber() {
        return factorsAndPowers.size() == 1 && factorsAndPowers.containsValue(1);
    }

    public boolean isForACompositeNumber() {
        return !isForAPrimeNumber();
    }

    /**
     * The number of factors can be found by taking the powers of all the prime factors in the prime factorization,
     * adding 1 to each, and multiplying these all together. This method calculates this and returns a string that
     * says the number of factors and how it was determined.
     */
    public String getNumberOfFactorsInfo() {
        int numberOfFactors = 1;
        ArrayList<String> powerStrings = new ArrayList<>();
        for (int power : factorsAndPowers.values()) {
            numberOfFactors *= (power + 1);
            powerStrings.add(String.format("(%d + 1)", power));
        }

        return String.format(
            "By looking at the powers, we can see there are %s = %d total factors. If 1 and %s are " +
                "excluded then there are %d total factors.",
            String.join(" x ", powerStrings),
            numberOfFactors,
            getCorrespondingLongString(),
            numberOfFactors - 2
        );
    }

        return
    public static String getPfInfoString(int anInt) {
        return new PrimeFactorization(anInt).getInfoMessage();
    }

    /**
     * Prime factorizations can be used to find the greatest common divisor (GCD) and least common multiple (LCM)
     * of 2 integers. This class does just that.
     */
    public static class GcdAndLcmInfo {
        /**
         * This will be displayed in the application and it also can help explain the code in this class.
         */
        public static String infoString =
            "One way to find the GCD and LCM of 2 numbers is to look at the prime factorizations " +
            "of those numbers. If those numbers do not have any common prime factors, then " +
            "the GCD is 1. If they do have common prime factors, then the prime factorization " +
            "of the GCD consists of all the common prime factors and the power of each factor " +
            "is the minimum power of that factor in the 2 prime factorizations. The prime " +
            "factorization of the LCM consists of all factors that are in either of the prime " +
            "factorizations of the 2 numbers. The power for each factor is the maximum power " +
            "of that factor in the 2 prime factorizations.";

        /**
         * Prime factorization of the 1st int provided
         */
        public final PrimeFactorization int1Pf;

        /**
         * Prime factorization of the 2nd int provided
         */
        public final PrimeFactorization int2Pf;

        public final Optional<PrimeFactorization> gcdPf;

        public final PrimeFactorization lcmPf;

        /**
         * Constructs a new object for info about int1 and int2 and their GCD and LCM.
         * @throws IllegalArgumentException if int1 or int2 are invalid inputs for creating a PrimeFactorization.
         */
        public GcdAndLcmInfo(int int1, int int2) {
            int1Pf = new PrimeFactorization(int1);
            int2Pf = new PrimeFactorization(int2);

            /*
            The prime factorization of the GCD of 2 numbers contains all the prime factors that are in both of the prime
            factorizations of the 2 numbers. The power of each prime factor is the minimum of the 2 powers in the 2
            prime factorizations. If there are no common prime factors then the GCD is 1.

            The prime factorization of the LCM of 2 numbers contains all prime factors that are in either of the prime
            factorizations for the 2 numbers. If a prime factor is in both prime factorizations then the power of that
            prime factor in the LCM prime factorization is the max of the 2 powers in the 2 prime factorizations. If a
            prime factor is unique to 1 of the prime factorizations of the 2 numbers, then the power of that prime factor
            in the LCM prime factorization is the same as in the prime factorization for that 1 number.
            */
            TreeMap<Integer, Integer> gcdPfMap = new TreeMap<>();
            TreeMap<Integer, Integer> lcmPfMap = new TreeMap<>();

            for (Map.Entry<Integer, Integer> entry : int1Pf.factorsAndPowers.entrySet()) {
                Integer factor = entry.getKey();
                Integer power1 = entry.getValue();
                Integer power2 = int2Pf.factorsAndPowers.get(factor);
                if (power2 == null) {
                    // Prime factor is unique to the first prime factorization
                    lcmPfMap.put(factor, power1);
                } else {
                    // Common prime factors
                    gcdPfMap.put(factor, Math.min(power1, power2));
                    lcmPfMap.put(factor, Math.max(power1, power2));
                }

            // Find the unique prime factors of the second prime factorization
            for (Map.Entry<Integer, Integer> entry : int2Pf.factorsAndPowers.entrySet()) {
                int factor = entry.getKey();
                if (!int1Pf.factorsAndPowers.containsKey(factor)) {
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
                .map(PrimeFactorization::getCorrespondingLongString)
                .orElse("1");
        }
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
        public String getCliAnswer(int inputInt) {
            return getPfInfoString(inputInt);
        }

        @Override
        public List<Component> getGuiComponents(int inputInt) {
            return List.of(
                NTPPanel.createCenteredLabel(getPfInfoString(inputInt), AnswerPanel.contentFont)
            );
        }
    }
}
