package com.opencsv.bean.verifier;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.mocks.SingleNumber;
import com.opencsv.exceptions.CsvConstraintViolationException;

public class PositiveEvensOnly implements BeanVerifier<SingleNumber> {
    @Override
    public boolean verifyBean(SingleNumber bean) throws CsvConstraintViolationException {
        if(bean.getNumber() < 0) {
            throw new CsvConstraintViolationException("Negative number encountered.");
        }
        return bean.getNumber() % 2 == 0;
    }
}
