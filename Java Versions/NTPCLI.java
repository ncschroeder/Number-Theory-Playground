package com.nicholasschroeder.numbertheoryplayground;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Launcher for the Number Theory Playground Command Line Interface.
 */
public class NTPCLI {
    public static void main(String[] args) {
        new NTPCLI();
    }

    Scanner inputReader = new Scanner(System.in);

    NTPCLI() {
        println("\nWelcome to the command line version of the Number Theory Playground\n");
        Section.initializeAllInfoOptionEndings();

        // Setup and show main menu
        StringJoiner menuLinesJoiner =
            new StringJoiner("\n")
            .add("You're at the main menu. Which section would you like to go to?");

        // Keys are the stringified ints that a user can type to go to a section and values
        // are the corresponding sections
        Map<String, Section> sectionInputValues = new HashMap<>();

        int i = 1;
        Section[] sections = Section.values();
        boolean indentForSingleDigits = sections.length >= 9;
        for (Section section : sections) {
            // Ensure the the info option ending was initialized and don't include it in the program if it wasn't
            if (section.getInfoOptionEndingForCli() == null) {
                logError(String.format("The info option ending for section %s wasn't initialized", section));
                continue;
            }

            String maybeIndent = indentForSingleDigits && i < 10 ? " " : "";
            menuLinesJoiner.add(String.format("%s(%d) %s", maybeIndent, i, section.getHeadingText()));
            sectionInputValues.put(String.valueOf(i), section);
            i++;
        }

        String exitValue = String.valueOf(i);
        String menuString =
            menuLinesJoiner
            .add(String.format("(%s) Exit", exitValue))
            .add("Enter your choice: ")
            .toString();

        while (true) {
            print(menuString);

            String input = getFormattedInput();
            if (input.equals(exitValue)) {
                println("\nI hope you found this interesting");
                return;
            }

            Section sectionToGoTo = sectionInputValues.get(input);
            if (sectionToGoTo == null) {
                println("\nInvalid input\n");
            } else {
                goToSection(sectionToGoTo);
            }
        }
    }

    /**
     * Lets the user type something and after they do and hit enter, a trimmed and all-lowercase version
     * of what they typed gets returned.
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

    void goToSection(Section section) {
        final String randomKey = "r";
        final String infoKey = "i";
        final String menuKey = "m";
        String sectionChoicesString = getSectionChoicesString(section, randomKey, infoKey, menuKey);
        String sectionInfo =
            section.getInfo()
            .stream()
            .map(this::insertNewLines)
            .collect(Collectors.joining("\n\n"));

        println();

        while (true) {
            println(sectionChoicesString);
            String input = getFormattedInput();
            switch (input) {
                case infoKey:
                    println();
                    println(sectionInfo);
                    println();
                    break;

                case menuKey:
                    println();
                    return;

                default:
                    int inputInt1 = 0;
                    int inputInt2 = 0;
                    boolean inputError = false;
                    boolean useRandom = input.equals(randomKey);

                    if (useRandom) {
                        inputInt1 = section.getRandomValidInt();
                        if (section.needs2Ints()) {
                            inputInt2 = section.getRandomValidInt();
                        }
                    } else {
                        if (section.needs2Ints()) {
                            String[] inputContents = input.split(" +");
                            if (inputContents.length != 2) {
                                inputError = true;
                            } else {
                                try {
                                    inputInt1 = Integer.parseInt(inputContents[0]);
                                    inputInt2 = Integer.parseInt(inputContents[1]);
                                } catch (NumberFormatException ex) {
                                    inputError = true;
                                }
                            }
                        } else {
                            try {
                                inputInt1 = Integer.parseInt(input);
                            } catch (NumberFormatException ex) {
                                inputError = true;
                            }
                        }
                    }

                    println();

                    if (!inputError) {
                        try {
                            println(getAnswerString(section, inputInt1, inputInt2));
                        } catch (IllegalArgumentException e) {
                            if (useRandom) {
                                logError(
                                    String.format(
                                        "Random number(s) generated for %s (%d, %d) resulted in an " +
                                            "illegal argument exception",
                                        section,
                                        inputInt1,
                                        inputInt2
                                    )
                                );
                            }
                            inputError = true;
                        }
                    }

                    if (inputError) {
                        println("Invalid input");
                    }
                    println();
            }
        }
    }

    /**
     * @return A string that says what section the user is at and what inputs a user can provide for the section and
     * what each input does.
     */
    String getSectionChoicesString(Section section, final String randomKey, final String infoKey, final String menuKey) {
        boolean twoIntsNeeded = section.needs2Ints();

        String intChoice =
            String.format(
                "%s to %s",
                twoIntsNeeded ? "2 space-separated integers" : "An integer",
                section.getActionSentenceEndingAndInputConstraintsSentence()
            );
        intChoice = insertNewLines(intChoice, true);

        String randomChoice =
            String.format(
                "(%s) to generate %s and %s",
                randomKey,
                twoIntsNeeded ? "2 random integers" : "a random integer",
                section.getActionSentenceEnding()
            );
        randomChoice = insertNewLines(randomChoice, true);

        String infoChoice = String.format("(%s) to get info about %s", infoKey, section.getInfoOptionEndingForCli());
        String menuChoice = String.format("(%s) to go to the menu", menuKey);

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
     * @return A new string that is the same as the argument string but with new line characters inserted.
     * Each portion of the string between new line characters will be at most 90 characters long. A new line
     * character will not be inserted in the middle of a word but instead will replace whitespace. If
     * there are any "words" that are more than 90 characters long then the argument string will be returned.
     */
    String insertNewLines(String s, boolean indent) {
        final int lineLength = 90;
        StringBuilder stringRemainingSb = new StringBuilder(s);
        StringBuilder stringBuiltSb = new StringBuilder(s.length());

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
     *
     * @param s
     * @return The result of calling the other insertNewLines and passing in false as the 2nd arg.
     */
    String insertNewLines(String s) {
        return insertNewLines(s, false);
    }

    /**
     *
     * @param length
     * @return A chunk of whitespace that is as long as the length arg
     * @throws IllegalArgumentException if length is negative
     */
    String getWhiteSpace(int length) {
        return " ".repeat(length);
    }

    /**
     *
     * @param strings
     * @param prefix
     * @return A string that consists of lines that consist of the elements of the list provided separated
     * by space. Each line is at most 75 characters long. If a non-blank prefix is provided, then it will be
     * placed above the lines of elements (the beginning of the string returned will be the prefix followed by
     * a new line character).
     */
    String stringifyList(List<String> strings, String prefix) {
        final int spaceSeparatorWidth = 4;
        String spaceSeparator = getWhiteSpace(spaceSeparatorWidth);
        StringJoiner spacesJoiner = new StringJoiner(spaceSeparator);

        StringJoiner linesJoiner = new StringJoiner("\n");
        if (!prefix.isBlank()) {
            linesJoiner.add(prefix);
        }

        for (String s : strings) {
            int spacesJoinerLength = spacesJoiner.length();
            if (spacesJoinerLength + spaceSeparatorWidth + s.length() > 75 && spacesJoinerLength > 0) {
                linesJoiner.add(spacesJoiner.toString());
                spacesJoiner = new StringJoiner(spaceSeparator);
            }
            spacesJoiner.add(s);
        }

        return
            linesJoiner
            .add(spacesJoiner.toString())
            .toString();
    }

    /**
     *
     * @param section
     * @param inputInt1 1st input.
     * @param inputInt2 2nd input. This isn't used for sections that only need 1 input.
     * @return A string that gives the answer for the given section and input(s).
     * @throws IllegalArgumentException if the input int(s) is/are invalid for the section provided.
     */
    String getAnswerString(Section section, int inputInt1, int inputInt2) {
        String int1String = getLongStringWithCommas(inputInt1);
        String int2String = getLongStringWithCommas(inputInt2);
        switch (section) {
            case PRIMES:
                return stringifyList(getPrimesStrings(inputInt1), getPrimesLabel(int1String));

            case TWIN_PRIMES:
                return stringifyList(getTwinPrimePairStrings(inputInt1), getTwinPrimesLabel(int1String));

            case PRIME_FACTORIZATION:
                return PrimeFactorization.getPfInfoString(inputInt1, int1String);

            case DIVISIBILITY:
                return getDivisibilityAnswer(inputInt1, int1String);

            case GCD_AND_LCM:
                return String.join(
                    "\n\n",
                    getGcdAndLcmMainLabel(int1String, int2String),
                    getEuclideanAnswer(inputInt1, int1String, inputInt2, int2String),
                    getGcdAndLcmViaPfAnswer(inputInt1, int1String, inputInt2, int2String)
                );

            case GOLDBACH: {
                List<String> pairStrings = getGoldbachPrimePairStrings(inputInt1);
                return stringifyList(pairStrings, getGoldbachLabel(pairStrings.size(), int1String));
            }

            case PYTHAG_TRIPLES:
                return getPythagoreanTriplesAnswer(inputInt1, int1String);

            default:
                String errorMessage = String.format("Section \"%s\" not found", section);
                logError(errorMessage);
                return errorMessage;
        }
    }

    /**
     *
     * @param inputInt
     * @param intString
     * @return A string that shows a paragraph of divisibility info acquired by using special tricks followed by a
     * section of information about divisibility info relating to prime factorizations.
     * @throws IllegalArgumentException if inputInt is invalid input for the DIVISIBILITY section.
     */
    String getDivisibilityAnswer(int inputInt, String intString) {
        String tricksInfo = getDivisibilityInfoViaTricks(inputInt);
        tricksInfo = insertNewLines(tricksInfo);

        StringJoiner linesJoiner =
            new StringJoiner("\n")
            .add(getDivisibilityMainLabel(intString))
            .add("")
            .add(divisibilityTricksInfoLabel)
            .add(tricksInfo)
            .add("")
            .add(PrimeFactorization.divisibilityInfoLabel);

        PrimeFactorization pf = new PrimeFactorization(inputInt);
        String pfInfoMessage = pf.getInfoMessage();
        if (pf.isForAPrimeNumber()) {
            return
                linesJoiner
                .add(insertNewLines(pfInfoMessage + ". " + PrimeFactorization.getPrimeNumberLabel(intString)))
                .toString();
        }

        String factorsListPrefix =
            pfInfoMessage + ". " + pf.getNumberOfFactorsInfo() + " " + PrimeFactorization.subfactorizationsLabel;
        factorsListPrefix = insertNewLines(factorsListPrefix);
        String factorsListWithPrefix =
            stringifyList(PrimeFactorization.getFactorPfStrings(inputInt), factorsListPrefix);

        return linesJoiner.add(factorsListWithPrefix).toString();
    }

    String getRowFor2ColumnTable(String column1Contents, int column1Width, String column2Contents) {
        int column1EndGap = column1Width - column1Contents.length();
        if (column1EndGap < 1) {
            logError("");
            column1EndGap = 1;
        }

        return column1Contents + getWhiteSpace(column1EndGap) + column2Contents;
    }

    String getRowFor3ColumnTable(
        String column1Contents,
        int column1Width,
        String column2Contents,
        int column2Width,
        String column3Contents
    ) {
        String first2Columns = getRowFor2ColumnTable(column1Contents, column1Width, column2Contents);

        int column2EndGap = column2Width - column2Contents.length();
        if (column2Width < 1) {
            logError("");
            column2EndGap = 1;
        }

        return first2Columns + getWhiteSpace(column2EndGap) + column3Contents;
    }

    /**
     *
     * @param inputInt1
     * @param int1String
     * @param inputInt2
     * @param int2String
     * @return A string that shows a table with info for all iterations of the Euclidean algorithm performed
     * on inputInt1 and inputInt2, along with a heading for the table and a message below the table about what
     * the GCD is.
     * @throws IllegalArgumentException if inputInt1 or inputInt2 are invalid for the GCD_AND_LCM section.
     */
    String getEuclideanAnswer(int inputInt1, String int1String, int inputInt2, String int2String) {
        List<EuclideanIteration> euclideanIterations = getEuclideanIterations(inputInt1, inputInt2);

        // The gap between the end of the longest item in a column and the item in the next column
        final int columnGap = 4;

        // Make column widths equal to the length of the longest element in the column + the column gap.
        // The 1st iteration will have the longest elements of all iterations.
        EuclideanIteration iteration1 = euclideanIterations.get(0);
        int maxColumnWidth =
            Math.max(euclideanMaxColumnHeading.length(), iteration1.getMaxString().length()) +
            columnGap;

        int minColumnWidth =
            Math.max(euclideanMinColumnHeading.length(), iteration1.getMinString().length()) +
            columnGap;

        String headRow =
            getRowFor3ColumnTable(euclideanMaxColumnHeading, maxColumnWidth, euclideanMinColumnHeading, minColumnWidth, euclideanRemainderColumnHeading);

        String prefix = euclideanHeading + "\n" + headRow + "\n";
        String suffix = "\n" + getEuclideanGcdMessage(int1String, int2String, euclideanIterations);

        // Create table
        return
            euclideanIterations
            .stream()
            .map(ei ->
                getRowFor3ColumnTable(ei.getMaxString(), maxColumnWidth, ei.getMinString(), minColumnWidth, ei.getRemainderString())
            )
            .collect(Collectors.joining("\n", prefix, suffix));
    }

    /**
     *
     * @param inputInt1
     * @param int1String
     * @param inputInt2
     * @param int2String
     * @return A string that shows a heading followed by a table where 1 column is for an integer and another column is for the corresponding
     * prime factorization. There are rows for inputInt1, inputInt2, the GCD of these, and the LCM of these. For the GCD
     * and LCM rows, there's also a column that says "GCD" or "LCM".
     * @throws IllegalArgumentException if inputInt1 or inputInt2 are invalid inputs for the PRIME_FACTORIZATION section.
     */
    String getGcdAndLcmViaPfAnswer(int inputInt1, String int1String, int inputInt2, String int2String) {
        PrimeFactorization.GcdAndLcmInfo info = new PrimeFactorization.GcdAndLcmInfo(inputInt1, inputInt2);
        int column1Width = 6; // Only contents of this column are 1 row that says "GCD" and another row that says "LCM"

        // Make column 2 as wide as the longest element in that column + 3
        int column2Width =
            Stream.of(gcdAndLcmPfNumberHeading, int1String, int2String, info.gcdString, info.lcmString)
            .mapToInt(String::length)
            .max()
            .orElseThrow()
            + 3;

        // Create table
        return String.join(
            "\n",
            PrimeFactorization.gcdAndLcmInfoLabel,
            getRowFor3ColumnTable("", column1Width, gcdAndLcmPfNumberHeading, column2Width, gcdAndLcmPfHeading),
            getRowFor3ColumnTable("", column1Width, int1String, column2Width, info.int1PfString),
            getRowFor3ColumnTable("", column1Width, int2String, column2Width, info.int2PfString),
            getRowFor3ColumnTable("GCD", column1Width, info.gcdString, column2Width, info.gcdPfString),
            getRowFor3ColumnTable("LCM", column1Width, info.lcmString, column2Width, info.lcmPfString)
        );
    }

    /**
     *
     * @param inputInt
     * @param intString
     * @return A string that says some Pythagorean triples that come after inputInt, with each triple on its own line,
     * as well as a message at the top.
     * @throws IllegalArgumentException if inputInt is invalid input for the PYTHAG_TRIPLES section.
     */
    String getPythagoreanTriplesAnswer(int inputInt, String intString) {
        StringJoiner lines =
            new StringJoiner("\n")
            .add(getPythagoreanTriplesLabel(intString));

        int i = 1;
        for (String triple : getPythagTripleStrings(inputInt)) {
            lines.add(i + ") " + triple);
            i++;
        }

        return lines.toString();
    }
    }
}
