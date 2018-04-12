package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvNumber;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberMockColumn {

    @CsvBindByPosition(position = 0)
    @CsvNumber("#.#")
    private BigDecimal bigDecimal;

    @CsvBindByPosition(position = 1)
    @CsvNumber("#")
    private BigInteger bigInteger;

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public BigInteger getBigInteger() {
        return bigInteger;
    }

    public void setBigInteger(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }
}
