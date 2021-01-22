import java.util.ArrayList;
import java.util.List;

/**
 * Utility class consisting of methods related to prime numbers.
 */
public class Primes {
    public static String getSectionInfo() {
        return "Prime numbers are numbers that are only divisible by 1 and themself. There are an infinite amount " +
                "of them. A number can be determined to be prime if it is not divisible by any prime numbers less " +
                "than or equal to the square root of that number. Fun fact: with the exception of 2 and 3, all prime " +
                "numbers are either 1 above or 1 below a multiple of 6.";
    }

    public static boolean isPrime(int number) {
        // All numbers less than 2 are not prime.
        if (number < 2) {
            return false;
        }
        // 2 and 3 are prime.
        if (number <= 3) {
            return true;
        }
        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }

        // Check if the argument number is divisible by any prime numbers less than or equal to the
        // floor of the square root of the argument number. If it is, then the argument number is not prime.
        // With the exception of 2 and 3, all prime numbers are either 1 below or 1 above a multiple of 6.
        // Iterate through these numbers and check for divisibility.
        int highestNumberToCheck = (int) Math.sqrt(number);
        for (int potentialPrime = 5; ; potentialPrime += 4) {
            for (int i = 0; i < 2; i++) {
                if (potentialPrime > highestNumberToCheck) {
                    return true;
                }
                if (number % potentialPrime == 0) {
                    return false;
                }
                if (i == 0) {
                    potentialPrime += 2;
                }
            }
        }
    }

    /**
     * @return A list of the first 30 prime numbers that appear after the argument number.
     */
    public static List<Integer> getPrimesAfter(int number) {
        ArrayList<Integer> primes = new ArrayList<>(30);
        int potentialPrime;

        // First, check if 2 and 3 should be added to the primes list.
        if (number <= 5) {
            // Make potentialPrime the first positive number that is 1 below a multiple of 6.
            potentialPrime = 5;
            if (number <= 2) {
                primes.add(2);
            }
            if (number <= 3) {
                primes.add(3);
            }
        } else {
            switch (number % 6) {
                case 0:
                    // For numbers that are divisible by 6, check if the next number is prime and set potentialPrime
                    // to the next number that is 1 below a multiple of 6.
                    if (isPrime(number + 1)) {
                        primes.add(number + 1);
                    }
                    potentialPrime = number + 5;
                    break;

                case 1:
                    // For numbers that are 1 above a multiple of 6, check if this number is prime and set
                    // potentialPrime to the next number that is 1 below a multiple of 6.
                    if (isPrime(number)) {
                        primes.add(number);
                    }
                    potentialPrime = number + 4;
                    break;

                default:
                    // Have potentialPrime be the next number that is 1 below a multiple of 6
                    potentialPrime = number;
                    while ((potentialPrime + 1) % 6 != 0) {
                        potentialPrime++;
                    }
                    break;
            }
        }

        // Iterate through potential prime numbers, which are numbers that are either 1 below or 1 above a multiple
        // of 6, and check these numbers for primality.
        for (;; potentialPrime += 4) {
            for (int i = 0; i < 2; i++) {
                if (isPrime(potentialPrime)) {
                    primes.add(potentialPrime);
                    if (primes.size() == 30) {
                        return primes;
                    }
                }
                if (i == 0) {
                    potentialPrime += 2;
                }
            }
        }
    }
}
