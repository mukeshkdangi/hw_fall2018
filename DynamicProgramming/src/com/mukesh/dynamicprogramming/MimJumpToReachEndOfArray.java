package com.mukesh.dynamicprogramming;

import java.util.Scanner;

public class MimJumpToReachEndOfArray {
//TODO
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.print("Enter Array Lenght");
        int len = scan.nextInt();
        int[] arr = new int[len];
        int[] arr2 = new int[len];
        int[] arr3 = new int[len];
        for (int ind = 0; ind < len; ind++) {
            arr[ind]= scan.nextInt();
        }

        int count=0;

        for (int ind = 1; ind < len; ind++) {
            for (int indj = 0; indj < ind; indj++) {
                if(arr[indj]>ind){
                    arr2[count++] = indj;
                } else {
                    if()
                }
            }
        }

        for (int ind = 0; ind < arr2.length; ind++) {
            System.out.print(arr2[ind]);
        }


    }
}

