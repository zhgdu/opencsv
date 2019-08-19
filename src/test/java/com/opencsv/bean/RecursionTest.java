package com.opencsv.bean;

import com.opencsv.bean.mocks.recurse.*;
import com.opencsv.exceptions.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecursionTest {

    private static final String HEADER = "intLevelZero,stringLevelOne,charLevelTwo,floatLevelThree,booleanLevelThree,shortLevelThree\n";
    private static final String DATA = "10,11,c,4.0,true,32\n";

    private RecursionMockLevelZero oneGoodMock() {
        RecursionMockLevelZero bean = new RecursionMockLevelZero();
        bean.setIntLevelZero(10);
        RecursionMockLevelOne l1 = new RecursionMockLevelOne();
        l1.setStringLevelOne("11");
        bean.setLevelOne(l1);
        RecursionMockLevelTwo l2 = new RecursionMockLevelTwo();
        l2.setCharLevelTwo('c');
        l1.setLevelTwo(l2);
        l2.procureTheThirdLevelPointZero().setFloatLevelThree(4.0f);
        RecursionMockLevelThreePointOne l3point1 = new RecursionMockLevelThreePointOne();
        l3point1.setBooleanLevelThree(true);
        l2.setLevelThreePointOne(l3point1);
        RecursionMockLevelThreePointTwo l3point2 = new RecursionMockLevelThreePointTwo();
        l3point2.setShortLevelThree((short)32);
        l2.setLevelThreePointTwo(l3point2);
        return bean;
    }

    private void checkReadingResults(List<RecursionMockLevelZero> beans) {
        assertNotNull(beans);
        assertEquals(1, beans.size());

        RecursionMockLevelZero b0 = beans.get(0);
        assertEquals(10, b0.getIntLevelZero());

        RecursionMockLevelOne b1 = b0.getLevelOne();
        assertEquals("11", b1.getStringLevelOne());

        RecursionMockLevelTwo b2 = b1.getLevelTwo();
        assertEquals('c', b2.getCharLevelTwo());

        RecursionMockLevelThreePointZero b3point0 = b2.procureTheThirdLevelPointZero();
        assertEquals(4.0f, b3point0.getFloatLevelThree());

        RecursionMockLevelThreePointOne b3point1 = b2.getLevelThreePointOne();
        assertTrue(b3point1.isBooleanLevelThree());

        RecursionMockLevelThreePointTwo b3point2 = b2.getLevelThreePointTwo();
        assertEquals((short)32, b3point2.getShortLevelThree());
    }

    @Test
    public void testPrimitives() {

        // boolean
        try {
            new CsvToBeanBuilder<BooleanRecurse>(new StringReader(StringUtils.EMPTY))
                    .withType(BooleanRecurse.class)
                    .build();
            fail("Exception should have been thrown.");
        }
        catch(CsvRecursionException e) {
            assertFalse(StringUtils.isBlank(e.getMessage()));
            assertEquals(Boolean.TYPE, e.getOffendingType());
        }

        // byte
        try {
            new CsvToBeanBuilder<ByteRecurse>(new StringReader(StringUtils.EMPTY))
                    .withType(ByteRecurse.class)
                    .build();
            fail("Exception should have been thrown.");
        }
        catch(CsvRecursionException e) {
            assertFalse(StringUtils.isBlank(e.getMessage()));
            assertEquals(Byte.TYPE, e.getOffendingType());
        }

        // char
        try {
            new CsvToBeanBuilder<CharacterRecurse>(new StringReader(StringUtils.EMPTY))
                    .withType(CharacterRecurse.class)
                    .build();
            fail("Exception should have been thrown.");
        }
        catch(CsvRecursionException e) {
            assertFalse(StringUtils.isBlank(e.getMessage()));
            assertEquals(Character.TYPE, e.getOffendingType());
        }

        // double
        try {
            new CsvToBeanBuilder<DoubleRecurse>(new StringReader(StringUtils.EMPTY))
                    .withType(DoubleRecurse.class)
                    .build();
            fail("Exception should have been thrown.");
        }
        catch(CsvRecursionException e) {
            assertFalse(StringUtils.isBlank(e.getMessage()));
            assertEquals(Double.TYPE, e.getOffendingType());
        }

        // float
        try {
            new CsvToBeanBuilder<FloatRecurse>(new StringReader(StringUtils.EMPTY))
                    .withType(FloatRecurse.class)
                    .build();
            fail("Exception should have been thrown.");
        }
        catch(CsvRecursionException e) {
            assertFalse(StringUtils.isBlank(e.getMessage()));
            assertEquals(Float.TYPE, e.getOffendingType());
        }

        // int
        try {
            new CsvToBeanBuilder<IntegerRecurse>(new StringReader(StringUtils.EMPTY))
                    .withType(IntegerRecurse.class)
                    .build();
            fail("Exception should have been thrown.");
        }
        catch(CsvRecursionException e) {
            assertFalse(StringUtils.isBlank(e.getMessage()));
            assertEquals(Integer.TYPE, e.getOffendingType());
        }

        // long
        try {
            new CsvToBeanBuilder<LongRecurse>(new StringReader(StringUtils.EMPTY))
                    .withType(LongRecurse.class)
                    .build();
            fail("Exception should have been thrown.");
        }
        catch(CsvRecursionException e) {
            assertFalse(StringUtils.isBlank(e.getMessage()));
            assertEquals(Long.TYPE, e.getOffendingType());
        }

        // short
        try {
            new CsvToBeanBuilder<ShortRecurse>(new StringReader(StringUtils.EMPTY))
                    .withType(ShortRecurse.class)
                    .build();
            fail("Exception should have been thrown.");
        }
        catch(CsvRecursionException e) {
            assertFalse(StringUtils.isBlank(e.getMessage()));
            assertEquals(Short.TYPE, e.getOffendingType());
        }
    }

    @Test
    public void testDuplicateRecursion() {
        try {
            new CsvToBeanBuilder<DuplicateRecurse>(new StringReader(StringUtils.EMPTY))
                    .withType(DuplicateRecurse.class)
                    .build();
            fail("Exception should have been thrown.");
        }
        catch(CsvRecursionException e) {
            assertFalse(StringUtils.isBlank(e.getMessage()));
            assertEquals(RecursionMockLevelZero.class, e.getOffendingType());
        }
    }

    @Test
    public void testBindAndRecurse() {
        try {
            new CsvToBeanBuilder<BindAndRecurse>(new StringReader(StringUtils.EMPTY))
                    .withType(BindAndRecurse.class)
                    .build();
            fail("Exception should have been thrown.");
        }
        catch(CsvRecursionException e) {
            assertFalse(StringUtils.isBlank(e.getMessage()));
            assertEquals(RecursionMockLevelZero.class, e.getOffendingType());
        }
    }

    /**
     * Tests that reading data into beans with recursion in use and using the
     * header name mapping strategy functions as expected.
     * <p>Also incidentally tests:<ul>
     *     <li>Creation of subordinate beans</li>
     *     <li>Access to subordinate beans using standard accessor methods</li>
     *     <li>Access to subordinate beans using reflection</li>
     *     <li>Bean creates subordinate bean (enforced by no nullary constructor)</li>
     *     <li>Multiple levels of recursion</li>
     *     <li>Multiple recursion directives in one enclosing bean</li>
     * </ul></p>
     */
    @Test
    public void testReadingHeaderNames() {
        MappingStrategy<RecursionMockLevelZero> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(RecursionMockLevelZero.class);
        CsvToBean<RecursionMockLevelZero> csvToBean =
                new CsvToBeanBuilder<RecursionMockLevelZero>(new StringReader(
                        HEADER + DATA))
                        .withMappingStrategy(strategy)
                        .build();
        List<RecursionMockLevelZero> beans = csvToBean.parse();
        List<CsvException> exceptions = csvToBean.getCapturedExceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.isEmpty());
        checkReadingResults(beans);
    }

    @Test
    public void testReadingColumnPositions() {
        CsvToBean<RecursionMockLevelZero> csvToBean =
                new CsvToBeanBuilder<RecursionMockLevelZero>(new StringReader(DATA))
                        .withType(RecursionMockLevelZero.class)
                        .build();
        List<RecursionMockLevelZero> beans = csvToBean.parse();
        List<CsvException> exceptions = csvToBean.getCapturedExceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.isEmpty());
        checkReadingResults(beans);
    }

    @Test
    public void testEmbeddedBeanNoNullaryConstructor() {
        try {
            CsvToBean<EmbeddedBeanNoNullaryConstructor> csvToBean = new CsvToBeanBuilder<EmbeddedBeanNoNullaryConstructor>(new StringReader(DATA))
                    .withType(EmbeddedBeanNoNullaryConstructor.class)
                    .build();
            csvToBean.parse();
            fail("Exception should have been thrown.");
        } catch (RuntimeException e) {
            assertFalse(StringUtils.isBlank(e.getMessage()));
            assertTrue(e.getCause() instanceof CsvBeanIntrospectionException);
            CsvBeanIntrospectionException csve = (CsvBeanIntrospectionException)e.getCause();
            assertFalse(StringUtils.isBlank(csve.getMessage()));
            assertNull(csve.getField());
            assertNull(csve.getBean());
            assertNotNull(csve.getCause());
        }
    }

    @Test
    public void testWritingHeaderNames() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        StringWriter w = new StringWriter();
        MappingStrategy<RecursionMockLevelZero> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(RecursionMockLevelZero.class);
        StatefulBeanToCsv<RecursionMockLevelZero> b2c = new StatefulBeanToCsvBuilder<RecursionMockLevelZero>(w)
                .withMappingStrategy(strategy)
                .withApplyQuotesToAll(false)
                .build();
        b2c.write(oneGoodMock());
        assertEquals("BOOLEANLEVELTHREE,CHARLEVELTWO,FLOATLEVELTHREE,INTLEVELZERO,SHORTLEVELTHREE,STRINGLEVELONE\n" +
                "true,c,4.0,10,32,11\n", w.toString());
    }

    @Test
    public void testWritingColumnPositions() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        StringWriter w = new StringWriter();
        StatefulBeanToCsv<RecursionMockLevelZero> b2c = new StatefulBeanToCsvBuilder<RecursionMockLevelZero>(w)
                .withApplyQuotesToAll(false)
                .build();
        b2c.write(oneGoodMock());
        assertEquals(DATA, w.toString());
    }

    @Test
    public void testNullMemberVariableOptional() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        StringWriter w = new StringWriter();
        StatefulBeanToCsv<RecursionMockLevelZero> b2c = new StatefulBeanToCsvBuilder<RecursionMockLevelZero>(w)
                .withApplyQuotesToAll(false)
                .build();
        RecursionMockLevelZero bean = oneGoodMock();
        bean.getLevelOne().setLevelTwo(null);
        b2c.write(bean);
        assertEquals("10,11,,,,\n", w.toString());
    }

    @Test
    public void testNullMemberVariableRequired() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        StringWriter w = new StringWriter();
        StatefulBeanToCsv<RecursionMockLevelZero> b2c = new StatefulBeanToCsvBuilder<RecursionMockLevelZero>(w)
                .withApplyQuotesToAll(false)
                .build();
        RecursionMockLevelZero bean = oneGoodMock();
        bean.setLevelOne(null);
        try {
            b2c.write(bean);
            fail("Exception should have been thrown.");
        } catch (CsvRequiredFieldEmptyException e) {
            assertEquals(RecursionMockLevelOne.class, e.getBeanClass());
            assertNotNull(e.getDestinationFields());
            assertFalse(e.getDestinationFields().isEmpty());
            assertEquals("stringLevelOne", e.getDestinationFields().get(0).getName());
        }
    }
}
