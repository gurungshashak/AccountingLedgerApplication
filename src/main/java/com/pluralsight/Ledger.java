package com.pluralsight;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Ledger {

    public static void showLedger(List<Transaction> transactions, Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Ledger ---");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A" -> display(transactions);
                case "D" -> display(filterAmount(transactions, true));
                case "P" -> display(filterAmount(transactions, false));
                case "R" -> Reports.showReports(transactions, scanner);
                case "H" -> running = false;
            }
        }
    }

    private static List<Transaction> filterAmount(List<Transaction> list, boolean deposits) {
        return list.stream()
                .filter(t -> deposits ? t.getAmount() > 0 : t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toList());
    }

    private static void display(List<Transaction> list) {
        list.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .forEach(System.out::println);
    }
}
