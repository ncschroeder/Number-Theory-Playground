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
                return Primes.getSectionInfo();

            case TWIN_PRIMES:
                return TwinPrimes.getSectionInfo();

            case PRIME_FACTORIZATION:
                return PrimeFactorization.getSectionInfo();

            case DIVISIBILITY:
                return Divisibility.getSectionInfo();

            case GCD_LCM:
                return GcdAndLcm.getSectionInfo();

            case GOLDBACH:
                return Goldbach.getSectionInfo();

            case PYTHAG_TRIPLES:
                return PythagoreanTriples.getSectionInfo();

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
