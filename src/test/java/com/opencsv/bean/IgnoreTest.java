package com.opencsv.bean;

import com.opencsv.bean.mocks.ignore.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IgnoreTest {

    /**
     * Tests that the field "serialVersionUID" <em>is</em> written if the bean
     * does not implement {@link java.io.Serializable}.
     * <p>Also incidentally tests that beans that opencsv cannot instantiate
     * can still be written. Yeah, I actually broke that once.</p>
     * @throws CsvException Never
     */
    @Test
    public void testSerialVersionUIDNonSerializable() throws CsvException {
        NonSerial bean = new NonSerial(2, "3");
        StringWriter w = new StringWriter();
        StatefulBeanToCsv<NonSerial> btcsv = new StatefulBeanToCsvBuilder<NonSerial>(w)
                .withApplyQuotesToAll(false)
                .build();
        btcsv.write(bean);
        assertEquals("SERIALVERSIONUID,TESTINT,TESTSTRING\n1,2,3\n", w.toString());
    }

    @Test
    public void testSerialVersionUIDSerializable() throws CsvException {
        Serial bean = new Serial(3, "4");
        StringWriter w = new StringWriter();
        StatefulBeanToCsv<Serial> btcsv = new StatefulBeanToCsvBuilder<Serial>(w)
                .withApplyQuotesToAll(false)
                .build();
        btcsv.write(bean);
        assertEquals("TESTINT,TESTSTRING\n3,4\n", w.toString());
    }

    @Test
    public void testInvalidInputForIgnore() throws NoSuchFieldException {
        // Data we need for the tests
        MultiValuedMap<Class<?>, Field> nullClass = new ArrayListValuedHashMap<>();
        nullClass.put(null, IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored"));
        MultiValuedMap<Class<?>, Field> nullField = new ArrayListValuedHashMap<>();
        nullField.put(IgnoreMock.class, null);
        MultiValuedMap<Class<?>, Field> classFieldMismatched = new ArrayListValuedHashMap<>();
        classFieldMismatched.put(
                NonSerial.class,
                IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored"));

        // Test with all currently known mapping strategies
        for(MappingStrategy<IgnoreMock> strategy : Arrays.asList(
                new HeaderColumnNameMappingStrategy<IgnoreMock>(),
                new ColumnPositionMappingStrategy<IgnoreMock>(),
                new HeaderColumnNameTranslateMappingStrategy<IgnoreMock>(),
                new FuzzyMappingStrategy<IgnoreMock>())) {
            strategy.setType(IgnoreMock.class);

            // Null class
            IllegalArgumentException e = assertThrows(
                    IllegalArgumentException.class,
                    () -> strategy.ignoreFields(nullClass));
            assertNotNull(e.getMessage());

            // Null field
            e = assertThrows(
                    IllegalArgumentException.class,
                    () -> strategy.ignoreFields(nullField));
            assertNotNull(e.getMessage());

            // Field does not match class
            e = assertThrows(
                    IllegalArgumentException.class,
                    () -> strategy.ignoreFields(classFieldMismatched));
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testInvalidInputToBuilderForIgnoreOnReading() {
        CsvToBeanBuilder<IgnoreMock> builder = new CsvToBeanBuilder<>(new StringReader(StringUtils.EMPTY));

        // Null class
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> builder.withIgnoreField(null, IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored")));
        assertNotNull(e.getMessage());

        // Null field
        e = assertThrows(
                IllegalArgumentException.class,
                () -> builder.withIgnoreField(IgnoreMock.class, null));
        assertNotNull(e.getMessage());

        // Field does not match class
        e = assertThrows(
                IllegalArgumentException.class,
                () -> builder.withIgnoreField(
                        NonSerial.class,
                        IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored")));
        assertNotNull(e.getMessage());
    }

    @Test
    public void testInvalidInputToBuilderForIgnoreOnWriting() {
        StringWriter w = new StringWriter();
        StatefulBeanToCsvBuilder<IgnoreMock> builder = new StatefulBeanToCsvBuilder<>(w);

        // Null class
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> builder.withIgnoreField(null, IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored")));
        assertNotNull(e.getMessage());

        // Null field
        e = assertThrows(
                IllegalArgumentException.class,
                () -> builder.withIgnoreField(IgnoreMock.class, null));
        assertNotNull(e.getMessage());

        // Field does not match class
        e = assertThrows(
                IllegalArgumentException.class,
                () -> builder.withIgnoreField(
                        NonSerial.class,
                        IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored")));
        assertNotNull(e.getMessage());
    }

    /**
     * Tests ignoring fields using {@link CsvIgnore} and a header name mapping
     * strategy on reading.
     * <p>Also incidentally tests:<ul>
     *     <li>Ignoring with {@link CsvIgnore} combined with all forms of
     *     header binding annotations</li>
     * </ul></p>
     */
    @Test
    public void testIgnoreWithAnnotationAndHeaderNameMappingOnReading() {
        final MappingStrategy<IgnoreMock> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(IgnoreMock.class);
        final String input = "bindingPrimitiveNotIgnored,bindingSplitNotIgnored,bindingJoinByNameNotIgnored,bindingJoinByNameNotIgnored,bindingPrimitiveIgnored,bindingSplitIgnored,bindingJoinByNameIgnored,bindingJoinByNameIgnored\n" +
                "1,2 3,4,5,6,7 8,9,10\n";
        final CsvToBean<IgnoreMock> csvToBean = new CsvToBeanBuilder<IgnoreMock>(new StringReader(input))
                .withMappingStrategy(strategy)
                .build();
        List<IgnoreMock> beans = csvToBean.parse();
        List<CsvException> exceptions = csvToBean.getCapturedExceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.isEmpty());
        assertNotNull(beans);
        assertEquals(1, beans.size());

        // Test field values
        IgnoreMock bean = beans.get(0);
        assertEquals(1, bean.getBindingPrimitiveNotIgnored());
        assertEquals(Arrays.asList(2, 3), bean.getBindingSplitNotIgnored());
        assertEquals(Arrays.asList(4, 5), new ArrayList<>(bean.getBindingJoinByNameNotIgnored().values()));
        assertEquals(0, bean.getBindingPrimitiveIgnored());
        assertNull(bean.getBindingSplitIgnored());
        assertNull(bean.getBindingJoinByNameIgnored());
    }

    /**
     * Tests ignoring fields using {@link CsvIgnore} and a column position
     * mapping strategy on reading.
     * <p>Also incidentally tests:<ul>
     *     <li>Ignoring with {@link CsvIgnore} combined with all forms of
     *     position binding annotations</li>
     * </ul></p>
     */
    @Test
    public void testIgnoreWithAnnotationAndColumnPositionMappingOnReading() {
        final String input = "1,2 3,4,5,6,7 8,9,10\n";
        final CsvToBean<IgnoreMock> csvToBean = new CsvToBeanBuilder<IgnoreMock>(new StringReader(input))
                .withType(IgnoreMock.class)
                .build();
        List<IgnoreMock> beans = csvToBean.parse();
        List<CsvException> exceptions = csvToBean.getCapturedExceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.isEmpty());
        assertNotNull(beans);
        assertEquals(1, beans.size());

        // Test field values
        IgnoreMock bean = beans.get(0);
        assertEquals(1, bean.getBindingPrimitiveNotIgnored());
        assertEquals(Arrays.asList(2, 3), bean.getBindingSplitNotIgnored());
        assertEquals(Arrays.asList(4, 5), new ArrayList<>(bean.getBindingJoinByPositionNotIgnored().values()));
        assertEquals(0, bean.getBindingPrimitiveIgnored());
        assertNull(bean.getBindingSplitIgnored());
        assertNull(bean.getBindingJoinByPositionIgnored());
    }

    /**
     * Ignores a field using
     * {@link CsvToBeanBuilder#withIgnoreField(Class, Field)} and a header name
     * mapping strategy on reading.
     * <p>Also incidentally tests:<ul>
     *     <li>Ignoring the same field with {@link CsvIgnore} and
     *     {@link CsvToBeanBuilder#withIgnoreField(Class, Field)}</li>
     * </ul></p>
     * @throws NoSuchFieldException Never
     */
    @Test
    public void testIgnoreWithMethodAndHeaderNameMappingOnReading() throws NoSuchFieldException {
        final MappingStrategy<IgnoreMock> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(IgnoreMock.class);
        final String input = "bindingPrimitiveNotIgnored,bindingSplitNotIgnored,bindingJoinByNameNotIgnored,bindingJoinByNameNotIgnored,bindingPrimitiveIgnored,bindingSplitIgnored,bindingJoinByNameIgnored,bindingJoinByNameIgnored\n" +
                "1,2 3,4,5,6,7 8,9,10\n";
        final CsvToBean<IgnoreMock> csvToBean = new CsvToBeanBuilder<IgnoreMock>(new StringReader(input))
                .withMappingStrategy(strategy)
                .withIgnoreField(IgnoreMock.class, IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored"))
                .withIgnoreField(IgnoreMock.class, IgnoreMock.class.getDeclaredField("bindingPrimitiveIgnored"))
                .build();
        List<IgnoreMock> beans = csvToBean.parse();
        List<CsvException> exceptions = csvToBean.getCapturedExceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.isEmpty());
        assertNotNull(beans);
        assertEquals(1, beans.size());

        // Test field values
        IgnoreMock bean = beans.get(0);
        assertEquals(0, bean.getBindingPrimitiveNotIgnored());
        assertEquals(Arrays.asList(2, 3), bean.getBindingSplitNotIgnored());
        assertEquals(Arrays.asList(4, 5), new ArrayList<>(bean.getBindingJoinByNameNotIgnored().values()));
        assertEquals(0, bean.getBindingPrimitiveIgnored());
        assertNull(bean.getBindingSplitIgnored());
        assertNull(bean.getBindingJoinByNameIgnored());
    }

    /**
     * Ignores a field using
     * {@link CsvToBeanBuilder#withIgnoreField(Class, Field)} and a column
     * position mapping strategy on reading.
     * <p>Also incidentally tests:<ul>
     *     <li>Ignoring the same field twice</li>
     * </ul></p>
     * @throws NoSuchFieldException Never
     */
    @Test
    public void testIgnoreWithMethodAndColumnPositionMappingOnReading() throws NoSuchFieldException {
        final String input = "1,2 3,4,5,6,7 8,9,10\n";
        final CsvToBean<IgnoreMock> csvToBean = new CsvToBeanBuilder<IgnoreMock>(new StringReader(input))
                .withType(IgnoreMock.class)
                .withIgnoreField(IgnoreMock.class, IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored"))
                .withIgnoreField(IgnoreMock.class, IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored"))
                .build();
        List<IgnoreMock> beans = csvToBean.parse();
        List<CsvException> exceptions = csvToBean.getCapturedExceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.isEmpty());
        assertNotNull(beans);
        assertEquals(1, beans.size());

        // Test field values
        IgnoreMock bean = beans.get(0);
        assertEquals(0, bean.getBindingPrimitiveNotIgnored());
        assertEquals(Arrays.asList(2, 3), bean.getBindingSplitNotIgnored());
        assertEquals(Arrays.asList(4, 5), new ArrayList<>(bean.getBindingJoinByPositionNotIgnored().values()));
        assertEquals(0, bean.getBindingPrimitiveIgnored());
        assertNull(bean.getBindingSplitIgnored());
        assertNull(bean.getBindingJoinByPositionIgnored());
    }

    /**
     * Ignores fields on reading with all known header name bindings.
     * <p>Also incidentally tests:<ul>
     *     <li>Calling {@link MappingStrategy#setType(Class)} after calling
     *     {@link MappingStrategy#ignoreFields(MultiValuedMap)}</li>
     * </ul></p>
     * @throws NoSuchFieldException Never
     */
    @Test
    public void testIgnoreAllWithHeaderNameMappingOnReading() throws NoSuchFieldException {
        final MappingStrategy<IgnoreMock> strategy = new HeaderColumnNameMappingStrategy<>();
        ListValuedMap<Class<?>, Field> ignoredFields = new ArrayListValuedHashMap<>();
        ignoredFields.put(IgnoreMock.class, IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored"));
        ignoredFields.put(IgnoreMock.class, IgnoreMock.class.getDeclaredField("bindingSplitNotIgnored"));
        ignoredFields.put(IgnoreMock.class, IgnoreMock.class.getDeclaredField("bindingJoinByNameNotIgnored"));
        strategy.ignoreFields(ignoredFields);
        strategy.setType(IgnoreMock.class);
        final String input = "bindingPrimitiveNotIgnored,bindingSplitNotIgnored,bindingJoinByNameNotIgnored,bindingJoinByNameNotIgnored,bindingPrimitiveIgnored,bindingSplitIgnored,bindingJoinByNameIgnored,bindingJoinByNameIgnored\n" +
                "1,2 3,4,5,6,7 8,9,10\n";
        final CsvToBean<IgnoreMock> csvToBean = new CsvToBeanBuilder<IgnoreMock>(new StringReader(input))
                .withMappingStrategy(strategy)
                .build();
        List<IgnoreMock> beans = csvToBean.parse();
        List<CsvException> exceptions = csvToBean.getCapturedExceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.isEmpty());
        assertNotNull(beans);
        assertEquals(1, beans.size());

        // Test field values
        IgnoreMock bean = beans.get(0);
        assertEquals(0, bean.getBindingPrimitiveNotIgnored());
        assertNull(bean.getBindingSplitNotIgnored());
        assertNull(bean.getBindingJoinByNameNotIgnored());
        assertEquals(0, bean.getBindingPrimitiveIgnored());
        assertNull(bean.getBindingSplitIgnored());
        assertNull(bean.getBindingJoinByNameIgnored());
    }

    /**
     * Ignores fields on reading with all known column position bindings.
     * <p>Also incidentally tests:<ul>
     *     <li>Ignoring all fields with binding annotations for a preferred
     *     mapping strategy during automatic selection does not change the
     *     selected mapping strategy.</li>
     *     <li>Ignoring a class/field combination not in use during mapping</li>
     * </ul></p>
     * @throws NoSuchFieldException Never
     */
    @Test
    public void testIgnoreAllWithColumnPositionMappingOnReading() throws NoSuchFieldException {
        final String input = "1,2 3,4,5,6,7 8,9,10\n";
        final CsvToBean<IgnoreMock> csvToBean = new CsvToBeanBuilder<IgnoreMock>(new StringReader(input))
                .withType(IgnoreMock.class)
                .withIgnoreField(IgnoreMock.class, IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored"))
                .withIgnoreField(IgnoreMock.class, IgnoreMock.class.getDeclaredField("bindingSplitNotIgnored"))
                .withIgnoreField(IgnoreMock.class, IgnoreMock.class.getDeclaredField("bindingJoinByPositionNotIgnored"))
                .withIgnoreField(IgnoreRecursionMock.class, IgnoreRecursionMock.class.getDeclaredField("topLevelInteger"))
                .build();
        List<IgnoreMock> beans = csvToBean.parse();
        List<CsvException> exceptions = csvToBean.getCapturedExceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.isEmpty());
        assertNotNull(beans);
        assertEquals(1, beans.size());

        // Test field values
        IgnoreMock bean = beans.get(0);
        assertEquals(0, bean.getBindingPrimitiveNotIgnored());
        assertNull(bean.getBindingSplitNotIgnored());
        assertNull(bean.getBindingJoinByPositionNotIgnored());
        assertEquals(0, bean.getBindingPrimitiveIgnored());
        assertNull(bean.getBindingSplitIgnored());
        assertNull(bean.getBindingJoinByPositionIgnored());
    }

    @Test
    public void testIgnoreWithRecursion() {
        final String input = "bindingPrimitiveNotIgnored,bindingSplitNotIgnored,bindingJoinByNameNotIgnored,bindingJoinByNameNotIgnored,bindingPrimitiveIgnored,bindingSplitIgnored,bindingJoinByNameIgnored,bindingJoinByNameIgnored,topLevelInteger\n" +
                "1,2 3,4,5,6,7 8,9,10,11\n";
        final CsvToBean<IgnoreRecursionMock> csvToBean = new CsvToBeanBuilder<IgnoreRecursionMock>(new StringReader(input))
                .withType(IgnoreRecursionMock.class)
                .build();
        List<IgnoreRecursionMock> beans = csvToBean.parse();
        List<CsvException> exceptions = csvToBean.getCapturedExceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.isEmpty());
        assertNotNull(beans);
        assertEquals(1, beans.size());

        // Test field values
        IgnoreRecursionMock bean = beans.get(0);
        assertEquals(11, bean.getTopLevelInteger());
        assertNull(bean.getIgnoredRecursiveMember());
    }

    /**
     * Tests that ignoring fields forces a remapping, which can change
     * bindings.
     * <p>Also incidentally tests:<ul>
     *     <li>Calling {@link MappingStrategy#setType(Class)} before
     *     {@link MappingStrategy#ignoreFields(MultiValuedMap)}</li>
     * </ul></p>
     * @throws NoSuchFieldException
     */
    @Test
    public void testRemappingOnIgnore() throws NoSuchFieldException {
        String input = "int1,int2\n1,2\n";

        // First we try without ignoring anything to establish that the fuzzy
        // mapping maps as we expect.
        MappingStrategy<IgnoreFuzzyMock> strategy = new FuzzyMappingStrategy<>();
        strategy.setType(IgnoreFuzzyMock.class);
        CsvToBean<IgnoreFuzzyMock> csvToBean = new CsvToBeanBuilder<IgnoreFuzzyMock>(new StringReader(input))
                .withMappingStrategy(strategy)
                .build();
        List<IgnoreFuzzyMock> beans = csvToBean.parse();
        assertNotNull(beans);
        assertEquals(1, beans.size());

        // Test individual values
        IgnoreFuzzyMock bean = beans.get(0);
        assertEquals(1, bean.getInt1());
        assertEquals(2, bean.getInt2());
        assertEquals(0, bean.getInt3());

        // Now the same thing, this time ignoring one of the fields
        ListValuedMap<Class<?>, Field> ignoredFields = new ArrayListValuedHashMap<>();
        ignoredFields.put(IgnoreFuzzyMock.class, IgnoreFuzzyMock.class.getDeclaredField("int1"));
        strategy.ignoreFields(ignoredFields);
        csvToBean = new CsvToBeanBuilder<IgnoreFuzzyMock>(new StringReader(input))
                .withMappingStrategy(strategy)
                .build();
        beans = csvToBean.parse();
        assertNotNull(beans);
        assertEquals(1, beans.size());

        // Test individual values
        bean = beans.get(0);
        assertEquals(0, bean.getInt1());
        assertEquals(2, bean.getInt2());
        assertEquals(1, bean.getInt3());
    }

    /**
     * Tests that the mechanisms for ignoring fields also work while writing.
     * Since the mechanism is basically decoupled from the question of reading
     * or writing, we take a small leap of faith and say that if all of the
     * reading tests in this class pass, all we need is one writing test to
     * demonstrate convincingly that all combinations tested with reading also
     * work with writing.
     */
    @Test
    public void testIgnoreOnWriting() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, NoSuchFieldException {
        // Prepare bean
        IgnoreMock bean = new IgnoreMock();
        ListValuedMap<String, Integer> stringMap = new ArrayListValuedHashMap<>();
        stringMap.put("bindingJoinByNameIgnored", 1);
        stringMap.put("bindingJoinByNameIgnored", 2);
        bean.setBindingJoinByNameIgnored(stringMap);
        stringMap = new ArrayListValuedHashMap<>();
        stringMap.put("bindingJoinByNameNotIgnored", 3);
        stringMap.put("bindingJoinByNameNotIgnored", 4);
        bean.setBindingJoinByNameNotIgnored(stringMap);
        ListValuedMap<Integer, Integer> integerMap = new ArrayListValuedHashMap<>();
        integerMap.put(6, 5);
        integerMap.put(7, 6);
        bean.setBindingJoinByPositionIgnored(integerMap);
        integerMap = new ArrayListValuedHashMap<>();
        integerMap.put(2, 7);
        integerMap.put(3, 8);
        bean.setBindingJoinByPositionNotIgnored(integerMap);
        bean.setBindingPrimitiveIgnored(9);
        bean.setBindingPrimitiveNotIgnored(10);
        bean.setBindingSplitIgnored(Arrays.asList(11, 12));
        bean.setBindingSplitNotIgnored(Arrays.asList(13, 14));

        StringWriter w = new StringWriter();
        StatefulBeanToCsv<IgnoreMock> beanToCsv = new StatefulBeanToCsvBuilder<IgnoreMock>(w)
                .withApplyQuotesToAll(false)
                .withIgnoreField(IgnoreMock.class, IgnoreMock.class.getDeclaredField("bindingPrimitiveNotIgnored"))
                .build();
        beanToCsv.write(bean);
        assertEquals(",13 14,7,8\n", w.toString());
    }
}
