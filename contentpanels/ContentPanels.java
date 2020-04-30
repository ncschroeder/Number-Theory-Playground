package contentpanels;

import java.io.FileNotFoundException;

public class ContentPanels {
    public HomePanel homePanel;
    public PrimeFactorizationPanel primeFactorizationPanel;
    public GcdLcmPanel gcdLcmPanel;
    public DivisibilityPanel divisPanel;
    public GoldbachPanel goldbachPanel;
    public TwinPrimesPanel twinPrimesPanel;

    public ContentPanels() throws FileNotFoundException {
        PrimeNumbers.fillPrimesArray();
        homePanel = new HomePanel();
        primeFactorizationPanel = new PrimeFactorizationPanel();
        gcdLcmPanel = new GcdLcmPanel();
        divisPanel = new DivisibilityPanel();
        goldbachPanel = new GoldbachPanel();
        twinPrimesPanel = new TwinPrimesPanel();
    }
}

    /*static class PrimesArray {
        int[] array;

        public PrimesArray() throws FileNotFoundException {
            array = new int[100];
            File primeNumbersFile = new File("src\\com\\numbertheoryplayground\\textfiles\\primeNumbers.txt");
            Scanner fileScanner = new Scanner(primeNumbersFile);
            for (int i = 0; i < 100; i++)
                array[i] = fileScanner.nextInt();
        }

        public int getElement(int index) {
            return array[index];
        }

        public boolean contains(int number) {
            // Binary search
            int startingIndex = 0;
            int endingIndex = array.length - 1;
            int middleIndex;

            do {
                middleIndex = (startingIndex + endingIndex) / 2;
                if (number > array[middleIndex])
                    startingIndex = middleIndex + 1;
                else
                    endingIndex = middleIndex;
            } while (startingIndex != endingIndex);

            return number == array[startingIndex];
        }
    }*/