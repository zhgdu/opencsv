package com.opencsv.bean;

import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.util.Locale;
import java.util.UUID;

/**
 * This class converts an String to a {@link java.util.UUID}
 * instance.
 *
 * @author Scott Conway
 * @since 5.4
 */
public class ConverterUUID extends AbstractCsvConverter {
    /**
     * Initializes the class.
     *
     * @param errorLocale The locale to use for error messages
     */
    public ConverterUUID(Locale errorLocale) {
        super(UUID.class, null, null, errorLocale);
    }

    @Override
    public Object convertToRead(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return UUID.fromString(value.trim());
    }
}
