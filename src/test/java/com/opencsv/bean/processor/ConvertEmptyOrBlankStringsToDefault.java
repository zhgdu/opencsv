package com.opencsv.bean.processor;

public class ConvertEmptyOrBlankStringsToDefault implements StringProcessor {
    String defaultValue;

    @Override
    public String processString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public void setParameterString(String value) {
        defaultValue = value;
    }
}
