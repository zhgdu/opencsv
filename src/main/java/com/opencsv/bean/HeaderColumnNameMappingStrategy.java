package com.opencsv.bean;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
public class HeaderColumnNameMappingStrategy<T> extends HeaderNameBaseMappingStrategy<T> {

    /**
     * Default constructor.
     */
    public HeaderColumnNameMappingStrategy() {
    }

    /**
     * Creates a map of annotated fields in the bean to be processed.
     * <p>This method is called by {@link #loadFieldMap()} when at least one
     * relevant annotation is found on a member variable.</p>
     */
    @Override
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
     * Partitions all non-synthetic fields of the bean type being processed
     * into annotated and non-annotated fields.
     *
     * @return A map in which all annotated fields are mapped under
     * {@link Boolean#TRUE}, and all non-annotated fields are mapped under
     * {@link Boolean#FALSE}.
     */
    @Override
    protected Map<Boolean, List<Field>> partitionFields() {
        return Stream.of(FieldUtils.getAllFields(getType()))
                .filter(f -> !f.isSynthetic())
                .collect(Collectors.partitioningBy(
                        f -> f.isAnnotationPresent(CsvBindByName.class)
                                || f.isAnnotationPresent(CsvCustomBindByName.class)
                                || f.isAnnotationPresent(CsvBindAndSplitByName.class)
                                || f.isAnnotationPresent(CsvBindAndJoinByName.class)));
    }
}
