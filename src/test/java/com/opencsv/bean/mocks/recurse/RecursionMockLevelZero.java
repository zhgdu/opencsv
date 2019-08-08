package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvRecurse;

public class RecursionMockLevelZero {
    @CsvBindByName
    @CsvBindByPosition(position = 0)
    private int intLevelZero;

    @CsvRecurse
    private RecursionMockLevelOne levelOne;

    public int getIntLevelZero() {
        return intLevelZero;
    }

    public void setIntLevelZero(int intLevelZero) {
        this.intLevelZero = intLevelZero;
    }

    public RecursionMockLevelOne getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(RecursionMockLevelOne levelOne) {
        this.levelOne = levelOne;
    }
}
