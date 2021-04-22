package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvRecurse;

public class RecursionMockLevelOneNoAnnotations {

    private String stringLevelOne;

    @CsvRecurse
    private RecursionMockLevelTwoNoAnnotations levelTwo;

    public String getStringLevelOne() {
        return stringLevelOne;
    }

    public void setStringLevelOne(String stringLevelOne) {
        this.stringLevelOne = stringLevelOne;
    }

    public RecursionMockLevelTwoNoAnnotations getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(RecursionMockLevelTwoNoAnnotations levelTwo) {
        this.levelTwo = levelTwo;
    }
}
