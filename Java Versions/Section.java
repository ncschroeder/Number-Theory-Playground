package com.nicholasschroeder.numbertheoryplayground;

public enum Section {
    HOME,
    PRIMES,
    TWIN_PRIMES,
    PRIME_FACTORIZATION,
    DIVISIBILITY,
    GCD_LCM,
    GOLDBACH,
    PYTHAG_TRIPLES;

    public String getInfo() {
        switch (this) {
            case HOME:
                return "Welcome to the Number Theory Playground";

            case PRIMES:
                return "Prime numbers are numbers that are only divisible by 1 and themself. There are an infinite amount " +
                        "of them. A number can be determined to be prime if it is not divisible by any prime numbers less " +
                        "than or equal to the square root of that number. Fun fact: with the exception of 2 and 3, all prime " +
                        "numbers are either 1 above or 1 below a multiple of 6.";

            case TWIN_PRIMES:
                return "Twin primes are prime numbers that differ by 2. It is conjectured that there are infinitely " +
                        "many of them. A conjecture is a statement that is believed to be true but has not been proven to be.";

            case PRIME_FACTORIZATION:
                return "The fundamental theorem of arithmetic states that every integer greater than 1 can be expressed " +
                        "as the product of prime numbers. This product is called it's prime factorization. There are some " +
                        "interesting applications for this. Visit the GCD and LCM or the divisibility sections for some " +
                        "applications.";

            case DIVISIBILITY:
                return "The factors of a number are all the numbers that can evenly divide that number. Some special " +
                        "tricks can be used to find some of the factors of a number. If the sum of the digits of a number is " +
                        "divisible by 3, then that number is divisible by 3. If the sum of the digits of a number is divisible " +
                        "by 9, then that number is divisible by 9. If a number is even and divisible by 3, then it is also " +
                        "divisible by 6. If the last 2 digits of a number is divisible by 4, then that number is divisible by " +
                        "4. If the last 3 digits of a number is divisible by 8, then that number is divisible by 8. If a " +
                        "number is divisible by both 3 and 4 then it is also divisible by 12. Another way you can tell what " +
                        "factors a number has and how many factors it has is by looking at it's prime factorization. To find " +
                        "the number of factors, you take all the powers of the prime factors, add 1 to each and then multiply " +
                        "them all together. All the \"sub-factorizations\" of this prime factorization are the prime " +
                        "factorizations of all the factors. Some examples of \"sub-factorizations\" are 2 and 2 * 3 in " +
                        "the prime factorization 2 * 3 * 5.";

            case GCD_LCM:
                return "GCD stands for greatest common divisor and LCM stands for least common multiple. One way to find the " +
                        "GCD and LCM of 2 numbers is to look at the prime factorizations of those numbers. If those numbers do " +
                        "not have any common prime factors, then the GCD is 1. If they do have common prime factors, then the " +
                        "prime factorization of the GCD consists of all the common prime factors and the power of each factor " +
                        "is the minimum power of that factor in the 2 prime factorizations. The prime factorization of the LCM " +
                        "consists of all factors that are in either of the prime factorizations of the 2 numbers. The power " +
                        "for each factor is the maximum power of that factor in the 2 prime factorizations. The Euclidean " +
                        "algorithm can be used to find the GCD of 2 numbers, usually faster than calculating the prime " +
                        "factorizations. For the Euclidean algorithm, first take 2 numbers. If the bigger number is divisible " +
                        "by the smaller number, then the smaller number is the GCD. Otherwise, the GCD of the 2 numbers is the " +
                        "same as the GCD of the smaller number and the remainder when the bigger number is divided by the " +
                        "smaller number. Repeat.";

            case GOLDBACH:
                return "The Goldbach conjecture states that every even number greater than or equal to 4 can be " +
                        "expressed as the sum of 2 prime numbers. A conjecture is a statement that is believed to be " +
                        "true but has not been proven to be true. The Goldbach conjecture has been verified to be true " +
                        "for all even numbers greater than or equal to 4 and less than or equal to a very high number. " +
                        "I don't know this number off the top of my head but it's way, way bigger than the maximum " +
                        "number you are allowed to use for input.";

            case PYTHAG_TRIPLES:
                return "The Pythagorean theorem says that for the side lengths of a right triangle, " +
                        "the sum of the squares of the 2 short sides equals the square of the long side (hypotenuse) " +
                        "or a^2 + b^2 = c^2. There are an infinite amount of trios of integers that a, b, and c " +
                        "can be. These trios are called Pythagorean triples.";

            default:
                return null;
        }
    }

    /**
     * @return 2 if this section is PRIME_FACTORIZATION, DIVISIBILITY, or GCD_LCM.
     * 4 if this section is GOLDBACH. 0 for all other situations.
     */
    public int getMinInputInt() {
        switch (this) {
            case PRIME_FACTORIZATION:
            case DIVISIBILITY:
            case GCD_LCM:
                return 2;

            case GOLDBACH:
                return 4;

            default:
                return 0;
        }
    }

    /**
     * @return 1 billion if this section is PRIMES, or TWIN_PRIMES.
     * 10 thousand if this section is PRIME_FACTORIZATION, DIVISIBILITY, or GCD_LCM.
     * 100 thousand if this section is GOLDBACH. 1 thousand if this section is PYTHAG_TRIPLES.
     * 0 for all other situations.
     */
    public int getMaxInputInt() {
        switch (this) {
            case PRIMES:
            case TWIN_PRIMES:
                return 1_000_000_000;

            case PRIME_FACTORIZATION:
            case DIVISIBILITY:
            case GCD_LCM:
                return 10_000;

            case GOLDBACH:
                return 100_000;

            case PYTHAG_TRIPLES:
                return 1_000;

            default:
                return 0;
        }
    }
}
