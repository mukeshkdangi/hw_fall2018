package com.mukesh.dynamicprogramming.totalpathsinmatrix;

import java.util.Scanner;

public class TotalPathsinNxNMatrix {
    public static void main(String args[]) {


        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();

        int[][] matx = new int[n][n];
        int[][] mem = new int[n][n];

        for (int row = 0; row < n; row++) {
            matx[row][0] = 1;
        }
        for (int col = 0; col < n; col++) {
            matx[0][col] = 1;

        }
        long startTime = System.nanoTime();
        calCulatePathsByNormalRec(matx, n);
        long endTime = System.nanoTime();
        System.out.println("Total Time in Normal Rec:" + (endTime - startTime));

        startTime = System.nanoTime();
        calCulatePathsByNormalRecMem(matx, n, mem);
        endTime = System.nanoTime();
        System.out.println("Total Time in Memoization :" + (endTime - startTime));

        scan.close();
    }

    private static void calCulatePathsByNormalRec(int[][] matx, int n) {
        for (int row = 1; row < n; row++) {
            for (int col = 1; col < n; col++) {
                matx[row][col] = matx[row - 1][col] + matx[row][col - 1];
            }
        }
        System.out.println("Normal Rec: Total Number of ways to reach are : " + matx[n - 1][n - 1]);

    }

    private static void calCulatePathsByNormalRecMem(int[][] matx, int n, int[][] mem) {
        for (int row = 1; row < n; row++) {
            for (int col = 1; col < n; col++) {
                if (mem[row][col] == 0) {
                    matx[row][col] = matx[row - 1][col] + matx[row][col - 1];
                    mem[row][col] = matx[row][col];
                }
            }
        }
        System.out.println("Memoization : Total Number of ways to reach end are : " + matx[n - 1][n - 1]);

    }


    /**
     * Result : Input : 5 
     * Normal Rec: Total Number of ways to reach are : 70 
     * Total Time in Normal Rec:481231 
     *
     * Memoization : Total Number of ways to reach end are : 70 
     * Total Time in Memoization :64706
     *
     */

}
