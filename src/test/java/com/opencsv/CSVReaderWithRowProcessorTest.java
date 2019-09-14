package com.opencsv;

import com.opencsv.exceptions.CsvException;
import com.opencsv.processor.BlankColumnsBecomeNull;
import com.opencsv.processor.RowProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVReaderWithRowProcessorTest {
    private static RowProcessor ROW_PROCESSOR = new BlankColumnsBecomeNull();
    private static final String LINES = "a,, \n, ,\n";

    @DisplayName("CSVReader with RowProcessor with good string")
    @Test
    public void readerWithRowProcessor() throws IOException, CsvException {

        StringReader stringReader = new StringReader(LINES);
        CSVReaderBuilder builder = new CSVReaderBuilder(stringReader);

        CSVReader csvReader = builder
                .withRowProcessor(ROW_PROCESSOR)
                .build();

        List<String[]> rows = csvReader.readAll();
        assertEquals(2, rows.size());

        String[] row1 = rows.get(0);
        assertEquals(3, row1.length);
        assertEquals("a", row1[0]);
        assertNull(row1[1]);
        assertEquals(" ", row1[2]);

        String[] row2 = rows.get(1);
        assertEquals(3, row2.length);
        assertNull(row2[0]);
        assertEquals(" ", row2[1]);
        assertNull(row2[2]);
    }

    @DisplayName("CSVReader without RowProcessor with good string")
    @Test
    public void readerWithoutRowProcessor() throws IOException, CsvException {

        StringReader stringReader = new StringReader(LINES);
        CSVReaderBuilder builder = new CSVReaderBuilder(stringReader);

        CSVReader csvReader = builder.build();

        List<String[]> rows = csvReader.readAll();
        assertEquals(2, rows.size());

        String[] row1 = rows.get(0);
        assertEquals(3, row1.length);
        assertEquals("a", row1[0]);
        assertTrue(row1[1].isEmpty());
        assertEquals(" ", row1[2]);

        String[] row2 = rows.get(1);
        assertEquals(3, row2.length);
        assertTrue(row2[0].isEmpty());
        assertEquals(" ", row2[1]);
        assertTrue(row2[2].isEmpty());
    }
}