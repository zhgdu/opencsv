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
    public void validate(String value) throws CsvValidationException {
        if (!isValid(value)) {
            throw new CsvValidationException(String.format("Value \"%s\" must start with a capital letter.", value));
        }
    }
}
