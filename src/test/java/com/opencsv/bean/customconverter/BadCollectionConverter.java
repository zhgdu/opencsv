package com.opencsv.bean.customconverter;

import com.opencsv.bean.AbstractCsvConverter;

public class BadCollectionConverter extends AbstractCsvConverter {

    private BadCollectionConverter() {}

    @Override
    public Object convertToRead(String value) {
        return null;
    }
}
