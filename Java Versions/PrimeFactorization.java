package com.nicholasschroeder.numbertheoryplayground;

import java.util.TreeMap;

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
     * A map whose keys are the prime factors of a number and the values are the powers those factors are
     * raised to to form the prime factorization of a number. A TreeMap is used so that the prime factors
     * are in order.
     */
    private final TreeMap<Integer, Integer> pfMap;

    /**
     * The long that this prime factorization is for.
     */
//    private long correspondingLong;

    /**
     * @throws IllegalArgumentException if anInt is less than 2.
     */
    public PrimeFactorization(int anInt) {
        if (anInt < 2) {
            throw new IllegalArgumentException("Cannot get prime factorization of integer less than 2");
        }
        pfMap = new TreeMap<>();
        int intRemaining = anInt;

        /*
        Find all the prime factors and their powers and put these in pfMap. Divide intRemaining by each
        prime factor that is found. When intRemaining becomes 1, the entire prime factorization has been
        found. All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so first
        2 and 3 will be checked to see if they're prime factors and then numbers that are either 1 above
        or 1 below a multiple of 6 will be checked.
        */

        for (int potentialPrimeFactor = 2; potentialPrimeFactor <= 3; potentialPrimeFactor++) {
            if (intRemaining % potentialPrimeFactor == 0) {
                int power = 0;
                do {
                    power++;
                    intRemaining /= potentialPrimeFactor;
                } while (intRemaining % potentialPrimeFactor == 0);
                pfMap.put(potentialPrimeFactor, power);
                if (intRemaining == 1) {
                    return;
                }
            }
        }

        for (int potentialPrimeFactor = 5; ; potentialPrimeFactor += 4) {
            for (int i = 0; i < 2; i++) {
                if (intRemaining % potentialPrimeFactor == 0) {
                    int power = 0;
                    do {
                        power++;
                        intRemaining /= potentialPrimeFactor;
                    } while (intRemaining % potentialPrimeFactor == 0);
                    pfMap.put(potentialPrimeFactor, power);
                    if (intRemaining == 1) {
                        return;
                    }
                }
                if (i == 0) {
                    potentialPrimeFactor += 2;
                }
            }
        }
    }

    /**
     * @param pfMap A map representing the prime factorization of a number. The keys are the prime factors and the
     *              values are the powers of those prime factors.
     * @throws IllegalArgumentException if pfMap is empty.
     */
    public PrimeFactorization(TreeMap<Integer, Integer> pfMap) {
        if (pfMap.isEmpty()) {
            throw new IllegalArgumentException("Cannot create prime factorization from an empty map");
        }
        this.pfMap = pfMap;
    }

    /**
     * @return A string representation of a prime factorization. " x " is used to represent multiplication among all
     * the prime factors and is placed between all the prime factors. "^" is used to represent the powers of prime
     * factors and is used for prime factors that have a power other than 1.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int primeFactor : pfMap.keySet()) {
            sb.append(primeFactor);
            int power = pfMap.get(primeFactor);
            if (power != 1) {
                sb.append("^").append(power);
            }
            sb.append(" x ");
        }
        // Delete last " x "
        sb.delete(sb.length() - 3, sb.length());
        return sb.toString();
    }

    /**
     * @return The long that this prime factorization is for, that is, the product of all the prime factors
     * raised to their respective powers. This method returns a long since it's possible that an int will be
     * too small when a PrimeFactorization is created using the constructor that takes a TreeMap as an
     * argument.
     */
    public long getCorrespondingLong() {
        long correspondingLong = 1;
        for (int primeFactor : pfMap.keySet()) {
            int power = pfMap.get(primeFactor);
            correspondingLong *= Math.pow(primeFactor, power);
        }
        return correspondingLong;
    }

    /**
     * @return true if the number that this prime factorization is representing is prime. For this case, the prime
     * factorization consists of a single number that has 1 as it's power. Returns false otherwise.
     */
    public boolean isForAPrimeNumber() {
        return pfMap.size() == 1 && pfMap.containsValue(1);
    }

    /**
     * @return A list of strings that contain info about the factors of the number that this prime factorization
     * represents. This info is acquired using this prime factorization.
     */
//    public List<String> getFactorsInfo() {
//        long number = getNumber();
//        StringBuilder sb = new StringBuilder();
//        ArrayList<String> info = new ArrayList<>();
//        info.add("The prime factorization of " + number + " is " + this);
//        if (isForAPrimeNumber()) {
//            info.add(number + " is prime and the only factors it has are itself and 1");
//            return info;
//        }
//
//        info.add("By looking at the powers of all the prime factors, we can see that there are");
//        // Find number of factors by taking the powers of all the prime factors in the prime factorization,
//        // adding 1 to each and multiplying these all together. Also build a string that shows how the
//        // number of factors was determined.
//        int numberOfFactors = 1;
//        for (int primeFactor : pfMap.keySet()) {
//            int power = pfMap.get(primeFactor);
//            numberOfFactors *= (power + 1);
//            sb.append("(").append(power).append(" + 1) x ");
//        }
//        // Delete last " x " of the built string
//        sb.delete(sb.length() - 3, sb.length());
//        sb.append(" = ").append(numberOfFactors).append(" total factors.");
//        info.add(sb.toString());
//        sb.delete(0, sb.length());
//        info.add(
//                "If 1 and " + number + " are excluded then there are " + (numberOfFactors - 2) + " factors"
//        );
//
//        info.add("By looking at the \"sub-factorizations\", we can see that these factors are:");
//        // Find all the factors and add their prime factorizations to the info list to show that they are
//        // "sub-factorizations". Some examples of "sub-factorizations" are 2 and 2 * 3 in the prime factorization
//        // 2 * 3 * 5.
//        long highestPossibleFactor = number / 2;
//        for (int potentialFactor = 2; potentialFactor <= highestPossibleFactor; potentialFactor++) {
//            if (number % potentialFactor == 0) {
//                PrimeFactorization factorPf = new PrimeFactorization(potentialFactor);
//                sb.append(factorPf.toString());
//                // If the prime factorization doesn't consist of single number that is raised to the power 1,
//                // display what number that prime factorization is for in parentheses.
//                if (!factorPf.isForAPrimeNumber()) {
//                    sb.append(" (").append(potentialFactor).append(")");
//                }
//                // Add space between each factor
//                sb.append("     ");
//
//                // Start a new line once the current line exceeds 60 characters
//                if (sb.length() > 60) {
//                    info.add(sb.toString());
//                    sb.delete(0, sb.length());
//                }
//            }
//        }
//        if (sb.length() > 0) {
//            info.add(sb.toString());
//        }
//        return info;
//    }
}
