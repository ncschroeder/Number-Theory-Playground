package com.nicholasschroeder.numbertheoryplayground;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Calculations {

    public static String getNumberStringWithCommas(int anInt) {
        String intString = String.valueOf(anInt);
        StringBuilder sb = new StringBuilder();
        int digitsAddedSinceLastComma = 0;
        for (int i = intString.length() - 1; i >= 0; i--) {
            if (digitsAddedSinceLastComma == 3) {
                sb.insert(0, ",");
                digitsAddedSinceLastComma = 0;
            }
            sb.insert(0, intString.charAt(i));
            digitsAddedSinceLastComma++;
        }
        return sb.toString();
    }

    private static boolean isPrime(int anInt) {
        // All numbers less than 2 are not prime
        if (anInt < 2) {
            return false;
        }

        // Check if the argument int is divisible by any prime numbers less than or equal to the
        // floor of the square root of the argument number. If it is, then the argument int is not prime.
        // All prime numbers besides 2 and 3 are either 1 below or 1 above a multiple of 6.

        if (anInt % 2 == 0 && anInt != 2) {
            return false;
        }

        int highestNumberToCheck = (int) Math.sqrt(anInt);
        for (int potentialPrime = 3; potentialPrime <= highestNumberToCheck; potentialPrime += 2) {
            if (anInt % potentialPrime == 0) {
                return false;
            }
        }
        return true;
    }



    /**
     * @return A list of the first 30 prime numbers that are greater than or equal to the argument number.
     * Each number is represented as a string with commas where appropriate.
     */
    public static List<String> getPrimesAfter(int anInt) {
        ArrayList<String> primes = new ArrayList<>(30);

        int possiblePrime;
        if (anInt <= 2) {
            primes.add("2");
            // Set possiblePrime to the first odd prime number
            possiblePrime = 3;
        } else {
            // set possiblePrime to the first odd int greater than or equal to the argument int
            possiblePrime = anInt % 2 == 0 ? anInt + 1 : anInt;
        }

        // Check odd numbers for primality until 30 prime numbers are found
        for (;; possiblePrime += 2) {
            if (isPrime(possiblePrime)) {
                primes.add(getNumberStringWithCommas(possiblePrime));
                if (primes.size() == 30) {
                    return primes;
                }
            }
        }
    }



    /**
     * @return A list of strings that say the first 20 pairs of twin prime numbers that appear after the
     * argument number. Each string consists of the 2 numbers with commas where appropriate and each number
     * is separated by " and ".
     */
    public static List<String> getTwinPrimesAfter(int anInt) {
        ArrayList<String> twinPrimes = new ArrayList<>(20);

        /*
        With the exception of 2 and 3, all prime numbers are either 1 above or 1 below a multiple of 6. This means
        that all prime number pairs besides 3 and 5 consist of 1 number that is 1 below a multiple of 6 and the
        other number is 1 above that same multiple of 6. This algorithm takes advantage of this.
        */

        int possiblePrime1;
        if (anInt <= 3) {
            twinPrimes.add("3 and 5");
            // Set possiblePrime1 to the first positive number that is 1 below a multiple of 6
            possiblePrime1 = 5;
        } else {
            // Set possiblePrime1 to the first int greater than or equal to the argument int that is 1 below
            // a multiple of 6
            possiblePrime1 = anInt;
            while (possiblePrime1 % 6 != 5) {
                possiblePrime1++;
            }
        }

        // Check pairs of numbers that are possibly twin prime pairs until 20 twin prime pairs are found
        for (;; possiblePrime1 += 6) {
            int possiblePrime2 = possiblePrime1 + 2;
            if (isPrime(possiblePrime1) && isPrime(possiblePrime2)) {
                twinPrimes.add(getNumberStringWithCommas(possiblePrime1) + " and " + getNumberStringWithCommas(possiblePrime2));
                if (twinPrimes.size() == 20) {
                    return twinPrimes;
                }
            }
        }
    }


    /**
     * @return An list of Strings that contain info about what numbers the argument number is divisible by.
     * This info is acquired by using special tricks.
     */
    public static String getDivisInfoViaTricks(int anInt) {
        String info = "";
        String intString = getNumberStringWithCommas(anInt);
        boolean isEven = anInt % 2 == 0;
        if (!isEven) {
            info += intString + " is not even so it cannot be divisible by any even numbers";
        }
        int sumOfDigits = getSumOfDigits(anInt);
        info += "The sum of the digits is " + sumOfDigits;
        // A number is divisible by 3 if and only if the sum of it's digits is divisible by 3
        boolean isDivisibleBy3 = sumOfDigits % 3 == 0;
        if (isDivisibleBy3) {
            info += sumOfDigits + " is divisible by 3 so " + intString + " is divisible by 3";
            if (sumOfDigits % 9 == 0) {
                // A number is divisible by 9 if and only if the sum of it's digits is divisible by 9
                info += sumOfDigits + " is divisible by 9 so " + intString + " is divisible by 9";
            } else {
                info += sumOfDigits + " is not divisible by 9 so " + intString + " is not divisible by 9";
            }
        } else {
            info += sumOfDigits + " is not divisible by 3 so " + intString + " is not divisible by 3" +
                    "Since " + intString + " is not divisible by 3, it can't be divisible by 6, 9, 12, and any " +
                    "other multiples of 3";
        }

        if (isEven) {
            if (isDivisibleBy3) {
                // A number is divisible by 6 if it's even and divisible by 3.
                info += intString + " is even and divisible by 3 so it's also divisible by 6";
            }

            int last2Digits = Math.abs(anInt) % 100;
            info += "The last 2 digits are " + last2Digits;

            if (last2Digits % 4 == 0) {
                // A number is divisible by 4 if the last 2 digits are divisible by 4.
                info += last2Digits + " is divisible by 4 so " + intString + " is divisible by 4";

                int last3Digits = Math.abs(anInt) % 1000;
                info += "The last 3 digits are " + last3Digits;

                if (last3Digits % 8 == 0) {
                    // A number is divisible by 8 if the last 3 digits are divisible by 8.
                    info += last3Digits + " is divisible by 8 so " + intString + " is divisible by 8";
                } else {
                    info += last3Digits + " is not divisible by 8 so " + intString + " is not divisible by 8";
                }

                if (isDivisibleBy3) {
                    // A number is divisible by 12 if it's divisible by both 3 and 4.
                    info += intString + " is divisible by both 3 and 4 so it's also divisible by 12";
                }
            } else {
                info += last2Digits + " is not divisible by 4 so " + intString + " is not divisible by 4." +
                        "Since " + intString + " is not divisible by 4, " + intString + " also can't be " +
                        "divisible by 8, 12, and any other multiples of 4";
            }
        }
        return info;
    }

    private static int getSumOfDigits(int anInt) {
        String intString = String.valueOf(anInt);
        int sum = 0;
        for (int i = 0; i < intString.length(); i++) {
            int digit = Character.getNumericValue(intString.charAt(i));
            sum += digit;
        }
        return sum;
    }
    

    /**
     * @return A list containing info about the Euclidean algorithm done on firstNumber and secondNumber. The strings in
     * this list contain info about iterations and there is another string at the end about which number is the GCD.
     * @throws IllegalArgumentException if firstNumber or secondNumber is nonpositive.
     */
    public static List<String> getEuclideanInfo(final int firstNumber, final int secondNumber) {
        if (firstNumber < 1 || secondNumber < 1) {
            throw new IllegalArgumentException("Can't find GCD for nonpositive numbers");
        }

        ArrayList<String> info = new ArrayList<>();

        int maxNumber = Math.max(firstNumber, secondNumber);
        int minNumber = Math.min(firstNumber, secondNumber);
        int remainder = maxNumber % minNumber;

        while (remainder != 0) {
            info.add(
                    maxNumber + " is not divisible by " + minNumber + ", so we now find the GCD of " +
                            minNumber + " and " + remainder
            );
            maxNumber = minNumber;
            minNumber = remainder;
            remainder = maxNumber % minNumber;
        }

        info.add(maxNumber + " is divisible by " + minNumber + ", so " + minNumber + " is the GCD of " +
                minNumber + " and " + maxNumber);

        // Check if maxNumber has changed
        if (maxNumber != Math.max(firstNumber, secondNumber)) {
            info.add("As a result, " + minNumber + " is the GCD of " + firstNumber + " and " + secondNumber);
        }
        return info;
    }


    /**
     * @return A list of strings that say the prime number pairs that sum up to the argument number. Each string contains
     * the 2 numbers separated by " and ".
     * @throws IllegalArgumentException if the argument number is less than 4 or is not even.
     */
    public static List<String> getGoldbachPrimePairs(int number) {
        if (number % 2 != 0 || number < 4) {
            throw new IllegalArgumentException("Number must be even and greater than or equal to 4");
        }

        ArrayList<String> pairs = new ArrayList<>();
        // All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6. First, check if the argument
        // number is 4 since 4 is the only even number greater than or equal to 4 that has 2 in a pair of prime numbers
        // that sum to it.
        if (number == 4) {
            pairs.add("2 and 2");
            return pairs;
        }

        // Check if 3 and a prime number sum to the argument number.
        if (isPrime(number - 3)) {
            pairs.add("3 and " + (number - 3));
        }

        // Check if a potential prime number and the difference between that potential prime number and the argument
        // number are both prime.

        // The max potential prime number that needs to be checked is equal to the floor of half of the argument number.
        // This is because after that point, calculations for primality will be done on numbers that have already been
        // determined to be pairs of prime numbers that sum to the argument number.

        int upperBound = number / 2;
        for (int possiblePrime1 = 5; possiblePrime1 <= upperBound; possiblePrime1 += (possiblePrime1 % 6 == 1 ? 4 : 2)) {
            int possiblePrime2 = number - possiblePrime1;
            if (isPrime(possiblePrime1) && isPrime(possiblePrime2)) {
                pairs.add(getNumberStringWithCommas(possiblePrime1) + " and " + getNumberStringWithCommas(possiblePrime2));
            }
        }
        return pairs;
    }



    /**
     * @return A list of strings that say the first 10 Pythagorean triples that appear after the number passed in as
     * an argument. Each string consists of what the 3 numbers are along with their squares in parentheses.
     */
    public static List<String> getPythagTriplesAfter(int number) {
        List<String> triples = new ArrayList<>(10);

        // sideLength1 represents one of the lengths of one of the two short sides of a right triangle.
        // 3 is the lowest number to be in a Pythagorean triple so make sideLength1 be at least that.
        int sideLength1 = Math.max(number, 3);
        // sideLength2 represents the length of the other short side. This needs to be a long to prevent overflow.
        long sideLength2 = sideLength1 + 1;

        while (true) {
            double hypotLength = Math.hypot(sideLength1, sideLength2);
            if (hypotLength < sideLength2 + 1) {
                // sideLength2 + 1 is the minimum possible integer value for the hypotenuse length.
                // If the hypotenuse length is less than this, then the max value for sideLength2 for the current
                // value of sideLength1 has been exceeded.
                sideLength1++;
                sideLength2 = sideLength1 + 1;
            } else {
                if ((int) hypotLength == hypotLength) {
                    // A Pythagorean triple has been found. Add a string to the triples list. This string
                    // consists of what the 3 numbers are and what their squares are in parentheses.
                    triples.add(
                            sideLength1 + "^2 (" + (sideLength1 * sideLength1) + ") + " + sideLength2 +
                                    "^2 (" + (sideLength2 * sideLength2) + ") = " + (int) hypotLength + "^2 (" +
                                    (long) (hypotLength * hypotLength) + ")"
                    );

                    if (triples.size() == 10) {
                        return triples;
                    }
                }
                sideLength2++;
            }
        }
    }
}
