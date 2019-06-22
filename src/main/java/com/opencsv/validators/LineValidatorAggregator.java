package com.opencsv.validators;

import com.opencsv.exceptions.CsvValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * The aggregator purpose is to collect multiple LineValidators and run them against a single line.
 * This way complex validations can be performed.
 */
public class LineValidatorAggregator {
    private static final int CAPACITY = 128;
    private List<LineValidator> validators = new ArrayList<>();

    /**
     * Default constructor.
     */
    public LineValidatorAggregator() {
    }

    /**
     * Add an validator to the aggregator.
     *
     * @param validator - validator to be added.
     */
    public void addValidator(LineValidator validator) {
        if (validator != null) {
            validators.add(validator);
        }
    }

    /**
     * Runs all LineValidators isValid command against the line.   This is a short circuit and - as soon as one validator
     * returns false then false is return.
     *
     * @param line - string to be validated.
     * @return true if all validators isValid methods returns true, false otherwise.
     */
    public boolean isValid(String line) {
        for (LineValidator validator : validators) {
            if (!validator.isValid(line)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Runs all LineValdators validate commands and if the string is invalid then it combines all the validation error
     * messages in a single CsvValidationException.
     *
     * @param line - string to be validation.
     * @throws CsvValidationException - thrown if the string is invalid.
     */
    public void validate(String line) throws CsvValidationException {
        StringBuilder combinedExceptionMessage = new StringBuilder(CAPACITY);

        for (LineValidator validator : validators) {
            try {
                validator.validate(line);
            } catch (CsvValidationException ex) {
                combinedExceptionMessage.append(ex.getMessage()).append("\n");
            }
        }

        if (combinedExceptionMessage.length() > 0) {
            throw new CsvValidationException(combinedExceptionMessage.toString());
        }
    }
}
