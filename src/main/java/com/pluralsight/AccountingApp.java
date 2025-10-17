package com.pluralsight;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AccountingApp {
    static Scanner scanner = new Scanner(System.in);
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        while (true) {
            homePage();
            String option = scanner.nextLine().toUpperCase();

            switch (option) {
                case "D":
                    addTransaction(true);
                    break;
                case "P":
                    addTransaction(false);
                    break;
                case "L":
                    ledgerScreen();
                    break;
                case "X":
                    System.out.println("!!!Come Again!!!");
                    return;
                default:
                    System.out.println("\n===Read The Options and Select Again Please===\n");
            }
        }
    }

    // Home Screen Of The App
    public static void homePage() {
        System.out.println("=====Welcome To The Bank=====");
        System.out.println(" What Would You Like To Do Today ");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.print("Please Select From The Options (D,P,L,X): ");
    }
    //save Transaction
    private static void saveTransaction(String entry) {
        try(BufferedWriter buffWriter = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true))) {
            buffWriter.write(entry);
            buffWriter.newLine();
        }catch (IOException e) {
            System.out.println("Error in saving transaction");
        }
    }
    //adding deposit and payment
    private static void addTransaction(boolean deposit) {
        System.out.print("Description: ");
        String Description = scanner.nextLine();
        System.out.print("Vendor: ");
        String Vendor = scanner.nextLine();
        System.out.print("Amount: ");
        double Amount = Double.parseDouble(scanner.nextLine());

        if (!deposit) {
            Amount = -Math.abs(Amount);
        }

        LocalDateTime date = LocalDateTime.now();
        LocalDateTime time = LocalDateTime.now();

        String entry = String.format("%s|%s|%s|%s|%.2f", date.format(dateFormat), time.format(timeFormat), Description,Vendor, Amount);

        saveTransaction(entry);
        System.out.println("Transaction added successfully");
    }

    //Ledger Home screen
    private static void ledgerScreen(){
        while(true){
            List<String[]> transactions = readTransactions();
            Collections.reverse(transactions);

            System.out.println("\n===Ledger===");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposit Only");
            System.out.println("P) Payment Only");
            System.out.println("R) Report");
            System.out.println("H) Home");
            System.out.print("Choose an option(A,D,P,R,H): ");
            String option = scanner.nextLine().toUpperCase();

            switch (option) {
                case "A":showTransactions(transactions, "All Transactions");
                    break;
                case "D":
                    showTransactions(getAmount(transactions, true), "All Deposit");
                    break;
                case "P":
                    showTransactions(getAmount(transactions, false), "All Payment");
                    break;
                case "R":
                    reportScreen(transactions);
                    break;
                case "H":
                    return;
                default:
                    System.out.println("\n===Read The Options and Select Again Please===\n");

            }
        }
    }
    // Report Screen
    private static void reportScreen(List<String[]> transactions) {
        while(true){
            System.out.println("===Reports===");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Choose an option(1,2,3,4,5,0): ");
            int option = scanner.nextInt();

            List<String[]> time;

            switch (option) {
                case 1:
                    LocalDate date = LocalDate.now();
                    time = byDate(transactions,date.withDayOfMonth(1),date);
                    showTransactions(time,"Month To Date");
                    break;
                case 2:
                    break;
                case 3:
                    LocalDate date2 = LocalDate.now();
                    time = byDate(transactions,date2.withDayOfMonth(1),date2);
                    showTransactions(time,"Year To Date");
                    break;
                case 4:
                    break;
                case 5:
                    System.out.print("Enter Vendor's Name: ");
                    String Vendor = scanner.nextLine();
                    time = transactions.stream().filter(t -> t[3].contains(Vendor)).collect(Collectors.toList());
                    showTransactions(time,"Vendor: " + Vendor);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\n===Read The Options and Select Again Please===\n");
            }
        }
    }
    //reading transactions
    static List<String[]> readTransactions (){
        List<String[]> list = new ArrayList<>();
        try{
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/transactions.csv"));
            for(String line : lines){
                String[] parts= line.split("\\|");
                if(parts.length==5)list.add(parts);
            }
        }catch (IOException e){
            System.out.println("No transaction");
        }
        return list;
    }
    // showing transactions
    public static void showTransactions(List<String[]> list, String title){
        System.out.println(title);
        if(list.isEmpty()){
            System.out.println("No transactions");
            return;
        }
        for(String[] t : list){
            System.out.printf("%s|%s|%s|%s|%.2f\n",t[0],t[1],t[2],t[3],Double.parseDouble(t[4]));
        }
    }
    // Show by Deposit or Payment
    static List<String[]> getAmount(List<String[]> list, boolean deposit){
        return list.stream()
                .filter(t ->deposit ? Double.parseDouble(t[4]) > 0 : Double.parseDouble(t[4]) < 0)
                .collect(Collectors.toList());
    }
    // by date
    static List<String[]> byDate(List<String[]> list, LocalDate from, LocalDate to){
        return list.stream()
                .filter(t->{
                    LocalDate date = LocalDate.parse(t[0]);
                    return (date.isEqual(from) || date.isAfter(from)) && (date.isEqual(to) || date.isBefore(to));
                })
                .collect(Collectors.toList());
    }
}





