package com.opencsv

import spock.lang.Specification
import spock.lang.Unroll

class CSVParserSpec extends Specification {
    @Unroll
    def 'parsing #testLine from String to array back to String returns the same result'(String testLine) {
        given:
        CSVParserBuilder builder = new CSVParserBuilder()
        CSVParser parser = builder.build()
        String[] parsedValues = parser.parseLine(testLine)
        String finalString = parser.parseToLine(parsedValues, false)

        expect:
        finalString == testLine

        where:
        testLine                                             | _
        "This,is,a,test"                                     | _
        "7,seven,7.89,12/11/16"                              | _
        "a,\"b,b,b\",c"                                      | _
        "a,b,c"                                              | _
        "a,\"PO Box 123,\nKippax,ACT. 2615.\nAustralia\",d." | _
        "zo\"\"har\"\"at,10-04-1980,29,C:\\\\foo.txt"        | _
    }

    def 'bug 165 - No character line showing up as an extra record with CSVParser'() {
        given:
        List<String[]> lines = new ArrayList<String[]>();

        lines.add(["value 1.1", "\n"])
        lines.add(["value 2.1", "value 2.2"])
        lines.add(["\"value 3.1\"", "\"I talked with Stefan and he asked \"\"\nWhat about odd number of quotes?\"\" and now I have doubts about my solution\""])

        when:
        StringWriter stringWriter = new StringWriter(128);

        CSVWriter csvWriter = new CSVWriter(stringWriter);
        for (String[] strings : lines) {
            csvWriter.writeNext(strings);
        }
        csvWriter.close();

        StringReader stringReader = new StringReader(stringWriter.toString());

        CSVReader csvReader = new CSVReaderBuilder(stringReader).withCSVParser(new CSVParser()).build();

        List<String[]> readLines = csvReader.readAll();

        csvReader.close()

        then:
        lines == readLines
    }

    @Unroll
    def 'parseToLine with applyQuotesToAll of false of #string1, #string2, #string3, #string4 should yield #expectedResult'() {
        given:
        CSVParserBuilder builder = new CSVParserBuilder()
        CSVParser parser = builder.build()
        String[] items = [string1, string2, string3, string4]
        String line = parser.parseToLine(items, false)

        expect:
        line == expectedResult

        where:
        string1     | string2             | string3 | string4  | expectedResult
        "This"      | " is"               | " a"    | " test." | "This, is, a, test."
        "This line" | " has \"a\" quote " | "in"    | "it"     | "This line, has \"\"a\"\" quote ,in,it"
    }

    @Unroll
    def 'parseToLine with applyQuotesToAll of true of #string1, #string2, #string3, #string4 should yield #expectedResult'() {
        given:
        CSVParserBuilder builder = new CSVParserBuilder()
        CSVParser parser = builder.build()
        String[] items = [string1, string2, string3, string4]
        String line = parser.parseToLine(items, true)

        expect:
        line == expectedResult

        where:
        string1     | string2             | string3 | string4  | expectedResult
        "This"      | " is"               | " a"    | " test." | "\"This\",\" is\",\" a\",\" test.\""
        "This line" | " has \"a\" quote " | "in"    | "it"     | "\"This line\",\" has \"\"a\"\" quote \",\"in\",\"it\""
    }
}
