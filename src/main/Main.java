package main;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Read inputs
        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("Input: " + line);
        }

        final int originalCost = -1;
        final int updatedCost = -1;
        System.out.println(originalCost);
        System.out.println(updatedCost);
    }
}