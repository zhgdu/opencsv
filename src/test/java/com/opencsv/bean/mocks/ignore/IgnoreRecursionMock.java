package com.opencsv.bean.mocks.ignore;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import com.opencsv.bean.CsvRecurse;

public class IgnoreRecursionMock {

    @CsvBindByName
    private int topLevelInteger;

    @CsvIgnore
    @CsvRecurse
    private IgnoreMock ignoredRecursiveMember;

    public int getTopLevelInteger() {
        return topLevelInteger;
    }

    public void setTopLevelInteger(int topLevelInteger) {
        this.topLevelInteger = topLevelInteger;
    }

    public IgnoreMock getIgnoredRecursiveMember() {
        return ignoredRecursiveMember;
    }

    public void setIgnoredRecursiveMember(IgnoreMock ignoredRecursiveMember) {
        this.ignoredRecursiveMember = ignoredRecursiveMember;
    }
}
