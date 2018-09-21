package com.opencsv.bean.mocks.split;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindAndSplitByPosition;

import java.util.List;

public class InvalidCapture {

    @CsvBindAndSplitByName(column = "name", capture = "*", elementType = String.class)
    @CsvBindAndSplitByPosition(position = 0, capture = "*", elementType = String.class)
    private List<String> name;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }
}
