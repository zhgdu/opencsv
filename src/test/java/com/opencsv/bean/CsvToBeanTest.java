package com.opencsv.bean;

import com.opencsv.*;
import com.opencsv.bean.mocks.*;
import com.opencsv.bean.verifier.PositiveEvensOnly;
import com.opencsv.bean.verifier.PositiveOddsOnly;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class CsvToBeanTest {
    private static final String TEST_STRING = "name,orderNumber,num\n" +
            "kyle,abc123456,123\n" +
            "jimmy,def098765,456 ";

    private static final String TEST_STRING_WITHOUT_MANDATORY_FIELD = "name,orderNumber,num\n" +
            "kyle,abc123456,123\n" +
            "jimmy,def098765,";

    private static final String GOOD_NUMBERS = "number\n0\n1\n2\n3\n4\n";
    private static final String BAD_NUMBERS = "number\n0\n1\n2\n-5\n3\n4\n";

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

    private CSVReader createReader() {
        return createReader(TEST_STRING);
    }

    private CSVReader createReader(String testString) {
        StringReader reader = new StringReader(testString);
        return new CSVReader(reader);
    }

    @Test(expected = RuntimeException.class)
    public void throwRuntimeExceptionWhenExceptionIsThrown() {
        CsvToBean bean = new CsvToBean();
        bean.parse(new ErrorHeaderMappingStrategy(), createReader());
    }

    @Test(expected = RuntimeException.class)
    public void throwRuntimeExceptionLineWhenExceptionIsThrown() {
        CsvToBean bean = new CsvToBean();
        bean.parse(new ErrorLineMappingStrategy(), new StringReader(TEST_STRING), false); // Extra arguments for code coverage
    }

    @Test
    public void parseBeanWithNoAnnotations() {
        HeaderColumnNameMappingStrategy<MockBean> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(MockBean.class);
        CsvToBean<MockBean> bean = new CsvToBean<>();

        List<MockBean> beanList = bean.parse(strategy, new StringReader(TEST_STRING), null); // Extra arguments for code coverage
        assertEquals(2, beanList.size());
        assertTrue(beanList.contains(createMockBean("kyle", "abc123456", 123)));
        assertTrue(beanList.contains(createMockBean("jimmy", "def098765", 456)));
    }

    private MockBean createMockBean(String name, String orderNumber, int num) {
        MockBean mockBean = new MockBean();
        mockBean.setName(name);
        mockBean.setOrderNumber(orderNumber);
        mockBean.setNum(num);
        return mockBean;
    }

    @Test
    public void bug133ShouldNotThrowNullPointerExceptionWhenProcessingEmptyWithNoAnnotations() {
        HeaderColumnNameMappingStrategy<Bug133Bean> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(Bug133Bean.class);

        StringReader reader = new StringReader("one;two;three\n" +
                "kyle;;123\n" +
                "jimmy;;456 ");

        CSVParserBuilder parserBuilder = new CSVParserBuilder();
        CSVReaderBuilder readerBuilder = new CSVReaderBuilder(reader);

        CSVParser parser = parserBuilder.withFieldAsNull(CSVReaderNullFieldIndicator.BOTH).withSeparator(';').build();
        CSVReader csvReader = readerBuilder.withCSVParser(parser).build();

        CsvToBean<Bug133Bean> bean = new CsvToBean<>();

        List<Bug133Bean> beanList = bean.parse(strategy, csvReader, null, true); // Extra arguments for code coverage
        assertEquals(2, beanList.size());
    }

    @Test
    public void throwIllegalStateWhenParseWithoutArgumentsIsCalled() {
        CsvToBean csvtb = new CsvToBean();
        String englishErrorMessage = null;
        try {
            csvtb.parse();
            fail("IllegalStateException should have been thrown.");
        } catch (IllegalStateException e) {
            englishErrorMessage = e.getLocalizedMessage();
        }

        // Now with another locale
        csvtb.setErrorLocale(Locale.GERMAN);
        try {
            csvtb.parse();
            fail("IllegalStateException should have been thrown.");
        } catch (IllegalStateException e) {
            assertNotSame(englishErrorMessage, e.getLocalizedMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void throwIllegalStateWhenOnlyReaderIsSpecifiedToParseWithoutArguments() {
        CsvToBean csvtb = new CsvToBean();
        csvtb.setCsvReader(new CSVReader(new StringReader(TEST_STRING)));
        csvtb.parse();
    }

    @Test(expected = IllegalStateException.class)
    public void throwIllegalStateWhenOnlyMapperIsSpecifiedToParseWithoutArguments() {
        CsvToBean csvtb = new CsvToBean();
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat = new HeaderColumnNameMappingStrategy<>();
        strat.setType(AnnotatedMockBeanFull.class);
        csvtb.setMappingStrategy(strat);
        csvtb.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwIllegalStateWhenReaderNotProvidedInBuilder() {
        new CsvToBeanBuilder<AnnotatedMockBeanFull>((Reader) null)
                .withType(AnnotatedMockBeanFull.class)
                .build();
    }

    @Test
    public void throwIllegalStateWhenTypeAndMapperNotProvidedInBuilder() {
        String englishErrorMessage = null;
        try {
            new CsvToBeanBuilder<>(new StringReader(TEST_STRING_WITHOUT_MANDATORY_FIELD))
                    .build();
            fail("IllegalStateException should have been thrown.");
        } catch (IllegalStateException e) {
            englishErrorMessage = e.getLocalizedMessage();
        }

        // Now with a different locale
        try {
            new CsvToBeanBuilder<>(new StringReader(TEST_STRING_WITHOUT_MANDATORY_FIELD))
                    .withErrorLocale(Locale.GERMAN)
                    .build();
            fail("IllegalStateException should have been thrown.");
        } catch (IllegalStateException e) {
            assertNotSame(englishErrorMessage, e.getLocalizedMessage());
        }
    }

    @Test
    public void testMinimumBuilder() {
        List<MinimalCsvBindByPositionBeanForWriting> result =
                new CsvToBeanBuilder<MinimalCsvBindByPositionBeanForWriting>(new StringReader("1,2,3\n4,5,6"))
                        .withType(MinimalCsvBindByPositionBeanForWriting.class)
                        .build()
                        .parse();
        assertEquals(2, result.size());
    }

    @Test
    public void testMinimumBuilderWithCSVReader() {
        CSVReaderBuilder readerBuilder = new CSVReaderBuilder(new StringReader("1,2,3\n4,5,6"));
        List<MinimalCsvBindByPositionBeanForWriting> result =
                new CsvToBeanBuilder<MinimalCsvBindByPositionBeanForWriting>(readerBuilder.build())
                        .withType(MinimalCsvBindByPositionBeanForWriting.class)
                        .build()
                        .parse();
        assertEquals(2, result.size());
    }

    private class BegToBeFiltered implements CsvToBeanFilter {

        @Override
        public boolean allowLine(String[] line) {
            for (String col : line) {
                if (col.equals("filtermebaby")) return false;
            }
            return true;
        }

    }

    @Test
    public void testMaximumBuilder() throws FileNotFoundException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> map = new HeaderColumnNameMappingStrategy<>();
        map.setType(AnnotatedMockBeanFull.class);

        // Yeah, some of these are the default values, but I'm having trouble concocting
        // a CSV file screwy enough to meet the requirements posed by not using
        // defaults for everything.
        CsvToBean csvtb =
                new CsvToBeanBuilder<AnnotatedMockBeanFull>(new FileReader("src/test/resources/testinputmaximumbuilder.csv"))
                        .withEscapeChar('?')
                        .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER) //default
                        .withFilter(new BegToBeFiltered())
                        .withIgnoreLeadingWhiteSpace(false)
                        .withIgnoreQuotations(true)
                        .withKeepCarriageReturn(false) //default
                        .withMappingStrategy(map)
                        .withQuoteChar('!')
                        .withSeparator('#')
                        .withSkipLines(1)
                        .withStrictQuotes(false) // default
                        .withThrowExceptions(false)
                        .withType(AnnotatedMockBeanFull.class)
                        .withVerifyReader(false)
                        .withMultilineLimit(Integer.MAX_VALUE)
                        .build();
        List<CsvException> capturedExceptions = csvtb.getCapturedExceptions();
        assertNotNull(capturedExceptions);
        assertEquals(0, capturedExceptions.size());
        List<AnnotatedMockBeanFull> result = csvtb.parse();

        // Three lines, one filtered, one throws an exception
        assertEquals(1, result.size());
        assertEquals(1, csvtb.getCapturedExceptions().size());
        AnnotatedMockBeanFull bean = result.get(0);
        assertEquals("\ttest string of everything!", bean.getStringClass());
        assertTrue(bean.getBoolWrapped());
        assertFalse(bean.isBoolPrimitive());
        assertTrue(bean.getByteWrappedDefaultLocale() == 1);
        // Nothing else really matters
    }

    @Test
    public void testMaximumBuilderWithCSVReader() throws FileNotFoundException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> map = new HeaderColumnNameMappingStrategy<>();
        map.setType(AnnotatedMockBeanFull.class);

        CSVParserBuilder cpb = new CSVParserBuilder();
        CSVParser csvParser = cpb.withEscapeChar('?')
                .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
                .withIgnoreLeadingWhiteSpace(false)
                .withIgnoreQuotations(true)
                .withQuoteChar('!')
                .withSeparator('#')
                .withStrictQuotes(false)
                .build();

        CSVReaderBuilder crb = new CSVReaderBuilder(new FileReader("src/test/resources/testinputmaximumbuilder.csv"));
        CSVReader csvReader = crb.withCSVParser(csvParser)
                .withSkipLines(1)
                .withKeepCarriageReturn(false)
                .withVerifyReader(false)
                .withMultilineLimit(Integer.MAX_VALUE)
                .build();

        // Yeah, some of these are the default values, but I'm having trouble concocting
        // a CSV file screwy enough to meet the requirements posed by not using
        // defaults for everything.
        CsvToBean csvtb =
                new CsvToBeanBuilder<AnnotatedMockBeanFull>(csvReader)
                        .withFilter(new BegToBeFiltered())
                        .withMappingStrategy(map)
                        .withThrowExceptions(false)
                        .withType(AnnotatedMockBeanFull.class)
                        .build();
        List<CsvException> capturedExceptions = csvtb.getCapturedExceptions();
        assertNotNull(capturedExceptions);
        assertEquals(0, capturedExceptions.size());
        List<AnnotatedMockBeanFull> result = csvtb.parse();

        // Three lines, one filtered, one throws an exception
        assertEquals(1, result.size());
        assertEquals(1, csvtb.getCapturedExceptions().size());
        AnnotatedMockBeanFull bean = result.get(0);
        assertEquals("\ttest string of everything!", bean.getStringClass());
        assertTrue(bean.getBoolWrapped());
        assertFalse(bean.isBoolPrimitive());
        assertTrue(bean.getByteWrappedDefaultLocale() == 1);
        // Nothing else really matters
    }

    @Test
    public void testColumnMappingStrategyWithBuilder() throws FileNotFoundException {
        List<AnnotatedMockBeanFull> result =
                new CsvToBeanBuilder<AnnotatedMockBeanFull>(new FileReader("src/test/resources/testinputposfullgood.csv"))
                        .withSeparator(';')
                        .withType(AnnotatedMockBeanFull.class)
                        .build()
                        .parse();
        assertEquals(2, result.size());
    }

    @Test
    public void testMappingWithoutAnnotationsWithBuilder() {
        List<MockBean> result =
                new CsvToBeanBuilder<MockBean>(new StringReader(TEST_STRING))
                        .withType(MockBean.class)
                        .build()
                        .parse();
        assertEquals(2, result.size());
    }

    @Test
    public void testEmptyInputWithHeaderNameMappingAndRequiredField() {
        MappingStrategy<AnnotatedMockBeanFull> map = new HeaderColumnNameMappingStrategy<>();
        map.setType(AnnotatedMockBeanFull.class);
        try {
            new CsvToBeanBuilder<AnnotatedMockBeanFull>(new StringReader(StringUtils.EMPTY))
                    .withType(AnnotatedMockBeanFull.class)
                    .withMappingStrategy(map)
                    .build()
                    .parse();
            fail("An exception should have been thrown.");
        } catch (RuntimeException re) {
            Throwable t = re.getCause();
            assertNotNull(t);
            assertTrue(t instanceof CsvRequiredFieldEmptyException);
        }
    }

    @Test
    public void testMismatchNumberOfData() {
        MappingStrategy<AnnotatedMockBeanFull> map = new HeaderColumnNameMappingStrategy<>();
        map.setType(AnnotatedMockBeanFull.class);
        StringBuilder sb = new StringBuilder(ICSVParser.INITIAL_READ_SIZE);
        String dateString = "19780115T063209";
        sb.append("BYTE1,BYTE2,BYTE3,DATE1\n");
        sb.append("1,2,3," + dateString + "\n");
        sb.append("4,5,6," + dateString + "\n");
        sb.append("7\n");
        sb.append("8,9,10," + dateString + "\n");
        try {
            new CsvToBeanBuilder<AnnotatedMockBeanFull>(new StringReader(sb.toString()))
                    .withType(AnnotatedMockBeanFull.class)
                    .withMappingStrategy(map)
                    .build()
                    .parse();
            fail("An exception should have been thrown.");
        } catch (RuntimeException re) {
            Throwable t = re.getCause();
            assertNotNull(t);
            assertTrue(t instanceof CsvRequiredFieldEmptyException);
        }
    }

    @Test
    public void bug154WhenUsingIteratorTheLineNumbersInTheExceptionShouldBePopulated() {
        String data = "a,b\n" +
                "P,1\n" +
                "Q,12\n" +
                "R,1a\n" +
                "S,1b";
        CsvToBean<Bug154Bean> c = new CsvToBeanBuilder<Bug154Bean>(new StringReader(data)).withType(Bug154Bean.class).withThrowExceptions(false).withErrorLocale(Locale.ROOT).build();
        for (Bug154Bean mockBean : c) {
            System.out.println(mockBean.toString());
        }
        assertEquals(2, c.getCapturedExceptions().size());
        CsvException exception1 = c.getCapturedExceptions().get(0);
        assertEquals(4, exception1.getLineNumber());
        assertNotNull(exception1.getLine());
        CsvException exception2 = c.getCapturedExceptions().get(1);
        assertEquals(5, exception2.getLineNumber());
        assertNotNull(exception2.getLine());
    }

    /**
     * Tests use of a single bean verifier.
     * <p>Also incidentally tests:</p>
     * <ul><li>Adding {@code null} as a verifier before adding other
     * verifiers.</li>
     * <li>A verifier that discards beans.</li>
     * <li>A verifier that verifies beans.</li></ul>
     */
    @Test
    public void testSingleVerifier() {
        List<SingleNumber> beans = new CsvToBeanBuilder<SingleNumber>(new StringReader(GOOD_NUMBERS))
                .withType(SingleNumber.class)
                .withVerifier(null)
                .withVerifier(new PositiveEvensOnly())
                .withOrderedResults(true)
                .build().parse();
        assertNotNull(beans);
        assertEquals(3, beans.size());
        assertEquals(0, beans.get(0).getNumber());
        assertEquals(2, beans.get(1).getNumber());
        assertEquals(4, beans.get(2).getNumber());
    }

    /**
     * Tests that multiple verifiers work together.
     * <p>Also incidentally tests:</p>
     * <ul><li>Adding {@code null} as a verifier after other verifiers.</li></ul>
     */
    @Test
    public void testMultipleVerifiers() {
        List<SingleNumber> beans = new CsvToBeanBuilder<SingleNumber>(new StringReader(GOOD_NUMBERS))
                .withType(SingleNumber.class)
                .withVerifier(new PositiveEvensOnly())
                .withVerifier(new PositiveOddsOnly())
                .withVerifier(null)
                .build().parse();
        assertNotNull(beans);
        assertTrue(beans.isEmpty());
    }

    @Test
    public void testNullVerifierClearsList() {
        CsvToBean<SingleNumber> csvToBean = new CsvToBeanBuilder<SingleNumber>(new StringReader(GOOD_NUMBERS))
                .withType(SingleNumber.class)
                .withVerifier(new PositiveEvensOnly())
                .withVerifier(new PositiveOddsOnly())
                .withOrderedResults(true)
                .build();
        csvToBean.setVerifiers(null);
        List<SingleNumber> beans = csvToBean.parse();
        assertNotNull(beans);
        assertEquals(5, beans.size());
        assertEquals(0, beans.get(0).getNumber());
        assertEquals(1, beans.get(1).getNumber());
        assertEquals(2, beans.get(2).getNumber());
        assertEquals(3, beans.get(3).getNumber());
        assertEquals(4, beans.get(4).getNumber());
    }

    @Test
    public void testVerifierThrowsExceptionCollected() {
        CsvToBean<SingleNumber> csvToBean = new CsvToBeanBuilder<SingleNumber>(new StringReader(BAD_NUMBERS))
                .withType(SingleNumber.class)
                .withVerifier(new PositiveOddsOnly())
                .withThrowExceptions(false)
                .withOrderedResults(true)
                .build();
        List<SingleNumber> beans = csvToBean.parse();
        List<CsvException> exceptions = csvToBean.getCapturedExceptions();
        assertNotNull(beans);
        assertEquals(2, beans.size());
        assertEquals(1, beans.get(0).getNumber());
        assertEquals(3, beans.get(1).getNumber());
        assertNotNull(exceptions);
        assertEquals(1, exceptions.size());
        assertTrue(exceptions.get(0) instanceof CsvConstraintViolationException);
        CsvConstraintViolationException csve = (CsvConstraintViolationException) exceptions.get(0);
        assertEquals(5, csve.getLineNumber());
        assertNotNull(csve.getLine());
    }

    @Test
    public void testVerifierThrowsExceptionRethrown() {
        try {
            new CsvToBeanBuilder<SingleNumber>(new StringReader(BAD_NUMBERS))
                    .withType(SingleNumber.class)
                    .withVerifier(new PositiveOddsOnly())
                    .withThrowExceptions(false)
                    .withOrderedResults(true)
                    .build().parse();
        } catch (RuntimeException re) {
            Throwable e = re.getCause();
            assertTrue(e instanceof CsvConstraintViolationException);
            CsvConstraintViolationException csve = (CsvConstraintViolationException) e;
            assertEquals(4, csve.getLineNumber());
            assertNotNull(csve.getLine());
        }
    }

    @Test
    public void testBug194() {
        String testString = "name,id,orderNumber\n" +
                "1,\"foo\", 3\n" +
                "1,\"a \" string with a quote in the middle\", 3\n" +
                "1,\"bar\", 3";

        String expectedString = "Error parsing CSV line: 3, values: a \" string with a quote in the middle, 3\n" +
                "1,bar, 3\n";
        StringReader stringReader = new StringReader(testString);
        MappingStrategy<MockBean> map = new HeaderColumnNameMappingStrategy<>();

        CsvToBean<MockBean> csvToBean = new CsvToBeanBuilder(stringReader)
                .withType(MockBean.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withQuoteChar('"')
                .withSeparator(',')
                .withThrowExceptions(false)
                .build();

        try {
            List<MockBean> rows = csvToBean.parse();
        } catch (Exception e) {
            assertEquals(expectedString, e.getMessage());
        }
    }
}
