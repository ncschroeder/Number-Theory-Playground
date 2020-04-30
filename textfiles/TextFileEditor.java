package com.numbertheoryplayground.textfiles;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileEditor {
    public static void main(String[] args) throws IOException {
        createFibonacciNumbers();
    }

    static void expandPrimeNumbersFile() throws IOException {
        int expansionAmount = 200;
        int numbersAdded = 2;
        int numbersPerLine = 10;
        FileWriter writer = new FileWriter("src\\com.numbertheoryplayground\\textfiles\\primeNumbers.txt");
        writer.write("2 3 ");
        int p = 5;
        while (numbersAdded < expansionAmount) {
            if (isPrime(p)) {
                writer.write(p + " ");
                numbersAdded++;
                if (numbersAdded % numbersPerLine == 0)
                    writer.write("\n");
            }

            if (isPrime(p + 2)) {
                writer.write((p + 2) + " ");
                numbersAdded++;
                if (numbersAdded % numbersPerLine == 0)
                    writer.write("\n");
            }
            p += 6;
        }
        writer.close();
    }

    static boolean isPrime(int number) {
        //From https://www.geeksforgeeks.org/primality-test-set-1-introduction-and-school-method/
        if (number <= 1) return false;
        if (number <= 3) return true;
        if (number % 2 == 0 || number % 3 == 0) return false;
        for (int i = 5; i * i <= number; i += 6)
            if (number % i == 0 || number % (i + 2) == 0)
                return false;
        return true;
    }

    static void createFibonacciNumbers() throws IOException {
        FileWriter writer = new FileWriter("src\\com.numbertheoryplayground\\textfiles\\fibonacciNumbers.txt");
        int[] fibonacciNumbers = new int[100];
        fibonacciNumbers[0] = 1;
        fibonacciNumbers[1] = 1;
        int numbersAdded = 2;

        for (int i = 2; i < 100; i++)
            fibonacciNumbers[i] = fibonacciNumbers[i - 1] + fibonacciNumbers[i - 2];

        for (int i : fibonacciNumbers) {
            writer.append(String.valueOf(i)).append(" ");
            numbersAdded++;
            if (numbersAdded % 10 == 0)
                writer.append("\n");
        }

        writer.close();
    }

    static void expandTwinPrimes() throws IOException {
        FileWriter writer = new FileWriter("src\\com.numbertheoryplayground\\textfiles\\twinPrimes.txt");

    }
}
