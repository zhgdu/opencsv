package com.opencsv.bean;

import com.opencsv.CSVReader;
import com.opencsv.ICSVParser;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class HeaderColumnNameMappingStrategy<T> extends AbstractMappingStrategy<String, String, ComplexFieldMapEntry<String, String, T>, T> {

    /**
     * Given a header name, this map allows one to find the corresponding
     * {@link BeanField}.
     */
    protected FieldMapByName<T> fieldMap = null;

    /** Holds a {@link java.util.Comparator} to sort columns on writing. */
    protected Comparator<String> writeOrder = null;
    
    /**
     * Default constructor.
     */
    public HeaderColumnNameMappingStrategy() {
    }
    
    @Override
    public void captureHeader(CSVReader reader) throws IOException, CsvRequiredFieldEmptyException {
        // Validation
        if(type == null) {
            throw new IllegalStateException(ResourceBundle
                    .getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                    .getString("type.unset"));
        }
        
        // Read the header
        String[] header = ObjectUtils.defaultIfNull(reader.readNextSilently(), ArrayUtils.EMPTY_STRING_ARRAY);
        headerIndex.initializeHeaderIndex(header);

        // Throw an exception if any required headers are missing
        List<FieldMapByNameEntry<T>> missingRequiredHeaders = fieldMap.determineMissingRequiredHeaders(header);
        if (!missingRequiredHeaders.isEmpty()) {
            String[] requiredHeaderNames = new String[missingRequiredHeaders.size()];
            List<Field> requiredFields = new ArrayList<>(missingRequiredHeaders.size());
            for(int i = 0; i < missingRequiredHeaders.size(); i++) {
                FieldMapByNameEntry<T> fme = missingRequiredHeaders.get(i);
                if(fme.isRegexPattern()) {
                    requiredHeaderNames[i] = String.format(
                            ResourceBundle
                                    .getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                                    .getString("matching"),
                            fme.getName());
                }
                else {
                    requiredHeaderNames[i] = fme.getName();
                }
                requiredFields.add(fme.getField().getField());
            }
            String missingRequiredFields = String.join(", ", requiredHeaderNames);
            String allHeaders = String.join(",", header);
            CsvRequiredFieldEmptyException e = new CsvRequiredFieldEmptyException(type, requiredFields,
                    String.format(
                            ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                                    .getString("header.required.field.absent"),
                            missingRequiredFields, allHeaders));
            e.setLine(header);
            throw e;
        }
    }
    
    @Override
    protected String chooseMultivaluedFieldIndexFromHeaderIndex(int index) {
        String[] s = headerIndex.getHeaderIndex();
        return index >= s.length ? null: s[index];
    }
    
    @Override
    public void verifyLineLength(int numberOfFields) throws CsvRequiredFieldEmptyException {
        if(!headerIndex.isEmpty()) {
            if (numberOfFields != headerIndex.getHeaderIndexLength()) {
                throw new CsvRequiredFieldEmptyException(type, ResourceBundle
                        .getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                        .getString("header.data.mismatch"));
            }
        }
    }
    
    @Override
    protected BeanField<T, String> findField(int col) throws CsvBadConverterException {
        BeanField<T, String> beanField = null;
        String columnName = getColumnName(col);
        if (columnName == null) {
            return null;
        }
        columnName = columnName.trim();
        if (!columnName.isEmpty()) {
            beanField = fieldMap.get(columnName.toUpperCase());
        }
        return beanField;
    }

    /**
     * Creates a map of annotated fields in the bean to be processed.
     * This method is called by {@link #loadFieldMap()} when at least one
     * relevant annotation is found on a member variable.
     *
     * @param fields A list of fields annotated with a name-binding annotation
     *               in the bean to be processed
     * @since 5.0
     */
    protected void loadAnnotatedFieldMap(List<Field> fields) {
        boolean required;

        for (Field field : fields) {
            String columnName, locale, writeLocale, capture, format;

            // Always check for a custom converter first.
            if (field.isAnnotationPresent(CsvCustomBindByName.class)) {
                CsvCustomBindByName annotation = field.getAnnotation(CsvCustomBindByName.class);
                columnName = annotation.column().toUpperCase().trim();
                if(StringUtils.isEmpty(columnName)) {
                    columnName = field.getName().toUpperCase();
                }
                @SuppressWarnings("unchecked")
                Class<? extends AbstractBeanField<T, String>> converter = (Class<? extends AbstractBeanField<T, String>>)field
                        .getAnnotation(CsvCustomBindByName.class)
                        .converter();
                BeanField<T, String> bean = instantiateCustomConverter(converter);
                bean.setField(field);
                required = annotation.required();
                bean.setRequired(required);
                fieldMap.put(columnName, bean);
            }

            // Then check for a collection
            else if(field.isAnnotationPresent(CsvBindAndSplitByName.class)) {
                CsvBindAndSplitByName annotation = field.getAnnotation(CsvBindAndSplitByName.class);
                required = annotation.required();
                columnName = annotation.column().toUpperCase().trim();
                locale = annotation.locale();
                writeLocale = annotation.writeLocaleEqualsReadLocale()
                        ? locale : annotation.writeLocale();
                String splitOn = annotation.splitOn();
                String writeDelimiter = annotation.writeDelimiter();
                Class<? extends Collection> collectionType = annotation.collectionType();
                Class<?> elementType = annotation.elementType();
                Class<? extends AbstractCsvConverter> splitConverter = annotation.converter();
                capture = annotation.capture();
                format = annotation.format();

                CsvConverter converter = determineConverter(field, elementType, locale, writeLocale, splitConverter);
                if (StringUtils.isEmpty(columnName)) {
                    fieldMap.put(field.getName().toUpperCase(),
                            new BeanFieldSplit<>(
                                    field, required, errorLocale, converter,
                                    splitOn, writeDelimiter, collectionType,
                                    capture, format));
                } else {
                    fieldMap.put(columnName, new BeanFieldSplit<>(
                            field, required, errorLocale, converter, splitOn,
                            writeDelimiter, collectionType, capture, format));
                }
            }

            // Then for a multi-column annotation
            else if(field.isAnnotationPresent(CsvBindAndJoinByName.class)) {
                CsvBindAndJoinByName annotation = field.getAnnotation(CsvBindAndJoinByName.class);
                required = annotation.required();
                String columnRegex = annotation.column();
                locale = annotation.locale();
                writeLocale = annotation.writeLocaleEqualsReadLocale()
                        ? locale : annotation.writeLocale();
                Class<?> elementType = annotation.elementType();
                Class<? extends MultiValuedMap> mapType = annotation.mapType();
                Class<? extends AbstractCsvConverter> joinConverter = annotation.converter();
                capture = annotation.capture();
                format = annotation.format();

                CsvConverter converter = determineConverter(field, elementType, locale, writeLocale, joinConverter);
                if (StringUtils.isEmpty(columnRegex)) {
                    fieldMap.putComplex(field.getName(),
                            new BeanFieldJoinStringIndex<>(
                                    field, required, errorLocale, converter,
                                    mapType, capture, format));
                } else {
                    fieldMap.putComplex(columnRegex, new BeanFieldJoinStringIndex<>(
                            field, required, errorLocale, converter, mapType,
                            capture, format));
                }
            }

            // Otherwise it must be CsvBindByName.
            else {
                CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
                required = annotation.required();
                columnName = annotation.column().toUpperCase().trim();
                locale = annotation.locale();
                writeLocale = annotation.writeLocaleEqualsReadLocale()
                        ? locale : annotation.writeLocale();
                capture = annotation.capture();
                format = annotation.format();
                CsvConverter converter = determineConverter(field, field.getType(), locale, writeLocale, null);

                if (StringUtils.isEmpty(columnName)) {
                    fieldMap.put(field.getName().toUpperCase(),
                            new BeanFieldSingleValue<>(field, required,
                                    errorLocale, converter, capture, format));
                } else {
                    fieldMap.put(columnName, new BeanFieldSingleValue<>(
                            field, required, errorLocale, converter, capture, format));
                }
            }
        }
    }

    /**
     * Creates a map of fields in the bean to be processed that have no
     * annotations.
     * This method is called by {@link #loadFieldMap()} when absolutely no
     * annotations that are relevant for this mapping strategy are found in the
     * type of bean being processed. It is then assumed that every field is to
     * be included, and that the name of the member variable must exactly match
     * the header name of the input.
     *
     * @param fields A list of all non-synthetic fields in the bean to be
     *               processed
     * @since 5.0
     */
    protected void loadUnadornedFieldMap(List<Field> fields) {
        for(Field field : fields) {
            CsvConverter converter = determineConverter(field, field.getType(), null, null, null);
            fieldMap.put(field.getName().toUpperCase(), new BeanFieldSingleValue<>(
                    field, false, errorLocale, converter, null, null));
        }
    }

    /**
     * Partitions all non-synthetic fields of the bean type being processed
     * into annotated and non-annotated fields.
     *
     * @return A map in which all annotated fields are mapped under
     * {@link Boolean#TRUE}, and all non-annotated fields are mapped under
     * {@link Boolean#FALSE}.
     * @since 5.0
     */
    protected Map<Boolean, List<Field>> partitionFields() {
        return Stream.of(FieldUtils.getAllFields(getType()))
                .filter(f -> !f.isSynthetic())
                .collect(Collectors.partitioningBy(
                        f -> f.isAnnotationPresent(CsvBindByName.class)
                                || f.isAnnotationPresent(CsvCustomBindByName.class)
                                || f.isAnnotationPresent(CsvBindAndSplitByName.class)
                                || f.isAnnotationPresent(CsvBindAndJoinByName.class)));
    }

    /**
     * Builds a map of fields for the bean.
     * In this mapping strategy, that means checking all fields for the
     * presence of any relevant annotations, and if any are found, sending
     * all annotated fields to {@link #loadAnnotatedFieldMap(List)}, otherwise
     * sending all fields to {@link #loadUnadornedFieldMap(List)}.
     */
    @Override
    protected void loadFieldMap() throws CsvBadConverterException {
        fieldMap = new FieldMapByName<>(errorLocale);
        fieldMap.setColumnOrderOnWrite(writeOrder);
        Map<Boolean, List<Field>> partitionedFields = partitionFields();

        if(!partitionedFields.get(Boolean.TRUE).isEmpty()) {
            loadAnnotatedFieldMap(partitionedFields.get(Boolean.TRUE));
        }
        else {
            loadUnadornedFieldMap(partitionedFields.get(Boolean.FALSE));
        }
    }

    @Override
    protected FieldMap<String, String, ? extends ComplexFieldMapEntry<String, String, T>, T> getFieldMap() {return fieldMap;}
    
    @Override
    public String findHeader(int col) {
        return headerIndex.getByPosition(col);
    }
    
    /**
     * Sets the {@link java.util.Comparator} to be used to sort columns when
     * writing beans to a CSV file.
     * Behavior of this method when used on a mapping strategy intended for
     * reading data from a CSV source is not defined.
     *
     * @param writeOrder The {@link java.util.Comparator} to use. May be
     *   {@code null}, in which case the natural ordering is used.
     * @since 4.3
     */
    public void setColumnOrderOnWrite(Comparator<String> writeOrder) {
        this.writeOrder = writeOrder;
        if(fieldMap != null) {
            fieldMap.setColumnOrderOnWrite(this.writeOrder);
        }
    }
}
