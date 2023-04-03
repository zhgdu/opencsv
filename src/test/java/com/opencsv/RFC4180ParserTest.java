package com.opencsv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RFC4180ParserTest {

    private RFC4180ParserBuilder builder;
    private RFC4180Parser parser;

    @BeforeEach
    public void setUp() {
        builder = new RFC4180ParserBuilder();
        parser = builder.build();
    }

    @DisplayName("Compare both versions of parseToLine")
    @ParameterizedTest(name = "value 1 = {0} value 2 = {1} value 3 = {2}")
    @CsvSource({"value1, value2, value3",
            "slightly more complex value, another value, third value"})
    public void compareParseToLine(String value1, String value2, String value3) throws IOException {
        String[] values = {value1, value2, value3};
        StringBuilder stringBuilder = new StringBuilder();

        String parse1 = parser.parseToLine(values, true);
        parser.parseToLine(values, true, stringBuilder);

        assertEquals(parse1, stringBuilder.toString());
    }
}
