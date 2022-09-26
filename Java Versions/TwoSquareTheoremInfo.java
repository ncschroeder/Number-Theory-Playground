public class TwoSquareTheoremInfo {
    private int primeNumber;
    private int int1;
    private final int int2;
    private final int square1;
    private final int square2;

    /**
     *
     * @param anInt
     * @return The result of creating a TwoSquareTheoremInfo instance using anInt and then
     *
     */
    public static String getInfoString(int anInt, String intString) {
        TwoSquareTheoremInfo info = new TwoSquareTheoremInfo(anInt);
        return
            String.format(
                "The first integer greater than or equal to %s that is prime and is 1 above a multiple " +
                    "of 4 is %s, which is equal to %s + %s",
                intString,
                getLongStringWithCommas(info.primeNumber),
                getLongAndSquareString(info.int1),
                getLongAndSquareString(info.int2)
            );
    }

    /**
     *
     * @param anInt
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public TwoSquareTheoremInfo(int anInt) {
        if (Section.TWO_SQUARE_THEOREM.isInvalidInput(anInt)) {
            throw new IllegalArgumentException();
        }

        // Make primeNumber equal to the first prime number greater than or equal to
        // the argument number that is 1 above a multiple of 4.
        primeNumber = anInt;
        while (primeNumber % 4 != 1) {
            primeNumber++;
        }
        while (!isPrime(primeNumber)) {
            primeNumber += 4;
        }

        for (int1 = 1; int1 < primeNumber; int1++) {
            int int1Square = int1 * int1;
            int number2Square = primeNumber - int1Square;
            double number2Double = Math.sqrt(number2Square);
            int number2Int = (int) number2Double;
            if (number2Int == number2Double) {
                this.int2 = number2Int;
                this.square1 = int1Square;
                this.square2 = number2Square;
                return;
            }
        }
        throw new IllegalStateException();
    }

    public int getPrimeNumber() {
        return primeNumber;
    }

    public int getInt1() {
        return int1;
    }

    public int getInt2() {
        return int2;
    }

    public int getSquare1() {
        return square1;
    }

    public int getSquare2() {
        return square2;
    }
}
