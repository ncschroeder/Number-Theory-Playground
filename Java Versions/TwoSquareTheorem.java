import java.awt.Component;
import java.util.List;

/**
 * Utility class related to the two square theorem and the section for it.
 */
public class TwoSquareTheorem {
    private static final String theoremInfo =
        "The Two Square Theorem says that every prime number that is 1 above a multiple " +
        "of 4 can be expressed as the sum of 2 squares.";

    public static final int minInputInt = 0;
    public static final int maxInputInt = oneBillion;

    public static class Info {
        private int primeNumber;
        private int int1;
        private final int int2;

        public Info(int anInt) {
            assertIsInRange(anInt, minInputInt, maxInputInt);

            primeNumber = anInt;
            while (primeNumber % 4 != 1) {
                primeNumber++;
            }
            while (!Primes.isPrime(primeNumber)) {
                primeNumber += 4;
            }

            for (int1 = 1; int1 < primeNumber; int1++) {
                int int1Square = int1 * int1;
                int number2Square = primeNumber - int1Square;
                double number2Double = Math.sqrt(number2Square);
                int number2Int = (int) number2Double;
                if (number2Int == number2Double) {
                    this.int2 = number2Int;
                    return;
                }
            }
            throw new IllegalStateException("Numbers not found for two square theorem algorithm");
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

    /**
     * @return The result of creating an Info instance using anInt and then using its attributes to
     * form a sentence.
     */
    private static String getInfoString(int anInt) {
        Info info = new Info(anInt);
        return String.format(
            "The first integer >= %s that is prime and is 1 above a multiple of 4 is %s, which is equal to %s + %s",
            getLongStringWithCommas(anInt),
            getLongStringWithCommas(info.primeNumber),
            getLongAndSquareString(info.int1),
            getLongAndSquareString(info.int2)
        );
    }

    public static class Section extends SingleInputSection {
        public Section() {
            super(
                "Two Square Theorem",
                List.of(theoremInfo),
                minInputInt,
                maxInputInt,
                "get the first prime number that is >= that integer and is 1 above a multiple " +
                    "of 4, as well as the squares that sum to that prime number",
                "the two square theorem"
            );
        }

        @Override
        public String getCliAnswer(int inputInt) {
            return NTPCLI.insertNewLines(getInfoString(inputInt));
        }

        @Override
        public List<Component> getGuiComponents(int inputInt) {
            String infoString = getInfoString(inputInt);
            NTPTextArea ta = new NTPTextArea(AnswerPanel.contentFont);
            ta.setText(infoString);
            return List.of(ta);
        }
    }
}
