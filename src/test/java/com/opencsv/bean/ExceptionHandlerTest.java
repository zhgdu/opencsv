package com.opencsv.bean;

import com.opencsv.TestUtils;
import com.opencsv.bean.exceptionhandler.ExceptionHandlerIgnore;
import com.opencsv.bean.exceptionhandler.ExceptionHandlerIgnoreThenThrowAfter;
import com.opencsv.bean.exceptionhandler.ExceptionHandlerQueueThenThrowAfter;
import com.opencsv.bean.mocks.AnnotatedMockBeanFull;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ExceptionHandlerTest {

    /**
     * Tests reading with an non-standard exception handler.
     * <p>Also incidentally tests:<ul>
     *     <li>The "ignore" exception handler</li>
     * </ul></p>
     * @throws IOException Never
     */
    @Test
    public void testReadWithExceptionHandler() throws IOException {
        CsvToBean<AnnotatedMockBeanFull> ctb = new CsvToBeanBuilder<AnnotatedMockBeanFull>(new FileReader("src/test/resources/testinputcase7.csv"))
                .withSeparator(';')
                .withType(AnnotatedMockBeanFull.class)
                .withExceptionHandler(new ExceptionHandlerIgnore())
                .build();
        List<AnnotatedMockBeanFull> beans = ctb.parse();
        assertNotNull(beans);
        assertTrue(beans.isEmpty());
        List<CsvException> exceptions = ctb.getCapturedExceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.isEmpty());
    }

    @Test
    public void testLambdaExceptionHandler() throws IOException {
        final String testString = "test";
        CsvToBean<AnnotatedMockBeanFull> ctb = new CsvToBeanBuilder<AnnotatedMockBeanFull>(new FileReader("src/test/resources/testinputcase7.csv"))
                .withSeparator(';')
                .withType(AnnotatedMockBeanFull.class)
                .withExceptionHandler(e -> {throw new CsvException(testString);})
                .build();
        try {
            ctb.parse();
            fail("CsvException should have been thrown.");
        }
        catch(RuntimeException re) {
            assertTrue(re.getCause() instanceof CsvException);
            CsvException csve = (CsvException)re.getCause();
            assertEquals(testString, csve.getMessage());
        }
    }

    @Test
    public void testReadWithQueueThenThrowHandler() throws IOException {
        BufferedReader inFile = Files.newBufferedReader(FileSystems.getDefault().getPath("src/test/resources/testinputcase85.csv"));
        String goodLine = inFile.readLine();
        String badLine = inFile.readLine();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 5; i++) {
            sb.append(badLine);
            sb.append('\n');
            for(int j = 0; j < 9; j++) {
                sb.append(goodLine);
                sb.append('\n');
            }
        }
        MappingStrategy<AnnotatedMockBeanFull> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(AnnotatedMockBeanFull.class);
        CsvToBean<AnnotatedMockBeanFull> ctb = new CsvToBeanBuilder<AnnotatedMockBeanFull>(new StringReader(sb.toString()))
                .withSeparator(';')
                .withMappingStrategy(strategy)
                .withExceptionHandler(new ExceptionHandlerQueueThenThrowAfter(3))
                .build();
        try {
            ctb.parse();
            fail("CsvException should have been thrown.");
        }
        catch(RuntimeException re) {
            assertTrue(re.getCause() instanceof CsvException);
            CsvException csve = (CsvException)re.getCause();
            assertEquals(1, csve.getLineNumber() % 10);
            List<CsvException> capturedExceptions = ctb.getCapturedExceptions();
            assertNotNull(capturedExceptions);
            assertFalse(capturedExceptions.isEmpty());
        }
    }

    /**
     * Tests writing with a non-standard exception handler.
     * <p>Also incidentally tests:<ul>
     *     <li>The "ignore then throw" exception handler.</li>
     * </ul></p>
     * @throws IOException Never
     * @throws CsvDataTypeMismatchException Never
     */
    @Test
    public void testWriteWithExceptionHandler() throws IOException, CsvDataTypeMismatchException {
        ImmutablePair<AnnotatedMockBeanFull, AnnotatedMockBeanFull> beans = TestUtils.createTwoGoodBeans();
        AnnotatedMockBeanFull goodBean = beans.left;
        AnnotatedMockBeanFull badBean = beans.right;
        badBean.setDateDefaultLocale(null); // required field
        StringWriter w = new StringWriter();
        MappingStrategy<AnnotatedMockBeanFull> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(AnnotatedMockBeanFull.class);
        StatefulBeanToCsv<AnnotatedMockBeanFull> b2csv = new StatefulBeanToCsvBuilder<AnnotatedMockBeanFull>(w)
                .withMappingStrategy(strategy) // so there is no header for assertFalse(w.toString().isEmpty())
                .withExceptionHandler(new ExceptionHandlerIgnoreThenThrowAfter(3))
                .build();
        List<AnnotatedMockBeanFull> inputBeans = new LinkedList<>();
        for(int i = 0; i < 5; i++) {
            inputBeans.add(badBean);
            for(int j = 0; j < 9; j++) {
                inputBeans.add(goodBean);
            }
        }
        try {
            b2csv.write(inputBeans);
            fail("CsvRequiredFieldEmptyException should have been thrown.");
        }
        catch(CsvRequiredFieldEmptyException csve) {
            // TODO: If we ever implement a separate thread for writing while
            //  beans are being converted to string, we should add a test:
            //  assertFalse(w.toString().isEmpty());
            assertEquals(1,  csve.getLineNumber() % 10);
            List<CsvException> capturedExceptions = b2csv.getCapturedExceptions();
            assertNotNull(capturedExceptions);
            assertTrue(capturedExceptions.isEmpty());
        }
    }

    @Test
    public void testQueueThenThrowExceptionHandler() throws IOException, CsvDataTypeMismatchException {
        ImmutablePair<AnnotatedMockBeanFull, AnnotatedMockBeanFull> beans = TestUtils.createTwoGoodBeans();
        AnnotatedMockBeanFull goodBean = beans.left;
        AnnotatedMockBeanFull badBean = beans.right;
        badBean.setDateDefaultLocale(null); // required field
        StringWriter w = new StringWriter();
        MappingStrategy<AnnotatedMockBeanFull> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(AnnotatedMockBeanFull.class);
        StatefulBeanToCsv<AnnotatedMockBeanFull> b2csv = new StatefulBeanToCsvBuilder<AnnotatedMockBeanFull>(w)
                .withMappingStrategy(strategy) // so there is no header for assertFalse(w.toString().isEmpty())
                .withExceptionHandler(new ExceptionHandlerQueueThenThrowAfter(3))
                .build();
        List<AnnotatedMockBeanFull> inputBeans = new LinkedList<>();
        for(int i = 0; i < 5; i++) {
            inputBeans.add(badBean);
            for(int j = 0; j < 9; j++) {
                inputBeans.add(goodBean);
            }
        }
        try {
            b2csv.write(inputBeans);
            fail("CsvRequiredFieldEmptyException should have been thrown.");
        }
        catch(CsvRequiredFieldEmptyException csve) {
            // TODO: If we ever implement a separate thread for writing while
            //  beans are being converted to strings, we should add a test:
            //  assertFalse(w.toString().isEmpty());
            assertEquals(1, csve.getLineNumber() % 10);
            List<CsvException> capturedExceptions = b2csv.getCapturedExceptions();
            assertNotNull(capturedExceptions);
            assertFalse(capturedExceptions.isEmpty());
        }
    }
}
