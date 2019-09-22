package com.opencsv.bean.processor;

import com.opencsv.bean.CsvBindByName;

public class ProcessorTestBean {
    @PreAssignmentProcessor(processor = ConvertEmptyOrBlankStringsToDefault.class, paramString = "-1")
    @CsvBindByName(column = "id")
    private int beanId;

    @PreAssignmentProcessor(processor = ConvertEmptyOrBlankStringsToNull.class)
    @CsvBindByName(column = "name")
    private String beanName;

    @PreAssignmentProcessor(processor = ConvertEmptyOrBlankStringsToDefault.class, paramString = "31415926")
    @CsvBindByName(column = "big number", capture = "^[A-Za-z ]*value: (.*)$", format = "value: %s")
    private long bigNumber;

    public int getBeanId() {
        return beanId;
    }

    public void setBeanId(int beanId) {
        this.beanId = beanId;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public long getBigNumber() {
        return bigNumber;
    }

    public void setBigNumber(long bigNumber) {
        this.bigNumber = bigNumber;
    }
}
