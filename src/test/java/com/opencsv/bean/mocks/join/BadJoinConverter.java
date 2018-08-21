package com.opencsv.bean.mocks.join;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.customconverter.BadCollectionConverter;
import org.apache.commons.collections4.MultiValuedMap;

public class BadJoinConverter {

    @CsvBindAndJoinByName(elementType = Object.class, converter = BadCollectionConverter.class)
    private MultiValuedMap<String, Object> objects;

    public MultiValuedMap<String, Object> getObjects() {
        return objects;
    }

    public void setObjects(MultiValuedMap<String, Object> objects) {
        this.objects = objects;
    }
}
