package com.pluralsight;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Reports {

    public static void showReports(List<Transaction> transactions, Scanner scanner) {
        System.out.println("\n--- Reports ---");
        System.out.println("1) Month To Date");
        System.out.println("2) Previous Month");
        System.out.println("3) Year To Date");
        System.out.println("4) Previous Year");
        System.out.println("5) Search by Vendor");
        System.out.println("0) Back");
        System.out.print("Enter your choice: ");

        String choice = scanner.nextLine();

        LocalDate now = LocalDate.now();

        switch (choice) {
            case "1" -> filterByDate(transactions, now.withDayOfMonth(1), now);
            case "2" -> filterByDate(transactions,
                    now.minusMonths(1).withDayOfMonth(1),
                    now.minusMonths(1).withDayOfMonth(now.minusMonths(1).lengthOfMonth()));
            case "3" -> filterByDate(transactions, now.withDayOfYear(1), now);
            case "4" -> filterByDate(transactions,
                    now.minusYears(1).withDayOfYear(1),
                    now.minusYears(1).withDayOfYear(365));
            case "5" -> {
                System.out.print("Enter vendor: ");
                String vendor = scanner.nextLine();
                transactions.stream()
                        .filter(t -> t.getVendor().equalsIgnoreCase(vendor))
                        .forEach(System.out::println);
            }
        }
    }

    private static void filterByDate(List<Transaction> list, LocalDate start, LocalDate end) {
        list.stream()
                .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                .forEach(System.out::println);
    }
}

