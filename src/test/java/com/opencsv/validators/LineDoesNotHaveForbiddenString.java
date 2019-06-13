package com.opencsv.validators;


import com.opencsv.exceptions.CsvLineValidationException;

public class LineDoesNotHaveForbiddenString implements LineValidator {

    private final String FORBIDDEN_STRING;
    private final String MESSAGE;

    public LineDoesNotHaveForbiddenString(String forbiddenString) {
        this.FORBIDDEN_STRING = forbiddenString;
        this.MESSAGE = "Line should not contain " + forbiddenString;
    }

    @Override
    public boolean isValid(String line) {
        if (line == null || FORBIDDEN_STRING == null) {
            return true;
        }

        return !line.contains(FORBIDDEN_STRING);
    }

    @Override
    public void validate(String line) throws CsvLineValidationException {
        if (!isValid(line)) {
            throw new CsvLineValidationException(MESSAGE);
        }
    }

    String getMessage() {
        return MESSAGE;
    }
}
