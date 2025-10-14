package com.expenseapp;

public class Expense {
    private String date;
    private String category;
    private double amount;
    private String note;

    public Expense() {}

    public Expense(String date, String category, double amount, String note) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.note = note;
    }

    public String getDate() { return date; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getNote() { return note; }

    public void setDate(String date) { this.date = date; }
    public void setCategory(String category) { this.category = category; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        return String.format("%s | %-12s | ₹%.2f | %s", date, category, amount, note);
    }

    // CSV row representation
    public String toCsv() {
        // escape commas simply by replacing with semicolon if present
        String safeNote = note == null ? "" : note.replace(",", ";");
        String safeCategory = category == null ? "" : category.replace(",", ";");
        return String.join(",", date, safeCategory, String.valueOf(amount), safeNote);
    }

    public static Expense fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 4) return null;
        String date = parts[0];
        String category = parts[1];
        double amount = 0;
        try { amount = Double.parseDouble(parts[2]); } catch (NumberFormatException e) {}
        String note = parts[3];
        return new Expense(date, category, amount, note);
    }
}
