package com.opencsv;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CSVParserWriter implements ICSVWriter {
    protected final Writer writer;
    protected final ICSVParser parser;
    protected final String lineEnd;

    public CSVParserWriter(Writer writer, ICSVParser parser, String lineEnd) {
        this.writer = writer;
        this.parser = parser;
        this.lineEnd = lineEnd;
    }

    @Override
    public void writeAll(Iterable<String[]> allLines, boolean applyQuotesToAll) {

    }

    @Override
    public void writeAll(List<String[]> allLines, boolean applyQuotesToAll) {

    }

    @Override
    public void writeAll(Iterable<String[]> allLines) {

    }

    @Override
    public void writeAll(List<String[]> allLines) {

    }

    @Override
    public int writeAll(ResultSet rs, boolean includeColumnNames) throws SQLException, IOException {
        return 0;
    }

    @Override
    public int writeAll(ResultSet rs, boolean includeColumnNames, boolean trim) throws SQLException, IOException {
        return 0;
    }

    @Override
    public int writeAll(ResultSet rs, boolean includeColumnNames, boolean trim, boolean applyQuotesToAll) throws SQLException, IOException {
        return 0;
    }

    @Override
    public void writeNext(String[] nextLine, boolean applyQuotesToAll) {

    }

    @Override
    public void writeNext(String[] nextLine) {

    }

    @Override
    public boolean checkError() {
        return false;
    }

    @Override
    public void setResultService(ResultSetHelper resultService) {

    }

    @Override
    public void flushQuietly() {

    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }
}
