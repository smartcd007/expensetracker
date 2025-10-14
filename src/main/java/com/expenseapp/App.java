package com.expenseapp;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String dataFile = "expenses.csv"; // saved in project root
        ExpenseManager manager = new ExpenseManager(dataFile);
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Expense Tracker (simple CLI)");
        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total Spent");
            System.out.println("4. Save & Exit");
            System.out.print("Choose an option: ");
            String input = sc.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.print("Date (YYYY-MM-DD): ");
                    String date = sc.nextLine().trim();

                    System.out.print("Category: ");
                    String category = sc.nextLine().trim();

                    System.out.print("Amount: ");
                    double amount = 0;
                    try {
                        amount = Double.parseDouble(sc.nextLine().trim());
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
                    List<Expense> list = manager.all();
                    if (list.isEmpty()) {
                        System.out.println("No expenses recorded.");
                    } else {
                        System.out.println("\nDate | Category      | Amount | Note");
                        System.out.println("-----------------------------------------------");
                        for (Expense ex : list) {
                            System.out.println(ex);
                        }
                    }
                    break;

                case "3":
                    System.out.printf("Total Spent: ₹%.2f%n", manager.total());
                    break;

                case "4":
                    manager.saveToFile();
                    System.out.println("Saved to " + dataFile + ". Exiting. Goodbye!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Enter 1-4.");
            }
        }
    }
}
