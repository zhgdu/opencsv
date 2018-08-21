package com.opencsv.bean.mocks.join;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.customconverter.BadCollectionConverter;

import java.util.List;

public class BadSplitConverter {

    @CsvBindAndSplitByName(elementType = Object.class, converter = BadCollectionConverter.class)
    private List<Object> objects;

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }
}
