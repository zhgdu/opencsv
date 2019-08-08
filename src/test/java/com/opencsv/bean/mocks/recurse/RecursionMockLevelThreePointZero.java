package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class RecursionMockLevelThreePointZero {

    /**
     * This constructor exists to ensure that opencsv cannot create this
     * bean. This bean must be created and initialized by the encapsulating
     * bean.
     *
     * @param f The floating point number to initialize the bean with
     */
    public RecursionMockLevelThreePointZero(float f) {
        floatLevelThree = f;
    }

    @CsvBindByName
    @CsvBindByPosition(position = 3)
    private float floatLevelThree;

    public float getFloatLevelThree() {
        return floatLevelThree;
    }

    public void setFloatLevelThree(float floatLevelThree) {
        this.floatLevelThree = floatLevelThree;
    }
}
