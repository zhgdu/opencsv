package com.opencsv.bean.mocks.join;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindAndJoinByPosition;

import java.util.Map;

public class NoCaptureGroup {

    @CsvBindAndJoinByName(column = "name", capture = "(?:.*)", elementType = String.class)
    private Map<String, String> nameByName;

    @CsvBindAndJoinByPosition(position = "0", capture = ".*", elementType = String.class)
    private Map<Integer, String> nameByPosition;

    public Map<String, String> getNameByName() {
        return nameByName;
    }

    public void setNameByName(Map<String, String> nameByName) {
        this.nameByName = nameByName;
    }

    public Map<Integer, String> getNameByPosition() {
        return nameByPosition;
    }

    public void setNameByPosition(Map<Integer, String> nameByPosition) {
        this.nameByPosition = nameByPosition;
    }
}
