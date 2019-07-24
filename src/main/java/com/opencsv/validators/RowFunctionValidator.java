package com.opencsv.validators;

import com.opencsv.exceptions.CsvValidationException;

import java.util.function.Function;

/**
 * This validator is best used to validate a specific property of the row - either about a specific
 * element or information about the array itself.
 * <p>An empty or null first row is considered invalid.</p>
 * <p>As with all row validators the assumption is you have control of the data and have skipped any initial
 * empty lines AND that your validator checks the size or handles the IndexOutOfBoundsException.</p>
 * <p>There are several examples coded in the RowFunctionValidatorTest but here are a couple to give you the
 * idea of the flexibility this validator offers.</p>
 *
 * <pre>{@code
 *     private static final String[] GOOD_ROW = {"8675309", "Firstname", "M", "Lastname", "Dec 06, 1951"};
 *     private static final String[] BAD_ROW = {"not a number", "not capitialized", "not an initial", "Not Single word", "12/06/51"};
 *     private static final String[] LONG_ROW = {"8675309", "Firstname", "M", "Lastname", "Dec 06, 1951", "More data"};
 *     private static final String[] SHORT_ROW = {"8675309", "Firstname", "Lastname", "Dec 06, 1951"};
 *
 *     private static final Function<String[], Boolean> THIRD_ELEMENT_IS_MIDDLE_INITIAL = (x) -> {
 *         return x.length > 2 && x[2].matches("^[A-Z]$");
 *     };
 *
 *     private static final Function<String[], Boolean> ROW_MUST_HAVE_FIVE_ELEMENTS = (x) -> {
 *         return (x.length == 5);
 *     };
 * }
 * {@code    @Test}
 * {@code    @DisplayName("Simple test to show checking an middle initial")}
 * {@code    public void thirdElementIsMiddleInitial() {
 *         validator = new RowFunctionValidator(THIRD_ELEMENT_IS_MIDDLE_INITIAL, "The third element must be the middle initial.");
 *
 *         assertTrue(validator.isValid(GOOD_ROW));
 *         assertFalse(validator.isValid(BAD_ROW));
 *     }
 * }
 * {@code    @Test}
 * {@code    @DisplayName("The row must have a specific number of elements in order to be valid.")}
 * {@code    public void numberOfElementsInARow() {
 *         validator = new RowFunctionValidator(ROW_MUST_HAVE_FIVE_ELEMENTS, "A Row can have only five elements.");
 *
 *         assertTrue(validator.isValid(GOOD_ROW));
 *         assertFalse(validator.isValid(LONG_ROW));
 *         assertFalse(validator.isValid(SHORT_ROW));
 *     }
 * }</pre>
 *
 * @author Scott Conway
 * @since 5.0
 */

public class RowFunctionValidator implements RowValidator {
    private Function<String[], Boolean> testFunction;
    private String failureMessage;

    /**
     * Default Constructor.
     *
     * @param testFunction   - function to run against the Array of Strings.
     * @param failureMessage - message to be included in the CsvValidationException error message.
     */
    public RowFunctionValidator(Function<String[], Boolean> testFunction, String failureMessage) {
        this.testFunction = testFunction;
        this.failureMessage = failureMessage;
    }

    @Override
    public boolean isValid(String[] row) {
        if (row == null || row.length == 0) {
            return false;
        }
        return testFunction.apply(row);
    }

    @Override
    public void validate(String[] row) throws CsvValidationException {
        if (!isValid(row)) {
            throw new CsvValidationException(failureMessage);
        }
    }
}
