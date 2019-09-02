package com.opencsv.bean.validators;

import com.opencsv.exceptions.CsvValidationException;

/**
 * This is the interface for validators for a single String value.  Currently this is used by the PreAssignmentValidator
 * to check the value of a string before time is taken to convert it.
 * <p>
 * For post conversion validation there are already a plethora of third party libraries that can be encorporated into
 * the bean OR you can just modify the setter to validate inputs.
 *
 * @author Scott Conway
 * @since 5.0
 */
public interface StringValidator {
    /**
     * Performs the validation check on the string and returns the result.
     *
     * @param value - string to be validated.
     * @return true if the value is valid, false otherwise.
     */
    boolean isValid(String value);

    /**
     * Performs the validation check on the string and throws an exception if invalid.
     *
     * @param value - string to be validated.
     * @throws CsvValidationException - thrown if invalid.  Should contain a message describing the error.
     */
    void validate(String value) throws CsvValidationException;
}
