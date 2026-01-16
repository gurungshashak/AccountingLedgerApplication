package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionFileManager {
    private static final String FILE_NAME = "transactions.csv";

    public static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                transactions.add(new Transaction(
                        LocalDate.parse(parts[0]),
                        LocalTime.parse(parts[1]),
                        parts[2],
                        parts[3],
                        Double.parseDouble(parts[4])
                ));
            }
        } catch (IOException e) {
        }
        return transactions;
    }

    public static void saveTransaction(Transaction transaction) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(transaction.toCSV() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving transaction.");
        }
    }
}

