package com.opencsv;

import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.processor.RowProcessor;
import com.opencsv.validators.LineValidatorAggregator;
import com.opencsv.validators.RowValidatorAggregator;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Reader;
import java.util.Locale;

/**
 * Base class for the builders of various incarnations of CSVReaders.
 * @param <T> The type pf the CSVReader class to return
 *
 * @author Andrew Rucker Jones
 * @since 5.5.2
 */
abstract public class CSVReaderBaseBuilder<T> {
    protected final Reader reader;
    protected final LineValidatorAggregator lineValidatorAggregator = new LineValidatorAggregator();
    protected final RowValidatorAggregator rowValidatorAggregator = new RowValidatorAggregator();
    private final CSVParserBuilder parserBuilder = new CSVParserBuilder();
    protected int skipLines = CSVReader.DEFAULT_SKIP_LINES;
    protected ICSVParser icsvParser = null;
    protected boolean keepCR;
    protected boolean verifyReader = CSVReader.DEFAULT_VERIFY_READER;
    protected CSVReaderNullFieldIndicator nullFieldIndicator = CSVReaderNullFieldIndicator.NEITHER;
    protected int multilineLimit = CSVReader.DEFAULT_MULTILINE_LIMIT;
    protected Locale errorLocale = Locale.getDefault();
    protected RowProcessor rowProcessor = null;

    public CSVReaderBaseBuilder(final Reader reader) {
        this.reader = reader;
    }

    /**
     * Used by unit tests.
     *
     * @return The reader.
     */
    protected Reader getReader() {
        return reader;
    }

    /**
     * Used by unit tests.
     *
     * @return The set number of lines to skip
     */
    protected int getSkipLines() {
        return skipLines;
    }

    /**
     * Used by unit tests.
     *
     * @return The CSVParser used by the builder.
     */
    protected ICSVParser getCsvParser() {
        return icsvParser;
    }

    /**
     * Used by unit tests.
     *
     * @return The upper limit on lines in multiline records.
     */
    protected int getMultilineLimit() {
        return multilineLimit;
    }

    /**
     * Returns if the reader built will keep or discard carriage returns.
     *
     * @return {@code true} if the reader built will keep carriage returns,
     * {@code false} otherwise
     */
    protected boolean keepCarriageReturn() {
        return this.keepCR;
    }

    /**
     * Creates a new {@link ICSVParser} if the class doesn't already hold one.
     *
     * @return The injected {@link ICSVParser} or a default parser.
     */
    protected ICSVParser getOrCreateCsvParser() {
        return ObjectUtils.defaultIfNull(icsvParser,
                parserBuilder
                        .withFieldAsNull(nullFieldIndicator)
                        .withErrorLocale(errorLocale)
                        .build());
    }

    /**
     * @return The flag indicating whether the reader should be verified before each read.
     */
    public boolean isVerifyReader() {
        return verifyReader;
    }

    /**
     * @return The locale for error messages
     */
    public Locale getErrorLocale() {
        return errorLocale;
    }

    /**
     * @return The {@link LineValidatorAggregator} for custom defined {@link com.opencsv.validators.LineValidator}s.
     */
    public LineValidatorAggregator getLineValidatorAggregator() {
        return lineValidatorAggregator;
    }

    /**
     * @return The {@link RowValidatorAggregator} for the custom defined {@link com.opencsv.validators.RowValidator}s.
     */
    public RowValidatorAggregator getRowValidatorAggregator() {
        return rowValidatorAggregator;
    }

    /**
     * Must create the CSVReader type requested.
     * @return A new instance of {@link CSVReader} or derived class
     */
    public abstract T build();
}
