package com.expenseapp;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class Expense {
    private String date; // stored in ISO yyyy-MM-dd
    private String category;
    private BigDecimal amount;
    private String note;

    public Expense() {
    }

    public Expense(String date, String category, BigDecimal amount, String note) {
        this.date = date;
        this.category = category;
        setAmount(amount);
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public void setDate(String date) {
        // validate date when setting
        try {
            LocalDate parsed = LocalDate.parse(date);
            this.date = parsed.toString();
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date: " + date, ex);
        }
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return String.format("%s | %-12s | ₹%.2f | %s", date, category, amount, note);
    }

    // CSV row representation
    public String toCsv() {
        try {
            StringWriter output = new StringWriter();
            try (CSVPrinter printer = new CSVPrinter(output, CSVFormat.DEFAULT)) {
                printer.printRecord(date, category, amount.toPlainString(), note);
            }
            String serialized = output.toString();
            return serialized.endsWith("\r\n")
                    ? serialized.substring(0, serialized.length() - 2)
                    : serialized.substring(0, serialized.length() - 1);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to serialize expense", ex);
        }
    }

    public static Expense fromCsv(String csvLine) {
        if (csvLine == null) {
            return null;
        }
        try (CSVParser parser = CSVFormat.DEFAULT.parse(new StringReader(csvLine))) {
            var records = parser.getRecords();
            if (records.size() != 1 || records.get(0).size() != 4) {
                return null;
            }
            return fromCsvRecord(records.get(0));
        } catch (IOException | IllegalArgumentException ex) {
            return null;
        }
    }

    static Expense fromCsvRecord(CSVRecord record) {
        if (record.size() != 4) {
            return null;
        }
        String date = record.get(0);
        // validate date is a real ISO date (yyyy-MM-dd)
        try {
            LocalDate parsed = LocalDate.parse(date);
            date = parsed.toString();
        } catch (DateTimeParseException ex) {
            return null;
        }
        BigDecimal amount;
        try {
            amount = new BigDecimal(record.get(2));
        } catch (NumberFormatException ex) {
            return null;
        }
        return new Expense(date, record.get(1), amount, record.get(3));
    }
}
