package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvRecurse;

public class RecursionMockLevelZeroNoAnnotations {
    private int intLevelZero;

    @CsvRecurse
    private RecursionMockLevelOneNoAnnotations levelOne;

    public int getIntLevelZero() {
        return intLevelZero;
    }

    public void setIntLevelZero(int intLevelZero) {
        this.intLevelZero = intLevelZero;
    }

    public RecursionMockLevelOneNoAnnotations getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(RecursionMockLevelOneNoAnnotations levelOne) {
        this.levelOne = levelOne;
    }
}
