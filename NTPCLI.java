import java.util.Random;
import java.util.Scanner;

/**
 * Launcher for the Number Theory Playground Command Line Interface.
 */
public class NTPCLI {
    final static Random random = new Random();
    final static Scanner inputReader = new Scanner(System.in);

    /**
     * Interacts with the user about which section to go to.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the command line version of the Number Theory Playground");
        while (true) {
            System.out.print(
                    "\nWould you like to:" +
                    "\n(1) Find prime numbers" +
                    "\n(2) Find twin primes" +
                    "\n(3) Get a number's prime factorization" +
                    "\n(4) Get a number's divisibility information" +
                    "\n(5) Get GCD and LCM information" +
                    "\n(6) Get a number's Goldbach pairs" +
                    "\n(7) Find Pythagorean triples" +
                    "\n(8) Exit" +
                    "\nEnter your choice: "
            );
            String input = inputReader.nextLine();
            switch (input) {
                case "1":
                    primesSection();
                    break;
                case "2":
                    twinPrimesSection();
                    break;
                case "3":
                    primeFactorizationSection();
                    break;
                case "4":
                    divisSection();
                    break;
                case "5":
                    gcdAndLcmSection();
                    break;
                case "6":
                    goldbachSection();
                    break;
                case "7":
                    pythagTriplesSection();
                    break;
                case "8":
                    System.out.println("\nI hope you found this interesting");
                    System.exit(0);
                default:
                    System.out.println("\nInvalid input");
                    break;
            }
        }
    }

    /**
     * Interacts with the user about prime numbers.
     */
    static void primesSection() {
        while (true) {
            System.out.println(
                    "\nType one of the following and press enter:" +
                    "\nA number less than or equal to 1 billion to get the first 30 prime numbers after it" +
                    "\n\"r\" to generate a random number and get the first 30 prime numbers after it" +
                    "\n\"i\" for information about prime numbers" +
                    "\n\"m\" to go to the menu"
            );
            int number;
            String input = inputReader.nextLine();
            switch (input.toLowerCase()) {
                case "m": return;

                case "i":
                    System.out.println();
                    for (String infoLine : Primes.getPrimesSectionInfo()) {
                        System.out.println(infoLine);
                    }
                    break;

                case "r":
                    number = random.nextInt(1_000_000_000);
                    System.out.println("\nThe primes after " + number + " are:");
                    for (String primesLine : Primes.getPrimesAfter(number)) {
                        System.out.println(primesLine);
                    }
                    break;

                default:
                    // If the user typed something that is not one of the above options, check if it's a number
                    // in the valid range and display the appropriate calculations if it is.
                    try {
                        number = Integer.parseInt(input);
                    } catch (NumberFormatException ex) {
                        System.out.println("\nInvalid input");
                        break;
                    }
                    if (number <= 1_000_000_000) {
                        System.out.println("\nThe primes after " + number + " are:");
                        for (String primesLine : Primes.getPrimesAfter(number)) {
                            System.out.println(primesLine);
                        }
                    } else {
                        System.out.println("\nInvalid input");
                    }
                    break;
            }
        }
    }

    /**
     * Interacts with the user about twin primes.
     */
    static void twinPrimesSection() {
        while (true) {
            System.out.println(
                    "\nType one of the following and press enter:" +
                    "\nA number that is less than or equal to 1 billion to get the first 20 pairs of " +
                    "twin primes after it" +
                    "\n\"r\" to generate a random number and get the first 20 pairs of twin primes after it" +
                    "\n\"i\" for information about twin primes" +
                    "\n\"m\" to go to the menu"
            );
            int number;
            String input = inputReader.nextLine();
            switch (input.toLowerCase()) {
                case "m": return;

                case "i":
                    System.out.println();
                    for (String infoLine : Primes.getTwinPrimesSectionInfo()) {
                        System.out.println(infoLine);
                    }
                    break;

                case "r":
                    number = random.nextInt(1_000_000_000);
                    System.out.println("\nTwin primes after " + number + ":\n");
                    for (String twinPrimesLine : Primes.getTwinPrimesAfter(number)) {
                        System.out.println(twinPrimesLine);
                    }
                    break;

                default:
                    // If the user typed something that is not one of the above options, check if it's a number
                    // in the valid range and display the appropriate calculations if it is.
                    try {
                        number = Integer.parseInt(input);
                    } catch (NumberFormatException ex) {
                        System.out.println("\nInvalid input");
                        continue;
                    }
                    if (number <= 1_000_000_000) {
                        System.out.println("\nTwin primes after " + number + ":\n");
                        for (String twinPrimesLine : Primes.getTwinPrimesAfter(number)) {
                            System.out.println(twinPrimesLine);
                        }
                    } else {
                        System.out.println("\nInvalid input");
                    }
                    break;
            }
        }
    }

    /**
     * Interacts with the user about prime factorizations.
     */
    static void primeFactorizationSection() {
        while (true) {
            System.out.println(
                    "\nType one of the following and press enter:" +
                    "\nA number to get it's prime factorization. This number should be greater than or " +
                    "equal to 2 and less than or equal to 10 million" +
                    "\n\"r\" to generate a random number and get it's prime factorization" +
                    "\n\"i\" for information about prime factorizations" +
                    "\n\"m\" to go to the menu"
            );
            int number;
            String input = inputReader.nextLine();
            switch (input) {
                case "m": return;

                case "i":
                    System.out.println();
                    for (String infoLine : PrimeFactorization.getSectionInfo()) {
                        System.out.println(infoLine);
                    }
                    break;

                case "r":
                    // Generate a random number that is greater than or equal to 2 and less than or equal to 10 million
                    number = Math.max(2, random.nextInt(10_000_000));
                    System.out.println("\nPrime factorization for " + number + ":\n" +
                            PrimeFactorization.getPfString(number));
                    break;

                default:
                    // If the user typed something that is not one of the above options, check if it's a number
                    // in the valid range and display the appropriate calculations if it is.
                    try {
                        number = Integer.parseInt(input);
                    } catch (NumberFormatException ex) {
                        System.out.println("\nInvalid input");
                        break;
                    }
                    if (number >= 2 && number <= 10_000_000) {
                        System.out.println("\nPrime factorzation for " + number + ":\n" +
                                PrimeFactorization.getPfString(number));
                    } else {
                        System.out.println("\nInvalid input");
                    }
                    break;
            }
        }
    }

    /**
     * Interacts with the user about divisibility.
     */
    static void divisSection() {
        while (true) {
            System.out.println(
                    "\nType one of the following and press enter:" +
                    "\nA number to get the divisibility info about it. This number should be greater than or equal " +
                    "to 2 and less than or equal to 10 million" +
                    "\n\"r\" to generate a random number and get the divisibility info of it" +
                    "\n\"i\" for information about divisibility" +
                    "\n\"m\" to go to the menu"
            );
            int number;
            String input = inputReader.nextLine();
            switch (input.toLowerCase()) {
                case "m": return;

                case "i":
                    System.out.println();
                    for (String infoLine : Divisibility.getSectionInfo()) {
                        System.out.println(infoLine);
                    }
                    break;

                case "r":
                    // Generate a random number that is greater than or equal to 2 and less than or equal to 10 million
                    number = Math.max(2, random.nextInt(10_000_000));
                    System.out.println("\nDivisibility info for " + number + ":\n");
                    for (String infoLine : Divisibility.getDivisInfoViaTricks(number)) {
                        System.out.println(infoLine);
                    }
                    System.out.println();
                    for (String infoLine : PrimeFactorization.getDivisInfo(number)) {
                        System.out.println(infoLine);
                    }
                    break;

                default:
                    // If the user typed something that is not one of the above options, check if it's a number
                    // in the valid range and display the appropriate calculations if it is.
                    try {
                        number = Integer.parseInt(input);
                    } catch (NumberFormatException ex) {
                        System.out.println("\nInvalid input");
                        continue;
                    }
                    if (number >= 2 && number <= 10_000_000) {
                        System.out.println("\nDivisibility info for " + number + ":\n");
                        for (String infoLine : Divisibility.getDivisInfoViaTricks(number)) {
                            System.out.println(infoLine);
                        }
                        System.out.println();
                        for (String infoLine : PrimeFactorization.getDivisInfo(number)) {
                            System.out.println(infoLine);
                        }
                    } else {
                        System.out.println("\nInvalid input");
                    }
                    break;
            }
        }
    }

    /**
     * Interacts with the user about GCDs and LCMs.
     */
    static void gcdAndLcmSection() {
        while (true) {
            System.out.println(
                    "\nType one of the following and press enter:" +
                    "\n2 numbers to get the GCD and LCM info for them. Have these numbers be greater than " +
                    "or equal to 2 and less than or equal to 10 million and separated by a single space." +
                    "\n\"r\" to generate 2 random numbers and get the GCD and LCM info for them" +
                    "\n\"i\" for information about GCDs and LCMs" +
                    "\n\"m\" to go to the menu"
            );
            int firstNumber, secondNumber;
            String input = inputReader.nextLine();
            switch (input.toLowerCase()) {
                case "m": return;

                case "i":
                    System.out.println();
                    for (String infoLine : GcdAndLcm.getSectionInfo()) {
                        System.out.println(infoLine);
                    }
                    break;

                case "r":
                    // Generate random numbers that are greater than or equal to 2 and less than or equal to 10 million
                    firstNumber = Math.max(2, random.nextInt(10_000_000));
                    secondNumber = Math.max(2, random.nextInt(10_000_000));
                    System.out.println("\nGCD and LCM info for " + firstNumber + " and " + secondNumber + ":\n");
                    for (String infoLine : PrimeFactorization.getGcdAndLcmInfo(firstNumber, secondNumber)) {
                        System.out.println(infoLine);
                    }
                    System.out.println();
                    for (String infoLine : GcdAndLcm.getEuclideanInfo(firstNumber, secondNumber)) {
                        System.out.println(infoLine);
                    }
                    break;

                default:
                    // If the user typed something that is not one of the above options, check if it's 2 numbers
                    // in the valid range and separated by a single space and display the appropriate calculations 
                    // if it is.
                    String[] inputArray = input.split(" ");
                    if (inputArray.length != 2) {
                        System.out.println("\nInvalid input");
                        break;
                    }
                    try {
                        firstNumber = Integer.parseInt(inputArray[0]);
                        secondNumber = Integer.parseInt(inputArray[1]);
                    } catch (NumberFormatException ex) {
                        System.out.println("\nInvalid input");
                        break;
                    }
                    if (firstNumber >= 2 && firstNumber <= 10_000_000 &&
                            secondNumber >= 2 && secondNumber <= 10_000_000) {
                        System.out.println("\nGCD and LCM info for " + firstNumber + " and " + secondNumber + ":\n");
                        for (String infoLine : PrimeFactorization.getGcdAndLcmInfo(firstNumber, secondNumber)) {
                            System.out.println(infoLine);
                        }
                        System.out.println();
                        for (String infoLine : GcdAndLcm.getEuclideanInfo(firstNumber, secondNumber)) {
                            System.out.println(infoLine);
                        }
                    } else {
                        System.out.println("\nInvalid input");
                    }
                    break;
            }
        }
    }

    /**
     * Interacts with the user about the Goldbach conjecture.
     */
    static void goldbachSection() {
        while (true) {
            System.out.println(
                    "\nType one of the following and press enter:" +
                    "\nA number to get the pairs of prime numbers that sum to it. Have this " +
                    "number be even, greater than or equal to 4, and less than or equal to 100 thousand" +
                    "\n\"r\" to generate a random even number and get the pairs of prime numbers that sum to it" +
                    "\n\"i\" for information about the Goldbach conjecture" +
                    "\n\"m\" to go to the menu"
            );
            int number;
            String input = inputReader.nextLine();
            switch (input.toLowerCase()) {
                case "m": return;

                case "i":
                    System.out.println();
                    for (String infoLine : Goldbach.getSectionInfo()) {
                        System.out.println(infoLine);
                    }
                    break;

                case "r":
                    // Generate a random number that is even, greater than or equal to 4, and less than or equal
                    // to 100 thousand
                    number = Math.max(4, random.nextInt(100_000));
                    if (number % 2 != 0) {
                        number++;
                    }
                    System.out.println("\nPairs of prime numbers that sum to " + number + ":\n");
                    for (String pairsLine : Goldbach.getGoldbachPairs(number)) {
                        System.out.println(pairsLine);
                    }
                    break;

                default:
                    // If the user typed something that is not one of the above options, validate it
                    // and display the appropriate calculations if it's valid.
                    try {
                        number = Integer.parseInt(input);
                    } catch (NumberFormatException ex) {
                        System.out.println("\nInvalid input");
                        break;
                    }
                    if (number >= 4 && number <= 100_000 && number % 2 == 0) {
                        System.out.println("\nPairs of prime numbers that sum to " + number + ":\n");
                        for (String pairsLine : Goldbach.getGoldbachPairs(number)) {
                            System.out.println(pairsLine);
                        }
                    } else {
                        System.out.println("\nInvalid input");
                    }
                    break;
            }
        }
    }

    /**
     * Interacts with the user about Pythagorean triples.
     */
    static void pythagTriplesSection() {
        while (true) {
            System.out.println(
                    "\nType one of the following and press enter:" +
                    "\nA number that is less than or equal to 1 thousand to get the first 10 Pythagorean " +
                    "triples after it" +
                    "\n\"r\" to generate a random number and get the first 10 Pythagorean triples after it" +
                    "\n\"i\" for information about Pythagorean triples" +
                    "\n\"m\" to go to the menu"
            );
            int number;
            String input = inputReader.nextLine();
            switch (input.toLowerCase()) {
                case "m": return;

                case "i":
                    System.out.println();
                    for (String infoLine : PythagoreanTriples.getSectionInfo()) {
                        System.out.println(infoLine);
                    }
                    break;

                case "r":
                    number = random.nextInt(1_000);
                    System.out.println("\nPythagorean triples after " + number + ":\n");
                    for (String infoLine : PythagoreanTriples.getTriples(number)) {
                        System.out.println(infoLine);
                    }
                    break;

                default:
                    // If the user typed something that is not one of the above options, check if it's a number
                    // in the valid range and display the appropriate calculations if it is.
                    try {
                        number = Integer.parseInt(input);
                    } catch (NumberFormatException ex) {
                        System.out.println("\nInvalid input");
                        continue;
                    }
                    if (number <= 1_000) {
                        System.out.println("\nPythagorean triples after " + number + ":\n");
                        for (String triplesLine : PythagoreanTriples.getTriples(number)) {
                            System.out.println(triplesLine);
                        }
                    } else {
                        System.out.println("\nInvalid input");
                    }
            }
        }
    }
}
