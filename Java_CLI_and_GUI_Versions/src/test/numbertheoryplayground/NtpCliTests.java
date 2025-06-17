package numbertheoryplayground;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.Misc.getSpace;
import static numbertheoryplayground.NtpCli.*;

/**
 * Has tests for code in the NtpCli class.
 */
class NtpCliTests {
    @Test
    void testPutNewLineChars() {
        /*
        Create some strings to help with testing. The number ending is the length.
        All of these strings contain a space except for string10WithoutASpace.
         */
        var string10 = "string 10 ";
        var string10WithoutASpace = string10.replace(' ', 'a');
        var string80 = string10.repeat(7) + string10WithoutASpace;
        var string90 = string80 + string10WithoutASpace;
        assumeTrue(string10.length() == 10 && string80.length() == 80 && string90.length() == 90);

        assertAll(
            () -> {
                // Strings ≤ 90 characters shouldn't have any changes made.
                assertEquals(
                    string10,
                    putNewLineChars(string10),
                    "10 character string doesn't have any changes made."
                );
                assertEquals(
                    string90,
                    putNewLineChars(string90),
                    "90 character string doesn't have any changes made."
                );
            },
            () -> {
                /*
                If the 91st character is a space, putNewLines should replace that character
                with a new line character and an indent if the indent arg is set to true.
                 */
                var testMessage = """
                    If the 91st character is a space, putNewLines replaces that \
                    character with a new line character
                    """;
                
                var string90AndAnA = string90 + " a";
                assertEquals(string90 + "\na", putNewLineChars(string90AndAnA), testMessage + '.');

                // Next test does the same thing but with indenting.
                testMessage += " and an indent.";
                assertEquals(string90 + "\n    a", putNewLineChars(string90AndAnA, true), testMessage);

                // Next test is the same as the first but loops more than once.
                testMessage = """
                    In each loop, if the 91st character is a space, putNewLines replaces \
                    that character with a new line character
                    """;
                
                String arg = String.join(" ", string90, string90, string10WithoutASpace);
                String expectedResult = String.join("\n", string90, string90, string10WithoutASpace);
                assertEquals(expectedResult, putNewLineChars(arg), testMessage + '.');

                /*
                Next test is the same as the 2nd but loops more than once. We're expecting
                4 space characters to be inserted at the start of the 2nd line, so take off
                4 characters from string90 and use that string.
                 */
                var string86 = string90.substring(0, 86);
                assumeTrue(string86.length() == 86);
                testMessage += " and an indent.";
                arg = String.join(" ", string90, string86, string10WithoutASpace);
                expectedResult = String.join("\n    ", string90, string86, string10WithoutASpace);
                assertEquals(expectedResult, putNewLineChars(arg, true), testMessage);
            },
            () -> {
                var testMessage =
                    "If the 91st character isn't a space, putNewLines goes back to find a space.";
                var string91 = string80 + ' ' + string10WithoutASpace;
                assumeTrue(string91.length() == 91);
                assertEquals(
                    string80 + '\n' + string10WithoutASpace,
                    putNewLineChars(string91),
                    testMessage
                );
            }
        );
    }


    @ParameterizedTest
    @MethodSource("getArgsForTestBuildStringWithStreamElementsOnShortLines")
    void testBuildStringWithStreamElementsOnShortLines(String expectedContentResult, List<String> contentStrings, String message) {
        var testHeading = "test heading";
        assertEquals(
            testHeading + '\n' + expectedContentResult,
            buildStringWithStreamElementsOnShortLines(testHeading, contentStrings.stream()),
            message
        );
    }

    static Stream<Arguments> getArgsForTestBuildStringWithStreamElementsOnShortLines() {
        var testMessage = """
            If a list of elements is 75 characters long after being joined together, \
            buildStringWithStreamElementsOnShortLines puts those elements on a single line.
            """;

        String spaceSeparator = getSpace(4);
        var testElement = "test element";
        var strings = new ArrayList<String>(5);
        for (var i = 1; i <= 4; i++) {
            strings.add(testElement);
        }
        strings.add("test elemen");

        String strings1Joined = String.join(spaceSeparator, strings);
        assumeTrue(strings1Joined.length() == 75);
        Arguments args1 = arguments(strings1Joined, strings, testMessage);


        testMessage = """
            If a list of elements is 76 characters long after being joined together, \
            buildStringWithStreamElementsOnShortLines puts the last element on its own line \
            and the rest of the elements on the same line.
            """;
        
        strings = new ArrayList<>(strings);
        strings.set(strings.size() - 1, testElement);
        assumeTrue(String.join(spaceSeparator, strings).length() == 76);
        String expectedRow1 = String.join(spaceSeparator, strings.subList(0, strings.size() - 1));
        String expectedResult = expectedRow1 + '\n' + testElement;
        Arguments args2 = arguments(expectedResult, strings, testMessage);


        return Stream.of(args1, args2);
    }


    @Test
    void testGetRowFor3ColumnTable() {
        assertEquals("1 2  3", getRowFor3ColumnTable("1", 2, "2", 3, "3"));
    }
}
