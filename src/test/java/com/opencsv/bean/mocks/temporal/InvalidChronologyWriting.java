package com.opencsv.bean.mocks.temporal;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.temporal.TemporalAccessor;

public class InvalidChronologyWriting {
    @CsvBindByPosition(position = 0)
    @CsvDate(writeChronologyEqualsReadChronology = false, writeChronology = "invalid")
    public TemporalAccessor ta;

    public InvalidChronologyWriting() {}
    public InvalidChronologyWriting(TemporalAccessor ta) { this.ta = ta;}
}
