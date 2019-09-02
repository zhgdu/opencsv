package com.opencsv.bean.validators;

import com.opencsv.exceptions.CsvValidationException;

/**
 *
 */
public class MustMatchRegexExpression implements StringValidator {
    private String regex = "";

    /**
     * Default constructor.
     */
    public MustMatchRegexExpression() {
        this.regex = "";
    }

    @Override
    public boolean isValid(String value) {
        if (regex.isEmpty()) {
            return true;
        }
        return value.matches(regex);
    }

    @Override
    public void validate(String value, String fieldName) throws CsvValidationException {
        if (!isValid(value)) {
            throw new CsvValidationException(String.format("Field %s value \"%s\" did not match expected format of %s", fieldName, value, regex));
        }
    }

    @Override
    public void setParameterString(String value) {
        if (value != null && !value.isEmpty()) {
            regex = value;
        }
    }
}
