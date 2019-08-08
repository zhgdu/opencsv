package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvRecurse;

public class RecursionMockLevelOne {

    @CsvBindByName(required = true)
    @CsvBindByPosition(position = 1, required = true)
    private String stringLevelOne;

    @CsvRecurse
    private RecursionMockLevelTwo levelTwo;

    public String getStringLevelOne() {
        return stringLevelOne;
    }

    public void setStringLevelOne(String stringLevelOne) {
        this.stringLevelOne = stringLevelOne;
    }

    public RecursionMockLevelTwo getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(RecursionMockLevelTwo levelTwo) {
        this.levelTwo = levelTwo;
    }
}
