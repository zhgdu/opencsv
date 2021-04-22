package com.opencsv.bean.mocks.recurse;

public class RecursionMockLevelThreePointZeroNoAnnotations {

    /**
     * This constructor exists to ensure that opencsv cannot create this
     * bean. This bean must be created and initialized by the encapsulating
     * bean.
     *
     * @param f The floating point number to initialize the bean with
     */
    public RecursionMockLevelThreePointZeroNoAnnotations(float f) {
        floatLevelThree = f;
    }

    private float floatLevelThree;

    public float getFloatLevelThree() {
        return floatLevelThree;
    }

    public void setFloatLevelThree(float floatLevelThree) {
        this.floatLevelThree = floatLevelThree;
    }
}
