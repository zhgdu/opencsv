package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class RecursionMockLevelThreePointTwo {
    @CsvBindByName
    @CsvBindByPosition(position = 5)
    private short shortLevelThree;

    public short getShortLevelThree() {
        return shortLevelThree;
    }

    public void setShortLevelThree(short shortLevelThree) {
        this.shortLevelThree = shortLevelThree;
    }
}
