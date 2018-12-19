package com.mukesh.dynamicprogramming;

import java.util.Scanner;

/**
 * @Author : Mukesh Dangi@USC
 */
public class RodCuttingWithMaxProfit {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter rod lenght");

        int len = scan.nextInt();
        System.out.println("Please enter rod  profit array lenght");
        int posCutLen = scan.nextInt();
        int[] posCut = new int[posCutLen];
        int[] posCutProf = new int[posCutLen];

        int[][] mat = new int[posCutProf.length + 1][len + 1];

        System.out.println("Please enter allowed cuts");
        for (int row = 0; row < posCutLen; row++) {
            posCut[row] = scan.nextInt();
        }

        System.out.println("Please enter allowed cuts' profit");
        for (int row = 0; row < posCutLen; row++) {
            posCutProf[row] = scan.nextInt();
        }


        for (int row = 1; row < posCutLen; row++) {
            for (int col = 1; col <= len; col++) {
                if (col >= row) {
                    mat[row][col] = Math.max(mat[row - 1][col], posCutProf[row-1] + mat[row][col - row]);
                } else {
                    mat[row][col] = mat[row - 1][col];
                }
            }
        }

        for (int row = 0; row < posCutLen; row++) {
            for (int col = 0; col <= len; col++) {
                System.out.print(" " + mat[row][col]);
            }
            System.out.println();
        }

    }
}
