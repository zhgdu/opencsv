package com.opencsv.exceptions;

import java.util.LinkedList;
import java.util.List;

/**
 * An exception class for collecting multiple exceptions.
 * For internal use only.
 *
 * @author Andrew Rucker Jones
 * @since 5.3
 */
public class CsvChainedException extends CsvException {

    private final List<CsvFieldAssignmentException> exceptionChain = new LinkedList<>();

    /**
     * Constructor.
     * @param csve The first exception for the list being collected.
     *             Must not be {@code null}.
     */
    public CsvChainedException(CsvFieldAssignmentException csve) {
        exceptionChain.add(csve);
    }

    /**
     * Add an exception to the chain of collections.
     * @param csve Exception to be added to this chain.
     *             Must not be {@code null}.
     */
    public void add(CsvFieldAssignmentException csve) {
        exceptionChain.add(csve);
    }

    /**
     * @return A list of all exceptions collected
     */
    public List<CsvFieldAssignmentException> getExceptionChain() {
        return exceptionChain;
    }

    /** Sets the line for all exceptions collected. */
    // The rest of the Javadoc is inherited
    @Override
    public void setLine(String[] line) {
        super.setLine(line);
        exceptionChain.forEach(e -> e.setLine(line));
    }

    /** Sets the line number for all exceptions collected. */
    // The rest of the Javadoc is inherited
    @Override
    public void setLineNumber(long lineNumber) {
        super.setLineNumber(lineNumber);
        exceptionChain.forEach(e -> e.setLineNumber(lineNumber));
    }
}
