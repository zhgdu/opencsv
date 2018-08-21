package com.opencsv.bean.mocks.join;

import com.opencsv.bean.CsvBindAndJoinByPosition;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.customconverter.ErrorCodeConverter;
import org.apache.commons.collections4.MultiValuedMap;

public class IdAndErrorJoinByPosition {

    @CsvBindByPosition(position = 0)
    private int id;

    @CsvBindAndJoinByPosition(position = "1-", elementType = ErrorCode.class, converter = ErrorCodeConverter.class)
    private MultiValuedMap<Integer, ErrorCode> ec;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MultiValuedMap<Integer, ErrorCode> getEc() {
        return ec;
    }

    public void setEc(MultiValuedMap<Integer, ErrorCode> ec) {
        this.ec = ec;
    }
}
