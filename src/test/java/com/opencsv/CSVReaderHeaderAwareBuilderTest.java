package com.opencsv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Andre Rosot
 * @since 4.2
 */
public class CSVReaderHeaderAwareBuilderTest {

    private CSVReaderHeaderAwareBuilder builder;

    @BeforeEach
    public void setup() {
        this.builder = new CSVReaderHeaderAwareBuilder(new StringReader("header"));
    }

    @Test
    public void shouldCreateCsvReaderHeaderAwareInstance() {
        assertThat(builder.build(), instanceOf(CSVReaderHeaderAware.class));
    }

    @Test
    public void shouldThrowExceptionWhenCannotReadHeader() throws IOException {
        Reader reader = mock(Reader.class);
        when(reader.read(any((char[].class)), eq(0), eq(8192))).thenThrow(new IOException());
        Assertions.assertThrows(RuntimeException.class, () -> {
            new CSVReaderHeaderAwareBuilder(reader).build();
        });
    }
}