package numbertheoryplayground.sectionclasses.outer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TwinPrimePairsTests {
    @ParameterizedTest
    @MethodSource("getArgs")
    void getPairStarts(long input, List<Integer> expectedPairStartInts) {
        List<Long> expectedPairStartLongs =
            expectedPairStartInts
            .stream()
            .map(Integer::longValue)
            .toList();
        
        assertEquals(expectedPairStartLongs, TwinPrimePairs.getPairStarts(input));
    }
    
    static Stream<Arguments> getArgs() {
        // I got these pair starts from OEIS sequence A001359 at https://oeis.org/A001359.
        
        var first20PairStarts =
            List.of(
                3, 5, 11, 17, 29, 41, 59, 71, 101, 107, 137,
                149, 179, 191, 197, 227, 239, 269, 281, 311
            );
        
        var first20PairStartsAfter500 =
            List.of(
                521, 569, 599, 617, 641, 659, 809, 821, 827, 857, 881, 1_019,
                1_031, 1_049, 1_061, 1_091, 1_151, 1_229, 1_277, 1_289
            );
        
        return Stream.of(
            arguments(0, first20PairStarts),
            arguments(first20PairStartsAfter500.getFirst(), first20PairStartsAfter500)
        );
    }
}