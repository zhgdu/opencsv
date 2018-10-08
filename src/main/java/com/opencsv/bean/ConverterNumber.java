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

import com.opencsv.ICSVParser;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This converter class is used in combination with {@link CsvNumber}, that is,
 * when number inputs and outputs should be formatted.
 *
 * @author Andrew Rucker Jones
 * @since 4.2
 */
public class ConverterNumber extends AbstractCsvConverter {

    private final DecimalFormat df;

    /**
     * @param type    The class of the type of the data being processed
     * @param locale   If not null or empty, specifies the locale used for
     *                 converting locale-specific data types
     * @param errorLocale The locale to use for error messages
     * @param formatString The string to use for formatting the number.
     * @see com.opencsv.bean.CsvNumber#value()
     */
    public ConverterNumber(Class<?> type, String locale, Locale errorLocale, String formatString)
            throws CsvBadConverterException {
        super(type, locale, errorLocale);

        // Check that the bean member is of an applicable type
        if(!Number.class.isAssignableFrom(
                this.type.isPrimitive()
                        ? ClassUtils.primitiveToWrapper(this.type)
                        : this.type)) {
            throw new CsvBadConverterException(
                    ConverterNumber.class,
                    ResourceBundle.getBundle(
                            ICSVParser.DEFAULT_BUNDLE_NAME,
                            this.errorLocale)
                            .getString("csvnumber.not.number"));
        }

        // Set up the formatter
        NumberFormat nf = NumberFormat.getInstance(ObjectUtils.defaultIfNull(this.locale, Locale.getDefault(Locale.Category.FORMAT)));
        if(!(nf instanceof DecimalFormat)) {
            throw new CsvBadConverterException(
                    ConverterNumber.class,
                    ResourceBundle.getBundle(
                            ICSVParser.DEFAULT_BUNDLE_NAME,
                            this.errorLocale)
                            .getString("numberformat.not.decimalformat"));
        }
        df = (DecimalFormat) nf;

        try {
            df.applyLocalizedPattern(formatString);
        } catch (IllegalArgumentException e) {
            CsvBadConverterException csve = new CsvBadConverterException(
                    ConverterNumber.class,
                    String.format(ResourceBundle.getBundle(
                            ICSVParser.DEFAULT_BUNDLE_NAME,
                            this.errorLocale)
                            .getString("invalid.number.pattern"),
                            formatString));
            csve.initCause(e);
            throw csve;
        }

        // Account for BigDecimal and BigInteger, which require special
        // processing
        if(this.type == BigInteger.class || this.type == BigDecimal.class) {
            df.setParseBigDecimal(true);
        }
    }

    @Override
    public Object convertToRead(String value) throws CsvDataTypeMismatchException {
        Number n;
        try {
            synchronized (df) {
                n = df.parse(value);
            }
        }
        catch(ParseException e) {
            CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(
                    value, type,
                    String.format(ResourceBundle.getBundle(
                            ICSVParser.DEFAULT_BUNDLE_NAME,
                            errorLocale)
                            .getString("unparsable.number"), value, df.toPattern()));
            csve.initCause(e);
            throw csve;
        }
        if(type == Byte.class || type == Byte.TYPE) {
            n = n.byteValue();
        }
        else if(type == Short.class || type == Short.TYPE) {
            n = n.shortValue();
        }
        else if(type == Integer.class || type == Integer.TYPE) {
            n = n.intValue();
        }
        else if(type == Long.class || type == Long.TYPE) {
            n = n.longValue();
        }
        else if(type == Float.class || type == Float.TYPE) {
            n = n.floatValue();
        }
        else if(type == Double.class || type == Double.TYPE) {
            n = n.doubleValue();
        }
        else if(type == BigInteger.class) {
            n = ((BigDecimal) n).toBigInteger();
        }
        // else: Either it's already a BigDecimal and nothing need be done,
        // or it's some derivative of java.lang.Number that we couldn't be
        // expected to know and accommodate for. In the latter case, a class
        // cast exception will be thrown later on assignment.

        return n;
    }

    /**
     *  Formats the number in question according to the pattern that has been
     *  provided.
     */
    // The rest of the Javadoc is inherited.
    @Override
    public String convertToWrite(Object value) {
        synchronized (df) {
            return value != null ? df.format(value) : null;
        }
    }
}
