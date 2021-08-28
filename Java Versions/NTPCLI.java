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
    Random random = new Random();

    NTPCLI() {
        goToMenu();
    }

    void goToMenu() {
        System.out.println("\nWelcome to the command line version of the Number Theory Playground\n");
        String menuString = "You're at the main menu. Which section would you like to go to?" +
                "\n(1) Prime Numbers" +
                "\n(2) Twin Prime Numbers" +
                "\n(3) Prime Factorization" +
                "\n(4) Divisibility" +
                "\n(5) GCD and LCM" +
                "\n(6) Goldbach Conjecture" +
                "\n(7) Pythagorean Triples" +
                "\n(8) Exit" +
                "\nEnter your choice: ";

        while (true) {
            System.out.print(menuString);
            switch (inputReader.nextLine()) {
                case "1":
                    goToSection(Section.PRIMES);
                    break;

                case "2":
                    goToSection(Section.TWIN_PRIMES);
                    break;

                case "3":
                    goToSection(Section.PRIME_FACTORIZATION);
                    break;

                case "4":
                    goToSection(Section.DIVISIBILITY);
                    break;

                case "5":
                    goToSection(Section.GCD_LCM);
                    break;

                case "6":
                    goToSection(Section.GOLDBACH);
                    break;

                case "7":
                    goToSection(Section.PYTHAG_TRIPLES);
                    break;

                case "8":
                    System.out.println("\nI hope you found this interesting");
                    return;

                default:
                    System.out.println("\nInvalid input\n");
            }
        }
    }

    void goToSection(Section section) {
        int minInputInt = section.getMinInputInt();
        int maxInputInt = section.getMaxInputInt();
        String sectionInfo = getStringWithNewLineChars(section.getInfo());
        String sectionChoicesString = getSectionChoicesString(section);
        System.out.println();

        while (true) {
            System.out.println(sectionChoicesString);
            String input = inputReader.nextLine().toLowerCase();
            switch (input) {
                case "m":
                    System.out.println();
                    return;

                case "i":
                    System.out.println('\n' + sectionInfo + '\n');
                    break;

                default:
                    int firstNumber = 0, secondNumber = 0;

                    if (input.equals("r")) {
                        // generate random numbers that are in the valid range
                        firstNumber = Math.max(random.nextInt(maxInputInt), minInputInt);
                        // Goldbach section requires even number
                        if (section == Section.GOLDBACH && firstNumber % 2 != 0) {
                            firstNumber++;
                        } else if (section == Section.GCD_LCM) {
                            // GCD and LCM section needs 2 numbers
                            secondNumber = Math.max(random.nextInt(maxInputInt), minInputInt);
                        }
                    } else {
                        // Check input for number or numbers and validate that these numbers are in the appropriate range
                        boolean inputError = false;
                        try {
                            if (section == Section.GCD_LCM) {
                                // GCD and LCM section has user enter 2 numbers separated by a space
                                if (input.isEmpty()) {
                                    inputError = true;
                                } else {
                                    String[] inputContents = input.split(" ");
                                    if (inputContents.length != 2) {
                                        inputError = true;
                                    } else {
                                        firstNumber = Integer.parseInt(inputContents[0]);
                                        secondNumber = Integer.parseInt(inputContents[1]);
                                        if (secondNumber < minInputInt || secondNumber > maxInputInt) {
                                            inputError = true;
                                        }
                                    }
                                }
                            } else {
                                firstNumber = Integer.parseInt(input);
                            }

                            // Goldbach section requires even number
                            if (firstNumber < minInputInt || firstNumber > maxInputInt ||
                                    (section == Section.GOLDBACH && firstNumber % 2 != 0)) {
                                inputError = true;
                            }
                        } catch (NumberFormatException ex) {
                            // This block is reached if the user's input was not able to be parsed as an int
                            inputError = true;
                        }

                        if (inputError) {
                            System.out.println("\nInvalid input\n");
                            continue;
                        }
                    }

                    System.out.println();
                    System.out.println(
                            section == Section.GCD_LCM ? getDoubleInputAnswerString(section, firstNumber, secondNumber)
                                    : getSingleInputAnswerString(section, firstNumber)
                    );
                    System.out.println();
            }
        }
    }

    /**
     * @return A string that says what section the user is at and what inputs a user can provide for the section and
     * what each input does.
     */
    String getSectionChoicesString(Section section) {
        StringBuilder sb = new StringBuilder("You're at the ");
        switch (section) {
            case PRIMES:
            case DIVISIBILITY:
                sb.append(section.toString().toLowerCase()).append(" ");
                break;

            case TWIN_PRIMES:
                sb.append("twin prime numbers ");
                break;

            case PRIME_FACTORIZATION:
                sb.append("prime factorization ");
                break;

            case GCD_LCM:
                sb.append("GCD and LCM ");
                break;

            case GOLDBACH:
                sb.append("Goldbach Conjecture ");
                break;

            case PYTHAG_TRIPLES:
                sb.append("Pythagorean triples ");
                break;
        }
        sb.append("section.\nType one of the following and press enter.\n");

        switch (section) {
            case PRIMES:
                sb
                        .append("A number to get the first 30 prime numbers after it. Have this number be greater ")
                        .append("\n\tthan or equal to 0 and less than or equal to 1 billion")
                        .append("\n\"r\" to generate a random number and get the first 30 prime numbers after it")
                        .append("\n\"i\" for information about prime numbers");
                break;

            case TWIN_PRIMES:
                sb
                        .append("A number that to get the first 20 pairs of twin primes after it. Have this number be ")
                        .append("\n\tgreater than or equal to 0 and less than or equal to 1 billion")
                        .append("\n\"r\" to generate a random number and get the first 20 pairs of twin primes after it")
                        .append("\n\"i\" for information about twin primes");
                break;

            case PRIME_FACTORIZATION:
                sb
                        .append("A number to get it's prime factorization. Have this number should be greater than or ")
                        .append("\n\tequal to 2 and less than or equal to 10 thousand")
                        .append("\n\"r\" to generate a random number and get it's prime factorization")
                        .append("\n\"i\" for information about prime factorizations");
                break;

            case DIVISIBILITY:
                sb
                        .append("A number to get the divisibility info about it. Have this number should be greater than ")
                        .append("\n\tor equal to 2 and less than or equal to 10 thousand")
                        .append("\n\"r\" to generate a random number and get the divisibility info of it")
                        .append("\n\"i\" for information about divisibility");
                break;

            case GCD_LCM:
                sb
                        .append("2 numbers to get the GCD and LCM info for them. Have these numbers be greater than ")
                        .append("\n\tor equal to 2 and less than or equal to 10 thousand and separated by a single space.")
                        .append("\n\"r\" to generate 2 random numbers and get the GCD and LCM info for them")
                        .append("\n\"i\" for information about GCDs and LCMs");
                break;

            case GOLDBACH:
                sb
                        .append("A number to get the pairs of prime numbers that sum to it. Have this ")
                        .append("\n\tnumber be even, greater than or equal to 4, and less than or equal to 100 thousand")
                        .append("\n\"r\" to generate a random even number and get the pairs of prime numbers that sum to it")
                        .append("\n\"i\" for information about the Goldbach conjecture");
                break;

            case PYTHAG_TRIPLES:
                sb
                        .append("A number to get the first 10 Pythagorean triples after it. Have this number be greater ")
                        .append("\n\tthan or equal to 0 and less than or equal to 1 thousand")
                        .append("\n\"r\" to generate a random number and get the first 10 Pythagorean triples after it")
                        .append("\n\"i\" for information about Pythagorean triples");
                break;
        }

        sb.append("\n\"m\" to go to the menu");
        return sb.toString();
    }

    /**
     * @return A new string that is the same as the argument string but with new line characters inserted. Each portion
     * of a string between new line characters is at most 90 characters long. A new line character will not be inserted
     * in the middle of a word but instead will be inserted to replace whitespace.
     */
    String getStringWithNewLineChars(final String s) {
        final int lineLength = 90;
        StringBuilder sb = new StringBuilder(s);
        int stringIndex = lineLength;
        while (stringIndex < s.length()) {
            while (sb.charAt(stringIndex) != ' ') {
                stringIndex--;
            }
            sb.replace(stringIndex, stringIndex + 1, "\n");
            stringIndex += lineLength;
        }
        return sb.toString();
    }

    /**
     * @return An answer string for sections that require the user to provide 1 input number.
     * @throws IllegalArgumentException if the section argument is not for a section that needs 1 input number.
     */
    String getSingleInputAnswerString(Section section, int anInt) {
        String intString = Calculations.getNumberStringWithCommas(anInt);
        StringBuilder sb = new StringBuilder();

        switch (section) {
            case PRIMES:
                // Build string of some primes that are greater than or equal to anInt
                // and have 6 primes be on each line.
                List<String> primes = Calculations.getPrimesAfter(anInt);

                sb
                        .append("The first ")
                        .append(primes.size())
                        .append(" primes greater than or equal to ")
                        .append(intString)
                        .append(" are:");

                for (int i = 0; i < primes.size(); i++) {
                    if (i % 6 == 0) {
                        sb.append('\n');
                    }
                    sb.append(primes.get(i)).append("   ");
                }
                break;

            case TWIN_PRIMES:
                // Build string of some pairs of twin primes that are greater than or equal to anInt
                // and have 4 pairs be on each line.
                List<String> twinPrimes = Calculations.getTwinPrimesAfter(anInt);
                sb
                        .append("The first ")
                        .append(twinPrimes.size())
                        .append(" pairs of twin primes after ")
                        .append(intString)
                        .append(" are:");

                for (int i = 0; i < twinPrimes.size(); i++) {
                    if (i % 4 == 0) {
                        sb.append('\n');
                    }
                    sb.append(twinPrimes.get(i)).append("   ");
                }
                break;

            case PRIME_FACTORIZATION:
                // Build string of the prime factorization of the argument number
                sb
                        .append("The prime factorization of ")
                        .append(intString)
                        .append(" is ")
                        .append(PrimeFactorization.getPfString(anInt));
                break;

            case DIVISIBILITY:
                // Build string of divisibility info for the argument number
                sb
                        .append("Divisibility info for ")
                        .append(intString)
                        .append(" acquired by using special tricks:\n")
                        .append(getStringWithNewLineChars(Calculations.getDivisInfoViaTricks(anInt)))
                        .append("\n\nInfo acquired from the prime factorization:");

                for (String infoLine : Calculations.getDivisInfoViaPf(anInt)) {
                    sb.append('\n').append(infoLine);
                }
                break;

            case GOLDBACH:
                // Build string of the prime number pairs that sum to the argument number and have 5 pairs be
                // on each line.
                List<String> goldbachPrimePairs = Calculations.getGoldbachPrimePairs(anInt);
                sb
                        .append("There are ")
                        .append(goldbachPrimePairs.size())
                        .append(" pairs of prime numbers that sum to ")
                        .append(intString)
                        .append(". They are:");

                for (int i = 0; i < goldbachPrimePairs.size(); i++) {
                    if (i % 5 == 0) {
                        sb.append('\n');
                    }
                    sb.append(goldbachPrimePairs.get(i)).append("   ");
                }
                break;

            case PYTHAG_TRIPLES:
                // Build string of some Pythagorean triples equations
                List<String> triplesEquations = Calculations.getPythagTriplesAfter(anInt);
                sb
                        .append("The first ")
                        .append(triplesEquations.size())
                        .append(" Pythagorean triples after ")
                        .append(intString)
                        .append(" are:");

                for (String equation : triplesEquations) {
                    sb.append('\n').append(equation);
                }
                break;

            default:
                throw new IllegalArgumentException("invalid section: " + section);
        }
        return sb.toString();
    }

    /**
     * @return An answer string for sections that require the user provide 2 input numbers.
     * @throws IllegalArgumentException if the section argument is not for a section that needs 2 input numbers.
     */
    String getDoubleInputAnswerString(Section section, int int1, int int2) {
        // Only section is the gcd and lcm section
        if (section != Section.GCD_LCM) {
            throw new IllegalArgumentException("invalid section: " + section);
        }

        String intString1 = Calculations.getNumberStringWithCommas(int1);
        String intString2 = Calculations.getNumberStringWithCommas(int2);

        // Build string of the GCD and LCM info for firstNumber and secondNumber
        StringBuilder sb = new StringBuilder()
                .append("GCD and LCM info for ")
                .append(intString1)
                .append(" and ")
                .append(intString2)
                .append(" acquired by looking at the prime factorizations:");

        for (String infoLine : Calculations.getGcdAndLcmInfoViaPf(int1, int2)) {
            sb.append('\n').append(infoLine);
        }

        sb.append("\n\nGCD info acquired from the Euclidean algorithm");
        for (String infoLine : Calculations.getEuclideanInfo(int1, int2)) {
            sb.append('\n').append(infoLine);
        }

        return sb.toString();
    }
}


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
