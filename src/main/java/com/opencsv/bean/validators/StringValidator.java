package com.opencsv.bean.validators;

import com.opencsv.bean.BeanField;
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
     * @param field Name of the field in the bean.  This will be used in
     *                  the {@link CsvValidationException} if the value is not
     *                  valid.
     * @throws CsvValidationException If the input is invalid. Should contain a
     * message describing the error.
     */
    void validate(String value, BeanField field) throws CsvValidationException;

    /**
     * This allows the validator extending {@link StringValidator} to be used
     * by multiple fields by allowing you to pass in data for the validator to
     * be used.
     * <p>Those data might be forbidden characters or regular expressions, to
     * name two possibilities.</p>
     * <p>If the validator needs multiple parameters, then you will need to
     * combine them into a single string using some sort of delimiter, say a
     * comma, and parse them out using some library that allows you to parse
     * such strings 😁.</p>
     * <p>If the validator does not need a value then just create an empty
     * method like the MustStartWithACapitalLetter validator used by the
     * BeanFieldValidatorTest.</p>
     *
     * @param value Information used by the validator to validate the string
     */
    void setParameterString(String value);
}
