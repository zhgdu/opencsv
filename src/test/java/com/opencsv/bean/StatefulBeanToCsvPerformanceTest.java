package com.opencsv.bean;

import com.opencsv.*;
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
    private static final int NUM_BEANS = 50000;
    private static Locale systemLocale;

    private RFC4180ParserBuilder rfc4180ParserBuilder = new RFC4180ParserBuilder();
    private RFC4180Parser rfc4180Parser;
    private CSVParserBuilder csvParserBuilder = new CSVParserBuilder();
    private CSVParser csvParser;
    private CSVWriterBuilder csvWriterBuilder;
    private ICSVWriter icsvWriter;

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
        int numBeans = NUM_BEANS;
        List<AnnotatedMockBeanFull> beanList = new ArrayList<>(numBeans);
        ImmutablePair<AnnotatedMockBeanFull, AnnotatedMockBeanFull> pair = createTwoGoodBeans();
        for (int i = 0; i < numBeans / 2; i++) {
            beanList.add(pair.left);
            beanList.add(pair.right);
        }

        StopWatch watch;
        Writer writer;
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat;

        csvParser = csvParserBuilder.build();
        rfc4180Parser = rfc4180ParserBuilder.build();

        csvWriterBuilder = new CSVWriterBuilder(new StringWriter());
        icsvWriter = csvWriterBuilder.withParser(csvParser).build();

        writeOrderedWithICSVWriter(100, beanList, icsvWriter, "throw away");

        csvWriterBuilder = new CSVWriterBuilder(new StringWriter());
        icsvWriter = csvWriterBuilder.withParser(csvParser).build();

        writeOrderedWithICSVWriter(numBeans, beanList, icsvWriter, "ordered with CSVParser");

        csvWriterBuilder = new CSVWriterBuilder(new StringWriter());
        icsvWriter = csvWriterBuilder.withParser(csvParser).build();

        writeUnorderedWithICSVWriter(numBeans, beanList, icsvWriter, "unordered with CSVParser");

        strat = new HeaderColumnNameMappingStrategy<>();
        strat.setType(AnnotatedMockBeanFull.class);

        csvWriterBuilder = new CSVWriterBuilder(new StringWriter());
        icsvWriter = csvWriterBuilder.withParser(rfc4180Parser).build();

        writeOrderedWithICSVWriter(numBeans, beanList, icsvWriter, "ordered with RFC4180Parser");

        csvWriterBuilder = new CSVWriterBuilder(new StringWriter());
        icsvWriter = csvWriterBuilder.withParser(rfc4180Parser).build();

        writeUnorderedWithICSVWriter(numBeans, beanList, icsvWriter, "unordered with RFC4180Parser");

        // Writing, ordered
        writer = new StringWriter();
        writeOrderedWithWriter(numBeans, beanList, writer, "ordered with Writer");

        // Writing, unordered
        writer = new StringWriter();
        writeUnorderedWithWriter(numBeans, beanList, writer, "unordered with Writer");

        strat = new HeaderColumnNameMappingStrategy<>();
        strat.setType(AnnotatedMockBeanFull.class);

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

    private void writeUnorderedWithWriter(int numBeans, List<AnnotatedMockBeanFull> beanList, Writer writer,
                                          String type) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat;
        StatefulBeanToCsv<AnnotatedMockBeanFull> btcsv;
        StopWatch watch;
        strat = new HeaderColumnNameMappingStrategy<>();
        strat.setType(AnnotatedMockBeanFull.class);
        btcsv = new StatefulBeanToCsvBuilder<AnnotatedMockBeanFull>(writer)
                .withMappingStrategy((MappingStrategy) strat)
                .withOrderedResults(false)
                .build();
        watch = StopWatch.createStarted();
        btcsv.write(beanList);
        watch.stop();
        System.out.println("Time taken to write " + numBeans + " beans, " + type + ": " + watch.toString());
    }

    private void writeOrderedWithWriter(int numBeans, List<AnnotatedMockBeanFull> beanList,
                                        Writer writer, String type) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat;
        StatefulBeanToCsv<AnnotatedMockBeanFull> btcsv;
        StopWatch watch;
        strat = new HeaderColumnNameMappingStrategy<>();
        strat.setType(AnnotatedMockBeanFull.class);
        btcsv = new StatefulBeanToCsvBuilder<AnnotatedMockBeanFull>(writer)
                .withMappingStrategy((MappingStrategy) strat).build();
        watch = StopWatch.createStarted();
        btcsv.write(beanList);
        watch.stop();
        System.out.println("Time taken to write " + numBeans + " beans, " + type + ": " + watch.toString());
    }

    private void writeUnorderedWithICSVWriter(int numBeans, List<AnnotatedMockBeanFull> beanList, ICSVWriter writer,
                                              String type) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        StatefulBeanToCsv<AnnotatedMockBeanFull> btcsv;
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat;

        StopWatch watch;
        strat = new HeaderColumnNameMappingStrategy<>();
        strat.setType(AnnotatedMockBeanFull.class);
        btcsv = new StatefulBeanToCsvBuilder<AnnotatedMockBeanFull>(writer)
                .withMappingStrategy((MappingStrategy) strat)
                .withOrderedResults(false)
                .build();
        watch = StopWatch.createStarted();
        btcsv.write(beanList);
        watch.stop();
        System.out.println("Time taken to write " + numBeans + " beans, " + type + ": " + watch.toString());
    }

    private void writeOrderedWithICSVWriter(int numBeans, List<AnnotatedMockBeanFull> beanList,
                                            ICSVWriter writer, String type) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat;
        StatefulBeanToCsv<AnnotatedMockBeanFull> btcsv;
        StopWatch watch;
        strat = new HeaderColumnNameMappingStrategy<>();
        strat.setType(AnnotatedMockBeanFull.class);
        btcsv = new StatefulBeanToCsvBuilder<AnnotatedMockBeanFull>(writer)
                .withMappingStrategy((MappingStrategy) strat).build();
        watch = StopWatch.createStarted();
        btcsv.write(beanList);
        watch.stop();
        System.out.println("Time taken to write " + numBeans + " beans, " + type + ": " + watch.toString());
    }
}
