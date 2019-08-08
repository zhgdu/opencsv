/*
 * Copyright 2018 Andrew Rucker Jones.
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

import org.apache.commons.collections4.MultiValuedMap;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Implements a {@link BeanFieldJoin} with a {@link java.lang.String} for an
 * index.
 * 
 * @param <T> The type of the bean being populated
 * 
 * @author Andrew Rucker Jones
 * @since 4.2
 */
public class BeanFieldJoinStringIndex<T> extends BeanFieldJoin<T, String> {

    /**
     * Creates a new instance.
     *
     * @param type The type of the class in which this field is found. This is
     *             the type as instantiated by opencsv, and not necessarily the
     *             type in which the field is declared in the case of
     *             inheritance.
     * @param field       The bean field this object represents
     * @param required    Whether or not a value is always required for this field
     * @param errorLocale The locale to use for error messages
     * @param converter   The converter to be used for performing the data
     *                    conversion on reading or writing
     * @param mapType     The type of the
     *                    {@link org.apache.commons.collections4.MultiValuedMap} that should be
     *                    instantiated for the bean field being populated
     * @param capture     See {@link CsvBindAndJoinByName#capture()}
     * @param format      The format string used for packaging values to be written.
     *                    If {@code null} or empty, it is ignored.
     */
    public BeanFieldJoinStringIndex(
            Class<?> type, Field field, boolean required, Locale errorLocale,
            CsvConverter converter, Class<? extends MultiValuedMap> mapType,
            String capture, String format) {
        super(type, field, required, errorLocale, converter, mapType, capture, format);
    }

    @Override
    protected Object putNewValue(MultiValuedMap<String, Object> map, String index, Object newValue) {
        return map.put(index, newValue);
    }
}
