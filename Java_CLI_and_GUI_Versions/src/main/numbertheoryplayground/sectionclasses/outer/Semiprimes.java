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

public class Semiprimes {
    private final static String INFO = """
    """;
    
    private static final long MIN_INPUT = 0;
    private static final long MAX_INPUT = FIFTY_TRILLION;
    private static final int NUM_TO_FIND = 20;
    
    record SemiprimeData(long semiprime, long factor1, long factor2) {
        @Override
        public String toString() {
            return
                String.format(
                    "%s (%s × %s)",
                    createStringWithCommas(semiprime),
                    createStringWithCommas(factor1),
                    createStringWithCommas(factor2)
                );
        }
    }
    
    /**
     * If the input is a semiprime, a SemiprimeData for it gets returned.
     * Otherwise, null gets returned.
     */
    private static SemiprimeData checkIfSemiprime(long input) {
        /*
        Just like with the algorithm for determining if a long is prime, to find the 1st
        factor of the input, we only need to check primes ≤ the square root of the input.
        If we don't find a factor, then that means the input is prime and not semiprime.
         */
        OptionalLong optionalFactor1 =
            getPossibleFactors(input)
            .filter(l -> isDivisible(input, l))
            .findFirst();
        
        if (optionalFactor1.isPresent()) {
            long factor1 = optionalFactor1.getAsLong();
            long factor2 = input / factor1;
            if (factor1 == factor2 || isPrime(factor2)) {
                return new SemiprimeData(input, factor1, factor2);
            }
        }
        
        return null;
    }
    
    /**
     * Returns a list of SemiprimeDatas for the first 20 semiprimes ≥ the input.
     */
    static Stream<SemiprimeData> getSemiprimeDatas(long input) {
        assertIsInRange(input, MIN_INPUT, MAX_INPUT);
        
        return
            LongStream.iterate(input, l -> l + 1)
            .mapToObj(Semiprimes::checkIfSemiprime)
            .filter(sd -> sd != null)
            .limit(NUM_TO_FIND);
    }
    
    private static Stream<String> getSemiprimeDataStrings(long input) {
        return getSemiprimeDatas(input).map(SemiprimeData::toString);
    }
    
    private static String getSemiprimesHeading(String inputString) {
        return String.format(
            "The first %d semiprimes ≥ %s are:",
            NUM_TO_FIND, inputString
        );
    }
    
    
    public static final class Section extends SingleInputSection {
        public Section() {
            super(
                "Semiprimes",
                INFO,
                MIN_INPUT,
                MAX_INPUT,
                String.format("the first %d semiprimes ≥ that number", NUM_TO_FIND),
                "semiprimes"
            );
        }
        
        @Override
        public String getCliAnswer(long inputLong, String inputString) {
            Stream<String> semiprimeDataStrings = getSemiprimeDataStrings(inputLong);
            return NtpCli.buildStringWithStreamElementsOnShortLines(
                getSemiprimesHeading(inputString),
                semiprimeDataStrings
            );
        }
        
        @Override
        public List<Component> getGuiComponents(long inputLong, String inputString) {
            var textArea =
                NtpTextArea.createNarrowOneWithStreamElements(getSemiprimeDataStrings(inputLong));
            return List.of(
                NtpGui.createListHeadingLabel(getSemiprimesHeading(inputString)),
                textArea
            );
        }
    }
}