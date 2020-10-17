/**
 * Utility class consisting of methods related to prime numbers and twin primes.
 */
public class Primes {
    public static String[] getPrimesSectionInfo() {
        return new String[] {
                "Prime numbers are numbers that are only divisible by 1 and themself.",
                "There are an infinite amount of them.",
                "A number can be determined to be prime if it is not divisible by any prime ",
                "numbers less than or equal to the square root of that number.",
                "Fun fact: with the exception of 2 and 3, all prime numbers are either",
                "1 above or 1 below a multiple of 6."
        };
    }

    public static String[] getTwinPrimesSectionInfo() {
        return new String[] {
                "Twin primes are prime numbers that differ by 2.",
                "It is conjectured that there are infinitely many of them.",
                "A conjecture is a statement that is believed to be true but has not been proven to be."
        };
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
     * @return An array of 6 strings that contain the first 30 prime numbers after the argument number. Each of these
     * strings contains 5 prime numbers.
     */
    public static String[] getPrimesAfter(int number) {
        int count = 0;
        StringBuilder primesSb = new StringBuilder();
        String[] primesLines = new String[6];
        // insertionIndex is for the primeLines array
        int insertionIndex = 0;
        int potentialPrime;

        // First, check if 2 and 3 should be added to one of the strings.
        if (number <= 5) {
            // Make potentialPrime the first positive number that is 1 below a multiple of 6.
            potentialPrime = 5;
            if (number <= 2) {
                primesSb.append("2   3   ");
                count += 2;
            } else if (number == 3) {
                primesSb.append("3   ");
                count++;
            }
        } else {
            switch (number % 6) {
                case 0:
                    // For numbers that are divisible by 6, check if the next number is prime and set potentialPrime
                    // to the next number that is 1 below a multiple of 6.
                    if (isPrime(number + 1)) {
                        primesSb.append(number + 1).append("   ");
                        count++;
                    }
                    potentialPrime = number + 5;
                    break;

                case 1:
                    // For numbers that are 1 above a multiple of 6, check if this number is prime and set
                    // potentialPrime to the next number that is 1 below a multiple of 6.
                    if (isPrime(number)) {
                        primesSb.append(number).append("   ");
                        count++;
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
        // of 6. Check these numbers for primality and take appropriate action when so.
        for (;; potentialPrime += 4) {
            for (int i = 0; i < 2; i++) {
                if (isPrime(potentialPrime)) {
                    primesSb.append(potentialPrime).append("   ");
                    count++;
                    if (count % 5 == 0) {
                        // Once primesSb has 5 entries, add the contents of primesSb to the primeLines array
                        // and clear primesSb.
                        primesLines[insertionIndex] = primesSb.toString();
                        primesSb.delete(0, primesSb.length());
                        insertionIndex++;
                    }
                    if (count == 30) {
                        return primesLines;
                    }
                }
                if (i == 0) {
                    potentialPrime += 2;
                }
            }
        }
    }

    /**
     * @return An array of 5 strings that contains the first 20 pairs of twin primes after the argument number.
     * Each string consists of 4 pairs of twin primes.
     */
    public static String[] getTwinPrimesAfter(int number) {
        String[] twinPrimesLines = new String[5];
        // insertionIndex is for the twinPrimesLines array
        int insertionIndex = 0;
        StringBuilder twinPrimesSb = new StringBuilder();
        int count = 0;

        if (number <= 3) {
            // 3 and 5 are the only special case since all other twin prime pairs consist of 1 number that is 1
            // below a multiple of 6 and the other number is 1 above a multiple of 6.
            twinPrimesSb.append("3 and 5     ");
            count++;
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
        // prime, then a twin prime pair is found. Appropriate action is taken in this case.
        for (;; potentialPrime += 6) {
            if (isPrime(potentialPrime) && isPrime(potentialPrime + 2)) {
                twinPrimesSb.append(potentialPrime).append(" and ").append(potentialPrime + 2).append("     ");
                count++;
                if (count % 4 == 0) {
                    // Once twinPrimesSb contains 4 twin primes pairs, add these pairs to the twinPrimesLines array
                    // and clear twinPrimesSb.
                    twinPrimesLines[insertionIndex] = twinPrimesSb.toString();
                    insertionIndex++;
                    twinPrimesSb.delete(0, twinPrimesSb.length());
                }
                if (count == 20) {
                    return twinPrimesLines;
                }
            }
        }
    }
}
