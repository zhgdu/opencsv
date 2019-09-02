package com.opencsv.bean.validators;

import com.opencsv.exceptions.CsvValidationException;

public class MustStartWithACapitalLetter implements StringValidator {
    @Override
    public boolean isValid(String value) {
        return value != null
                && !value.isEmpty()
                && Character.isUpperCase(value.charAt(0));
    }

    @Override
    public void validate(String value, String fieldName) throws CsvValidationException {
        if (!isValid(value)) {
            throw new CsvValidationException(String.format("For field %s the value must start with a capital letter but instead was \"%s\".", fieldName, value));
        }
    }

    @Override
    public void setParameterString(String value) {
        // not needed.
    }


}
