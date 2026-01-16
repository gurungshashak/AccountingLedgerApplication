package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Transaction> transactions = TransactionFileManager.loadTransactions();

        boolean running = true;

        while (running) {
            System.out.println("\n=== FINlANCIAL TRACKER ===");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "D" -> addTransaction(scanner, transactions, true);
                case "P" -> addTransaction(scanner, transactions, false);
                case "L" -> Ledger.showLedger(transactions, scanner);
                case "X" -> running = false;
            }
        }
        System.out.println("Goodbye!");
    }

    private static void addTransaction(Scanner scanner, List<Transaction> list, boolean deposit) {
        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        if (!deposit) amount *= -1;

        Transaction transaction = new Transaction(
                LocalDate.now(),
                LocalTime.now(),
                description,
                vendor,
                amount
        );

        list.add(transaction);
        TransactionFileManager.saveTransaction(transaction);
        System.out.println("Transaction saved!");
    }
}
