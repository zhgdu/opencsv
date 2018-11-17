/*
 * Copyright 2017 Andrew Rucker Jones.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opencsv.bean;

import com.opencsv.bean.customconverter.BadCollectionConverter;
import com.opencsv.bean.mocks.split.*;
import com.opencsv.bean.mocks.join.BadSplitConverter;
import com.opencsv.bean.mocks.join.ErrorCode;
import com.opencsv.bean.mocks.join.IdAndErrorSplitByName;
import com.opencsv.bean.mocks.join.IdAndErrorSplitByPosition;
import com.opencsv.exceptions.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.TreeBag;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Andrew Rucker Jones
 */
public class CollectionSplitTest {

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

    /**
     * Tests a collection of a wrapped primitive type.
     * <p>Also incidentally tests:
     * <ul><li>Capture by name without a matching regular expression</li>
     * <li>Correct use of {@link java.util.Collection}</li></ul></p>
     *
     * @throws IOException Never
     */
    @Test
    public void testGoodCollectionPrimitive() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        // Representative of a primitive type
        Collection<Integer> collectionType = bean.getCollectionType();
        assertTrue(collectionType instanceof ArrayList);
        assertEquals(6, collectionType.size());
        assertEquals("[2, 2, 1, 3, 3, 3]", collectionType.toString());
    }
    
    @Test
    public void testGoodCollectionDate() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        // Representative of a date type
        Set<Date> setType = bean.getSetType();
        assertTrue(setType instanceof HashSet);
        assertEquals(2, setType.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Date d : setType) {
            String formattedDate = sdf.format(d);
            assertTrue("1978-01-15".equals(formattedDate) || "2018-01-01".equals(formattedDate));
        }
    }

    /**
     * Tests correct use of {@link java.util.List}.
     * <p>Also incidentally tests:
     * <ul><li>Capture by name with a matching regular expression</li></ul></p>
     *
     * @throws IOException Never
     */
    @Test
    public void testGoodCollectionTypeList() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        List<Integer> listType = bean.getListType();
        assertTrue(listType instanceof LinkedList);
        assertEquals(6, listType.size());
        assertEquals("[2, 2, 1, 3, 3, 3]", listType.toString());
    }
    
    @Test
    public void testGoodCollectionTypeSet() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        Set<Date> setType = bean.getSetType();
        assertTrue(setType instanceof HashSet);
        assertEquals(2, setType.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Date d : setType) {
            String formattedDate = sdf.format(d);
            assertTrue("1978-01-15".equals(formattedDate) || "2018-01-01".equals(formattedDate));
        }
    }
    
    @Test
    public void testGoodCollectionTypeSortedSet() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        SortedSet<? extends Number> sortedSetType = bean.getSortedSetType();
        assertTrue(sortedSetType instanceof TreeSet);
        assertEquals(3, sortedSetType.size());
        assertEquals("[1, 2, 3]", sortedSetType.toString());
    }
    
    @Test
    public void testGoodCollectionTypeNavigableSet() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        NavigableSet<Integer> navigableSetType = bean.getNavigableSetType();
        assertTrue(navigableSetType instanceof TreeSet);
        assertEquals(3, navigableSetType.size());
        assertEquals("[1, 2, 3]", navigableSetType.toString());
    }
    
    @Test
    public void testGoodCollectionTypeQueue() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        Queue<Integer> queueType = bean.getQueueType();
        assertTrue(queueType instanceof ArrayDeque);
        assertEquals(6, queueType.size());
        assertEquals("[2, 2, 1, 3, 3, 3]", queueType.toString());
    }
    
    @Test
    public void testGoodCollectionTypeDeque() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        Deque<Integer> dequeType = bean.getDequeType();
        assertTrue(dequeType instanceof ArrayDeque);
        assertEquals(6, dequeType.size());
        assertEquals("[2, 2, 1, 3, 3, 3]", dequeType.toString());
    }
    
    @Test
    public void testGoodCollectionTypeBag() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        Bag<Integer> bagType = bean.getBagType();
        assertTrue(bagType instanceof HashBag);
        assertEquals(6, bagType.size());
        assertEquals("[1:1,2:2,3:3]", bagType.toString());
    }
    
    @Test
    public void testGoodCollectionTypeSortedBag() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        SortedBag<Integer> sortedBagType = bean.getSortedBagType();
        assertTrue(sortedBagType instanceof TreeBag);
        assertEquals(6, sortedBagType.size());
        assertEquals("[1:1,2:2,3:3]", sortedBagType.toString());
    }
    
    @Test
    public void testGoodCollectionTypeNamedParametrized() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        Stack<Integer> stackType = bean.getStackType();
        assertTrue(stackType instanceof Stack);
        assertEquals(6, stackType.size());
        assertEquals("[2, 2, 1, 3, 3, 3]", stackType.toString());
    }
    
    @Test
    public void testGoodCollectionTypeNamedUnparametrized() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        IntegerSetSortedToString nonparameterizedCollectionType = bean.getNonparameterizedCollectionType();
        assertTrue(nonparameterizedCollectionType instanceof IntegerSetSortedToString);
        assertEquals(3, nonparameterizedCollectionType.size());
        assertEquals("[1,2,3]", nonparameterizedCollectionType.toString());
    }
    
    @Test
    public void testGoodCollectionHeaderMapping() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        // Representative of anything, demonstrating correct mapping
        Collection<Integer> collectionType = bean.getCollectionType();
        assertTrue(collectionType instanceof ArrayList);
        assertEquals(6, collectionType.size());
        assertEquals("[2, 2, 1, 3, 3, 3]", collectionType.toString());
    }

    /**
     * Tests a good split using column mapping.
     * <p>Also incidentally tests:
     * <ul><li>Capture by position without a matching regular expression</li>
     * <li>Capture by position with a matching regular expression</li></ul></p>
     */
    @Test
    public void testGoodCollectionColumnMapping() {
        List<AnnotatedMockBeanCollectionSplitByColumn> beanList =
                new CsvToBeanBuilder<AnnotatedMockBeanCollectionSplitByColumn>(new StringReader("A string is great,f:1.0 f:2.0 f:3.0"))
                        .withType(AnnotatedMockBeanCollectionSplitByColumn.class)
                        .build().parse();
        assertEquals(1, beanList.size());
        AnnotatedMockBeanCollectionSplitByColumn bean = beanList.get(0);
        assertEquals("[A, string, is, great]", bean.getStringList().toString());
        assertEquals("[1.0, 2.0, 3.0]", bean.getFloatList().toString());
    }
    
    @Test
    public void testPrecedenceCustomAndCollectionConverter() {
        List<AnnotationPrecedenceWithCollections> beanList =
                new CsvToBeanBuilder<AnnotationPrecedenceWithCollections>(
                        new StringReader(
                                "precedenceGoesToCustom,precedenceGoesToCollection\nThis is a string,2 2 1 3 3 3"))
                        .withType(AnnotationPrecedenceWithCollections.class).build().parse();
        assertEquals(1, beanList.size());
        AnnotationPrecedenceWithCollections bean = beanList.get(0);
        
        // Custom converter always comes first
        List<String> precedenceGoesToCustom = bean.getPrecedenceGoesToCustom();
        assertEquals("[This, is, a, string]", precedenceGoesToCustom.toString());
    }
    
    @Test
    public void testPrecedenceCollectionAndStandardConverter() {
        List<AnnotationPrecedenceWithCollections> beanList =
                new CsvToBeanBuilder<AnnotationPrecedenceWithCollections>(
                        new StringReader(
                                "precedenceGoesToCustom,precedenceGoesToCollection\nThis is a string,2 2 1 3 3 3"))
                        .withType(AnnotationPrecedenceWithCollections.class).build().parse();
        assertEquals(1, beanList.size());
        AnnotationPrecedenceWithCollections bean = beanList.get(0);
        
        // Collections rank higher than standard conversion
        List<Integer> precedenceGoesToCollection = bean.getPrecedenceGoesToCollection();
        assertEquals("[2, 2, 1, 3, 3, 3]", precedenceGoesToCollection.toString());
    }
    
    @Test
    public void testUnknownElementType() {
        final String input = "$45";
        CsvToBean<UnknownElementType> csv2b = new CsvToBeanBuilder<UnknownElementType>(new StringReader(input))
                .withType(UnknownElementType.class)
                .withThrowExceptions(false)
                .build();
        csv2b.parse();
        List<CsvException> exceptionList = csv2b.getCapturedExceptions();
        assertEquals(1, exceptionList.size());
        CsvException csve = exceptionList.get(0);
        assertTrue(csve instanceof CsvDataTypeMismatchException);
        CsvDataTypeMismatchException dtme = (CsvDataTypeMismatchException) csve;
        assertEquals(1, dtme.getLineNumber());
        assertNotNull(dtme.getLine());
        assertEquals(Currency.class, dtme.getDestinationClass());
        assertEquals(input, dtme.getSourceObject());
    }
    
    @Test
    public void testNonCollectionBeanMember() {
        try {
            new CsvToBeanBuilder<NonCollectionBeanMember>(new StringReader("1 2 3"))
                    .withType(NonCollectionBeanMember.class)
                    .build().parse();
            fail("Should have thrown exception.");
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BeanFieldSplit.class, csve.getConverterClass());
        }
    }
    
    @Test
    public void testWrongCollectionTypeBeanMember() {
        try {
            new CsvToBeanBuilder<WrongCollectionType>(new StringReader("l\n2 2 1 3 3 3"))
                    .withType(WrongCollectionType.class)
                    .build().parse();
            fail("Exception should have been thrown.");
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BeanFieldSplit.class, csve.getConverterClass());
        }
    }
    
    @Test
    public void testWrongElementTypeBeanMember() {
        CsvToBean<WrongElementType> csv2b = new CsvToBeanBuilder<WrongElementType>(new StringReader("l\n2 2 1 3 3 3"))
                .withType(WrongElementType.class)
                .withThrowExceptions(false).build();
        List<WrongElementType> beanList = csv2b.parse();
        
        // This is Java Generics. Type erasure allows this assignment, because
        // at runtime, everything is List<Object>. It's just when accessing the
        // elements of the collection later that things blow up if you try to
        // access them as the type they're supposed to be instead of the type
        // they are.
        assertEquals(1, beanList.size());
        List<CsvException> exceptionList = csv2b.getCapturedExceptions();
        assertTrue(exceptionList.isEmpty());
    }
    
    @Test
    public void testInterfaceAsCollectionTypeInAnnotation() {
        List<InterfaceAsCollectionType> beanList = new CsvToBeanBuilder<InterfaceAsCollectionType>(new StringReader("1 2 3 4"))
                .withType(InterfaceAsCollectionType.class)
                .build().parse();
        assertEquals(1, beanList.size());
        InterfaceAsCollectionType bean = beanList.get(0);
        assertEquals("[1, 2, 3, 4]", bean.getS().toString());
    }
    
    @Test
    public void testEmptySplitOn() {
        try {
            new CsvToBeanBuilder<InvalidRegexAsSplitOn>(new StringReader("1a2b3c4"))
                    .withType(InvalidRegexAsSplitOn.class)
                    .build().parse();
            fail("Should have thrown exception.");
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BeanFieldSplit.class, csve.getConverterClass());
        }
    }
    
    @Test
    public void testInvalidRegexAsSplitOn() {
        try {
            new CsvToBeanBuilder<InvalidRegexAsSplitOn>(new StringReader("1a2b3c4"))
                    .withType(InvalidRegexAsSplitOn.class)
                    .build().parse();
            fail("Should have thrown exception.");
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BeanFieldSplit.class, csve.getConverterClass());
        }
    }
    
    @Test
    public void testUnknownCollectionType() {
        try {
            new CsvToBeanBuilder<UnknownCollectionType>(new StringReader("2 2 1 3 3 3"))
                    .withType(UnknownCollectionType.class).build().parse();
            fail("Exception should have been thrown.");
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BeanFieldSplit.class, csve.getConverterClass());
        }
    }

    /**
     * Tests writing with a header name mapping strategy.
     * <p>Also incidentally tests:
     * <ul><li>Writing with a format string and a header name mapping strategy</li></ul></p>
     *
     * @throws CsvDataTypeMismatchException Never
     * @throws CsvRequiredFieldEmptyException Never
     */
    @Test
    public void testWriteHeaderNameStrategy() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        StringWriter writer = new StringWriter();
        StatefulBeanToCsv<AnnotatedMockBeanCollectionSplit> b2csv =
                new StatefulBeanToCsvBuilder<AnnotatedMockBeanCollectionSplit>(writer).build();
        AnnotatedMockBeanCollectionSplit bean = new AnnotatedMockBeanCollectionSplit();
        SortedSet<Integer> sortedSet = new TreeSet<>();
        sortedSet.addAll(Arrays.asList(2, 2, 1, 3, 3, 3));
        bean.setSortedSetType(sortedSet);
        Set<Date> set = new HashSet<>();
        Calendar cal = new GregorianCalendar(1978, 0, 15);
        set.add(cal.getTime());
        cal = new GregorianCalendar(2018, 0, 1);
        set.add(cal.getTime());
        bean.setSetType(set);
        b2csv.write(bean);
        String s = writer.toString();
        
        // Unfortunately, the set the dates are read into is not sorted, so
        // depending on I don't know what (probably the JDK version), the order
        // can change.
        String option1 = "\"COLLECTIONTYPE\",\"DEQUETYPE\",\"LISTTYPE\",\"NAVIGABLESETTYPE\",\"QUEUETYPE\",\"SETTYPE\",\"SORTEDSETTYPE\"\n" +
                "\"\",\"\",\"\",\"\",\"\",\"1978-01-15 2018-01-01\",\"a1j a2j a3j\"\n";
        String option2 = "\"COLLECTIONTYPE\",\"DEQUETYPE\",\"LISTTYPE\",\"NAVIGABLESETTYPE\",\"QUEUETYPE\",\"SETTYPE\",\"SORTEDSETTYPE\"\n" +
                "\"\",\"\",\"\",\"\",\"\",\"2018-01-01 1978-01-15\",\"a1j a2j a3j\"\n";
        assertTrue(option1.equals(s) || option2.equals(s));
    }
    
    /**
     * Tests writing with a delimiter.
     * <p>Also incidentally tests:
     * <ul><li>writing with {@link ColumnPositionMappingStrategy}</li>
     * <li>Writing with a format string and {@link ColumnPositionMappingStrategy}</li>
     * <li>Writing with an empty list</li>
     * <li>Writing with a list full of {@code null}</li>
     * <li>Writing with a format string and empty input</li></ul></p>
     * 
     * @throws CsvDataTypeMismatchException Never
     * @throws CsvRequiredFieldEmptyException Never
     */
    @Test
    public void testWriteWithWriteDelimiter() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        StringWriter writer = new StringWriter();
        StatefulBeanToCsv<AnnotatedMockBeanCollectionSplitByColumn> b2csv =
                new StatefulBeanToCsvBuilder<AnnotatedMockBeanCollectionSplitByColumn>(writer).build();
        AnnotatedMockBeanCollectionSplitByColumn bean = new AnnotatedMockBeanCollectionSplitByColumn();
        Queue<Float> floatList = new ArrayDeque<>(Arrays.asList(1.0f, 2.0f, 3.0f));
        bean.setFloatList(floatList);
        bean.setStringList(Arrays.asList("This", "string", "dumb"));
        b2csv.write(bean);
        floatList = new LinkedList<>(Arrays.asList(4.0f, 5.0f, 6.0f));
        bean.setFloatList(floatList);
        bean.setStringList(Arrays.<String>asList(null, null, null));
        b2csv.write(bean);
        floatList = new ArrayDeque<>(Arrays.asList(7.0f, 8.0f, 9.0f));
        bean.setFloatList(floatList);
        bean.setStringList(null);
        b2csv.write(bean);
        assertEquals(
                "\"g:This,g:string,g:dumb\",\"1.0 silly delimiter 2.0 silly delimiter 3.0\"\n"
                + "\",,\",\"4.0 silly delimiter 5.0 silly delimiter 6.0\"\n"
                + "\"\",\"7.0 silly delimiter 8.0 silly delimiter 9.0\"\n",
                writer.toString());
    }
    
    @Test
    public void testWithSplitOn() throws IOException {
        List<DerivedMockBeanCollectionSplit> beanList = new CsvToBeanBuilder<DerivedMockBeanCollectionSplit>(new FileReader("src/test/resources/testgoodcollections.csv"))
                .withType(DerivedMockBeanCollectionSplit.class).build().parse();
        assertEquals(1, beanList.size());
        DerivedMockBeanCollectionSplit bean = beanList.get(0);
        
        Stack<Integer> stackType = bean.getStackType();
        assertEquals(6, stackType.size());
        assertEquals("[2, 2, 1, 3, 3, 3]", stackType.toString());
    }
    
    @Test
    public void testRequiredNotPresentOnRead() {
        CsvToBean<AnnotatedMockBeanCollectionSplitByColumn> csv2b = new CsvToBeanBuilder<AnnotatedMockBeanCollectionSplitByColumn>(new StringReader("A string is great"))
                        .withType(AnnotatedMockBeanCollectionSplitByColumn.class)
                        .withThrowExceptions(false).build();
        List<AnnotatedMockBeanCollectionSplitByColumn> beanList = csv2b.parse();
        List<CsvException> exceptionList = csv2b.getCapturedExceptions();
        assertTrue(beanList.isEmpty());
        assertEquals(1, exceptionList.size());
        CsvException csve = exceptionList.get(0);
        assertTrue(csve instanceof CsvRequiredFieldEmptyException);
        CsvRequiredFieldEmptyException rfee = (CsvRequiredFieldEmptyException) csve;
        assertEquals(AnnotatedMockBeanCollectionSplitByColumn.class, rfee.getBeanClass());
        assertEquals(1, rfee.getLineNumber());
        assertNotNull(rfee.getLine());
    }
    
    @Test
    public void testRequiredNotPresentOnWrite() throws CsvDataTypeMismatchException {
        AnnotatedMockBeanCollectionSplitByColumn bean = new AnnotatedMockBeanCollectionSplitByColumn();
        bean.setFloatList(new ArrayDeque<Float>());
        bean.setStringList(Collections.singletonList("Test"));
        StringWriter writer = new StringWriter();
        StatefulBeanToCsv b2csv = new StatefulBeanToCsvBuilder(writer).build();
        try {
            b2csv.write(bean);
            fail("Exception should have been thrown.");
        }
        catch(CsvRequiredFieldEmptyException csve) {
            assertEquals(AnnotatedMockBeanCollectionSplitByColumn.class, csve.getBeanClass());
            assertEquals("floatList", csve.getDestinationField().getName());
            assertEquals(1, csve.getLineNumber());
        }
    }

    @Test
    public void testCustomConverterByNameRead() throws IOException {
        ResourceBundle res = ResourceBundle.getBundle("collectionconverter", Locale.GERMAN);
        List<IdAndErrorSplitByName> beanList = new CsvToBeanBuilder<IdAndErrorSplitByName>(new FileReader("src/test/resources/testinputsplitcustombyname.csv"))
                .withType(IdAndErrorSplitByName.class).build().parse();
        assertEquals(2, beanList.size());

        // Bean one
        IdAndErrorSplitByName bean = beanList.get(0);
        assertEquals(1, bean.getId());
        List<ErrorCode> errorCodes = bean.getEc();
        assertEquals(3, errorCodes.size());
        ErrorCode ec = errorCodes.get(0);
        assertEquals(10, ec.errorCode);
        assertEquals(res.getString("default.error"), ec.errorMessage);
        ec = errorCodes.get(1);
        assertEquals(11, ec.errorCode);
        assertEquals("doesnt.exist", ec.errorMessage);
        ec = errorCodes.get(2);
        assertEquals(12, ec.errorCode);
        assertEquals(res.getString("default.error"), ec.errorMessage);

        // Bean two
        bean = beanList.get(1);
        assertEquals(2, bean.getId());
        errorCodes = bean.getEc();
        assertEquals(3, errorCodes.size());
        ec = errorCodes.get(0);
        assertEquals(20, ec.errorCode);
        assertEquals("doesnt.exist", ec.errorMessage);
        ec = errorCodes.get(1);
        assertEquals(21, ec.errorCode);
        assertEquals(res.getString("default.error"), ec.errorMessage);
        ec = errorCodes.get(2);
        assertEquals(22, ec.errorCode);
        assertEquals("doesnt.exist", ec.errorMessage);
    }

    @Test
    public void testCustomConverterByPositionRead() throws IOException {
        ResourceBundle res = ResourceBundle.getBundle("collectionconverter");
        List<IdAndErrorSplitByPosition> beanList = new CsvToBeanBuilder<IdAndErrorSplitByPosition>(new FileReader("src/test/resources/testinputsplitcustombyposition.csv"))
                .withType(IdAndErrorSplitByPosition.class).build().parse();
        assertEquals(2, beanList.size());

        // Bean one
        IdAndErrorSplitByPosition bean = beanList.get(0);
        assertEquals(1, bean.getId());
        List<ErrorCode> errorCodes = bean.getEc();
        assertEquals(3, errorCodes.size());
        ErrorCode ec = errorCodes.get(0);
        assertEquals(10, ec.errorCode);
        assertEquals(res.getString("default.error"), ec.errorMessage);
        ec = errorCodes.get(1);
        assertEquals(11, ec.errorCode);
        assertEquals("doesnt.exist", ec.errorMessage);
        ec = errorCodes.get(2);
        assertEquals(12, ec.errorCode);
        assertEquals(res.getString("default.error"), ec.errorMessage);

        // Bean two
        bean = beanList.get(1);
        assertEquals(2, bean.getId());
        errorCodes = bean.getEc();
        assertEquals(3, errorCodes.size());
        ec = errorCodes.get(0);
        assertEquals(20, ec.errorCode);
        assertEquals("doesnt.exist", ec.errorMessage);
        ec = errorCodes.get(1);
        assertEquals(21, ec.errorCode);
        assertEquals(res.getString("default.error"), ec.errorMessage);
        ec = errorCodes.get(2);
        assertEquals(22, ec.errorCode);
        assertEquals("doesnt.exist", ec.errorMessage);
    }

    @Test
    public void testCustomConverterByNameWrite() throws IOException, CsvException {
        List<IdAndErrorSplitByName> beanList = new CsvToBeanBuilder<IdAndErrorSplitByName>(new FileReader("src/test/resources/testinputsplitcustombyname.csv"))
                .withType(IdAndErrorSplitByName.class).build().parse();
        StringWriter writer = new StringWriter();
        new StatefulBeanToCsvBuilder<IdAndErrorSplitByName>(writer).build().write(beanList);
        assertEquals("\"EC\",\"ID\"\n\"10default.error 11default.error 12default.error\",\"1\"\n\"20default.error 21default.error 22default.error\",\"2\"\n", writer.toString());
    }

    @Test
    public void testCustomConverterByPositionWrite() throws IOException, CsvException {
        List<IdAndErrorSplitByPosition> beanList = new CsvToBeanBuilder<IdAndErrorSplitByPosition>(new FileReader("src/test/resources/testinputsplitcustombyposition.csv"))
                .withType(IdAndErrorSplitByPosition.class).build().parse();
        StringWriter writer = new StringWriter();
        new StatefulBeanToCsvBuilder<IdAndErrorSplitByPosition>(writer).build().write(beanList);
        assertEquals("\"1\",\"10default.error 11default.error 12default.error\"\n\"2\",\"20default.error 21default.error 22default.error\"\n", writer.toString());
    }

    @Test
    public void testBadCustomConverter() throws IOException {
        try {
            // Input doesn't matter. The test doesn't get that far.
            new CsvToBeanBuilder<BadSplitConverter>(new FileReader("src/test/resources/testinputsplitcustombyname.csv"))
                    .withType(BadSplitConverter.class)
                    .build().parse();
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BadCollectionConverter.class, csve.getConverterClass());
        }
    }

    @Test
    public void testCaptureByNameInvalidRegex() {
        try {
            MappingStrategy<InvalidCapture> strat = new HeaderColumnNameMappingStrategy<>();
            strat.setType(InvalidCapture.class);
            fail("Exception should have been thrown.");
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BeanFieldSplit.class, csve.getConverterClass());
            assertNotNull(csve.getCause());
        }
    }

    @Test
    public void testCaptureByPositionInvalidRegex() {
        try {
            MappingStrategy<InvalidCapture> strat = new ColumnPositionMappingStrategy<>();
            strat.setType(InvalidCapture.class);
            fail("Exception should have been thrown.");
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BeanFieldSplit.class, csve.getConverterClass());
            assertNotNull(csve.getCause());
        }
    }

    @Test
    public void testCaptureByNameRegexWithoutCaptureGroup() {
        try {
            MappingStrategy<NoCaptureGroup> strat = new HeaderColumnNameMappingStrategy<>();
            strat.setType(NoCaptureGroup.class);
            fail("Exception should have been thrown.");
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BeanFieldSplit.class, csve.getConverterClass());
            assertNull(csve.getCause());
        }
    }

    @Test
    public void testCaptureByPositionRegexWithoutCaptureGroup() {
        try {
            MappingStrategy<NoCaptureGroup> strat = new ColumnPositionMappingStrategy<>();
            strat.setType(NoCaptureGroup.class);
            fail("Exception should have been thrown.");
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BeanFieldSplit.class, csve.getConverterClass());
            assertNull(csve.getCause());
        }
    }

    @Test
    public void testFormatByNameWriteInvalidFormatString() {
        try {
            MappingStrategy<InvalidFormatString> strat = new HeaderColumnNameMappingStrategy<>();
            strat.setType(InvalidFormatString.class);
            fail("Exception should have been thrown.");
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BeanFieldSplit.class, csve.getConverterClass());
            assertNotNull(csve.getCause());
        }
    }

    @Test
    public void testFormatByPositionWriteInvalidFormatString() {
        try {
            MappingStrategy<InvalidFormatString> strat = new ColumnPositionMappingStrategy<>();
            strat.setType(InvalidFormatString.class);
            fail("Exception should have been thrown.");
        }
        catch(CsvBadConverterException csve) {
            assertEquals(BeanFieldSplit.class, csve.getConverterClass());
            assertNotNull(csve.getCause());
        }
    }
}
