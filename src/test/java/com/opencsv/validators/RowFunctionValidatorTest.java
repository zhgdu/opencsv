package com.opencsv.validators;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RowFunctionValidatorTest {
    private static final String[] GOOD_ROW = {"8675309", "Firstname", "M", "Lastname", "Dec 06, 1951"};
    private static final String[] BAD_ROW = {"not a number", "not capitialized", "not an initial", "Not Single word", "12/06/51"};
    private static final String[] LONG_ROW = {"8675309", "Firstname", "M", "Lastname", "Dec 06, 1951", "More data"};
    private static final String[] SHORT_ROW = {"8675309", "Firstname", "Lastname", "Dec 06, 1951"};
    private static final String[] EMPTY_ROW = {};
    private static final String FAILURE_MESSAGE = "The first element of the row must be a number!";

    private RowValidator validator;
    private static final Function<String[], Boolean> FIRST_ELEMENT_IS_A_NUMBER = (x) -> {
        return x[0].matches("^[0-9]+$");
    };
    private static final RowValidator FIRST_ELEMENT_VALIDATOR = new RowFunctionValidator(FIRST_ELEMENT_IS_A_NUMBER, FAILURE_MESSAGE);

    private static final Function<String[], Boolean> SECOND_ELEMENT_IS_SIMPLE_NAME = (x) -> {
        return x[1].matches("^[A-Z]+([a-z]*)*$");
    };

    private static final Function<String[], Boolean> THIRD_ELEMENT_IS_MIDDLE_INITIAL = (x) -> {
        return x[2].matches("^[A-Z]$");
    };

    private static final Function<String[], Boolean> FOURTH_ELEMENT_IS_SIMPLE_NAME = (x) -> {
        return x[3].matches("^[A-Z]+([a-z]*)*$");
    };

    private static final Function<String[], Boolean> FIFTH_ELEMENT_IS_SPECIFIC_DATE_FOMRAT = (x) -> {
        return x[4].matches("^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s+(0?[1-9]|[1-2][0-9]|3[01]),\\s*(19[0-9]{2}|[2-9][0-9]{3}|[0-9]{2})$");
    };

    private static final Function<String[], Boolean> ROW_MUST_HAME_FIVE_ELEMENTS = (x) -> {
        return (x.length == 5);
    };

    @Test
    @DisplayName("isValid with a function to ensure that the first element is a number.")
    public void functionTestNumberIsValid() {
        assertTrue(FIRST_ELEMENT_VALIDATOR.isValid(GOOD_ROW));
        assertFalse(FIRST_ELEMENT_VALIDATOR.isValid(BAD_ROW));
    }

    @Test
    @DisplayName("validate with a function to ensure that the first element is a number.")
    public void functionTestNumberValidate() {
        Assertions.assertDoesNotThrow(() -> FIRST_ELEMENT_VALIDATOR.validate(GOOD_ROW));
        Assertions.assertEquals(FAILURE_MESSAGE,
                Assertions.assertThrows(CsvValidationException.class, () -> FIRST_ELEMENT_VALIDATOR.validate(BAD_ROW)).getMessage()
        );
    }

    @Test
    @DisplayName("null String array is considered invalid")
    public void nullArrayIsInvalid() {
        assertFalse(FIRST_ELEMENT_VALIDATOR.isValid(null));
    }

    @Test
    @DisplayName("An empty String array is considered invalid")
    public void AnEmptyArrayIsInvalid() {
        assertFalse(FIRST_ELEMENT_VALIDATOR.isValid(EMPTY_ROW));
    }

    @Test
    @DisplayName("Simple test to show checking a name")
    public void secondElementIsTheFirstName() {
        validator = new RowFunctionValidator(SECOND_ELEMENT_IS_SIMPLE_NAME, "The second element must be the capitalized first name.");

        assertTrue(validator.isValid(GOOD_ROW));
        assertFalse(validator.isValid(BAD_ROW));
    }

    @Test
    @DisplayName("Simple test to show checking an middle initial")
    public void thirdElementIsMiddleInitial() {
        validator = new RowFunctionValidator(THIRD_ELEMENT_IS_MIDDLE_INITIAL, "The third element must be the middle initial.");

        assertTrue(validator.isValid(GOOD_ROW));
        assertFalse(validator.isValid(BAD_ROW));
    }

    @Test
    @DisplayName("Fourth element is also a name")
    public void fourthElementIsLastName() {
        validator = new RowFunctionValidator(FOURTH_ELEMENT_IS_SIMPLE_NAME, "The fourth element must be the capitalized last name.");

        assertTrue(validator.isValid(GOOD_ROW));
        assertFalse(validator.isValid(BAD_ROW));
    }

    @Test
    @DisplayName("Fifth element is a very specific date format")
    public void fifthElementIsADate() {
        validator = new RowFunctionValidator(FIFTH_ELEMENT_IS_SPECIFIC_DATE_FOMRAT, "The fifth element must be a date with the MMM dd, yyyy format.");

        assertTrue(validator.isValid(GOOD_ROW));
        assertFalse(validator.isValid(BAD_ROW));
    }

    @Test
    @DisplayName("The row must have a specific number of elements in order to be valid.")
    public void numberOfElementsInARow() {
        validator = new RowFunctionValidator(ROW_MUST_HAME_FIVE_ELEMENTS, "A Row can have only five elements.");

        assertTrue(validator.isValid(GOOD_ROW));
        assertFalse(validator.isValid(LONG_ROW));
        assertFalse(validator.isValid(SHORT_ROW));
    }
}
