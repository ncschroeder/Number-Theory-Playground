import java.util.ArrayList;

/**
 * Utility class consisting of methods related to the Goldbach conjecture.
 */
public class Goldbach {
    public static String[] getSectionInfo() {
        return new String[] {
                "The Goldbach conjecture states that every even number greater than or equal to 4 can be ",
                "expressed as the sum of 2 prime numbers. A conjecture is a statement that is believed to be ",
                "true but has not been proven to be true."
        };
    }

    /**
     * @return An ArrayList of Strings that contain the pairs of prime numbers that sum up to the argument number.
     * Each String contains a max of 4 of these pairs.
     * @throws IllegalArgumentException if the argument number is not even or is less than 4.
     */
    public static ArrayList<String> getGoldbachPairs(int number) {
        // Number must be even and greater than or equal to 4.
        if (number % 2 != 0 || number < 4) {
            throw new IllegalArgumentException("Can only get Goldbach pairs of an even number greater than or equal to 4");
        }

        ArrayList<String> pairs = new ArrayList<>();
        // 4 is a special case since it is the only one that has 2 in it's solution.
        if (number == 4) {
            pairs.add("2 and 2");
            return pairs;
        }

        int count = 0;
        StringBuilder pairsSb = new StringBuilder();

        // First, check if 3 and a prime number sum to the number. 3 is special because it and 2 are the only
        // prime numbers that are not either 1 below or 1 above a multiple of 6.
        if (Primes.isPrime(number - 3)) {
            pairsSb.append("3 and ").append(number - 3).append("   ");
            count++;
        }

        // Iterate through potential prime numbers, which are either 1 above or 1 below a multiple of 6. Check if
        // a potential prime number and the difference between that potential prime number and the argument number
        // are both prime. If so, then a pair of prime numbers that sum to the argument number has been found. Take
        // appropriate action in this case. The max potential prime number that needs to be checked is equal to the
        // floor of half of the argument number. This is because after that point, calculations for primality will be
        // done on numbers that have already been determined to be pairs of prime numbers that sum to the argument number.

        int upperBound = number / 2;
        for (int potentialPrime = 5; ; potentialPrime += 4) {
            for (int i = 0; i < 2; i++) {
                if (potentialPrime > upperBound) {
                    // End has been reached
                    if (pairsSb.length() > 0) {
                        pairs.add(pairsSb.toString());
                    }
                    return pairs;
                }

                int otherPotentialPrime = number - potentialPrime;
                if (Primes.isPrime(potentialPrime) && Primes.isPrime(otherPotentialPrime)) {
                    pairsSb.append(potentialPrime).append(" and ").append(otherPotentialPrime).append("   ");
                    count++;
                    // Once pairsSb has 4 pairs, add these pairs to the pairs ArrayList and clear pairsSb to put
                    // new pairs in.
                    if (count % 4 == 0) {
                        pairs.add(pairsSb.toString());
                        pairsSb.delete(0, pairsSb.length());
                    }
                }

                if (i == 0) {
                    potentialPrime += 2;
                }
            }
        }
    }
}