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

import java.lang.annotation.*;

/**
 * This annotation indicates that the destination field is a number that is
 * specially formatted.
 * Numbers that have no more formatting than that which comes with a locale (or
 * the default locale) do not require this annotation. If a locale is specified
 * in the attendant CSV binding annotation ({@link CsvBindByName},
 * {@link CsvBindByPosition}, etc.), it is used for the conversion. The
 * following types are supported:
 * <ul><li>byte / {@link java.lang.Byte}</li>
 * <li>double / {@link java.lang.Double}</li>
 * <li>float / {@link java.lang.Float}</li>
 * <li>int / {@link java.lang.Integer}</li>
 * <li>long / {@link java.lang.Long}</li>
 * <li>short / {@link java.lang.Short}</li>
 * <li>{@link java.math.BigDecimal}</li>
 * <li>{@link java.math.BigInteger}</li></ul>
 *
 * @since 4.2
 * @author Andrew Rucker Jones
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvNumber {

    /**
     * A decimal format string.
     * This must be a localized pattern understood by
     * {@link java.text.DecimalFormat}. The locale is gleaned from one of the
     * other CSV-related annotations if present there, or failing that, is the
     * default locale for the JVM. If your code might run under different
     * locales, you are strongly encouraged to always specify a locale for
     * conversions, otherwise your code will behave unpredictably.
     *
     * @return The format string for parsing input
     */
    String value();

    /**
     * Whether or not the same format string is used for writing as for reading.
     * If this is true, {@link #value()} is used for both reading and writing
     * and {@link #writeFormat()} is ignored.
     *
     * @return Whether the read format is used for writing as well
     * @since 5.0
     */
    boolean writeFormatEqualsReadFormat() default true;

    /**
     * A number format string.
     * The default value is blank and only exists to make sure the parameter is
     * optional.
     *
     * @return The format string for formatting output
     * @see #value()
     * @see #writeFormatEqualsReadFormat()
     * @since 5.0
     */
    String writeFormat() default "";
}
