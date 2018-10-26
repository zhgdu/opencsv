package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBindByName;

public class SingleNumber {

    @CsvBindByName
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
