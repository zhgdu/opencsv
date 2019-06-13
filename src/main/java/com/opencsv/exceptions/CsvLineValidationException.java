package com.opencsv.exceptions;

/**
 * Exception thrown by a LineValidator or LineValidatorAggregator when a single line is invalid.
 *
 * @author Scott Conway
 * @since 5.0
 */
public class CsvLineValidationException extends CsvException {
    /**
     * Default constructor.
     */
    public CsvLineValidationException() {
        super();
    }

    /**
     * Constructor that allows for a human readable message.
     *
     * @param message - error text.
     */
    public CsvLineValidationException(String message) {
        super(message);
    }
}
