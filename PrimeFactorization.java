import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Utility class consisting of methods related to prime factorizations. A PrimeFactorization object can be instantiated
 * privately to help with implementation of the public methods.
 */
public class PrimeFactorization {
    public static String[] getSectionInfo() {
        return new String[]{
                "The fundamental theorem of arithmetic states that every integer greater than 1 can be expressed ",
                "as the product of prime numbers. This product is called it's prime factorization. There are some",
                "interesting applications for this. Visit the GCD and LCM or the divisibility sections for some applications."
        };
    }

    /**
     * @return A string representation of a number's prime factorization.
     * @throws IllegalArgumentException if the argument number is less than 2.
     */
    public static String getPfString(int number) {
        return new PrimeFactorization(number).toString();
    }

    /**
     * @return An ArrayList of Strings that consists of divisibility info about the argument number. This info is
     * acquired by looking at the prime factorization of the argument number.
     * @throws IllegalArgumentException if the argument number is less than 2.
     */
    public static ArrayList<String> getDivisInfo(int number) {
        PrimeFactorization pf = new PrimeFactorization(number);
        ArrayList<String> divisInfo = new ArrayList<>();
        divisInfo.add("Prime factorization info:");
        divisInfo.add("The prime factorization for " + number + " is " + pf.toString());
        if (pf.isForAPrimeNumber()) {
            divisInfo.add(number + " is prime and doesn't have any factors other than 1 and itself");
            return divisInfo;
        }

        StringBuilder sb = new StringBuilder();

        // The number of factors can be determined by taking all the powers of each prime factor, add 1 to each
        // of them, and then multiply them all together. The following code calculates this and makes a string
        // that shows how it was calculated.
        divisInfo.add("By looking at the powers of all the prime factors, we can see that there are");
        int numberOfFactors = 1;
        for (int primeFactor : pf.pfMap.keySet()) {
            int power = pf.pfMap.get(primeFactor);
            numberOfFactors *= (power + 1);
            sb.append("(").append(power).append(" + 1) * ");
        }
        // Delete last " * "
        sb.delete(sb.length() - 3, sb.length());

        sb.append(" = ").append(numberOfFactors);
        divisInfo.add(sb.toString());
        sb.delete(0, sb.length());
        divisInfo.add(
                "total factors. If 1 and " + number + " are excluded then there are " +
                        (numberOfFactors - 2) + " factors"
        );

        // Find all the factors and add their prime factorizations to divisInfo to show that they are
        // "sub-factorizations". Some examples of "sub-factorizations" are 2 and 2 * 3 in the prime factorization
        // 2 * 3 * 5.
        divisInfo.add("By looking at the \"sub-factorizations\", we can see that these factors are:");
        int highestPossibleFactor = number / 2;
        for (int potentialFactor = 2; potentialFactor <= highestPossibleFactor; potentialFactor++) {
            if (number % potentialFactor == 0) {
                PrimeFactorization factorPf = new PrimeFactorization(potentialFactor);
                sb.append(factorPf.toString());
                if (!factorPf.isForAPrimeNumber()) {
                    sb.append(" (").append(potentialFactor).append(")");
                }
                sb.append("     ");

                // Start a new line once the current line reaches a certain length
                if (sb.length() > 60) {
                    divisInfo.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            }
        }
        if (sb.length() > 0) {
            divisInfo.add(sb.toString());
        }
        return divisInfo;
    }

    /**
     * @return An array of strings that contain info about the GCD and LCM of the two argument numbers. This info is
     * acquired by looking at the prime factorizations of the two argument numbers.
     * @throws IllegalArgumentException if firstNumber is less than 2 or secondNumber is less than 2.
     */
    public static String[] getGcdAndLcmInfo(int firstNumber, int secondNumber) {
        PrimeFactorization pf1 = new PrimeFactorization(firstNumber);
        PrimeFactorization pf2 = new PrimeFactorization(secondNumber);
        PrimeFactorization gcdPfOrNull = getGcdPfOrNull(pf1, pf2);
        PrimeFactorization lcmPf = getLcmPf(pf1, pf2);

        String gcdInfo;
        if (gcdPfOrNull == null) {
            gcdInfo = "There are no common prime factors so the GCD is 1";
        } else {
            gcdInfo = "The prime factorization of the GCD is " + gcdPfOrNull.toString();
            if (!gcdPfOrNull.isForAPrimeNumber()) {
                gcdInfo += ", which is " + gcdPfOrNull.getNumber();
            }
        }

        return new String[]{
                "Prime factorization info:",
                "The prime factorization of " + firstNumber + " is " + pf1.toString(),
                "The prime factorization of " + secondNumber + " is " + pf2.toString(),
                gcdInfo,
                "The prime factorization of the LCM is " + lcmPf.toString() + ", which is " + lcmPf.getNumber()
        };
    }

    /**
     * @return null if there are no common prime factors for pf1 and pf2. Otherwise a new PrimeFactorization for the
     * GCD of the numbers represented by the PrimeFactorizations pf1 and pf2.
     */
    private static PrimeFactorization getGcdPfOrNull(PrimeFactorization pf1, PrimeFactorization pf2) {
        // Find the prime factors that are in both prime factorizations and add that prime factor along with the lower
        // of the 2 powers to the gcd prime factorization map.
        Map<Integer, Integer> gcdPfMap = new TreeMap<>();
        for (int primeFactor : pf1.pfMap.keySet()) {
            if (pf2.pfMap.containsKey(primeFactor)) {
                int power1 = pf1.pfMap.get(primeFactor);
                int power2 = pf2.pfMap.get(primeFactor);
                gcdPfMap.put(primeFactor, Math.min(power1, power2));
            }
        }
        if (gcdPfMap.isEmpty()) {
            return null;
        }
        return new PrimeFactorization(gcdPfMap);
    }

    /**
     * @return A new PrimeFactorization object that is for the LCM of the 2 numbers represented by the
     * PrimeFactorizations pf1 and pf2.
     */
    private static PrimeFactorization getLcmPf(PrimeFactorization pf1, PrimeFactorization pf2) {
        Map<Integer, Integer> lcmPfMap = new TreeMap<>();

        // Go through the prime factors of the first prime factorization and if that same prime factor is in the
        // prime factorization of the second number, add that prime factor with the higher power of the 2 to the
        // LCM prime factorization map.
        for (int primeFactor : pf1.pfMap.keySet()) {
            int power1 = pf1.pfMap.get(primeFactor);
            if (pf2.pfMap.containsKey(primeFactor)) {
                int power2 = pf2.pfMap.get(primeFactor);
                lcmPfMap.put(primeFactor, Math.max(power1, power2));
            } else {
                lcmPfMap.put(primeFactor, power1);
            }
        }

        // Find the unique prime factors of the second prime factorization and add these to the LCM
        // prime factorization map
        for (int primeFactor : pf2.pfMap.keySet()) {
            if (!pf1.pfMap.containsKey(primeFactor)) {
                int power = pf2.pfMap.get(primeFactor);
                lcmPfMap.put(primeFactor, power);
            }
        }
        return new PrimeFactorization(lcmPfMap);
    }

    /**
     * A map whose keys are the prime factors of a number and the values are the powers those factors are raised to
     * to form the prime factorization of a number.
     */
    private final Map<Integer, Integer> pfMap;

    /**
     * @throws IllegalArgumentException if the argument number is less than 2.
     */
    private PrimeFactorization(int number) {
        if (number < 2) {
            throw new IllegalArgumentException("Cannot get prime factorization of number less than 2");
        }
        pfMap = new TreeMap<>();

        // Find all the prime factors and their powers and put these in pfMap. Divide the number variable by each
        // prime factor that is found. When the number variable becomes 1, then the entire prime factorization
        // has been found.

        // First, check if the number has 2 and 3 as prime factors. These are special cases since 2 and 3 are the
        // only prime numbers that are not 1 above or 1 below a multiple of 6.
        for (int potentialPrimeFactor = 2; potentialPrimeFactor <= 3; potentialPrimeFactor++) {
            if (number % potentialPrimeFactor == 0) {
                int power = 0;
                do {
                    power++;
                    number /= potentialPrimeFactor;
                } while (number % potentialPrimeFactor == 0);
                pfMap.put(potentialPrimeFactor, power);
                if (number == 1) {
                    return;
                }
            }
        }

        // Iterate through potential prime numbers, which are either 1 below or 1 above a multiple of 6.
        for (int potentialPrimeFactor = 5; ; potentialPrimeFactor += 4) {
            for (int i = 0; i < 2; i++) {
                if (number % potentialPrimeFactor == 0) {
                    int power = 0;
                    do {
                        power++;
                        number /= potentialPrimeFactor;
                    } while (number % potentialPrimeFactor == 0);
                    pfMap.put(potentialPrimeFactor, power);
                    if (number == 1) {
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
    private PrimeFactorization(Map<Integer, Integer> pfMap) {
        if (pfMap.isEmpty()) {
            throw new IllegalArgumentException("Cannot create prime factorization from an empty map");
        }
        this.pfMap = pfMap;
    }

    /**
     * @return The number that this prime factorization is for, that is, the product of all the prime factors raised
     * to their respective powers.
     */
    private long getNumber() {
        long number = 1;
        for (int primeFactor : pfMap.keySet()) {
            int power = pfMap.get(primeFactor);
            number *= Math.pow(primeFactor, power);
        }
        return number;
    }

    /**
     * @return A string representation of a prime factorization. "*" is used to represent multiplication among all
     * the prime factors and is placed between all the prime factors. "^" is used to represent the powers of prime
     * factors and is used for prime factors that have a power other than 1.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int primeFactor : pfMap.keySet()) {
            sb.append(primeFactor);
            int power = pfMap.get(primeFactor);

            // Only display the power of a prime factor if the power is not 1
            if (power != 1) {
                sb.append("^").append(power);
            }
            sb.append(" * ");
        }
        // Delete last " * "
        sb.delete(sb.length() - 3, sb.length());
        return sb.toString();
    }

    /**
     * If this is true, then this prime factorization consists of a single number that is not raised to any power.
     *
     * @return true if the number that this prime factorization is representing is prime and false otherwise.
     */
    private boolean isForAPrimeNumber() {
        return pfMap.size() == 1 && pfMap.containsValue(1);
    }
}