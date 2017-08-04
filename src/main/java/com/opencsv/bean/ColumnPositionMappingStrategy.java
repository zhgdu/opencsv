package com.opencsv.bean;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

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
public class ColumnPositionMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

    private String[] columnMapping = new String[]{};
    private boolean columnsExplicitlySet = false;

    /**
     * Default constructor.
     */
    public ColumnPositionMappingStrategy() {
    }
    
    /**
     * Captures the header from the reader - required by the MappingStrategy
     * interface and is a do nothing method for the
     * ColumnPositionMappingStrategy.
     *
     * @param reader {@inheritDoc}
     * @throws IOException Would be thrown by the CSVReader if it was used :)
     */
    @Override
    public void captureHeader(CSVReader reader) throws IOException {
        //do nothing, first line is not header
    }

    /**
     * This method returns an empty array.
     * The column position mapping strategy assumes that there is no header, and
     * thus it also does not write one, accordingly.
     * @return An empty array
     */
    @Override
    public String[] generateHeader() {
        return new String[0];
    }

    @Override
    public Integer getColumnIndex(String name) {
        return indexLookup.get(name);
    }

    @Override
    public int findMaxFieldIndex() {
        return columnMapping == null ? -1 : columnMapping.length-1;
    }

    /**
     * Gets a column name.
     *
     * @param col Position of the column.
     * @return Column name or null if col &gt; number of mappings.
     */
    @Override
    public String getColumnName(int col) {
      return col < columnMapping.length ? columnMapping[col] : null;
    }

    /**
     * Retrieves the column mappings.
     *
     * @return String array with the column mappings.
     */
    public String[] getColumnMapping() {
        return columnMapping.clone();
    }

    /**
     * Setter for the ColumnMappings.
     *
     * @param columnMapping Column names to be mapped.
     */
    public void setColumnMapping(String... columnMapping) {
        this.columnMapping = columnMapping != null ? columnMapping.clone() : new String[]{};
        resetIndexMap();
        createIndexLookup(this.columnMapping);
        columnsExplicitlySet = true;
    }

    /**
     * Sets the class type that is being mapped.
     * Also initializes the mapping between column positions and bean fields.
     */
    // The rest of the Javadoc is inherited.
    @Override
    public void setType(Class<? extends T> type) throws CsvBadConverterException {
        super.setType(type);
        if (!columnsExplicitlySet) {
            SortedMap<Integer, BeanField> cols = new TreeMap<>();
            for (BeanField beanField : fieldMap.values()) {
                if (beanField
                        .getField()
                        .getAnnotation(CsvCustomBindByPosition.class) != null) {
                    cols.put(beanField
                            .getField()
                            .getAnnotation(CsvCustomBindByPosition.class)
                            .position(), beanField);
                } else if (beanField
                        .getField()
                        .getAnnotation(CsvBindByPosition.class) != null) {
                    cols.put(beanField
                            .getField()
                            .getAnnotation(CsvBindByPosition.class)
                            .position(), beanField);
                }
            }

            if (!cols.isEmpty()) {
                columnMapping = new String[cols.lastKey() + 1];
                for (Map.Entry<Integer, BeanField> entry : cols.entrySet()) {
                    columnMapping[entry.getKey()] = entry
                            .getValue()
                            .getField()
                            .getName()
                            .toUpperCase()
                            .trim();
                }
                resetIndexMap();
                createIndexLookup(columnMapping);
            } else {
                columnMapping = new String[0];
            }
        }
    }

    @Override
    protected void loadFieldMap() throws CsvBadConverterException {
        boolean required;
        fieldMap = new HashMap<>();

        for (Field field : loadFields(getType())) {
            String columnName;
            String fieldLocale;

            // Custom converters always have precedence.
            if (field.isAnnotationPresent(CsvCustomBindByPosition.class)) {
                columnName = field.getName().toUpperCase().trim();
                CsvCustomBindByPosition annotation = field
                        .getAnnotation(CsvCustomBindByPosition.class);
                Class<? extends AbstractBeanField> converter = annotation.converter();
                BeanField bean = instantiateCustomConverter(converter);
                bean.setField(field);
                required = annotation.required();
                bean.setRequired(required);
                fieldMap.put(columnName, bean);
            }

            // Then it must be a bind by position.
            else {
                CsvBindByPosition annotation = field.getAnnotation(CsvBindByPosition.class);
                required = annotation.required();
                columnName = field.getName().toUpperCase().trim();
                fieldLocale = annotation.locale();
                if (field.isAnnotationPresent(CsvDate.class)) {
                    String formatString = field.getAnnotation(CsvDate.class).value();
                    fieldMap.put(columnName, new BeanFieldDate(field, required, formatString, fieldLocale, errorLocale));
                } else {
                    fieldMap.put(columnName, new BeanFieldPrimitiveTypes(field, required, fieldLocale, errorLocale));
                }
            }
        }
    }

    @Override
    public void verifyLineLength(int numberOfFields) throws CsvRequiredFieldEmptyException {
        if(columnMapping != null) {
            BeanField f;
            StringBuilder sb = null;
            for(int i = numberOfFields; i < columnMapping.length; i++) {
                f = findField(i);
                if(f != null && f.isRequired()) {
                    if(sb == null) {
                        sb = new StringBuilder(ResourceBundle.getBundle("opencsv", errorLocale).getString("multiple.required.field.empty"));
                    }
                    sb.append(' ');
                    sb.append(f.getField().getName());
                }
            }
            if(sb != null) {
                throw new CsvRequiredFieldEmptyException(type, sb.toString());
            }
        }
    }
    
    private List<Field> loadFields(Class<? extends T> cls) {
        List<Field> fields = new ArrayList<>();
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(CsvBindByPosition.class)
                    || field.isAnnotationPresent(CsvCustomBindByPosition.class)) {
                fields.add(field);
            }
        }
        annotationDriven = !fields.isEmpty();
        return fields;
    }
}
