package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.customconverter.ConvertGermanToBoolean;
import org.apache.commons.collections4.MultiValuedMap;

import java.util.List;

public class FuzzyMock {

    @CsvBindByName(column = "integerHeader")
    private int intHeader;

    @CsvBindAndSplitByName(elementType = Integer.class, column = "splitIntegerHeader")
    private List<Integer> splitIntHeaders;

    @CsvBindAndJoinByName(elementType = Integer.class, column = "joinedIntegerHeader")
    private MultiValuedMap<String, Integer> joinedIntHeaders;

    @CsvCustomBindByName(converter = ConvertGermanToBoolean.class, column = "booleanHeader")
    private Boolean boolHeader;

    private int integerHeader;

    private int splitIntegerHeader; // Only named "split" for the sake of proving precedence of annotations

    private int joinedIntegerHeader; // Only named "joined" for the sake of proving precedence of annotations

    private boolean booleanHeader;

    private String exactMatch;

    private String wildlyInexactMatch;

    public int getIntHeader() {
        return intHeader;
    }

    public void setIntHeader(int intHeader) {
        this.intHeader = intHeader;
    }

    public List<Integer> getSplitIntHeaders() {
        return splitIntHeaders;
    }

    public void setSplitIntHeaders(List<Integer> splitIntHeaders) {
        this.splitIntHeaders = splitIntHeaders;
    }

    public MultiValuedMap<String, Integer> getJoinedIntHeaders() {
        return joinedIntHeaders;
    }

    public void setJoinedIntHeaders(MultiValuedMap<String, Integer> joinedIntHeaders) {
        this.joinedIntHeaders = joinedIntHeaders;
    }

    public Boolean getBoolHeader() {
        return boolHeader;
    }

    public void setBoolHeader(Boolean boolHeader) {
        this.boolHeader = boolHeader;
    }

    public int getIntegerHeader() {
        return integerHeader;
    }

    public void setIntegerHeader(int integerHeader) {
        this.integerHeader = integerHeader;
    }

    public int getSplitIntegerHeader() {
        return splitIntegerHeader;
    }

    public void setSplitIntegerHeader(int splitIntegerHeader) {
        this.splitIntegerHeader = splitIntegerHeader;
    }

    public int getJoinedIntegerHeader() {
        return joinedIntegerHeader;
    }

    public void setJoinedIntegerHeader(int joinedIntegerHeader) {
        this.joinedIntegerHeader = joinedIntegerHeader;
    }

    public boolean isBooleanHeader() {
        return booleanHeader;
    }

    public void setBooleanHeader(boolean booleanHeader) {
        this.booleanHeader = booleanHeader;
    }

    public String getExactMatch() {
        return exactMatch;
    }

    public void setExactMatch(String exactMatch) {
        this.exactMatch = exactMatch;
    }

    public String getWildlyInexactMatch() {
        return wildlyInexactMatch;
    }

    public void setWildlyInexactMatch(String wildlyInexactMatch) {
        this.wildlyInexactMatch = wildlyInexactMatch;
    }
}
