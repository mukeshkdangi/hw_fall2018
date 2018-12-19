package com.mukesh.dynamicprogramming;

import java.util.Scanner;

public class MInEditDistance {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String s1 = scan.nextLine();
        String s2 = scan.nextLine();

        int len1 = s1.length();
        int len2 = s2.length();
        int[][] mat = new int[len2+1][len1+1];



        for (int col = 0; col < len1; col++) {
            mat[0][col] = col;
        }
        System.out.println();



        for (int row = 0; row < len2; row++) {
            mat[row][0] = row;
        }


        for (int row = 1; row < len2; row++) {
            for (int col = 1; col < len1; col++) {

                if (s1.charAt(col) == s2.charAt(row)) {
                    mat[row][col] = mat[row - 1][col - 1];
                } else {
                    mat[row][col] = Math.min(Math.min(mat[row - 1][col - 1], mat[row - 1][col]), mat[row][col - 1])+1;
                }
            }

        }

        for (int row = 0; row < len2; row++) {
            for (int col = 0; col < len1; col++) {
                System.out.print(mat[row][col] +" ");
            }
            System.out.println();
        }

        System.out.print( "Min Edit Distance is : " + mat[len2-1][len1+31] +" ");



    }
}

