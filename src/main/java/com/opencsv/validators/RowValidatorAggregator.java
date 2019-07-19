package com.opencsv.validators;

import com.opencsv.exceptions.CsvValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * The aggregator purpose is to collect multiple RowValidators and run them against a single array of Strings.
 * This way complex validations can be performed.
 *
 * @author Scott Conway
 * @since 5.0
 */
public class RowValidatorAggregator {
    private static final int CAPACITY = 256;
    private List<RowValidator> validators = new ArrayList<>();

    /**
     * Default constructor.
     */
    public RowValidatorAggregator() {
    }

    /**
     * Add an validator to the aggregator.
     *
     * @param validator - validator to be added.
     */
    public void addValidator(RowValidator validator) {
        if (validator != null) {
            validators.add(validator);
        }
    }

    /**
     * Runs all RowValidator isValid command against the line.   This is a short circuit and - as soon as one validator
     * returns false then false is return.
     *
     * @param row - Array of Strings to be validated.
     * @return true if all validators isValid methods returns true, false otherwise.
     */
    public boolean isValid(String[] row) {
        for (RowValidator validator : validators) {
            if (!validator.isValid(row)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Runs all RowValdators validate commands and if the string is invalid then it combines all the validation error
     * messages in a single CsvValidationException.
     *
     * @param row - Array of Strings to be validation.
     * @throws CsvValidationException - thrown if the string is invalid.
     */
    public void validate(String[] row) throws CsvValidationException {
        StringBuilder combinedExceptionMessage = new StringBuilder(CAPACITY);

        for (RowValidator validator : validators) {
            try {
                validator.validate(row);
            } catch (CsvValidationException ex) {
                combinedExceptionMessage.append(ex.getMessage()).append("\n");
            }
        }

        if (combinedExceptionMessage.length() > 0) {
            throw new CsvValidationException(combinedExceptionMessage.toString());
        }
    }
}
