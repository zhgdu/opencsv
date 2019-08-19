package com.opencsv.bean.mocks.ignore;

public class NonSerial {
    private static final long serialVersionUID = 1L;
    private int testInt;
    private String testString;

    public NonSerial(int testInt, String testString) {
        this.testInt = testInt;
        this.testString = testString;
    }

    public int getTestInt() {
        return testInt;
    }

    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }
}
