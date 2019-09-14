package com.opencsv.validators;

import com.opencsv.exceptions.CsvValidationException;

/**
 * This is the interface for validators for a String read by the Reader in the CSVReader before it is processed.
 * This should only be used if you have a very good understanding and full control of the data being processed.
 * <p>
 * Since this is working on an individual line it may not be a full record if an element has a new line character
 * in it.
 *
 * @author Scott Conway
 * @since 5.0
 */
public interface LineValidator {
    /**
     * Performs the validation check on the string and returns the result.
     *
     * While not called directly in opencsv it is in the interface to provide an easy way to
     * test if the validator is function properly.
     *
     * @param line - string to be validated.
     * @return true if the line is valid, false otherwise.
     */
    boolean isValid(String line);

    /**
     * Performs the validation check on the string and throws an exception if invalid.
     *
     * @param line - string to be validated.
     * @throws CsvValidationException - thrown if invalid.  Should contain a message describing the error.
     */
    void validate(String line) throws CsvValidationException;
}
