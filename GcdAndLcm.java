import java.util.ArrayList;

/**
 * Utility class consisting of methods related to GCDs and LCMs. There is another method for getting GCD and LCM info
 * from using a number's prime factorization and this method is in the PrimeFactorization utility class.
 */
public class GcdAndLcm {
    public static String[] getSectionInfo() {
        return new String[] {
                "GCD stands for greatest common divisor and LCM stands for least common multiple.",
                "One way to find the GCD and LCM of 2 numbers is to look at the prime factorizations of those numbers.",
                "If those numbers do not have any common prime factors, then the GCD is 1. If they do have common prime ",
                "factors, then the prime factorization of the GCD consists of all the common prime factors and the power",
                "of each factor is the minimum power of that factor in the 2 prime factorizations. The prime factorization",
                "of the LCM consists of all factors that are in either of the prime factorizations of the 2 numbers. The",
                "power for each factor is the maximum power of that factor in the 2 prime factorizations.",
                "The Euclidean algorithm can be used to find the GCD of 2 numbers, usually faster than calculating the prime factorizations.",
                "For the Euclidean algorithm, first take 2 numbers. If the bigger number is divisible by the smaller number,",
                "then the smaller number is the GCD. Otherwise, the GCD of the 2 numbers is the same as the GCD of the smaller number",
                "and the remainder when the bigger number is divided by the smaller number. Repeat."
        };
    }

    /**
     * @return An ArrayList consisting of info about every iteration of the euclidean algorithm done on firstNumber
     * and secondNumber. Every element of this ArrayList is info about a single iteration and there is another element
     * about which number is the GCD at the end. An ArrayList is used since the amount of iterations varies.
     * @throws IllegalArgumentException if firstNumber or secondNumber is nonpositive.
     */
    public static ArrayList<String> getEuclideanInfo(final int firstNumber, final int secondNumber) {
        if (firstNumber < 1 || secondNumber < 1) {
            throw new IllegalArgumentException("Can't find GCD for nonpositive numbers");
        }

        ArrayList<String> euclideanInfo = new ArrayList<>();
        euclideanInfo.add("Euclidean algorithm info");

        int maxNumber = Math.max(firstNumber, secondNumber);
        int minNumber = Math.min(firstNumber, secondNumber);
        int remainder = maxNumber % minNumber;

        while (remainder != 0) {
            euclideanInfo.add(maxNumber + " is not divisible by " + minNumber + ", so we now find the GCD of " +
                    minNumber + " and " + remainder);
            maxNumber = minNumber;
            minNumber = remainder;
            remainder = maxNumber % minNumber;
        }

        euclideanInfo.add(maxNumber + " is divisible by " + minNumber + ", so " + minNumber + " is the GCD of " +
                minNumber + " and " + maxNumber);

        // Check if maxNumber has changed
        if (maxNumber != Math.max(firstNumber, secondNumber)) {
            euclideanInfo.add("As a result, " + minNumber + " is the GCD of " + firstNumber + " and " + secondNumber);
        }
        return euclideanInfo;
    }
}
