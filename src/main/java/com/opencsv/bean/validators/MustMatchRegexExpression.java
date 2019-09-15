package com.opencsv.bean.validators;

import com.opencsv.ICSVParser;
import com.opencsv.bean.BeanField;
import com.opencsv.exceptions.CsvValidationException;

import java.util.ResourceBundle;

/**
 * TODO: Javadoc
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
    public void validate(String value, BeanField field) throws CsvValidationException {
        if (!isValid(value)) {
            throw new CsvValidationException(String.format(ResourceBundle.getBundle("mustMatchRegex", field.getErrorLocale())
                    .getString("validator.regex.mismatch"), field.getField().getName(), value, regex));
        }
    }

    @Override
    public void setParameterString(String value) {
        if (value != null && !value.isEmpty()) {
            regex = value;
        }
    }
}
