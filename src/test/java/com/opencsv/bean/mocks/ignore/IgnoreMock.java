package com.opencsv.bean.mocks.ignore;

import com.opencsv.bean.*;
import org.apache.commons.collections4.MultiValuedMap;

import java.util.List;

public class IgnoreMock {

    @CsvBindByPosition(position = 0)
    @CsvBindByName
    private int bindingPrimitiveNotIgnored;

    @CsvBindAndSplitByPosition(position = 1, elementType = Integer.class)
    @CsvBindAndSplitByName(elementType = Integer.class)
    private List<Integer> bindingSplitNotIgnored;

    @CsvBindAndJoinByName(elementType = Integer.class)
    private MultiValuedMap<String, Integer> bindingJoinByNameNotIgnored;

    @CsvBindAndJoinByPosition(position = "2-3", elementType = Integer.class)
    private MultiValuedMap<Integer, Integer> bindingJoinByPositionNotIgnored;

    @CsvIgnore
    @CsvBindByPosition(position = 4)
    @CsvBindByName
    private int bindingPrimitiveIgnored;

    @CsvIgnore
    @CsvBindAndSplitByPosition(position = 5, elementType = Integer.class)
    @CsvBindAndSplitByName(elementType = Integer.class)
    private List<Integer> bindingSplitIgnored;

    @CsvIgnore
    @CsvBindAndJoinByName(elementType = Integer.class)
    private MultiValuedMap<String, Integer> bindingJoinByNameIgnored;

    @CsvIgnore
    @CsvBindAndJoinByPosition(position = "6-7", elementType = Integer.class)
    private MultiValuedMap<Integer, Integer> bindingJoinByPositionIgnored;

    public int getBindingPrimitiveNotIgnored() {
        return bindingPrimitiveNotIgnored;
    }

    public void setBindingPrimitiveNotIgnored(int bindingPrimitiveNotIgnored) {
        this.bindingPrimitiveNotIgnored = bindingPrimitiveNotIgnored;
    }

    public List<Integer> getBindingSplitNotIgnored() {
        return bindingSplitNotIgnored;
    }

    public void setBindingSplitNotIgnored(List<Integer> bindingSplitNotIgnored) {
        this.bindingSplitNotIgnored = bindingSplitNotIgnored;
    }

    public MultiValuedMap<String, Integer> getBindingJoinByNameNotIgnored() {
        return bindingJoinByNameNotIgnored;
    }

    public void setBindingJoinByNameNotIgnored(MultiValuedMap<String, Integer> bindingJoinByNameNotIgnored) {
        this.bindingJoinByNameNotIgnored = bindingJoinByNameNotIgnored;
    }

    public MultiValuedMap<Integer, Integer> getBindingJoinByPositionNotIgnored() {
        return bindingJoinByPositionNotIgnored;
    }

    public void setBindingJoinByPositionNotIgnored(MultiValuedMap<Integer, Integer> bindingJoinByPositionNotIgnored) {
        this.bindingJoinByPositionNotIgnored = bindingJoinByPositionNotIgnored;
    }

    public int getBindingPrimitiveIgnored() {
        return bindingPrimitiveIgnored;
    }

    public void setBindingPrimitiveIgnored(int bindingPrimitiveIgnored) {
        this.bindingPrimitiveIgnored = bindingPrimitiveIgnored;
    }

    public List<Integer> getBindingSplitIgnored() {
        return bindingSplitIgnored;
    }

    public void setBindingSplitIgnored(List<Integer> bindingSplitIgnored) {
        this.bindingSplitIgnored = bindingSplitIgnored;
    }

    public MultiValuedMap<String, Integer> getBindingJoinByNameIgnored() {
        return bindingJoinByNameIgnored;
    }

    public void setBindingJoinByNameIgnored(MultiValuedMap<String, Integer> bindingJoinByNameIgnored) {
        this.bindingJoinByNameIgnored = bindingJoinByNameIgnored;
    }

    public MultiValuedMap<Integer, Integer> getBindingJoinByPositionIgnored() {
        return bindingJoinByPositionIgnored;
    }

    public void setBindingJoinByPositionIgnored(MultiValuedMap<Integer, Integer> bindingJoinByPositionIgnored) {
        this.bindingJoinByPositionIgnored = bindingJoinByPositionIgnored;
    }
}
