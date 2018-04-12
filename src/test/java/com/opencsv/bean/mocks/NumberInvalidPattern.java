package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvNumber;

public class NumberInvalidPattern {

    @CsvBindByName
    @CsvNumber("0.0.0")
    private Integer number;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
