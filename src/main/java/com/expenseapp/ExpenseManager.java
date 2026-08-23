package com.expenseapp;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

public class ExpenseManager {
    private final List<Expense> expenses = new ArrayList<>();
    private final Path csvPath;

    public ExpenseManager(String csvFile) {
        this.csvPath = Paths.get(csvFile);
        loadFromFile();
    }

    public void addExpense(Expense e) {
        expenses.add(e);
    }

    public List<Expense> all() {
        return new ArrayList<>(expenses);
    }

    public List<Expense> search(String query) {
        if (query == null || query.isBlank()) {
            return all();
        }

        String normalizedQuery = query.trim().toLowerCase(Locale.ROOT);
        List<Expense> matches = new ArrayList<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            String note = expense.getNote();
            if ((category != null && category.toLowerCase(Locale.ROOT).contains(normalizedQuery))
                    || (note != null && note.toLowerCase(Locale.ROOT).contains(normalizedQuery))) {
                matches.add(expense);
            }
        }
        return matches;
    }

    public BigDecimal total() {
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void saveToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(csvPath);
                CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (Expense e : expenses) {
                printer.printRecord(e.getDate(), e.getCategory(), e.getAmount().toPlainString(), e.getNote());
            }
        } catch (IOException ex) {
            System.err.println("Error saving expenses: " + ex.getMessage());
        }
    }

    private void loadFromFile() {
        if (!Files.exists(csvPath))
            return;
        try (Reader reader = Files.newBufferedReader(csvPath);
                CSVParser parser = CSVFormat.DEFAULT.parse(reader)) {
            for (var record : parser) {
                Expense e = Expense.fromCsvRecord(record);
                if (e != null)
                    expenses.add(e);
            }
        } catch (IOException ex) {
            System.err.println("Error loading expenses: " + ex.getMessage());
        }
    }
}
