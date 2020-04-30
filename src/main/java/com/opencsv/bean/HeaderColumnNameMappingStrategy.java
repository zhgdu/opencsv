package com.opencsv.bean;

import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

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
    protected void loadAnnotatedFieldMap(ListValuedMap<Class<?>, Field> fields) {
        boolean required;

        for (Map.Entry<Class<?>, Field> classField : fields.entries()) {
            Class<?> localType = classField.getKey();
            Field localField = classField.getValue();
            String columnName, locale, writeLocale, capture, format;

            // Always check for a custom converter first.
            if (localField.isAnnotationPresent(CsvCustomBindByName.class)) {
                CsvCustomBindByName annotation = localField
                        .getAnnotation(CsvCustomBindByName.class);
                columnName = annotation.column().toUpperCase().trim();
                if(StringUtils.isEmpty(columnName)) {
                    columnName = localField.getName().toUpperCase();
                }
                @SuppressWarnings("unchecked")
                Class<? extends AbstractBeanField<T, String>> converter = (Class<? extends AbstractBeanField<T, String>>)localField
                        .getAnnotation(CsvCustomBindByName.class)
                        .converter();
                BeanField<T, String> bean = instantiateCustomConverter(converter);
                bean.setType(localType);
                bean.setField(localField);
                required = annotation.required();
                bean.setRequired(required);
                fieldMap.put(columnName, bean);
            }

            // Then check for a collection
            else if(localField.isAnnotationPresent(CsvBindAndSplitByName.class)) {
                CsvBindAndSplitByName annotation = localField
                        .getAnnotation(CsvBindAndSplitByName.class);
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

                CsvConverter converter = determineConverter(
                        localField, elementType, locale,
                        writeLocale, splitConverter);
                if (StringUtils.isEmpty(columnName)) {
                    fieldMap.put(localField.getName().toUpperCase(),
                            new BeanFieldSplit<>(
                                    localType,
                                    localField, required,
                                    errorLocale, converter, splitOn,
                                    writeDelimiter, collectionType,
                                    elementType, capture, format));
                } else {
                    fieldMap.put(columnName, new BeanFieldSplit<>(
                            localType,
                            localField, required, errorLocale,
                            converter, splitOn, writeDelimiter, collectionType,
                            elementType, capture, format));
                }
            }

            // Then for a multi-column annotation
            else if(localField.isAnnotationPresent(CsvBindAndJoinByName.class)) {
                CsvBindAndJoinByName annotation = localField
                        .getAnnotation(CsvBindAndJoinByName.class);
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

                CsvConverter converter = determineConverter(
                        localField, elementType, locale,
                        writeLocale, joinConverter);
                if (StringUtils.isEmpty(columnRegex)) {
                    fieldMap.putComplex(localField.getName(),
                            new BeanFieldJoinStringIndex<>(
                                    localType,
                                    localField, required,
                                    errorLocale, converter, mapType, capture,
                                    format));
                } else {
                    fieldMap.putComplex(columnRegex, new BeanFieldJoinStringIndex<>(
                            localType,
                            localField, required, errorLocale,
                            converter, mapType, capture, format));
                }
            }

            // Otherwise it must be CsvBindByName.
            else {
                CsvBindByName annotation = localField.getAnnotation(CsvBindByName.class);
                required = annotation.required();
                columnName = annotation.column().toUpperCase().trim();
                locale = annotation.locale();
                writeLocale = annotation.writeLocaleEqualsReadLocale()
                        ? locale : annotation.writeLocale();
                capture = annotation.capture();
                format = annotation.format();
                CsvConverter converter = determineConverter(
                        localField,
                        localField.getType(), locale,
                        writeLocale, null);

                if (StringUtils.isEmpty(columnName)) {
                    fieldMap.put(localField.getName().toUpperCase(),
                            new BeanFieldSingleValue<>(
                                    localType,
                                    localField, required,
                                    errorLocale, converter, capture, format));
                } else {
                    fieldMap.put(columnName, new BeanFieldSingleValue<>(
                            localType,
                            localField, required, errorLocale,
                            converter, capture, format));
                }
            }
        }
    }

    /**
     * Returns a set of the annotations that are used for binding in this
     * mapping strategy.
     * <p>In this mapping strategy, those are currently:<ul>
     *     <li>{@link CsvBindByName}</li>
     *     <li>{@link CsvCustomBindByName}</li>
     *     <li>{@link CsvBindAndJoinByName}</li>
     *     <li>{@link CsvBindAndSplitByName}</li>
     * </ul></p>
     */
    @Override
    protected Set<Class<? extends Annotation>> getBindingAnnotations() {
        // With Java 9 this can be done more easily with Set.of()
        return new HashSet<>(Arrays.asList(
                CsvBindByName.class,
                CsvCustomBindByName.class,
                CsvBindAndSplitByName.class,
                CsvBindAndJoinByName.class));
    }
}
