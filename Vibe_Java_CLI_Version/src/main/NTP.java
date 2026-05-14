import java.math.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class NTP {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static final long FIVE_HUNDRED_BILLION   =     500_000_000_000L;
    private static final long TEN_TRILLION           =  10_000_000_000_000L;
    private static final long FIFTY_TRILLION         =  50_000_000_000_000L;
    private static final long ONE_QUADRILLION        =   1_000_000_000_000_000L;
    private static final long FIVE_QUADRILLION       =   5_000_000_000_000_000L;
    private static final long TEN_QUADRILLION        =  10_000_000_000_000_000L;
    private static final long NINE_QUINTILLION       = 9_000_000_000_000_000_000L;

    /**
     * A main-menu entry: {@code run} runs the section. {@code name} is held as a field so it
     * is defined once instead of being hardcoded in both the menu listing and in the method
     * that runs that section.
     */
    record Section(String name, Consumer<String> run) {}

    private static final List<Section> SECTIONS =
        List.of(
            new Section("Prime Numbers", NTP::primeNumbersSection),
            new Section("Semiprimes", NTP::semiprimesSection),
            new Section("Twin Prime Pairs", NTP::twinPrimePairsSection),
            new Section("Prime Factorization", NTP::primeFactorizationSection),
            new Section("Factors", NTP::factorsSection),
            new Section("GCD and LCM", NTP::gcdAndLcmSection),
            new Section("Goldbach Conjecture", NTP::goldbachConjectureSection),
            new Section("Pythagorean Triples", NTP::pythagoreanTriplesSection),
            new Section("Two Square Theorem", NTP::twoSquareTheoremSection),
            new Section("Fibonacci-Like Sequences", NTP::fibonacciLikeSequencesSection),
            new Section("Ancient Egyptian Multiplication", NTP::ancientEgyptianMultiplicationSection)
        );
    
    private static void println(Object o) { System.out.println(o); }

    private static void println() { System.out.println(); }
    
    private static void printf(String format, Object... args) {
        System.out.printf(format, args);
        println();
    }

    public static void main(String[] args) {
        while (true) {
            println("\n=== Number Theory Playground ===");
            for (int i = 0; i < SECTIONS.size(); i++) {
                printf("%d. %s", i + 1, SECTIONS.get(i).name());
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
    
    static String formatWithCommas(long n) { return String.format("%,d", n); }

    static String formatWithCommas(BigInteger n) { return String.format("%,d", n); }

    private static void printSectionHeader(String name) {
        printf("=== %s ===", name);
    }
    
    /*
    For runSingleInputSection and runDoubleInputSection, the action consumers are called with
    validated input number(s) to produce the section's output
     */
    
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
    
    private static Long readInput(String info, long min, long max) {
        return readInput("Enter a number", info, min, max, () -> randomInput(min, max));
    }
    
    /**
     * Prompts for an integer within [min, max].
     * Accepts 'r' (random, via {@code randomGen}), 'i' (info), or 'm' for menu (returns null).
     * {@code randomGen} is a parameter so callers with extra constraints can supply their own
     * generator — e.g. the Goldbach section needs the random value to be even.
     */
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
                    printf("Number must be between %s and %s.", formatWithCommas(min), formatWithCommas(max));
                    continue;
                }
                return n;
            } catch (NumberFormatException e) {
                println("Invalid input — enter a whole number.");
            }
        }
    }
    
    /**
     * Prompts for two integers within [min, max] separated by whitespace.
     * Accepts 'r' (two random), 'i' (info), or 'm' for menu (returns null).
     */
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
                printf("Random numbers: %s, %s", formatWithCommas(a), formatWithCommas(b));
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
                    printf("Each number must be between %s and %s.", formatWithCommas(min), formatWithCommas(max));
                    continue;
                }
                return new long[]{a, b};
            } catch (NumberFormatException e) {
                println("Invalid input — enter two whole numbers.");
            }
        }
    }
    
    /**
     * Picks a random digit count from 1 to the digit count of max, then a random number with that many digits
     * (capped at max so the top-digit-count bucket stays within range). Retries if the random number falls below min.
     */
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

    /**
     * Returns {@code text} re-flowed so that no line exceeds 90 characters, breaking only at
     * existing spaces.
     */
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

    /**
     * Returns {@code items}, each rendered via {@code mapper} and separated by four spaces, laid
     * out across lines so that no line exceeds 75 characters.
     */
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

    private static void primeNumbersSection(String name) {
        runSingleInputSection(
            name,
            "Finds the first 30 primes ≥ a given number.",
            """
            A prime is an integer greater than 1 whose only positive divisors are 1 \
            and itself. The primes begin 2, 3, 5, 7, 11, 13, 17, ..., and 2 is the \
            only even prime. Euclid proved around 300 BC that there are infinitely \
            many primes. The Prime Number Theorem says primes near n are spaced \
            roughly ln(n) apart, so they thin out as numbers grow but never run dry.""",
            0,
            TEN_TRILLION,
            n -> {
                printf("First 30 primes ≥ %s:", formatWithCommas(n));
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
    
    private static void semiprimesSection(String name) {
        runSingleInputSection(
            name,
            "Finds the first 20 semiprimes ≥ a given number.",
            """
            A semiprime is a positive integer that is the product of exactly two \
            primes; the two primes may be equal, as in 4 = 2 × 2 or 9 = 3 × 3. The \
            first few semiprimes are 4, 6, 9, 10, 14, 15, 21, 22, 25, 26. Semiprimes \
            underpin RSA cryptography: factoring a large semiprime n = p × q into \
            its prime factors is believed to be computationally hard, and the \
            security of RSA rests on that hardness.""",
            0,
            FIFTY_TRILLION,
            n -> {
                printf("First 20 semiprimes ≥ %s:", formatWithCommas(n));
                println(wrapped(findSemiprimesFrom(n), Object::toString));
            }
        );
    }
    
    record SemiprimeData(long semiprime, long factor1, long factor2) {
        @Override
        public String toString() {
            return String.format("%s = %s × %s", formatWithCommas(semiprime), formatWithCommas(factor1), formatWithCommas(factor2));
        }
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
        if (n % 2 == 0) {
            return isPrime(n / 2) ? new SemiprimeData(n, 2, n / 2) : null;
        }
        
        for (long p = 3; p * p <= n; p += 2) {
            if (n % p == 0) {
                return isPrime(n / p) ? new SemiprimeData(n, p, n / p) : null;
            }
        }
        
        return null;
    }

    // --- Twin Prime Pairs section ---

    private static void twinPrimePairsSection(String name) {
        runSingleInputSection(
            name,
            "Finds the first 20 twin prime pairs (p, p+2) with p ≥ a given number.",
            """
            A twin prime pair is two primes that differ by 2, such as (3, 5), \
            (11, 13), or (29, 31). The Twin Prime Conjecture — that infinitely many \
            such pairs exist — is one of the oldest open problems in number theory. \
            In 2013, Yitang Zhang proved that infinitely many prime pairs differ by \
            at most 70 million; that bound has since been reduced to 246. Apart \
            from (3, 5), every twin prime pair has the form (6k − 1, 6k + 1).""",
            0,
            FIVE_HUNDRED_BILLION,
            n -> {
                printf("First 20 twin prime pairs with p ≥ %s:", formatWithCommas(n));
                Function<Long, String> lowerToString =
                    l -> String.format("(%s, %s)", formatWithCommas(l), formatWithCommas(l + 2));
                println(wrapped(findTwinPrimePairLowers(n), lowerToString));
            }
        );
    }
    
    /**
     * Finds twin prime pairs and returns a list of the lower numbers in each pair.
     */
    static List<Long> findTwinPrimePairLowers(long start) {
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

    private static void primeFactorizationSection(String name) {
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
            TEN_QUADRILLION,
            n -> printf("%s = %s", formatWithCommas(n), fpsToString(primeFactorsAndPowersOf(n)))
        );
    }
    
    record FactorAndPower(long factor, int power) {}
    
    /**
     * Returns the prime factorization of {@code n} as (primeFactor, powerOf2) pairs, ordered by
     * ascending factors. For example, 60 yields (2, 2), (3, 1), (5, 1).
     */
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

    /**
     * Renders {@code fps} as a product expression like {@code 2^2 × 3 × 5}, omitting the
     * exponent where it is 1. An empty list renders as {@code "1"}.
     */
    static String fpsToString(List<FactorAndPower> fps) {
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
            "Tests divisibility by 3, 4, 6, 7, 8, 9, 11, and 12 using divisibility rules, " +
            "then shows each factor with its prime factorization.",
            """
            A factor (or divisor) of n is a positive integer that divides n with \
            no remainder. Divisibility rules give shortcuts for small divisors: \
            digit sum (3 and 9), last two digits (4), last three digits (8), \
            alternating digit sum (11), and the alternating sum of 3-digit \
            blocks (7). Divisibility by 6 follows from divisibility by 2 and 3, \
            and divisibility by 12 follows from divisibility by 3 and 4. The \
            complete factor list comes from the prime factorization \
            n = p₁^a₁ × ... × pₖ^aₖ — each factor corresponds to a choice of \
            exponents 0 ≤ eᵢ ≤ aᵢ, giving (a₁ + 1)(a₂ + 1)...(aₖ + 1) factors \
            total. For example, 12 = 2² × 3 has 6 factors: 1, 2, 3, 4, 6, 12.""",
            2,
            TEN_QUADRILLION,
            n -> {
                println("Divisibility:");
                
                SumAndExpression dse = getDigitSe(n);
                for (int d : new int[]{3, 4, 6, 7, 8, 9, 11, 12}) {
                    printf("  %s", checkDivisibility(n, d, dse));
                }
                println();

                List<FactorAndPower> pfs = primeFactorsAndPowersOf(n);
                printf("%s = %s", formatWithCommas(n), fpsToString(pfs));
                printf("\nFactors of %s:", formatWithCommas(n));
                println(wrapped(factorsFrom(pfs), Object::toString));
            }
        );
    }

    
    record DivisibilityResult(int divisor, boolean divisible, String explanation) {
        @Override
        public String toString() {
            return String.format("%2d: %s — %s", divisor, explanation, divisible ? "divisible" : "not divisible");
        }
    }
    
    /**
     * Instances of this are often shortened to "se".
     */
    record SumAndExpression(long sum, String expression) {}
    
    /**
     * Returns the sum of {@code n}'s digits, paired with a printable expression of that sum
     * such as {@code "1 + 2 + 3"}.
     */
    static SumAndExpression getDigitSe(long n) {
        String digits = String.valueOf(Math.abs(n));
        long sum = 0;
        var sb = new StringBuilder();
        
        for (int i = 0; i < digits.length(); i++) {
            int d = digits.charAt(i) - '0';
            sum += d;
            if (i > 0) sb.append(" + ");
            sb.append(d);
        }
        
        return new SumAndExpression(sum, sb.toString());
    }

    private static DivisibilityResult checkDivisibility(long n, int divisor, SumAndExpression digitSe) {
        return switch (divisor) {
            case 3  -> divisibilityBy3(digitSe);
            case 4  -> divisibilityBy4(n);
            case 6  -> divisibilityBy6(n, digitSe);
            case 7  -> divisibilityBy7(n);
            case 8  -> divisibilityBy8(n);
            case 9  -> divisibilityBy9(digitSe);
            case 11 -> divisibilityBy11(n);
            case 12 -> divisibilityBy12(n, digitSe);
            default -> throw new IllegalArgumentException("No divisibility rule for " + divisor);
        };
    }
    
    /**
     * n is divisible by 3 if the sum of its digits is.
     */
    private static DivisibilityResult divisibilityBy3(SumAndExpression digitSe) {
        return new DivisibilityResult(
            3,
            digitSe.sum() % 3 == 0,
            String.format("digit sum %s = %s", digitSe.expression(), formatWithCommas(digitSe.sum()))
        );
    }
    
    /**
     * n is divisible by 4 if the number formed from the last 2 digits is.
     */
    private static DivisibilityResult divisibilityBy4(long n) {
        long lastTwo = n % 100;
        return new DivisibilityResult(4, lastTwo % 4 == 0, String.format("last two digits %d", lastTwo));
    }
    
    /**
     * n is divisible by 6 if it's divisible by 2 and 3.
     */
    private static DivisibilityResult divisibilityBy6(long n, SumAndExpression digitSe) {
        boolean even = n % 2 == 0;
        boolean by3 = digitSe.sum() % 3 == 0;

        String explanation;
        if (!even) {
            explanation = String.format("%s is odd", formatWithCommas(n));
        } else if (by3) {
            explanation =
                String.format(
                    "%s is even and digit sum %s is a multiple of 3",
                    formatWithCommas(n), formatWithCommas(digitSe.sum())
                );
        } else {
            explanation =
                String.format(
                    "%s is even but digit sum %s is not a multiple of 3",
                    formatWithCommas(n), formatWithCommas(digitSe.sum())
                );
        }

        return new DivisibilityResult(6, even && by3, explanation);
    }

    /**
     * n is divisible by 7 exactly when the alternating block sum is.
     */
    private static DivisibilityResult divisibilityBy7(long n) {
        SumAndExpression bse = getAlternatingBlockSe(n);
        return new DivisibilityResult(
            7,
            bse.sum() % 7 == 0,
            String.format("alternating 3-digit block sum %s = %s", bse.expression(), formatWithCommas(bse.sum()))
        );
    }
    
    /**
     * Splits n into 3-digit groups from right to left and adds them with alternating signs
     * (first + , next − , next + , ...).
     */
    static SumAndExpression getAlternatingBlockSe(long n) {
        long remaining = Math.abs(n);
        long sum = 0;
        var sb = new StringBuilder();
        int i = 0;
        
        do {
            long block = remaining % 1_000;
            remaining /= 1_000;
            int sign = (i % 2 == 0) ? 1 : -1;
            sum += sign * block;
            if (i == 0) {
                sb.append(block);
            } else {
                sb.append(sign == 1 ? " + " : " − ").append(block);
            }
            i++;
        } while (remaining > 0);
        
        return new SumAndExpression(sum, sb.toString());
    }
    
    /**
     * n is divisible by 8 if the number formed from the last 3 digits is.
     */
    private static DivisibilityResult divisibilityBy8(long n) {
        long lastThree = n % 1_000;
        return new DivisibilityResult(8, lastThree % 8 == 0, String.format("last three digits %d", lastThree));
    }
    
    /**
     * n is divisible by 9 if the sum of its digits is.
     */
    private static DivisibilityResult divisibilityBy9(SumAndExpression digitSe) {
        return new DivisibilityResult(
            9,
            digitSe.sum() % 9 == 0,
            String.format("digit sum %s = %s", digitSe.expression(), formatWithCommas(digitSe.sum()))
        );
    }
    
    /**
     * n is divisible by 11 exactly when the alternating digit sum is.
     */
    private static DivisibilityResult divisibilityBy11(long n) {
        SumAndExpression ds = getAlternatingDigitSe(n);
        return new DivisibilityResult(
            11,
            ds.sum() % 11 == 0,
            String.format("alternating digit sum %s = %s", ds.expression(), formatWithCommas(ds.sum()))
        );
    }
    
    /**
     * Adds the digits of n with alternating signs, starting from the rightmost digit
     * (rightmost + , next − , next + , ...).
     */
    static SumAndExpression getAlternatingDigitSe(long n) {
        String digits = String.valueOf(Math.abs(n));
        long sum = 0;
        var sb = new StringBuilder();
        int len = digits.length();
        
        for (int i = 0; i < len; i++) {
            int d = digits.charAt(len - 1 - i) - '0';
            int sign = (i % 2 == 0) ? 1 : -1;
            sum += sign * d;
            if (i == 0) {
                sb.append(d);
            } else {
                sb.append(sign == 1 ? " + " : " − ").append(d);
            }
        }
        
        return new SumAndExpression(sum, sb.toString());
    }
    
    /**
     * n is divisible by 12 if it's divisible by 3 and 4.
     */
    private static DivisibilityResult divisibilityBy12(long n, SumAndExpression digitSe) {
        long lastTwo = n % 100;
        boolean by4 = lastTwo % 4 == 0;
        boolean by3 = digitSe.sum() % 3 == 0;

        String explanation;
        if (!by4) {
            explanation = String.format("last two digits %d is not a multiple of 4", lastTwo);
        } else if (by3) {
            explanation =
                String.format(
                    "last two digits %d is a multiple of 4 and digit sum %s is a multiple of 3",
                    lastTwo, formatWithCommas(digitSe.sum())
                );
        } else {
            explanation =
                String.format(
                    "last two digits %d is a multiple of 4 but digit sum %s is not a multiple of 3",
                    lastTwo, formatWithCommas(digitSe.sum())
                );
        }

        return new DivisibilityResult(12, by4 && by3, explanation);
    }
    
    
    record Factor(long value, String factorization) {
        @Override
        public String toString() {
            return String.format("%s = %s", formatWithCommas(value), factorization);
        }
    }
    
    /**
     * Returns every factor of the number whose prime factorization is {@code fps}, sorted
     * ascending, each paired with its own factorization. For 12 = 2² × 3 this gives
     * 1, 2, 3, 4, 6, 12.
     */
    static List<Factor> factorsFrom(List<FactorAndPower> fps) {
        var result = new ArrayList<Factor>();
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

    private static void gcdAndLcmSection(String name) {
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
            FIVE_QUADRILLION,
            (a, b) -> {
                println("Euclidean algorithm:");
                printEuclideanTable(euclideanAlgorithm(a, b));

                List<FactorAndPower> aFps = primeFactorsAndPowersOf(a);
                List<FactorAndPower> bFps = primeFactorsAndPowersOf(b);
                println("\nPrime factorizations:");
                printf("%s = %s", formatWithCommas(a), fpsToString(aFps));
                printf("%s = %s", formatWithCommas(b), fpsToString(bFps));

                var pfData = new GcdAndLcmPrimeFactorizationData(aFps, bFps);
                printf(
                    "\nGCD = %s = %s",
                    fpsToString(pfData.gcdFps()),
                    formatWithCommas(product(pfData.gcdFps()))
                );
                printf(
                    "LCM = %s = %s",
                    fpsToString(pfData.lcmFps()),
                    formatWithCommas(product(pfData.lcmFps()))
                );
            }
        );
    }
    
    record EuclideanStep(long max, long min, long remainder) {}
    
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
        printf(rowFormat, "max", "min", "remainder");
        
        for (EuclideanStep step : steps) {
            printf(rowFormat, formatWithCommas(step.max()), formatWithCommas(step.min()), formatWithCommas(step.remainder()));
        }
    }
    
    /**
     * Given the prime factorizations of two numbers, holds the prime factorizations of their
     * GCD and LCM: the GCD takes the smaller powerOf2 of each prime the two share, and the LCM
     * takes the larger powerOf2 of every prime that appears in either.
     */
    static class GcdAndLcmPrimeFactorizationData {
        private final List<FactorAndPower> gcdFps;
        private final List<FactorAndPower> lcmFps;
        
        List<FactorAndPower> gcdFps() { return gcdFps; }
        List<FactorAndPower> lcmFps() { return lcmFps; }

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
                if (findFactor(aFps, fp.factor()).isEmpty()) {
                    lcmFps.add(fp);
                }
            }
            
            lcmFps.sort(Comparator.comparingLong(FactorAndPower::factor));
            this.gcdFps = List.copyOf(gcdFps);
            this.lcmFps = List.copyOf(lcmFps);
        }
    }
    
    private static Optional<FactorAndPower> findFactor(List<FactorAndPower> fps, long factor) {
        return fps.stream().filter(pf -> pf.factor() == factor).findFirst();
    }
    
    /**
     * Multiplies the prime factorization {@code fps} back out into the number it represents.
     * The result is a BigInteger because an LCM of two large inputs can exceed the range of
     * a long.
     */
    private static BigInteger product(List<FactorAndPower> fps) {
        BigInteger result = BigInteger.ONE;
        for (FactorAndPower fp : fps) {
            result =
                result.multiply(
                    BigInteger.valueOf(fp.factor())
                    .pow(fp.power())
                );
        }
        return result;
    }
    
    // --- Goldbach Conjecture section ---
    
    private static void goldbachConjectureSection(String name) {
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
            List<Long> lowers = findGoldbachPairLowers(n);
            printf("\nPrime pairs summing to %s:", formatWithCommas(n));
            Function<Long, String> lowerToString =
                l -> String.format("(%s, %s)", formatWithCommas(l), formatWithCommas(n - l));
            println(wrapped(lowers, lowerToString));
        }
    }

    /**
     * n should be even. This method finds the pairs of prime numbers that sum to n and
     * returns a list of the lower numbers in each pair.
     */
    static List<Long> findGoldbachPairLowers(long n) {
        if (n == 4) return List.of(2L);
        var lowers = new ArrayList<Long>();
        for (long p = 3; p <= n / 2; p += 2) {
            if (isPrime(p) && isPrime(n - p)) {
                lowers.add(p);
            }
        }
        return lowers;
    }

    // --- Pythagorean Triples section ---
    
    private static void pythagoreanTriplesSection(String name) {
        runSingleInputSection(
            name,
            "Finds the first 10 Pythagorean triples with smallest side ≥ a given number.",
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
                printf("First 10 Pythagorean triples with smallest side ≥ %s:", formatWithCommas(n));
                triples.forEach(NTP::println);
            }
        );
    }
    
    record PythagoreanTriple(long a, long b, long c) {
        /*
        gcd(a, b) = 1 suffices; for a Pythagorean triple it implies gcd(a, b, c) = 1. Do the Euclidean algorithm
        on a and b. In production, the only way PythagoreanTriples are created is in firstPythagoreanTriples
        below and a and b are always going to be the short and long leg lengths, respectively. That's why max
        is initialized to b.
         */
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

    /**
     * Returns the first 10 Pythagorean triples (a, b, c) with a ≥ {@code minA} and a ≤ b, sorted by a then b.
     */
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
    
    private static void twoSquareTheoremSection(String name) {
        runSingleInputSection(
            name,
            "Finds the first Pythagorean prime ≥ a given number, along " +
            "with the numbers whose squares sum to it.",
            """
            Fermat's Theorem on Sums of Two Squares states that an odd prime p \
            can be written as a² + b² if and only if p ≡ 1 (mod 4). Such primes \
            are called Pythagorean primes; the first few are 5, 13, 17, 29, 37, \
            41. When the representation exists, it is unique up to the order \
            and signs of a and b. The theorem connects to the Gaussian integers \
            ℤ[i], in which these primes split as p = (a + bi)(a − bi).""",
            0,
            ONE_QUADRILLION,
            n -> {
                var ts = new TwoSquareData(n);
                printf("First Pythagorean prime ≥ %s:", formatWithCommas(n));
                println(ts);
            }
        );
    }
    
    /**
     * Given a starting value, finds the first Pythagorean prime at or above it (a prime that
     * is one more than a multiple of 4) and the two numbers {@code a} and {@code b} whose
     * squares sum to it.
     */
    static class TwoSquareData {
        final long pythagPrime, a, b;
        
        TwoSquareData(long start) {
            long candidate = Math.max(start, 5);
            while (candidate % 4 != 1) candidate++;
            while (!isPrime(candidate)) candidate += 4;
            long foundA = 0, foundB = 0;
            
            for (long l = 1; 2 * l * l <= candidate; l++) {
                long bSq = candidate - l * l;
                long j = (long) Math.sqrt((double) bSq);
                if (j * j == bSq) {
                    foundA = l;
                    foundB = j;
                    break;
                }
                if ((j + 1) * (j + 1) == bSq) {
                    foundA = l;
                    foundB = j + 1;
                    break;
                }
            }
            
            if (foundA == 0) {
                throw new IllegalStateException(String.format("Prime %d is not a sum of two squares", candidate));
            }
            
            pythagPrime = candidate;
            a = foundA;
            b = foundB;
        }

        @Override
        public String toString() {
            return String.format(
                "%s = %s² + %s²    (%s = %s + %s)",
                formatWithCommas(pythagPrime), formatWithCommas(a), formatWithCommas(b), formatWithCommas(pythagPrime), formatWithCommas(a * a), formatWithCommas(b * b)
            );
        }
    }


    // --- Fibonacci-Like Sequences section ---

    private static void fibonacciLikeSequencesSection(String name) {
        runDoubleInputSection(
            name,
            "Builds a 20-term sequence whose first two terms are the inputs; each later " +
            "term is the sum of the previous two. Shows ratios at positions 5/4, 10/9, 15/14, 20/19.",
            """
            A Fibonacci-like sequence starts with two seed values; each subsequent \
            term is the sum of the previous two: a, b, a+b, a+2b, 2a+3b, .... The \
            classical Fibonacci sequence uses seeds 1, 1, and the Lucas sequence \
            uses 2, 1. For any starting pair (with at least one positive value), \
            the ratio of consecutive terms converges to the golden ratio \
            φ = (1 + √5) / 2 ≈ 1.618. This convergence reflects the fact that \
            the dominant eigenvalue of the recurrence's 2×2 transition matrix is φ.""",
            1,
            NINE_QUINTILLION,
            (a, b) -> {
                List<BigInteger> sequence = fibonacciLikeSequence(a, b);
                println("Sequence:");
                println(wrapped(sequence, NTP::formatWithCommas));

                println("\nRatios:");
                for (int i : new int[]{ 4, 9, 14, 19 }) {
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
        
        public boolean exact() {
            return exact;
        }
        
        @Override
        public String toString() {
            return String.format(
                "%s / %s %c %s",
                formatWithCommas(numerator),
                formatWithCommas(denominator),
                exact ? '=' : '≈',
                value.toPlainString()
            );
        }
        
        static final MathContext MATH_CONTEXT_WITH_ROUNDING = MathContext.DECIMAL64;
        
        static final MathContext MATH_CONTEXT_WITHOUT_ROUNDING =
            new MathContext(MATH_CONTEXT_WITH_ROUNDING.getPrecision(), RoundingMode.UNNECESSARY);

        Ratio(BigInteger numerator, BigInteger denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
            var num = new BigDecimal(numerator);
            var den = new BigDecimal(denominator);
            BigDecimal value;
            boolean exact;
            
            try {
                value = num.divide(den, MATH_CONTEXT_WITHOUT_ROUNDING);
                exact = true;
            } catch (ArithmeticException e) {
                value = num.divide(den, MATH_CONTEXT_WITH_ROUNDING);
                exact = false;
            }
            
            this.value = value;
            this.exact = exact;
        }
    }

    // --- Ancient Egyptian Multiplication section ---

    private static void ancientEgyptianMultiplicationSection(String name) {
        runDoubleInputSection(
            name,
            """
            Multiplies two numbers via powers of 2: shows every power of 2 of 2 \
            ≤ the first input paired with its product with the second input, then \
            the subset of powers that sum to the first input.""",
            """
            Ancient Egyptian (or "peasant") multiplication computes a × b using \
            only doubling and addition: write a in binary as a sum of distinct \
            powers of 2, then add the corresponding doublings of b. For example, \
            13 × 12 = 8 × 12 + 4 × 12 + 1 × 12 = 96 + 48 + 12 = 156. The method \
            appears in the Rhind Mathematical Papyrus from around 1650 BC. It is \
            essentially the same algorithm as modern shift-and-add binary \
            multiplication.""",
            2,
            NINE_QUINTILLION,
            (a, b) -> {
                printf("All powers of 2 ≤ %s:", formatWithCommas(a));
                printEgyptianTable(powersOfTwoTable(a, b));
                printf("\nPowers of 2 summing to %s:", formatWithCommas(a));
                printEgyptianTable(powersOfTwoSumming(a, b));
            }
        );
    }
    
    record PowerAndMultiple(long powerOf2, BigInteger multiple) {}

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
        final String powerColHeading = "Power of 2";
        final String multipleColHeading = "Multiple";
        int powerColWidth = powerColHeading.length();
        int multipleColWidth = multipleColHeading.length();
        for (PowerAndMultiple row : rows) {
            powerColWidth = Math.max(powerColWidth, formatWithCommas(row.powerOf2()).length());
            multipleColWidth = Math.max(multipleColWidth, formatWithCommas(row.multiple()).length());
        }
        String rowFormat = "%" + powerColWidth + "s  %" + multipleColWidth + "s";
        printf(rowFormat, powerColHeading, multipleColHeading);
        for (PowerAndMultiple row : rows) {
            printf(rowFormat, formatWithCommas(row.powerOf2()), formatWithCommas(row.multiple()));
        }
    }
}
