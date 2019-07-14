package com.opencsv.validators;

import com.opencsv.exceptions.CsvValidationException;

/**
 * This validator is used when the number of columns is not neccessarily known but must be consistent.
 * The first row validated will always be considered valid, unless null or empty, and after that the each
 * subsequent row must have the same number of columns as the first.
 * <p>
 * Arrays that are empty or null are considered to have 0 elements.  An empty or null first row is considered
 * invalid.
 * <p>
 * As with all row validators the assumption is you have control of the data and have skipped any initial
 * empty lines.    If you have data that has empty lines or separators lines in your data then you should not
 * use this class.   Either extend it or write your own validator.
 *
 * @author Scott Conway
 * @since 5.0
 */
public class RowMustHaveSameNumberOfColumnsAsFirstRowValidator implements RowValidator {
    private static final int NO_ROWS = 0;
    private int numRows = NO_ROWS;

    /**
     * Default constructor.
     */
    public RowMustHaveSameNumberOfColumnsAsFirstRowValidator() {
    }

    @Override
    public boolean isValid(String[] row) {
        if (row == null || row.length == 0) {
            return false;
        }

        if (firstRowNotSetYet()) {
            numRows = row.length;
        }
        return row.length == numRows;
    }

    @Override
    public void validate(String[] row) throws CsvValidationException {
        if (!isValid(row)) {
            if (firstRowNotSetYet()) {
                throw new CsvValidationException("First row should not be empty or null");
            } else if (row == null || row.length == 0) {
                throw new CsvValidationException("Row should not be empty or null");
            } else {
                throw new CsvValidationException(String.format("Row was expected to have %d elements but had %d instead", numRows, row.length));
            }

        }
    }

    private boolean firstRowNotSetYet() {
        return numRows == NO_ROWS;
    }
}
