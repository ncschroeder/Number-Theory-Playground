package numbertheoryplayground;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import numbertheoryplayground.sectionclasses.abstract_.*;

import static numbertheoryplayground.Misc.*;

/**
 * Class with code related to the Number Theory Playground Command Line Interface, including the code for
 * running the application and some static methods. The static methods are used in this class and by Section
 * classes for implementing getCliAnswer.
 */
public class NtpCli {
    private static Scanner inputReader;

    private static String getFormattedInput() {
        return inputReader.nextLine().trim().toLowerCase();
    }
    
    private static void print(String s) {
        System.out.print(s);
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
        inputReader = new Scanner(System.in);
        String ntpInfo =
            buildStringWithHeadingAndInfoParagraphs(
                "Number Theory Playground", ntpInfoParagraphStreamSupplier.get()
            );
        
        /*
        Setup and show main menu. There'll be options to go to the sections and an exit option.
        There are currently 10 sections so the ints 1-10 will be what the user can type to go to
        a section. "e" can be typed to exit. The ints and "e" will be at the start of the lines
        for the options. There'll be a 1-space indent for the lines that start with a single
        digit or "e".

        Let inputsAndSections be a map where the keys are the string representations of the
        aforementioned ints and the values are the corresponding Section objects.
         */
        
        
        var inputsAndSections = new HashMap<String, Section>();
        final String ntpInfoValue = "i";
        final String exitValue = "e";
        String menuString;
        
        {
            var menuLines = new ArrayList<String>();
            menuLines.add("You're at the main menu. Which section would you like to go to?");
            menuLines.add(String.format(" (%s) Get info about the Number Theory Playground", ntpInfoValue));
            
            var inputForSection = 1;
            for (var section : Section.createInstances()) {
                var maybeIndent = inputForSection < 10 ? " " : "";
                menuLines.add(String.format("%s(%d) %s", maybeIndent, inputForSection, section.getHeading()));
                inputsAndSections.put(Integer.toString(inputForSection++), section);
            }

            menuLines.add(String.format(" (%s) Exit", exitValue));
            menuLines.add("Enter your choice: ");
            menuString = String.join("\n", menuLines);
        }
        
        println("\nWelcome to the command line version of the Number Theory Playground\n");
        
        while (true) {
            print(menuString);
            String input = getFormattedInput();
            
            switch (input) {
                case ntpInfoValue:
                    println(ntpInfo);
                    break;
                
                case exitValue:
                    println("I hope you found this interesting.");
                    return;
                    
                default:
                    var sectionToGoTo = inputsAndSections.get(input);
                    if (sectionToGoTo != null) {
                        goToSection(sectionToGoTo);
                    } else {
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
        /*
        These are valid input values and they're final so they can be used for branches in the
        switch statement below.
         */
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
        
        println();
        
        while (true) {
            println(section.getHeading());
            println();
            println(sectionChoicesString);
            String input = getFormattedInput();
            println();
            
            switch (input) {
                case infoValue:
                    println(sectionInfo);
                    break;
                    
                case menuValue:
                    return;
                    
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
                            String[] inputContents = input.split(" +");
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
            
            println();
        }
    }
    
    /**
     */
    private static String getSectionChoicesString(Section section, String randomValue, String infoValue, String menuValue) {
        
        String actionSentence =
            String.format(
                "%s to %s",
                section.getActionSentenceEnding()
                section.isSingleInputSection() ? "A number" : "2 space-separated numbers",
            );
        String intChoice = actionSentence + ' ' + section.getInputInfoSentences();
        
        String randomChoice =
            String.format(
                "(%s) to generate %s and %s",
                randomValue,
                section.getActionSentenceEnding()
                section.isSingleInputSection() ? "a random number" : "2 random numbers",
            );
        
        String infoChoice =
            String.format("(%s) to get info about %s.", infoValue, section.getCliInfoOptionEnding());
        String menuChoice = String.format("(%s) to go to the menu.", menuValue);
        
        Stream<String> lines =
            Stream.of(
                "Type one of the following and press Enter:",
                intChoice,
                randomChoice,
                infoChoice,
                menuChoice
            );
        
        return buildStringWithStreamElementsOnSeparateLines(lines);
    }
    
    private static String buildStringWithHeadingAndInfoParagraphs(
        String headingStart, Stream<String> infoParagraphsStream
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
     * possibly replacing some space chars. If the indent param is true then 4 indentation spaces
     * will be inserted after each new line char. Each portion of the returned string between new
     * line chars will be at most 90 chars long.
     *
     * If there are any "words" that are longer than 90 characters then an IndexOutOfBoundsException
     * will be thrown when calling `lines.add(s.subSequence(lineStartIndex, spaceIndex));`.
     */
    public static String putNewLineChars(String s, boolean indent) {
        var maxLineLength = 90;
        if (s.length() <= maxLineLength) return s;
        var indentLength = 4;
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
     * false is provided as the 2nd arg so that no indenting is done.
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
        String spaceSeparator = getSpace(4);
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
            if (curLine.length() + spaceSeparator.length() + curString.length() > maxLineLength) {
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
        return
            Stream.concat(Stream.ofNullable(heading), stream)
            .map(s -> putNewLineChars(s, true))
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
    public static String getRowFor2ColumnTable(String column1Contents, int column1Width, String column2Contents) {
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
