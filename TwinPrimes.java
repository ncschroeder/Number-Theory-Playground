import java.util.ArrayList;
import java.util.List;

/**
 * Utility class containing methods related to twin primes.
 */
public class TwinPrimes {
    public static String getSectionInfo() {
        return "Twin primes are prime numbers that differ by 2. It is conjectured that there are infinitely " +
                "many of them. A conjecture is a statement that is believed to be true but has not been proven to be.";
    }

    /**
     * @return A list of strings that say the first 20 pairs of twin prime numbers that appear after the argument number.
     * Each string consists of the 2 numbers separated by " and ".
     */
    public static List<String> getTwinPrimesAfter(int number) {
        ArrayList<String> twinPrimes = new ArrayList<>(20);

        // With the exception of 2 and 3, all prime numbers are either 1 above or 1 below a multiple of 6. This means
        // that all prime number pairs besides 3 and 5 consist of 1 number that is 1 below a multiple of 6 and the other
        // number is 1 above that same multiple of 6. This algorithm takes advantage of this.

        if (number <= 3) {
            twinPrimes.add("3 and 5");
        }

        int potentialPrime;
        if (number <= 5) {
            // Set potentialPrime to the first positive number that is 1 below a multiple of 6
            potentialPrime = 5;
        } else {
            // Set potentialPrime to the first number greater than or equal to the argument number that is 1 below
            // a multiple of 6
            potentialPrime = number;
            while ((potentialPrime + 1) % 6 != 0) {
                potentialPrime++;
            }
        }

        // Check pairs of numbers that are potentially twin prime pairs until 20 twin prime pairs are found
        for (;; potentialPrime += 6) {
            if (Primes.isPrime(potentialPrime) && Primes.isPrime(potentialPrime + 2)) {
                twinPrimes.add(potentialPrime + " and " + (potentialPrime + 2));
                if (twinPrimes.size() == 20) {
                    return twinPrimes;
                }
            }
        }
    }
}