import java.util.*;

/**
 * Utility class consisting of methods related to GCDs and LCMs.
 */
public class GcdAndLcm {
    public static String getSectionInfo() {
        return "GCD stands for greatest common divisor and LCM stands for least common multiple. One way to find the " +
                "GCD and LCM of 2 numbers is to look at the prime factorizations of those numbers. If those numbers do " +
                "not have any common prime factors, then the GCD is 1. If they do have common prime factors, then the " +
                "prime factorization of the GCD consists of all the common prime factors and the power of each factor " +
                "is the minimum power of that factor in the 2 prime factorizations. The prime factorization of the LCM " +
                "consists of all factors that are in either of the prime factorizations of the 2 numbers. The power " +
                "for each factor is the maximum power of that factor in the 2 prime factorizations. The Euclidean " +
                "algorithm can be used to find the GCD of 2 numbers, usually faster than calculating the prime " +
                "factorizations. For the Euclidean algorithm, first take 2 numbers. If the bigger number is divisible " +
                "by the smaller number, then the smaller number is the GCD. Otherwise, the GCD of the 2 numbers is the " +
                "same as the GCD of the smaller number and the remainder when the bigger number is divided by the " +
                "smaller number. Repeat.";
    }

    /**
     * @return A list containing info about the Euclidean algorithm done on firstNumber and secondNumber. The strings in
     * this list contain info about iterations and there is another string at the end about which number is the GCD.
     * @throws IllegalArgumentException if firstNumber or secondNumber is nonpositive.
     */
    public static List<String> getEuclideanInfo(final int firstNumber, final int secondNumber) {
        if (firstNumber < 1 || secondNumber < 1) {
            throw new IllegalArgumentException("Can't find GCD for nonpositive numbers");
        }

        ArrayList<String> info = new ArrayList<>();

        int maxNumber = Math.max(firstNumber, secondNumber);
        int minNumber = Math.min(firstNumber, secondNumber);
        int remainder = maxNumber % minNumber;

        while (remainder != 0) {
            info.add(
                    maxNumber + " is not divisible by " + minNumber + ", so we now find the GCD of " +
                    minNumber + " and " + remainder
            );
            maxNumber = minNumber;
            minNumber = remainder;
            remainder = maxNumber % minNumber;
        }

        info.add(maxNumber + " is divisible by " + minNumber + ", so " + minNumber + " is the GCD of " +
                minNumber + " and " + maxNumber);

        // Check if maxNumber has changed
        if (maxNumber != Math.max(firstNumber, secondNumber)) {
            info.add("As a result, " + minNumber + " is the GCD of " + firstNumber + " and " + secondNumber);
        }
        return info;
    }

    /**
     * @return An array of 4 strings that contain info about the GCD and LCM of the two argument numbers. This info is
     * acquired by looking at the prime factorizations of the two argument numbers.
     * @throws IllegalArgumentException if firstNumber is less than 2 or secondNumber is less than 2.
     */
    public static String[] getGcdAndLcmInfoViaPf(int firstNumber, int secondNumber) {
        PrimeFactorization pf1 = new PrimeFactorization(firstNumber);
        PrimeFactorization pf2 = new PrimeFactorization(secondNumber);

        Map<Integer, Integer> pf1Map = pf1.toMap();
        Map<Integer, Integer> pf2Map = pf2.toMap();

        // The prime factorization of the GCD of 2 numbers contains all the prime factors that are in both of the prime
        // factorizations of the 2 numbers. The power of each prime factor is the minimum of the 2 powers in the 2
        // prime factorizations. If there are no common prime factors then the GCD is 1.

        // The prime factorization of the LCM of 2 numbers contains all prime factors that are in either of the prime
        // factorizations for the 2 numbers. If a prime factor is in both prime factorizations then the power of that
        // prime factor in the LCM prime factorization is the max of the 2 powers in the 2 prime factorizations. If a
        // prime factor is unique to 1 of the prime factorizations of the 2 numbers, then the power of that prime factor
        // in the LCM prime factorization is the same as in the prime factorization for that 1 number.
        Map<Integer, Integer> gcdPfMap = new TreeMap<>();
        Map<Integer, Integer> lcmPfMap = new TreeMap<>();

        for (int primeFactor : pf1Map.keySet()) {
            int power1 = pf1Map.get(primeFactor);
            if (pf2Map.containsKey(primeFactor)) {
                // Common prime factors
                int power2 = pf2Map.get(primeFactor);
                gcdPfMap.put(primeFactor, Math.min(power1, power2));
                lcmPfMap.put(primeFactor, Math.max(power1, power2));
            } else {
                // Prime factor is unique to the first prime factorization
                lcmPfMap.put(primeFactor, power1);
            }
        }

        // Find the unique prime factors of the second prime factorization
        for (int primeFactor : pf2Map.keySet()) {
            if (!pf1Map.containsKey(primeFactor)) {
                int power = pf2Map.get(primeFactor);
                lcmPfMap.put(primeFactor, power);
            }
        }

        String gcdInfo;
        if (gcdPfMap.isEmpty()) {
            gcdInfo = "There are no common prime factors so the GCD is 1";
        } else {
            PrimeFactorization gcdPf = new PrimeFactorization(gcdPfMap);
            gcdInfo = "The prime factorization of the GCD is " + gcdPf.toString();
            if (!gcdPf.isForAPrimeNumber()) {
                gcdInfo += ", which is " + gcdPf.getNumber();
            }
        }

        PrimeFactorization lcmPf = new PrimeFactorization(lcmPfMap);
        String lcmInfo = "The prime factorization of the LCM is " + lcmPf.toString();
        if (!lcmPf.isForAPrimeNumber()) {
            lcmInfo += ", which is " + lcmPf.getNumber();
        }

        return new String[] {
                "The prime factorization of " + firstNumber + " is " + pf1.toString(),
                "The prime factorization of " + secondNumber + " is " + pf2.toString(),
                gcdInfo,
                lcmInfo
        };
    }
}