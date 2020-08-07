package com.numbertheoryplayground;

import java.util.Random;
import java.util.Scanner;

// Number Theory Playground Command Line Interface
public class NTPCLI {
    static Random random;
    static Scanner inputReader;

    public static void main(String[] args) {
        random = new Random();
        inputReader = new Scanner(System.in);
        System.out.println("Welcome to the command line version of the Number Theory Playground");
        while (true) {
            System.out.print("Would you like to:\n" +
                    "(1) Get a number's divisibility information\n" +
                    "(2) Find prime numbers\n" +
                    "(3) Find twin primes\n" +
                    "(4) Get a number's prime factorization\n" +
                    "(5)Get GCD and LCM information\n" +
                    "(6) Get a number's Goldbach pairs\n" +
                    "(7) Find Pythagorean triples\n" +
                    "(8) Exit\n" +
                    "Enter your choice: ");
            String input = inputReader.nextLine();
            switch (input) {
                case "1":
                    divisSection();
                    break;
                case "2":
                    primesSection();
                    break;
                case "3":
                    twinPrimesSection();
                    break;
                case "4":
                    primeFactorizationSection();
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
                    System.out.println("Hope you found this interesting");
                    System.exit(0);
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }

    static void divisSection() {
        while (true) {
            System.out.println("Enter a number to find out what numbers it is divisible by or enter m to go to the menu");
            String inputString = inputReader.nextLine();
            if (inputString.equals("m")) break;
            int inputInt;
            try {
                inputInt = Integer.parseInt(inputString);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input");
                continue;
            }
//            System.out.println(Functions.getDivisInfo(inputInt));
        }
    }

    static void primesSection() {
        while (true) {
            System.out.println("Enter a number to find the first 20 primes that appear after that number or press \"r\" " +
                    "to generate a random number or enter \"i\" for information about prime numbers or enter \"m\" to " +
                    "go to the menu");
            String userInput = inputReader.nextLine();
            if (userInput.equals("m")) break;
            if (userInput.equals("i")) {
                String info = "Prime numbers are numbers that are only divisible by 1 and themself. There are an infinite " +
                        "amount of them. Fun fact: not only are prime numbers odd (besides 2), but with the exception of " +
                        "2 and 3, all prime numbers are either 1 below or 1 above a multiple of 6";
                System.out.println(info);
                continue;
            }
            if (userInput.equals("r")) {
                int number = random.nextInt(1000);
                randomLoop:
                    while (true) {
                        System.out.print("Find primes after " + number + "? (y/n): ");
                        userInput = inputReader.nextLine();
                        if (userInput.equals("y")) {
                            System.out.println(Functions.getPrimes(number) + "\n");
                            while (true) {
                                System.out.print("Generate another random number? (y/n): ");
                                userInput = inputReader.nextLine();
                                if (userInput.equals("y")) {
                                    number = random.nextInt(1000);
                                    break;
                                }
                                if (userInput.equals("n")) break randomLoop;
                                System.out.print("Invalid input. ");
                            }
                        } else if (userInput.equals("n")) break;
                        System.out.print("Invalid input. ");
                    }
            } else {
                long number;
                try {
                    number = Integer.parseInt(userInput);
                } catch (NumberFormatException ex) {
                    System.out.print("Invalid input. ");
                    continue;
                }
                System.out.println(Functions.getPrimes(number) + "\n");
            }
        }
    }

    static void twinPrimesSection() {

    }

    static void primeFactorizationSection() {

    }

    static void gcdAndLcmSection() {
        System.out.println("You have selected GCD and LCM");
        while (true) {
            System.out.println("Enter the first number or enter \"r\" to generate random numbers or \"m\" to " +
                    "go to the menu");
            String userInput = inputReader.nextLine();
            if (userInput.equals("m")) break;
            if (userInput.equals("r")) {
                int firstNumber = random.nextInt(100);
                int secondNumber = random.nextInt(100);
                randomLoop:
                    while (true) {
                        System.out.print("Find out the GCD and LCM for " + firstNumber +
                                " and " + secondNumber + "? (y/n): ");
                        userInput = inputReader.nextLine();
                        if (userInput.equals("y")) {
                            System.out.println(Functions.getGcdAndLcmInfo(firstNumber, secondNumber));
                            while (true) {
                                System.out.print("Generate more random numbers? (y/n): ");
                                userInput = inputReader.nextLine();
                                if (userInput.equals("n")) break randomLoop;
                                else if (userInput.equals("y")) {
                                    firstNumber = random.nextInt(100);
                                    secondNumber = random.nextInt(100);
                                    break;
                                }
                                System.out.print("Invalid input. ");
                            }
                        } else if (userInput.equals("n")) {
                            while (true) {
                                System.out.print("Generate different random numbers? (y/n): ");
                                userInput = inputReader.nextLine();
                                if (userInput.equals("y")) {
                                    firstNumber = random.nextInt(100);
                                    secondNumber = random.nextInt(100);
                                    break;
                                }
                                if (userInput.equals("n")) break randomLoop;
                                System.out.println("Invalid input");
                            }
                        } else {
                            System.out.print("Invalid input. ");
                        }
                    }
            } else {
                // This block is reached if the user wants to choose which numbers to enter
                long firstNumber;
                try {
                    firstNumber = Integer.parseInt(userInput);
                } catch (Exception ex) {
                    System.out.print("Invalid input. ");
                    continue;
                }
                while (true) {
                    System.out.print("Enter second number or press b to go back: ");
                    userInput = inputReader.nextLine();
                    if (userInput.equals("b")) break;
                    long secondNumber;
                    try {
                        secondNumber = Integer.parseInt(userInput);
                    } catch (NumberFormatException ex) {
                        System.out.print("Invalid input. ");
                        continue;
                    }
                    System.out.println(Functions.getGcdAndLcmInfo(firstNumber, secondNumber));
                    break;
                }
            }
        }
    }

    static void goldbachSection() {

    }

    static void pythagTriplesSection() {

    }
}
