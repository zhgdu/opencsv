package com.opencsv;

import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.processor.RowProcessor;
import com.opencsv.validators.LineValidator;
import com.opencsv.validators.RowValidator;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.io.Reader;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Builder for {@link CSVReaderHeaderAware}.
 *
 * @author Andre Rosot
 * @since 4.2
 */
public class CSVReaderHeaderAwareBuilder extends CSVReaderBaseBuilder<CSVReaderHeaderAware> {

    /**
     * Sets the reader to an underlying CSV source.
     *
     * @param reader The reader to an underlying CSV source.
     */
    public CSVReaderHeaderAwareBuilder(Reader reader) {
        super(reader);
    }

    /**
     * Sets the number of lines to skip before reading.
     *
     * @param skipLines The number of lines to skip before reading.
     * @return {@code this}
     */
    public CSVReaderHeaderAwareBuilder withSkipLines(
            final int skipLines) {
        this.skipLines = Math.max(skipLines, 0);
        return this;
    }

    /**
     * Sets the parser to use to parse the input.
     *
     * @param icsvParser The parser to use to parse the input.
     * @return {@code this}
     */
    public CSVReaderHeaderAwareBuilder withCSVParser(
            final ICSVParser icsvParser) {
        this.icsvParser = icsvParser;
        return this;
    }

    /**
     * Sets if the reader will keep or discard carriage returns.
     *
     * @param keepCR True to keep carriage returns, false to discard.
     * @return {@code this}
     */
    public CSVReaderHeaderAwareBuilder withKeepCarriageReturn(boolean keepCR) {
        this.keepCR = keepCR;
        return this;
    }

    /**
     * Checks to see if the {@link CSVReaderHeaderAware} should verify the reader state before
     * reads or not.
     *
     * <p>This should be set to false if you are using some form of asynchronous
     * reader (like readers created by the java.nio.* classes).</p>
     *
     * <p>The default value is true.</p>
     *
     * @param verifyReader True if {@link CSVReaderHeaderAware} should verify reader before each read, false otherwise.
     * @return {@code this}
     */
    public CSVReaderHeaderAwareBuilder withVerifyReader(boolean verifyReader) {
        this.verifyReader = verifyReader;
        return this;
    }

    /**
     * Checks to see if it should treat a field with two separators, two quotes, or both as a null field.
     *
     * @param indicator {@link CSVReaderNullFieldIndicator} set to what should be considered a null field.
     * @return {@code this}
     */
    public CSVReaderHeaderAwareBuilder withFieldAsNull(CSVReaderNullFieldIndicator indicator) {
        this.nullFieldIndicator = indicator;
        return this;
    }

    /**
     * Sets the maximum number of lines allowed in a multiline record.
     * More than this number in one record results in an IOException.
     *
     * @param multilineLimit No more than this number of lines is allowed in a
     *                       single input record. The default is {@link CSVReader#DEFAULT_MULTILINE_LIMIT}.
     * @return {@code this}
     */
    public CSVReaderHeaderAwareBuilder withMultilineLimit(int multilineLimit) {
        this.multilineLimit = multilineLimit;
        return this;
    }

    /**
     * Sets the locale for all error messages.
     *
     * @param errorLocale Locale for error messages
     * @return {@code this}
     * @since 4.0
     */
    public CSVReaderHeaderAwareBuilder withErrorLocale(Locale errorLocale) {
        this.errorLocale = ObjectUtils.defaultIfNull(errorLocale, Locale.getDefault());
        return this;
    }

    /**
     * Adds a {@link LineValidator} to the {@link CSVReaderHeaderAware}.
     * Multiple {@link LineValidator}s can be added with multiple calls.
     *
     * @param lineValidator {@link LineValidator} to inject.
     * @return {@code this}
     * @since 5.0
     */
    public CSVReaderHeaderAwareBuilder withLineValidator(LineValidator lineValidator) {
        lineValidatorAggregator.addValidator(lineValidator);
        return this;
    }

    /**
     * Adds a {@link RowValidator} to the {@link CSVReaderHeaderAware}.
     * Multiple {@link RowValidator}s can be added with multiple calls.
     *
     * @param rowValidator {@link RowValidator} to inject
     * @return {@code this}
     * @since 5.0
     */
    public CSVReaderHeaderAwareBuilder withRowValidator(RowValidator rowValidator) {
        rowValidatorAggregator.addValidator(rowValidator);
        return this;
    }

    /**
     * Adds a {@link RowProcessor} to the {@link CSVReaderHeaderAware}.
     * Only a single {@link RowProcessor} can be added so multiple calls will overwrite
     * the previously set {@link RowProcessor}.
     *
     * @param rowProcessor {@link RowProcessor} to inject
     * @return {@code this}
     * @since 5.0
     */
    public CSVReaderHeaderAwareBuilder withRowProcessor(RowProcessor rowProcessor) {
        this.rowProcessor = rowProcessor;
        return this;
    }

    public CSVReaderHeaderAware build() throws RuntimeException {
        final ICSVParser parser = getOrCreateCsvParser();
        try {
            return new CSVReaderHeaderAware(reader, skipLines, parser, keepCR, verifyReader,
                    multilineLimit, errorLocale, lineValidatorAggregator, rowValidatorAggregator, rowProcessor);
        } catch (IOException e) {
            throw new RuntimeException(ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale).getString("csvreaderheaderaware.impossible"), e);
        }
    }
}
