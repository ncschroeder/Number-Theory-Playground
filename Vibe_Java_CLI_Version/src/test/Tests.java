import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    @Test
    void findPrimesFromZero() {
        assertEquals(
            List.of(
                2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L, 29L, 31L, 37L, 41L, 43L, 47L,
                53L, 59L, 61L, 67L, 71L, 73L, 79L, 83L, 89L, 97L, 101L, 103L, 107L, 109L, 113L
            ),
            NTP.findPrimesFrom(0)
        );
    }

    @Test
    void findPrimesFromThousand() {
        assertEquals(
            List.of(
                1_009L, 1_013L, 1_019L, 1_021L, 1_031L, 1_033L, 1_039L, 1_049L, 1_051L, 1_061L,
                1_063L, 1_069L, 1_087L, 1_091L, 1_093L, 1_097L, 1_103L, 1_109L, 1_117L, 1_123L,
                1_129L, 1_151L, 1_153L, 1_163L, 1_171L, 1_181L, 1_187L, 1_193L, 1_201L, 1_213L
            ),
            NTP.findPrimesFrom(1000)
        );
    }

    @Test
    void findTwinPrimesFromZero() {
        assertEquals(
            List.of(
                3L, 5L, 11L, 17L, 29L, 41L, 59L, 71L, 101L, 107L,
                137L, 149L, 179L, 191L, 197L, 227L, 239L, 269L, 281L, 311L
            ),
            NTP.findTwinPrimesFrom(0)
        );
    }

    @Test
    void findTwinPrimesFromThousand() {
        assertEquals(
            List.of(
                1_019L, 1_031L, 1_049L, 1_061L, 1_091L, 1_151L, 1_229L, 1_277L, 1_289L, 1_301L,
                1_319L, 1_427L, 1_451L, 1_481L, 1_487L, 1_607L, 1_619L, 1_667L, 1_697L, 1_721L
            ),
            NTP.findTwinPrimesFrom(1000)
        );
    }

    @Test
    void wrappedEmpty() {
        assertEquals("", NTP.wrapped(Stream.of()));
    }

    @Test
    void wrappedSinglePiece() {
        assertEquals("abc", NTP.wrapped(Stream.of("abc")));
    }

    @Test
    void wrappedFitsOnOneLine() {
        assertEquals("1    2    3", NTP.wrapped(Stream.of("1", "2", "3")));
    }

    @Test
    void wrappedFillsLineUpToLimit() {
        // 10-char piece + 4-space separator: 5 pieces = 66 chars (fits), 6 would be 80 (wraps).
        String piece = "abcdefghij";
        String line = String.join("    ", piece, piece, piece, piece, piece);
        assertEquals(line, NTP.wrapped(Stream.of(piece, piece, piece, piece, piece)));
    }

    @Test
    void wrappedMultipleLines() {
        String piece = "abcdefghij";
        String line = String.join("    ", piece, piece, piece, piece, piece);
        assertEquals(
            line + "\n" + line,
            NTP.wrapped(Stream.of(piece, piece, piece, piece, piece, piece, piece, piece, piece, piece))
        );
    }
}
