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
