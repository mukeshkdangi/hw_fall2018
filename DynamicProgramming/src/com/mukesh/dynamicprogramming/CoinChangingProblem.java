package com.mukesh.dynamicprogramming;

import java.util.Scanner;

public class CoinChangingProblem {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter the total sum for which you need min Dem Coin Count");
        int total = scan.nextInt();

        System.out.println("Please enter the available Dem Count");
        int demLen = scan.nextInt();

        int[] oneD = new int[demLen + 1];
        int[] twoD = new int[total];
        oneD[0] = 0;
        System.out.println("Please enter the available Dems");
        for (int row = 0; row < demLen; row++) {
            oneD[row] = scan.nextInt();
        }

        for (int row = 1; row < total; row++) {
            for (int col = 0; col < demLen; col++) {
                if (row > oneD[col])
                    oneD[row] = Math.min(twoD[row], 1 + twoD[row - oneD[col]]);
            }
        }

        for (int row = 1; row < total; row++) {
            System.out.print(" " + twoD[row]);

        }


    }
}
