package com.expenseapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

class ExpenseManagerTest {
    @TempDir
    Path tempDirectory;

    @Test
    void search_matchesCategoryAndNoteIgnoringCase() {
        ExpenseManager manager = managerWith(
                new Expense("2025-10-01", "Food", new BigDecimal("10.00"), "Lunch"),
                new Expense("2025-10-02", "Travel", new BigDecimal("20.00"), "Airport taxi"));

        assertEquals(1, manager.search("food").size());
        assertEquals("Airport taxi", manager.search("AIRPORT").get(0).getNote());
    }

    @Test
    void search_preservesOrderAndDoesNotChangeTotal() {
        Expense first = new Expense("2025-10-01", "Food", new BigDecimal("10.00"), "Lunch");
        Expense second = new Expense("2025-10-02", "Food", new BigDecimal("20.00"), "Dinner");
        ExpenseManager manager = managerWith(first, second);

        List<Expense> matches = manager.search("oo");

        assertEquals(List.of(first, second), matches);
        assertEquals(new BigDecimal("30.00"), manager.total());
    }

    @Test
    void search_blankOrNullReturnsAllAndHandlesNullFields() {
        Expense withNullFields = new Expense("2025-10-01", null, new BigDecimal("10.00"), null);
        Expense other = new Expense("2025-10-02", "Travel", new BigDecimal("20.00"), "Taxi");
        ExpenseManager manager = managerWith(withNullFields, other);

        assertEquals(2, manager.search(null).size());
        assertEquals(2, manager.search("   ").size());
        assertTrue(manager.search("missing").isEmpty());
        assertTrue(manager.search("travel").contains(other));
    }

    private ExpenseManager managerWith(Expense... expenses) {
        ExpenseManager manager = new ExpenseManager(tempDirectory.resolve("expenses.csv").toString());
        for (Expense expense : expenses) {
            manager.addExpense(expense);
        }
        return manager;
    }
}