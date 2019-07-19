package com.opencsv.validators;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RowMustHaveSameNumberOfColumnsAsFirstRowValidatorTest {
    private static final String[] FOUR_COLUMNS = {"one", "two", "three", "four"};
    private static final String[] THREE_COLUMNS = {"one", "two", "three"};
    private static final String[] TWO_COLUMNS = {"one", "two"};
    private static final String[] NO_COLUMNS = {};
    private static final String[] NULL_ROW = null;
    private RowValidator validator;

    @BeforeEach
    public void setup() {
        validator = new RowMustHaveSameNumberOfColumnsAsFirstRowValidator();
    }

    @Test
    @DisplayName("RowValidator isValid with three rows first")
    public void isValidThreeRowsFirst() {
        assertTrue(validator.isValid(THREE_COLUMNS));
        assertTrue(validator.isValid(THREE_COLUMNS));
        assertFalse(validator.isValid(TWO_COLUMNS));
        assertFalse(validator.isValid(NO_COLUMNS));
        assertFalse(validator.isValid(NULL_ROW));
        assertFalse(validator.isValid(FOUR_COLUMNS));
        assertTrue(validator.isValid(THREE_COLUMNS));
    }

    @Test
    @DisplayName("RowValidator validate with three rows first")
    public void validateThreeRowsFirst() throws CsvValidationException {
        Assertions.assertDoesNotThrow(() -> validator.validate(THREE_COLUMNS));
        Assertions.assertDoesNotThrow(() -> validator.validate(THREE_COLUMNS));
        Assertions.assertThrows(CsvValidationException.class, () -> validator.validate(FOUR_COLUMNS));
        Assertions.assertThrows(CsvValidationException.class, () -> validator.validate(TWO_COLUMNS));
        Assertions.assertThrows(CsvValidationException.class, () -> validator.validate(NO_COLUMNS));
        Assertions.assertThrows(CsvValidationException.class, () -> validator.validate(NULL_ROW));
        Assertions.assertDoesNotThrow(() -> validator.validate(THREE_COLUMNS));
    }

    @Test
    @DisplayName("RowValidator isValid with null first row")
    public void isValidNullFirstRow() {
        assertFalse(validator.isValid(NULL_ROW));
    }

    @Test
    @DisplayName("RowValidator isValid with empty first row")
    public void isValidEmptyFirstRow() {
        assertFalse(validator.isValid(NO_COLUMNS));
    }

    @Test
    @DisplayName("RowValidator validate with null first row")
    public void validateNullFirstRow() {
        Assertions.assertThrows(CsvValidationException.class, () -> validator.validate(NULL_ROW));
    }

    @Test
    @DisplayName("RowValidator validate with empty first row")
    public void validateEmptyFirstRow() {
        Assertions.assertThrows(CsvValidationException.class, () -> validator.validate(NO_COLUMNS));
    }
}
