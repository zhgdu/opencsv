package com.opencsv.bean.validators;

import com.opencsv.bean.CsvBindByName;

public class ValidatorTestBean {

    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "^[0-9]{3,6}$")
    @CsvBindByName(column = "id")
    private int beanId;

    @PreAssignmentValidator(validator = MustStartWithACapitalLetter.class)
    @CsvBindByName(column = "name")
    private String beanName;

    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "^[A-Za-z ]*value: [0-9]{7,10}$")
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
