package com.opencsv.bean;

import com.opencsv.bean.mocks.AnnotatedMockBeanFull;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class StatefulBeanToCsvPerformanceTest {
    private static Locale systemLocale;

    @BeforeClass
    public static void storeSystemLocale() {
        systemLocale = Locale.getDefault();
    }

    @Before
    public void setSystemLocaleToValueNotGerman() {
        Locale.setDefault(Locale.US);
    }

    @After
    public void setSystemLocaleBackToDefault() {
        Locale.setDefault(systemLocale);
    }

    private ImmutablePair<AnnotatedMockBeanFull, AnnotatedMockBeanFull> createTwoGoodBeans()
            throws IOException {
        List<AnnotatedMockBeanFull> beans = new CsvToBeanBuilder(
                new FileReader("src/test/resources/testinputwriteposfullgood.csv"))
                .withType(AnnotatedMockBeanFull.class).withSeparator(';').build().parse();
        return new ImmutablePair<>(beans.get(0), beans.get(1));
    }

    @Test
    public void testPerformance()
            throws IOException, CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException {
        System.out.println("The following are performance data. Please keep an eye on them as you develop.");
        int numBeans = 10000;
        List<AnnotatedMockBeanFull> beanList = new ArrayList<>(numBeans);
        ImmutablePair<AnnotatedMockBeanFull, AnnotatedMockBeanFull> pair = createTwoGoodBeans();
        for (int i = 0; i < numBeans / 2; i++) {
            beanList.add(pair.left);
            beanList.add(pair.right);
        }

        // Writing, ordered
        Writer writer = new StringWriter();
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat = new HeaderColumnNameMappingStrategy<>();
        strat.setType(AnnotatedMockBeanFull.class);
        StatefulBeanToCsv<AnnotatedMockBeanFull> btcsv = new StatefulBeanToCsvBuilder<AnnotatedMockBeanFull>(writer)
                .withMappingStrategy((MappingStrategy) strat).build();
        StopWatch watch = StopWatch.createStarted();
        btcsv.write(beanList);
        watch.stop();
        System.out.println("Time taken to write " + numBeans + " beans, ordered: " + watch.toString());

        // Writing, unordered
        writer = new StringWriter();
        strat = new HeaderColumnNameMappingStrategy<>();
        strat.setType(AnnotatedMockBeanFull.class);
        btcsv = new StatefulBeanToCsvBuilder<AnnotatedMockBeanFull>(writer)
                .withMappingStrategy((MappingStrategy) strat)
                .withOrderedResults(false)
                .build();
        watch = StopWatch.createStarted();
        btcsv.write(beanList);
        watch.stop();
        System.out.println("Time taken to write " + numBeans + " beans, unordered: " + watch.toString());

        // Reading, ordered
        Reader reader = new StringReader(writer.toString());
        CsvToBean<AnnotatedMockBeanFull> csvtb = new CsvToBeanBuilder<AnnotatedMockBeanFull>(reader)
                .withType(AnnotatedMockBeanFull.class)
                .withMappingStrategy((MappingStrategy) strat).build();
        watch = StopWatch.createStarted();
        List<AnnotatedMockBeanFull> beans = csvtb.parse();
        watch.stop();
        assertEquals(numBeans, beans.size());
        System.out.println("Time taken to read " + numBeans + " beans, ordered: " + watch.toString());

        // Reading, ordered
        reader = new StringReader(writer.toString());
        csvtb = new CsvToBeanBuilder<AnnotatedMockBeanFull>(reader)
                .withType(AnnotatedMockBeanFull.class)
                .withOrderedResults(false)
                .withMappingStrategy((MappingStrategy) strat).build();
        watch = StopWatch.createStarted();
        beans = csvtb.parse();
        watch.stop();
        assertEquals(numBeans, beans.size());
        System.out.println("Time taken to read " + numBeans + " beans, unordered: " + watch.toString());
    }

}
