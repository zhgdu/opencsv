package com.opencsv

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * The purpose of this test is to show the difference between the three default writer/parser combinations.
 */
class CSVParserWriterSpec extends Specification {
    public static final int INITIAL_SIZE = 256
    @Shared
    RFC4180ParserBuilder rfc4180ParserBuilder = new RFC4180ParserBuilder();
    @Shared
    CSVParserBuilder csvParserBuilder = new CSVParserBuilder();

    @Shared
    ICSVParser rfc4180Parser = rfc4180ParserBuilder.build();
    @Shared
    ICSVParser csvParser = csvParserBuilder.build();

    @Shared
    StringWriter csvStringWriter = new StringWriter(INITIAL_SIZE);
    @Shared
    StringWriter csvParserStringWriter = new StringWriter(INITIAL_SIZE);
    @Shared
    StringWriter rfc4180StringWriter = new StringWriter(INITIAL_SIZE);

    // The reason for three different builders is I wanted three different writers
    // to ensure there is no "cross contamination" by sharing a StringWriter.
    @Shared
    CSVWriterBuilder csvWriterBuilder = new CSVWriterBuilder(csvStringWriter);
    @Shared
    CSVWriterBuilder csvParserWriterBuilder = new CSVWriterBuilder(csvParserStringWriter);
    @Shared
    CSVWriterBuilder rfc4180WriterBuilder = new CSVWriterBuilder(rfc4180StringWriter);

    @Shared
    ICSVWriter defaultCSVWriter = csvWriterBuilder.build();
    @Shared
    ICSVWriter defaultCSVParserWriter = csvParserWriterBuilder.withParser(csvParser).build();
    @Shared
    ICSVWriter defaultRFC4180ParserWriter = rfc4180WriterBuilder.withParser(rfc4180Parser).build();

    @Unroll
    def '#value1, #value2, #value3 should produce #csvWriterValue from CSVWriter, #csvParserValue from CSVParserWriter with CSVParser, and #rfc4180Value from CSVParserWriter with RFC4180Parser'(String value1, String value2, String value3, String csvWriterValue, String csvParserValue, String rfc4180Value) {
        given:
        String[] values = [value1, value2, value3]
        defaultCSVWriter.writeNext(values);
        defaultCSVParserWriter.writeNext(values);
        defaultRFC4180ParserWriter.writeNext(values);

        expect:

        csvWriterValue == csvStringWriter.toString()
        csvParserValue == csvParserStringWriter.toString()
        rfc4180Value == rfc4180StringWriter.toString()

        where:
        value1 | value2 | value3 | csvWriterValue        | csvParserValue | rfc4180Value
        "a"    | "b"    | "c"    | "\"a\",\"b\",\"c\"\n" | "a,b,c\n"      | "a,b,c\n"

    }
}