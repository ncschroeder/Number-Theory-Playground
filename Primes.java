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
        // All numbers less than 2 are not prime
        if (number < 2) {
            return false;
        }
        // 2 and 3 are prime
        if (number <= 3) {
            return true;
        }

        // Check if the argument number is divisible by any prime numbers less than or equal to the
        // floor of the square root of the argument number. If it is, then the argument number is not prime.
        // All prime numbers besides 2 and 3 are either 1 below or 1 above a multiple of 6.

        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }

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
     * @return A list of the first 30 prime numbers that are greater than or equal to the argument number.
     */
    public static List<Integer> getPrimesAfter(int number) {
        ArrayList<Integer> primes = new ArrayList<>(30);

        // All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6. This algorithm takes
        // advantage of this.

        int potentialPrime;
        if (number <= 5) {
            // Set potentialPrime to the first positive number that is 1 below a multiple of 6.
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
                    // If the argument number is divisible by 6, check if the next number is prime and set potentialPrime
                    // to the next number that is 1 below a multiple of 6.
                    if (isPrime(number + 1)) {
                        primes.add(number + 1);
                    }
                    potentialPrime = number + 5;
                    break;

                case 1:
                    // If the argument number is 1 above a multiple of 6, check if this number is prime and set
                    // potentialPrime to the next number that is 1 below a multiple of 6.
                    if (isPrime(number)) {
                        primes.add(number);
                    }
                    potentialPrime = number + 4;
                    break;

                default:
                    // Set potentialPrime be the next number that is 1 below a multiple of 6
                    potentialPrime = number;
                    while ((potentialPrime + 1) % 6 != 0) {
                        potentialPrime++;
                    }
                    break;
            }
        }

        // Check potential prime numbers for primality until 30 prime numbers are found
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