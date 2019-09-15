package com.opencsv.validators;

import com.opencsv.exceptions.CsvValidationException;

/**
 * This is the interface for validators for a {@link String} read by the
 * {@link java.io.Reader} in the {@link com.opencsv.CSVReader} before it is
 * processed.
 * <p>This should only be used if you have a very good understanding and full
 * control of the data being processed.</p>
 * <p>Since this is working on an individual line it may not be a full record
 * if an element has a newline character in it.</p>
 *
 * @author Scott Conway
 * @since 5.0
 */
public interface LineValidator {
    /**
     * Performs the validation check on the string and returns the result.
     *
     * While not called directly in opencsv it is in the interface to provide
     * an easy way to test if the validator is function properly.
     *
     * @param line {@link String} to be validated
     * @return {@code true} if the line is valid, {@code false} otherwise
     */
    boolean isValid(String line);

    /**
     * Performs the validation check on the string and throws an exception if
     * invalid.
     *
     * @param line {@link String} to be validated
     * @throws CsvValidationException Thrown if invalid. Should contain a
     * message describing the error.
     */
    void validate(String line) throws CsvValidationException;
}
