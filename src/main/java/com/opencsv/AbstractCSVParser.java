package com.opencsv;

import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * The purpose of the AbstractCSVParser is to consolidate the duplicate code agmonst the
 * parsers.
 */
public abstract class AbstractCSVParser implements ICSVParser {
    /**
     * This is the character that the CSVParser will treat as the separator.
     */
    protected final char separator;
    /**
     * This is the character that the CSVParser will treat as the quotation character.
     */
    protected final char quotechar;
    /**
     * Determines the handling of null fields.
     *
     * @See CSVReaderNullFieldIndicator
     */
    protected final CSVReaderNullFieldIndicator nullFieldIndicator;

    /**
     * Value to be appended to string to process.
     */
    protected String pending;

    /**
     * Common Constructor.
     *
     * @param separator          The delimiter to use for separating entries
     * @param quotechar          The character to use for quoted elements
     * @param nullFieldIndicator Indicate what should be considered null
     */
    public AbstractCSVParser(char separator, char quotechar, CSVReaderNullFieldIndicator nullFieldIndicator) {
        this.separator = separator;
        this.quotechar = quotechar;
        this.nullFieldIndicator = nullFieldIndicator;
    }

    @Override
    public char getSeparator() {
        return separator;
    }

    @Override
    public char getQuotechar() {
        return quotechar;
    }

    @Override
    public boolean isPending() {
        return pending != null;
    }


    @Override
    public String[] parseLineMulti(String nextLine) throws IOException {
        return parseLine(nextLine, true);
    }

    @Override
    public String[] parseLine(String nextLine) throws IOException {
        return parseLine(nextLine, false);
    }

    @Override
    public String parseToLine(String[] values, boolean applyQuotesToAll) {
        StringBuilder builder = new StringBuilder(ICSVParser.INITIAL_READ_SIZE);

        for (int i = 0; i < values.length; i++) {
            builder.append(convertToCsvValue(values[i], applyQuotesToAll));
            if (i < values.length - 1) {
                builder.append(getSeparator());
            }
        }
        return builder.toString();
    }

    /**
     * Used when reverse parsing an array of strings to a single string.  Handles the application of quotes around
     * the string and handling any quotes within the string.
     *
     * @param value            String to be tested.
     * @param applyQuotestoAll All values should be surrounded with quotes.
     * @return String that will go into the csv string.
     */
    protected abstract String convertToCsvValue(String value, boolean applyQuotestoAll);

    /**
     * Used by reverse parsing to determine if a value should be surrounded by quote characters.
     *
     * @param value         String to be tested.
     * @param forceSurround If the value is not null it will be surrounded with quotes.
     * @return True if the string should be surrounded with quotes, false otherwise.
     */
    protected boolean isSurroundWithQuotes(String value, boolean forceSurround) {
        if (value == null) {
            return nullFieldIndicator.equals(CSVReaderNullFieldIndicator.EMPTY_QUOTES);
        }

        return forceSurround || value.contains(Character.toString(getSeparator())) || value.contains(NEWLINE);
    }

    /**
     * Parses an incoming String and returns an array of elements.
     *
     * @param nextLine The string to parse
     * @param multi    Does it take multiple lines to form a single record.
     * @return The list of elements, or null if nextLine is null
     * @throws IOException If bad things happen during the read
     */
    protected abstract String[] parseLine(String nextLine, boolean multi) throws IOException;

    @Override
    public CSVReaderNullFieldIndicator nullFieldIndicator() {
        return nullFieldIndicator;
    }

    @Override
    public String getPendingText() {
        return StringUtils.defaultString(pending);
    }
}
