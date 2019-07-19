package com.opencsv.bean.mocks.temporal;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.temporal.TemporalAccessor;

public class InvalidFormatStringWriting {
    @CsvBindByPosition(position = 0)
    @CsvDate(writeFormatEqualsReadFormat = false, writeFormat = "invalid format string")
    public TemporalAccessor ta;

    public InvalidFormatStringWriting() {}
    public InvalidFormatStringWriting(TemporalAccessor ta) { this.ta = ta; }
}
