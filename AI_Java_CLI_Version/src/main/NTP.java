import java.math.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

class NTP {
    static final Scanner scanner = new Scanner(System.in);
    static final Random random = new Random();

    // Max input constants
    static final long FIVE_HUNDRED_BILLION = 500_000_000_000L;
    static final long TEN_TRILLION = 10_000_000_000_000L;
    static final long FIFTY_TRILLION = TEN_TRILLION * 5;
    static final long ONE_QUADRILLION = 1_000_000_000_000_000L;
    static final long FIVE_QUADRILLION = ONE_QUADRILLION * 5;
    static final long TEN_QUADRILLION = ONE_QUADRILLION * 10;
    static final long NINE_QUINTILLION = 9_000_000_000_000_000_000L;

    static final String OVERVIEW =
        Stream.of("""
            Number theory is the branch of mathematics concerned with the integers \
            and the relationships between them: primes, divisibility, factorization, \
            and the patterns they form. It is one of the oldest parts of \
            mathematics, studied for its elegance for millennia before it became the \
            foundation of modern cryptography, computing, and coding theory.""",
            """
            The Number Theory Playground is an interactive tour of these ideas. Each \
            section takes a number or two and works a classic construction out in \
            full — the primes above a value, a number's prime factorization and \
            divisors, GCD and LCM, Goldbach pairs, Pythagorean triples, and more. At \
            any input prompt you can enter 'i' for background on that topic, 'r' for \
            a random input, or 'm' to return to this menu. Choose an option above to \
            begin exploring."""
        )
        .map(NTP::wrapped)
        .collect(Collectors.joining("\n\n"));

    /**
     * A main-menu entry: {@code run} runs the section. {@code name} is held as a field so it
     * is defined once instead of being hardcoded in both the menu listing and in the method
     * that runs that section.
     */
    record Section(String name, Consumer<String> run) {}

    static final List<Section> SECTIONS =
        List.of(
            new Section("Prime Numbers", NTP::primeNumbersSection),
            new Section("Semiprimes", NTP::semiprimesSection),
            new Section("Twin Prime Pairs", NTP::twinPrimePairsSection),
            new Section("Prime Factorization", NTP::primeFactorizationSection),
            new Section("Divisibility", NTP::divisibilitySection),
            new Section("GCD and LCM", NTP::gcdAndLcmSection),
            new Section("Goldbach Conjecture", NTP::goldbachConjectureSection),
            new Section("Pythagorean Triples", NTP::pythagoreanTriplesSection),
            new Section("Two Square Theorem", NTP::twoSquareTheoremSection),
            new Section("Fibonacci-Like Sequences", NTP::fibonacciLikeSequencesSection),
            new Section("Ancient Egyptian Multiplication", NTP::ancientEgyptianMultiplicationSection)
        );
    
    static void println() { System.out.println(); }
    
    static void println(Object o) { System.out.println(o); }
    
    static void printf(String format, Object... args) {
        System.out.printf(format, args);
        println();
    }

    public static void main(String[] args) {
        String menu;
        final String infoValue = "i";
        final String exitValue = "e";

        {
            var menuLinesJoiner = new StringJoiner("\n").add("=== Number Theory Playground ===");
            for (int i = 0; i < SECTIONS.size(); i++) {
                menuLinesJoiner.add(String.format("%2d. %s", i + 1, SECTIONS.get(i).name()));
            }
            
            menu =
                menuLinesJoiner
                .add(String.format("%2s. Overview of number theory and this playground", infoValue))
                .add(String.format("%2s. Exit", exitValue))
                .add("Select an option: ")
                .toString();
        }

        while (true) {
            println();
            System.out.print(menu);
            String input = scanner.nextLine().trim();
            
            if (input.equals(exitValue)) {
                println("Goodbye!");
                return;
            }

            if (input.equals(infoValue)) {
                println();
                println(OVERVIEW);
                continue;
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

    static void printSectionHeader(String name) { printf("=== %s ===", name); }
    
    /*
    For runSingleInputSection and runDoubleInputSection, the action consumers are called with
    validated input number(s) to produce the section's output
     */
    
    static void runSingleInputSection(String name, String description, String info, long min, long max, LongConsumer action) {
        println();
        printSectionHeader(name);
        println(description);
        while (true) {
            Long n = readInput(info, min, max);
            if (n == null) return;
            println();
            action.accept(n);
            println();
        }
    }

    static void runDoubleInputSection(String name, String description, String info, long min, long max, BiConsumer<Long, Long> action) {
        println();
        printSectionHeader(name);
        println(description);
        while (true) {
            long[] inputs = readTwoInputs(info, min, max);
            if (inputs == null) return;
            println();
            action.accept(inputs[0], inputs[1]);
            println();
        }
    }
    
    static Long readInput(String info, long min, long max) {
        return readInput("Enter a number", info, min, max, () -> randomInput(min, max));
    }
    
    /**
     * Prompts for an integer within [min, max].
     * Accepts 'r' (random, via {@code randomGen}), 'i' (info), or 'm' for menu (returns null).
     * {@code randomGen} is a parameter so callers with extra constraints can supply their own
     * generator — e.g. the Goldbach section needs the random value to be even.
     */
    static Long readInput(String prompt, String info, long min, long max, LongSupplier randomGen) {
        while (true) {
            System.out.printf(
                "%s (%s–%s), 'r' for random, 'i' for info, or 'm' for menu: ",
                prompt,
                formatWithCommas(min),
                formatWithCommas(max)
            );
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("m")) return null;
            if (input.equalsIgnoreCase("i")) {
                println();
                println(wrapped(info));
                println();
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
    static long[] readTwoInputs(String info, long min, long max) {
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
                println();
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
                return new long[]{ a, b };
            } catch (NumberFormatException e) {
                println("Invalid input — enter two whole numbers.");
            }
        }
    }
    
    /**
     * Picks a random digit count from 1 to the digit count of max, then a random number with that many digits
     * (capped at max so the top-digit-count bucket stays within range). Retries if the random number falls below min.
     */
    static long randomInput(long min, long max) {
        int maxDigits = String.valueOf(max).length();
        while (true) {
            int digits = 1 + random.nextInt(maxDigits);
            long origin = digits == 1 ? 0 : pow10(digits - 1);
            long bound = digits == maxDigits ? max + 1 : pow10(digits);
            long value = random.nextLong(origin, bound);
            if (value >= min) return value;
        }
    }

    static long pow10(int n) {
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
    
    static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (long i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }


    static void primeNumbersSection(String name) {
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

    
    static void semiprimesSection(String name) {
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

    static SemiprimeData semiprimeFactors(long n) {
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


    static void twinPrimePairsSection(String name) {
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


    static void primeFactorizationSection(String name) {
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
    
    
    static void divisibilitySection(String name) {
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
                println("Divisibility:\n");
                println(divisResults(n));
                
                String nString = formatWithCommas(n);
                List<FactorAndPower> pfs = primeFactorsAndPowersOf(n);
                printf("\n%s = %s", nString, fpsToString(pfs));
                
                if (pfs.size() == 1 && pfs.getFirst().power == 1) {
                    println(nString + " is prime and doesn't have any factors besides 1 and itself.");
                } else {
                    printf("\nFactors of %s:", nString);
                    println(wrapped(factorsFrom(pfs), Object::toString));
                }
            }
        );
    }

    
    static String divisResult(int divisor, boolean divisible, String explanation) {
        return String.format("%2d: %s — %s", divisor, explanation, (divisible ? "" : "not ") +  "divisible");
    }
    
    /**
     * Instances of this are often shortened to "se".
     */
    record SumAndExpression(int sum, String expression) {}

    /**
     * The 1st line of the returned string has shows the digit sum. The rest of the lines are for each
     * of the divisors 3, 4, 6, 7, 8, 9, 11, and 12. Each of these lines states whether it divides
     * {@code n} and the reasoning from its divisibility rule. The digit sum is used for the rules for
     * 3 and 9 so that's why it's shown on the 1st line.
     */
    static String divisResults(long n) {
        var lines = new ArrayList<String>();
        
        SumAndExpression digitSe = getDigitSe(n);
        int digitSum = digitSe.sum();
        lines.add(String.format("The digit sum is %s = %d", digitSe.expression, digitSe.sum));
        
        // n is divisible by 3 if the sum of its digits is.
        boolean divisBy3 = digitSum % 3 == 0;
        lines.add(
            divisResult(
                3,
                divisBy3,
                String.format("digit sum %s divisible by 3", divisBy3 ? "is" : "isn't")
            )
        );
        
        // n is divisible by 4 if the number formed from the last 2 digits is.
        long lastTwo = n % 100;
        boolean divisBy4 = lastTwo % 4 == 0;
        lines.add(
            divisResult(
                4,
                divisBy4,
                String.format("last two digits are %d, which %s divisible by 4", lastTwo, divisBy4 ? "is" : "isn't")
            )
        );

        // n is divisible by 6 if it's divisible by 2 and 3.
        boolean even = n % 2 == 0;
        String by6Explanation = (even && divisBy3 ? "" : "not ") + "divisible by 2 and 3";
        lines.add(divisResult(6, even && divisBy3, by6Explanation));

        // n is divisible by 7 exactly when the alternating 3-digit block sum is.
        SumAndExpression blockSe = getAlternatingBlockSe(n);
        boolean divisBy7 = blockSe.sum() % 7 == 0;
        lines.add(
            divisResult(
                7,
                divisBy7,
                String.format(
                    "alternating 3-digit block sum %s = %s, which %s divisible by 7",
                    blockSe.expression(),
                    formatWithCommas(blockSe.sum()),
                    divisBy7 ? "is" : "isn't"
                )
            )
        );

        // n is divisible by 8 if the number formed from the last 3 digits is.
        long lastThree = n % 1_000;
        boolean divisBy8 = lastThree % 8 == 0;
        lines.add(
            divisResult(
                8,
                divisBy8,
                String.format("last three digits are %d, which %s divisible by 8", lastThree, divisBy8 ? "is" : "isn't")
            )
        );

        // n is divisible by 9 if the sum of its digits is.
        
        boolean divisBy9 = digitSum % 9 == 0;
        lines.add(
            divisResult(
                9,
                divisBy9,
                String.format("digit sum %s divisible by 9", divisBy9 ? "is" : "isn't")
            )
        );

        // n is divisible by 11 exactly when the alternating digit sum is.
        SumAndExpression altDigitSe = getAlternatingDigitSe(n);
        boolean divisBy11 = altDigitSe.sum() % 11 == 0;
        lines.add(
            divisResult(
                11,
                divisBy11,
                String.format(
                    "alternating digit sum %s = %s, which %s divisible by 11",
                    altDigitSe.expression(),
                    formatWithCommas(altDigitSe.sum()),
                    divisBy11 ? "is" : "isn't"
                )
            )
        );

        // n is divisible by 12 if it's divisible by 3 and 4.
        String by12Explanation = (divisBy3 && divisBy4 ? "" : "not ") + "divisible by 3 and 4";
        lines.add(divisResult(12, divisBy3 && divisBy4, by12Explanation));

        return String.join("\n", lines);
    }
    
    /**
     * Sums n's digits.
     */
    static SumAndExpression getDigitSe(long n) {
        String digits = String.valueOf(n);
        int sum = 0;
        var expressionBuilder = new StringBuilder();
        
        for (int i = 0; i < digits.length(); i++) {
            int digit = Character.getNumericValue(digits.charAt(i));
            sum += digit;
            if (i > 0) expressionBuilder.append(" + ");
            expressionBuilder.append(digit);
        }
        
        return new SumAndExpression(sum, expressionBuilder.toString());
    }
    
    /**
     * Does an alternating sum of n's digits. Adds the 1st digit, subtracts the 2nd digit, and so on.
     */
    static SumAndExpression getAlternatingDigitSe(long n) {
        String digits = String.valueOf(n);
        int sum = 0;
        var expressionBuilder = new StringBuilder();
        
        for (int i = 0; i < digits.length(); i++) {
            int digit = Character.getNumericValue(digits.charAt(i));
            int sign = (i % 2 == 0) ? 1 : -1;
            sum += sign * digit;
            if (i == 0) {
                expressionBuilder.append(digit);
            } else {
                expressionBuilder.append(sign == 1 ? " + " : " − ").append(digit);
            }
        }
        
        return new SumAndExpression(sum, expressionBuilder.toString());
    }
    
    /**
     * Does an alternating sum of 3-digit groups in n from right to left. Adds the rightmost group,
     * subtracts the group to the left of that, and so on.
     */
    static SumAndExpression getAlternatingBlockSe(long n) {
        String digits = String.valueOf(n);
        int sum = 0;
        var expressionBuilder = new StringBuilder();
        int i = 0;

        for (int end = digits.length(); end > 0; end -= 3) {
            String block = digits.substring(Math.max(0, end - 3), end);
            int sign = (i % 2 == 0) ? 1 : -1;
            sum += sign * Integer.parseInt(block);
            if (i == 0) {
                expressionBuilder.append(block);
            } else {
                expressionBuilder.append(sign == 1 ? " + " : " − ").append(block);
            }
            i++;
        }
        
        return new SumAndExpression(sum, expressionBuilder.toString());
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
        
        for (FactorAndPower fp : fps) {
            long primeFactor = fp.factor();
            int inputFpPower = fp.power();
            int startSize = result.size();
            
            for (int newFactorPower = 1; newFactorPower <= inputFpPower; newFactorPower++) {
                var pow = (long) Math.pow(primeFactor, newFactorPower);
                var primeFactorAndPowerString =
                    formatWithCommas(primeFactor) + (newFactorPower > 1 ? "^" + newFactorPower : "");
                result.add(new Factor(pow, primeFactorAndPowerString));
                
                for (int i = 0; i < startSize; i++) {
                    Factor existingFactor = result.get(i);
                    long newFactorValue = existingFactor.value() * pow;
                    String newFactorFactorization =
                        String.format("%s × %s", existingFactor.factorization(), primeFactorAndPowerString);
                    result.add(new Factor(newFactorValue, newFactorFactorization));
                }
            }
        }
        
        result.sort(Comparator.comparingLong(Factor::value));
        return result;
    }


    static void gcdAndLcmSection(String name) {
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
    
    static void printEuclideanTable(List<EuclideanStep> steps) {
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
        final List<FactorAndPower> gcdFps;
        final List<FactorAndPower> lcmFps;
        
        List<FactorAndPower> gcdFps() { return gcdFps; }
        List<FactorAndPower> lcmFps() { return lcmFps; }

        GcdAndLcmPrimeFactorizationData(List<FactorAndPower> aFps, List<FactorAndPower> bFps) {
            var gcdFps = new ArrayList<FactorAndPower>();
            var lcmFps = new ArrayList<FactorAndPower>();
            
            for (FactorAndPower fp : aFps) {
                findFactor(bFps, fp.factor())
                .ifPresentOrElse(
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
    
    static Optional<FactorAndPower> findFactor(List<FactorAndPower> fps, long factor) {
        return fps.stream().filter(pf -> pf.factor() == factor).findFirst();
    }
    
    /**
     * Multiplies the prime factorization {@code fps} back out into the number it represents.
     * The result is a BigInteger because an LCM of two large inputs can exceed the range of
     * a long.
     */
    static BigInteger product(List<FactorAndPower> fps) {
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
    
    
    static void goldbachConjectureSection(String name) {
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

    
    static void pythagoreanTriplesSection(String name) {
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

    
    static void twoSquareTheoremSection(String name) {
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

    
    static void fibonacciLikeSequencesSection(String name) {
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
                for (int i : new int[] { 4, 9, 14, 19 }) {
                    println(ratioExpression(sequence.get(i), sequence.get(i - 1)));
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
    
    static final MathContext MATH_CONTEXT_WITH_ROUNDING = MathContext.DECIMAL64;
    
    static final MathContext MATH_CONTEXT_WITHOUT_ROUNDING =
        new MathContext(MATH_CONTEXT_WITH_ROUNDING.getPrecision(), RoundingMode.UNNECESSARY);

    static String ratioExpression(BigInteger numerator, BigInteger denominator) {
        var num = new BigDecimal(numerator);
        var den = new BigDecimal(denominator);
        BigDecimal value;
        char equalityChar;

        try {
            value = num.divide(den, MATH_CONTEXT_WITHOUT_ROUNDING);
            equalityChar = '=';
        } catch (ArithmeticException e) {
            value = num.divide(den, MATH_CONTEXT_WITH_ROUNDING);
            equalityChar = '≈';
        }
        
        return String.format(
            "%s / %s %c %s",
            formatWithCommas(numerator),
            formatWithCommas(denominator),
            equalityChar,
            value.toPlainString()
        );
    }


    static void ancientEgyptianMultiplicationSection(String name) {
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

    static void printEgyptianTable(List<PowerAndMultiple> rows) {
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
