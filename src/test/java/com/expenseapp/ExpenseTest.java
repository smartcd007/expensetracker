package com.expenseapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTest {

    @Test
    void fromCsv_invalidDate_returnsNull() {
        Expense e = Expense.fromCsv("2025-10-99,food,10,lunch");
        assertNull(e, "Invalid date should cause fromCsv to return null");
    }

    @Test
    void fromCsv_validDate_parses() {
        Expense e = Expense.fromCsv("2025-10-09,food,10,lunch");
        assertNotNull(e);
        assertEquals("2025-10-09", e.getDate());
        assertEquals("food", e.getCategory());
        assertEquals(10.0, e.getAmount());
        assertEquals("lunch", e.getNote());
    }

    @Test
    void setDate_invalid_throws() {
        Expense e = new Expense();
        assertThrows(IllegalArgumentException.class, () -> e.setDate("2025-10-99"));
    }

    @Test
    void setDate_valid_normalizes() {
        Expense e = new Expense();
        e.setDate("2025-01-05");
        assertEquals("2025-01-05", e.getDate());
    }
}
