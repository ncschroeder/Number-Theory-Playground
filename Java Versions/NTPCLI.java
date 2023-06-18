package com.nicholasschroeder.numbertheoryplayground;

import java.util.HashMap;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Launcher for the Number Theory Playground Command Line Interface.
 */
public class NTPCLI {
    public static void main(String[] args) {
        new NTPCLI();
    }
        
    Scanner inputReader = new Scanner(System.in);
    
    /**
     * Lets the user type something and after they do and hit enter, a trimmed and all-lowercase version
     * of what they typed gets returned
     */
    String getFormattedInput() {
        return inputReader.nextLine().trim().toLowerCase();
    }
    
    void print(String s) {
        System.out.print(s);
    }
    
    void println(String s) {
        System.out.println(s);
    }
    
    void println() {
        System.out.println();
    }
    
    void printInvalidInput() {
        println("Invalid input");
    }

    NTPCLI() {
        println("\nWelcome to the command line version of the Number Theory Playground\n");
        
        /*
        Setup and show main menu. There are currently 10 sections and there'll be options to go to each
        of these and an exit option. Each option will have a number in the range of 1-11. This number will
        be at the start of the line for an option. There'll be a 1-space indent for the lines that start
        with a single digit.
        */
        
        StringJoiner menuLinesJoiner =
            new StringJoiner("\n")
            .add("You're at the main menu. Which section would you like to go to?");
    
        // Let sectionMap be a Map where the keys are the stringified numbers that a user can
        // type to go to a section and the values are the corresponding Sections
        var sectionMap = new HashMap<String, Section>();
    
        int i = 1;
        for (Section section : Section.createInstances()) {
            menuLinesJoiner.add(String.format("%s(%d) %s", i < 10 ? " " : "", i, section.getHeadingText()));
            sectionMap.put(String.valueOf(i), section);
            i++;
        }
    
        String exitKey = String.valueOf(i);
        String menuString =
            menuLinesJoiner
            .add(String.format("(%s) Exit", exitKey))
            .add("Enter your choice: ")
            .toString();
    
        while (true) {
            print(menuString);
        
            String input = getFormattedInput();
            if (input.equals(exitKey)) {
                println("\nI hope you found this interesting");
                return;
            }
        
            Section sectionToGoTo = sectionMap.get(input);
            if (sectionToGoTo == null) {
                println();
                printInvalidInput();
                println();
            } else {
                goToSection(sectionToGoTo);
            }
        }
    }
    
    /**
     * Displays the choices for the section and lets the user either enter custom input to use for the
     * calculation of the section, generate random input to use for the calculation, get info about the
     * section, or go bock to the main menu.
     */
    void goToSection(Section section) {
        final String randomKey = "r";
        final String infoKey = "i";
        final String menuKey = "m";
        String sectionChoicesString = getSectionChoicesString(section, randomKey, infoKey, menuKey);
        
        // Let sectionInfo contain the paragraphs of section info with new lines inserted into the
        // paragraphs and with each paragraph separated by a blank line
        String sectionInfo =
            section.getInfo().stream()
            .map(NTPCLI::insertNewLines)
            .collect(Collectors.joining("\n\n"));
        
        println();
    
        while (true) {
            println(sectionChoicesString);
            String input = getFormattedInput();
            println();
            switch (input) {
                case infoKey:
                    println(sectionInfo);
                    break;
    
                case menuKey:
                    return;
    
                case randomKey:
                    println(section.getRandomCliAnswer());
                    break;
    
                default:
                    try {
                        if (section.isSingleInputSection()) {
                            int inputInt = Integer.parseInt(input);
                            println(((SingleInputSection)section).getCliAnswer(inputInt));
                        } else {
                            String[] inputContents = input.split(" +");
                            if (inputContents.length != 2) {
                                printInvalidInput();
                            } else {
                                int inputInt1 = Integer.parseInt(inputContents[0]);
                                int inputInt2 = Integer.parseInt(inputContents[1]);
                                println(((DoubleInputSection)section).getCliAnswer(inputInt1, inputInt2));
                            }
                        }
                    } catch (IllegalArgumentException ex) {
                        printInvalidInput();
                    }
            }
            println();
        }
    }

    /**
     * Returns a string that says what section the user is at and what inputs a user can provide for the
     * section and what each input does.
     *
     * randomKey is what the user should type to generate a random int or ints and apply this to the
     * algorithm of the section.
     * infoKey is what the user should type to get info about this section.
     * menuKey is what the user should type to go back to the menu.
     */
    String getSectionChoicesString(Section section, final String randomKey, final String infoKey, final String menuKey) {
        boolean oneIntNeeded = section.isSingleInputSection();
        
        String intChoice =
            String.format(
                "%s to %s. %s.",
                oneIntNeeded ? "An integer" : "2 space-separated integers",
                section.getActionSentenceEnding(),
                section.getInputConstraintsSentence()
            );
        intChoice = insertNewLines(intChoice, true);
    
        String randomChoice =
            String.format(
                "(%s) to generate %s and %s.",
                randomKey,
                oneIntNeeded ? "a random integer" : "2 random integers",
                section.getActionSentenceEnding()
            );
        randomChoice = insertNewLines(randomChoice, true);
    
        String infoChoice = String.format("(%s) to get info about %s.", infoKey, section.getCliInfoOptionEnding());
        String menuChoice = String.format("(%s) to go to the menu.", menuKey);
        
        return String.join(
            "\n",
            section.getHeadingText(),
            "",
            "Type one of the following and press enter:",
            intChoice,
            randomChoice,
            infoChoice,
            menuChoice
        );
    }
        
    /**
     * Returns a new string that has the same content as the param string but with new line characters
     * inserted. If the indent param is true then 4 indentation spaces will also be inserted after the
     * new line characters, so there is an indent on the 2nd and subsequent lines. Each portion of the
     * string between new line characters will be at most 90 characters long. A new line character will
     * not be inserted in the middle of a word but instead will replace whitespace. If there are any
     * "words" that are longer than 90 characters then the argument string will be returned.
     */
    public static String insertNewLines(String s, boolean indent) {
        final int lineLength = 90;
        
        var stringRemainingSb = new StringBuilder(s);
        var stringBuiltSb = new StringBuilder(s.length());
        
        while (stringRemainingSb.length() > lineLength) {
            int whiteSpaceIndex = stringRemainingSb.lastIndexOf(" ", lineLength);
            if (whiteSpaceIndex == -1) {
                logError(String.format("String provided has a \"word\" >= %d characters", lineLength));
                return s;
            }

            CharSequence subSequence = stringRemainingSb.subSequence(0, whiteSpaceIndex);
            stringBuiltSb.append(subSequence).append("\n");            
            stringRemainingSb.delete(0, whiteSpaceIndex + 1);
            
            if (indent) {
                stringRemainingSb.insert(0, getWhiteSpace(4));
            }
        }
        
        return stringBuiltSb.append(stringRemainingSb).toString();
    }
    
    /**
     * false is provided as the 2nd arg so that no indenting is done.
     */
    public static String insertNewLines(String s) {
        return insertNewLines(s, false);
    }

    /**
     * Returns a string that consists of the heading on its own line followed by lines that consist of
     * the elements of the Stream provided separated by space. Each line is at most 75 characters long.
     * The exception to this is that if an element of the Stream is more than 75 characters long then it
     * will be placed on a line by itself.
     */
    public static String stringifyList(String heading, Stream<String> strings) {
        final int spaceSeparatorWidth = 4;
        final String spaceSeparator = getWhiteSpace(spaceSeparatorWidth);
        
        // Use AtomicReference to allow for reassignability in a lambda
        var spacesJoinerRef =
            new AtomicReference<StringJoiner>(new StringJoiner(spaceSeparator));
        var linesJoiner = new StringJoiner("\n").add(heading);
            
        strings.forEachOrdered(s -> {
            int spacesJoinerLength = spacesJoinerRef.get().length();
            if (spacesJoinerLength + spaceSeparatorWidth + s.length() > 75 && spacesJoinerLength > 0) {
                linesJoiner.add(spacesJoinerRef.get().toString());
                spacesJoinerRef.set(new StringJoiner(spaceSeparator));
            }
            spacesJoinerRef.get().add(s);
        });
        
        return
            linesJoiner
            .add(spacesJoinerRef.get().toString())
            .toString();
    }

    /**
     * Returns a string that consists of column1Contents followed by whitespace followed by
     * column2Contents. The length of column1Contents and the whitespace will be equal to
     * column1Width. column1Width should be > the length of column1Contents.
     */
    public static String getRowFor2ColumnTable(String column1Contents, int column1Width, String column2Contents) {
        int column1EndGap = column1Width - column1Contents.length();
        if (column1EndGap < 1) {
            logError("column1Width wasn't > the length of column1Contents");
            column1EndGap = 1;
        }
        return column1Contents + getWhiteSpace(column1EndGap) + column2Contents;
    }
    
    /**
     * Returns a string that consists of column1Contents followed by whitespace followed by column2Contents
     * followed by whitespace followed by column3Contents. The length of column1Contents and the following
     * whitespace will be equal to column1Width. The length of column2Contents and the following whitespace
     * will be equal to column2Width. column1Width should be > the length of column1Contents and column2Width
     * should be > the length of column2Contents.
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
            logError("column2Width wasn't > than the length of column2Contents");
            column2EndGap = 1;
        }
        return first2Columns + getWhiteSpace(column2EndGap) + column3Contents;
    }
}