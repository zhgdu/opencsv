package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvRecurse;

import java.util.Random;

public class RecursionMockLevelTwoNoAnnotations {

    public RecursionMockLevelTwoNoAnnotations() {
        Random r = new Random();
        levelThreePointZero = new RecursionMockLevelThreePointZeroNoAnnotations(r.nextFloat());
    }

    private char charLevelTwo;

    @CsvRecurse
    private final RecursionMockLevelThreePointZeroNoAnnotations levelThreePointZero;

    @CsvRecurse
    private RecursionMockLevelThreePointOneNoAnnotations levelThreePointOne;

    @CsvRecurse
    private RecursionMockLevelThreePointTwoNoAnnotations levelThreePointTwo;

    public char getCharLevelTwo() {
        return charLevelTwo;
    }

    public void setCharLevelTwo(char charLevelTwo) {
        this.charLevelTwo = charLevelTwo;
    }

    /**
     * Accessor method for the third subordinate bean.
     * Their is no assignment method and the accessor method is named so it
     * will not be found by opencsv. This ensures reflection is used to get
     * and set the value.
     *
     * @return The bean at the third level of subordination
     */
    public RecursionMockLevelThreePointZeroNoAnnotations procureTheThirdLevelPointZero() {
        return levelThreePointZero;
    }

    public RecursionMockLevelThreePointOneNoAnnotations getLevelThreePointOne() {
        return levelThreePointOne;
    }

    public void setLevelThreePointOne(RecursionMockLevelThreePointOneNoAnnotations levelThreePointOne) {
        this.levelThreePointOne = levelThreePointOne;
    }

    public RecursionMockLevelThreePointTwoNoAnnotations getLevelThreePointTwo() {
        return levelThreePointTwo;
    }

    public void setLevelThreePointTwo(RecursionMockLevelThreePointTwoNoAnnotations levelThreePointTwo) {
        this.levelThreePointTwo = levelThreePointTwo;
    }
}
