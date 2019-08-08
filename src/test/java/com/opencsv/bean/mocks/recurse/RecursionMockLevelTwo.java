package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvRecurse;

import java.util.Random;

public class RecursionMockLevelTwo {

    public RecursionMockLevelTwo() {
        Random r = new Random();
        levelThreePointZero = new RecursionMockLevelThreePointZero(r.nextFloat());
    }

    @CsvBindByName
    @CsvBindByPosition(position = 2)
    private char charLevelTwo;

    @CsvRecurse
    private RecursionMockLevelThreePointZero levelThreePointZero;

    @CsvRecurse
    private RecursionMockLevelThreePointOne levelThreePointOne;

    @CsvRecurse
    private RecursionMockLevelThreePointTwo levelThreePointTwo;

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
    public RecursionMockLevelThreePointZero procureTheThirdLevelPointZero() {
        return levelThreePointZero;
    }

    public RecursionMockLevelThreePointOne getLevelThreePointOne() {
        return levelThreePointOne;
    }

    public void setLevelThreePointOne(RecursionMockLevelThreePointOne levelThreePointOne) {
        this.levelThreePointOne = levelThreePointOne;
    }

    public RecursionMockLevelThreePointTwo getLevelThreePointTwo() {
        return levelThreePointTwo;
    }

    public void setLevelThreePointTwo(RecursionMockLevelThreePointTwo levelThreePointTwo) {
        this.levelThreePointTwo = levelThreePointTwo;
    }
}
