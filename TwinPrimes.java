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
     * @return A list of strings that say the first 20 pairs of twin prime numbers that appear after the number passed
     * in as an argument. Each string consists of the 2 numbers separated by " and ".
     */
    public static List<String> getTwinPrimesAfter(int number) {
        ArrayList<String> twinPrimes = new ArrayList<>(20);
        if (number <= 3) {
            // 3 and 5 are the only special case since all other twin prime pairs consist of 1 number that is 1
            // below a multiple of 6 and the other number is 1 above a multiple of 6.
            twinPrimes.add("3 and 5");
        }
        int potentialPrime;
        if (number <= 5) {
            // Make potentialPrime 5 since that is first positive number that is 1 below a multiple of 6.
            potentialPrime = 5;
        } else {
            // Make potentialPrime the next number that is 1 below a multiple of 6.
            potentialPrime = number;
            while ((potentialPrime + 1) % 6 != 0) {
                potentialPrime++;
            }
        }

        // With the exception of 2 and 3, all prime numbers are either 1 below or 1 above a multiple of 6.
        // With the exception of 3 and 5, all twin prime pairs consist of 1 number that is 1 below a multiple of 6
        // and 1 number that is 1 above a multiple of 6. Check each of these pairs of numbers and if they're both
        // prime, then a twin prime pair is found.
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
