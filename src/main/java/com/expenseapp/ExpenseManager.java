package com.expenseapp;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

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

    public double total() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public void saveToFile() {
        try (BufferedWriter bw = Files.newBufferedWriter(csvPath)) {
            for (Expense e : expenses) {
                bw.write(e.toCsv());
                bw.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Error saving expenses: " + ex.getMessage());
        }
    }

    private void loadFromFile() {
        if (!Files.exists(csvPath)) return;
        try (BufferedReader br = Files.newBufferedReader(csvPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                Expense e = Expense.fromCsv(line);
                if (e != null) expenses.add(e);
            }
        } catch (IOException ex) {
            System.err.println("Error loading expenses: " + ex.getMessage());
        }
    }
}
