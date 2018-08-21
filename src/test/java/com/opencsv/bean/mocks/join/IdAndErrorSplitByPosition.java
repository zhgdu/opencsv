package com.opencsv.bean.mocks.join;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.customconverter.ErrorCodeConverter;

import java.util.List;

public class IdAndErrorSplitByPosition {

    @CsvBindByPosition(position = 0)
    private int id;

    @CsvBindAndSplitByPosition(position = 1, elementType = ErrorCode.class, converter = ErrorCodeConverter.class)
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
