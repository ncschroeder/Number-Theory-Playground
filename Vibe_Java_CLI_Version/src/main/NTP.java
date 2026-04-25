import java.math.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class NTP {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    record Section(String name, Consumer<String> run) {}

    private static final List<Section> SECTIONS = List.of(
        new Section("Prime Numbers", NTP::primeNumbers),
        new Section("Semiprimes", NTP::semiprimes),
        new Section("Twin Prime Pairs", NTP::twinPrimePairs),
        new Section("Prime Factorization", NTP::primeFactorization),
        new Section("Factors", NTP::factors),
        new Section("GCD and LCM", NTP::gcdAndLcm),
        new Section("Goldbach Conjecture", NTP::goldbachConjecture),
        new Section("Pythagorean Triples", NTP::pythagoreanTriples),
        new Section("Two Square Theorem", NTP::twoSquareTheorem),
        new Section("Fibonacci-Like Sequences", NTP::fibonacciLikeSequences),
        new Section("Ancient Egyptian Multiplication", NTP::ancientEgyptianMultiplication)
    );
    
    private static void println(Object x) { System.out.println(x); }

    private static void println() { System.out.println(); }

    public static void main(String[] args) {
        while (true) {
            println("\n=== Number Theory Playground ===");
            for (int i = 0; i < SECTIONS.size(); i++) {
                println(String.format("%d. %s", i + 1, SECTIONS.get(i).name()));
            }
            println("0. Exit");
            System.out.print("Select an option: ");

            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                println("Goodbye!");
                return;
            }
            int idx = -1;
            try {
                idx = Integer.parseInt(input) - 1;
            } catch (NumberFormatException ignored) {}
            if (idx >= 0 && idx < SECTIONS.size()) {
                Section s = SECTIONS.get(idx);
                s.run().accept(s.name());
            } else {
                println("Invalid option.");
            }
        }
    }
    
    private static String fmt(long n) { return String.format("%,d", n); }
    
    private static String fmt(BigInteger n) { return String.format("%,d", n); }

    private static void printSectionHeader(String name) {
        println(String.format("\n=== %s ===", name));
    }
    
    // Prompts for an integer within [min, max]. Accepts 'random' or 'back'. Returns null on 'back'.
    private static Long readInput(String prompt, long min, long max) {
        while (true) {
            System.out.printf("\n%s (%s–%s), 'random', or 'back': ", prompt, fmt(min), fmt(max));
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("back")) return null;
            if (input.equalsIgnoreCase("random")) {
                long n = randomInput(min, max);
                println("Random number: " + fmt(n));
                return n;
            }
            try {
                long n = Long.parseLong(input);
                if (n < min || n > max) {
                    println(String.format("Number must be between %s and %s.", fmt(min), fmt(max)));
                    continue;
                }
                return n;
            } catch (NumberFormatException e) {
                println("Invalid input — enter a whole number.");
            }
        }
    }

    // action: called with the validated input number to produce the section's output
    private static void runSection(String name, String description, long min, long max, LongConsumer action) {
        printSectionHeader(name);
        println(description);
        while (true) {
            Long n = readInput("Enter a number", min, max);
            if (n == null) return;
            println();
            action.accept(n);
        }
    }

    private static void runSectionTwoInputs(String name, String description, long min, long max, BiConsumer<Long, Long> action) {
        printSectionHeader(name);
        println(description);
        while (true) {
            Long a = readInput("Enter first number", min, max);
            if (a == null) return;
            Long b = readInput("Enter second number", min, max);
            if (b == null) return;
            println();
            action.accept(a, b);
        }
    }

    // Picks a random digit count from 1 to the digit count of max, then a random number with that many digits
    // (capped at max so the top-digit-count bucket stays within range). Retries if the value falls below min.
    private static long randomInput(long min, long max) {
        int maxDigits = String.valueOf(max).length();
        while (true) {
            int digits = 1 + random.nextInt(maxDigits);
            long origin = digits == 1 ? 0 : pow10(digits - 1);
            long bound = digits == maxDigits ? max + 1 : pow10(digits);
            long value = random.nextLong(origin, bound);
            if (value >= min) return value;
        }
    }

    private static long pow10(int n) {
        long result = 1;
        for (int i = 0; i < n; i++) result *= 10;
        return result;
    }

    static String wrapped(Stream<String> pieces) {
        StringBuilder result = new StringBuilder();
        StringBuilder line = new StringBuilder();
        pieces.forEach(piece -> {
            String entry = (line.isEmpty() ? "" : "    ") + piece;
            if (!line.isEmpty() && line.length() + entry.length() > 75) {
                result.append(line).append('\n');
                line.delete(0, line.length());
                line.append(piece);
            } else {
                line.append(entry);
            }
        });
        if (!line.isEmpty()) result.append(line);
        return result.toString();
    }

    // --- Prime Numbers section ---

    private static void primeNumbers(String name) {
        runSection(
            name,
            "Finds the first 30 primes >= a given number.",
            0,
            10_000_000_000_000L,
            n -> {
                println(String.format("First 30 primes >= %s:", fmt(n)));
                println(wrapped(findPrimesFrom(n).mapToObj(NTP::fmt)));
            }
        );
    }
    
    static LongStream findPrimesFrom(long start) {
        LongStream evens = start <= 2 ? LongStream.of(2) : LongStream.empty();
        long firstOdd = start <= 3 ? 3 : (start % 2 == 0 ? start + 1 : start);
        LongStream odds = LongStream.iterate(firstOdd, n -> n + 2);
        return
            LongStream.concat(evens, odds)
            .filter(NTP::isPrime)
            .limit(30);
    }

    // --- Semiprimes section ---

    record SemiprimeData(long semiprime, long factor1, long factor2) {
        @Override
        public String toString() {
            return String.format("%s = %s × %s", fmt(semiprime), fmt(factor1), fmt(factor2));
        }
    }

    private static void semiprimes(String name) {
        runSection(
            name,
            "Finds the first 20 semiprimes >= a given number.",
            0,
            50_000_000_000_000L,
            n -> {
                println(String.format("First 20 semiprimes >= %s:", fmt(n)));
                println(wrapped(findSemiprimesFrom(n).map(Object::toString)));
            }
        );
    }

    static Stream<SemiprimeData> findSemiprimesFrom(long start) {
        return
            LongStream.iterate(Math.max(start, 4), n -> n + 1)
            .mapToObj(NTP::semiprimeFactors)
            .filter(Objects::nonNull)
            .limit(20);
    }

    private static SemiprimeData semiprimeFactors(long n) {
        for (long p = 2; p * p <= n; p++) {
            if (n % p == 0) {
                return isPrime(n / p) ? new SemiprimeData(n, p, n / p) : null;
            }
        }
        return null;
    }

    // --- Twin Prime Pairs section ---

    private static void twinPrimePairs(String name) {
        runSection(
            name,
            "Finds the first 20 twin prime pairs (p, p+2) with p >= a given number.",
            0,
            500_000_000_000L,
            n -> {
                println(String.format("First 20 twin prime pairs with p >= %s:", fmt(n)));
                println(wrapped(findTwinPrimesFrom(n).mapToObj(p -> String.format("(%s, %s)", fmt(p), fmt(p + 2)))));
            }
        );
    }

    static LongStream findTwinPrimesFrom(long start) {
        LongStream special = start <= 3 ? LongStream.of(3) : LongStream.empty();
        long first = Math.max(start, 5);
        while (first % 6 != 5) first++;
        return
            LongStream.concat(special, LongStream.iterate(first, n -> n + 6))
            .filter(n -> isPrime(n) && isPrime(n + 2))
            .limit(20);
    }

    // --- Prime Factorization section ---

    private static void primeFactorization(String name) {
        runSection(
            name,
            "Shows the prime factorization of a given number.",
            2,
            10_000_000_000_000_000L,
            n -> println(String.format("%s = %s", fmt(n), primeFactorizationOf(n)))
        );
    }

    static String primeFactorizationOf(long n) {
        return formatPrimeFactors(primeFactorsOf(n));
    }

    // Returns [prime, exponent] pairs in ascending prime order.
    static List<long[]> primeFactorsOf(long n) {
        List<long[]> result = new ArrayList<>();
        List<FactorAndPower> result = new ArrayList<>();
        long remaining = n;
        
        int count2 = 0;
        while (remaining % 2 == 0) {
            remaining /= 2;
            count2++;
        }
        if (count2 > 0) result.add(new FactorAndPower(2, count2));
        
        for (long p = 3; p * p <= remaining; p += 2) {
            if (remaining % p == 0) {
                int count = 0;
                while (remaining % p == 0) {
                    remaining /= p;
                    count++;
                }
                result.add(new FactorAndPower(p, count));
            }
        }
        
        if (remaining > 1) result.add(new FactorAndPower(remaining, 1));
        return result;
    }

    private static String formatPrimeFactors(List<long[]> primeFactors) {
        if (primeFactors.isEmpty()) return "1";
        StringBuilder result = new StringBuilder();
        for (long[] pf : primeFactors) {
            if (!result.isEmpty()) result.append(" × ");
            result.append(fmt(pf[0]));
            if (pf[1] > 1) result.append("^").append(pf[1]);
        }
        return result.toString();
    }

    // --- Factors section ---

    record Factor(long value, String factorization) {}

    private static void factors(String name) {
        runSection(
            name,
            "Shows each factor of the input number with its prime factorization.",
            2,
            10_000_000_000_000_000L,
            n -> {
                List<FactorAndPower> pfs = primeFactorsAndPowersOf(n);
                println(String.format("%s = %s", fmt(n), formatPrimeFactors(pfs)));
                println(String.format("\nFactors of %s:", fmt(n)));
                println(wrapped(factorsFrom(pfs).stream().map(f -> String.format("%s = %s", fmt(f.value()), f.factorization()))));
            }
        );
    }

    static List<Factor> factorsFrom(List<long[]> primeFactors) {
        List<Factor> result = new ArrayList<>();
        result.add(new Factor(1, "1"));
        
        for (FactorAndPower fp : primeFactors) {
            long factor = fp.factor();
            int power = fp.power();
            int startSize = result.size();
            long pe = 1;
            
            for (int k = 1; k <= power; k++) {
                pe *= factor;
                String pePart = fmt(factor) + (k > 1 ? "^" + k : "");
                for (int j = 0; j < startSize; j++) {
                    Factor existing = result.get(j);
                    long value = existing.value() * pe;
                    String factorization = existing.value() == 1
                            ? pePart
                            : existing.factorization() + " × " + pePart;
                    result.add(new Factor(value, factorization));
                }
            }
        }
        
        result.sort(Comparator.comparingLong(Factor::value));
        return result;
    }

    // --- GCD and LCM section ---

    record EuclideanStep(long max, long min, long remainder) {}

    private static void gcdAndLcm(String name) {
        runSectionTwoInputs(
            name,
            "Computes the GCD via the Euclidean algorithm, and GCD/LCM via prime factorizations.",
            2,
            5_000_000_000_000_000L,
            (a, b) -> {
                println("Euclidean algorithm:");
                printEuclideanTable(euclideanAlgorithm(a, b));

                List<FactorAndPower> aFactors = primeFactorsAndPowersOf(a);
                List<FactorAndPower> bFactors = primeFactorsAndPowersOf(b);
                println("\nPrime factorizations:");
                println(String.format("%s = %s", fmt(a), formatPrimeFactors(aFactors)));
                println(String.format("%s = %s", fmt(b), formatPrimeFactors(bFactors)));

                GcdLcm gcdLcm = gcdAndLcmFactors(aFactors, bFactors);
                println(String.format("\nGCD = %s = %s", formatPrimeFactors(gcdLcm.gcd()), fmt(product(gcdLcm.gcd()))));
                println(String.format("LCM = %s = %s", formatPrimeFactors(gcdLcm.lcm()), fmt(product(gcdLcm.lcm()))));
            }
        );
    }
    
    static List<EuclideanStep> euclideanAlgorithm(long a, long b) {
        List<EuclideanStep> steps = new ArrayList<>();
        long max = Math.max(a, b);
        long min = Math.min(a, b);
        while (min > 0) {
            long remainder = max % min;
            steps.add(new EuclideanStep(max, min, remainder));
            max = min;
            min = remainder;
        }
        return steps;
    }
    
    private static void printEuclideanTable(List<EuclideanStep> steps) {
        int maxW = "max".length();
        int minW = "min".length();
        int remW = "remainder".length();
        for (EuclideanStep step : steps) {
            maxW = Math.max(maxW, fmt(step.max()).length());
            minW = Math.max(minW, fmt(step.min()).length());
            remW = Math.max(remW, fmt(step.remainder()).length());
        }
        String rowFormat = "%" + maxW + "s  %" + minW + "s  %" + remW + "s";
        println(String.format(rowFormat, "max", "min", "remainder"));
        for (EuclideanStep step : steps) {
            println(String.format(rowFormat, fmt(step.max()), fmt(step.min()), fmt(step.remainder())));
        }
    }
    
    record GcdLcm(List<FactorAndPower> gcd, List<FactorAndPower> lcm) {}
    
    static GcdLcm gcdAndLcmFactors(List<FactorAndPower> a, List<FactorAndPower> b) {
        List<FactorAndPower> gcd = new ArrayList<>();
        List<FactorAndPower> lcm = new ArrayList<>();
        for (FactorAndPower pa : a) {
            findPrime(b, pa.factor()).ifPresentOrElse(
                match -> {
                    gcd.add(new FactorAndPower(pa.factor(), Math.min(pa.power(), match.power())));
                    lcm.add(new FactorAndPower(pa.factor(), Math.max(pa.power(), match.power())));
                },
                () -> lcm.add(pa)
            );
        }
        
        for (FactorAndPower pb : b) {
            if (findPrime(a, pb.factor()).isEmpty()) lcm.add(pb);
        }
        
        lcm.sort(Comparator.comparingLong(FactorAndPower::factor));
        return new GcdLcm(gcd, lcm);
    }
    
    private static Optional<FactorAndPower> findPrime(List<FactorAndPower> factors, long prime) {
        return factors.stream().filter(pf -> pf.factor() == prime).findFirst();
    }
    
    private static BigInteger product(List<FactorAndPower> factors) {
        BigInteger result = BigInteger.ONE;
        for (FactorAndPower pf : factors) {
            result = result.multiply(BigInteger.valueOf(pf.factor()).pow(pf.power()));
        }
        return result;
    }
    
    // --- Goldbach Conjecture section ---
    
    private static void goldbachConjecture(String name) {
        final long min = 4;
        final long max = 1_500_000;
        printSectionHeader(name);
        println("Finds all pairs of primes that sum to a given even number.");
        while (true) {
            Long n = readInput("Enter an even number", min, max);
            if (n == null) return;
            if (n % 2 != 0) {
                println("Number must be even.");
                continue;
            }
            List<Long> lowers = goldbachPairs(n);
            println(String.format("\nPrime pairs summing to %s:", fmt(n)));
            println(wrapped(lowers.stream().map(p -> String.format("(%s, %s)", fmt(p), fmt(n - p)))));
        }
    }

    static List<Long> goldbachPairs(long n) {
        List<Long> lowers = new ArrayList<>();
        for (long p = 2; p <= n / 2; p++) {
            if (isPrime(p) && isPrime(n - p)) lowers.add(p);
        }
        return lowers;
    }

    // --- Pythagorean Triples section ---
    
    private static void pythagoreanTriples(String name) {
        runSection(
            name,
            "Finds the first 10 Pythagorean triples with smallest side >= a given number.",
            0,
            10_000,
            n -> {
                List<PythagoreanTriple> triples = firstPythagoreanTriples(n);
                println(String.format("First 10 Pythagorean triples with smallest side >= %s:", fmt(n)));
                triples.forEach(NTP::println);
            }
        );
    }
    
    record PythagoreanTriple(long a, long b, long c) {
        @Override
        public String toString() {
            return String.format(
                "%s² + %s² = %s²    (%s + %s = %s)",
                fmt(a), fmt(b), fmt(c), fmt(a * a), fmt(b * b), fmt(c * c)
            );
        }
    }

    static List<PythagoreanTriple> firstPythagoreanTriples(long minA) {
        List<PythagoreanTriple> result = new ArrayList<>();
        long a = minA;
        // (c-b)(c+b) = a². Iterate d1 < a with d1*d2 = a², same parity. Descending d1 yields ascending b.
        while (true) {
            long aSq = a * a;
            for (long d1 = a - 1; d1 >= 1; d1--) {
                if (aSq % d1 != 0) continue;
                long d2 = aSq / d1;
                if ((d1 % 2) != (d2 % 2)) continue;
                long b = (d2 - d1) / 2;
                long c = (d1 + d2) / 2;
                if (b > a) {
                    result.add(new PythagoreanTriple(a, b, c));
                    if (result.size() == 10) return result;
                }
            }
            a++;
        }
    }

    // --- Two Square Theorem section ---

    record TwoSquareData(long prime, long a, long b) {
        @Override
        public String toString() {
            return String.format(
                "%s = %s² + %s²    (%s = %s + %s)",
                fmt(prime), fmt(a), fmt(b), fmt(prime), fmt(a * a), fmt(b * b)
            );
        }
    }

    private static void twoSquareTheorem(String name) {
        runSection(
            name,
            "Finds the first Pythagorean prime >= a given number, along with the numbers whose squares sum to it.",
            0,
            1_000_000_000_000_000L,
            n -> {
                TwoSquareData ts = firstPythagoreanPrimeFrom(n);
                println(String.format("First Pythagorean prime >= %s:", fmt(n)));
                println(ts);
            }
        );
    }

    static TwoSquareData firstPythagoreanPrimeFrom(long start) {
        // Pythagorean primes are primes ≡ 1 (mod 4). Smallest is 5.
        long candidate = Math.max(start, 5);
        while (candidate % 4 != 1) candidate++;
        while (!isPrime(candidate)) candidate += 4;
        for (long a = 1; 2 * a * a <= candidate; a++) {
            long bSq = candidate - a * a;
            long b = (long) Math.sqrt((double) bSq);
            if (b * b == bSq) return new TwoSquareData(candidate, a, b);
            if ((b + 1) * (b + 1) == bSq) return new TwoSquareData(candidate, a, b + 1);
        }
        throw new IllegalStateException(String.format("Prime %d is not a sum of two squares", candidate));
    }

    // --- Fibonacci-Like Sequences section ---

    private static void fibonacciLikeSequences(String name) {
        runSectionTwoInputs(
            name,
            "Builds a 20-term sequence whose first two terms are the inputs; each later term is the sum of the previous two. Shows ratios at positions 5/4, 10/9, 15/14, 20/19.",
            1,
            9_000_000_000_000_000_000L,
            (a, b) -> {
                List<BigInteger> sequence = fibonacciLikeSequence(a, b);
                println("Sequence:");
                for (int i = 0; i < sequence.size(); i++) {
                    println(String.format("%2d: %s", i + 1, fmt(sequence.get(i))));
                }

                println("\nRatios:");
                for (int i : new int[]{4, 9, 14, 19}) {
                    println(new Ratio(sequence.get(i), sequence.get(i - 1)));
                }
            }
        );
    }

    static List<BigInteger> fibonacciLikeSequence(long a, long b) {
        List<BigInteger> result = new ArrayList<>();
        result.add(BigInteger.valueOf(a));
        result.add(BigInteger.valueOf(b));
        for (int i = 2; i < 20; i++) {
            result.add(result.get(i - 1).add(result.get(i - 2)));
        }
        return result;
    }

    static class Ratio {
        private final BigInteger numerator;
        private final BigInteger denominator;
        private final BigDecimal value;
        private final boolean exact;

        Ratio(BigInteger numerator, BigInteger denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
            BigDecimal num = new BigDecimal(numerator);
            BigDecimal den = new BigDecimal(denominator);
            BigDecimal value;
            boolean exact;
            try {
                // divide() with no rounding throws if the quotient has a non-terminating decimal expansion.
                value = num.divide(den);
                exact = true;
            } catch (ArithmeticException e) {
                value = num.divide(den, MathContext.DECIMAL64);
                exact = false;
            }
            this.value = value;
            this.exact = exact;
        }

        public boolean exact() {
            return exact;
        }

        @Override
        public String toString() {
            return String.format(
                "%s / %s %c %s",
                fmt(numerator), fmt(denominator), exact ? '=' : '≈', value.toPlainString()
            );
        }
    }

    // --- Ancient Egyptian Multiplication section ---

    record PowerAndMultiple(long power, BigInteger multiple) {}

    private static void ancientEgyptianMultiplication(String name) {
        runSectionTwoInputs(
            name,
            "Multiplies two numbers via powers of 2: shows every power of 2 ≤ the first input paired with its product with the second input, then the subset of powers that sum to the first input.",
            2,
            9_000_000_000_000_000L,
            (a, b) -> {
                println(String.format("All powers of 2 ≤ %s:", fmt(a)));
                printEgyptianTable(powersOfTwoTable(a, b));
                println(String.format("\nPowers of 2 summing to %s:", fmt(a)));
                printEgyptianTable(powersOfTwoSumming(a, b));
            }
        );
    }

    static List<PowerAndMultiple> powersOfTwoTable(long a, long b) {
        List<PowerAndMultiple> result = new ArrayList<>();
        long power = 1;
        BigInteger multiple = BigInteger.valueOf(b);
        
        while (power <= a) {
            result.add(new PowerAndMultiple(power, multiple));
            if (power > a / 2) break;
            power *= 2;
            multiple = multiple.shiftLeft(1);
        }
        
        return result;
    }

    static List<PowerAndMultiple> powersOfTwoSumming(long a, long b) {
        List<PowerAndMultiple> result = new ArrayList<>();
        long power = 1;
        BigInteger multiple = BigInteger.valueOf(b);
        long remaining = a;
        
        while (remaining > 0) {
            if (remaining % 2 == 1) result.add(new PowerAndMultiple(power, multiple));
            remaining /= 2;
            power *= 2;
            multiple = multiple.shiftLeft(1);
        }
        
        return result;
    }

    private static void printEgyptianTable(List<PowerAndMultiple> rows) {
        int powerW = "power".length();
        int multW = "multiple".length();
        for (PowerAndMultiple row : rows) {
            powerW = Math.max(powerW, fmt(row.power()).length());
            multW = Math.max(multW, fmt(row.multiple()).length());
        }
        String rowFormat = "%" + powerW + "s  %" + multW + "s";
        println(String.format(rowFormat, "power", "multiple"));
        for (PowerAndMultiple row : rows) {
            println(String.format(rowFormat, fmt(row.power()), fmt(row.multiple())));
        }
    }
}
