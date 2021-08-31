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

public class CSVReaderHeaderAwareWithRowProcessorTest {
    private static final RowProcessor ROW_PROCESSOR = new BlankColumnsBecomeNull();
    private static final String LINES = "one,two,three\na,, \n, ,\n";

    @DisplayName("CSVReaderHeaderAware with RowProcessor with good string")
    @Test
    public void readerWithRowProcessor() throws IOException, CsvException {

        StringReader stringReader = new StringReader(LINES);
        CSVReaderHeaderAware csvReader = new CSVReaderHeaderAwareBuilder(stringReader)
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

    @DisplayName("CSVReaderHeaderAware without RowProcessor with good string")
    @Test
    public void readerWithoutRowProcessor() throws IOException, CsvException {

        StringReader stringReader = new StringReader(LINES);
        CSVReaderHeaderAware csvReader = new CSVReaderHeaderAwareBuilder(stringReader).build();

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