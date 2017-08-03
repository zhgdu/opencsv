/*
 * Copyright 2016 Andrew Rucker Jones.
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

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.locale.LocaleConvertUtilsBean;

/**
 * This class wraps fields from the reflection API in order to handle
 * translation of primitive types and to add a "required" flag.
 *
 * @param <T> The type of the bean
 * @author Andrew Rucker Jones
 * @since 3.8
 */
public class BeanFieldPrimitiveTypes<T> extends AbstractBeanField<T> {

    private final String locale;
    
    /**
     * @param field    A {@link java.lang.reflect.Field} object
     * @param required True if the field is required to contain a value, false
     *                 if it is allowed to be null or a blank string
     * @param locale   If not null or empty, specifies the locale used for
     *                 converting locale-specific data types
     * @param errorLocale The locale to use for error messages.
     */
    public BeanFieldPrimitiveTypes(Field field, boolean required, String locale, Locale errorLocale) {
        super(field, required, errorLocale);
        this.locale = locale;
    }

    @Override
    protected Object convert(String value)
            throws CsvDataTypeMismatchException {
        Object o = null;

        if (StringUtils.isNotBlank(value)) {
            try {
                if(StringUtils.isEmpty(locale)) {
                    ConvertUtilsBean converter = new ConvertUtilsBean();
                    converter.register(true, false, 0);
                    o = converter.convert(value, field.getType());
                }
                else {
                    LocaleConvertUtilsBean lcub = new LocaleConvertUtilsBean();
                    lcub.setDefaultLocale(new Locale(locale));
                    o = lcub.convert(value, field.getType());
                }
            } catch (ConversionException e) {
                CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(
                        value, field.getType(),
                        String.format(
                                ResourceBundle.getBundle("opencsv", errorLocale).getString("conversion.impossible"),
                                value, field.getType().getCanonicalName()));
                csve.initCause(e);
                throw csve;
            }
        }
        return o;
    }
    
    /**
     * This method takes the current value of the field in question in the bean
     * passed in and converts it to a string.
     * It works for all of the primitives, wrapped primitives,
     * {@link java.lang.String}, {@link java.math.BigDecimal}, and
     * {@link java.math.BigInteger}.
     * 
     * @throws CsvDataTypeMismatchException If there is an error converting
     *   value to a string
     */
    // The rest of the Javadoc is automatically inherited from the base class.
    @Override
    protected String convertToWrite(Object value)
            throws CsvDataTypeMismatchException {
        String result = null;
        if(value != null) {
            try {
                if(StringUtils.isEmpty(locale)) {
                    ConvertUtilsBean converter = new ConvertUtilsBean();
                    result = converter.convert(value);
                }
                else {
                    LocaleConvertUtilsBean converter = new LocaleConvertUtilsBean();
                    converter.setDefaultLocale(new Locale(locale));
                    result = converter.convert(value);
                }
            }
            catch(ConversionException e) {
                CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(
                        ResourceBundle.getBundle("opencsv", errorLocale).getString("field.not.primitive"));
                csve.initCause(e);
                throw csve;
            }
        }
        return result;
    }
}
