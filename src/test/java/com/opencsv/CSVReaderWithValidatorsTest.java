package com.opencsv;

import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.LineDoesNotHaveForbiddenString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CSVReaderWithValidatorsTest {
    private static final String BAD = "bad";
    private static final String AWFUL = "awful";
    private LineDoesNotHaveForbiddenString lineDoesNotHaveBadString;
    private LineDoesNotHaveForbiddenString lineDoesNotHaveAwfulString;

    @BeforeEach
    public void setup() {
        lineDoesNotHaveBadString = new LineDoesNotHaveForbiddenString(BAD);
        lineDoesNotHaveAwfulString = new LineDoesNotHaveForbiddenString(AWFUL);

    }

    @DisplayName("CSVReader with LineValidator with good string")
    @Test
    public void readerWithLineValidatorWithValidString() throws IOException, CsvException {
        String lines = "a,b,c\nd,e,f\n";
        StringReader stringReader = new StringReader(lines);
        CSVReaderBuilder builder = new CSVReaderBuilder(stringReader);

        CSVReader csvReader = builder.withLineValidator(lineDoesNotHaveBadString)
                .build();

        List<String[]> rows = csvReader.readAll();
        assertEquals(2, rows.size());
    }

    @DisplayName("CSVReader with LineValidator with bad string")
    @Test
    public void readerWithLineValidatorWithBadString() throws IOException {
        String lines = "a,b,c\nd,bad,f\n";
        StringReader stringReader = new StringReader(lines);
        CSVReaderBuilder builder = new CSVReaderBuilder(stringReader);

        CSVReader csvReader = builder.withLineValidator(lineDoesNotHaveBadString)
                .build();

        assertThrows(CsvValidationException.class, () -> {
            List<String[]> rows = csvReader.readAll();
        });
    }
}
