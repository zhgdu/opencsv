package com.opencsv;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Handy reader when there's insufficient motivation to use the bean binding but
 * the header mapping is still desired.
 * 
 * @author Andre Rosot
 */
public class CSVReaderHeaderAware extends CSVReader {

    private final Map<String, Integer> headerIndex = new HashMap<>();

    /**
     * Constructor with supplied reader.
     *
     * @param reader The reader to an underlying CSV source
     * @throws IOException If there is an error when reading the header
     */
    public CSVReaderHeaderAware(Reader reader) throws IOException {
        super(reader);
        String[] headers = super.readNext();
        for (int i = 0; i < headers.length; i++) {
            headerIndex.put(headers[i], i);
        }
    }

    /**
     * Retrieves a specific data element from a line based on the value of the header.
     *
     * @param headerName Name of the header element whose data we are trying to find
     * @return The data element whose position matches that of the header whose value is passed in. Will return null when there are no more data elements.
     * @throws IOException An error occured during the read or there is a mismatch in the number of data items in a row
     * and the number of header items
     * @throws IllegalArgumentException If headerName does not exist
     */
    public String readNext(String headerName) throws IOException {
        Integer index = headerIndex.get(headerName);
        if (index == null) {
            throw new IllegalArgumentException(String.format(
                    ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                            .getString("header.nonexistant"),
                    headerName));
        }

        String[] strings = readNext();

        if (strings == null) {
            return null;
        }

        if (strings.length != headerIndex.size()) {
            throw new IOException(String.format(
                    ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                            .getString("header.data.mismatch.with.line.number"),
                    getRecordsRead()));
        }

        return strings[index];

    }

    /**
     * Reads the next line and returns a map of header values and data values.
     *
     * @return A map whose key is the header row of the data file and the values is the data values. Or null if the line is blank.
     * @throws IOException An error occured during the read or there is a mismatch in the number of data items in a row
     * and the number of header items.
     */
    public Map<String, String> readMap() throws IOException {
        String[] strings = readNext();
        if (strings == null) {
            return null;
        }
        if (strings.length != headerIndex.size()) {
            throw new IOException(String.format(
                    ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                            .getString("header.data.mismatch.with.line.number"),
                    getRecordsRead()));
        }
        Map<String, String> mappedLine = new HashMap<>();
        for (Map.Entry<String, Integer> entry : headerIndex.entrySet()) {
            if (entry.getValue() < strings.length) {
                mappedLine.put(entry.getKey(), strings[entry.getValue()]);
            }
        }
        return mappedLine;
    }
}
