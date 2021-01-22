import java.util.ArrayList;
import java.util.Collection;

/**
 * Utility class consisting of methods related to divisibility.
 */
public class Divisibility {
    public static String getSectionInfo() {
        return "The factors of a number are all the numbers that can evenly divide that number. Some special " +
                "tricks can be used to find some of the factors of a number. If the sum of the digits of a number is " +
                "divisible by 3, then that number is divisible by 3. If the sum of the digits of a number is divisible " +
                "by 9, then that number is divisible by 9. If a number is even and divisible by 3, then it is also " +
                "divisible by 6. If the last 2 digits of a number is divisible by 4, then that number is divisible by " +
                "4. If the last 3 digits of a number is divisible by 8, then that number is divisible by 8. If a " +
                "number is divisible by both 3 and 4 then it is also divisible by 12. Another way you can tell what " +
                "factors a number has and how many factors it has is by looking at it's prime factorization. To find " +
                "the number of factors, you take all the powers of the prime factors, add 1 to each and then multiply " +
                "them all together. All the \"sub-factorizations\" of this prime factorization are the prime " +
                "factorizations of all the factors. Some examples of \"sub-factorizations\" are 2 and 2 * 3 in " +
                "the prime factorization 2 * 3 * 5.";
    }

    /**
     * @return An ArrayList of Strings that contain info about what numbers the argument number is divisible by.
     * This info is acquired by using special tricks.
     */
    public static Collection<String> getDivisInfoViaTricks(int number) {
        ArrayList<String> divisInfo = new ArrayList<>();
        boolean isEven = number % 2 == 0;
        if (!isEven) {
            divisInfo.add(number + " is not even so it cannot be divisible by any even numbers");
        }
        int sumOfDigits = sumOfDigits(number);
        divisInfo.add("The sum of the digits is " + sumOfDigits);
        // A number is divisible by 3 if and only if the sum of it's digits is divisible by 3
        boolean isDivisibleBy3 = sumOfDigits % 3 == 0;
        if (isDivisibleBy3) {
            divisInfo.add(sumOfDigits + " is divisible by 3 so " + number + " is divisible by 3");
            if (sumOfDigits % 9 == 0) {
                // A number is divisible by 9 if and only if the sum of it's digits is divisible by 9
                divisInfo.add(sumOfDigits + " is divisible by 9 so " + number + " is divisible by 9");
            } else {
                divisInfo.add(sumOfDigits + " is not divisible by 9 so " + number + " is not divisible by 9");
            }
        } else {
            divisInfo.add(sumOfDigits + " is not divisible by 3 so " + number + " is not divisible by 3");
            divisInfo.add("Since " + number + " is not divisible by 3, it can't be divisible by ");
            divisInfo.add("6, 9, 12, and any other multiples of 3");
        }

        if (isEven) {
            if (isDivisibleBy3) {
                // A number is divisible by 6 if it's even and divisible by 3.
                divisInfo.add(number + " is even and divisible by 3 so it's also divisible by 6");
            }
            int last2Digits = Math.abs(number) % 100;
            divisInfo.add("The last 2 digits are " + last2Digits);
            if (last2Digits % 4 == 0) {
                // A number is divisible by 4 if the last 2 digits are divisible by 4.
                divisInfo.add(last2Digits + " is divisible by 4 so " + number + " is divisible by 4");

                int last3Digits = Math.abs(number) % 1000;
                divisInfo.add("The last 3 digits are " + last3Digits);
                if (last3Digits % 8 == 0) {
                    // A number is divisible by 8 if the last 3 digits are divisible by 8.
                    divisInfo.add(last3Digits + " is divisible by 8 so " + number + " is divisible by 8");
                } else {
                    divisInfo.add(last3Digits + " is not divisible by 8 so " + number + " is not divisible by 8");
                }

                if (isDivisibleBy3) {
                    // A number is divisible by 12 if it's divisible by both 3 and 4.
                    divisInfo.add(number + " is divisible by both 3 and 4 so it's also divisible by 12");
                }
            } else {
                divisInfo.add(last2Digits + " is not divisible by 4 so " + number + " is not divisible by 4.");
                divisInfo.add("Since " + number + " is not divisible by 4, " + number + " also can't be divisible by ");
                divisInfo.add("8, 12, and any other multiples of 4");
            }
        }
        return divisInfo;
    }

    private static int sumOfDigits(int number) {
        String numberString = String.valueOf(number);
        int sum = 0;
        for (int i = 0; i < numberString.length(); i++) {
            int digit = Character.getNumericValue(numberString.charAt(i));
            sum += digit;
        }
        return sum;
    }

    /**
     * @return An ArrayList of Strings that consists of divisibility info about the argument number. This info is
     * acquired by looking at the prime factorization of the argument number.
     * @throws IllegalArgumentException if the argument number is less than 2.
     */
    public static Collection<String> getDivisInfoViaPf(int number) {
        return new PrimeFactorization(number).getFactorsInfo();
    }
}