package com.opencsv.bean.mocks.temporal;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.temporal.TemporalAccessor;

public class InvalidChronologyReading {
    @CsvBindByPosition(position = 0)
    @CsvDate(chronology = "invalid")
    public TemporalAccessor ta;

    public InvalidChronologyReading() {}
    public InvalidChronologyReading(TemporalAccessor ta) { this.ta = ta;}
}
