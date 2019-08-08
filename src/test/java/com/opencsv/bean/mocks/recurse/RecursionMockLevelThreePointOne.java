package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class RecursionMockLevelThreePointOne {
    @CsvBindByName
    @CsvBindByPosition(position = 4)
    private boolean booleanLevelThree;

    public boolean isBooleanLevelThree() {
        return booleanLevelThree;
    }

    public void setBooleanLevelThree(boolean booleanLevelThree) {
        this.booleanLevelThree = booleanLevelThree;
    }
}
