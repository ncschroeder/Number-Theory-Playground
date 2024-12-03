import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import numbertheoryplayground.Misc;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static numbertheoryplayground.NTPCLI.*;

/**
 * Has tests for code in the NTPCLI class.
 */
class NTPCLITest {
    @Test
    void testInsertNewLines() {
        // Create some strings to help with testing. The number suffix is the length.
        // All these test strings contain whitespace except for string10WithoutWhitespace.
        String string10 = "string 10 ";
        String string10WithoutWhitespace = string10.replace(' ', 'a');
        String string80 = string10.repeat(7) + string10WithoutWhitespace;
        String string90 = string80 + string10WithoutWhitespace;
        assumeTrue(string10.length() == 10 && string80.length() == 80 && string90.length() == 90);
        
        assertAll(
            () -> {
                // Strings <= 90 characters shouldn't have any changes made
                assertEquals(
                    string10,
                    insertNewLines(string10),
                    "10 character string doesn't have any changes made"
                );
                assertEquals(
                    string90,
                    insertNewLines(string90),
                    "90 character string doesn't have any changes made"
                );
            },
            () -> {
                // If the 91st character is whitespace, insertNewLines should replace that character
                // with a new line character and an indent if the indent arg is set to true
                String testMessage =
                    "If the 91st character is whitespace, insertNewLines replaces that " +
                    "character with a new line character";
                String string90AndAnA = string90 + " a";
                assertEquals(string90 + "\na", insertNewLines(string90AndAnA), testMessage);
                
                // Next test does the same thing but with indenting
                testMessage += " and an indent";
                assertEquals(string90 + "\n    a", insertNewLines(string90AndAnA, true), testMessage);
                
                // Next test is the same as the 1st but loops more than once
                testMessage =
                    "In each loop, if the 91st character is whitespace, insertNewLines replaces " +
                    "that character with a new line character";
                String arg = String.join(" ", string90, string90, string10WithoutWhitespace);
                String expectedResult = String.join("\n", string90, string90, string10WithoutWhitespace);
                assertEquals(expectedResult, insertNewLines(arg));
                
                // Next test is the same as the 2nd but loops more than once. We're expecting 4
                // whitespace characters to be inserted at the start of the 2nd line, so take
                // off 4 characters from string90 and use that string.
                String string86 = string90.substring(0, 86);
                assumeTrue(string86.length() == 86);
                testMessage += " and an indent";
                arg = String.join(" ", string90, string86, string10WithoutWhitespace);
                expectedResult = String.join("\n    ", string90, string86, string10WithoutWhitespace);
                assertEquals(expectedResult, insertNewLines(arg, true), testMessage);
            },
            () -> {
                String testMessage =
                    "If the 91st character isn't whitespace, insertNewLines goes back to find whitespace";
                String string91 = string80 + " " + string10WithoutWhitespace;
                assumeTrue(string91.length() == 91);
                assertEquals(
                    string80 + "\n" + string10WithoutWhitespace,
                    insertNewLines(string91),
                    testMessage
                );
            }
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("getStreamToStringArgs")
    void testStreamToString(String expectedContentResult, List<String> contentStrings, String message) {
        String testHeading = "test heading";
        assertEquals(
            testHeading + "\n" + expectedContentResult,
            streamToString(testHeading, contentStrings.stream()),
            message
        );
    }
    
    static Stream<Arguments> getStreamToStringArgs() {
        String testMessage =
            "If a list of elements is 75 characters long after being joined together, " +
            "streamToString puts those elements on a single line";
    
        String spaceSeparator = Misc.getWhiteSpace(4);
        String testElement = "test element";
        var strings = new ArrayList<String>(5);
        for (int i = 0; i < 4; i++) {
            strings.add(testElement);
        }
        strings.add("test elemen");
    
        String strings1Joined = String.join(spaceSeparator, strings);
        assumeTrue(strings1Joined.length() == 75);
        Arguments args1 = arguments(strings1Joined, strings, testMessage);
    
        
        testMessage =
            "If a list of elements is 76 characters long after being joined together, streamToString " +
            "puts the last element on its own line and the rest of the elements on the same line";
        
        strings = new ArrayList<>(strings);
        strings.set(strings.size() - 1, testElement);
        assumeTrue(String.join(spaceSeparator, strings).length() == 76);
        String expectedRow1 = String.join(spaceSeparator, strings.subList(0, strings.size() - 1));
        String expectedResult = expectedRow1 + "\n" + testElement;
        Arguments args2 = arguments(expectedResult, strings, testMessage);

        
        return Stream.of(args1, args2);
    }
    
    
    @Test
    void testGetRowForTable() {
        assertEquals(
            "test column 1   test column 2",
            getRowFor2ColumnTable("test column 1", 16, "test column 2")
        );
        
        assertEquals(
            "test column 1       test column 2                 test column 3",
            getRowFor3ColumnTable("test column 1", 20, "test column 2", 30, "test column 3")
        );
    }
}