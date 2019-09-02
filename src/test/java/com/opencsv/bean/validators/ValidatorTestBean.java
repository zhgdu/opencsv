package com.opencsv.bean.validators;

import com.opencsv.bean.CsvBindByName;

public class ValidatorTestBean {

    @CsvBindByName(column = "id")
    private int beanId;

    @PreAssignmentValidator(validator = MustStartWithACapitalLetter.class)
    @CsvBindByName(column = "name")
    private String beanName;

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
}
