package com.opencsv.bean.mocks;

import com.opencsv.bean.*;
import org.apache.commons.collections4.MultiValuedMap;

import java.util.Date;
import java.util.List;

public class WriteLocale {

    @CsvBindByPosition(
            position = 0,
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true)
    @CsvBindByName(
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true)
    private double primitivePlain;

    @CsvBindByPosition(
            position = 1,
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true)
    @CsvBindByName(
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true)
    @CsvNumber(
            value = "###.###,#",
            writeFormatEqualsReadFormat = false,
            writeFormat = "###\u00A0###,000")
    private double numberPlain;

    @CsvBindByPosition(
            position = 2,
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true)
    @CsvBindByName(
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true)
    @CsvDate("dd/MMM/yyyy")
    private Date datePlain;

    @CsvBindAndSplitByPosition(
            position = 3,
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true,
            elementType = Double.class)
    @CsvBindAndSplitByName(
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true,
            elementType = Double.class)
    private List<Double> primitiveSplit;

    @CsvBindAndSplitByPosition(
            position = 4,
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true,
            elementType = Double.class)
    @CsvBindAndSplitByName(
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true,
            elementType = Double.class)
    @CsvNumber(
            value = "###.###,#",
            writeFormatEqualsReadFormat = false,
            writeFormat = "###\u00A0###,000")
    private List<Double> numberSplit;

    @CsvBindAndSplitByPosition(
            position = 5,
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true,
            elementType = Date.class)
    @CsvBindAndSplitByName(
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            required = true,
            elementType = Date.class)
    @CsvDate("dd/MMM/yyyy")
    private List<Date> dateSplit;

    @CsvBindAndJoinByPosition(
            position = "6-7",
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            elementType = Double.class)
    private MultiValuedMap<Integer, Double> primitiveJoinPosition;

    @CsvBindAndJoinByPosition(
            position = "8-9",
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            elementType = Double.class)
    @CsvNumber(
            value = "###.###,#",
            writeFormatEqualsReadFormat = false,
            writeFormat = "###\u00A0###,000")
    private MultiValuedMap<Integer, Double> numberJoinPosition;

    @CsvBindAndJoinByPosition(
            position = "10-11",
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            elementType = Date.class)
    @CsvDate("dd/MMM/yyyy")
    private MultiValuedMap<Integer, Date> dateJoinPosition;

    @CsvBindAndJoinByName(
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            elementType = Double.class)
    private MultiValuedMap<String, Double> primitiveJoinName;

    @CsvBindAndJoinByName(
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            elementType = Double.class)
    @CsvNumber(
            value = "###.###,#",
            writeFormatEqualsReadFormat = false,
            writeFormat = "###\u00A0###,000")
    private MultiValuedMap<String, Double> numberJoinName;

    @CsvBindAndJoinByName(
            locale = "de",
            writeLocaleEqualsReadLocale = false,
            writeLocale = "fr",
            elementType = Date.class)
    @CsvDate("dd/MMM/yyyy")
    private MultiValuedMap<String, Date> dateJoinName;

    @CsvBindByPosition(
            position = 12,
            locale = "de",
            writeLocaleEqualsReadLocale = true,
            writeLocale = "fr",
            required = true)
    @CsvBindByName(
            locale = "de",
            writeLocaleEqualsReadLocale = true,
            writeLocale = "fr",
            required = true)
    private double redHerring;

    public double getPrimitivePlain() {
        return primitivePlain;
    }

    public void setPrimitivePlain(double primitivePlain) {
        this.primitivePlain = primitivePlain;
    }

    public double getNumberPlain() {
        return numberPlain;
    }

    public void setNumberPlain(double numberPlain) {
        this.numberPlain = numberPlain;
    }

    public Date getDatePlain() {
        return datePlain;
    }

    public void setDatePlain(Date datePlain) {
        this.datePlain = datePlain;
    }

    public List<Double> getPrimitiveSplit() {
        return primitiveSplit;
    }

    public void setPrimitiveSplit(List<Double> primitiveSplit) {
        this.primitiveSplit = primitiveSplit;
    }

    public List<Double> getNumberSplit() {
        return numberSplit;
    }

    public void setNumberSplit(List<Double> numberSplit) {
        this.numberSplit = numberSplit;
    }

    public List<Date> getDateSplit() {
        return dateSplit;
    }

    public void setDateSplit(List<Date> dateSplit) {
        this.dateSplit = dateSplit;
    }

    public MultiValuedMap<Integer, Double> getPrimitiveJoinPosition() {
        return primitiveJoinPosition;
    }

    public void setPrimitiveJoinPosition(MultiValuedMap<Integer, Double> primitiveJoinPosition) {
        this.primitiveJoinPosition = primitiveJoinPosition;
    }

    public MultiValuedMap<Integer, Double> getNumberJoinPosition() {
        return numberJoinPosition;
    }

    public void setNumberJoinPosition(MultiValuedMap<Integer, Double> numberJoinPosition) {
        this.numberJoinPosition = numberJoinPosition;
    }

    public MultiValuedMap<Integer, Date> getDateJoinPosition() {
        return dateJoinPosition;
    }

    public void setDateJoinPosition(MultiValuedMap<Integer, Date> dateJoinPosition) {
        this.dateJoinPosition = dateJoinPosition;
    }

    public MultiValuedMap<String, Double> getPrimitiveJoinName() {
        return primitiveJoinName;
    }

    public void setPrimitiveJoinName(MultiValuedMap<String, Double> primitiveJoinName) {
        this.primitiveJoinName = primitiveJoinName;
    }

    public MultiValuedMap<String, Double> getNumberJoinName() {
        return numberJoinName;
    }

    public void setNumberJoinName(MultiValuedMap<String, Double> numberJoinName) {
        this.numberJoinName = numberJoinName;
    }

    public MultiValuedMap<String, Date> getDateJoinName() {
        return dateJoinName;
    }

    public void setDateJoinName(MultiValuedMap<String, Date> dateJoinName) {
        this.dateJoinName = dateJoinName;
    }

    public double getRedHerring() {
        return redHerring;
    }

    public void setRedHerring(double redHerring) {
        this.redHerring = redHerring;
    }
}
