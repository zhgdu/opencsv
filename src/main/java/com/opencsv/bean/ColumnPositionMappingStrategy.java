package com.opencsv.bean;

import com.opencsv.CSVReader;
import com.opencsv.ICSVParser;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Allows for the mapping of columns with their positions. Using this strategy
 * without annotations ({@link com.opencsv.bean.CsvBindByPosition} or
 * {@link com.opencsv.bean.CsvCustomBindByPosition}) requires all the columns
 * to be present in the CSV file and for them to be in a particular order. Using
 * annotations allows one to specify arbitrary zero-based column numbers for
 * each bean member variable to be filled. Also this strategy requires that the
 * file does NOT have a header. That said, the main use of this strategy is
 * files that do not have headers.
 *
 * @param <T> Type of object that is being processed.
 */
public class ColumnPositionMappingStrategy<T> extends AbstractMappingStrategy<String, Integer, ComplexFieldMapEntry<String, Integer, T>, T> {

    /**
     * Whether the user has programmatically set the map from column positions
     * to field names.
     */
    private boolean columnsExplicitlySet = false;

    /**
     * The map from column position to {@link BeanField}.
     */
    private FieldMapByPosition<T> fieldMap;

    /**
     * Holds a {@link java.util.Comparator} to sort columns on writing.
     */
    private Comparator<Integer> writeOrder;

    /**
     * Used to store a mapping from presumed input column index to desired
     * output column index, as determined by applying {@link #writeOrder}.
     */
    private Integer[] columnIndexForWriting = null;

    /**
     * Default constructor.
     */
    public ColumnPositionMappingStrategy() {
    }

    /**
     * There is no header per se for this mapping strategy, but this method
     * checks the first line to determine how many fields are present and
     * adjusts its field map accordingly.
     */
    // The rest of the Javadoc is inherited
    @Override
    public void captureHeader(CSVReader reader) throws IOException {
        // Validation
        if (type == null) {
            throw new IllegalStateException(ResourceBundle
                    .getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                    .getString("type.unset"));
        }

        String[] firstLine = reader.peek();
        fieldMap.setMaxIndex(firstLine.length - 1);
        if (!columnsExplicitlySet) {
            headerIndex.clear();
            for (FieldMapByPositionEntry entry : fieldMap) {
                Field f = entry.getField().getField();
                if (f.getAnnotation(CsvCustomBindByPosition.class) != null
                        || f.getAnnotation(CsvBindAndSplitByPosition.class) != null
                        || f.getAnnotation(CsvBindAndJoinByPosition.class) != null
                        || f.getAnnotation(CsvBindByPosition.class) != null) {
                    headerIndex.put(entry.getPosition(), f.getName().toUpperCase().trim());
                }
            }
        }
    }

    /**
     * @return {@inheritDoc} For this mapping strategy, it's simply
     * {@code index} wrapped as an {@link java.lang.Integer}.
     */
    // The rest of the Javadoc is inherited
    @Override
    protected Object chooseMultivaluedFieldIndexFromHeaderIndex(int index) {
        return Integer.valueOf(index);
    }

    @Override
    public BeanField<T> findField(int col) {
        // If we have a mapping for changing the order of the columns on
        // writing, be sure to use it.
        if (columnIndexForWriting != null) {
            return col < columnIndexForWriting.length ? fieldMap.get(columnIndexForWriting[col]) : null;
        }
        return fieldMap.get(col);
    }

    /**
     * This method returns an empty array.
     * The column position mapping strategy assumes that there is no header, and
     * thus it also does not write one, accordingly.
     *
     * @return An empty array
     */
    // The rest of the Javadoc is inherited
    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        String[] h = super.generateHeader(bean);
        columnIndexForWriting = new Integer[h.length];

        // Once we support Java 8, this might be nicer with Arrays.parallelSetAll().
        for (int i = 0; i < columnIndexForWriting.length; i++) {
            columnIndexForWriting[i] = i;
        }

        // Create the mapping for input column index to output column index.
        Arrays.sort(columnIndexForWriting, writeOrder);
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }

    /**
     * Gets a column name.
     *
     * @param col Position of the column.
     * @return Column name or null if col &gt; number of mappings.
     */
    @Override
    public String getColumnName(int col) {
        return headerIndex.getByPosition(col);
    }

    /**
     * Retrieves the column mappings.
     *
     * @return String array with the column mappings.
     */
    public String[] getColumnMapping() {
        return headerIndex.getHeaderIndex();
    }

    /**
     * Setter for the column mapping.
     * This mapping is for reading. Use of this method in conjunction with
     * writing is undefined.
     *
     * @param columnMapping Column names to be mapped.
     */
    public void setColumnMapping(String... columnMapping) {
        if (columnMapping != null) {
            headerIndex.initializeHeaderIndex(columnMapping);
        } else {
            headerIndex.clear();
        }
        columnsExplicitlySet = true;
    }

    @Override
    protected void loadFieldMap() throws CsvBadConverterException {
        boolean required;
        fieldMap = new FieldMapByPosition<>(errorLocale);
        fieldMap.setColumnOrderOnWrite(writeOrder);

        for (Field field : loadFields(getType())) {
            String fieldLocale, capture, format;

            // Custom converters always have precedence.
            if (field.isAnnotationPresent(CsvCustomBindByPosition.class)) {
                CsvCustomBindByPosition annotation = field
                        .getAnnotation(CsvCustomBindByPosition.class);
                Class<? extends AbstractBeanField> converter = annotation.converter();
                BeanField<T> bean = instantiateCustomConverter(converter);
                bean.setField(field);
                required = annotation.required();
                bean.setRequired(required);
                fieldMap.put(annotation.position(), bean);
            }

            // Then check for a collection
            else if (field.isAnnotationPresent(CsvBindAndSplitByPosition.class)) {
                CsvBindAndSplitByPosition annotation = field.getAnnotation(CsvBindAndSplitByPosition.class);
                required = annotation.required();
                fieldLocale = annotation.locale();
                String splitOn = annotation.splitOn();
                String writeDelimiter = annotation.writeDelimiter();
                Class<? extends Collection> collectionType = annotation.collectionType();
                Class<?> elementType = annotation.elementType();
                Class<? extends AbstractCsvConverter> splitConverter = annotation.converter();
                capture = annotation.capture();
                format = annotation.format();

                CsvConverter converter = determineConverter(field, elementType, fieldLocale, splitConverter);
                fieldMap.put(annotation.position(), new BeanFieldSplit<T>(
                        field, required, errorLocale, converter, splitOn,
                        writeDelimiter, collectionType, capture, format));
            }

            // Then check for a multi-column annotation
            else if (field.isAnnotationPresent(CsvBindAndJoinByPosition.class)) {
                CsvBindAndJoinByPosition annotation = field.getAnnotation(CsvBindAndJoinByPosition.class);
                required = annotation.required();
                fieldLocale = annotation.locale();
                Class<?> elementType = annotation.elementType();
                Class<? extends MultiValuedMap> mapType = annotation.mapType();
                Class<? extends AbstractCsvConverter> joinConverter = annotation.converter();
                capture = annotation.capture();
                format = annotation.format();

                CsvConverter converter = determineConverter(field, elementType, fieldLocale, joinConverter);
                fieldMap.putComplex(annotation.position(), new BeanFieldJoinIntegerIndex<T>(
                        field, required, errorLocale, converter, mapType, capture, format));
            }

            // Then it must be a bind by position.
            else {
                CsvBindByPosition annotation = field.getAnnotation(CsvBindByPosition.class);
                required = annotation.required();
                fieldLocale = annotation.locale();
                capture = annotation.capture();
                format = annotation.format();
                CsvConverter converter = determineConverter(field, field.getType(), fieldLocale, null);

                fieldMap.put(annotation.position(), new BeanFieldSingleValue<T>(
                        field, required, errorLocale, converter, capture, format));
            }
        }
    }

    @Override
    public void verifyLineLength(int numberOfFields) throws CsvRequiredFieldEmptyException {
        if (!headerIndex.isEmpty()) {
            BeanField f;
            StringBuilder sb = null;
            for (int i = numberOfFields; i <= headerIndex.findMaxIndex(); i++) {
                f = findField(i);
                if (f != null && f.isRequired()) {
                    if (sb == null) {
                        sb = new StringBuilder(ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale).getString("multiple.required.field.empty"));
                    }
                    sb.append(' ');
                    sb.append(f.getField().getName());
                }
            }
            if (sb != null) {
                throw new CsvRequiredFieldEmptyException(type, sb.toString());
            }
        }
    }

    private List<Field> loadFields(Class<? extends T> cls) {
        List<Field> fields = new LinkedList<>();
        for (Field field : FieldUtils.getAllFields(cls)) {
            if (field.isAnnotationPresent(CsvBindByPosition.class)
                    || field.isAnnotationPresent(CsvCustomBindByPosition.class)
                    || field.isAnnotationPresent(CsvBindAndJoinByPosition.class)
                    || field.isAnnotationPresent(CsvBindAndSplitByPosition.class)) {
                fields.add(field);
            }
        }
        setAnnotationDriven(!fields.isEmpty());
        return fields;
    }

    /**
     * Returns the column position for the given column number.
     * Yes, they're the same thing. For this mapping strategy, it's a simple
     * conversion from an integer to a string.
     */
    // The rest of the Javadoc is inherited
    @Override
    public String findHeader(int col) {
        return Integer.toString(col);
    }

    @Override
    protected FieldMap<String, Integer, ? extends ComplexFieldMapEntry<String, Integer, T>, T> getFieldMap() {
        return fieldMap;
    }

    /**
     * Sets the {@link java.util.Comparator} to be used to sort columns when
     * writing beans to a CSV file.
     * Behavior of this method when used on a mapping strategy intended for
     * reading data from a CSV source is not defined.
     *
     * @param writeOrder The {@link java.util.Comparator} to use. May be
     *                   {@code null}, in which case the natural ordering is used.
     * @since 4.3
     */
    public void setColumnOrderOnWrite(Comparator<Integer> writeOrder) {
        this.writeOrder = writeOrder;
        if (fieldMap != null) {
            fieldMap.setColumnOrderOnWrite(this.writeOrder);
        }
    }
}
