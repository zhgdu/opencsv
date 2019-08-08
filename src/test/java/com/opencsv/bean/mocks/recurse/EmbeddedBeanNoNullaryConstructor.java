package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvRecurse;

public class EmbeddedBeanNoNullaryConstructor {
    @CsvBindByPosition(position = 0)
    private int intLevelZero;

    @CsvBindByPosition(position = 1)
    private String stringLevelOne;

    @CsvBindByPosition(position = 2)
    private char charLevelTwo;

    @CsvRecurse
    private RecursionMockLevelThreePointZero levelThree;

    public char getCharLevelTwo() {
        return charLevelTwo;
    }

    public void setCharLevelTwo(char charLevelTwo) {
        this.charLevelTwo = charLevelTwo;
    }

    public String getStringLevelOne() {
        return stringLevelOne;
    }

    public void setStringLevelOne(String stringLevelOne) {
        this.stringLevelOne = stringLevelOne;
    }

    public int getIntLevelZero() {
        return intLevelZero;
    }

    public void setIntLevelZero(int intLevelZero) {
        this.intLevelZero = intLevelZero;
    }

    public RecursionMockLevelThreePointZero getLevelThree() {
        return levelThree;
    }

    public void setLevelThree(RecursionMockLevelThreePointZero levelThree) {
        this.levelThree = levelThree;
    }
}
