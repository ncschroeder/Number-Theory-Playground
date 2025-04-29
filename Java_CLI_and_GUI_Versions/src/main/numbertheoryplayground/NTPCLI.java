package numbertheoryplayground;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import numbertheoryplayground.sectionclasses.abstract_.*;
import numbertheoryplayground.sectionclasses.outer.GoldbachConjecture;

import static numbertheoryplayground.Misc.*;

/**
 * Class with code related to the Number Theory Playground Command Line Interface, including the
 * code for running the application and some static methods. The static methods are used in this
 * class and by nested Section classes for implementing getCliAnswer.
 */
public class NtpCli {
    // Field and short instance methods

    private static Scanner inputReader;

//    /**
//     * Lets the user type something and after they do and hit Enter, a trimmed and all-lowercase version
//     * of what they typed gets returned.
//     */
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
//        System.out.println(4.0 / 3);
//        System.out.println(BigDecimal.valueOf(4).divide(BigDecimal.valueOf(3), MathContext.DECIMAL64));
//        if (true) return;
        inputReader = new Scanner(System.in);
        GoldbachConjecture.setMaxInputForCli();
        
        /*
        Setup and show main menu. There'll be options to go to the sections and an exit option.
        There are currently 10 sections so the ints 1-10 will be what the user can type to go to
        a section. "e" can be typed to exit. The ints and "e" will be at the start of the lines
        for the options. There'll be a 1-space indent for the lines that start with a single
        digit or "e".

        Let inputsAndSections be a map where the keys are the string representations of the
        aforementioned ints and the values are the corresponding Section objects.
         */
        
        //        StringJoiner menuLinesJoiner =
//            new StringJoiner("\n")
//            .add("You're at the main menu. Which section would you like to go to?");
        
        var inputsAndSections = new HashMap<String, Section>();
        var exitValue = "e";
        String menuString;
        
        {
            var menuLines = new ArrayList<String>();
            menuLines.add("You're at the main menu. Which section would you like to go to?");
            var inputForSection = 1;
            
            for (var section : Section.createInstances()) {
                var maybeIndent = inputForSection < 10 ? " " : "";
                menuLines.add(String.format("%s(%d) %s", maybeIndent, inputForSection, section.getHeading()));
                inputsAndSections.put(Integer.toString(inputForSection++), section);
            }

//            exitValue = Integer.toString(i);
            menuLines.add(String.format(" (%s) Exit", exitValue));
            menuLines.add("Enter your choice: ");
            menuString = String.join("\n", menuLines);
        }
        
        println("\nWelcome to the command line version of the Number Theory Playground\n");
        
        while (true) {
            print(menuString);
            String input = getFormattedInput();
            if (input.equals(exitValue)) {
                println("\nI hope you found this interesting");
                return;
            }

//            Optional.ofNullable(inputsAndSections.get(input))
//            .ifPresentOrElse(
//                NtpCli::goToSection,
//                () -> {
//                    println();
//                    printInvalidInput();
//                    println();
//                }
//            );
            
            var sectionToGoTo = inputsAndSections.get(input);
            if (sectionToGoTo != null) {
                goToSection(sectionToGoTo);
            } else {
                println();
                printInvalidInput();
                println();
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
            getSectionChoicesString(section, randomValue, infoValue, menuValue);
        
        /*
        Let sectionInfo contain the paragraphs of section info with new lines inserted into the
        paragraphs and with each paragraph separated by a blank line.
         */
        String sectionInfo =
            section
            .getInfoParagraphs()
            .stream()
            .map(NtpCli::putNewLineChars)
            .collect(Collectors.joining("\n\n"));
        
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
                            println(sis.getCliAnswer(inputLong));
                        } else if (section instanceof DoubleInputSection dis) {
                            String[] inputContents = input.split(" +");
                            if (inputContents.length != 2) {
                                printInvalidInput();
                                // share code with illegal argument exception block?
                            } else {
                                var inputLong1 = stripCommasAndParse(inputContents[0]);
                                var inputLong2 = stripCommasAndParse(inputContents[1]);
                                println(dis.getCliAnswer(inputLong1, inputLong2));
                            }
                        }
                    } catch (NumberFormatException | InvalidInputNumberException ex) {
                        printInvalidInput();
                    }
                    
//                    if (section.needs1Int()) {
//                        try {
//                            println(getAnswerString(section, Integer.parseInt(input)));
//                        } catch (NumberFormatException ex) {
//                            printInvalidInput();
//                        }
//                    } else {
//                        String[] inputContents = input.split(" +");
//                        if (inputContents.length != 2) {
//                            printInvalidInput();
//                        } else {
//                            try {
//                                int inputInt1 = Integer.parseInt(inputContents[0]);
//                                int inputInt2 = Integer.parseInt(inputContents[1]);
//                                println(getDoubleInputAnswerString(section, inputInt1, inputInt2));
//                            } catch (NumberFormatException ex) {
//                                printInvalidInput();
//                            }
//                        }
//                    }
            }
            
            println();

//                    int inputInt1 = 0;
//                    int inputInt2 = 0;
//                    boolean inputError = false;
//                    boolean useRandom = input.equals(randomKey);
//
//                    if (useRandom) {
//                        // Generate random numbers that are in the valid range
//                        inputInt1 = section.getRandomValidInt();
//                        if (section.needs2Ints()) {
//                            inputInt2 = section.getRandomValidInt();
//                        }
//                    } else {
//                        // Check input for number or numbers and validate that these numbers are in the appropriate range
//                        if (section.needs2Ints()) {
////                        if (section == Section.GCD_LCM) {
//                            // GCD and LCM section has user enter 2 numbers separated by space
//                            String[] inputContents = input.split(" +");
//                            if (inputContents.length != 2) {
//                                inputError = true;
//                            } else {
//                                try {
//                                    inputInt1 = Integer.parseInt(inputContents[0]);
//                                    inputInt2 = Integer.parseInt(inputContents[1]);
////                                    if (int2 < minInputInt || int2 > maxInputInt) {
////                                        inputError = true;
////                                    }
//                                } catch (NumberFormatException ex) {
//                                    inputError = true;
//                                }
//                            }
//                        } else {
//                            try {
//                                inputInt1 = Integer.parseInt(input);
//                            } catch (NumberFormatException ex) {
//                                inputError = true;
//                            }
//                        }
//                    }
//
//                    // Goldbach section requires even number
////                        if (int1 < minInputInt || int1 > maxInputInt ||
////                                (section == Section.GOLDBACH && int1 % 2 != 0)) {
////                            inputError = true;
////                        }
//
////                        try {
////                            if (section == Section.GCD_LCM) {
////                                // GCD and LCM section has user enter 2 numbers separated by a space
////                                if (input.isEmpty()) {
////                                    inputError = true;
////                                } else {
////                                    String[] inputContents = input.split(" +");
////                                    if (inputContents.length != 2) {
////                                        inputError = true;
////                                    } else {
////                                        int1 = Integer.parseInt(inputContents[0]);
////                                        int2 = Integer.parseInt(inputContents[1]);
////                                        if (int2 < minInputInt || int2 > maxInputInt) {
////                                            inputError = true;
////                                        }
////                                    }
////                                }
////                            } else {
////                                int1 = Integer.parseInt(input);
////                            }
////
////                            // Goldbach section requires even number
////                            if (int1 < minInputInt || int1 > maxInputInt ||
////                                    (section == Section.GOLDBACH && int1 % 2 != 0)) {
////                                inputError = true;
////                            }
////                        } catch (NumberFormatException ex) {
////                            // This block is reached if the user's input was not able to be parsed as an int
////                            inputError = true;
////                        }
//
////                        if (inputError) {
////                            System.out.println("\nInvalid input\n");
////                            continue;
////                        }
////                    }
//
//                    println();
//                    if (!inputError) {
//                        try {
////                            println(getAnswerString(section, inputInt1, inputInt2));
//                        } catch (IllegalArgumentException e) {
//                            if (useRandom) {
//                                logError(
//                                    String.format(
//                                        "Random number(s) generated for %s (%d, %d) resulted in an " +
//                                        "illegal argument exception",
//                                        section,
//                                        inputInt1,
//                                        inputInt2
//                                    )
////                                    "Random number(s) generated for " + section +
////                                    " (" + inputInt1 + ", " + inputInt2 + ") resulted " +
////                                    "in an illegal argument exception"
//                                );
//                            }
//                            inputError = true;
//                        }
//                    }
//
//                    if (inputError) {
////                            System.out.println("\nInvalid input\n");
//                        println("Invalid input");
//                    }
//                    println();
//            }
        }
    }
    
    /**
//     * ~~Returns a string that says what section the user is at and what inputs a user can provide for
//     * the section and what each input does.
//     *
//     * randomValue is what the user should type to generate a random int or ints and apply this to the
//     * algorithm of the section.
//     * infoValue is what the user should type to get info about this section.
//     * menuValue is what the user should type to go back to the menu.~~
     */
    private static String getSectionChoicesString(Section section, String randomValue, String infoValue, String menuValue) {
        boolean oneIntegerNeeded = section.isSingleInputSection();
        
        String actionSentence =
            String.format(
                "%s to %s",
                oneIntegerNeeded ? "An integer" : "2 space-separated integers",
                section.getActionSentenceEnding()
            );
        String intChoice = actionSentence + ' ' + section.getInputInfoSentences();
        
        String randomChoice =
            String.format(
                "(%s) to generate %s and %s",
                randomValue,
                oneIntegerNeeded ? "a random integer" : "2 random integers",
                section.getActionSentenceEnding()
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

//        return String.join(
//            "\n",
//            section.getHeading(),
//            "",
//            "Type one of the following and press Enter:",
//            intChoice,
//            randomChoice,
//            infoChoice,
//            menuChoice
//        );
    }
    
    
    /**
     * Returns a new string that's mostly the same as the param string, but with new line chars
     * possibly replacing some space chars. If the indent param is true then 4 indentation spaces
     * will be inserted after each new line char. Each portion of the returned string between new
     * line chars will be at most 90 chars long.
     *
     * If there are any "words" that are longer than 90 characters then an IndexOutOfBoundsException
     * will be thrown when calling `lines.add(s.subSequence(lineStartIndex, spaceIndex));`.
     *
//     * the argument string will be returned.
//     * so that there's an indent on the 2nd and subsequent lines.
//     * A new line char won't be inserted in the middle of a word but instead will replace a space.
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

//            if (spaceIndex < lineStartIndex) {
//                printError(String.format("String provided has a \"word\" >= %d characters", maxLineLength));
//                return s;
//            }

            lines.add(s.subSequence(lineStartIndex, spaceIndex));
            lineStartIndex = spaceIndex + 1;
        }
        
        String separator = indent ? '\n' + getSpace(indentLength) : "\n";
        return String.join(separator, lines);
    }
    
//        // TODO: check this
//        var stringRemainingSb = new StringBuilder(s); // pass in s.trim()?
//        var stringBuiltSb = new StringBuilder(s.length()); // maybe use string joiner
//
//        while (stringRemainingSb.length() > maxLineLength) {
//            int whiteSpaceIndex = stringRemainingSb.lastIndexOf(" ", maxLineLength);
//
//            if (whiteSpaceIndex == -1) {
//                printError(String.format("String provided has a \"word\" >= %d characters", maxLineLength));
//                return s;
//            }
////            int subSequenceEndIndex = whiteSpaceIndex == -1 ? lineLength - 1 : whiteSpaceIndex - 1;
//            CharSequence subSequence = stringRemainingSb.subSequence(0, whiteSpaceIndex);
//            stringBuiltSb.append(subSequence).append("\n");
//
//            // Delete
//            stringRemainingSb.delete(0, whiteSpaceIndex + 1);
//
//            if (indent) {
//                stringRemainingSb.insert(0, getWhiteSpace(4));
//            }
//        }
//
//        return stringBuiltSb.append(stringRemainingSb).toString();
//    }
    
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
        //        var spaceSeparatorLength = 4;
        //new StringBuilder(maxLineLength);
        
//        StringJoiner linesJoiner = new StringJoiner("\n").add(heading);
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
        
//        strings.forEachOrdered(s -> {
//            if (curLine.length() == 0) {
//                // This block will only run on the 1st iteration.
//                curLine.append(s);
//            } else if (curLine.length() + spaceSeparatorLength + s.length() <= maxLineLength) {
//                curLine.append(spaceSeparator).append(s);
//            } else {
//                lines.add(new StringBuilder(curLine));
//                curLine.delete(0, curLine.length()).append(s);
//            }
//        });
        
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

//private static class ListStringifier {
//        public ListStringifier(String heading) {
//
//        }
//        public void add(String s) {
//
//        }
//
//        @Override
//        public String toString() {
//            return super.toString();
//        }
//    }
//
//    public static Collector<String, ListStringifier, String> getCollector(String heading) {
//        Collectors.joining("");
//        return Collector.of(
//            () -> new ListStringifier(heading),
//            ListStringifier::add,
//            (ls1, ls2) -> { throw new UnsupportedOperationException(); },
////                try {
////                    throw new UnsupportedOperationException();
////                } catch (Exception e) {
////                    throw new IllegalArgumentException(e);
////                }
////            },
//            ListStringifier::toString
//        );
//    }
    
//    /**
//     * @param section
//     * @return A string that gives the answer for the given single input section and input.
//     * @throws IllegalArgumentException if the input int(s) is/are invalid for the section provided.
//     */
//    String getAnswerString(OldSection section, int inputInt/*, int inputInt2*/) {
////        String int1String = getLongStringWithCommas(inputInt);
////        String int2String = getLongStringWithCommas(inputInt2);
//        // TODO try-catch?
//        switch (section) {
//            case PRIMES:
//                return stringifyList(Primes.getPrimesStrings(inputInt), Primes.getPrimesListHeading(inputInt));//int1String));
//
//            case TWIN_PRIMES:
//                return stringifyList(TwinPrimes.getTwinPrimePairStrings(inputInt), TwinPrimes.getTwinPrimesListHeading(inputInt));//int1String));
//
//            case PRIME_FACTORIZATION:
//                return PrimeFactorization.getPfInfoString(inputInt);//, int1String);
//
//            case DIVISIBILITY:
//                return getDivisibilityAnswer(inputInt);//, int1String);
//
//            case GOLDBACH: {
//                List<String> pairStrings = Goldbach.getGoldbachPrimePairStrings(inputInt);
//                return stringifyList(pairStrings, Goldbach.getGoldbachListHeading(pairStrings.size(), inputInt));//, int1String));
//            }
//
//            case PYTHAG_TRIPLES:
//                return getPythagoreanTriplesAnswer(inputInt);//, int1String);
//
//            case TWO_SQUARE_THEOREM:
//                // do error handling
//                return insertNewLines(TwoSquareTheoremInfo.getInfoString(inputInt));//, int1String));
//
////            case FIBONACCI_LIKE_SEQUENCES:
//////                return getFibonacciLikeSequencesAnswer(inputInt, int1String, inputInt2, int2String);
////                FibonacciLikeSequenceInfo info = new FibonacciLikeSequenceInfo(inputInt, inputInt2);
////                String prefix = FibonacciLikeSequenceInfo.getListHeading(inputInt, inputInt2);
////                return stringifyList(info.getStringSequence(), prefix) + "\n\n" + info.getEndRatioMessage();
//
//            default:
////                String errorMessage = String.format("Section \"%s\" not found", section);
//                //"Section \"" + section.name() + "\" not found";
//                logError(String.format("Section %s isn't recognized as a single input section", section));
//                return "";
//        }
//    }
    
//    /**
//     *
//     * @param section
//     * @return A string that gives the answer for the given double input section and inputs.
//     */
//    String getDoubleInputAnswerString(OldSection section, int inputInt1, int inputInt2) {
//        switch (section) {
//            case GCD_AND_LCM:
//                return String.join(
//                    "\n\n",
//                    GcdAndLcm.getGcdAndLcmInfoHeading(inputInt1, inputInt2),
//                    //                    getGcdAndLcmInfoHeading(int1String, int2String),
//                    //                    getEuclideanAnswer(inputInt1, int1String, inputInt2, int2String),
//                    getEuclideanAnswer(inputInt1, inputInt2),
//                    getGcdAndLcmViaPfAnswer(inputInt1, inputInt2)
////                    getGcdAndLcmViaPfAnswer(inputInt1, int1String, inputInt2, int2String)
//                );
//
//            case FIBONACCI_LIKE_SEQUENCES:
//                FibonacciLikeSequenceInfo info = new FibonacciLikeSequenceInfo(inputInt1, inputInt2);
//                String prefix = FibonacciLikeSequenceInfo.getFibonacciLikeSequencesListHeading(inputInt1, inputInt2);//FibonacciLikeSequenceInfo.getListHeading(inputInt1, inputInt2);
//                return stringifyList(info.getStringSequence(), prefix) + "\n\n" + info.getEndRatioMessage();
//
//            case ANCIENT_MULTIPLICATION:
//                return getAncientMultiplicationAnswer(inputInt1, inputInt2);
//
//            default:
//                logError(String.format("Section %s isn't recognized as a double input section", section));
//                return "";
//        }
//    }

//    String getPrimesAnswer(int inputInt, String intString) {
//        return
//            stringifyList(getPrimesStrings(inputInt), 0, 0, getPrimesLabel(intString));
//    }

//    String getTwinPrimesAnswer(int inputInt, String intString) {
//        return
//            stringifyList(getTwinPrimePairStrings(inputInt), 4, 60, getTwinPrimesLabel(intString));
//    }
    
//    /**
//     * @return A string that shows a paragraph of divisibility info acquired by using special tricks followed by a
//     * section of information about divisibility info relating to prime factorizations.
//     * @throws IllegalArgumentException if inputInt is invalid input for the DIVISIBILITY section.
//     */
////    String getDivisibilityAnswer(int inputInt, String intString) {
//    String getDivisibilityAnswer(int inputInt) {
//        String tricksInfo = Divisibility.getDivisibilityInfoViaTricks(inputInt);
//        tricksInfo = insertNewLines(tricksInfo);
//
//        StringJoiner linesJoiner =
//            new StringJoiner("\n")
//    //            .add(getDivisibilityInfoHeading(intString))
//            .add(Divisibility.getDivisibilityInfoHeading(inputInt))
//            .add("")
//            .add(Divisibility.divisibilityTricksInfoHeading)
//            .add(tricksInfo)
//            .add("")
//            .add(Divisibility.divisibilityPfInfoHeading);
//
////        PrimeFactorization.FactorsInfo pfFactorsInfo = PrimeFactorization.getDivisInfoViaPf(inputInt);
//        PrimeFactorization pf = new PrimeFactorization(inputInt);
//
//        if (pf.isForAPrimeNumber()) {
//            linesJoiner
//            .add(insertNewLines(pf.getInfoMessage() + ". " + Divisibility.getPrimeNumberSentence(inputInt)));
//        } else {
//            String factorsListPrefix =
//                pf.getInfoMessage() + ". " + pf.getNumberOfFactorsInfo() + " " + Divisibility.subfactorizationsSentence;
//            factorsListPrefix = insertNewLines(factorsListPrefix);
//            String factorsListWithPrefix =
//                stringifyList(PrimeFactorization.getFactorPfStrings(inputInt), factorsListPrefix);
//            linesJoiner.add(factorsListWithPrefix);
//        }
//
//        return linesJoiner.toString();
//    }
    
//    /**
//     * @return A string that shows a table with info for all iterations of the Euclidean algorithm performed
//     * on inputInt1 and inputInt2, along with a heading for the table and a message below the table about what
//     * the GCD is.
//     * @throws IllegalArgumentException if inputInt1 or inputInt2 are invalid for the GCD_AND_LCM section.
//     */
//    String getEuclideanAnswer(int inputInt1, String int1String, int inputInt2, String int2String) {
//    String getEuclideanAnswer(int inputInt1, int inputInt2) {
//        List<GcdAndLcm.EuclideanIteration> euclideanIterations = GcdAndLcm.getEuclideanIterations(inputInt1, inputInt2);
//
//        // The gap between the end of the longest item in a column and the item in the next column
//        final int columnGap = 4;
//
//        // Make column widths equal to the length of the longest element in the column + the column gap.
//        // The 1st iteration will have the longest elements of all iterations.
//        GcdAndLcm.EuclideanIteration iteration1 = euclideanIterations.get(0);
//        int maxColumnWidth =
//            Math.max(GcdAndLcm.euclideanMaxColumnHeading.length(), iteration1.getMaxString().length()) +
//            columnGap;
//
//        int minColumnWidth =
//            Math.max(GcdAndLcm.euclideanMinColumnHeading.length(), iteration1.getMinString().length()) +
//            columnGap;
//
//        String headRow =
//            getRowFor3ColumnTable(GcdAndLcm.euclideanMaxColumnHeading, maxColumnWidth, GcdAndLcm.euclideanMinColumnHeading, minColumnWidth, GcdAndLcm.euclideanRemainderColumnHeading);
////            maxHeading + getWhiteSpace(maxColumnWidth - maxHeading.length()) +
////            minHeading + getWhiteSpace(minColumnWidth - minHeading.length()) +
////            remainderHeading;
//
////        EuclideanIteration
//        String prefix = GcdAndLcm.euclideanHeading + "\n" + headRow + "\n";
//        String suffix = "\n" + GcdAndLcm.getEuclideanGcdMessage(inputInt1, inputInt2, euclideanIterations); //getEuclideanGcdMessage(int1String, int2String, euclideanIterations);
////            "The GCD of " + int1String + " and " + int2String + " is " +
////            euclideanIterations.get(euclideanIterations.size() - 1).getMinString();
//
//        // Create table
//        return
//            euclideanIterations
//            .stream()
//            .map(ei ->
//                getRowFor3ColumnTable(ei.getMaxString(), maxColumnWidth, ei.getMinString(), minColumnWidth, ei.getRemainderString())
//            )
//            .collect(Collectors.joining("\n", prefix, suffix));
//    }
    
//    /**
//     * @param inputInt1
//     * @param inputInt2
//     * @return A string that shows a heading followed by a table where 1 column is for an integer and another column is for the corresponding
//     * prime factorization. There are rows for inputInt1, inputInt2, the GCD of these, and the LCM of these. For the GCD
//     * and LCM rows, there's also a column that says "GCD" or "LCM".
//     * @throws IllegalArgumentException if inputInt1 or inputInt2 are invalid inputs for the PRIME_FACTORIZATION section.
//     */
////    String getGcdAndLcmViaPfAnswer(int inputInt1, String int1String, int inputInt2, String int2String) {
//    String getGcdAndLcmViaPfAnswer(int inputInt1, int inputInt2) {
//        PrimeFactorization.GcdAndLcmInfo info = new PrimeFactorization.GcdAndLcmInfo(inputInt1, inputInt2);
//        int column1Width = 6; // Only contents of this column are 1 row that says "GCD" and another row that says "LCM"
//
//        String inputInt1String = getLongStringWithCommas(inputInt1);
//        String inputInt2String = getLongStringWithCommas(inputInt2);
//
//        // Make column 2 as wide as the longest element in that column + 3
//        int column2Width =
//            Stream.of(GcdAndLcm.gcdAndLcmNumberColumnHeading, inputInt1String, inputInt2String, info.gcdString, info.lcmString)
//            .mapToInt(String::length)
//            .max()
//            .orElseThrow()
//            + 3;
//
//        // Create table
//        return String.join(
//            "\n",
//        GcdAndLcm.gcdAndLcmPfInfoHeading,
//            getRowFor3ColumnTable("", column1Width, GcdAndLcm.gcdAndLcmNumberColumnHeading, column2Width, GcdAndLcm.gcdAndLcmPfColumnHeading),
//            getRowFor3ColumnTable("", column1Width, inputInt1String, column2Width, info.int1PfString),
//            getRowFor3ColumnTable("", column1Width, inputInt2String, column2Width, info.int2PfString),
//            getRowFor3ColumnTable(GcdAndLcm.gcdRowHeading, column1Width, info.gcdString, column2Width, info.gcdPfString),
//            getRowFor3ColumnTable(GcdAndLcm.lcmRowHeading, column1Width, info.lcmString, column2Width, info.lcmPfString)
//        );
//    }
    


//    String getGoldbachAnswer(int inputInt, String intString) {
//        List<String> pairStrings = getGoldbachPrimePairStrings(inputInt);
//        return
//            stringifyList(pairStrings, 0, 0, getGoldbachLabel(pairStrings.size(), intString));
//    }
    
    /**
     * @param inputInt
     * @return A string that says some Pythagorean triples that come after inputInt, with each triple on its own line,
     * as well as a message at the top.
     * @throws IllegalArgumentException if inputInt is invalid input for the PYTHAG_TRIPLES section.
     */
//    String getPythagoreanTriplesAnswer(int inputInt) {//, String intString) {
//        StringJoiner lines =
//        new StringJoiner("\n")
//        .add(PythagoreanTriples.getPythagoreanTriplesListHeading(inputInt));//intString));
//
//        int i = 1;
//        for (String triple : PythagoreanTriples.getPythagTripleStrings(inputInt)) {
//            lines.add(i + ") " + triple);
//            i++;
//        }
//
//        return lines.toString();
//    }
    
    /**
     * @param int1
     * @param int1String
     * @param int2
     * @param int2String
     * @return A string of the elements of the Fibonacci-like sequence for the input ints and a message that says
     * what the ratio between the last 2 elements of the sequence are.
     * @throws IllegalArgumentException if the constructor for FibonacciLikeSequenceInfo throws
     *                                  an exception when int1 and int2 are given as args.
     */
//    String getFibonacciLikeSequencesAnswer(int int1, String int1String, int int2, String int2String) {
//        FibonacciLikeSequenceInfo info = new FibonacciLikeSequenceInfo(int1, int2);
//        String prefix = FibonacciLikeSequenceInfo.getListHeading(int1, int2);//int1String, int2String);
//        return
//            stringifyList(info.getStringSequence(), prefix) + "\n\n" + info.getEndRatioMessage();
//    }
    
//    /**
//     * @param inputInt1
//     * @param inputInt2
//     * @return An instance of AncientEgyptianMultiplicationInfo will be created and this method will return a
//     * string that shows a heading and 2 tables, 1 that will contain the data returned by calling getRows1() on the
//     * info object and 1 that will contain the data returned by calling getRows2() on the info object.
//     * @throws IllegalArgumentException if the constructor for AncientEgyptianMultiplicationInfo
//     *                                  throws an exception when inputInt1 and inputInt2 are given as args.
//     */
//    String getAncientMultiplicationAnswer(int inputInt1, /*String int1String,*/ int inputInt2/*, String int2String*/) {
//        AncientMultiplicationInfo info = new AncientMultiplicationInfo(inputInt1, inputInt2);
//
//        int columnGap = 3;
//
//        String powersOf2ColumnHeading = info.getAllPowersOf2ColumnHeading();
//        int table1Column1Width = powersOf2ColumnHeading.length() + columnGap;
//        String headRow =
//            getRowFor2ColumnTable(powersOf2ColumnHeading, table1Column1Width, info.getMaxIntMultiplesColumnHeading());
//
//        String table1 =
//            info.getRows1()
//            .stream()
//            .map(r ->
//                getRowFor2ColumnTable(r.getPowerOf2String(), table1Column1Width, r.getCorrespondingMultipleString())
//            )
//            .collect(Collectors.joining("\n", headRow + "\n", ""));
//
////        List<String> allMaxIntMultipleStrings = info.getAllMaxIntMultipleStrings();
////        String table =
////            IntStream.range(0, info.allPowersOf2.size())
////            .mapToObj(i -> getRowFor2ColumnTable(info.allPowersOf2.get(i).toString(), table1Column1Width, allMaxIntMultipleStrings.get(i)))
////            .collect(Collectors.joining("\n", headRow + "\n", ""));
//
//
////        List<String> maxIntMultiplesThatSumToProductStrings = info.getMaxIntMultiplesThatSumToProductStrings();
//        powersOf2ColumnHeading = info.getPowersOf2ThatSumToMinIntColumnHeading();
//        int table2Column1Width = powersOf2ColumnHeading.length() + columnGap;
//        headRow = getRowFor2ColumnTable(powersOf2ColumnHeading, table2Column1Width, info.getMaxIntMultiplesColumnHeading());
//
//        String productMessage = String.format("The sum of the right column is %s, which is the product", info.getProductString());
//
//        String table2 =
//            info.getRows2()
//            .stream()
//            .map(r -> getRowFor2ColumnTable(r.getPowerOf2String(), table2Column1Width, r.getCorrespondingMultipleString()))
//            .collect(Collectors.joining("\n", headRow + "\n", "\n" + productMessage));
//
////        String table2 =
////            IntStream.range(0, info.powersOf2ThatSumToMinInt.size())
////            .mapToObj(i -> getRowFor2ColumnTable(info.powersOf2ThatSumToMinInt.get(i).toString(), table2Column1Width, maxIntMultiplesThatSumToProductStrings.get(i)))
////            .collect(Collectors.joining("\n", headRow + "\n", "\n" + productMessage));
//
//        return String.join(
//            "\n\n",
//            AncientMultiplicationInfo.getAncientMultiplicationHeading(inputInt1, inputInt2),
//            //AncientEgyptianMultiplicationInfo.getInfoHeading(inputInt1, inputInt2),//int1String, int2String),
//            table1,
//            table2
//        );
//    }
    
//    void printPrimesAnswer(int anInt, String intString) {
//        List<String> primes;
//        try {
//            primes = getPrimesStrings(anInt);
//        } catch (IllegalArgumentException ex) {
//            String errorString = anInt + " was passed as an argument to Calculations.getPrimes and " +
//                    "this caused an IllegalArgumentException";
//            logError(errorString);
//            System.out.println(errorString);
//            return;
//        }
//
//        // maybe use String.join and sublists to build a string
//        print("The first " + primes.size() + " primes greater than or equal to " + intString + " are:");
//        for (int i = 0; i < primes.size(); i++) {
//            if (isDivisible(i, 6)) {
//                println();
//            }
//            print(primes.get(i) + "   ");
//        }
//    }

//    void printTwinPrimesAnswer(int anInt, String intString) {
//        List<String> twinPrimePairs;
//        try {
//            twinPrimePairs = getTwinPrimePairStrings(anInt);
//        } catch (IllegalArgumentException ex) {
//            String errorString = anInt + " was passed as an argument to Calculations.GetTwinPrimes and this caused " +
//                    "an IllegalArgumentException";
//            logError(errorString);
//            System.out.println(errorString);
//            return;
//        }
//
//        print("The first " + twinPrimePairs.size() + " twin prime pairs after " + intString + " are:");
//        for (int i = 0; i < twinPrimePairs.size(); i++) {
//            if (isDivisible(i, 4)) {
//                println();
//            }
//            print(twinPrimePairs.get(i) + "   ");
//        }
//        // println()?
//    }

//    void printPfAnswer(int anInt, String intString) {
//        String pfString;
//        try {
//            pfString = PrimeFactorization.getPfString(anInt);
//        } catch (IllegalArgumentException ex) {
//            String errorString = anInt + " was passed as an argument to PrimeFactrorization.getPfString and " +
//                    "this caused an IllegalArgumentException";
//            System.out.println(errorString);
//            return;
//        }
//
//        println("The prime factorization of " + intString + " is " + pfString);
//    }

//    void printDivisAnswer(int anInt, String intString) {
//        String tricksInfo;
//        try {
//            tricksInfo = getDivisibilityInfoViaTricks(anInt);
//        } catch (IllegalArgumentException ex) {
//            String errorString = anInt + " was passed as an argument to Calculations.GetDivisInfoViaTricks and this caused " +
//                    "an IllegalArgumentException";
//            logError(errorString);
//            System.out.println(errorString);
//            return;
//        }
//
//        println("Divisibility info for " + intString + " acquired by using special tricks:\n\n" + tricksInfo);
//    }

//    void printGcdAndLcmAnswer(int int1, String int1String, int int2, String int2String) {
//
//    }

//    void printGoldbachAnswer(int anInt, String intString) {
//        List<String> goldbachPrimePairs;
//        try {
//            goldbachPrimePairs = getGoldbachPrimePairStrings(anInt);
//        } catch (IllegalArgumentException ex) {
//            String errorString = anInt + " was passed as an argument to Calculations.GetGoldbachPrimePairs and this caused " +
//                    "an IllegalArgumentException";
//            logError(errorString);
//            System.out.println(errorString);
//            return;
//        }
//
//        print("There are " + getLongStringWithCommas(goldbachPrimePairs.size()) +
//                " pairs of prime numbers that sum to " + intString + ". They are:");
//        for (int i = 0; i < goldbachPrimePairs.size(); i++) {
//            if (isDivisible(i, 6)) {
//                println();
//            }
//            print(goldbachPrimePairs.get(i) + "   ");
//        }
//        // println();?
//    }

//    void printPythagTriplesAnswer(int anInt, String intString) {
//
//    }

    
    /**
     *
     * @param int1
     * @param int1String
     * @param int2
     * @param int2String
     * @throws IllegalArgumentException if the constructor for AncientEgyptianMultiplicationInfo
     * throws an exception when int1 and int2 are given as args.
     */
//    void printAncientEgyptianMultiplicationAnswer(int int1, String int1String, int int2, String int2String) {
//        AncientEgyptianMultiplicationInfo info = new AncientEgyptianMultiplicationInfo(int1, int2);
//    }

    /**
     * @return A string that says what section the user is at and what inputs a user can provide for the section and
     * what each input does.
     * @throws NullPointerException if section is null.
     */
//    String getSectionChoicesString(Section section, final String randomKey, final String infoKey, final String menuKey) {
        
        /*
        // What will happen if the user enters "i"
        String iChoice = "\"i\" to get information about ";

        String intAndRChoiceAction = "";
        switch (section) {
            case PRIMES:
                intAndRChoiceAction = "get the first 30 prime numbers greater than or equal to that integer";
                iChoice += "prime numbers";
                break;

            case TWIN_PRIMES:
                intAndRChoiceAction =
                    "get the first 20 pairs of twin prime numbers where the lowest number in the " +
                    "pair is greater than or equal to that integer";
                iChoice += "twin prime numbers";
                break;

            case PRIME_FACTORIZATION:
                intAndRChoiceAction = "get its prime factorization";
                iChoice += "prime factorizations";
                break;

            case DIVISIBILITY:
                intAndRChoiceAction = "get divisibility info about it";
                iChoice += "divisibility";
                break;

            case GCD_AND_LCM:
                intAndRChoiceAction = "get GCD and LCM info about them";
                iChoice += "GCDs and LCMs";
                break;

            case GOLDBACH:
                intAndRChoiceAction = "get the pairs of prime numbers that sum to it";
                iChoice += "the Goldbach Conjecture";
                break;

            case PYTHAG_TRIPLES:
                intAndRChoiceAction =
                    "get the 1st 10 Pythagorean triples where the lowest number in the " +
                    "triple is greater than or equal to that integer";
                iChoice += "Pythagorean triples";
                break;
        }

        boolean twoIntsNeeded = section.needs2Ints();
        String intChoice =
            (twoIntsNeeded ? "2 integers" : "An integer") + " to " + intAndRChoiceAction +
            ". Have " + (twoIntsNeeded ? "these integers" : "this integer") +
            " be greater than or equal to " + section.getMinInputInt() + " and less than or " +
            "equal to " + section.getMaxInputString();

        intChoice = getStringWithNewLineChars(intChoice);

        String rChoice =
            "\"r\" to generate " + (twoIntsNeeded ? "2 random integers" : "a random integer") +
            " and " + intAndRChoiceAction;

        List<String> lines =
            List.of(
                section.getHeadingText(),
                "Type one of the following and press enter:",
                intChoice,
                rChoice,
                iChoice,
                "\"m\" to go to the menu"
            );

//        return String.join("\n", lines);

        return section.getHeadingText() + "\nType one of the following and press enter:\n" +
                intChoice + "\n" + rChoice + "\n" + iChoice + "\n\"m\" to go to the menu";
*/
//        String choicesString = section.getHeadingDisplay() + "\nType one of the following and press enter:\n";
//        choicesString +=
//                intChoice + "\n\"r\" " + rChoice + "\n\"i\" " + iChoice + "\n\"m\" to go to the menu";
//        sb.append("section.\nType one of the following and press enter.\n");

//        switch (section) {
//            case PRIMES:
//                sb
//                        .append("A number to get the first 30 prime numbers after it. Have this number be greater ")
//                        .append("\n\tthan or equal to 0 and less than or equal to 1 billion")
//                        .append("\n\"r\" to generate a random number and get the first 30 prime numbers after it")
//                        .append("\n\"i\" for information about prime numbers");
//                break;
//
//            case TWIN_PRIMES:
//                sb
//                        .append("A number that to get the first 20 pairs of twin primes after it. Have this number be ")
//                        .append("\n\tgreater than or equal to 0 and less than or equal to 1 billion")
//                        .append("\n\"r\" to generate a random number and get the first 20 pairs of twin primes after it")
//                        .append("\n\"i\" for information about twin primes");
//                break;
//
//            case PRIME_FACTORIZATION:
//                sb
//                        .append("A number to get it's prime factorization. Have this number should be greater than or ")
//                        .append("\n\tequal to 2 and less than or equal to 10 thousand")
//                        .append("\n\"r\" to generate a random number and get it's prime factorization")
//                        .append("\n\"i\" for information about prime factorizations");
//                break;
//
//            case DIVISIBILITY:
//                sb
//                        .append("A number to get the divisibility info about it. Have this number should be greater than ")
//                        .append("\n\tor equal to 2 and less than or equal to 10 thousand")
//                        .append("\n\"r\" to generate a random number and get the divisibility info of it")
//                        .append("\n\"i\" for information about divisibility");
//                break;
//
//            case GCD_LCM:
//                sb
//                        .append("2 numbers to get the GCD and LCM info for them. Have these numbers be greater than ")
//                        .append("\n\tor equal to 2 and less than or equal to 10 thousand and separated by space.")
//                        .append("\n\"r\" to generate 2 random numbers and get the GCD and LCM info for them")
//                        .append("\n\"i\" for information about GCDs and LCMs");
//                break;
//
//            case GOLDBACH:
//                sb
//                        .append("A number to get the pairs of prime numbers that sum to it. Have this ")
//                        .append("\n\tnumber be even, greater than or equal to 4, and less than or equal to 100 thousand")
//                        .append("\n\"r\" to generate a random even number and get the pairs of prime numbers that sum to it")
//                        .append("\n\"i\" for information about the Goldbach conjecture");
//                break;
//
//            case PYTHAG_TRIPLES:
//                sb
//                        .append("A number to get the first 10 Pythagorean triples after it. Have this number be greater ")
//                        .append("\n\tthan or equal to 0 and less than or equal to 1 thousand")
//                        .append("\n\"r\" to generate a random number and get the first 10 Pythagorean triples after it")
//                        .append("\n\"i\" for information about Pythagorean triples");
//                break;
//        }
//
//        sb.append();
//        return sb.toString();
//    }

    /**
     * @return An answer string for sections that require the user to provide 1 input number.
     * @throws IllegalArgumentException if the section argument is not for a section that needs 1 input number.
     */
//    String getSingleInputAnswerString(Section section, int anInt) {
//        String intString = Misc.getLongStringWithCommas(anInt);
//        StringBuilder sb = new StringBuilder();
//
//        switch (section) {
//            case PRIMES:
//                // Build string of some primes that are greater than or equal to anInt
//                // and have 6 primes be on each line.
//                List<String> primes = Calculations.getPrimesStrings(anInt);
//
//                sb
//                        .append("The first ")
//                        .append(primes.size())
//                        .append(" primes greater than or equal to ")
//                        .append(intString)
//                        .append(" are:");
//
//                for (int i = 0; i < primes.size(); i++) {
//                    if (i % 6 == 0) {
//                        sb.append('\n');
//                    }
//                    sb.append(primes.get(i)).append("   ");
//                }
//                break;
//
//            case TWIN_PRIMES:
//                // Build string of some pairs of twin primes that are greater than or equal to anInt
//                // and have 4 pairs be on each line.
//                List<String> twinPrimes = Calculations.getTwinPrimePairStrings(anInt);
//                sb
//                        .append("The first ")
//                        .append(twinPrimes.size())
//                        .append(" pairs of twin primes after ")
//                        .append(intString)
//                        .append(" are:");
//
//                for (int i = 0; i < twinPrimes.size(); i++) {
//                    if (i % 4 == 0) {
//                        sb.append('\n');
//                    }
//                    sb.append(twinPrimes.get(i)).append("   ");
//                }
//                break;
//
//            case PRIME_FACTORIZATION:
//                // Build string of the prime factorization of the argument number
//                sb
//                        .append("The prime factorization of ")
//                        .append(intString)
//                        .append(" is ")
//                        .append(PrimeFactorization.getPfString(anInt));
//                break;
//
//            case DIVISIBILITY:
//                // Build string of divisibility info for the argument number
//                sb
//                        .append("Divisibility info for ")
//                        .append(intString)
//                        .append(" acquired by using special tricks:\n")
////                        .append(getStringWithNewLineChars(Calculations.getDivisibilityInfoViaTricks(anInt)))
//                        .append("\n\nInfo acquired from the prime factorization:");
//
////                for (String infoLine : PrimeFactorization.getDivisInfoViaPf(anInt)) {
////                    sb.append('\n').append(infoLine);
////                }
//                break;
//
//            case GOLDBACH:
//                // Build string of the prime number pairs that sum to the argument number and have 5 pairs be
//                // on each line.
//                List<String> goldbachPrimePairs = Calculations.getGoldbachPrimePairStrings(anInt);
//                sb
//                        .append("There are ")
//                        .append(goldbachPrimePairs.size())
//                        .append(" pairs of prime numbers that sum to ")
//                        .append(intString)
//                        .append(". They are:");
//
//                for (int i = 0; i < goldbachPrimePairs.size(); i++) {
//                    if (i % 5 == 0) {
//                        sb.append('\n');
//                    }
//                    sb.append(goldbachPrimePairs.get(i)).append("   ");
//                }
//                break;
//
//            case PYTHAG_TRIPLES:
//                // Build string of some Pythagorean triples equations
//                List<String> triplesEquations = Calculations.getPythagTripleStrings(anInt);
//                sb
//                        .append("The first ")
//                        .append(triplesEquations.size())
//                        .append(" Pythagorean triples after ")
//                        .append(intString)
//                        .append(" are:");
//
//                for (String equation : triplesEquations) {
//                    sb.append('\n').append(equation);
//                }
//                break;
//
//            default:
//                throw new IllegalArgumentException("invalid section: " + section);
//        }
//        return sb.toString();
//    }

    /**
     * @return An answer string for sections that require the user provide 2 input numbers.
     * @throws IllegalArgumentException if the section argument is not for a section that needs 2 input numbers.
     */
//    String getDoubleInputAnswerString(Section section, int int1, int int2) {
//        // Only section is the gcd and lcm section
//        if (section != Section.GCD_AND_LCM) {
//            throw new IllegalArgumentException("invalid section: " + section);
//        }
//
//        String intString1 = Misc.getLongStringWithCommas(int1);
//        String intString2 = Misc.getLongStringWithCommas(int2);
//
//        // Build string of the GCD and LCM info for firstNumber and secondNumber
//        StringBuilder sb = new StringBuilder()
//                .append("GCD and LCM info for ")
//                .append(intString1)
//                .append(" and ")
//                .append(intString2)
//                .append(" acquired by looking at the prime factorizations:");
//
//        for (String infoLine : PrimeFactorization.getGcdAndLcmInfoViaPf(int1, int2)) {
//            sb.append('\n').append(infoLine);
//        }
//
//        sb.append("\n\nGCD info acquired from the Euclidean algorithm");
//        for (String infoLine : Calculations.getEuclideanInfo(int1, int2)) {
//            sb.append('\n').append(infoLine);
//        }
//
//        return sb.toString();
//    }
//}


// Build string that shows choices for the current section and set the range for valid inputs
//            String sectionChoicesString = "Type one of the following and press enter\n";
//            int minInputNumber, maxInputNumber;
//
//            switch (section) {
//                case "primes":
//                    sectionChoicesString +=
//                            "A number to get the first 30 prime numbers after it. Have this number be greater than or " +
//                                    "\n\tequal to " + Integer.MIN_VALUE + " and less than or equal to 1 billion"
//                                    + "\n\"r\" to generate a random number and get the first 30 prime numbers after it"
//                                    + "\n\"i\" for information about prime numbers";
//                    minInputNumber = Integer.MIN_VALUE;
//                    maxInputNumber = 1_000_000_000;
//                    break;
//
//                case "twin primes":
//                    sectionChoicesString +=
//                            "A number that to get the first 20 pairs of twin primes after it. Have this number be " +
//                                    "\n\tgreater than or equal to " + Integer.MIN_VALUE + " and less than or equal to " +
//                                    "1 billion" +
//                                    "\n\"r\" to generate a random number and get the first 20 pairs of twin primes after it" +
//                                    "\n\"i\" for information about twin primes";
//                    minInputNumber = Integer.MIN_VALUE;
//                    maxInputNumber = 1_000_000_000;
//                    break;
//
//                case "prime factorization":
//                    sectionChoicesString +=
//                            "A number to get it's prime factorization. Have this number should be greater than or " +
//                                    "\n\tequal to 2 and less than or equal to 10 thousand" +
//                                    "\n\"r\" to generate a random number and get it's prime factorization" +
//                                    "\n\"i\" for information about prime factorizations";
//                    minInputNumber = 2;
//                    maxInputNumber = 10_000;
//                    break;
//
//                case "divisibility":
//                    sectionChoicesString +=
//                            "A number to get the divisibility info about it. Have this number should be greater than " +
//                                    "\n\tor equal to 2 and less than or equal to 10 thousand" +
//                                    "\n\"r\" to generate a random number and get the divisibility info of it" +
//                                    "\n\"i\" for information about divisibility";
//                    minInputNumber = 2;
//                    maxInputNumber = 10_000;
//                    break;
//
//                case "gcd and lcm":
//                    sectionChoicesString +=
//                            "2 numbers to get the GCD and LCM info for them. Have these numbers be greater than " +
//                                    "\n\tor equal to 2 and less than or equal to 10 thousand and separated by a single space." +
//                                    "\n\"r\" to generate 2 random numbers and get the GCD and LCM info for them" +
//                                    "\n\"i\" for information about GCDs and LCMs";
//                    minInputNumber = 2;
//                    maxInputNumber = 10_000;
//                    break;
//
//                case "goldbach":
//                    sectionChoicesString +=
//                            "A number to get the pairs of prime numbers that sum to it. Have this " +
//                                    "\n\tnumber be even, greater than or equal to 4, and less than or equal to 100 thousand" +
//                                    "\n\"r\" to generate a random even number and get the pairs of prime numbers that sum to it" +
//                                    "\n\"i\" for information about the Goldbach conjecture";
//                    minInputNumber = 4;
//                    maxInputNumber = 100_000;
//                    break;
//
//                case "pythag triples":
//                    sectionChoicesString +=
//                            "A number to get the first 10 Pythagorean triples after it. Have this number be greater " +
//                                    "\n\tthan or equal to " + Integer.MIN_VALUE + " and less than or equal to 1 thousand" +
//                                    "\n\"r\" to generate a random number and get the first 10 Pythagorean triples after it" +
//                                    "\n\"i\" for information about Pythagorean triples";
//                    minInputNumber = Integer.MIN_VALUE;
//                    maxInputNumber = 1_000;
//                    break;
//
//                default:
//                    throw new IllegalStateException("invalid section: " + section);
//            }
//
//            sectionChoicesString += "\n\"m\" to go to the menu";
    /**
     * Interacts with the user about which section to go to.
     */

//    NTPCLI() {
//        System.out.println("Welcome to the command line version of the Number Theory Playground");
//        while (true) {
//            System.out.print(
//                    "\nWould you like to:" +
//                            "\n(1) Find prime numbers" +
//                            "\n(2) Find twin primes" +
//                            "\n(3) Get a number's prime factorization" +
//                            "\n(4) Get a number's divisibility information" +
//                            "\n(5) Get GCD and LCM information" +
//                            "\n(6) Get a number's Goldbach pairs" +
//                            "\n(7) Find Pythagorean triples" +
//                            "\n(8) Exit" +
//                            "\nEnter your choice: "
//            );
//            String input = inputReader.nextLine();
//            switch (input) {
//                case "1":
//                    primesSection();
//                    break;
//                case "2":
//                    twinPrimesSection();
//                    break;
//                case "3":
//                    primeFactorizationSection();
//                    break;
//                case "4":
//                    divisSection();
//                    break;
//                case "5":
//                    gcdAndLcmSection();
//                    break;
//                case "6":
//                    goldbachSection();
//                    break;
//                case "7":
//                    pythagTriplesSection();
//                    break;
//                case "8":
//                    System.out.println("\nI hope you found this interesting");
//                    System.exit(0);
//                default:
//                    System.out.println("\nInvalid input");
//                    break;
//            }
//        }
//    }

//    void goToSection(String section) {
//        // Build string that shows choices
//        String choicesString = "Type one of the following and press enter\n";
//        switch (section) {
//            case "primes":
//                choicesString +=
//                        "A number less than or equal to 1 billion to get the first 30 prime numbers after it"
//                                + "\n\"r\" to generate a random number and get the first 30 prime numbers after it"
//                                + "\n\"i\" for information about prime numbers";
//                break;
//            case "twin primes":
//                choicesString +=
//                        "A number that is less than or equal to 1 billion to get the first 20 pairs of twin primes after it"
//                                + "\n\"r\" to generate a random number and get the first 20 pairs of twin primes after it"
//                                + "\n\"i\" for information about twin primes";
//                break;
//
//            case "prime factorization":
//                choicesString +=
//                        "A number to get it's prime factorization. This number should be greater than or "
//                                + "equal to 2 and less than or equal to 10 million"
//                                + "\n\"r\" to generate a random number and get it's prime factorization"
//                                + "\n\"i\" for information about prime factorizations";
//                break;
//
//            case "divisibility":
//                choicesString +=
//                        "A number to get the divisibility info about it. This number should be greater than or equal " +
//                                "to 2 and less than or equal to 10 million" +
//                                "\n\"r\" to generate a random number and get the divisibility info of it" +
//                                "\n\"i\" for information about divisibility";
//                break;
//
//            case "gcd and lcm":
//                choicesString +=
//                        "2 numbers to get the GCD and LCM info for them. Have these numbers be greater than " +
//                                "or equal to 2 and less than or equal to 10 million and separated by a single space." +
//                                "\n\"r\" to generate 2 random numbers and get the GCD and LCM info for them" +
//                                "\n\"i\" for information about GCDs and LCMs";
//                break;
//
//            case "goldbach":
//                choicesString +=
//                        "A number to get the pairs of prime numbers that sum to it. Have this " +
//                                "number be even, greater than or equal to 4, and less than or equal to 100 thousand" +
//                                "\n\"r\" to generate a random even number and get the pairs of prime numbers that sum to it" +
//                                "\n\"i\" for information about the Goldbach conjecture";
//                break;
//
//            case "pythag triples":
//                choicesString +=
//                        "A number that is less than or equal to 1 thousand to get the first 10 Pythagorean triples after it" +
//                                "\n\"r\" to generate a random number and get the first 10 Pythagorean triples after it" +
//                                "\n\"i\" for information about Pythagorean triples";
//                break;
//        }
//
//        choicesString += "\n\"m\" to go to the menu";
//
//        choiceAskLoop:
//        while (true) {
//            System.out.println(choicesString);
//            String input = inputReader.nextLine().toLowerCase();
//            switch (input) {
//                case "m":
//                    return;
//
//                case "i":
//                    String infoString;
//                    switch (section) {
//                        case "primes":
//                            infoString = Primes.getSectionInfo2();
//                            break;
//                        case "twin primes":
//                            infoString = TwinPrimes.getSectionInfo2();
//                            break;
//                        case "prime factorization":
//                            infoString = PrimeFactorization.getSectionInfo2();
//                            break;
//                        case "divisibility":
//                            infoString = Divisibility.getSectionInfo2();
//                            break;
//                        case "gcd and lcm":
//                            infoString = GcdAndLcm.getSectionInfo2();
//                            break;
//                        case "goldbach":
//                            infoString = Goldbach.getSectionInfo2();
//                            break;
//                        case "pythag triples":
//                            infoString = PythagoreanTriples.getSectionInfo2();
//                            break;
//                        default:
//                            throw new IllegalArgumentException("");
//                    }
//                    System.out.println(infoString);
//                    break;
//
////                case "r":
////                    break;
//
//                default:
//                    int firstNumber = 0;
//                    int secondNumber = 0;
//                    int maxNumber = 0;
//                    int minInputNumber = 0;
//                    int minRandomNumber = 0;
//                    switch (section) {
//                        case "primes":
//                        case "twin primes":
//                            minInputNumber = Integer.MIN_VALUE;
//                            minRandomNumber = 0;
//                            maxNumber = 1_000_000_000;
//                            break;
//
//                        case "prime factorization":
//                        case "divisibility":
//                        case "gcd and lcm":
//                            minInputNumber = 2;
//                            minRandomNumber = 2;
//                            maxNumber = 10_000;
//                            break;
//
//                        case "goldbach":
//                            minInputNumber = 4;
//                            minRandomNumber = 4;
//                            maxNumber = 100_000;
//                            break;
//
//                        case "pythag triples":
//                            minInputNumber = Integer.MIN_VALUE;
//                            minRandomNumber = 0;
//                            maxNumber = 1_000;
//                            break;
//                    }
//
//                    if (input.equals("r")) {
//                        // generate random numbers
//                        firstNumber = Math.max(random.nextInt(maxNumber), minInputNumber);
//                        if (section.equals("goldbach")) {
//                            if (firstNumber % 2 != 0) {
//                                firstNumber++;
//                            }
//                        } else if (section.equals("gcd and lcm")) {
//                            secondNumber = Math.max(random.nextInt(maxNumber), minInputNumber);
//                        }
//                    } else {
//                        // Check input for number or numbers
//                        try {
//                            if (section.equals("gcd and lcm")) {
//                                String[] inputContents = input.split(" ");
//                                firstNumber = Integer.parseInt(inputContents[0]);
//                                secondNumber = Integer.parseInt(inputContents[1]);
//                                if (secondNumber < minInputNumber || secondNumber > maxNumber) {
//                                    throw new Exception("");
//                                }
//                            } else {
//                                firstNumber = Integer.parseInt(input);
//                                if (section.equals("goldbach") && firstNumber % 2 != 0) {
//                                    //TODO make sure input is validated for this case
//                                    throw new Exception("");
//                                }
//                            }
//                            if (firstNumber < minInputNumber || firstNumber > maxNumber) {
//                                throw new Exception("");
//                            }
//                        } catch (Exception ex) {
//                            System.out.println("\nInvalid input\n");
//                            continue choiceAskLoop;
//                        }
//                    }
//
//
//                    String answerString = "";
//                    StringBuilder answerSb = new StringBuilder();
//                    switch (section) {
//                        case "primes":
//                            answerSb.append("The first 30 primes after ").append(firstNumber).append(" are");
////                            answerString += "The first 30 primes after " + firstNumber + " are\n";
//                            List<Integer> primes = Primes.getPrimesAfter2(firstNumber);
//                            for (int i = 0; i < primes.size(); i++) {
//                                if (i % 6 == 0) {
//                                    answerSb.append("\n");
//                                }
//                                answerSb.append(primes.get(i)).append("   ");
////                                answerString += primes.get(i) + "   ";
//                            }
//                            break;
//
//                        case "twin primes":
//                            answerSb.append("The first 20 pairs of twin primes after ").append(firstNumber).append(" are");
////                            answerString += "The first 20 pairs of twin primes after " + firstNumber + " are\n";
//                            List<String> twinPrimes = TwinPrimes.getTwinPrimesAfter2(firstNumber);
//                            for (int i = 0; i < twinPrimes.size(); i++) {
//                                if (i % 4 == 0) {
////                                    answerString += "\n";
//                                    answerSb.append("\n");
//                                }
//                                answerSb.append(twinPrimes.get(i)).append("   ");
////                                answerString += twinPrimes.get(i) + "   ";
//                            }
//                            break;
//
//                        case "prime factorization":
//                            answerSb.append("The prime factorization of ").append(firstNumber).append(" is ")
//                                    .append(PrimeFactorization.getPfString(firstNumber));
////                            answerString += "The prime factorization of " + firstNumber + " is " +
////                                    PrimeFactorization.getPfString(firstNumber);
//                            break;
//
//                        case "divisibility":
//                            answerSb.append("Divisibility info for ").append(firstNumber)
//                                    .append(" acquired by using special tricks");
////                            answerString += "Divisibility info for " + firstNumber + " acquired by using special tricks";
//                            Collection<String> tricksInfo = Divisibility.getDivisInfoViaTricks(firstNumber);
//                            for (String infoLine : tricksInfo) {
////                                answerString += "\n" + infoLine;
//                                answerSb.append('\n').append(infoLine);
//                            }
//                            answerSb.append("\nInfo acquired from the prime factorization");
////                            answerString += "\nInfo acquired from the prime factorization";
//                            for (String infoLine : Divisibility.getDivisInfoViaPf(firstNumber)) {
////                                answerString += "\n" + infoLine;
//                                answerSb.append('\n').append(infoLine);
//                            }
//                            break;
//
//                        case "gcd and lcm":
//                            answerSb.append("GCD and LCM info for ").append(firstNumber).append(" and ")
//                                    .append(section).append(" acquired by the prime factorizations");
////                            answerString += "GCD and LCM info for " + firstNumber + " and " + secondNumber +
////                                    " acquired by the prime factorizations";
//                            for (String infoLine : GcdAndLcm.getGcdAndLcmInfoViaPf(firstNumber, secondNumber)) {
////                                answerString += "\n" + infoLine;
//                                answerSb.append('\n').append(infoLine);
//                            }
//
////                            answerString += "\n\nEuclidean algorithm info";
//                            answerSb.append("\n\nGCD info acquired from the Euclidean algorithm");
//                            for (String infoLine : GcdAndLcm.getEuclideanInfo(firstNumber, secondNumber)) {
////                                answerString += "\n" + infoLine;
//                                answerSb.append('\n').append(infoLine);
//                            }
//                            break;
//
//                        case "goldbach":
//                            answerSb.append("Prime number pairs that sum to ").append(firstNumber);
////                            answerString += "Prime number pairs that sum to " + firstNumber + "\n";
//                            List<String> goldbachPrimePairs = Goldbach.getGoldbachPairs2(firstNumber);
//                            for (int i = 0; i < goldbachPrimePairs.size(); i++) {
//                                if (i % 5 == 0) {
//                                    answerSb.append('\n');
////                                    answerString += "\n";
//                                }
//                                answerSb.append(goldbachPrimePairs.get(i)).append("   ");
////                                answerString += goldbachPrimePairs.get(i) + "   ";
//                            }
//                            break;
//
//                        case "pythag triples":
//                            answerSb.append("The first 10 Pythagorean triples after ").append(firstNumber);
////                            answerString += "The first 10 Pythagorean triples after " + firstNumber;
//                            for (String triple : PythagoreanTriples.getTriples2(firstNumber)) {
////                                answerString += "\n" + triple;
//                                answerSb.append('\n').append(triple);
//                            }
//                            break;
//                    }
//                    System.out.println(answerSb.toString());
//                    break;
//            }
//        }
//    }
//
//    void switchStatement() {
//        String section = "";
//        switch (section) {
//            case "primes":
//                break;
//            case "twin primes":
//                break;
//            case "prime factorization":
//                break;
//            case "divisibility":
//                break;
//            case "gcd and lcm":
//                break;
//            case "goldbach":
//                break;
//            case "pythag triples":
//                break;
//        }
//    }
//
//    /**
//     * Interacts with the user about prime numbers.
//     */
//    void primesSection() {
//        while (true) {
//            System.out.println(
//                    "\nType one of the following and press enter:" +
//                    "\nA number less than or equal to 1 billion to get the first 30 prime numbers after it" +
//                    "\n\"r\" to generate a random number and get the first 30 prime numbers after it" +
//                    "\n\"i\" for information about prime numbers" +
//                    "\n\"m\" to go to the menu"
//            );
//            int number;
//            String input = inputReader.nextLine();
//            switch (input.toLowerCase()) {
//                case "m": return;
//
//                case "i":
//                    System.out.println();
//                    for (String infoLine : Primes.getPrimesSectionInfo()) {
//                        System.out.println(infoLine);
//                    }
//                    break;
//
//                case "r":
//                    number = random.nextInt(1_000_000_000);
//                    System.out.println("\nThe primes after " + number + " are:");
//                    for (String primesLine : Primes.getPrimesAfter(number)) {
//                        System.out.println(primesLine);
//                    }
//                    break;
//
//                default:
//                    // If the user typed something that is not one of the above options, check if it's a number
//                    // and display the appropriate calculations if it is.
//                    try {
//                        number = Integer.parseInt(input);
//                    } catch (NumberFormatException ex) {
//                        System.out.println("\nInvalid input");
//                        break;
//                    }
//                    if (number <= 1_000_000_000) {
//                        System.out.println("\nThe primes after " + number + " are:");
//                        for (String primesLine : Primes.getPrimesAfter(number)) {
//                            System.out.println(primesLine);
//                        }
//                    } else {
//                        System.out.println("\nInvalid input");
//                    }
//                    break;
//            }
//        }
//    }
//
//    /**
//     * Interacts with the user about twin primes.
//     */
//    void twinPrimesSection() {
//        while (true) {
//            System.out.println(
//                    "\nType one of the following and press enter:" +
//                    "\nA number that is less than or equal to 1 billion to get the first 20 pairs of " +
//                    "twin primes after it" +
//                    "\n\"r\" to generate a random number and get the first 20 pairs of twin primes after it" +
//                    "\n\"i\" for information about twin primes" +
//                    "\n\"m\" to go to the menu"
//            );
//            int number;
//            String input = inputReader.nextLine();
//            switch (input.toLowerCase()) {
//                case "m": return;
//
//                case "i":
//                    System.out.println();
////                    for (String infoLine : Primes.getTwinPrimesSectionInfo()) {
////                        System.out.println(infoLine);
////                    }
//                    break;
//
//                case "r":
//                    number = random.nextInt(1_000_000_000);
//                    System.out.println("\nTwin primes after " + number + ":\n");
//                    for (String twinPrimesLine : Primes.getTwinPrimesAfter(number)) {
//                        System.out.println(twinPrimesLine);
//                    }
//                    break;
//
//                default:
//                    // If the user typed something that is not one of the above options, check if it's a number
//                    // and display the appropriate calculations if it is.
//                    try {
//                        number = Integer.parseInt(input);
//                    } catch (NumberFormatException ex) {
//                        System.out.println("\nInvalid input");
//                        continue;
//                    }
//                    if (number <= 1_000_000_000) {
//                        System.out.println("\nTwin primes after " + number + ":\n");
//                        for (String twinPrimesLine : Primes.getTwinPrimesAfter(number)) {
//                            System.out.println(twinPrimesLine);
//                        }
//                    } else {
//                        System.out.println("\nInvalid input");
//                    }
//                    break;
//            }
//        }
//    }
//
//    /**
//     * Interacts with the user about prime factorizations.
//     */
//    void primeFactorizationSection() {
//        while (true) {
//            System.out.println(
//                    "\nType one of the following and press enter:" +
//                    "\nA number to get it's prime factorization. This number should be greater than or " +
//                    "equal to 2 and less than or equal to 10 million" +
//                    "\n\"r\" to generate a random number and get it's prime factorization" +
//                    "\n\"i\" for information about prime factorizations" +
//                    "\n\"m\" to go to the menu"
//            );
//            int number;
//            String input = inputReader.nextLine();
//            switch (input) {
//                case "m": return;
//
//                case "i":
//                    System.out.println();
//                    for (String infoLine : PrimeFactorization.getSectionInfo()) {
//                        System.out.println(infoLine);
//                    }
//                    break;
//
//                case "r":
//                    // Generate a random number that is greater than or equal to 2 and less than or equal to 10 million
//                    number = Math.max(2, random.nextInt(10_000_000));
//                    System.out.println("\nPrime factorization for " + number + ":\n" +
//                            PrimeFactorization.getPfString(number));
//                    break;
//
//                default:
//                    // If the user typed something that is not one of the above options, check if it's a number
//                    // and display the appropriate calculations if it is.
//                    try {
//                        number = Integer.parseInt(input);
//                    } catch (NumberFormatException ex) {
//                        System.out.println("\nInvalid input");
//                        break;
//                    }
//                    if (number >= 2 && number <= 10_000_000) {
//                        System.out.println("\nPrime factorzation for " + number + ":\n" +
//                                PrimeFactorization.getPfString(number));
//                    } else {
//                        System.out.println("\nInvalid input");
//                    }
//                    break;
//            }
//        }
//    }
//
//    /**
//     * Interacts with the user about divisibility.
//     */
//    void divisSection() {
//        while (true) {
//            System.out.println(
//                    "\nType one of the following and press enter:" +
//                    "\nA number to get the divisibility info about it. This number should be greater than or equal " +
//                    "to 2 and less than or equal to 10 million" +
//                    "\n\"r\" to generate a random number and get the divisibility info of it" +
//                    "\n\"i\" for information about divisibility" +
//                    "\n\"m\" to go to the menu"
//            );
//            int number;
//            String input = inputReader.nextLine();
//            switch (input.toLowerCase()) {
//                case "m": return;
//
//                case "i":
//                    System.out.println();
//                    for (String infoLine : Divisibility.getSectionInfo()) {
//                        System.out.println(infoLine);
//                    }
//                    break;
//
//                case "r":
//                    // Generate a random number that is greater than or equal to 2 and less than or equal to 10 million
//                    number = Math.max(2, random.nextInt(10_000_000));
//                    System.out.println("\nDivisibility info for " + number + ":\n");
//                    for (String infoLine : Divisibility.getDivisInfoViaTricks(number)) {
//                        System.out.println(infoLine);
//                    }
//                    System.out.println();
//                    for (String infoLine : Divisibility.getDivisInfoViaPf(number)) {
//                        System.out.println(infoLine);
//                    }
//                    break;
//
//                default:
//                    // If the user typed something that is not one of the above options, check if it's a number
//                    // and display the appropriate calculations if it is.
//                    try {
//                        number = Integer.parseInt(input);
//                    } catch (NumberFormatException ex) {
//                        System.out.println("\nInvalid input");
//                        continue;
//                    }
//                    if (number >= 2 && number <= 10_000_000) {
//                        System.out.println("\nDivisibility info for " + number + ":\n");
//                        for (String infoLine : Divisibility.getDivisInfoViaTricks(number)) {
//                            System.out.println(infoLine);
//                        }
//                        System.out.println();
//                        for (String infoLine : Divisibility.getDivisInfoViaPf(number)) {
//                            System.out.println(infoLine);
//                        }
//                    } else {
//                        System.out.println("\nInvalid input");
//                    }
//                    break;
//            }
//        }
//    }
//
//    /**
//     * Interacts with the user about GCDs and LCMs.
//     */
//    void gcdAndLcmSection() {
//        while (true) {
//            System.out.println(
//                    "\nType one of the following and press enter:" +
//                    "\n2 numbers to get the GCD and LCM info for them. Have these numbers be greater than " +
//                    "or equal to 2 and less than or equal to 10 million and separated by a single space." +
//                    "\n\"r\" to generate 2 random numbers and get the GCD and LCM info for them" +
//                    "\n\"i\" for information about GCDs and LCMs" +
//                    "\n\"m\" to go to the menu"
//            );
//            int firstNumber, secondNumber;
//            String input = inputReader.nextLine();
//            switch (input.toLowerCase()) {
//                case "m": return;
//
//                case "i":
//                    System.out.println();
//                    for (String infoLine : GcdAndLcm.getSectionInfo()) {
//                        System.out.println(infoLine);
//                    }
//                    break;
//
//                case "r":
//                    // Generate random numbers that are greater than or equal to 2 and less than or equal to 10 million
//                    firstNumber = Math.max(2, random.nextInt(10_000_000));
//                    secondNumber = Math.max(2, random.nextInt(10_000_000));
//                    System.out.println("\nGCD and LCM info for " + firstNumber + " and " + secondNumber + ":\n");
//                    for (String infoLine : GcdAndLcm.getGcdAndLcmInfoViaPf(firstNumber, secondNumber)) {
//                        System.out.println(infoLine);
//                    }
//                    System.out.println();
//                    for (String infoLine : GcdAndLcm.getEuclideanInfo(firstNumber, secondNumber)) {
//                        System.out.println(infoLine);
//                    }
//                    break;
//
//                default:
//                    // If the user typed something that is not one of the above options, check if it's 2 numbers
//                    // separated by a single space and display the appropriate calculations if it is.
//                    String[] inputArray = input.split(" ");
//                    if (inputArray.length != 2) {
//                        System.out.println("\nInvalid input");
//                        break;
//                    }
//                    try {
//                        firstNumber = Integer.parseInt(inputArray[0]);
//                        secondNumber = Integer.parseInt(inputArray[1]);
//                    } catch (NumberFormatException ex) {
//                        System.out.println("\nInvalid input");
//                        break;
//                    }
//                    if (firstNumber >= 2 && firstNumber <= 10_000_000 &&
//                            secondNumber >= 2 && secondNumber <= 10_000_000) {
//                        System.out.println("\nGCD and LCM info for " + firstNumber + " and " + secondNumber + ":\n");
//                        for (String infoLine : GcdAndLcm.getGcdAndLcmInfoViaPf(firstNumber, secondNumber)) {
//                            System.out.println(infoLine);
//                        }
//                        System.out.println();
//                        for (String infoLine : GcdAndLcm.getEuclideanInfo(firstNumber, secondNumber)) {
//                            System.out.println(infoLine);
//                        }
//                    } else {
//                        System.out.println("\nInvalid input");
//                    }
//                    break;
//            }
//        }
//    }
//
//    /**
//     * Interacts with the user about the Goldbach conjecture.
//     */
//    void goldbachSection() {
//        while (true) {
//            System.out.println(
//                    "\nType one of the following and press enter:" +
//                    "\nA number to get the pairs of prime numbers that sum to it. Have this " +
//                    "number be even, greater than or equal to 4, and less than or equal to 100 thousand" +
//                    "\n\"r\" to generate a random even number and get the pairs of prime numbers that sum to it" +
//                    "\n\"i\" for information about the Goldbach conjecture" +
//                    "\n\"m\" to go to the menu"
//            );
//            int number;
//            String input = inputReader.nextLine();
//            switch (input.toLowerCase()) {
//                case "m": return;
//
//                case "i":
//                    System.out.println();
//                    for (String infoLine : Goldbach.getSectionInfo()) {
//                        System.out.println(infoLine);
//                    }
//                    break;
//
//                case "r":
//                    // Generate a random number that is even, greater than or equal to 4, and less than or equal
//                    // to 100 thousand
//                    number = Math.max(4, random.nextInt(100_000));
//                    if (number % 2 != 0) {
//                        number++;
//                    }
//                    System.out.println("\nPairs of prime numbers that sum to " + number + ":\n");
//                    for (String pairsLine : Goldbach.getGoldbachPairs(number)) {
//                        System.out.println(pairsLine);
//                    }
//                    break;
//
//                default:
//                    // If the user typed something that is not one of the above options, validate it
//                    // and display the appropriate calculations if it's valid.
//                    try {
//                        number = Integer.parseInt(input);
//                    } catch (NumberFormatException ex) {
//                        System.out.println("\nInvalid input");
//                        break;
//                    }
//                    if (number >= 4 && number <= 100_000 && number % 2 == 0) {
//                        System.out.println("\nPairs of prime numbers that sum to " + number + ":\n");
//                        for (String pairsLine : Goldbach.getGoldbachPairs(number)) {
//                            System.out.println(pairsLine);
//                        }
//                    } else {
//                        System.out.println("\nInvalid input");
//                    }
//                    break;
//            }
//        }
//    }
//
//    /**
//     * Interacts with the user about Pythagorean triples.
//     */
//    void pythagTriplesSection() {
//        while (true) {
//            System.out.println(
//                    "\nType one of the following and press enter:" +
//                    "\nA number that is less than or equal to 1 thousand to get the first 10 Pythagorean " +
//                    "triples after it" +
//                    "\n\"r\" to generate a random number and get the first 10 Pythagorean triples after it" +
//                    "\n\"i\" for information about Pythagorean triples" +
//                    "\n\"m\" to go to the menu"
//            );
//            int number;
//            String input = inputReader.nextLine();
//            switch (input.toLowerCase()) {
//                case "m": return;
//
//                case "i":
//                    System.out.println();
//                    for (String infoLine : PythagoreanTriples.getSectionInfo()) {
//                        System.out.println(infoLine);
//                    }
//                    break;
//
//                case "r":
//                    number = random.nextInt(1_000);
//                    System.out.println("\nPythagorean triples after " + number + ":\n");
//                    for (String infoLine : PythagoreanTriples.getTriples(number)) {
//                        System.out.println(infoLine);
//                    }
//                    break;
//
//                default:
//                    // If the user typed something that is not one of the above options, check if it's a number
//                    // and display the appropriate calculations if it is.
//                    try {
//                        number = Integer.parseInt(input);
//                    } catch (NumberFormatException ex) {
//                        System.out.println("\nInvalid input");
//                        continue;
//                    }
//                    if (number <= 1_000) {
//                        System.out.println("\nPythagorean triples after " + number + ":\n");
//                        for (String triplesLine : PythagoreanTriples.getTriples(number)) {
//                            System.out.println(triplesLine);
//                        }
//                    } else {
//                        System.out.println("\nInvalid input");
//                    }
//            }
//        }
//    }
//}
