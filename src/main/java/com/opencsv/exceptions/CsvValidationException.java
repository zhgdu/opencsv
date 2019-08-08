package com.opencsv.exceptions;

/**
 * Exception thrown by a LineValidator or LineValidatorAggregator when a single line is invalid.
 *
 * @author Scott Conway
 * @since 5.0
 */
public class CsvValidationException extends CsvException {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public CsvValidationException() {
        super();
    }

    /**
     * Constructor that allows for a human readable message.
     *
     * @param message - error text.
     */
    public CsvValidationException(String message) {
        super(message);
    }
}
