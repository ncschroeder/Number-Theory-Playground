package com.nicholasschroeder.numbertheoryplayground;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A class that has methods related to prime factorizations. Some methods are static and some require an instance
 * of this class.
 */
public class PrimeFactorization {
    /**
     * @return A string representation of the argument number's prime factorization.
     * @throws IllegalArgumentException if the argument number is less than 2.
     */
    public static String getPfString(int anInt) {
        return new PrimeFactorization(anInt).toString();
    }

    /**
     * @return An array whose first element is a PrimeFactorization object for the GCD of pf1 and pf2 if the GCD
     * is not 1. If the GCD is 1 then this element is null. The second element is a PrimeFactorization for
     * the LCM of pf1 and pf2.
     */
    public static PrimeFactorization[] getGcdAndLcmPfs(PrimeFactorization pf1, PrimeFactorization pf2) {
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

        for (int primeFactor : pf1.pfMap.keySet()) {
            int power1 = pf1.pfMap.get(primeFactor);
            if (pf2.pfMap.containsKey(primeFactor)) {
                // Common prime factors
                int power2 = pf2.pfMap.get(primeFactor);
                gcdPfMap.put(primeFactor, Math.min(power1, power2));
                lcmPfMap.put(primeFactor, Math.max(power1, power2));
            } else {
                // Prime factor is unique to the first prime factorization
                lcmPfMap.put(primeFactor, power1);
            }
        }

        // Find the unique prime factors of the second prime factorization
        for (int primeFactor : pf2.pfMap.keySet()) {
            if (!pf1.pfMap.containsKey(primeFactor)) {
                int power = pf2.pfMap.get(primeFactor);
                lcmPfMap.put(primeFactor, power);
            }
        }

        return new PrimeFactorization[] {
                gcdPfMap.isEmpty() ? null : new PrimeFactorization(gcdPfMap),
                new PrimeFactorization(lcmPfMap)
        };
    }

    /**
     *
     * @param intParam
     * @return A list of prime factorization strings for all factors of intParam.
     */
    public static List<String> getFactorPfStrings(int intParam) {
        if (Section.DIVISIBILITY.isInvalidInput(intParam)) {
            throw new IllegalArgumentException();
        }

        return
            IntStream.rangeClosed(2, intParam / 2)
            .filter(i -> isDivisible(intParam, i))
            .mapToObj(i -> new PrimeFactorization(i).toStringWithCorrespondingLong())
            .collect(Collectors.toList());
    }

    public static final String divisibilityInfoLabel = "Prime factorization info";

    public static final String subfactorizationsLabel =
        "By looking at the \"sub-factorizations\", we can see the factors are:";

    public static String getPrimeNumberLabel(String intString) {
        return intString + " is prime and doesn't have any factors other than itself and 1.";
    }

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
        if (Section.PRIME_FACTORIZATION.isInvalidInput(anInt)) {
            throw new IllegalArgumentException();
        }

        factorsAndPowers = new TreeMap<>();
        correspondingLong = anInt;
        int intRemaining = anInt;

        /*
        Find all the prime factors and their powers and put these in factorsAndPowers. Divide intRemaining by each
        prime factor that is found. When intRemaining becomes 1, the entire prime factorization has been
        found. First 2 will be checked and then odd numbers will be checked since all prime numbers
        besides 2 are odd.
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
     *     factors and the values are the powers of those prime factors.
     * @throws IllegalArgumentException if factorsAndPowers is empty or contains any non-prime keys
     *     or non-positive values.
     */
    public PrimeFactorization(TreeMap<Integer, Integer> factorsAndPowers) {
        if (factorsAndPowers.isEmpty() || factorsAndPowers.entrySet().stream().anyMatch(
            entry -> !isPrime(entry.getKey()) || entry.getValue() < 1)
        ) {
            throw new IllegalArgumentException("Invalid map provided: " + factorsAndPowers);
        }

        this.factorsAndPowers = factorsAndPowers;
        correspondingLong = 1;
        for (Map.Entry<Integer, Integer> entry : factorsAndPowers.entrySet()) {
            correspondingLong *= Math.pow(entry.getKey(), entry.getValue());
        }
    }

    /**
     * @return A string representation of a prime factorization. " x " is used to represent multiplication among all
     * the prime factors and is placed between all the prime factors. "^" is used to represent the powers of prime
     * factors and is used for prime factors that have a power other than 1.
     */
    public String toString() {
        return
            factorsAndPowers
            .entrySet()
            .stream()
            .map(entry -> {
                String factorString = getLongStringWithCommas(entry.getKey());
                int power = entry.getValue();
                return power == 1 ? factorString : factorString + "^" + power;
            })
            .collect(Collectors.joining(" x "));
    }

    /**
     *
     * @return the result of calling toString and if this prime factorization is for a composite
     * number then this is followed by the corresponding long of this prime factorization in
     * parentheses.
     */
    public String toStringWithCorrespondingLong() {
        return
            isForAPrimeNumber()
            ? toString()
            : toString() + " (" + getCorrespondingLongString() + ")";
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

    public String getInfoMessage() {
        return "The prime factorization of " + getCorrespondingLongString() + " is " + this;
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
     * The number of factors can be found by taking the powers of all the prime factors in the
     * prime factorization, adding 1 to each, and multiplying these all together. This method
     * calculates this and returns a string that says the number of factors and how it was
     * determined.
     */
    public String getNumberOfFactorsInfo() {
        int numberOfFactors = 1;
        ArrayList<String> powerStrings = new ArrayList<>();
        for (int power : factorsAndPowers.values()) {
            numberOfFactors *= (power + 1);
            powerStrings.add("(" + power + " + 1)");
        }

        return
            "By looking at the powers, we can see there are " +
            String.join(" x ", powerStrings) + " = " + numberOfFactors +
            " total factors. If 1 and " + getCorrespondingLongString() +
            " are excluded then there are " + (numberOfFactors - 2) + " total factors.";
    }
}
