package com.mukesh.dynamicprogramming.fibsum;

public class FibSum {

    public static void main(String args[]) {
        System.out.println("Finb series using memoization");
        int arr = 0;
        long startTime = System.nanoTime();
        arr = calFibNumbers(10);
        long endTime = System.nanoTime();
        System.out.println(" Number value with normal rec is " + arr + "With time =" + (endTime - startTime));

        startTime = System.nanoTime();
        int[] mem = new int[56];
        arr = calFibNumbersMem(10, mem);
        endTime = System.nanoTime();
        System.out.println(" Number value using Memoization is " + arr + "With time =" + (endTime - startTime));

    }

    public static int calFibNumbers(int number) {
        if (number <= 0)
            return 0;
        else if (number == 1)
            return 1;
        else
            return calFibNumbers(number - 1) + calFibNumbers(number - 2);
    }

    public static int calFibNumbersMem(int number, int[] mem) {
        if (number <= 0)
            return 0;
        else if (number == 1)
            return 1;
        else if (mem[number] == 0) {
            mem[number] = calFibNumbersMem(number - 1, mem) + calFibNumbersMem(number - 2, mem);
        }
        return mem[number];
    }

}
