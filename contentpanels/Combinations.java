// package com.numbertheoryplayground.contentpanels;

public class Combinations {
    /*static int factorial(int number) {
        if (number == 0) return 1;
        return number * factorial(number - 1);
    }

    static int number_of_combinations(int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    static int[][] combinations(int[] array, int k) {
        int number_of_elements = array.length;
        int combinations_array_length = number_of_combinations(number_of_elements, k);
        int[][] combinations_array = new int[combinations_array_length][k];
        int[] sub_array = new int[k];

        int[] iterators = new int[k];
        int[] max_iterators = new int[k];
        int i, j;

        for (i = 0; i < k; ++i) {
            iterators[i] = i;
            sub_array[i] = i + 1;
        }
        combinations_array[0] = sub_array.clone();

        max_iterators[k - 1] = number_of_elements - 1;
        for (i = k - 2; i >= 0; --i) {
            max_iterators[i] = max_iterators[i + 1] - 1;
        }
        //System.out.println(Arrays.toString(iterators));
        //System.out.println(Arrays.toString(max_iterators));

        int current_iterator, right_iterator;
        for (i = 1; i < combinations_array_length; ++i) {
            current_iterator = k - 1;
            while (iterators[current_iterator] == max_iterators[current_iterator]) {
                --current_iterator;
            }
            ++iterators[current_iterator];
            if (current_iterator != k - 1) {
                right_iterator = current_iterator + 1;
                while (right_iterator < k) {
                    iterators[right_iterator] = iterators[right_iterator - 1] + 1;
                    ++right_iterator;
                }
            }

            for (j = 0; j < k; ++j) {
                sub_array[j] = array[iterators[j]];
            }
            combinations_array[i] = sub_array.clone();
        }
        return combinations_array;
    }
    }*/

}
