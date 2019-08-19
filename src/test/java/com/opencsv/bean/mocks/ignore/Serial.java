package com.opencsv.bean.mocks.ignore;

import java.io.Serializable;

public class Serial implements Serializable {
    private static final long serialVersionUID = 1L;
    private int testInt;
    private String testString;

    public Serial(int testInt, String testString) {
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
