package com.opencsv.bean.mocks.temporal;

import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;

public class OpencsvTemporalAccessor implements TemporalAccessor {
    @Override
    public boolean isSupported(TemporalField field) {
        return false;
    }

    @Override
    public long getLong(TemporalField field) {
        return 0;
    }
}
