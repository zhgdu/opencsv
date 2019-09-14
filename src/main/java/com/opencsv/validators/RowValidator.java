package com.opencsv.validators;

import com.opencsv.exceptions.CsvValidationException;

/**
 * This is the interface for validators for an array of Strings read by the CSVReader after it is processed.
 * This should only be used if you have a very good understanding and full control of the data being processed.
 * Good examples of a RowValidator would be to ensure the number of columns in the row or, if you know what data
 * is in which column, check the format and type of data.
 *
 * @author Scott Conway
 * @since 5.0
 */
public interface RowValidator {
    /**
     * Performs the validation check on the string and returns the result.
     * While not called directly in opencsv it is in the interface to provide an easy way to
     * test if the validator is function properly.
     *
     * @param row - array of strings to be validated.
     * @return true if the row is valid, false otherwise.
     */
    boolean isValid(String[] row);

    /**
     * Performs the validation check on the Row (Array of Strings) and throws an exception if invalid.
     *
     * @param row - array of Strings to be validated.
     * @throws CsvValidationException - thrown if invalid.  Should contain a message describing the error.
     */
    void validate(String[] row) throws CsvValidationException;
}
