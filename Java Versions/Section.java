public enum Section {
    HOME("Home", getHomeInfo()),
    PRIMES("Prime Numbers", getPrimesInfo()),
    TWIN_PRIMES("Twin Prime Numbers", getTwinPrimesInfo()),
    PRIME_FACTORIZATION("Prime Factorization", getPfInfo()),
    DIVISIBILITY("Divisibility", getDivisInfo()),
    GCD_AND_LCM("GCD and LCM", getGcdAndLcmInfo()),
    GOLDBACH("Goldbach Conjecture", getGoldbachInfo()),
    PYTHAG_TRIPLES("Pythagorean Triples", getPythagTriplesInfo());

    private static final Random random = new Random();

    private final String headingText;

    private final String info;

    Section(String headingText, String info) {
        this.headingText = headingText;
        this.info = info;
    }

    public String getHeadingText() {
        return headingText;
    }

    public String getInfo() {
        return info;
    }

    /**
     * @return
     * 2 if this section is PRIME_FACTORIZATION, DIVISIBILITY, or GCD_AND_LCM;
     * 4 if this section is GOLDBACH;
     * 0 for all other situations.
     */
    public int getMinInputInt() {
        switch (this) {
            case PRIME_FACTORIZATION:
            case DIVISIBILITY:
            case GCD_AND_LCM:
                return 2;

            case GOLDBACH:
                return 4;

            default:
                return 0;
        }
    }

    /**
     * @return
     * 1 thousand if this section is PYTHAG_TRIPLES;
     * 10 thousand if this section is PRIME_FACTORIZATION, DIVISIBILITY, or GCD_AND_LCM;
     * 100 thousand if this section is GOLDBACH;
     * 1 billion if this section is PRIMES or TWIN_PRIMES;
     * 0 for all other sections.
     */
    public int getMaxInputInt() {
        switch (this) {
            case PYTHAG_TRIPLES:
                return 1_000;

            case PRIME_FACTORIZATION:
            case DIVISIBILITY:
            case GCD_AND_LCM:
                return 10_000;

            case GOLDBACH:
                return 100_000;

            case PRIMES:
            case TWIN_PRIMES:
                return 1_000_000_000;

            default:
                return 0;
        }
    }

    public String getMaxInputString() {
        int maxInputInt = getMaxInputInt();
        switch (maxInputInt) {
            case 1_000:
                return "1 thousand";

            case 10_000:
                return "10 thousand";

            case 100_000:
                return "100 thousand";

            case 1_000_000_000:
                return "1 billion";

            default:
                logError("");
                return getLongStringWithCommas(maxInputInt);
        }
    }

    /**
     *
     * @return A random int in the appropriate range. If this method is called with GOLDBACH, then this
     * int will be even.
     */
    public int getRandomValidInt() {
        int randomInt = Math.max(getMinInputInt(), random.nextInt(getMaxInputInt()));
        if (this == GOLDBACH && isOdd(randomInt)) {
            randomInt++;
        }
        return randomInt;
    }

    public boolean needs2Ints() {
        return this == GCD_AND_LCM;
    }

    public boolean isValidInput(int i) {
        return i >= getMinInputInt() && i <= getMaxInputInt() &&
            (this != GOLDBACH || isEven(i));
    }

    public boolean isInvalidInput(int i) {
        return !isValidInput(i);
    }

    public boolean anyAreInvalidInput(int int1, int int2) {
        return isInvalidInput(int1) || isInvalidInput(int2);
    }


    /*
    You can't use values of static variables as args for the enum constants but you can use
    returned values of static methods.
    */

    private static String getHomeInfo() {
        return "Welcome to the Number Theory Playground";
    }

    private static String getPrimesInfo() {
        return "Prime numbers are numbers that are only divisible by 1 and themself. There are " +
                "an infinite amount of them. A number can be determined to be prime if it is not " +
                "divisible by any prime numbers less than or equal to the square root of that " +
                "number. Fun fact: with the exception of 2 and 3, all prime numbers are either " +
                "1 above or 1 below a multiple of 6.";
    }

    private static String getTwinPrimesInfo() {
        return "Twin primes are prime numbers that differ by 2. It is conjectured that there " +
                "are infinitely many of them. A conjecture is a statement that is believed to be " +
                "true but has not been proven to be.";
    }

    private static String getPfInfo() {
        return "The fundamental theorem of arithmetic states that every integer greater than " +
                "1 can be expressed as the product of prime numbers. This product is called " +
                "it's prime factorization. There are some interesting applications for this. " +
                "Visit the GCD and LCM or the divisibility sections for some applications.";
    }

    private static String getDivisInfo() {
        return "The factors of a number are all the numbers that can evenly divide that number. " +
                "Some special tricks can be used to find some of the factors of a number. If the " +
                "sum of the digits of a number is divisible by 3, then that number is divisible " +
                "by 3. If the sum of the digits of a number is divisible by 9, then that number " +
                "is divisible by 9. If a number is even and divisible by 3, then it is also " +
                "divisible by 6. If the last 2 digits of a number is divisible by 4, then that " +
                "number is divisible by 4. If the last 3 digits of a number is divisible by 8, " +
                "then that number is divisible by 8. If a number is divisible by both 3 and 4 " +
                "then it is also divisible by 12. Another way you can tell what factors a number " +
                "has and how many factors it has is by looking at it's prime factorization. To " +
                "find the number of factors, you take all the powers of the prime factors, add " +
                "1 to each and then multiply them all together. All the \"sub-factorizations\" " +
                "of this prime factorization are the prime factorizations of all the factors. " +
                "Some examples of \"sub-factorizations\" are 2 and 2 * 3 in the prime " +
                "factorization 2 * 3 * 5.";
    }

    private static String getGcdAndLcmInfo() {
        return "GCD stands for greatest common divisor and LCM stands for least common multiple. " +
                "One way to find the GCD and LCM of 2 numbers is to look at the prime factorizations " +
                "of those numbers. If those numbers do not have any common prime factors, then " +
                "the GCD is 1. If they do have common prime factors, then the prime factorization " +
                "of the GCD consists of all the common prime factors and the power of each factor " +
                "is the minimum power of that factor in the 2 prime factorizations. The prime " +
                "factorization of the LCM consists of all factors that are in either of the prime " +
                "factorizations of the 2 numbers. The power for each factor is the maximum power " +
                "of that factor in the 2 prime factorizations. The Euclidean algorithm can be " +
                "used to find the GCD of 2 numbers, usually faster than calculating the prime " +
                "factorizations. For the Euclidean algorithm, first take 2 numbers. If the bigger " +
                "number is divisible by the smaller number, then the smaller number is the GCD. " +
                "Otherwise, the GCD of the 2 numbers is the same as the GCD of the smaller number " +
                "and the remainder when the bigger number is divided by the smaller number. Repeat.";
    }

    private static String getGoldbachInfo() {
        return "The Goldbach conjecture states that every even number greater than or equal to " +
                "4 can be expressed as the sum of 2 prime numbers. A conjecture is a statement " +
                "that is believed to be true but has not been proven to be true. The Goldbach " +
                "conjecture has been verified to be true for all even numbers greater than or " +
                "equal to 4 and less than or equal to a very high number. I don't know this " +
                "number off the top of my head but it's way, way bigger than the maximum number " +
                "you're allowed to use for input.";
    }

    private static String getPythagTriplesInfo() {
        return "The Pythagorean theorem says that for the side lengths of a right triangle, " +
                "the sum of the squares of the 2 short sides equals the square of the long " +
                "side (hypotenuse) or a^2 + b^2 = c^2. There are an infinite amount of trios " +
                "of integers that a, b, and c can be. These trios are called Pythagorean triples.";
    }
}
