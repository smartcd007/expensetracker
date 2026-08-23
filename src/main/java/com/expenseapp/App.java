package com.expenseapp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String dataFile = "expenses.csv"; // saved in project root
        ExpenseManager manager = new ExpenseManager(dataFile);
        Scanner sc = new Scanner(System.in);

        clearScreen();
        System.out.println("Welcome to Expense Tracker (simple CLI)");
        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Search Expenses");
            System.out.println("4. View Total Spent");
            System.out.println("5. Save & Exit");
            System.out.print("Choose an option: ");
            String input = sc.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.print("Date (YYYY-MM-DD): ");
                    String date = sc.nextLine().trim();

                    System.out.print("Category: ");
                    String category = sc.nextLine().trim();

                    System.out.print("Amount: ");
                    BigDecimal amount;
                    try {
                        amount = new BigDecimal(sc.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount. Try again.");
                        break;
                    }

                    System.out.print("Note: ");
                    String note = sc.nextLine().trim();

                    Expense e = new Expense(date, category, amount, note);
                    manager.addExpense(e);
                    System.out.println("✅ Expense added.");
                    break;

                case "2":
                    clearScreen();
                    displayExpenses(manager.all(), "No expenses recorded.");
                    break;

                case "3":
                    System.out.print("Search category or note: ");
                    String query = sc.nextLine();
                    clearScreen();
                    displayExpenses(manager.search(query), "No expenses found matching that query.");
                    break;

                case "4":
                    clearScreen();
                    System.out.printf("Total Spent: ₹%.2f%n", manager.total());
                    break;

                case "5":
                    manager.saveToFile();
                    System.out.println("Saved to " + dataFile + ". Exiting. Goodbye!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Enter 1-5.");
            }
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void displayExpenses(List<Expense> expenses, String emptyMessage) {
        if (expenses.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }

        System.out.println("\nDate | Category      | Amount | Note");
        System.out.println("-----------------------------------------------");
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }
}
