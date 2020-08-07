package com.numbertheoryplayground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

public class Functions {
    public static boolean isPrime(long number) {
        if (number < 2) return false;
        if (number <= 3) return true;
        if (number % 2 == 0 || number % 3 == 0) return false;
        int highestNumberToCheck = (int) Math.sqrt(number);
        for (int potentialPrime = 5; ; potentialPrime += 4) {
            for (int i = 0; i < 2; i++) {
                if (potentialPrime > highestNumberToCheck) return true;
                if (number % potentialPrime == 0) return false;
                if (i == 0) potentialPrime += 2;
            }
        }
    }

    private static TreeMap<Long, Integer> getPrimeFactorizationMap(long number) {
        if (number < 2) {
            throw new ArithmeticException("Cannot get prime factorization of number less than 2");
        }

        TreeMap<Long, Integer> primeFactorization = new TreeMap<>();
        long changingNumber = number;
        long highestNumberToCheck = (int) Math.sqrt(number);

        // First, check if the number has 2 and 3 as prime factors
        for (int potentialPrimeFactor = 2; potentialPrimeFactor <= 3; potentialPrimeFactor++) {
            if (potentialPrimeFactor > highestNumberToCheck) {
                primeFactorization.put(changingNumber, 1);
                return primeFactorization;
            }
            if (changingNumber % potentialPrimeFactor == 0) {
                int powerCount = 0;
                do {
                    powerCount++;
                    changingNumber /= potentialPrimeFactor;
                } while (changingNumber % potentialPrimeFactor == 0);
                primeFactorization.put((long) potentialPrimeFactor, powerCount);
                if (changingNumber == 1) {
                    return primeFactorization;
                }
            }
        }

        // Next, check potential prime factors, which are numbers that are either 1 above
        // or 1 below a multiple of 6
        for (long potentialPrimeFactor = 5; ; potentialPrimeFactor += 4) {
            for (int i = 0; i < 2; i++) {
                if (potentialPrimeFactor > highestNumberToCheck) {
                    primeFactorization.put(changingNumber, 1);
                    return primeFactorization;
                }
                if (changingNumber % potentialPrimeFactor == 0) {
                    int powerCount = 0;
                    do {
                        powerCount++;
                        changingNumber /= potentialPrimeFactor;
                    } while (changingNumber % potentialPrimeFactor == 0);
                    primeFactorization.put(potentialPrimeFactor, powerCount);
                    if (changingNumber == 1) {
                        return primeFactorization;
                    }
                }
                if (i == 0) potentialPrimeFactor += 2;
            }
        }
    }

    public static String getPrimeFactorizationString(long number) {
        StringBuilder answer = new StringBuilder();
        long changingNumber = number;
        long highestNumberToCheck = (int) Math.sqrt(number);

        // First, check if the number has 2 and 3 as prime factors
        for (int potentialPrimeFactor = 2; potentialPrimeFactor <= 3; potentialPrimeFactor++) {
            if (potentialPrimeFactor > highestNumberToCheck) {
                answer.append(changingNumber);
                return answer.toString();
            }
            if (changingNumber % potentialPrimeFactor == 0) {
                int powerCount = 0;
                do {
                    powerCount++;
                    changingNumber /= potentialPrimeFactor;
                } while (changingNumber % potentialPrimeFactor == 0);
                answer.append(potentialPrimeFactor);
                if (powerCount > 1) {
                    answer.append("^").append(powerCount);
                }
                answer.append(" * ");
                if (changingNumber == 1) {
                    // Delete last " * "
                    answer.delete(answer.length() - 2, answer.length());
                    return answer.toString();
                }
            }
        }

        // Next, check potential prime factors, which are numbers that are either 1 above
        // or 1 below a multiple of 6
        for (long potentialPrimeFactor = 5; ; potentialPrimeFactor += 4) {
            for (int i = 0; i < 2; i++) {
                if (potentialPrimeFactor > highestNumberToCheck) {
                    answer.append(changingNumber);
                    return answer.toString();
                }
                if (changingNumber % potentialPrimeFactor == 0) {
                    int powerCount = 0;
                    do {
                        powerCount++;
                        changingNumber /= potentialPrimeFactor;
                    } while (changingNumber % potentialPrimeFactor == 0);
                    answer.append(potentialPrimeFactor);
                    if (powerCount > 1) {
                        answer.append("^").append(powerCount);
                    }
                    answer.append(" * ");
                    if (changingNumber == 1) {
                        // Delete last " * "
                        answer.delete(answer.length() - 2, answer.length());
                        return answer.toString();
                    }
                }
                if (i == 0) potentialPrimeFactor += 2;
            }
        }
    }

    private static String getPrimeFactorizationString(TreeMap<Long, Integer> primeFactorizationMap) {
        StringBuilder PFStringBuilder = new StringBuilder();
        Set<Long> primeFactors = primeFactorizationMap.keySet();
        for (Long factor : primeFactors) {
            PFStringBuilder.append(factor);
            Integer power = primeFactorizationMap.get(factor);
            if (power != 1) {
                PFStringBuilder.append("^").append(power);
            }
            PFStringBuilder.append(" * ");
        }

        // Delete last " * "
        PFStringBuilder.delete(PFStringBuilder.length() - 3, PFStringBuilder.length());

        return PFStringBuilder.toString();
    }

    private static long getPrimeFactorizationProduct(TreeMap<Long, Integer> primeFacMap) {
        long product = 1;
        Set<Long> primeFactors = primeFacMap.keySet();
        for (Long factor : primeFactors) {
            product *= Math.pow(factor, primeFacMap.get(factor));
        }
        return product;
    }

    public static String[] getPrimes(long number) {
        int count = 0;
        StringBuilder line = new StringBuilder();
        String[] lines = new String[6];
        int insertionIndex = 0;
        long potentialPrime;
        if (number <= 5) {
            potentialPrime = 5;
            if (number <= 2) {
                line.append("2, 3, ");
                count += 2;
            } else if (number == 3) {
                line.append("3, ");
                count++;
            }
        } else {
            switch ((int) number % 6) {
                case 0:
                    if (isPrime(number + 1)) {
                        line.append(number + 1).append(",  ");
                        count++;
                    }
                    potentialPrime = number + 5;
                    break;
                case 1:
                    if (isPrime(number)) {
                        line.append(number).append(",  ");
                        count++;
                    }
                    potentialPrime = number + 4;
                    break;
                default:
                    potentialPrime = number;
                    while ((potentialPrime + 1) % 6 != 0) potentialPrime++;
                    break;
            }
        }

        // ALl prime numbers are either 1 below or 1 above a multiple of 6 so check these numbers
        // for primality until 30 primes are found.
        for (;; potentialPrime += 4) {
            for (int i = 0; i < 2; i++) {
                if (isPrime(potentialPrime)) {
                    line.append(potentialPrime).append(",  ");
                    count++;
                    if (count == 30) {
                        // Delete last comma
                        line.delete(line.length() - 3, line.length());
                        lines[insertionIndex] = line.toString();
                        return lines;
                    }
                    if (count % 5 == 0) {
                        // Start a new line
                        lines[insertionIndex] = line.toString();
                        line.delete(0, line.length());
                        insertionIndex++;
                    }
                }
                if (i == 0) potentialPrime += 2;
            }
        }
    }

    /**
     * @return A string containing the first 20 pairs of twin primes after the number passed in as an argument.
     */
    public static String[] getTwinPrimes(long number) {
        String[] lines = new String[5];
        StringBuilder lineSB = new StringBuilder();
        int insertionIndex = 0;
        int count = 0;
        if (number <= 3) {
            // 3 and 5 are the only special case since all other twin primes are for numbers that are one below and
            // one above a multiple of 6.
            lineSB.append("3 and 5");
            count++;
        }
        long potentialPrime;
        if (number < 5) potentialPrime = 5;
        else {
            potentialPrime = number;
            while ((potentialPrime + 1) % 6 != 0) potentialPrime++;
        }
        for (;; potentialPrime += 6) {
            if (isPrime(potentialPrime) && isPrime(potentialPrime + 2)) {
                lineSB.append(potentialPrime).append(" and ").append(potentialPrime + 2).append("     ");
                count++;
                if (count % 4 == 0) {
                    lines[insertionIndex] = lineSB.toString();
                    insertionIndex++;
                    lineSB.delete(0, lineSB.length());
                }
                if (count == 20) return lines;
            }
        }
    }

//    public static String getDivisInfo(long number) {
//        return getDivisInfoViaTricks(number) + "\n\n" + getDivisInfoViaPF(number);
//    }

    public static String[] getDivisInfoViaPF(long number) {
        TreeMap<Long, Integer> PFMap = getPrimeFactorizationMap(number);
        if (PFMap.size() == 1 && PFMap.containsValue(1)) {
            return new String[] {number + " doesn't have any factors, which makes it prime."};
        }
        int numFactors = getNumFactorsViaPF(PFMap);
        String[] lines = new String[numFactors + 3];
        lines[0] = "The prime factorization of " + number + " is " + getPrimeFactorizationString(PFMap);
        lines[1] = "Excluding 1 and " + number + ", there are " + numFactors + " factors";
        lines[2] = "The factors are:";

        int insertionIndex = 3;
        long lastPossibleFactor = number / 2;
        for (long i = 2; i <= lastPossibleFactor; i++) {
            if (number % i == 0) {
                if (PFMap.containsKey(i)) {
                    // If the found factor is prime, then only display the number and not it's prime factorization
                    lines[insertionIndex] = String.valueOf(i);
                } else {
                    // If the found factor is not prime, then display it's prime factorization as well
                    lines[insertionIndex] = i + ", which is " + getPrimeFactorizationString(i);
                }
                insertionIndex++;
            }
        }
        return lines;
    }

    /**
     * @return An int for the number of factors a number has excluding 1 and itself
     */
    private static int getNumFactorsViaPF(TreeMap<Long, Integer> PFMap) {
        int numFactors = 1;
        Collection<Integer> powers = PFMap.values();
        for (Integer power : powers) {
            numFactors *= (power + 1);
        }
        return numFactors - 2;
    }

    public static ArrayList<String> getDivisInfoViaTricks(long number) {
        ArrayList<String> lines = new ArrayList<String>();
        boolean isEven = (number % 2 == 0);
        boolean isDivisibleBy3;
        int sumOfDigits = sumOfDigits(number);

        StringBuilder output = new StringBuilder();
        if (!isEven) {
            lines.add(number + " is not even so it cannot be divisible by any even numbers");
//            output.append(number).append(" is not even so it cannot be divisible by any even numbers\n");
        }
        lines.add("The sum of the digits is " + sumOfDigits);
//        output.append("The sum of the digits is ").append(sumOfDigits).append("\n");
        if (sumOfDigits % 3 == 0) {
            isDivisibleBy3 = true;
            lines.add("This is divisible by 3 so " + number + " is divisible by 3");
//            output.append("This is divisible by 3 so ").append(number).append(" is divisible by 3\n");
            if (sumOfDigits % 9 == 0) {
                lines.add("This is divisible by 9 so " + number + " is divisible by 9");
//                output.append("This is divisible by 9 so ").append(number).append(" is divisible by 9\n");
            } else {
                lines.add("This is not divisible by 9 so " + number + " is not divisible by 9");
//                output.append("This is not divisible by 9 so ").append(number).append(" is not divisible by 9\n");
            }
        } else {
            isDivisibleBy3 = false;
            lines.add("This is not divisible by 3 so " + number + " is not divisible by 3");
            lines.add("Since " + number + " is not divisible by 3, it cannot be divisible by 6, 9, nor 12 either");
//            output.append("This is not divisible by 3 so ").append(number).append(" is not divisible by 3\n");
//            output.append("Since ").append(number).append(" is not divisible by 3, it cannot be")
//                    .append(" divisible by 6, 9, nor 12 either\n");
        }

//        output.append(divisibleBy7(number));
//        output.append(divisibleBy11(number));

        if (isEven) {
            if (isDivisibleBy3) {
                lines.add(number + " is divisible by both 2 and 3 so it's also divisible by 6");
//                output.append(number).append(" is divisible by both 2 and 3 so it's also divisible by 6\n");
            }
            int last2Digits = (int) number % 100;
//            output.append("The last 2 digits are ").append(last2Digits).append("\n");
            lines.add("The last 2 digits are " + last2Digits);
            if (last2Digits % 4 == 0) {
                lines.add("This is divisible by 4 so " + number + " is divisible by 4");
//                output.append("This is divisible by 4 so ").append(number).append(" is divisible by 4\n");
                int last3Digits = (int) number % 1000;
                lines.add("The last 3 digits are " + last3Digits);
//                output.append("The last 3 digits are ").append(last3Digits).append("\n");
                if (last3Digits % 8 == 0) {
                    lines.add("This is divisible by 8 so " + number + " is divisible by 8");
//                    output.append("This is divisible by 8 so ").append(number).append(" is divisible by 8.\n");
                } else {
                    lines.add("This is not divisible by 8 so " + number + " is not divisible by 8");
//                    output.append("This is not divisible by 8 so ").append(number).append(" is not divisible by 8\n");
                }
                if (isDivisibleBy3) {
                    lines.add(number + " is divisible by both 3 and 4 so it's also divisible by 12");
//                    output.append(number).append(" is divisible by both 3 and 4 so it's also divisible by 12\n");
                }
            } else {
                lines.add("This is not divisible by 4 so " + number + " is not divisible by 4.");
                lines.add("Since 4 is not a factor; 8, 12, and any other multiples of 4 also cannot be factors");
//                output.append("This is not divisible by 4 so ").append(number).append(" is not divisible by 4\n")
//                        .append("8 and 12 cannot be factors\n");
            }
        }
        return lines;
    }

    private static int sumOfDigits(long number) {
        String numberString = String.valueOf(number);
        int sum = 0;
        for (int i = 0; i < numberString.length(); i++) {
            int digit = Character.getNumericValue(numberString.charAt(i));
            sum += digit;
        }
        return sum;
    }

    private static int removeLastDigit(long number) {
        String numberString = String.valueOf(number);
        String newNumber = numberString.substring(0, numberString.length() - 1);
        return Integer.parseInt(newNumber);
    }

    private static String divisibleBy7(long number) {
        // This is independent from the other divisibility tests so the code will reside in a function
        StringBuilder answer = new StringBuilder();
        int lastDigitDoubled = (int) (number % 10) * 2;
        int numberWithoutLastDigit = removeLastDigit(number);
        answer.append("The last digit doubled is ").append(lastDigitDoubled).append(" and the number without " +
                "it's last digit is ").append(numberWithoutLastDigit).append(". ").append(numberWithoutLastDigit)
                .append(" - ").append(lastDigitDoubled).append(" = ").append(numberWithoutLastDigit - lastDigitDoubled)
                .append(", which is ");
        if (!(numberWithoutLastDigit - lastDigitDoubled % 7 == 0)) answer.append("not ");
        answer.append("divisible by 7");
        return answer.toString();
    }

    private static String divisibleBy11(long number) {
        // This is independent from the other divisibility tests so the code will reside in a function
        StringBuilder answer = new StringBuilder();
        boolean add = true;
        int d11Result = 0;
        answer.append("We start with a result that is initialized to 0\n");
        String numberString = String.valueOf(number);
        for (int i = 0; i < numberString.length(); i++) {
            answer.append("Digit ").append(i + 1).append(" of the number is ").append(numberString.charAt(i))
                    .append(". ");
            if (add) {
                answer.append("We add this to the result. ");
                d11Result += Character.getNumericValue(numberString.charAt(i));
            } else {
                answer.append("We subtract this from the result. ");
                d11Result -= Character.getNumericValue(numberString.charAt(i));
            }
            answer.append("The result is now ").append(d11Result).append("\n");
            add = !add;
        }
        answer.append("The result ends up being ").append(d11Result).append(". ");
        if (d11Result % 11 == 0) {
            answer.append("This is divisible by 11 so ").append(number).append(" is divisible by 11");
        } else {
            answer.append("This is not divisible by 11 so ").append(number).append(" is not divisible by 11");
        }
        return answer.toString();
    }

    public static String[] getGcdAndLcmInfo(long firstNumber, long secondNumber) {
        ArrayList<String> euclideanLines = euclidean(firstNumber, secondNumber);
        int totalNumberOfLines = euclideanLines.size() + 5;
        String[] lines = new String[totalNumberOfLines];
        int insertionIndex = 0;

        for (String line : euclideanLines) {
            lines[insertionIndex] = line;
            insertionIndex++;
        }

        lines[insertionIndex] = "\n";
        insertionIndex++;

        String[] PFLines = getGcdAndLcmViaPF(firstNumber, secondNumber);
        for (String line : PFLines) {
            lines[insertionIndex] = line;
            insertionIndex++;
        }

        return lines;
    }

    private static ArrayList<String> euclidean(long firstNumber, long secondNumber) {
        if (firstNumber < 0 || secondNumber < 0) {
            throw new ArithmeticException("Can't find GCD for negative numbers");
        }
        ArrayList<String> lines = new ArrayList<String>();
        StringBuilder lineSB = new StringBuilder();
        while (firstNumber != 0 && secondNumber != 0) {
            lineSB.append("The GCD of ").append(firstNumber).append(" and ").append(secondNumber)
                    .append(" is also the GCD of ");
            if (firstNumber < secondNumber) {
                lineSB.append(firstNumber).append(" and ").append(secondNumber % firstNumber)
                        .append(" (").append(secondNumber).append(" modulo ").append(firstNumber)
                        .append(")\n");
                secondNumber = secondNumber % firstNumber;
            } else {
                lineSB.append(secondNumber).append(" and ").append(firstNumber % secondNumber)
                        .append(" (").append(firstNumber).append(" modulo ").append(secondNumber)
                        .append(")\n");
                firstNumber = firstNumber % secondNumber;
            }
            lines.add(lineSB.toString());
            lineSB.delete(0, lineSB.length());
        }
        if (firstNumber == 0) lines.add(secondNumber + " is the GCD");
        else lines.add(firstNumber + " is the GCD");
        return lines;
    }

    private static String[] getGcdAndLcmViaPF(long firstNumber, long secondNumber) {
        String[] lines = new String[4];
//        int insertionIndex = 0;
        TreeMap<Long, Integer> primeFactorization1 = getPrimeFactorizationMap(firstNumber);
        TreeMap<Long, Integer> primeFactorization2 = getPrimeFactorizationMap(secondNumber);
        Set<Long> primeFactors1 = primeFactorization1.keySet();
        Set<Long> primeFactors2 = primeFactorization2.keySet();
        TreeMap<Long, Integer> gcdPrimeFactorization = new TreeMap<>();
        TreeMap<Long, Integer> lcmPrimeFactorization = new TreeMap<>();
//        StringBuilder lineSB = new StringBuilder();

        // First, iterate through the prime factors of the first number. For the factors that are common
        // to both numbers, the lower power gets added to the GCD and the higher power gets added to the
        // LCM. For the factors that are only in the first number, add this factor and it's power to the LCM.
        for (Long factor : primeFactors1) {
            if (primeFactors2.contains(factor)) {
                int firstPower = primeFactorization1.get(factor);
                int secondPower = primeFactorization2.get(factor);
                gcdPrimeFactorization.put(factor, Math.min(firstPower, secondPower));
                lcmPrimeFactorization.put(factor, Math.max(firstPower, secondPower));
            } else {
                lcmPrimeFactorization.put(factor, primeFactorization1.get(factor));
            }
        }

        // Now go through the prime factors of the second number and find the unique factors and add them
        // to the LCM.
        for (Long factor : primeFactors2) {
            if (!primeFactors1.contains(factor)) {
                lcmPrimeFactorization.put(factor, primeFactorization2.get(factor));
            }
        }

        lines[0] = "The prime factorization of " + firstNumber + " is " + getPrimeFactorizationString(firstNumber);
        lines[1] = "The prime factorization of " + secondNumber + " is " + getPrimeFactorizationString(secondNumber);
//        lineSB.append("The prime factorization of ").append(firstNumber).append(" is ")
//                .append(getPrimeFactorizationString(firstNumber));
//
//                .append("\nThe prime factorization of ")
//                .append(secondNumber).append(" is ").append(getPrimeFactorizationString(secondNumber));

        if (gcdPrimeFactorization.isEmpty()) {
            lines[2] = "There are no common prime factors so the GCD is 1";
        } else {
            lines[2] = "The prime factorization of the GCD is ";
//            lineSB.append("\nThe prime factorization of the GCD is ");
            if (gcdPrimeFactorization.size() == 1 && gcdPrimeFactorization.containsValue(1)) {
//                lineSB.append(gcdPrimeFactorization.firstKey());
                lines[2] += gcdPrimeFactorization.firstKey();
            } else {
                lines[2] += getPrimeFactorizationString(gcdPrimeFactorization) + ", which is " +
                        getPrimeFactorizationProduct(gcdPrimeFactorization);
//                lineSB.append(getPrimeFactorizationString(gcdPrimeFactorization)).append(", which is ")
//                        .append(getPrimeFactorizationProduct(gcdPrimeFactorization));
            }
        }

//        lineSB.append("\nThe prime factorization of the LCM is ").append(getPrimeFactorizationString(lcmPrimeFactorization))
//                .append(", which is ").append(getPrimeFactorizationProduct(lcmPrimeFactorization));
        lines[3] = "The prime factorization of the LCM is " + getPrimeFactorizationString(lcmPrimeFactorization) +
                ", which is " + getPrimeFactorizationProduct(lcmPrimeFactorization);
        return lines;
    }

    /**
     * @return A string with the first 10 pythagorean triples after the number passed in as an argument
     */
    public static String[] getPythagTriples(long number) {
        // Leg length must be greater than 0 so set the leg length to 1 if the input argument is <= 0.
        long legLength = Math.max(number, 1);
        final int maxLegLength = 46340;
        int count = 0;
        StringBuilder result = new StringBuilder();
        String[] lines = new String[10];
        int insertionIndex = 0;

        for (; ; legLength++) {
            long otherLegLengthMin = legLength + 1;
            long otherLegLengthMax = otherLegLengthMin + 1;

            while (((otherLegLengthMax + 1) * (otherLegLengthMax + 1)) - (otherLegLengthMax * otherLegLengthMax)
                    < (legLength * legLength) && otherLegLengthMax <= maxLegLength) {
                otherLegLengthMax++;
            }

            for (long otherLegLength = otherLegLengthMin; otherLegLength <= otherLegLengthMax; otherLegLength++) {
                double hypotenuse = Math.sqrt(legLength * legLength + otherLegLength * otherLegLength);
                if (hypotenuse == (long) hypotenuse) {
                    lines[insertionIndex] = legLength + "^2 (" + (legLength * legLength) + ") + " + otherLegLength +
                            "^2 (" + (otherLegLength * otherLegLength) + ") = " + (long) hypotenuse + "^2 (" +
                            (long) (hypotenuse * hypotenuse) + ")";
//                    result.append(legLength).append("^2 (").append(legLength * legLength).append(") + ")
//                            .append(otherLegLength).append("^2 (").append(otherLegLength * otherLegLength)
//                            .append(") = ").append((int) hypotenuse).append("^2 (")
//                            .append((int) (hypotenuse * hypotenuse)).append(")\n");
                    insertionIndex++;
                    if (insertionIndex == 10) return lines;
                }
            }
        }
    }

    public static ArrayList<String> getGoldbachPairs(long number) {
        // Number must be even
        if (number % 2 != 0) {
            throw new ArithmeticException("Can't get Goldbach pairs of an odd number.");
        }
        if (number < 4) {
            throw new ArithmeticException("Can only get Goldbach pairs of an even number greater than or equal to 4");
        }

        ArrayList<String> lines = new ArrayList<String>();
        // 4 is a special case since it is the only one that has 2 in it's solution.
        if (number == 4) {
            lines.add("2 and 2");
            return lines;
        }

        int count = 0;
        StringBuilder lineSB = new StringBuilder();

        // First, check if 3 and a prime number sum to the number
        if (isPrime(number - 3)) {
            lineSB.append("3 and ").append(number - 3).append("   ");
            count++;
        }

        long upperBound = number / 2;

        for (long potentialPrime = 5; ; potentialPrime += 4) {
            for (int i = 0; i < 2; i++) {
                if (isPrime(potentialPrime) && isPrime(number - potentialPrime)) {
                    lineSB.append(potentialPrime).append(" and ").append(number - potentialPrime).append("   ");
                    count++;
                    // Start a new line once the current line has 4 entries
                    if (count % 4 == 0) {
                        lines.add(lineSB.toString());
                        lineSB.delete(0, lineSB.length());
                    }
                }

                if (potentialPrime > upperBound) return lines;

                if (i == 0) potentialPrime += 2;
            }
        }
    }
}