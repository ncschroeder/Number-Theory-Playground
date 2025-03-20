package com.numbertheoryplayground.calculationsimpl;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Calculations {
    static boolean isDivisible(int a, int b) {
        return a % b == 0;
    }
    
    static boolean isEven(int i) {
        return isDivisible(i, 2);
    }
    
    static boolean isOdd(int i) {
        return !isEven(i);
    }
    
    static boolean isPrime(int i) {
        return true;
    }
    
    public static int[] getPrimes(int input) {
        return new int[] {};
    }
    
    public static int[] getTwinPrimePairStarts(int input) {
        return new int[] {};
    }
    
    public static List<int[]> getGoldbachPrimePairs(int input) {
        return List.of();
    }
    
    public record PythagoreanTriple(int a, int b, int c) {
        @JsonProperty("isPrimitive")
        public boolean isPrimitive() {
            return true;
        }
    }
    
    public static List<PythagoreanTriple> getPythagoreanTriples(int input) {
        return List.of();
    }
}
