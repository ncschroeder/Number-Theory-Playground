package numbertheoryplayground.sectionclasses.outer;

import java.awt.Component;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import numbertheoryplayground.NtpCli;
import numbertheoryplayground.gui.NtpGui;
import numbertheoryplayground.gui.NtpTextArea;
import numbertheoryplayground.sectionclasses.abstract_.SingleInputSection;

import static numbertheoryplayground.Misc.*;
import static numbertheoryplayground.sectionclasses.outer.Divisibility.isDivisible;
import static numbertheoryplayground.sectionclasses.outer.PrimeNumbers.*;

/**
 * Utility class related to semiprimes and the section for it.
 */
public class Semiprimes {
    private final static String INFO = """
A semiprime, also known as a biprime, is a number made by multiplying 2, possibly equal,
prime numbers. The first 5 semiprimes and their prime number factors are 4 (2 × 2), 6 (2 × 3),
9 (3 × 3), 10 (2 × 5), and 14 (2 × 7). Since there are an infinite amount of prime numbers,
there are also an infinite amount of semiprimes. The largest known semiprime is the square of
the largest known prime number, which is (2^136,279,841) − 1.""";
    
    /*
    The calculation for this section is: find the first 20 semiprimes that are ≥ an input
    number, as well as their prime number factors.
     */
    
    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = FIFTY_TRILLION;
    private static final int NUM_SEMIPRIMES_TO_FIND = 20;
    
    record SemiprimeData(long semiprime, long primeFactor1, long primeFactor2) {
        @Override
        public String toString() {
            return String.format(
                "%s (%s × %s)",
                createStringWithCommas(semiprime),
                createStringWithCommas(primeFactor1),
                createStringWithCommas(primeFactor2)
            );
        }
    }
    
    /**
     * If the input is a semiprime, then a SemiprimeData for it gets returned.
     * Otherwise, null gets returned.
     */
    private static SemiprimeData checkIfSemiprime(long input) {
        /*
        First, we need to find the first int factor of the input that's > 1 and < the input.
        Just like with the algorithm for determining if a long is prime, we only need to check
        primes ≤ the square root of the input. If we don't find a factor, then that means that
        the input is prime and not semiprime.
         */
        OptionalLong optionalPrimeFactor1 =
            getPossibleFactors(input)
            .filter(l -> isDivisible(input, l))
            .findFirst();
        
        if (optionalPrimeFactor1.isPresent()) {
            long primeFactor1 = optionalPrimeFactor1.getAsLong();
            long factor2 = input / primeFactor1;
            if (primeFactor1 == factor2 || isPrime(factor2)) {
                return new SemiprimeData(input, primeFactor1, factor2);
            }
        }
        
        return null;
    }
    
    /**
     * Returns a stream of SemiprimeDatas for the first 20 semiprimes ≥ the input.
     */
    static Stream<SemiprimeData> getSemiprimesData(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        return
            LongStream.iterate(input, l -> l + 1)
            .mapToObj(Semiprimes::checkIfSemiprime)
            .filter(sd -> sd != null)
            .limit(NUM_SEMIPRIMES_TO_FIND);
    }
    
    private static Stream<String> getSemiprimesDataStrings(long input) {
        return getSemiprimesData(input).map(SemiprimeData::toString);
    }
    
    private static String getSemiprimesHeading(String inputString) {
        return String.format(
            "The first %d semiprimes ≥ %s are:",
            NUM_SEMIPRIMES_TO_FIND, inputString
        );
    }
    
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Semiprimes",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                String.format(
                    "the first %d semiprimes ≥ that number, as well as their prime number factors",
                    NUM_SEMIPRIMES_TO_FIND
                ),
                "semiprimes"
            );
        }
        
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            Stream<String> semiprimesDataStrings = getSemiprimesDataStrings(inputLong);
            return NtpCli.buildStringWithStreamElementsOnShortLines(
                getSemiprimesHeading(inputString),
                semiprimesDataStrings
            );
        }
        
        @Override
        public List<Component> getGuiComponents(long inputLong, String inputString) {
            var textArea =
                NtpTextArea.createNarrowOneWithStreamElements(getSemiprimesDataStrings(inputLong));
            return List.of(
                NtpGui.createListHeadingLabel(getSemiprimesHeading(inputString)),
                textArea
            );
        }
    }
}