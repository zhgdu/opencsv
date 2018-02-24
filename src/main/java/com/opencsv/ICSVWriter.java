package com.opencsv;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ICSVWriter extends Closeable, Flushable {
    int INITIAL_STRING_SIZE = 1024;
    /**
     * The character used for escaping quotes.
     */
    char DEFAULT_ESCAPE_CHARACTER = '"';
    /**
     * The default separator to use if none is supplied to the constructor.
     */
    char DEFAULT_SEPARATOR = ',';
    /**
     * The default quote character to use if none is supplied to the
     * constructor.
     */
    char DEFAULT_QUOTE_CHARACTER = '"';
    /**
     * The quote constant to use when you wish to suppress all quoting.
     */
    char NO_QUOTE_CHARACTER = '\u0000';
    /**
     * The escape constant to use when you wish to suppress all escaping.
     */
    char NO_ESCAPE_CHARACTER = '\u0000';

    void writeAll(Iterable<String[]> allLines, boolean applyQuotesToAll);

    void writeAll(List<String[]> allLines, boolean applyQuotesToAll);

    void writeAll(Iterable<String[]> allLines);

    void writeAll(List<String[]> allLines);

    int writeAll(ResultSet rs, boolean includeColumnNames) throws SQLException, IOException;

    int writeAll(ResultSet rs, boolean includeColumnNames, boolean trim) throws SQLException, IOException;

    int writeAll(ResultSet rs, boolean includeColumnNames, boolean trim, boolean applyQuotesToAll) throws SQLException, IOException;

    void writeNext(String[] nextLine, boolean applyQuotesToAll);

    void writeNext(String[] nextLine);

    boolean checkError();

    void setResultService(ResultSetHelper resultService);

    void flushQuietly();
}
