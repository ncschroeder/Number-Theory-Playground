import java.util.ArrayList;
import java.util.List;

/**
 * Utility class consisting of methods related to the Goldbach conjecture.
 */
public class Goldbach {
    public static String getSectionInfo() {
        return "The Goldbach conjecture states that every even number greater than or equal to 4 can be " +
                "expressed as the sum of 2 prime numbers. A conjecture is a statement that is believed to be " +
                "true but has not been proven to be true. The Goldbach conjecture has been verified to be true " +
                "for all even numbers greater than or equal to 4 and less than or equal to a very high number. " +
                "I don't know this number off the top of my head but it's way, way bigger than the maximum " +
                "number you are allowed to use for input.";
    }

    /**
     * @return A list of strings that say the prime number pairs that sum up to the argument number. Each string contains
     * the 2 numbers separated by " and ".
     * @throws IllegalArgumentException if the argument number is less than 4 or is not even.
     */
    public static List<String> getGoldbachPrimePairs(int number) {
        if (number % 2 != 0 || number < 4) {
            throw new IllegalArgumentException("Number must be even and greater than or equal to 4");
        }

        ArrayList<String> pairs = new ArrayList<>();
        // All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6. First, check if the argument
        // number is 4 since 4 is the only even number greater than or equal to 4 that has 2 in a pair of prime numbers
        // that sum to it.
        if (number == 4) {
            pairs.add("2 and 2");
            return pairs;
        }

        // Check if 3 and a prime number sum to the argument number.
        if (Primes.isPrime(number - 3)) {
            pairs.add("3 and " + (number - 3));
        }

        // Check if a potential prime number and the difference between that potential prime number and the argument
        // number are both prime.

        // The max potential prime number that needs to be checked is equal to the floor of half of the argument number.
        // This is because after that point, calculations for primality will be done on numbers that have already been
        // determined to be pairs of prime numbers that sum to the argument number.

        int upperBound = number / 2;
        for (int potentialPrime = 5; ; potentialPrime += 4) {
            for (int i = 0; i < 2; i++) {
                if (potentialPrime > upperBound) {
                    return pairs;
                }
                int otherPotentialPrime = number - potentialPrime;
                if (Primes.isPrime(potentialPrime) && Primes.isPrime(otherPotentialPrime)) {
                    pairs.add(potentialPrime + " and " + otherPotentialPrime);
                }
                if (i == 0) {
                    potentialPrime += 2;
                }
            }
        }
    }
}