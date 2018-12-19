package com.mukesh.dynamicprogramming;

import java.util.Scanner;

public class EggDropping {
    public static void main(String[] args) {
/**
 * In Progress
 */
        Scanner scan = new Scanner(System.in);

        int egg = scan.nextInt();
        int flor = scan.nextInt();
        int[][] mat = new int[egg][flor];

        for (int row = 0; row < egg; row++) {
            mat[row][0] = 1;
        }
        for (int col = 0; col < flor; col++) {
            mat[0][col] = col + 1;
        }

        for (int row = 1; row < egg; row++) {
            for (int col = 1; col < flor; col++) {
                int c = 0;
                mat[row][col] = Integer.MAX_VALUE;
                for (int k = 1; k <= col; k++) {
                    c = 1 + Math.max(mat[row - 1][k - 1], mat[row][col - k]);

                    if (c < mat[row][col]) {
                        mat[row][col] = c;

                    }
                }
            }
        }

        for (int row = 0; row < egg; row++) {
            for (int col = 0; col < flor; col++) {
                System.out.print(" " + mat[row][col]);

            }
            System.out.println();
        }

    }
}
