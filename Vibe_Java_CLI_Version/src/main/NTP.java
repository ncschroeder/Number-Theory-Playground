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
        println(String.format("=== %s ===", name));
    }
    
    // Prompts for an integer within [min, max]. Accepts 'r' (random), 'i' (info), or 'm' for menu (returns null).
    private static Long readInput(String info, long min, long max) {
        return readInput("Enter a number", info, min, max, () -> randomInput(min, max));
    }

    private static Long readInput(String prompt, String info, long min, long max, LongSupplier randomGen) {
        while (true) {
            System.out.printf("%s (%s–%s), 'r' for random, 'i' for info, or 'm' for menu: ", prompt, formatWithCommas(min), formatWithCommas(max));
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("m")) return null;
            if (input.equalsIgnoreCase("i")) {
                println();
                println(wrapped(info));
                continue;
            }
            if (input.equalsIgnoreCase("r")) {
                long n = randomGen.getAsLong();
                println("Random number: " + formatWithCommas(n));
                return n;
            }
            try {
                long n = Long.parseLong(input);
                if (n < min || n > max) {
                    println(String.format("Number must be between %s and %s.", formatWithCommas(min), formatWithCommas(max)));
                    continue;
                }
                return n;
            } catch (NumberFormatException e) {
                println("Invalid input — enter a whole number.");
            }
        }
    }

    // action: called with the validated input number to produce the section's output
    private static void runSingleInputSection(String name, String description, String info, long min, long max, LongConsumer action) {
        println();
        printSectionHeader(name);
        println(description);
        while (true) {
            println();
            Long n = readInput(info, min, max);
            if (n == null) return;
            println();
            action.accept(n);
        }
    }

    private static void runDoubleInputSection(String name, String description, String info, long min, long max, BiConsumer<Long, Long> action) {
        println();
        printSectionHeader(name);
        println(description);
        while (true) {
            println();
            long[] inputs = readTwoInputs(info, min, max);
            if (inputs == null) return;
            println();
            action.accept(inputs[0], inputs[1]);
        }
    }

    // Prompts for two integers within [min, max] separated by whitespace.
    // Accepts 'r' (two random), 'i' (info), or 'm' for menu (returns null).
    private static long[] readTwoInputs(String info, long min, long max) {
        while (true) {
            System.out.printf(
                "Enter two numbers separated by whitespace (each %s–%s), 'r' for random, 'i' for info, or 'm' for menu: ",
                formatWithCommas(min), formatWithCommas(max)
            );
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("m")) return null;
            if (input.equalsIgnoreCase("i")) {
                println();
                println(wrapped(info));
                continue;
            }
            if (input.equalsIgnoreCase("r")) {
                long a = randomInput(min, max);
                long b = randomInput(min, max);
                println(String.format("Random numbers: %s, %s", formatWithCommas(a), formatWithCommas(b)));
                return new long[]{a, b};
            }
            String[] parts = input.split("\\s+");
            if (parts.length != 2) {
                println("Enter exactly two numbers separated by whitespace.");
                continue;
            }
            try {
                long a = Long.parseLong(parts[0]);
                long b = Long.parseLong(parts[1]);
                if (a < min || a > max || b < min || b > max) {
                    println(String.format("Each number must be between %s and %s.", formatWithCommas(min), formatWithCommas(max)));
                    continue;
                }
                return new long[]{a, b};
            } catch (NumberFormatException e) {
                println("Invalid input — enter two whole numbers.");
            }
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

    static String wrapped(String text) {
        var result = new StringBuilder();
        int lineLen = 0;
        
        for (String word : text.split(" ")) {
            if (lineLen == 0) {
                result.append(word);
                lineLen = word.length();
            } else if (lineLen + 1 + word.length() > 90) {
                result.append('\n').append(word);
                lineLen = word.length();
            } else {
                result.append(' ').append(word);
                lineLen += 1 + word.length();
            }
        }
        
        return result.toString();
    }

    static <T> String wrapped(List<T> items, Function<T, String> mapper) {
        var result = new StringBuilder();
        var line = new StringBuilder();
        
        for (T item : items) {
            String piece = mapper.apply(item);
            String entry = (line.isEmpty() ? "" : "    ") + piece;
            if (!line.isEmpty() && line.length() + entry.length() > 75) {
                result.append(line).append('\n');
                line.delete(0, line.length());
                line.append(piece);
            } else {
                line.append(entry);
            }
        }
        
        if (!line.isEmpty()) result.append(line);
        return result.toString();
    }
    
    private static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (long i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // --- Prime Numbers section ---

    private static void primeNumbers(String name) {
        runSingleInputSection(
            name,
            "Finds the first 30 primes >= a given number.",
            """
            A prime is an integer greater than 1 whose only positive divisors are 1 \
            and itself. The primes begin 2, 3, 5, 7, 11, 13, 17, ..., and 2 is the \
            only even prime. Euclid proved around 300 BC that there are infinitely \
            many primes. The Prime Number Theorem says primes near n are spaced \
            roughly ln(n) apart, so they thin out as numbers grow but never run dry.""",
            0,
            10_000_000_000_000L,
            n -> {
                println(String.format("First 30 primes >= %s:", formatWithCommas(n)));
                println(wrapped(findPrimesFrom(n), NTP::formatWithCommas));
            }
        );
    }
    
    static List<Long> findPrimesFrom(long start) {
        var result = new ArrayList<Long>();
        if (start <= 2) result.add(2L);
        long n = start <= 3 ? 3 : (start % 2 == 0 ? start + 1 : start);
        while (true) {
            if (isPrime(n)) {
                result.add(n);
                if (result.size() == 30) break;
            }
            n += 2;
        }
        return result;
    }

    // --- Semiprimes section ---

    record SemiprimeData(long semiprime, long factor1, long factor2) {
        @Override
        public String toString() {
            return String.format("%s = %s × %s", formatWithCommas(semiprime), formatWithCommas(factor1), formatWithCommas(factor2));
        }
    }

    private static void semiprimes(String name) {
        runSingleInputSection(
            name,
            "Finds the first 20 semiprimes >= a given number.",
            """
            A semiprime is a positive integer that is the product of exactly two \
            primes; the two primes may be equal, as in 4 = 2 × 2 or 9 = 3 × 3. The \
            first few semiprimes are 4, 6, 9, 10, 14, 15, 21, 22, 25, 26. Semiprimes \
            underpin RSA cryptography: factoring a large semiprime n = p × q into \
            its prime factors is believed to be computationally hard, and the \
            security of RSA rests on that hardness.""",
            0,
            50_000_000_000_000L,
            n -> {
                println(String.format("First 20 semiprimes >= %s:", formatWithCommas(n)));
                println(wrapped(findSemiprimesFrom(n), Object::toString));
            }
        );
    }

    static List<SemiprimeData> findSemiprimesFrom(long start) {
        var result = new ArrayList<SemiprimeData>();
        for (long n = Math.max(start, 4); ; n++) {
            SemiprimeData sd = semiprimeFactors(n);
            if (sd != null) {
                result.add(sd);
                if (result.size() == 20) break;
            }
        }
        return result;
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
        runSingleInputSection(
            name,
            "Finds the first 20 twin prime pairs (p, p+2) with p >= a given number.",
            """
            A twin prime pair is two primes that differ by 2, such as (3, 5), \
            (11, 13), or (29, 31). The Twin Prime Conjecture — that infinitely many \
            such pairs exist — is one of the oldest open problems in number theory. \
            In 2013, Yitang Zhang proved that infinitely many prime pairs differ by \
            at most 70 million; that bound has since been reduced to 246. Apart \
            from (3, 5), every twin prime pair has the form (6k − 1, 6k + 1).""",
            0,
            500_000_000_000L,
            n -> {
                println(String.format("First 20 twin prime pairs with p >= %s:", formatWithCommas(n)));
            }
        );
    }

    static List<Long> findTwinPrimesFrom(long start) {
        var result = new ArrayList<Long>();
        if (start <= 3) result.add(3L);
        long n = Math.max(start, 5);
        while (n % 6 != 5) n++;
        while (true) {
            if (isPrime(n) && isPrime(n + 2)) {
                result.add(n);
                if (result.size() == 20) break;
            }
            n += 6;
        }
        return result;
    }

    // --- Prime Factorization section ---

    record FactorAndPower(long factor, int power) {}

    private static void primeFactorization(String name) {
        runSingleInputSection(
            name,
            "Shows the prime factorization of a given number.",
            """
            The Fundamental Theorem of Arithmetic states that every integer greater \
            than 1 has a unique factorization into primes, up to the order of the \
            factors. For example, 60 = 2² × 3 × 5. This factorization is the \
            canonical fingerprint of an integer and underlies divisibility, GCD, \
            LCM, and functions like Euler's totient. No fast classical algorithm \
            is known for factoring large integers, which is why RSA-style \
            cryptography remains secure.""",
            2,
            10_000_000_000_000_000L,
            n -> println(String.format("%s = %s", formatWithCommas(n), fpsToString(primeFactorsAndPowersOf(n))))
        );
    }
    
    // Returns (factor, power) pairs in ascending prime order.
    static List<FactorAndPower> primeFactorsAndPowersOf(long n) {
        var result = new ArrayList<FactorAndPower>();
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

    private static String fpsToString(List<FactorAndPower> fps) {
        return
            fps.isEmpty()
            ? "1"
            :
                fps
                .stream()
                .map(fp -> fp.power() > 1 ? formatWithCommas(fp.factor()) + "^" + fp.power() : formatWithCommas(fp.factor()))
                .collect(Collectors.joining(" × "));
    }

    // --- Factors section ---
    
    private static void factors(String name) {
        runSingleInputSection(
            name,
            "Shows each factor of the input number with its prime factorization.",
            """
            A factor (or divisor) of n is a positive integer that divides n with \
            no remainder. The factors of n correspond to choices of exponents \
            0 ≤ eᵢ ≤ aᵢ in its prime factorization n = p₁^a₁ × ... × pₖ^aₖ, so \
            the count of factors is (a₁ + 1)(a₂ + 1)...(aₖ + 1). For example, \
            12 = 2² × 3 has (2+1)(1+1) = 6 factors: 1, 2, 3, 4, 6, 12. Highly \
            composite numbers are integers with more divisors than any smaller \
            positive integer.""",
            2,
            10_000_000_000_000_000L,
            n -> {
                List<FactorAndPower> pfs = primeFactorsAndPowersOf(n);
                println(String.format("%s = %s", formatWithCommas(n), fpsToString(pfs)));
                println(String.format("\nFactors of %s:", formatWithCommas(n)));
            }
        );
    }
    
    record Factor(long value, String factorization) {}

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
        runDoubleInputSection(
            name,
            "Computes the GCD via the Euclidean algorithm, and GCD/LCM via prime factorizations.",
            """
            The greatest common divisor (GCD) of a and b is the largest integer \
            dividing both, and the least common multiple (LCM) is the smallest \
            positive integer divisible by both. Euclid's algorithm computes the \
            GCD by repeatedly replacing the larger value with its remainder \
            modulo the smaller, terminating when the remainder is 0. From the \
            prime factorizations, the GCD takes the minimum of each shared \
            prime's exponent and the LCM takes the maximum. They satisfy the \
            identity GCD(a, b) × LCM(a, b) = a × b.""",
            2,
            5_000_000_000_000_000L,
            (a, b) -> {
                println("Euclidean algorithm:");
                printEuclideanTable(euclideanAlgorithm(a, b));

                List<FactorAndPower> aFps = primeFactorsAndPowersOf(a);
                List<FactorAndPower> bFps = primeFactorsAndPowersOf(b);
                println("\nPrime factorizations:");
                println(String.format("%s = %s", formatWithCommas(a), fpsToString(aFps)));
                println(String.format("%s = %s", formatWithCommas(b), fpsToString(bFps)));

                GcdLcm gcdLcm = gcdAndLcmFactors(aFactors, bFactors);
                println(String.format("\nGCD = %s = %s", formatPrimeFactors(gcdLcm.gcd()), fmt(product(gcdLcm.gcd()))));
                println(String.format("LCM = %s = %s", formatPrimeFactors(gcdLcm.lcm()), fmt(product(gcdLcm.lcm()))));
            }
        );
    }
    
    static List<EuclideanStep> euclideanAlgorithm(long a, long b) {
        var steps = new ArrayList<EuclideanStep>();
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
            maxW = Math.max(maxW, formatWithCommas(step.max()).length());
            minW = Math.max(minW, formatWithCommas(step.min()).length());
            remW = Math.max(remW, formatWithCommas(step.remainder()).length());
        }
        
        String rowFormat = "%" + maxW + "s  %" + minW + "s  %" + remW + "s";
        println(String.format(rowFormat, "max", "min", "remainder"));
        
        for (EuclideanStep step : steps) {
            println(String.format(rowFormat, formatWithCommas(step.max()), formatWithCommas(step.min()), formatWithCommas(step.remainder())));
        }
    }
    
    static class GcdAndLcmPrimeFactorizationData {
        private final List<FactorAndPower> gcdFps;
        private final List<FactorAndPower> lcmFps;

        GcdAndLcmPrimeFactorizationData(List<FactorAndPower> aFps, List<FactorAndPower> bFps) {
            var gcdFps = new ArrayList<FactorAndPower>();
            var lcmFps = new ArrayList<FactorAndPower>();
            
            for (FactorAndPower fp : aFps) {
                findFactor(bFps, fp.factor()).ifPresentOrElse(
                    match -> {
                        gcdFps.add(new FactorAndPower(fp.factor(), Math.min(fp.power(), match.power())));
                        lcmFps.add(new FactorAndPower(fp.factor(), Math.max(fp.power(), match.power())));
                    },
                    () -> lcmFps.add(fp)
                );
            }
            
            for (FactorAndPower fp : bFps) {
                if (findFactor(aFps, fp.factor()).isEmpty()) lcmFps.add(fp);
            }
            
            lcmFps.sort(Comparator.comparingLong(FactorAndPower::factor));
            
            this.gcdFps = List.copyOf(gcdFps);
            this.lcmFps = List.copyOf(lcmFps);
        }

        List<FactorAndPower> gcdFps() { return gcdFps; }
        List<FactorAndPower> lcmFps() { return lcmFps; }
    }
    
    private static Optional<FactorAndPower> findFactor(List<FactorAndPower> fps, long factor) {
        return fps.stream().filter(pf -> pf.factor() == factor).findFirst();
    }
    
    private static BigInteger product(List<FactorAndPower> fps) {
        BigInteger result = BigInteger.ONE;
        for (FactorAndPower fp : fps) {
            result = result.multiply(BigInteger.valueOf(fp.factor()).pow(fp.power()));
        }
        return result;
    }
    
    // --- Goldbach Conjecture section ---
    
    private static void goldbachConjecture(String name) {
        final long min = 4;
        final long max = 1_500_000;
        final String info = """
            Christian Goldbach conjectured in 1742 that every even integer greater \
            than 2 is the sum of two primes; for example, 10 = 3 + 7 = 5 + 5. It \
            has been verified by computer for all even numbers up to roughly \
            4 × 10¹⁸ but remains unproven. The related "weak" Goldbach conjecture \
            — that every odd integer greater than 5 is a sum of three primes — \
            was proven by Harald Helfgott in 2013. The number of representations \
            tends to grow with n, making counterexamples increasingly unlikely \
            as n increases.""";
        
        println();
        printSectionHeader(name);
        println("Finds all pairs of primes that sum to a given even number.");
        while (true) {
            println();
            Long n = readInput("Enter an even number", info, min, max, () -> 2 * randomInput(min / 2, max / 2));
            if (n == null) return;
            if (n % 2 != 0) {
                println("Number must be even.");
                continue;
            }
            List<Long> lowers = goldbachPairs(n);
            println(wrapped(lowers.stream().map(p -> String.format("(%s, %s)", fmt(p), fmt(n - p)))));
            println(String.format("\nPrime pairs summing to %s:", formatWithCommas(n)));
        }
    }

    static List<Long> goldbachPairs(long n) {
        if (n == 4) return List.of(2L);
        var lowers = new ArrayList<Long>();
        for (long p = 3; p <= n / 2; p += 2) {
            if (isPrime(p) && isPrime(n - p)) lowers.add(p);
        }
        return lowers;
    }

    // --- Pythagorean Triples section ---
    
    private static void pythagoreanTriples(String name) {
        runSingleInputSection(
            name,
            "Finds the first 10 Pythagorean triples with smallest side >= a given number.",
            """
            A Pythagorean triple is three positive integers (a, b, c) satisfying \
            a² + b² = c², corresponding to a right triangle with integer side \
            lengths. The smallest is (3, 4, 5). Euclid's formula generates all \
            primitive triples — those with gcdFps(a, b, c) = 1 — as a = m² − n², \
            b = 2mn, c = m² + n² for coprime m > n of opposite parity. Every \
            Pythagorean triple is a positive integer multiple of a primitive \
            one, and there are infinitely many.""",
            0,
            10_000,
            n -> {
                List<PythagoreanTriple> triples = firstPythagoreanTriples(n);
                println(String.format("First 10 Pythagorean triples with smallest side >= %s:", formatWithCommas(n)));
                triples.forEach(NTP::println);
            }
        );
    }
    
    record PythagoreanTriple(long a, long b, long c) {
        boolean isPrimitive() {
            long max = b;
            long min = a;
            long remainder = max % min;
            
            while (remainder != 0) {
                max = min;
                min = remainder;
                remainder = max % min;
            }
            
            // At this point, min is the GCD.
            return min == 1;
        }

        @Override
        public String toString() {
            return String.format(
                "%s² + %s² = %s²    (%s + %s = %s)%s",
                formatWithCommas(a), formatWithCommas(b), formatWithCommas(c),
                formatWithCommas(a * a), formatWithCommas(b * b), formatWithCommas(c * c),
                isPrimitive() ? "  primitive" : ""
            );
        }
    }

    static List<PythagoreanTriple> firstPythagoreanTriples(long minA) {
        var result = new ArrayList<PythagoreanTriple>();
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

    static class TwoSquareData {
        final long prime, a, b;

        TwoSquareData(long start) {
            long candidate = Math.max(start, 5);
            while (candidate % 4 != 1) candidate++;
            while (!isPrime(candidate)) candidate += 4;
            long foundA = 0, foundB = 0;
            for (long i = 1; 2 * i * i <= candidate; i++) {
                long bSq = candidate - i * i;
                long j = (long) Math.sqrt((double) bSq);
                if (j * j == bSq) { foundA = i; foundB = j; break; }
                if ((j + 1) * (j + 1) == bSq) { foundA = i; foundB = j + 1; break; }
            }
            if (foundA == 0)
                throw new IllegalStateException(String.format("Prime %d is not a sum of two squares", candidate));
            prime = candidate;
            a = foundA;
            b = foundB;
        }

        @Override
        public String toString() {
            return String.format(
                "%s = %s² + %s²    (%s = %s + %s)",
                formatWithCommas(prime), formatWithCommas(a), formatWithCommas(b), formatWithCommas(prime), formatWithCommas(a * a), formatWithCommas(b * b)
            );
        }
    }

    private static void twoSquareTheorem(String name) {
        runSingleInputSection(
            name,
            "Finds the first Pythagorean prime >= a given number, along with the numbers whose squares sum to it.",
            """
            Fermat's Theorem on Sums of Two Squares states that an odd prime p \
            can be written as a² + b² if and only if p ≡ 1 (mod 4). Such primes \
            are called Pythagorean primes; the first few are 5, 13, 17, 29, 37, \
            41. When the representation exists, it is unique up to the order \
            and signs of a and b. The theorem connects to the Gaussian integers \
            ℤ[i], in which these primes split as p = (a + bi)(a − bi).""",
            0,
            1_000_000_000_000_000L,
            n -> {
                var ts = new TwoSquareData(n);
                println(String.format("First Pythagorean prime >= %s:", formatWithCommas(n)));
                println(ts);
            }
        );
    }


    // --- Fibonacci-Like Sequences section ---

    private static void fibonacciLikeSequences(String name) {
        runDoubleInputSection(
            name,
            "Builds a 20-term sequence whose first two terms are the inputs; each later term is the sum of the previous two. Shows ratios at positions 5/4, 10/9, 15/14, 20/19.",
            """
            A Fibonacci-like sequence starts with two seed values; each subsequent \
            term is the sum of the previous two: a, b, a+b, a+2b, 2a+3b, .... The \
            classical Fibonacci sequence uses seeds 1, 1, and the Lucas sequence \
            uses 2, 1. For any starting pair (with at least one positive value), \
            the ratio of consecutive terms converges to the golden ratio \
            φ = (1 + √5) / 2 ≈ 1.618. This convergence reflects the fact that \
            the dominant eigenvalue of the recurrence's 2×2 transition matrix is φ.""",
            1,
            9_000_000_000_000_000_000L,
            (a, b) -> {
                List<BigInteger> sequence = fibonacciLikeSequence(a, b);
                println("Sequence:");
                println(wrapped(sequence, NTP::formatWithCommas));

                println("\nRatios:");
                for (int i : new int[]{4, 9, 14, 19}) {
                    println(new Ratio(sequence.get(i), sequence.get(i - 1)));
                }
            }
        );
    }

    static List<BigInteger> fibonacciLikeSequence(long a, long b) {
        var result = new ArrayList<BigInteger>();
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
            var num = new BigDecimal(numerator);
            var den = new BigDecimal(denominator);
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
                formatWithCommas(numerator), formatWithCommas(denominator), exact ? '=' : '≈', value.toPlainString()
            );
        }
    }

    // --- Ancient Egyptian Multiplication section ---

    record PowerAndMultiple(long power, BigInteger multiple) {}

    private static void ancientEgyptianMultiplication(String name) {
        runDoubleInputSection(
            name,
            "Multiplies two numbers via powers of 2: shows every power of 2 ≤ the first input paired with its product with the second input, then the subset of powers that sum to the first input.",
            """
            Ancient Egyptian (or "peasant") multiplication computes a × b using \
            only doubling and addition: write a in binary as a sum of distinct \
            powers of 2, then add the corresponding doublings of b. For example, \
            13 × 12 = 8 × 12 + 4 × 12 + 1 × 12 = 96 + 48 + 12 = 156. The method \
            appears in the Rhind Mathematical Papyrus from around 1650 BC. It is \
            essentially the same algorithm as modern shift-and-add binary \
            multiplication.""",
            2,
            9_000_000_000_000_000L,
            (a, b) -> {
                println(String.format("All powers of 2 ≤ %s:", formatWithCommas(a)));
                printEgyptianTable(powersOfTwoTable(a, b));
                println(String.format("\nPowers of 2 summing to %s:", formatWithCommas(a)));
                printEgyptianTable(powersOfTwoSumming(a, b));
            }
        );
    }

    static List<PowerAndMultiple> powersOfTwoTable(long a, long b) {
        var result = new ArrayList<PowerAndMultiple>();
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
        var result = new ArrayList<PowerAndMultiple>();
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
            powerW = Math.max(powerW, formatWithCommas(row.power()).length());
            multW = Math.max(multW, formatWithCommas(row.multiple()).length());
        }
        String rowFormat = "%" + powerW + "s  %" + multW + "s";
        println(String.format(rowFormat, "power", "multiple"));
        for (PowerAndMultiple row : rows) {
            println(String.format(rowFormat, formatWithCommas(row.power()), formatWithCommas(row.multiple())));
        }
    }
}
