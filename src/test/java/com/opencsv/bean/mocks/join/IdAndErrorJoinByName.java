package com.opencsv.bean.mocks.join;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.customconverter.ErrorCodeConverter;
import org.apache.commons.collections4.MultiValuedMap;

public class IdAndErrorJoinByName {

    @CsvBindByName
    private int id;

    @CsvBindAndJoinByName(elementType = ErrorCode.class, converter = ErrorCodeConverter.class, locale = "de-DE")
    private MultiValuedMap<String, ErrorCode> ec;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MultiValuedMap<String, ErrorCode> getEc() {
        return ec;
    }

    public void setEc(MultiValuedMap<String, ErrorCode> ec) {
        this.ec = ec;
    }
}
