package com.opencsv.bean;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvBeanIntrospectionException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.text.StrBuilder;

/*
 * Copyright 2007 Kyle Miller.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Maps data to objects using the column names in the first row of the CSV file
 * as reference. This way the column order does not matter.
 *
 * @param <T> Type of the bean to be returned
 */
public class HeaderColumnNameMappingStrategy<T> implements MappingStrategy<T> {

    // TODO: header and indexLookup should be replaced with a BidiMap from Apache
    // Commons Collections once Apache Commons BeanUtils supports Collections
    // version 4.0 or newer. Until then I don't like BidiMaps, because they
    // aren't done with Generics, meaning everything is an Object and there is
    // no type safety.
    /**
     * An ordered array of the headers for the columns of a CSV input.
     * When reading, this array is automatically populated from the input source.
     * When writing, it is guessed from annotations, or, lacking any annotations,
     * from the names of the variables in the bean to be written.
     */
    protected String[] header;
    
    /** This map makes finding the column index of a header name easy. */
    protected Map<String, Integer> indexLookup = new HashMap<>();
    
    /**
     * Given a header name, this map allows one to find the corresponding
     * property descriptor.
     */
    protected Map<String, PropertyDescriptor> descriptorMap = null;
    
    /**
     * Given a header name, this map allows one to find the corresponding
     * {@link BeanField}.
     */
    protected Map<String, BeanField> fieldMap = null;
    
    /** This is the class of the bean to be manipulated. */
    protected Class<? extends T> type;
    
    /**
     * Whether or not annotations were found and should be used for determining
     * the binding between columns in a CSV source or destination and fields in
     * a bean.
     */
    protected boolean annotationDriven;
    
    /** Locale for error messages. */
    protected Locale errorLocale = Locale.getDefault();
    
    /**
     * Default constructor.
     */
    public HeaderColumnNameMappingStrategy() {
    }
    
    @Override
    public void captureHeader(CSVReader reader) throws IOException, CsvRequiredFieldEmptyException {
        // Validation
        if(type == null) {
            throw new IllegalStateException(ResourceBundle.getBundle("opencsv", errorLocale).getString("type.unset"));
        }
        
        // Read the header
        header = ObjectUtils.defaultIfNull(reader.readNext(), ArrayUtils.EMPTY_STRING_ARRAY);

        // Create a list for the Required fields keys.
        List<String> requiredKeys = new ArrayList<>();

        for(Map.Entry<String, BeanField> entrySet : fieldMap.entrySet()) {
            BeanField beanField = entrySet.getValue();
            if (beanField.isRequired()) {
                requiredKeys.add(entrySet.getKey().toUpperCase());
            }
        }

        if (requiredKeys.isEmpty()) {
            return;
        }

        // Remove fields that are in the header
        for (int i = 0; i < header.length && !requiredKeys.isEmpty(); i++) {
            requiredKeys.remove(header[i].toUpperCase());
        }

        // Throw an exception if anything is left
        if (!requiredKeys.isEmpty()) {
            StrBuilder builder = new StrBuilder(128);
            String missingRequiredFields = builder.appendWithSeparators(requiredKeys, ",").toString();
            // TODO consider CsvRequiredFieldsEmpty for multiple missing required fields.
            throw new CsvRequiredFieldEmptyException(type, fieldMap.get(requiredKeys.get(0)).getField(),
                    String.format(ResourceBundle.getBundle("opencsv", errorLocale).getString("header.required.field.absent"),
                            missingRequiredFields));
        }
    }
    
    @Override
    public void verifyLineLength(int numberOfFields) throws CsvRequiredFieldEmptyException {
        if(header != null) {
            BeanField f;
            StringBuilder sb = null;
            for(int i = numberOfFields; i < header.length; i++) {
                f = findField(i);
                if(f.isRequired()) {
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
    
    /**
     * This method generates a header that can be used for writing beans of the
     * type provided back to a file.
     * The ordering of the headers is alphabetically ascending.
     * @return An array of header names for the output file, or an empty array
     *   if no header should be written
     */
    @Override
    public String[] generateHeader() {
        if(type == null) {
            throw new IllegalStateException(ResourceBundle.getBundle("opencsv", errorLocale).getString("type.before.header"));
        }
        
        // Always take what's been given or previously determined first.
        if(header == null) {

            // To make testing simpler and because not all receivers are
            // guaranteed to be as flexible with column order as opencsv,
            // make the column ordering deterministic by sorting the column
            // headers alphabetically.
            SortedSet<String> set = new TreeSet(fieldMap.keySet());
            header = set.toArray(new String[fieldMap.size()]);
        }
        
        // Clone so no one has direct access to internal data structures
        return ArrayUtils.clone(header);
    }

    /**
     * Creates an index map of column names to column position.
     *
     * @param values Array of header values.
     */
    protected void createIndexLookup(String[] values) {
        if (indexLookup.isEmpty()) {
            for (int i = 0; i < values.length; i++) {
                indexLookup.put(values[i], i);
            }
        }
    }

    /**
     * Resets index map of column names to column position.
     */
    protected void resetIndexMap() {
        indexLookup.clear();
    }

    @Override
    public Integer getColumnIndex(String name) {
        if (null == header) {
            throw new IllegalStateException(ResourceBundle.getBundle("opencsv", errorLocale).getString("header.unread"));
        }

        createIndexLookup(header);

        return indexLookup.get(name);
    }

    @Override
    @Deprecated
    public PropertyDescriptor findDescriptor(int col) {
        String columnName = getColumnName(col);
        BeanField beanField = null;
        if(StringUtils.isNotBlank(columnName)) {
            beanField = fieldMap.get(columnName.toUpperCase().trim());
        }
        if(beanField != null) {
            return findDescriptor(beanField.getField().getName());
        }
        if(StringUtils.isNotBlank(columnName)) {
            return findDescriptor(columnName);
        }
        return null;
    }

    @Override
    public BeanField findField(int col) throws CsvBadConverterException {
        BeanField beanField = null;
        String columnName = getColumnName(col);
        if(StringUtils.isNotBlank(columnName)) {
            beanField = fieldMap.get(columnName.toUpperCase().trim());
        }
        return beanField;
    }
    
    @Override
    public int findMaxFieldIndex() {
        return header == null ? -1 : header.length-1;
    }

    /**
     * Get the column name for a given column position.
     *
     * @param col Column position.
     * @return The column name or null if the position is larger than the
     * header array or there are no headers defined.
     */
    public String getColumnName(int col) {
        return (null != header && col < header.length) ? header[col] : null;
    }

    /**
     * Find the property descriptor for a given column.
     *
     * @param name Column name to look up.
     * @return The property descriptor for the column.
     * @deprecated Introspection will be replaced with reflection in version 5.0
     */
    @Deprecated
    protected PropertyDescriptor findDescriptor(String name) {
        return descriptorMap.get(name.toUpperCase().trim());
    }

    /**
     * Builds a map of property descriptors for the bean.
     *
     * @return Map of property descriptors
     * @throws IntrospectionException Thrown on error getting information
     *                                about the bean.
     * @deprecated Introspection will be replaced with reflection in version 5.0
     */
    @Deprecated
    protected Map<String, PropertyDescriptor> loadDescriptorMap() throws IntrospectionException {
        Map<String, PropertyDescriptor> map = new HashMap<>();

        PropertyDescriptor[] descriptors = loadDescriptors(getType());
        for (PropertyDescriptor descriptor : descriptors) {
            map.put(descriptor.getName().toUpperCase(), descriptor);
        }

        return map;
    }

    /**
     * Attempts to instantiate the class of the custom converter specified.
     *
     * @param converter The class for a custom converter
     * @return The custom converter
     * @throws CsvBadConverterException If the class cannot be instantiated
     */
    protected BeanField instantiateCustomConverter(Class<? extends AbstractBeanField> converter)
            throws CsvBadConverterException {
        try {
            BeanField c = converter.newInstance();
            c.setErrorLocale(errorLocale);
            return c;
        } catch (IllegalAccessException | InstantiationException oldEx) {
            CsvBadConverterException newEx =
                    new CsvBadConverterException(converter,
                            String.format(ResourceBundle.getBundle("opencsv", errorLocale).getString("custom.converter.invalid"), converter.getCanonicalName()));
            newEx.initCause(oldEx);
            throw newEx;
        }
    }

    /**
     * Builds a map of fields for the bean.
     *
     * @throws CsvBadConverterException If there is a problem instantiating the
     *                                  custom converter for an annotated field
     */
    protected void loadFieldMap() throws CsvBadConverterException {
        boolean required;
        fieldMap = new HashMap<>();

        for (Field field : loadFields(getType())) {
            String columnName;
            String locale;

            // Always check for a custom converter first.
            if (field.isAnnotationPresent(CsvCustomBindByName.class)) {
                CsvCustomBindByName annotation = field.getAnnotation(CsvCustomBindByName.class);
                columnName = annotation.column().toUpperCase().trim();
                if(StringUtils.isEmpty(columnName)) {
                    columnName = field.getName().toUpperCase();
                }
                Class<? extends AbstractBeanField> converter = field
                        .getAnnotation(CsvCustomBindByName.class)
                        .converter();
                BeanField bean = instantiateCustomConverter(converter);
                bean.setField(field);
                required = annotation.required();
                bean.setRequired(required);
                fieldMap.put(columnName, bean);
            }

            // Otherwise it must be CsvBindByName.
            else {
                CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
                required = annotation.required();
                columnName = annotation.column().toUpperCase().trim();
                locale = annotation.locale();
                if (field.isAnnotationPresent(CsvDate.class)) {
                    String formatString = field.getAnnotation(CsvDate.class).value();
                    if (StringUtils.isEmpty(columnName)) {
                        fieldMap.put(field.getName().toUpperCase(),
                                new BeanFieldDate(field, required, formatString, locale, errorLocale));
                    } else {
                        fieldMap.put(columnName, new BeanFieldDate(field, required, formatString, locale, errorLocale));
                    }
                } else {
                    if (StringUtils.isEmpty(columnName)) {
                        fieldMap.put(field.getName().toUpperCase(),
                                new BeanFieldPrimitiveTypes(field, required, locale, errorLocale));
                    } else {
                        fieldMap.put(columnName, new BeanFieldPrimitiveTypes(field, required, locale, errorLocale));
                    }
                }
            }
        }
    }

    private PropertyDescriptor[] loadDescriptors(Class<? extends T> cls) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(cls);
        return beanInfo.getPropertyDescriptors();
    }

    private List<Field> loadFields(Class<? extends T> cls) {
        List<Field> fields = new ArrayList<>();
        for (Field field : FieldUtils.getAllFields(cls)) {
            if (field.isAnnotationPresent(CsvBindByName.class)
                    || field.isAnnotationPresent(CsvCustomBindByName.class)) {
                fields.add(field);
            }
        }
        annotationDriven = !fields.isEmpty();
        return fields;
    }

    @Override
    public T createBean() throws InstantiationException, IllegalAccessException, IllegalStateException {
        if(type == null) {
            throw new IllegalStateException(ResourceBundle.getBundle("opencsv", errorLocale).getString("type.unset"));
        }
        return type.newInstance();
    }

    /**
     * Get the class type that the Strategy is mapping.
     *
     * @return Class of the object that mapper will create.
     */
    public Class<? extends T> getType() {
        return type;
    }

    /**
     * Sets the class type that is being mapped.
     * Also initializes the mapping between column names and bean fields.
     */
    // The rest of the Javadoc is inherited.
    @Override
    public void setType(Class<? extends T> type) throws CsvBadConverterException {
        this.type = type;
        loadFieldMap();
        try {
            descriptorMap = loadDescriptorMap();
        }
        catch(IntrospectionException e) {
            // For the record, especially with respect to code coverage, I have
            // tried to trigger this exception, and I can't. I have read the
            // source code for Java 8, and I can find no possible way for
            // IntrospectionException to be thrown by our code.
            // -Andrew Jones 31.07.2017
            CsvBeanIntrospectionException csve = new CsvBeanIntrospectionException(
                    ResourceBundle.getBundle("opencsv", errorLocale).getString("bean.descriptors.uninitialized"));
            csve.initCause(e);
            throw csve;
        }
    }
    
    @Override
    public void setErrorLocale(Locale errorLocale) {
        this.errorLocale = ObjectUtils.defaultIfNull(errorLocale, Locale.getDefault());
        
        // It's very possible that setType() was called first, which creates all
        // of the BeanFields, so we need to go back through the list and correct
        // them all.
        if(fieldMap != null) {
            for(BeanField f : fieldMap.values()) {
                f.setErrorLocale(errorLocale);
            }
        }
    }

    /**
     * Determines whether the mapping strategy is driven by annotations.
     * For this mapping strategy, the supported annotations are:
     * <ul><li>{@link com.opencsv.bean.CsvBindByName}</li>
     * <li>{@link com.opencsv.bean.CsvCustomBindByName}</li>
     * </ul>
     *
     * @return Whether the mapping strategy is driven by annotations
     */
    @Override
    public boolean isAnnotationDriven() {
        return annotationDriven;
    }
}
