package com.nypost.crawler;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;

public class CommonManager {
    static TreeMap<String, Integer> adjuList = new TreeMap<>();


    {

    }
    public static void main(String args[]) {

        Scanner scan = new Scanner(System.in);
        int num = scan.nextInt();

        scan.nextLine();
        String empA = scan.nextLine();
        String empB = scan.nextLine();


        for (int i = 0; i ++ < num;) {
            String managerEmp = scan.nextLine();

            buildAdjuMap(managerEmp, 1);

        }int[] A = new int[]{2,3,4,};



      //  adjuList.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, e1, e2 ->));
        System.out.println(adjuList);
        System.out.println(adjuList.keySet().toArray()[0]);

        LinkedList<String> empAList = new LinkedList<>();
        LinkedList<String> empBList = new LinkedList<>();

        buildEmpParentList(empA, empAList);
        buildEmpParentList(empB, empBList);

        empAList.retainAll(empBList);

        System.out.println(adjuList);
    }

    private static void buildEmpParentList(String empA, LinkedList<String> empAList) {

        if (empA == null) return;
        Integer manager = adjuList.get(empA);
    }

    private static void buildAdjuMap(String emp, Integer value) {
        if (adjuList.containsKey(emp)) {
            adjuList.put(emp, adjuList.get(emp) + 1);
        } else {
            adjuList.put(emp, 1);
        }

    }
}

