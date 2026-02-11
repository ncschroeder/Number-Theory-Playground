package numbertheoryplayground;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import numbertheoryplayground.sectionclasses.abstract_.*;
import numbertheoryplayground.sectionclasses.outer.GoldbachConjecture;

import static numbertheoryplayground.Misc.*;

/**
 * Utility class with code related to the Number Theory Playground command line interface,
 * including the code for running the application and some static methods. The static methods
 * are used in this class and by nested section classes for implementing getCliAnswer.
 */
public class NtpCli {
    private static final Scanner inputReader = new Scanner(System.in);
    
    private static String getFormattedInput() {
        return inputReader.nextLine().strip().toLowerCase();
    }
    
    private static void println(String s) {
        System.out.println(s);
    }
    
    private static void println() {
        System.out.println();
    }
    
    private static void printInvalidInput() {
        println("Invalid input");
    }
    
    public static void main(String[] args) {
        GoldbachConjecture.setMaxInputForCli();
        
        String ntpInfo =
            buildStringWithHeadingAndInfoParagraphs(
                "Number Theory Playground",
                getParagraphs(NTP_INFO)
            );
        
        /*
        Setup and show main menu. There'll be options to exit, get info about the NTP, and go to
        the sections. There are currently 11 sections so the ints 1-11 will be what the user can
        enter to go to a section. These ints will be at the start of the lines for the section
        options. There'll be a 1-space indent for the lines that start with a single digit.

        Let inputsAndSections be a map where the keys are the string representations of the
        aforementioned ints and the values are the corresponding Section objects.
         */
        
        var inputsAndSections = new HashMap<String, Section>();
        final String exitValue = "e";
        final String ntpInfoValue = "i";
        String menuString;
        
        {
            final String directions = """
                Enter "e" to exit, "i" to get info about the Number Theory Playground, \
                or one of the following to go to a section:""";
            
            StringJoiner menuLinesJoiner =
                new StringJoiner("\n")
                .add("Number Theory Playground Main Menu")
                .add("")
                .add(putNewLineChars(directions));
            
            var inputForSection = 1;
            for (var section : Section.createInstances()) {
                var maybeIndent = inputForSection < 10 ? " " : "";
                menuLinesJoiner.add(String.format("%s(%d) %s", maybeIndent, inputForSection, section.getHeading()));
                inputsAndSections.put(Integer.toString(inputForSection++), section);
            }
            
            menuString = menuLinesJoiner.toString();
        }
        
        println("\nWelcome to the command line version of the Number Theory Playground.");
        
        while (true) {
            println();
            println(menuString);
            String input = getFormattedInput();
            
            switch (input) {
                case ntpInfoValue:
                    println();
                    println(ntpInfo);
                    break;
                
                case exitValue:
                    println("\nI hope you found this interesting.");
                    return;
                    
                default:
                    var sectionToGoTo = inputsAndSections.get(input);
                    if (sectionToGoTo != null) {
                        goToSection(sectionToGoTo);
                    } else {
                        println();
                        printInvalidInput();
                    }
            }
        }
    }
    
    /**
     * Displays the section heading and choices for the section and then lets the user either
     * enter custom input to use for the calculation(s) of the section, generate random input
     * to use for the calculation(s), get info about the section, or go back to the main menu.
     */
    private static void goToSection(Section section) {
        // Valid input values
        final String randomValue = "r";
        final String infoValue = "i";
        final String menuValue = "m";
        
        var sectionChoicesString =
            buildSectionChoicesString(section, randomValue, infoValue, menuValue);
        String sectionHeading = section.getHeading();
        String sectionInfo =
            buildStringWithHeadingAndInfoParagraphs(
                sectionHeading, section.getInfoParagraphs().stream()
            );
        
        
        while (true) {
            println();
            println(sectionHeading);
            println();
            println(sectionChoicesString);
            
            String input = getFormattedInput();
            if (input.equals(menuValue)) return;
            
            println();
            
            switch (input) {
                case infoValue:
                    println(sectionInfo);
                    break;
                    
                case randomValue:
                    println(section.getRandomCliAnswer());
                    break;
                    
                default:
                    try {
                        if (section instanceof SingleInputSection sis) {
                            var inputLong = stripCommasAndParse(input);
                            var inputString = createStringWithCommas(inputLong);
                            println(sis.getCliAnswer(inputLong, inputString));
                        } else if (section instanceof DoubleInputSection dis) {
                            String[] inputContents = input.split("\\s+");
                            if (inputContents.length != 2) {
                                printInvalidInput();
                            } else {
                                var input1Long = stripCommasAndParse(inputContents[0]);
                                var input2Long = stripCommasAndParse(inputContents[1]);
                                var input1String = createStringWithCommas(input1Long);
                                var input2String = createStringWithCommas(input2Long);
                                println(dis.getCliAnswer(input1Long, input2Long, input1String, input2String));
                            }
                        }
                    } catch (NumberFormatException | InvalidInputNumberException ex) {
                        printInvalidInput();
                    }
            }
        }
    }
    
    private static String buildSectionChoicesString(
        Section section,
        String randomValue,
        String infoValue,
        String menuValue
    ) {
        String numChoice =
            String.format(
                "%s to %s. %s",
                section.isSingleInputSection() ? "A whole number" : "2 whitespace-separated whole numbers",
                section.getActionSentencesEnding(),
                section.getInputInfoSentences()
            );
        
        String randomChoice =
            String.format(
                "\"%s\" to generate %s and %s.",
                randomValue,
                section.isSingleInputSection() ? "a random whole number" : "2 random whole numbers",
                section.getActionSentencesEnding()
            );
        
        String infoChoice =
            String.format(
                "\"%s\" to get info about %s.",
                infoValue, section.getCliInfoOptionEnding()
            );
        
        String menuChoice = String.format("\"%s\" to go to the main menu.", menuValue);
        
        Stream<String> lines =
            Stream.of(
                "Enter one of the following:",
                numChoice,
                randomChoice,
                infoChoice,
                menuChoice
            );
        
        return buildStringWithStreamElementsOnSeparateLines(lines);
    }
    
    private static String buildStringWithHeadingAndInfoParagraphs(
        String headingStart,
        Stream<String> infoParagraphsStream
    ) {
        return
            Stream.concat(
                Stream.of(headingStart + " Info"),
                infoParagraphsStream.map(NtpCli::putNewLineChars)
            )
            .collect(Collectors.joining("\n\n"));
    }
    
    /**
     * Returns a new string that's mostly the same as the param string, but with new line chars
     * possibly replacing some space chars. If the indent param is true, then 4 indentation spaces
     * will be inserted after each new line char. Each portion of the returned string between new
     * line chars will be at most 90 chars long.
     *
     * If there are any "words" that are longer than 90 chars, then an IndexOutOfBoundsException
     * will be thrown when running `lines.add(s.subSequence(lineStartIndex, spaceIndex));`.
     */
    public static String putNewLineChars(String s, boolean indent) {
        final int maxLineLength = 90;
        if (s.length() <= maxLineLength) return s;
        final int indentLength = 4;
        var lines = new ArrayList<CharSequence>();
        var lineStartIndex = 0;
        
        while (true) {
            int spaceSearchStartIndex =
                lineStartIndex + (!indent || lines.isEmpty() ? maxLineLength : maxLineLength - indentLength);
            
            if (spaceSearchStartIndex >= s.length()) {
                lines.add(s.subSequence(lineStartIndex, s.length()));
                break;
            }
            
            int spaceIndex = s.lastIndexOf(' ', spaceSearchStartIndex);
            lines.add(s.subSequence(lineStartIndex, spaceIndex));
            lineStartIndex = spaceIndex + 1;
        }
        
        String separator = indent ? '\n' + getSpace(indentLength) : "\n";
        return String.join(separator, lines);
    }
    
    /**
     * false is provided as the second arg so that no indenting is done.
     */
    public static String putNewLineChars(String s) {
        return putNewLineChars(s, false);
    }
    
    /**
     * Returns a string that consists of the heading on its own line followed by lines that
     * consist of the strings of the stream provided separated by space. Each line has a max
     * length of maxLineLength, of course. The exception to this is that if a string of the
     * stream is longer than maxLineLength then it'll be placed on a line by itself.
     */
    private static String buildStringWithStreamElements(
        String heading,
        Stream<String> stream,
        int maxLineLength
    ) {
        final int spaceSeparatorLength = 4;
        String spaceSeparator = getSpace(spaceSeparatorLength);
        var lines = new ArrayList<CharSequence>();
        lines.add(heading);
        
        /*
        There's no way to clear a string joiner so we'll have to set curLine to a new string
        joiner when we get done building a line. We can't use the forEachOrdered method since we
        would have to call it with a Consumer and the most convenient way to do that would be
        with a lambda. Variables used in this method must be final or effectively final to be
        used in a Consumer function in this method, so we wouldn't be able to reassign curLine.
        We'll use an iterator.
         */
        Iterator<String> iterator = stream.iterator();
        StringJoiner curLine =
            new StringJoiner(spaceSeparator)
            .add(iterator.next());
        
        while (iterator.hasNext()) {
            var curString = iterator.next();
            if (curLine.length() + spaceSeparatorLength + curString.length() > maxLineLength) {
                lines.add(curLine.toString());
                curLine = new StringJoiner(spaceSeparator);
            }
            curLine.add(curString);
        }
        
        lines.add(curLine.toString());
        return String.join("\n", lines);
    }
    
    public static String buildStringWithStreamElementsOnShortLines(
        String heading,
        Stream<String> stream
    ) {
        return buildStringWithStreamElements(heading, stream, 75);
    }
    
    public static String buildStringWithStreamElementsOnLongLines(
        String heading,
        Stream<String> stream
    ) {
        return buildStringWithStreamElements(heading, stream, 100);
    }
    
    public static String buildStringWithStreamElementsOnSeparateLines(
        String heading,
        Stream<String> stream
    ) {
        heading = heading != null ? putNewLineChars(heading) : null;
        stream = stream.map(s -> putNewLineChars(s, true));
        return
            Stream.concat(Stream.ofNullable(heading), stream)
            .collect(Collectors.joining("\n"));
    }
    
    public static String buildStringWithStreamElementsOnSeparateLines(Stream<String> stream) {
        return buildStringWithStreamElementsOnSeparateLines(null, stream);
    }
    
    /**
     * Returns a string that consists of column1Contents followed by space followed by
     * column2Contents. The length of column1Contents and the space will be equal to
     * column1Width. column1Width should be > the length of column1Contents.
     */
    public static String getRowFor2ColumnTable(
        String column1Contents,
        int column1Width,
        String column2Contents
    ) {
        int column1EndGap = column1Width - column1Contents.length();
        if (column1EndGap < 1) {
            printError("column1Width wasn't > the length of column1Contents");
            column1EndGap = 1;
        }
        return column1Contents + getSpace(column1EndGap) + column2Contents;
    }
    
    /**
     * Returns a string that consists of column1Contents followed by space followed by
     * column2Contents followed by space followed by column3Contents. The length of
     * column1Contents and the following space will be equal to column1Width. The length of
     * column2Contents and the following space will be equal to column2Width. column1Width
     * should be > the length of column1Contents and column2Width should be > the length of
     * column2Contents.
     */
    public static String getRowFor3ColumnTable(
        String column1Contents,
        int column1Width,
        String column2Contents,
        int column2Width,
        String column3Contents
    ) {
        String first2Columns = getRowFor2ColumnTable(column1Contents, column1Width, column2Contents);
        int column2EndGap = column2Width - column2Contents.length();
        if (column2EndGap < 1) {
            printError("column2Width wasn't > than the length of column2Contents");
            column2EndGap = 1;
        }
        return first2Columns + getSpace(column2EndGap) + column3Contents;
    }
}
