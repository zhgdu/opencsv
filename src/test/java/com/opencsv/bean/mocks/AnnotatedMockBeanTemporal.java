package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.TemporalTest;

import java.time.*;
import java.time.chrono.*;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;

/**
 * A test class that should provide full coverage of
 * {@link java.time.temporal.TemporalAccessor}-based types.
 *
 * @see AnnotatedMockBeanFull
 * @author Andrew Rucker Jones
 */
public class AnnotatedMockBeanTemporal {

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * <li>Setting an explicit format string but leaving
     * {@link CsvDate#writeFormatEqualsReadFormat()} {@code true} for dates</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 0)
    @CsvDate(
            value = "GGGG yyyy MMMM dd HH mm ss z",
            writeFormat = "GGGG") // Will be ignored
    private TemporalAccessor temporalAccessor;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 1, locale = "de")
    @CsvDate("GGGG yyyy MMMM dd HH mm ss z")
    private TemporalAccessor temporalAccessorLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 2)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G yyyy MMMM dd")
    private ChronoLocalDate chronoLocalDate;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 3, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G yyyy MMMM dd")
    private ChronoLocalDate chronoLocalDateLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 4)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G yyyy MMMM dd")
    private LocalDate localDate;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 5, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G yyyy MMMM dd")
    private LocalDate localDateLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 6)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G yyyy MMMM dd HH mm ss")
    private ChronoLocalDateTime chronoLocalDateTime;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 7, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G yyyy MMMM dd HH mm ss")
    private ChronoLocalDateTime chronoLocalDateTimeLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 8)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G yyyy MMMM dd HH mm ss")
    private LocalDateTime localDateTime;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 9, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G yyyy MMMM dd HH mm ss")
    private LocalDateTime localDateTimeLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 10)
    @CsvDate("G yyyy MMMM dd HH mm ss z")
    private ChronoZonedDateTime chronoZonedDateTime;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 11, locale = "de")
    @CsvDate("G yyyy MMMM dd HH mm ss z")
    private ChronoZonedDateTime chronoZonedDateTimeLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * <li>{@link TemporalTest#testBeanInputDoesNotMatchFormatString()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 12)
    @CsvDate("G yyyy MMMM dd HH mm ss z")
    private Temporal temporal;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 13, locale = "de")
    @CsvDate("G yyyy MMMM dd HH mm ss z")
    private Temporal temporalLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 14)
    @CsvDate(
            value= "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G")
    private Era era;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 15, locale = "de")
    @CsvDate(
            value= "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G")
    private Era eraLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 16)
    @CsvDate(
            value= "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G")
    private IsoEra isoEra;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 17, locale = "de")
    @CsvDate(
            value= "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G")
    private IsoEra isoEraLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 18)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "E")
    private DayOfWeek dayOfWeek;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 19, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "E")
    private DayOfWeek dayOfWeekLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 20)
    @CsvDate(value = "G yyyy MMMM dd", chronology = "Hijrah-umalqura")
    private HijrahDate hijrahDate;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 21, locale = "de")
    @CsvDate(value = "G yyyy MMMM dd", chronology = "Hijrah-umalqura")
    private HijrahDate hijrahDateLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 22)
    @CsvDate(
            value = "G yyyy MMMM dd",
            chronology = "Hijrah-umalqura",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G",
            writeChronologyEqualsReadChronology = false)
    private HijrahEra hijrahEra;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 23, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd",
            chronology = "Hijrah-umalqura",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G",
            writeChronologyEqualsReadChronology = false)
    private HijrahEra hijrahEraLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 24)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "uuuu MMMM dd HH mm ss")
    private Instant instant;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * <li>Deriving a chronology from a locale that does not specify a chronology</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "ja")
    @CsvBindByPosition(position = 25, locale = "ja")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            chronology = "",
            writeFormatEqualsReadFormat = false,
            writeFormat = "uuuu MMMM dd HH mm ss")
    private Instant instantLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 26)
    @CsvDate(value = "G yy MMMM dd", chronology = "Japanese")
    private JapaneseDate japaneseDate;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * <li>Deriving a chronology from a locale that specifies a chronology, reading</li>
     * <li>Deriving a chronology from a locale that specifies a chronology, writing</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de-JP-u-ca-japanese")
    @CsvBindByPosition(position = 27, locale = "de-JP-u-ca-japanese")
    @CsvDate(
            value = "G yy MMMM dd",
            chronology = " ",
            writeChronologyEqualsReadChronology = false,
            writeChronology = " ")
    private JapaneseDate japaneseDateLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 28)
    @CsvDate(
            value = "G yy MMMM dd",
            chronology = "Japanese",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G",
            writeChronologyEqualsReadChronology = false)
    private JapaneseEra japaneseEra;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 29, locale = "de")
    @CsvDate(
            value = "G yy MMMM dd",
            chronology = "Japanese",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G",
            writeChronologyEqualsReadChronology = false)
    private JapaneseEra japaneseEraLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 30)
    @CsvDate(
            value= "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "HH mm ss")
    private LocalTime localTime;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 31, locale = "de")
    @CsvDate(
            value= "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "HH mm ss")
    private LocalTime localTimeLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 32)
    @CsvDate(value = "G yy MMMM dd", chronology = "Minguo")
    private MinguoDate minguoDate;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 33, locale = "de")
    @CsvDate(value = "G yy MMMM dd", chronology = "Minguo")
    private MinguoDate minguoDateLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 34)
    @CsvDate(
            value = "G yy MMMM dd",
            chronology = "Minguo",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G",
            writeChronologyEqualsReadChronology = false)
    private MinguoEra minguoEra;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 35, locale = "de")
    @CsvDate(
            value = "G yy MMMM dd",
            chronology = "Minguo",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G",
            writeChronologyEqualsReadChronology = false)
    private MinguoEra minguoEraLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 36)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "MMMM")
    private Month month;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 37, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "MMMM")
    private Month monthLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 38)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "dd")
    private MonthDay monthDay;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 39, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "dd")
    private MonthDay monthDayLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 40)
    @CsvDate("uuuu-MMMM-dd'T'HH:mm:ssxxx")
    private OffsetDateTime offsetDateTime;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 41, locale = "de")
    @CsvDate("uuuu-MMMM-dd'T'HH:mm:ssxxx")
    private OffsetDateTime offsetDateTimeLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 42)
    @CsvDate(
            value = "yyyy-MMMM-dd'T'HH:mm:ssxxx",
            writeFormatEqualsReadFormat = false,
            writeFormat = "HH:mm:ssxxx")
    private OffsetTime offsetTime;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 43, locale = "de")
    @CsvDate(
            value = "yyyy-MMMM-dd'T'HH:mm:ssxxx",
            writeFormatEqualsReadFormat = false,
            writeFormat = "HH:mm:ssxxx")
    private OffsetTime offsetTimeLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 44)
    @CsvDate(value = "G yyyy MMMM dd", chronology = "ThaiBuddhist")
    private ThaiBuddhistDate thaiBuddhistDate;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 45, locale = "de")
    @CsvDate(value = "G yyyy MMMM dd", chronology = "ThaiBuddhist")
    private ThaiBuddhistDate thaiBuddhistDateLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 46)
    @CsvDate(
            value = "G yyyy MMMM dd",
            chronology = "ThaiBuddhist",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G",
            writeChronologyEqualsReadChronology = false)
    private ThaiBuddhistEra thaiBuddhistEra;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 47, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd",
            chronology = "ThaiBuddhist",
            writeFormatEqualsReadFormat = false,
            writeFormat = "G",
            writeChronologyEqualsReadChronology = false)
    private ThaiBuddhistEra thaiBuddhistEraLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 48)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "yyyy")
    private Year year;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 49, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "yyyy")
    private Year yearLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 50)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "yyyy MMMM")
    private YearMonth yearMonth;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 51, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss z",
            writeFormatEqualsReadFormat = false,
            writeFormat = "yyyy MMMM")
    private YearMonth yearMonthLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 52)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss xxx",
            writeFormatEqualsReadFormat = false,
            writeFormat = "xxx")
    private ZoneOffset zoneOffset;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 53, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss xxx",
            writeFormatEqualsReadFormat = false,
            writeFormat = "xxx")
    private ZoneOffset zoneOffsetLocale;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName
    @CsvBindByPosition(position = 54)
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss X",
            writeChronologyEqualsReadChronology = false,
            writeChronology = "Japanese")
    private ZonedDateTime zonedDateTime;

    /**
     * Field for {@link java.time.temporal.TemporalAccessor}-based annotation
     * tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>{@link TemporalTest#testReadGoodDataByName()}</li>
     * <li>{@link TemporalTest#testReadGoodDataByPosition()} ()}</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>{@link TemporalTest#testWriteGoodDataByName()}</li>
     * <li>{@link TemporalTest#testWriteGoodDataByPosition()}</li>
     * </ul></p>
     */
    @CsvBindByName(locale = "de")
    @CsvBindByPosition(position = 55, locale = "de")
    @CsvDate(
            value = "G yyyy MMMM dd HH mm ss X",
            writeChronology = "Japanese") // Will not be used
    private ZonedDateTime zonedDateTimeLocale;

    public TemporalAccessor getTemporalAccessor() {
        return temporalAccessor;
    }

    public void setTemporalAccessor(TemporalAccessor temporalAccessor) {
        this.temporalAccessor = temporalAccessor;
    }

    public TemporalAccessor getTemporalAccessorLocale() {
        return temporalAccessorLocale;
    }

    public void setTemporalAccessorLocale(TemporalAccessor temporalAccessorLocale) {
        this.temporalAccessorLocale = temporalAccessorLocale;
    }

    public ChronoLocalDate getChronoLocalDate() {
        return chronoLocalDate;
    }

    public void setChronoLocalDate(ChronoLocalDate chronoLocalDate) {
        this.chronoLocalDate = chronoLocalDate;
    }

    public ChronoLocalDate getChronoLocalDateLocale() {
        return chronoLocalDateLocale;
    }

    public void setChronoLocalDateLocale(ChronoLocalDate chronoLocalDateLocale) {
        this.chronoLocalDateLocale = chronoLocalDateLocale;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDate getLocalDateLocale() {
        return localDateLocale;
    }

    public void setLocalDateLocale(LocalDate localDateLocale) {
        this.localDateLocale = localDateLocale;
    }

    public ChronoLocalDateTime getChronoLocalDateTime() {
        return chronoLocalDateTime;
    }

    public void setChronoLocalDateTime(ChronoLocalDateTime chronoLocalDateTime) {
        this.chronoLocalDateTime = chronoLocalDateTime;
    }

    public ChronoLocalDateTime getChronoLocalDateTimeLocale() {
        return chronoLocalDateTimeLocale;
    }

    public void setChronoLocalDateTimeLocale(ChronoLocalDateTime chronoLocalDateTimeLocale) {
        this.chronoLocalDateTimeLocale = chronoLocalDateTimeLocale;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTimeLocale() {
        return localDateTimeLocale;
    }

    public void setLocalDateTimeLocale(LocalDateTime localDateTimeLocale) {
        this.localDateTimeLocale = localDateTimeLocale;
    }

    public ChronoZonedDateTime getChronoZonedDateTime() {
        return chronoZonedDateTime;
    }

    public void setChronoZonedDateTime(ChronoZonedDateTime chronoZonedDateTime) {
        this.chronoZonedDateTime = chronoZonedDateTime;
    }

    public ChronoZonedDateTime getChronoZonedDateTimeLocale() {
        return chronoZonedDateTimeLocale;
    }

    public void setChronoZonedDateTimeLocale(ChronoZonedDateTime chronoZonedDateTimeLocale) {
        this.chronoZonedDateTimeLocale = chronoZonedDateTimeLocale;
    }

    public Temporal getTemporal() {
        return temporal;
    }

    public void setTemporal(Temporal temporal) {
        this.temporal = temporal;
    }

    public Temporal getTemporalLocale() {
        return temporalLocale;
    }

    public void setTemporalLocale(Temporal temporalLocale) {
        this.temporalLocale = temporalLocale;
    }

    public Era getEra() {
        return era;
    }

    public void setEra(Era era) {
        this.era = era;
    }

    public Era getEraLocale() {
        return eraLocale;
    }

    public void setEraLocale(Era eraLocale) {
        this.eraLocale = eraLocale;
    }

    public IsoEra getIsoEra() {
        return isoEra;
    }

    public void setIsoEra(IsoEra isoEra) {
        this.isoEra = isoEra;
    }

    public IsoEra getIsoEraLocale() {
        return isoEraLocale;
    }

    public void setIsoEraLocale(IsoEra isoEraLocale) {
        this.isoEraLocale = isoEraLocale;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek getDayOfWeekLocale() {
        return dayOfWeekLocale;
    }

    public void setDayOfWeekLocale(DayOfWeek dayOfWeekLocale) {
        this.dayOfWeekLocale = dayOfWeekLocale;
    }

    public HijrahDate getHijrahDate() {
        return hijrahDate;
    }

    public void setHijrahDate(HijrahDate hijrahDate) {
        this.hijrahDate = hijrahDate;
    }

    public HijrahDate getHijrahDateLocale() {
        return hijrahDateLocale;
    }

    public void setHijrahDateLocale(HijrahDate hijrahDateLocale) {
        this.hijrahDateLocale = hijrahDateLocale;
    }

    public HijrahEra getHijrahEra() {
        return hijrahEra;
    }

    public void setHijrahEra(HijrahEra hijrahEra) {
        this.hijrahEra = hijrahEra;
    }

    public HijrahEra getHijrahEraLocale() {
        return hijrahEraLocale;
    }

    public void setHijrahEraLocale(HijrahEra hijrahEraLocale) {
        this.hijrahEraLocale = hijrahEraLocale;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public Instant getInstantLocale() {
        return instantLocale;
    }

    public void setInstantLocale(Instant instantLocale) {
        this.instantLocale = instantLocale;
    }

    public JapaneseDate getJapaneseDate() {
        return japaneseDate;
    }

    public void setJapaneseDate(JapaneseDate japaneseDate) {
        this.japaneseDate = japaneseDate;
    }

    public JapaneseDate getJapaneseDateLocale() {
        return japaneseDateLocale;
    }

    public void setJapaneseDateLocale(JapaneseDate japaneseDateLocale) {
        this.japaneseDateLocale = japaneseDateLocale;
    }

    public JapaneseEra getJapaneseEra() {
        return japaneseEra;
    }

    public void setJapaneseEra(JapaneseEra japaneseEra) {
        this.japaneseEra = japaneseEra;
    }

    public JapaneseEra getJapaneseEraLocale() {
        return japaneseEraLocale;
    }

    public void setJapaneseEraLocale(JapaneseEra japaneseEraLocale) {
        this.japaneseEraLocale = japaneseEraLocale;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public LocalTime getLocalTimeLocale() {
        return localTimeLocale;
    }

    public void setLocalTimeLocale(LocalTime localTimeLocale) {
        this.localTimeLocale = localTimeLocale;
    }

    public MinguoDate getMinguoDate() {
        return minguoDate;
    }

    public void setMinguoDate(MinguoDate minguoDate) {
        this.minguoDate = minguoDate;
    }

    public MinguoDate getMinguoDateLocale() {
        return minguoDateLocale;
    }

    public void setMinguoDateLocale(MinguoDate minguoDateLocale) {
        this.minguoDateLocale = minguoDateLocale;
    }

    public MinguoEra getMinguoEra() {
        return minguoEra;
    }

    public void setMinguoEra(MinguoEra minguoEra) {
        this.minguoEra = minguoEra;
    }

    public MinguoEra getMinguoEraLocale() {
        return minguoEraLocale;
    }

    public void setMinguoEraLocale(MinguoEra minguoEraLocale) {
        this.minguoEraLocale = minguoEraLocale;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Month getMonthLocale() {
        return monthLocale;
    }

    public void setMonthLocale(Month monthLocale) {
        this.monthLocale = monthLocale;
    }

    public MonthDay getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(MonthDay monthDay) {
        this.monthDay = monthDay;
    }

    public MonthDay getMonthDayLocale() {
        return monthDayLocale;
    }

    public void setMonthDayLocale(MonthDay monthDayLocale) {
        this.monthDayLocale = monthDayLocale;
    }

    public OffsetDateTime getOffsetDateTime() {
        return offsetDateTime;
    }

    public void setOffsetDateTime(OffsetDateTime offsetDateTime) {
        this.offsetDateTime = offsetDateTime;
    }

    public OffsetDateTime getOffsetDateTimeLocale() {
        return offsetDateTimeLocale;
    }

    public void setOffsetDateTimeLocale(OffsetDateTime offsetDateTimeLocale) {
        this.offsetDateTimeLocale = offsetDateTimeLocale;
    }

    public OffsetTime getOffsetTime() {
        return offsetTime;
    }

    public void setOffsetTime(OffsetTime offsetTime) {
        this.offsetTime = offsetTime;
    }

    public OffsetTime getOffsetTimeLocale() {
        return offsetTimeLocale;
    }

    public void setOffsetTimeLocale(OffsetTime offsetTimeLocale) {
        this.offsetTimeLocale = offsetTimeLocale;
    }

    public ThaiBuddhistDate getThaiBuddhistDate() {
        return thaiBuddhistDate;
    }

    public void setThaiBuddhistDate(ThaiBuddhistDate thaiBuddhistDate) {
        this.thaiBuddhistDate = thaiBuddhistDate;
    }

    public ThaiBuddhistDate getThaiBuddhistDateLocale() {
        return thaiBuddhistDateLocale;
    }

    public void setThaiBuddhistDateLocale(ThaiBuddhistDate thaiBuddhistDateLocale) {
        this.thaiBuddhistDateLocale = thaiBuddhistDateLocale;
    }

    public ThaiBuddhistEra getThaiBuddhistEra() {
        return thaiBuddhistEra;
    }

    public void setThaiBuddhistEra(ThaiBuddhistEra thaiBuddhistEra) {
        this.thaiBuddhistEra = thaiBuddhistEra;
    }

    public ThaiBuddhistEra getThaiBuddhistEraLocale() {
        return thaiBuddhistEraLocale;
    }

    public void setThaiBuddhistEraLocale(ThaiBuddhistEra thaiBuddhistEraLocale) {
        this.thaiBuddhistEraLocale = thaiBuddhistEraLocale;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Year getYearLocale() {
        return yearLocale;
    }

    public void setYearLocale(Year yearLocale) {
        this.yearLocale = yearLocale;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    public YearMonth getYearMonthLocale() {
        return yearMonthLocale;
    }

    public void setYearMonthLocale(YearMonth yearMonthLocale) {
        this.yearMonthLocale = yearMonthLocale;
    }

    public ZoneOffset getZoneOffset() {
        return zoneOffset;
    }

    public void setZoneOffset(ZoneOffset zoneOffset) {
        this.zoneOffset = zoneOffset;
    }

    public ZoneOffset getZoneOffsetLocale() {
        return zoneOffsetLocale;
    }

    public void setZoneOffsetLocale(ZoneOffset zoneOffsetLocale) {
        this.zoneOffsetLocale = zoneOffsetLocale;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    public ZonedDateTime getZonedDateTimeLocale() {
        return zonedDateTimeLocale;
    }

    public void setZonedDateTimeLocale(ZonedDateTime zonedDateTimeLocale) {
        this.zonedDateTimeLocale = zonedDateTimeLocale;
    }
}
