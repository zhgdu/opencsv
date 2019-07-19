package com.opencsv.bean.mocks.temporal;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.temporal.TemporalAccessor;

public class InvalidFormatStringReading {
    @CsvBindByPosition(position = 0)
    @CsvDate("invalid format string")
    public TemporalAccessor ta;

    public InvalidFormatStringReading() {}
    public InvalidFormatStringReading(TemporalAccessor ta) { this.ta = ta; }
}
