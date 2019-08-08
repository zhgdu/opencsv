package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvRecurse;

public class DuplicateRecurse {
    @CsvRecurse
    public RecursionMockLevelZero r1;

    @CsvRecurse
    public RecursionMockLevelZero r2;
}
