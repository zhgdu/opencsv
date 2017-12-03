package com.opencsv;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Handy reader when there's no motivation enough to use the bean binding but the header mapping is still desired
 * @Author Andre Rosot
 */
public class CSVReaderHeaderAware extends CSVReader {

    private final Map<String, Integer> headerIndex = new HashMap<>();

    public CSVReaderHeaderAware(Reader reader) throws IOException {
        super(reader);
        String[] headers = readNext();
        for (int i = 0; i < headers.length; i++) {
            headerIndex.put(headers[i], i);
        }
    }

    public String readNext(String headerName) throws IOException {
        Integer index = headerIndex.get(headerName);
        if (index != null) {
            String[] strings = readNext();
            if (index < strings.length) {
                return strings[index];
            }
        }
        throw new IllegalArgumentException("No column found for header: " + headerName);
    }

    public Map<String, String> readMap() throws IOException {
        String[] strings = readNext();
        if (strings == null) return null;
        Map<String, String> mappedLine = new HashMap<>();
        for (Map.Entry<String, Integer> entry : headerIndex.entrySet()) {
            if (entry.getValue() < strings.length) {
                mappedLine.put(entry.getKey(), strings[entry.getValue()]);
            }
        }
        return mappedLine;
    }
}
