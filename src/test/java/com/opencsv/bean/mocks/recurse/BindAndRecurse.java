package com.opencsv.bean.mocks.recurse;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;

public class BindAndRecurse {
    @CsvRecurse
    @CsvBindByName
    public RecursionMockLevelZero r;
}
