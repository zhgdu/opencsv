package com.opencsv.bean.mocks.join;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.customconverter.ErrorCodeConverter;

import java.util.List;

public class IdAndErrorSplitByName {

    @CsvBindByName
    private int id;

    @CsvBindAndSplitByName(elementType = ErrorCode.class, converter = ErrorCodeConverter.class, locale = "de-DE")
    private List<ErrorCode> ec;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ErrorCode> getEc() {
        return ec;
    }

    public void setEc(List<ErrorCode> ec) {
        this.ec = ec;
    }
}
