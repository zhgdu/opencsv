package com.opencsv;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CSVIteratorTest {
    private static final String[] STRINGS = {"test1", "test2"};
    private CSVIterator iterator;
    private CSVReader mockReader;

    private static Locale systemLocale;

    @BeforeAll
    public static void storeSystemLocale() {
        systemLocale = Locale.getDefault();
    }

    @AfterEach
    public void setSystemLocaleBackToDefault() {
        Locale.setDefault(systemLocale);
    }

    @BeforeEach
    public void setUp() throws IOException, CsvValidationException {
        Locale.setDefault(Locale.US);
        mockReader = mock(CSVReader.class);
        when(mockReader.readNext()).thenReturn(STRINGS);
        iterator = new CSVIterator(mockReader);
    }

    @Test
    public void readerExceptionCausesRunTimeException() throws IOException, CsvValidationException {
        when(mockReader.readNext()).thenThrow(new IOException("reader threw test exception"));
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
        });
    }

    @Test
    public void removethrowsUnsupportedOperationException() {
        String englishErrorMessage = null;
        try {
            iterator.remove();
            fail("UnsupportedOperationException should have been thrown by read-only iterator.");
        }
        catch(UnsupportedOperationException e) {
            englishErrorMessage = e.getLocalizedMessage();
        }
        
        // Now with a different locale
        iterator.setErrorLocale(Locale.GERMAN);
        try {
            iterator.remove();
            fail("UnsupportedOperationException should have been thrown by read-only iterator.");
        }
        catch(UnsupportedOperationException e) {
            assertNotSame(englishErrorMessage, e.getLocalizedMessage());
        }
    }

    @Test
    public void initialReadReturnsStrings() {
        assertArrayEquals(STRINGS, iterator.next());
    }

    @Test
    public void hasNextWorks() throws IOException, CsvValidationException {
        when(mockReader.readNext()).thenReturn(null);
        assertTrue(iterator.hasNext()); // initial read from constructor
        iterator.next();
        assertFalse(iterator.hasNext());
    }
}
