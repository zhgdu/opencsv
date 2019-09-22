package com.opencsv.bean.processor;

public class ConvertEmptyOrBlankStringsToNull implements StringProcessor {
    @Override
    public String processString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value;
    }

    @Override
    public void setParameterString(String value) {
        // not needed
    }
}
