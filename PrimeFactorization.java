import java.util.*;

/**
 * A class that has methods related to prime factorizations. Some methods are static and some require an instance
 * of this class.
 */
public class PrimeFactorization {
    public static String getSectionInfo() {
        return "The fundamental theorem of arithmetic states that every integer greater than 1 can be expressed " +
                "as the product of prime numbers. This product is called it's prime factorization. There are some " +
                "interesting applications for this. Visit the GCD and LCM or the divisibility sections for some " +
                "applications.";
    }

    /**
     * @return A string representation of the argument number's prime factorization.
     * @throws IllegalArgumentException if the argument number is less than 2.
     */
    public static String getPfString(int number) {
        return new PrimeFactorization(number).toString();
    }

    /**
     * A map whose keys are the prime factors of a number and the values are the powers those factors are raised to
     * to form the prime factorization of a number.
     */
    private final Map<Integer, Integer> pfMap;

    /**
     * @throws IllegalArgumentException if the argument number is less than 2.
     */
    public PrimeFactorization(int number) {
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
    public PrimeFactorization(Map<Integer, Integer> pfMap) {
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

            // Only display the power of a prime factor if the power is not 1
            if (power != 1) {
                sb.append("^").append(power);
            }
            sb.append(" x ");
        }
        // Delete last " x "
        sb.delete(sb.length() - 3, sb.length());
        return sb.toString();
    }

    public Map<Integer, Integer> toMap() {
        return pfMap;
    }

    /**
     * @return The number that this prime factorization is for, that is, the product of all the prime factors raised
     * to their respective powers. This method returns a long for when the LCM of 2 numbers is too big for an int.
     */
    public long getNumber() {
        long number = 1;
        for (int primeFactor : pfMap.keySet()) {
            int power = pfMap.get(primeFactor);
            number *= Math.pow(primeFactor, power);
        }
        return number;
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
    public List<String> getFactorsInfo() {
        long number = getNumber();
        StringBuilder sb = new StringBuilder();
        ArrayList<String> info = new ArrayList<>();
        info.add("The prime factorization of " + number + " is " + this);
        if (isForAPrimeNumber()) {
            info.add(number + " is prime and the only factors it has are itself and 1");
            return info;
        }

        info.add("By looking at the powers of all the prime factors, we can see that there are");
        // Find number of factors by taking the powers of all the prime factors in the prime factorization,
        // adding 1 to each and multiplying these all together. Also build a string that shows how the
        // number of factors was determined.
        int numberOfFactors = 1;
        for (int primeFactor : pfMap.keySet()) {
            int power = pfMap.get(primeFactor);
            numberOfFactors *= (power + 1);
            sb.append("(").append(power).append(" + 1) * ");
        }
        // Delete last " * "
        sb.delete(sb.length() - 3, sb.length());
        sb.append(" = ").append(numberOfFactors).append(" total factors.");
        info.add(sb.toString());
        sb.delete(0, sb.length());
        info.add(
                "If 1 and " + number + " are excluded then there are " + (numberOfFactors - 2) + " factors"
        );

        info.add("By looking at the \"sub-factorizations\", we can see that these factors are:");
        // Find all the factors and add their prime factorizations to the info list to show that they are
        // "sub-factorizations". Some examples of "sub-factorizations" are 2 and 2 * 3 in the prime factorization
        // 2 * 3 * 5.
        long highestPossibleFactor = number / 2;
        for (int potentialFactor = 2; potentialFactor <= highestPossibleFactor; potentialFactor++) {
            if (number % potentialFactor == 0) {
                PrimeFactorization factorPf = new PrimeFactorization(potentialFactor);
                sb.append(factorPf.toString());
                // If the prime factorization doesn't consist of single number that is raised to the power 1,
                // display what number that prime factorization is for in parentheses.
                if (!factorPf.isForAPrimeNumber()) {
                    sb.append(" (").append(potentialFactor).append(")");
                }
                sb.append("     ");

                // Start a new line once the current line exceeds 60 characters
                if (sb.length() > 60) {
                    info.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            }
        }
        if (sb.length() > 0) {
            info.add(sb.toString());
        }

        return info;
    }
}
