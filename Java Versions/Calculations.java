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



    private static class PrimeFactorization {
        private long number;

        /**
         * A map whose keys are the prime factors of a number and the values are the powers those factors are raised to
         * to form the prime factorization of a number.
         */
        private final Map<Integer, Integer> pfMap;

        /**
         * @throws IllegalArgumentException if the argument number is less than 2.
         */
        public PrimeFactorization(int number) {
            if (number < 2) {
                throw new IllegalArgumentException("Cannot get prime factorization of number less than 2");
            }
            this.number = number;
            pfMap = new TreeMap<>();

            // Find all the prime factors and their powers and put these in pfMap. Divide the number variable by each
            // prime factor that is found. When the number variable becomes 1, the entire prime factorization
            // has been found. All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so first
            // 2 and 3 will be checked to see if they're prime factors and then numbers that are either 1 above or 1 below
            // a multiple of 6 will be checked.

            for (int potentialPrimeFactor = 2; potentialPrimeFactor <= 3; potentialPrimeFactor++) {
                if (number % potentialPrimeFactor == 0) {
                    int power = 0;
                    do {
                        power++;
                        number /= potentialPrimeFactor;
                    } while (number % potentialPrimeFactor == 0);
                    pfMap.put(potentialPrimeFactor, power);
                    if (number == 1) {
                        return;
                    }
                }
            }

            for (int potentialPrimeFactor = 5; ; potentialPrimeFactor += (potentialPrimeFactor % 6 == 1 ? 4 : 2)) {
                if (number % potentialPrimeFactor == 0) {
                    int power = 0;
                    do {
                        power++;
                        number /= potentialPrimeFactor;
                    } while (number % potentialPrimeFactor == 0);
                    pfMap.put(potentialPrimeFactor, power);
                    if (number == 1) {
                        return;
                    }
                }
            }
        }

        /**
         * @param pfMap A map representing the prime factorization of a number. The keys are the prime factors and the
         *              values are the powers of those prime factors.
         * @throws IllegalArgumentException if pfMap is empty.
         */
        public PrimeFactorization(Map<Integer, Integer> pfMap) {
            if (pfMap.isEmpty()) {
                throw new IllegalArgumentException("Cannot create prime factorization from an empty map");
            }
            this.pfMap = pfMap;
            // number is the product of all prime factors raised to their powers
            this.number = 1;
            for (int primeFactor : pfMap.keySet()) {
                int power = pfMap.get(primeFactor);
                this.number *= Math.pow(primeFactor, power);
            }
        }

        /**
         * @return A string representation of a prime factorization. " x " is used to represent multiplication among all
         * the prime factors and is placed between all the prime factors. "^" is used to represent the powers of prime
         * factors and is used for prime factors that have a power other than 1.
         */
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int primeFactor : pfMap.keySet()) {
                sb.append(getNumberStringWithCommas(primeFactor));
                int power = pfMap.get(primeFactor);
                if (power != 1) {
                    sb.append("^").append(power);
                }
                sb.append(" x ");
            }
            // Delete last " x "
            sb.delete(sb.length() - 3, sb.length());
            return sb.toString();
        }

        /**
         *
         * @return The number that this prime factorization is for.
         */
        public long getNumber() {
            return number;
        }

        /**
         * @return true if the number that this prime factorization is representing is prime. For this case, the prime
         * factorization consists of a single number that has 1 as it's power. Returns false otherwise.
         */
        public boolean isForAPrimeNumber() {
            return pfMap.size() == 1 && pfMap.containsValue(1);
        }

        /**
         *
         * @param pf1
         * @param pf2
         * @return An array whose first element is a PrimeFactorization object for the GCD of pf1 and pf2 if the GCD
         * is not 1. If the GCD is 1 then this element is null. The second element is a PrimeFactorization object for
         * the LCM of pf1 and pf2.
         */
        public static PrimeFactorization[] getGcdAndLcmPfs(PrimeFactorization pf1, PrimeFactorization pf2) {
            // The prime factorization of the GCD of 2 numbers contains all the prime factors that are in both of the prime
            // factorizations of the 2 numbers. The power of each prime factor is the minimum of the 2 powers in the 2
            // prime factorizations. If there are no common prime factors then the GCD is 1.

            // The prime factorization of the LCM of 2 numbers contains all prime factors that are in either of the prime
            // factorizations for the 2 numbers. If a prime factor is in both prime factorizations then the power of that
            // prime factor in the LCM prime factorization is the max of the 2 powers in the 2 prime factorizations. If a
            // prime factor is unique to 1 of the prime factorizations of the 2 numbers, then the power of that prime factor
            // in the LCM prime factorization is the same as in the prime factorization for that 1 number.
            Map<Integer, Integer> gcdPfMap = new TreeMap<>();
            Map<Integer, Integer> lcmPfMap = new TreeMap<>();

            for (int primeFactor : pf1.pfMap.keySet()) {
                int power1 = pf1.pfMap.get(primeFactor);
                if (pf2.pfMap.containsKey(primeFactor)) {
                    // Common prime factors
                    int power2 = pf2.pfMap.get(primeFactor);
                    gcdPfMap.put(primeFactor, Math.min(power1, power2));
                    lcmPfMap.put(primeFactor, Math.max(power1, power2));
                } else {
                    // Prime factor is unique to the first prime factorization
                    lcmPfMap.put(primeFactor, power1);
                }
            }

            // Find the unique prime factors of the second prime factorization
            for (int primeFactor : pf2.pfMap.keySet()) {
                if (!pf1.pfMap.containsKey(primeFactor)) {
                    int power = pf2.pfMap.get(primeFactor);
                    lcmPfMap.put(primeFactor, power);
                }
            }

            return new PrimeFactorization[] {
                    gcdPfMap.isEmpty() ? null : new PrimeFactorization(gcdPfMap),
                    new PrimeFactorization(lcmPfMap)
            };
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
     * @return An list of Strings that consists of divisibility info about the argument number. This info is
     * acquired by looking at the prime factorization of the argument number.
     * @throws IllegalArgumentException if the argument number is less than 2.
     */
    public static List<String> getDivisInfoViaPf(int number) {
        return new ArrayList<>();
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
     * @return An array of 4 strings that contain info about the GCD and LCM of the two argument numbers. This info is
     * acquired by looking at the prime factorizations of the two argument numbers.
     * @throws IllegalArgumentException if int1 is less than 2 or int2 is less than 2.
     */
    public static String[] getGcdAndLcmInfoViaPf(int int1, int int2) {
        PrimeFactorization pf1 = new PrimeFactorization(int1);
        PrimeFactorization pf2 = new PrimeFactorization(int2);

        PrimeFactorization[] gcdAndLcmPfs = PrimeFactorization.getGcdAndLcmPfs(pf1, pf2);

        PrimeFactorization gcdPf = gcdAndLcmPfs[0];
        PrimeFactorization lcmPf = gcdAndLcmPfs[1];

        String gcdInfo;
        if (gcdPf == null) {
            gcdInfo = "There are no common prime factors so the GCD is 1";
        } else {
            gcdInfo = "The prime factorization of the GCD is " + gcdPf;
            if (!gcdPf.isForAPrimeNumber()) {
                gcdInfo += ", which is " + gcdPf.getNumber();
            }
        }

        String lcmInfo = "The prime factorization of the LCM is " + lcmPf;
        if (!lcmPf.isForAPrimeNumber()) {
            lcmInfo += ", which is " + lcmPf.getNumber();
        }

        return new String[] {
                "The prime factorization of " + getNumberStringWithCommas(int1) + " is " + pf1,
                "The prime factorization of " + getNumberStringWithCommas(int2) + " is " + pf2,
                gcdInfo,
                lcmInfo
        };
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
