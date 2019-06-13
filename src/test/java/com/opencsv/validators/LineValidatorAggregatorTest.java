package com.opencsv.validators;

import com.opencsv.exceptions.CsvLineValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class LineValidatorAggregatorTest {
    private static final String BAD = "bad";
    private static final String AWFUL = "awful";
    private LineValidatorAggregator aggregator;
    private LineDoesNotHaveForbiddenString lineDoesNotHaveBadString;
    private LineDoesNotHaveForbiddenString lineDoesNotHaveAwfulString;

    @BeforeEach
    public void setup() {
        aggregator = new LineValidatorAggregator();
        lineDoesNotHaveBadString = new LineDoesNotHaveForbiddenString(BAD);
        lineDoesNotHaveAwfulString = new LineDoesNotHaveForbiddenString(AWFUL);

        aggregator.addValidator(null);
        aggregator.addValidator(lineDoesNotHaveAwfulString);
        aggregator.addValidator(lineDoesNotHaveBadString);
    }


    @DisplayName("Validate that")
    @ParameterizedTest(name = "Line \"{0}\" should be {1} and if invalid it {2} have first forbidden string error and {3} have second forbidden string error")
    @CsvSource({"a;b;c, valid, will not, will not",
            "awful;b;c, invalid, will, will not",
            "a;bad;c, invalid, will not, will",
            "awful;bad;c, invalid, will, will"})
    public void validateLine(String line, String valid, String willHaveAwful, String willHaveBadString) {
        Assertions.assertAll("Parameter data must be valid.",
                () -> assertNotNull(line),
                () -> assertFalse(line.isEmpty()),
                () -> assertTrue("valid".equals(valid) || "invalid".equals(valid)),
                () -> assertTrue("will".equals(willHaveAwful) || "will not".equals(willHaveAwful)),
                () -> assertTrue("will".equals(willHaveBadString) || "will not".equals(willHaveBadString))
        );


        Assertions.assertTrue(aggregator.isValid(line) == "valid".equals(valid));

        try {
            aggregator.validate(line);
            if ("invalid".equals(valid)) {
                fail("was supposed to be invalid!");
            }
        } catch (CsvLineValidationException ex) {
            if ("valid".equals(valid)) {
                fail("was supposed to be valid!");
            }
            String exceptionMessage = ex.getMessage();
            Assertions.assertAll("Exception message is incorrect",
                    () -> assertTrue("will".equals(willHaveAwful) == exceptionMessage.contains(lineDoesNotHaveAwfulString.getMessage()), "Supposed to have Awful message."),
                    () -> assertTrue("will".equals(willHaveBadString) == exceptionMessage.contains(lineDoesNotHaveBadString.getMessage()), "Supposed to have Bad message."));
        }
    }
}
