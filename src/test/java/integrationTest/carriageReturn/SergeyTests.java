package integrationTest.carriageReturn;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SergeyTests {
    @Test
    public void bug233SergeyC_Version() throws IOException, CsvValidationException {
        StringBuilder sb = new StringBuilder(ICSVParser.INITIAL_READ_SIZE);

        sb.append("\"a\",\"123\r\n4567\",c");
        sb.append("\r\n");
        sb.append("\"a\",\"b\",\"abc\r\n\"\r\n");
        sb.append("\"\\\n\\\nabc\",\"a\",\"b\"\r\n");

        // public CSVReader(Reader reader, char separator, char quotechar, char escape, int line, boolean strictQuotes,
        // boolean ignoreLeadingWhiteSpace, boolean keepCarriageReturn)
        CSVReader c = new CSVReaderBuilder(new StringReader(sb.toString()))
                .withCSVParser(new CSVParserBuilder()
                        .withStrictQuotes(false)
                        .build())
                .withKeepCarriageReturn(true)
                .build();

        String[] nextLine = c.readNext();
        assertEquals(3, nextLine.length);

        assertEquals("a", nextLine[0]);
        assertEquals(1, nextLine[0].length());

        assertEquals("123\r\n4567", nextLine[1]);
        assertFalse(nextLine[2].endsWith("\r"));
        assertEquals("c", nextLine[2]);

        nextLine = c.readNext();
        assertEquals(3, nextLine.length);
        assertEquals("abc\r\n", nextLine[2]);

        //if there are \n\n at the beginning of string and withKeepCarriageReturn is true the reader just fails
        nextLine = c.readNext();
        assertEquals("\n\nabc", nextLine[0]);

    }

    @Test
    public void bug233SergeyC_Version2() throws IOException, CsvValidationException {
        StringBuilder sb = new StringBuilder(ICSVParser.INITIAL_READ_SIZE);

        sb.append("\"a\",\"123\r\n4567\",c");
        sb.append("\r\n");
        sb.append("\"a\",\"b\",\"abc\r\"\r\n");
        sb.append("\"\\\n\\\nabc\",\"a\",\"b\"\r\n");

        // public CSVReader(Reader reader, char separator, char quotechar, char escape, int line, boolean strictQuotes,
        // boolean ignoreLeadingWhiteSpace, boolean keepCarriageReturn)
        CSVReader c = new CSVReaderBuilder(new StringReader(sb.toString()))
                .withCSVParser(new CSVParserBuilder()
                        .withStrictQuotes(false)
                        .build())
                .withKeepCarriageReturn(true)
                .build();

        String[] nextLine = c.readNext();
        assertEquals(3, nextLine.length);

        assertEquals("a", nextLine[0]);
        assertEquals(1, nextLine[0].length());

        assertEquals("123\r\n4567", nextLine[1]);
        assertFalse(nextLine[2].endsWith("\r"));
        assertEquals("c", nextLine[2]);

        nextLine = c.readNext();
        assertEquals(3, nextLine.length);
        assertEquals("abc\r", nextLine[2]);

        //if there are \n\n at the beginning of string and withKeepCarriageReturn is true the reader just fails
        nextLine = c.readNext();
        assertEquals("\n\nabc", nextLine[0]);

    }
    @Test
    @DisplayName("Is the parser removing the carriage return")
    public void parserTest() throws IOException {
        CSVParser parser = new CSVParserBuilder()
                .withStrictQuotes(true)
                .build();

        String[] nextLine = parser.parseLine("\"a\",\"b\",\"abc\r\"\n");
        assertEquals(3, nextLine.length);
        assertEquals("a", nextLine[0]);
        assertEquals("b", nextLine[1]);
        assertEquals("abc\r", nextLine[2]);
    }

    @Test
    @DisplayName("test with rfc4180parser")
    public void bug233SergeyC_rfc4180() throws IOException, CsvValidationException {
        StringBuilder sb = new StringBuilder(ICSVParser.INITIAL_READ_SIZE);

        sb.append("\"a\",\"123\r4567\r\",c");
        sb.append("\r\n");
        sb.append("\"a\",\"b\",\"abc\r\n\"\r\n");
        sb.append("\"\\\n\\\nabc\",\"a\",\"b\"\r\n");

        // public CSVReader(Reader reader, char separator, char quotechar, char escape, int line, boolean strictQuotes,
        // boolean ignoreLeadingWhiteSpace, boolean keepCarriageReturn)
        CSVReader c = new CSVReaderBuilder(new StringReader(sb.toString()))
                .withCSVParser(new RFC4180ParserBuilder()
                        .build())
                .withKeepCarriageReturn(true)
                .build();

        String[] nextLine = c.readNext();
        assertEquals(3, nextLine.length);

        assertEquals("a", nextLine[0]);
        assertEquals(1, nextLine[0].length());

        assertEquals("123\r4567\r", nextLine[1]);
        assertFalse(nextLine[2].endsWith("\r"));
        assertEquals("c", nextLine[2]);

        nextLine = c.readNext();
        assertEquals(3, nextLine.length);
        assertEquals("abc\r\n", nextLine[2]);

        //if there are \n\n at the beginning of string and withKeepCarriageReturn is true the reader just fails
        nextLine = c.readNext();
        assertEquals("\n\nabc", nextLine[0]);

    }

    @Test
    @DisplayName("reverseParse line")
    public void reverseParse(){
        String[] strings = new String[] {
                "a", "b", "abc\r" };

        CSVParser parser = new CSVParserBuilder()
                .withStrictQuotes(true)
                .build();

        String line = parser.parseToLine(strings, true);
        assertEquals("\"a\",\"b\",\"abc\r\"", line);
    }

    @Test
    @DisplayName("reverseParse then parse line")
    public void reverseParseThenParse() throws IOException {
        String[] strings = new String[] {
                "a", "b", "abc\r" };

        CSVParser parser = new CSVParserBuilder()
                .withStrictQuotes(false)
                .build();

        String line = parser.parseToLine(strings, true);
        assertEquals("\"a\",\"b\",\"abc\r\"", line);

        String[] parsedStrings = parser.parseLine(line);
        assertEquals(3, parsedStrings.length);
        assertEquals("a", parsedStrings[0]);
        assertEquals("b", parsedStrings[1]);
        assertEquals("abc\r", parsedStrings[2]);
    }

    @Test
    @DisplayName("Use RFC4180Parser to reverseParse then parse line")
    public void reverseParseThenParseWithRFC4180Parser() throws IOException {
        String[] strings = new String[] {
                "a", "b", "abc\r" };

        RFC4180Parser parser = new RFC4180ParserBuilder()
                .build();

        String line = parser.parseToLine(strings, true);
        assertEquals("\"a\",\"b\",\"abc\r\"", line);

        String[] parsedStrings = parser.parseLine(line);
        assertEquals(3, parsedStrings.length);
        assertEquals("a", parsedStrings[0]);
        assertEquals("b", parsedStrings[1]);
        assertEquals("abc\r", parsedStrings[2]);
    }
}
