package contentpanels;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


// This class consists of an array of prime numbers and several methods that use those prime numbers
public class PrimeNumbers {
    public static int[] primesArray = new int[100];

    public static void fillPrimesArray() throws FileNotFoundException {
        File primeNumbersFile = new File("textfiles\\PrimeNumbers.txt");
        Scanner fileScanner = new Scanner(primeNumbersFile);
        for (int i = 0; i < 100; i++)
            primesArray[i] = fileScanner.nextInt();
        fileScanner.close();
    }

    public static TreeMap<Integer, Integer> getPFMap(int number) {
        // PF stands for prime factorization.
        // Returns a map whose keys are the prime factors and the values are the
        // powers of that prime factor.

//        if (number <= 1) return null;

        TreeMap<Integer, Integer> primeFactors = new TreeMap<Integer, Integer>();
        int changingNumber = number;
        int powerCount;
        int upperBound = (int)Math.floor(Math.sqrt(number));

        // First, get all the prime factors that are stored in primesArray.
        for (int prime : primesArray) {
            if (prime > upperBound) {
                primeFactors.put(changingNumber, 1);
                return primeFactors;
            }

            // When a prime factor is found, find it's power.
            if (changingNumber % prime == 0) {
                powerCount = 0;
                do {
                    powerCount++;
                    changingNumber /= prime;
                } while (changingNumber % prime == 0);
                primeFactors.put(prime, powerCount);
                if (changingNumber == 1)
                    return primeFactors;
                upperBound = (int) Math.floor(Math.sqrt(changingNumber));
            }
        }

        int highestKnownPrime = primesArray[primesArray.length - 1];
        int potentialPrime;

        // highestKnownPrime is either one above or one below a multiple of 6.
        if ((highestKnownPrime + 1) % 6 == 0) {
            if (changingNumber % (highestKnownPrime + 2) == 0) {
                highestKnownPrime = highestKnownPrime + 2;
                powerCount = 0;
                do {
                    powerCount++;
                    changingNumber /= highestKnownPrime;
                } while (changingNumber % highestKnownPrime == 0);
                primeFactors.put(highestKnownPrime, powerCount);
                if (changingNumber == 1)
                    return primeFactors;
                upperBound = (int) Math.floor(Math.sqrt(changingNumber));
            }
        }
        potentialPrime = highestKnownPrime + 4;

        for (int i; ; potentialPrime += 4) {
            for (i = 0; i < 2; i++, potentialPrime += 2) {
                if (potentialPrime > upperBound) {
                    primeFactors.put(changingNumber, 1);
                    return primeFactors;
                }
                if (changingNumber % potentialPrime == 0) {
                    powerCount = 0;
                    do {
                        powerCount++;
                        changingNumber /= potentialPrime;
                    } while (changingNumber % potentialPrime == 0);
                    primeFactors.put(potentialPrime, powerCount);
                    upperBound = (int) Math.floor(Math.sqrt(changingNumber));
                }
            }
        }

        /*
        while (true) {
            if (potentialPrime > upperBound) {
                primeFactors.put(changingNumber, 1);
                return primeFactors;
            }
            if (changingNumber % potentialPrime == 0) {
                powerCount = 0;
                do {
                    powerCount++;
                    changingNumber /= potentialPrime;
                } while (changingNumber % potentialPrime == 0);
                primeFactors.put(potentialPrime, powerCount);
                upperBound = (int) Math.floor(Math.sqrt(changingNumber));
            }
            potentialPrime += 2;
            if (potentialPrime > upperBound) {
                primeFactors.put(changingNumber, 1);
                return primeFactors;
            }
            if (changingNumber % potentialPrime == 0) {
                powerCount = 0;
                do {
                    powerCount++;
                    changingNumber /= potentialPrime;
                } while (changingNumber % potentialPrime == 0);
                primeFactors.put(potentialPrime, powerCount);
                upperBound = (int) Math.floor(Math.sqrt(changingNumber));
            }
            potentialPrime += 4;
        }*/
    }

    public static String getPFString(TreeMap<Integer, Integer> PFMap) {
        // PF stands for prime factorization
        StringBuilder output = new StringBuilder();
        Set<Integer> primeFactors = PFMap.keySet();
        Integer power;
        for (Integer factor : primeFactors) {
            output.append(factor);
            power = PFMap.get(factor);
            if (power != 1)
                output.append("^").append(power);
            output.append(" * ");
        }

        // Delete last " * "
        output.delete(output.length() - 3, output.length());

        return output.toString();
    }

//    public static String getPFString(int number) {
//    }

    public static String getPFGcdAndLcm(int firstNumber, int secondNumber) {
        // PF stands for prime factorization
        TreeMap<Integer, Integer> PF1 = getPFMap(firstNumber);
        TreeMap<Integer, Integer> PF2 = getPFMap(secondNumber);
        Set<Integer> primeFactors1 = PF1.keySet();
        Set<Integer> primeFactors2 = PF2.keySet();
        TreeMap<Integer, Integer> gcdPF = new TreeMap<Integer, Integer>();
        TreeMap<Integer, Integer> lcmPF = new TreeMap<Integer, Integer>();
//        StringBuilder output = new StringBuilder();

        int firstPower, secondPower;
        for (Integer factor : primeFactors1) {
            if (primeFactors2.contains(factor)) {
                firstPower = PF1.get(factor);
                secondPower = PF2.get(factor);
                if (firstPower > secondPower) {
                    gcdPF.put(factor, secondPower);
                    lcmPF.put(factor, firstPower);
                } else {
                    gcdPF.put(factor, firstPower);
                    lcmPF.put(factor, secondPower);
                }
            }
        }

        // If there are no common prime factors then the GCD is 1.
        if (gcdPF.isEmpty())
            gcdPF.put(1, 1);

        for (Integer factor : primeFactors1) {
            if (!primeFactors2.contains(factor))
                lcmPF.put(factor, PF1.get(factor));
        }

        for (Integer factor : primeFactors2) {
            if (!primeFactors1.contains(factor))
                lcmPF.put(factor, PF2.get(factor));
        }

        return "The prime factorization of " + firstNumber + " is " + getPFString(getPFMap(firstNumber)) +
                "\nThe prime factorization of " + secondNumber + " is " + getPFString(getPFMap(secondNumber)) +
                "\nThe prime factorization of the GCD is " + getPFString(gcdPF) + ", which is " +
                getPFProduct(gcdPF) + "\nThe prime factorization of the LCM is " + getPFString(lcmPF) +
                ", which is " + getPFProduct(lcmPF);

//        output.append("The prime factorization of ").append(firstNumber).append(" is ")
//                .append(getPFString(getPFMap(firstNumber))).append("\nand the prime factorization of ")
//                .append(secondNumber).append(" is ").append(getPFString(getPFMap(secondNumber)));
        /*
        output.append("First, let's look at the prime factorization of these 2 numbers.\n")
                .append("The prime factorization of ").append(firstNumber).append(" is ")
                .append(getPFString(PF1)).append(" and\n").append("the prime factorization of ")
                .append(secondNumber).append(" is ").append(getPFString(PF2))
                .append("\nLet's look at the common prime factors.\n");

        // Find common factors
        int firstPower, secondPower;
        for (Integer factor : primeFactors1) {
            if (primeFactors2.contains(factor)) {
                firstPower = PF1.get(factor);
                secondPower = PF2.get(factor);

                output.append(factor).append(" is a common factor. One of the powers is ")
                        .append(firstPower).append(" and the other is ").append(secondPower)
                        .append(".\nThe max of these is in the LCM and the minimum is ")
                        .append("in the GCD.\n");

                if (firstPower > secondPower) {
                    output.append(factor).append("^").append(firstPower)
                            .append(" is in the LCM and ").append(factor).append("^")
                            .append(secondPower).append(" is in the GCD.\n");

                    gcdPF.put(factor, secondPower);
                    lcmPF.put(factor, firstPower);

                } else if (firstPower < secondPower) {
                    output.append(factor).append("^").append(secondPower)
                            .append(" is in the LCM and ").append(factor).append("^")
                            .append(firstPower).append(" is in the GCD.\n");

                    gcdPF.put(factor, firstPower);
                    lcmPF.put(factor, secondPower);

                } else {

                    output.append("The powers are equal so ").append(factor).append("^")
                            .append(firstPower).append(" goes in both the GCD and LCM\n");

                    gcdPF.put(factor, firstPower);
                    lcmPF.put(factor, firstPower);
                }
            }
        }

        output.append("Now let's look for unique factors.\n");
        for (Integer factor : primeFactors1) {
            if (!primeFactors2.contains(factor)) {
                output.append(factor).append(" is a unique factor of ").append(firstNumber)
                        .append(" so ").append(factor).append("^").append(PF1.get(factor))
                        .append(" is in the LCM.\n");
                lcmPF.put(factor, PF1.get(factor));
            }
        }

        for (Integer factor : primeFactors2) {
            if (!primeFactors1.contains(factor)) {
                output.append(factor).append(" is a unique factor of ").append(secondNumber)
                        .append(" so we add ").append(factor).append("^").append(PF2.get(factor))
                        .append(" to the LCM and do nothing to the GCD\n");
                lcmPF.put(factor, PF2.get(factor));
            }
        }

        output.append("The prime factorization of the GCD is ").append(getPFString(gcdPF))
                .append(" which is ").append(getPFProduct(gcdPF)).append("\n");
        output.append("The prime factorization of the LCM is ").append(getPFString(lcmPF))
                .append(" which is ").append(getPFProduct(lcmPF));
*/
//        return output.toString();
    }

    public static int getPFProduct(TreeMap<Integer, Integer> PFMap) {
        int product = 1;
        Set<Integer> primeFactors = PFMap.keySet();
        for (Integer factor : primeFactors)
            product *= Math.pow(factor, PFMap.get(factor));
        return product;
    }

    public static int getPFNumberOfFactors(TreeMap<Integer, Integer> PFMap) {
        int numberOfFactors = 1;
        Collection<Integer> powers = PFMap.values();
        for (Integer power : powers)
            numberOfFactors *= (power + 1);
        return numberOfFactors;
    }

    static boolean isPrime(int number) {
        // Need to check if the number is divisible by any prime numbers less than or
        // equal to the square root of the number passed in.

        if (number <= 1) return false;

        int upperBound = (int)Math.floor(Math.sqrt(number));

        // 1st, iterate through primesArray
        for (int prime : primesArray) {
            if (prime > upperBound)
                return true;
            if (number % prime == 0)
                return false;
        }

        // If there are remaining prime factors, calculate them manually instead of relying on
        // contents of an array.
        // Every prime number is 1 below or 1 above a multiple of 6. Set potentialPrime to the
        // next number that is 1 below a multiple of 6 to check for primality.
        int highestKnownPrime = primesArray[primesArray.length - 1];
        int potentialPrime;
        if ((highestKnownPrime + 1) % 6 == 0) {
            if (number % (highestKnownPrime + 2) == 0)
                return false;
            potentialPrime = highestKnownPrime + 6;
        }
        else
            potentialPrime = highestKnownPrime + 4;

        for (; potentialPrime <= upperBound; potentialPrime += 6) {
            if (number % potentialPrime == 0 || number % (potentialPrime + 2) == 0)
                return false;
        }
        return true;
    }

    public static String getGoldbachPairs(int number) {
        // Assume number is even.

        // 4 is a special case since it is the only one that has 2 in it's solution.
        if (number == 4)
            return "2 and 2";

        StringBuilder output = new StringBuilder();
        int numberToCheck;
        int upperBound = number / 2;

        // First, iterate through primesArray. i starts at 1 to skip 2 in primesArray.
        for (int i = 1; i < primesArray.length; i++) {
            if (primesArray[i] > upperBound)
                break;
            numberToCheck = number - primesArray[i];
            if (isPrime(numberToCheck)) {
                output.append(primesArray[i]).append(" and ").append(numberToCheck).append(", ");
            }
        }

        // Delete last comma
        output.delete(output.length() - 2, output.length());
        return output.toString();
    }

    public static String[] getTwinPrimes(int number) {
        // Finds first 100 twin primes after the number passed in as an argument.
        // Returns an array of strings where each element is a string containing 10 twin primes.

        String[] output = new String[10];
        StringBuilder arrayElement = new StringBuilder();
        int count = 0;

        // First, find if input number is in primesArray
        if (number < primesArray[primesArray.length - 1]) {

        }

        return output;
    }
}
