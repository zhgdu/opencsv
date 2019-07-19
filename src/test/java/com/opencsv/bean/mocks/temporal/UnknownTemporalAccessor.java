package com.opencsv.bean.mocks.temporal;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

public class UnknownTemporalAccessor {
    @CsvBindByPosition(position = 0)
    @CsvDate
    private OpencsvTemporalAccessor ta;
}
