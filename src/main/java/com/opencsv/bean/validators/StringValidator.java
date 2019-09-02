package com.opencsv.bean.validators;

import com.opencsv.exceptions.CsvValidationException;

/**
 * This is the interface for validators for a single
 * {@link java.lang.String} value.
 * <p>Currently this is used by the {@link PreAssignmentValidator} to check the
 * value of a string before time is taken to convert it.</p>
 * <p>For post-conversion validation there are already a plethora of third
 * party libraries that can be incorporated into the bean, <em>or</em> you can
 * just modify the setter to validate inputs.</p>
 *
 * @author Scott Conway
 * @since 5.0
 */
public interface StringValidator {
    /**
     * Performs the validation check on the string and returns the result.
     *
     * @param value String to be validated
     * @return {@code true} if the value is valid, {@code false} otherwise
     */
    boolean isValid(String value);

    /**
     * Performs the validation check on the string and throws an exception if
     * invalid.
     *
     * @param value String to be validated
     * @throws CsvValidationException If the input is invalid. Should contain a
     * message describing the error.
     */
    void validate(String value) throws CsvValidationException;
}
