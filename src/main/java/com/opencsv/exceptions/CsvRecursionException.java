package com.opencsv.exceptions;

/**
 * This exception is thrown on initiation of field mapping if
 * {@link com.opencsv.bean.CsvRecurse} has been improperly used.
 *
 * @author Andrew Rucker Jones
 * @since 5.0
 */
public class CsvRecursionException extends CsvRuntimeException {
    private static final long serialVersionUID = 1L;

    private final Class<?> offendingType;

    /**
     * Constructor for an error message and the type that caused a recursion
     * problem.
     *
     * @param message A human-readable error message
     * @param offendingType The type that is misconfigured and caused the error
     */
    public CsvRecursionException(String message, Class<?> offendingType) {
        super(message);
        this.offendingType = offendingType;
    }

    /**
     * @return The type that is misconfigured and caused the error
     */
    public Class<?> getOffendingType() {
        return offendingType;
    }
}
