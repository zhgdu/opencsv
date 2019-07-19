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

import com.opencsv.ICSVParser;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.BiFunction;

/**
 * This class converts an input to a date type.
 * <p>This class should work with any type derived from {@link java.util.Date}
 * as long as it has a constructor taking one long that specifies the number
 * of milliseconds since the epoch. The following types are explicitly
 * supported:
 * <ul><li>java.util.Date</li>
 * <li>java.sql.Date</li>
 * <li>java.sql.Time</li>
 * <li>java.sql.Timestamp</li></ul></p>
 * <p>This class should work for any type that implements
 * {@link java.util.Calendar} or is derived from
 * {@link javax.xml.datatype.XMLGregorianCalendar}. The following types are
 * explicitly supported:
 * <ul><li>Calendar (always a GregorianCalendar)</li>
 * <li>GregorianCalendar</li>
 * <li>XMLGregorianCalendar</li></ul>
 * It is also known to work with
 * org.apache.xerces.jaxp.datatype.XMLGregorianCalendarImpl.</p>
 * <p>This class works for all types from the JDK that implement
 * {@link java.time.temporal.TemporalAccessor}.</p>
 *
 *
 * @author Andrew Rucker Jones
 * @since 4.2 (previously BeanFieldDate since 3.8)
 * @see com.opencsv.bean.CsvDate
 */
public class ConverterDate extends AbstractCsvConverter {

    private static final String CSVDATE_NOT_DATE = "csvdate.not.date";

    /**
     * The formatter for all inputs to old-style date representations.
     * <em>It is absolutely critical that access to this member variable is always
     * synchronized!</em>
     */
    private final SimpleDateFormat readSdf;

    /**
     * The formatter for all outputs from old-style date representations.
     * <em>It is absolutely critical that access to this member variable is always
     * synchronized!</em>
     */
    private final SimpleDateFormat writeSdf;

    /**
     * The formatter for all inputs to
     * {@link java.time.temporal.TemporalAccessor} representations.
     */
    private final DateTimeFormatter readDtf;

    /**
     * The formatter for all outputs from
     * {@link java.time.temporal.TemporalAccessor} representations.
     */
    private final DateTimeFormatter writeDtf;

    /**
     * A reference to the function to use when converting from strings to
     * {@link java.time.temporal.TemporalAccessor}-based values.
     */
    private final BiFunction<DateTimeFormatter, String, TemporalAccessor> readTemporalConversionFunction;

    /**
     * A reference to the function to use when converting from
     * {@link java.time.temporal.TemporalAccessor}-based values to strings.
     */
    private final BiFunction<DateTimeFormatter, TemporalAccessor, String> writeTemporalConversionFunction;

    /**
     * Initializes the class.
     * This includes initializing the locales for reading and writing, the
     * format strings for reading and writing, and the chronologies for
     * reading and writing, all as necessary based on the type to be converted.
     *
     * @param type         The type of the field being populated
     * @param readFormat The string to use for parsing the date. See
     *                     {@link com.opencsv.bean.CsvDate#value()}
     * @param writeFormat The string to use for formatting the date. See
     *                     {@link CsvDate#writeFormat()}
     * @param locale       If not null or empty, specifies the locale used for
     *                     converting locale-specific data types
     * @param writeLocale   If not null or empty, specifies the locale used for
     *                 converting locale-specific data types for writing
     * @param errorLocale The locale to use for error messages
     * @param readChronology The {@link java.time.chrono.Chronology} to be used
     *                       for reading if
     *                   {@link java.time.temporal.TemporalAccessor}-based
     *                   fields are in use
     * @param writeChronology The {@link java.time.chrono.Chronology} to be
     *                        used for writing if
     *                   {@link java.time.temporal.TemporalAccessor}-based
     *                   fields are in use
     */
    public ConverterDate(Class<?> type, String locale, String writeLocale, Locale errorLocale, String readFormat, String writeFormat, String readChronology, String writeChronology) {
        super(type, locale, writeLocale, errorLocale);

        // Chronology
        Chronology readChrono, writeChrono;
        try {
            readChrono = StringUtils.isNotBlank(readChronology) ?
                    Chronology.of(readChronology) :
                    Chronology.ofLocale(this.locale);
        }
        catch(DateTimeException e) {
            CsvBadConverterException csve = new CsvBadConverterException(getClass(),
                    String.format(ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, this.errorLocale)
                            .getString("chronology.not.found"), readChronology));
            csve.initCause(e);
            throw csve;
        }
        try {
            writeChrono = StringUtils.isNotBlank(writeChronology) ?
                    Chronology.of(writeChronology) :
                    Chronology.ofLocale(this.writeLocale);
        }
        catch(DateTimeException e) {
            CsvBadConverterException csve = new CsvBadConverterException(getClass(),
                    String.format(ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, this.errorLocale)
                            .getString("chronology.not.found"), writeChronology));
            csve.initCause(e);
            throw csve;
        }

        // Format string, locale, and conversion function for reading
        try {
            if(TemporalAccessor.class.isAssignableFrom(type)) {
                readSdf = null;
                DateTimeFormatter dtfWithoutChronology;
                if(this.locale != null) {
                    dtfWithoutChronology = DateTimeFormatter.ofPattern(readFormat, this.locale);
                }
                else {
                    dtfWithoutChronology = DateTimeFormatter.ofPattern(readFormat);
                }
                readDtf = dtfWithoutChronology.withChronology(readChrono);

                if(TemporalAccessor.class.equals(type)) {
                    readTemporalConversionFunction = DateTimeFormatter::parse;
                }
                else if(ChronoLocalDate.class.equals(type)
                        || LocalDate.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, LocalDate::from);
                }
                else if(ChronoLocalDateTime.class.equals(type)
                        || LocalDateTime.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, LocalDateTime::from);
                }
                else if(ChronoZonedDateTime.class.equals(type)
                        || ZonedDateTime.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, ZonedDateTime::from);
                }
                else if(Temporal.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parseBest(s, ZonedDateTime::from,
                            OffsetDateTime::from, Instant::from,
                            LocalDateTime::from, LocalDate::from, OffsetTime::from,
                            LocalTime::from);
                }
                else if (Era.class.equals(type) || IsoEra.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> IsoEra.of(readDtf.parse(s).get(ChronoField.ERA));
                }
                else if(DayOfWeek.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, DayOfWeek::from);
                }
                else if(HijrahDate.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, HijrahDate::from);
                }
                else if(HijrahEra.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> HijrahEra.of(readDtf.parse(s).get(ChronoField.ERA));
                }
                else if(Instant.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, Instant::from);
                }
                else if(JapaneseDate.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, JapaneseDate::from);
                }
                else if(JapaneseEra.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> JapaneseEra.of(readDtf.parse(s).get(ChronoField.ERA));
                }
                else if(LocalTime.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, LocalTime::from);
                }
                else if(MinguoDate.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, MinguoDate::from);
                }
                else if(MinguoEra.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> MinguoEra.of(readDtf.parse(s).get(ChronoField.ERA));
                }
                else if(Month.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, Month::from);
                }
                else if(MonthDay.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, MonthDay::from);
                }
                else if(OffsetDateTime.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, OffsetDateTime::from);
                }
                else if(OffsetTime.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, OffsetTime::from);
                }
                else if(ThaiBuddhistDate.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, ThaiBuddhistDate::from);
                }
                else if(ThaiBuddhistEra.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> ThaiBuddhistEra.of(readDtf.parse(s).get(ChronoField.ERA));
                }
                else if(Year.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, Year::from);
                }
                else if(YearMonth.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, YearMonth::from);
                }
                else if(ZoneOffset.class.equals(type)) {
                    readTemporalConversionFunction = (readDtf, s) -> readDtf.parse(s, ZoneOffset::from);
                }
                else {
                    throw new CsvBadConverterException(getClass(),String.format(
                            ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, this.errorLocale)
                                    .getString(CSVDATE_NOT_DATE), type));
                }

            }
            else {
                readDtf = null;
                readTemporalConversionFunction = null;
                if (this.locale != null) {
                    readSdf = new SimpleDateFormat(readFormat, this.locale);
                } else {
                    readSdf = new SimpleDateFormat(readFormat);
                }
            }
        } catch (IllegalArgumentException e) {
            CsvBadConverterException csve = new CsvBadConverterException(getClass(), String.format(
                    ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, this.errorLocale)
                            .getString("invalid.date.format.string"), readFormat));
            csve.initCause(e);
            throw csve;
        }

        // Format string, locale, and conversion function for writing
        try {
            if(TemporalAccessor.class.isAssignableFrom(type)) {
                writeSdf = null;
                DateTimeFormatter dtfWithoutChronology;
                if(this.writeLocale != null) {
                    dtfWithoutChronology = DateTimeFormatter.ofPattern(writeFormat, this.writeLocale);
                }
                else {
                    dtfWithoutChronology = DateTimeFormatter.ofPattern(writeFormat);
                }
                writeDtf = dtfWithoutChronology.withChronology(writeChrono);

                if(Instant.class.equals(type)) {
                    writeTemporalConversionFunction = (writeDtf, value) -> {
                        LocalDateTime ldt = LocalDateTime.ofInstant((Instant)value, ZoneId.of("UTC"));
                        return writeDtf.format(ldt);
                    };
                }
                else {
                    writeTemporalConversionFunction = (writeDtf, value) -> writeDtf.format((TemporalAccessor)value);
                }
            }
            else {
                writeDtf = null;
                writeTemporalConversionFunction = null;
                if (this.writeLocale != null) {
                    writeSdf = new SimpleDateFormat(writeFormat, this.writeLocale);
                } else {
                    writeSdf = new SimpleDateFormat(writeFormat);
                }
            }
        } catch (IllegalArgumentException e) {
            CsvBadConverterException csve = new CsvBadConverterException(getClass(), String.format(
                    ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, this.errorLocale)
                            .getString("invalid.date.format.string"), writeFormat));
            csve.initCause(e);
            throw csve;
        }
    }
    
    @Override
    public Object convertToRead(String value) throws CsvDataTypeMismatchException {
        Object returnValue = null;
        if(StringUtils.isNotBlank(value)) {

            // Convert Date-based types
            if (Date.class.isAssignableFrom(type)) {
                Date d;
                try {
                    synchronized (readSdf) {
                        d = readSdf.parse(value);
                    }

                    returnValue = type.getConstructor(Long.TYPE).newInstance(d.getTime());
                }
                // I would have preferred a CsvBeanIntrospectionException, but that
                // would have broken backward compatibility. This is not completely
                // illogical: I know all of the data types I expect here, and they
                // should all be instantiated with no problems. Ergo, this must be
                // the wrong data type.
                catch(ParseException | InstantiationException
                        | IllegalAccessException | NoSuchMethodException
                        | InvocationTargetException e) {
                    CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(value, type);
                    csve.initCause(e);
                    throw csve;
                }
            // Convert TemporalAccessor-based types
            } else if (TemporalAccessor.class.isAssignableFrom(type)) {
                try {
                    returnValue = type.cast(readTemporalConversionFunction.apply(readDtf, value));
                } catch (DateTimeException | ArithmeticException e) {
                    CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(value, type);
                    csve.initCause(e);
                    throw csve;
                }
            // Convert Calendar-based types
            } else if (Calendar.class.isAssignableFrom(type)
                    || XMLGregorianCalendar.class.isAssignableFrom(type)) {
                // Parse input
                Date d;
                try {
                    synchronized (readSdf) {
                        d = readSdf.parse(value);
                    }
                } catch (ParseException e) {
                    CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(value, type);
                    csve.initCause(e);
                    throw csve;
                }

                // Make a GregorianCalendar out of it, because this works for all
                // supported types, at least as an intermediate step.
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(d);

                // XMLGregorianCalendar requires special processing.
                if (type == XMLGregorianCalendar.class) {
                    try {
                        returnValue = type.cast(DatatypeFactory
                                .newInstance()
                                .newXMLGregorianCalendar(gc));
                    } catch (DatatypeConfigurationException e) {
                        // I've never known how to handle this exception elegantly,
                        // especially since I can't conceive of the circumstances
                        // under which it is thrown.
                        CsvDataTypeMismatchException ex = new CsvDataTypeMismatchException(
                                ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                                        .getString("xmlgregoriancalendar.impossible"));
                        ex.initCause(e);
                        throw ex;
                    }
                }
                else {
                    returnValue = type.cast(gc);
                }
            } else {
                throw new CsvDataTypeMismatchException(value, type, String.format(
                        ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale).getString(CSVDATE_NOT_DATE), type));
            }
        }

        return returnValue;
    }

    /**
     * This method converts the encapsulated date type to a string, respecting
     * any locales and conversion patterns that have been set through opencsv
     * annotations.
     * 
     * @param value The object containing a date of one of the supported types
     * @return A string representation of the date. If a
     *   {@link CsvBindByName#locale() locale} or {@link CsvDate#value() conversion
     *   pattern} has been specified through annotations, these are used when
     *   creating the return value.
     * @throws CsvDataTypeMismatchException If an unsupported type as been
     *   improperly annotated
     */
    @Override
    public String convertToWrite(Object value)
            throws CsvDataTypeMismatchException {
        String returnValue = null;
        if(value != null) {

            // For Date-based conversions
            if (Date.class.isAssignableFrom(type)) {
                synchronized (writeSdf) {
                    returnValue = writeSdf.format((Date)value);
                }
            // For TemporalAccessor-based conversions
            } else if (TemporalAccessor.class.isAssignableFrom(type)) {
                try {
                    returnValue = writeTemporalConversionFunction.apply(writeDtf, (TemporalAccessor)value);
                } catch (DateTimeException | ArithmeticException e) {
                    CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(value, type);
                    csve.initCause(e);
                    throw csve;
                }
            // For Calendar-based conversions
            } else if (Calendar.class.isAssignableFrom(type)
                    || XMLGregorianCalendar.class.isAssignableFrom(type)) {
                Calendar c;
                if(value instanceof XMLGregorianCalendar) {
                    c = ((XMLGregorianCalendar)value).toGregorianCalendar();
                }
                else {
                    c = (Calendar)value;
                }
                synchronized (writeSdf) {
                    returnValue = writeSdf.format(c.getTime());
                }
            } else {
                throw new CsvDataTypeMismatchException(value, type, String.format(
                        ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale).getString(CSVDATE_NOT_DATE), type));
            }
        }
        return returnValue;
    }
}
