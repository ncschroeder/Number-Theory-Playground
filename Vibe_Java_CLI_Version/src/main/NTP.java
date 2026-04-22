import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.LongConsumer;
import java.util.stream.Stream;

public class NTP {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static void main(String[] args) {
        while (true) {
            println("\n=== Number Theory Playground ===");
            println("1. Prime Numbers");
            println("2. Semiprimes");
            println("3. Twin Prime Pairs");
            println("4. Prime Factorization");
            println("5. Factors");
            println("6. GCD and LCM");
            println("0. Exit");
            System.out.print("Select an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> primeNumbers();
                case "2" -> semiprimes();
                case "3" -> twinPrimePairs();
                case "4" -> primeFactorization();
                case "5" -> factors();
                case "6" -> gcdAndLcm();
                case "0" -> { println("Goodbye!"); return; }
                default  -> println("Invalid option.");
            }
        }
    }

    // action: called with the validated input number to produce the section's output
    private static void runSection(String title, String description, long min, long max, LongConsumer action) {
        println("\n" + title);
        println(description);
        while (true) {
            Long n = readInput("Enter a number", min, max);
            if (n == null) return;
            action.accept(n);
        }
    }

    // Prompts for an integer within [min, max]. Accepts 'random' or 'back'. Returns null on 'back'.
    private static Long readInput(String prompt, long min, long max) {
        while (true) {
            System.out.print("\n" + prompt + " (" + fmt(min) + "–" + fmt(max) + "), 'random', or 'back': ");
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
                    println("Number must be between " + fmt(min) + " and " + fmt(max) + ".");
                    continue;
                }
                return n;
            } catch (NumberFormatException e) {
                println("Invalid input — enter a whole number.");
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

    private static void primeNumbers() {
        runSection("=== Prime Numbers ===", "Finds the first 30 primes >= a given number.", 0, 10_000_000_000_000L, n -> {
            List<Long> primes = findPrimesFrom(n);
            println("\nFirst 30 primes >= " + fmt(n) + ":");
            println(wrapped(primes.stream().map(NTP::fmt)));
        });
    }

    // --- Semiprimes section ---

    private static void semiprimes() {
        runSection("=== Semiprimes ===", "Finds the first 20 semiprimes >= a given number.", 0, 50_000_000_000_000L, n -> {
            List<long[]> results = findSemiprimesFrom(n);
            println("\nFirst 20 semiprimes >= " + fmt(n) + ":");
            println(wrapped(results.stream().map(s -> fmt(s[0]) + " = " + fmt(s[1]) + " × " + fmt(s[2]))));
        });
    }

    private static List<long[]> findSemiprimesFrom(long start) {
        List<long[]> result = new ArrayList<>();
        long candidate = Math.max(start, 4);
        while (result.size() < 20) {
            long[] factors = semiprimeFactors(candidate);
            if (factors != null) result.add(new long[]{candidate, factors[0], factors[1]});
            candidate++;
        }
        return result;
    }

    private static long[] semiprimeFactors(long n) {
        for (long p = 2; p * p <= n; p++) {
            if (n % p == 0) {
                return isPrime(n / p) ? new long[]{p, n / p} : null;
            }
        }
        return null;
    }

    // --- Twin Prime Pairs section ---

    private static void twinPrimePairs() {
        runSection("=== Twin Prime Pairs ===",
                   "Finds the first 20 twin prime pairs (p, p+2) with p >= a given number.",
                   0, 500_000_000_000L, n -> {
            List<Long> lowers = findTwinPrimesFrom(n);
            println("\nFirst 20 twin prime pairs with p >= " + fmt(n) + ":");
            println(wrapped(lowers.stream().map(p -> "(" + fmt(p) + ", " + fmt(p + 2) + ")")));
        });
    }

    static List<Long> findTwinPrimesFrom(long start) {
        List<Long> result = new ArrayList<>();
        // (3, 5) is the only twin prime pair whose lower isn't of the form 6k-1.
        if (start <= 3) result.add(3L);
        long candidate = Math.max(start, 5);
        while (candidate % 6 != 5) candidate++;
        while (result.size() < 20) {
            if (isPrime(candidate) && isPrime(candidate + 2)) {
                result.add(candidate);
            }
            candidate += 6;
        }
        return result;
    }

    // --- Prime Factorization section ---

    private static void primeFactorization() {
        runSection("=== Prime Factorization ===",
                   "Shows the prime factorization of a given number.",
                   2, 10_000_000_000_000_000L, n -> println("\n" + fmt(n) + " = " + primeFactorizationOf(n)));
    }

    static String primeFactorizationOf(long n) {
        return formatPrimeFactors(primeFactorsOf(n));
    }

    // Returns [prime, exponent] pairs in ascending prime order.
    static List<long[]> primeFactorsOf(long n) {
        List<long[]> result = new ArrayList<>();
        long remaining = n;
        int count2 = 0;
        while (remaining % 2 == 0) {
            remaining /= 2;
            count2++;
        }
        if (count2 > 0) result.add(new long[]{2, count2});
        for (long p = 3; p * p <= remaining; p += 2) {
            if (remaining % p == 0) {
                int count = 0;
                while (remaining % p == 0) {
                    remaining /= p;
                    count++;
                }
                result.add(new long[]{p, count});
            }
        }
        if (remaining > 1) result.add(new long[]{remaining, 1});
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

    private static void factors() {
        runSection("=== Factors ===",
                   "Shows each factor of the input number with its prime factorization.",
                   2, 10_000_000_000_000_000L, n -> {
            List<long[]> pfs = primeFactorsOf(n);
            println("\n" + fmt(n) + " = " + formatPrimeFactors(pfs));
            println("\nFactors of " + fmt(n) + ":");
            println(wrapped(factorsFrom(pfs).stream().map(f -> fmt(f.value()) + " = " + f.factorization())));
        });
    }

    static List<Factor> factorsFrom(List<long[]> primeFactors) {
        List<Factor> result = new ArrayList<>();
        result.add(new Factor(1L, "1"));
        for (long[] pf : primeFactors) {
            long p = pf[0];
            long e = pf[1];
            int startSize = result.size();
            long pe = 1;
            for (int k = 1; k <= e; k++) {
                pe *= p;
                String pePart = fmt(p) + (k > 1 ? "^" + k : "");
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

    private static void gcdAndLcm() {
        final long min = 2;
        final long max = 5_000_000_000_000_000L;
        println("\n=== GCD and LCM ===");
        println("Computes the GCD via the Euclidean algorithm, and GCD/LCM via prime factorizations.");
        while (true) {
            Long a = readInput("Enter first number", min, max);
            if (a == null) return;
            Long b = readInput("Enter second number", min, max);
            if (b == null) return;

            println("\nEuclidean algorithm:");
            printEuclideanTable(euclideanAlgorithm(a, b));

            List<long[]> aFactors = primeFactorsOf(a);
            List<long[]> bFactors = primeFactorsOf(b);
            println("\nPrime factorizations:");
            println(fmt(a) + " = " + formatPrimeFactors(aFactors));
            println(fmt(b) + " = " + formatPrimeFactors(bFactors));

            List<long[]> gcdFs = gcdFactors(aFactors, bFactors);
            List<long[]> lcmFs = lcmFactors(aFactors, bFactors);
            println("\nGCD = " + formatPrimeFactors(gcdFs) + " = " + fmt(product(gcdFs)));
            println("LCM = " + formatPrimeFactors(lcmFs) + " = " + fmt(product(lcmFs)));
        }
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

    // Intersects two ascending-prime factorizations, taking the min exponent per shared prime.
    static List<long[]> gcdFactors(List<long[]> a, List<long[]> b) {
        List<long[]> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < a.size() && j < b.size()) {
            long[] pa = a.get(i);
            long[] pb = b.get(j);
            if (pa[0] == pb[0]) {
                result.add(new long[]{pa[0], Math.min(pa[1], pb[1])});
                i++;
                j++;
            } else if (pa[0] < pb[0]) {
                i++;
            } else {
                j++;
            }
        }
        return result;
    }

    // Unions two ascending-prime factorizations, taking the max exponent per prime.
    static List<long[]> lcmFactors(List<long[]> a, List<long[]> b) {
        List<long[]> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < a.size() || j < b.size()) {
            if (i == a.size()) {
                result.add(new long[]{b.get(j)[0], b.get(j)[1]});
                j++;
            } else if (j == b.size()) {
                result.add(new long[]{a.get(i)[0], a.get(i)[1]});
                i++;
            } else {
                long[] pa = a.get(i);
                long[] pb = b.get(j);
                if (pa[0] == pb[0]) {
                    result.add(new long[]{pa[0], Math.max(pa[1], pb[1])});
                    i++;
                    j++;
                } else if (pa[0] < pb[0]) {
                    result.add(new long[]{pa[0], pa[1]});
                    i++;
                } else {
                    result.add(new long[]{pb[0], pb[1]});
                    j++;
                }
            }
        }
        return result;
    }

    private static long product(List<long[]> factors) {
        long result = 1;
        for (long[] pf : factors) {
            for (long i = 0; i < pf[1]; i++) result *= pf[0];
        }
        return result;
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

    static List<Long> findPrimesFrom(long start) {
        List<Long> primes = new ArrayList<>();
        long candidate = Math.max(start, 2);
        while (primes.size() < 30) {
            if (isPrime(candidate)) primes.add(candidate);
            candidate++;
        }
        return primes;
    }

    private static void println(Object x) { System.out.println(x); }

    private static String fmt(long n) { return String.format("%,d", n); }

    private static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (long i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
