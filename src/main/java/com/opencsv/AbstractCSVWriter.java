package com.opencsv;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * The AbstractCSVWriter was created to prevent duplication of code between the CSVWriter and the
 * CSVParserWriter classes.
 *
 * @since 4.2
 */
public abstract class AbstractCSVWriter implements ICSVWriter {

    protected final Writer writer;
    protected String lineEnd;
    protected ResultSetHelper resultService;
    protected volatile IOException exception;

    /**
     * Constructor to initialize the common values.
     * @param writer Writer used for output of csv data.
     * @param lineEnd String to append at end of data (either "\n" or "\r\n").
     */
    public AbstractCSVWriter(Writer writer, String lineEnd) {
        this.writer = writer;
        this.lineEnd = lineEnd;
    }

    /**
     * Writes iterable to a CSV file. The list is assumed to be a String[]
     *
     * @param allLines         an Iterable of String[], with each String[] representing a line of
     *                         the file.
     * @param applyQuotesToAll true if all values are to be quoted.  false if quotes only
     *                         to be applied to values which contain the separator, escape,
     *                         quote or new line characters.
     */
    @Override
    public void writeAll(Iterable<String[]> allLines, boolean applyQuotesToAll) {
        StringBuilder sb = new StringBuilder(INITIAL_STRING_SIZE);
        try {
            for (String[] line : allLines) {
                writeNext(line, applyQuotesToAll, sb);
                sb.setLength(0);
            }
        } catch (IOException e) {
            exception = e;
        }
    }

    /**
     * Writes the entire list to a CSV file.
     * The list is assumed to be a String[].
     *
     * @param allLines         A List of String[] with each String[] representing a line of
     *                         the file.
     * @param applyQuotesToAll True if all values are to be quoted. False if quotes only
     *                         to be applied to values which contain the separator, escape,
     *                         quote, or new line characters.
     */
    @Override
    public void writeAll(List<String[]> allLines, boolean applyQuotesToAll) {
        writeAll((Iterable<String[]>) allLines, applyQuotesToAll);
    }

    /**
     * Writes iterable to a CSV file. The list is assumed to be a String[]
     *
     * @param allLines an Iterable of String[], with each String[] representing a line of
     *                 the file.
     */
    @Override
    public void writeAll(Iterable<String[]> allLines) {
        StringBuilder sb = new StringBuilder(INITIAL_STRING_SIZE);
        try {
            for (String[] line : allLines) {
                writeNext(line, true, sb);
                sb.setLength(0);
            }
        } catch (IOException e) {
            exception = e;
        }
    }

    /**
     * Writes the entire list to a CSV file.
     * The list is assumed to be a String[].
     *
     * @param allLines A List of String[] with each String[] representing a line of
     *                 the file.
     */
    @Override
    public void writeAll(List<String[]> allLines) {
        writeAll((Iterable<String[]>) allLines);
    }

    /**
     * Writes the column names.
     *
     * @param rs               ResultSet containing column names.
     * @param applyQuotesToAll Whether all header names should be quoted.
     * @throws SQLException Thrown by {@link ResultSetHelper#getColumnNames(java.sql.ResultSet)}
     */
    protected void writeColumnNames(ResultSet rs, boolean applyQuotesToAll) throws SQLException {
        writeNext(resultService().getColumnNames(rs), applyQuotesToAll);
    }

    /**
     * Writes the entire ResultSet to a CSV file.
     * <p>
     * The caller is responsible for closing the ResultSet. Values are not trimmed.
     * Quotes are applied to all values in the output.
     *
     * @param rs                 The result set to write
     * @param includeColumnNames True if you want column names in the output, false otherwise
     * @return Number of lines written.
     * @throws IOException  Thrown by ResultSetHelper.getColumnValues()
     * @throws SQLException Thrown by ResultSetHelper.getColumnValues()
     */
    @Override
    public int writeAll(ResultSet rs, boolean includeColumnNames) throws SQLException, IOException {
        return writeAll(rs, includeColumnNames, false, true);
    }

    /**
     * Writes the entire ResultSet to a CSV file.
     * <p>
     * The caller is responsible for closing the ResultSet. Quotes are applied to
     * all values in the output.
     *
     * @param rs                 The Result set to write.
     * @param includeColumnNames Include the column names in the output.
     * @param trim               Remove spaces from the data before writing.
     * @return Number of lines written - including header.
     * @throws IOException  Thrown by ResultSetHelper.getColumnValues()
     * @throws SQLException Thrown by ResultSetHelper.getColumnValues()
     */
    @Override
    public int writeAll(ResultSet rs, boolean includeColumnNames, boolean trim) throws SQLException, IOException {
        return writeAll(rs, includeColumnNames, trim, true);
    }

    /**
     * Writes the entire ResultSet to a CSV file.
     * <p>
     * The caller is responsible for closing the ResultSet.
     *
     * @param rs                 The Result set to write.
     * @param includeColumnNames Include the column names in the output.
     * @param trim               Remove spaces from the data before writing.
     * @param applyQuotesToAll   Whether all values should be quoted.
     * @return Number of lines written - including header.
     * @throws IOException  Thrown by ResultSetHelper.getColumnValues()
     * @throws SQLException Thrown by ResultSetHelper.getColumnValues()
     */
    @Override
    public int writeAll(ResultSet rs, boolean includeColumnNames, boolean trim, boolean applyQuotesToAll) throws SQLException, IOException {
        int linesWritten = 0;

        if (includeColumnNames) {
            writeColumnNames(rs, applyQuotesToAll);
            linesWritten++;
        }

        while (rs.next()) {
            writeNext(resultService().getColumnValues(rs, trim), applyQuotesToAll);
            linesWritten++;
        }

        return linesWritten;
    }

    /**
     * Writes the next line to the file.
     *
     * @param nextLine         A string array with each comma-separated element as a separate
     *                         entry.
     * @param applyQuotesToAll True if all values are to be quoted. False applies quotes only
     *                         to values which contain the separator, escape, quote, or new line characters.
     */
    @Override
    public void writeNext(String[] nextLine, boolean applyQuotesToAll) {
        try {
            writeNext(nextLine, applyQuotesToAll, new StringBuilder(INITIAL_STRING_SIZE));
        } catch (IOException e) {
            exception = e;
        }
    }

    /**
     * Writes the next line to the file.  This method is a fail-fast method that will throw the
     * IOException of the writer supplied to the CSVWriter (if the Writer does not handle the exceptions itself like
     * the PrintWriter class).
     *
     * @param nextLine         a string array with each comma-separated element as a separate
     *                         entry.
     * @param applyQuotesToAll true if all values are to be quoted.  false applies quotes only
     *                         to values which contain the separator, escape, quote or new line characters.
     * @param appendable       Appendable used as buffer.
     * @throws IOException Exceptions thrown by the writer supplied to CSVWriter.
     */
    protected abstract void writeNext(String[] nextLine, boolean applyQuotesToAll, Appendable appendable) throws IOException;

    /**
     * Writes the next line to the file.
     *
     * @param nextLine A string array with each comma-separated element as a separate
     *                 entry.
     */
    @Override
    public void writeNext(String[] nextLine) {
        writeNext(nextLine, true);
    }

    /**
     * Flush underlying stream to writer.
     *
     * @throws IOException If bad things happen
     */
    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    /**
     * Close the underlying stream writer flushing any buffered content.
     *
     * @throws IOException If bad things happen
     */
    @Override
    public void close() throws IOException {
        flush();
        writer.close();
    }

    /**
     * Flushes the buffer and checks to see if the there has been an error in the printstream.
     *
     * @return True if the print stream has encountered an error
     * either on the underlying output stream or during a format
     * conversion.
     */
    @Override
    public boolean checkError() {

        if (writer instanceof PrintWriter) {
            PrintWriter pw = (PrintWriter) writer;
            return pw.checkError();
        }

        flushQuietly();  // checkError in the PrintWriter class flushes the buffer so we shall too.
        return exception != null;
    }

    /**
     * Sets the result service.
     *
     * @param resultService The ResultSetHelper
     */
    @Override
    public void setResultService(ResultSetHelper resultService) {
        this.resultService = resultService;
    }

    /**
     * Lazy resultSetHelper creation.
     *
     * @return Instance of resultSetHelper
     */
    protected ResultSetHelper resultService() {
        if (resultService == null) {
            resultService = new ResultSetHelperService();
        }
        return resultService;
    }

    /**
     * Flushes the writer without throwing any exceptions.
     */
    @Override
    public void flushQuietly() {
        try {
            flush();
        } catch (IOException e) {
            // catch exception and ignore.
        }
    }
}
