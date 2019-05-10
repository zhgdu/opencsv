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
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * This class collects as many generally useful parts of the implementation
 * of a mapping strategy as possible.
 * Anyone is welcome to use it as a base class for their own mapping strategies.
 * 
 * @param <T> Type of object that is being processed.
 * 
 * @author Andrew Rucker Jones
 * @since 4.2
 */
abstract public class AbstractMappingStrategy<I, K extends Comparable<K>, C extends ComplexFieldMapEntry<I, K, T>, T> implements MappingStrategy<T> {
    
    /** This is the class of the bean to be manipulated. */
    protected Class<? extends T> type;
    
    /**
     * Maintains a bi-directional mapping between column position(s) and header
     * name.
     */
    protected final HeaderIndex headerIndex = new HeaderIndex();
    
    /** Locale for error messages. */
    protected Locale errorLocale = Locale.getDefault();

    /**
     * For {@link BeanField#indexAndSplitMultivaluedField(java.lang.Object, java.lang.Object)}
     * it is necessary to determine which index to pass in.
     * 
     * @param index The current column position while transmuting a bean to CSV
     *   output
     * @return The index to be used for this mapping strategy for
     *   {@link BeanField#indexAndSplitMultivaluedField(java.lang.Object, java.lang.Object) }
     */
    abstract protected Object chooseMultivaluedFieldIndexFromHeaderIndex(int index);
    
    /**
     * Returns the {@link FieldMap} associated with this mapping strategy.
     * 
     * @return The {@link FieldMap} used by this strategy
     */
    abstract protected FieldMap<I,K,? extends C,T> getFieldMap();
    
    /**
     * Builds a map of fields for the bean.
     *
     * @throws CsvBadConverterException If there is a problem instantiating the
     *                                  custom converter for an annotated field
     */
    abstract protected void loadFieldMap() throws CsvBadConverterException;

    /**
     * Gets the field for a given column position.
     *
     * @param col The column to find the field for
     * @return BeanField containing the field for a given column position, or
     * null if one could not be found
     * @throws CsvBadConverterException If a custom converter for a field cannot
     *                                  be initialized
     */
    abstract protected BeanField<T> findField(int col);

    /**
     * Must be called once the length of input for a line/record is known to
     * verify that the line was complete.
     * Complete in this context means, no required fields are missing. The issue
     * here is, as long as a column is present but empty, we can check whether
     * the field is required and throw an exception if it is not, but if the data
     * end prematurely, we never have this chance without indication that no more
     * data are on the way.
     * Another validation is that the number of fields must match the number of
     * headers to prevent a data mismatch situation.
     *
     * @param numberOfFields The number of fields present in the line of input
     * @throws CsvRequiredFieldEmptyException If a required column is missing
     * @since 4.0
     */
    abstract protected void verifyLineLength(int numberOfFields) throws CsvRequiredFieldEmptyException;
    
    /**
     * Implementation will return a bean of the type of object being mapped.
     *
     * @return A new instance of the class being mapped.
     * @throws InstantiationException Thrown on error creating object.
     * @throws IllegalAccessException Thrown on error creating object.
     * @throws IllegalStateException If the type of the bean has not been
     *   initialized through {@link #setType(java.lang.Class)}
     */
    protected T createBean() throws InstantiationException, IllegalAccessException, IllegalStateException {
        if(type == null) {
            throw new IllegalStateException(ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale).getString("type.unset"));
        }
        return type.newInstance();
    }

    /**
     * Gets the name (or position number) of the header for the given column
     * number.
     * The column numbers are zero-based.
     * 
     * @param col The column number for which the header is sought
     * @return The name of the header
     */
    abstract public String findHeader(int col);

    /**
     * Finds and returns the highest index in this mapping.
     * This is especially important for writing, since position-based mapping
     * can ignore some columns that must be included in the output anyway.
     * {@link #findField(int) } will return null for these columns, so we need
     * a way to know when to stop writing new columns.
     * @return The highest index in the mapping. If there are no columns in the
     *   mapping, returns -1.
     * @since 3.9
     */
    public int findMaxFieldIndex() {
        return headerIndex.findMaxIndex();
    }

    /**
     * This method generates a header that can be used for writing beans of the
     * type provided back to a file.
     * <p>The ordering of the headers is determined by the
     * {@link com.opencsv.bean.FieldMap} in use.</p>
     * <p>This method should be called first by all overriding classes to make
     * certain {@link #headerIndex} is properly initialized.</p>
     */
    // The rest of the Javadoc is inherited
    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        if(type == null) {
            throw new IllegalStateException(ResourceBundle
                    .getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                    .getString("type.before.header"));
        }
        
        // Always take what's been given or previously determined first.
        if(headerIndex.isEmpty()) {
            String[] header = getFieldMap().generateHeader(bean);
            headerIndex.initializeHeaderIndex(header);
            return header;
        }
        
        // Otherwise, put headers in the right places.
        return headerIndex.getHeaderIndex();
    }

    /**
     * Get the column name for a given column position.
     *
     * @param col Column position.
     * @return The column name or null if the position is larger than the
     * header array or there are no headers defined.
     */
    public String getColumnName(int col) {
        // headerIndex is never null because it's final
        return headerIndex.getByPosition(col);
    }

    /**
     * Get the class type that the Strategy is mapping.
     *
     * @return Class of the object that this {@link MappingStrategy} will create.
     */
    public Class<? extends T> getType() {
        return type;
    }

    @Override
    public T populateNewBean(String[] line)
            throws InstantiationException, IllegalAccessException,
            CsvRequiredFieldEmptyException, CsvDataTypeMismatchException,
            CsvConstraintViolationException {
        verifyLineLength(line.length);
        T bean = createBean();
        for (int col = 0; col < line.length; col++) {
                setFieldValue(bean, line[col], col);
        }
        return bean;
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
    }
    
    /**
     * Attempts to instantiate the class of the custom converter specified.
     *
     * @param converter The class for a custom converter
     * @return The custom converter
     * @throws CsvBadConverterException If the class cannot be instantiated
     */
    protected BeanField<T> instantiateCustomConverter(Class<? extends AbstractBeanField<T>> converter)
            throws CsvBadConverterException {
        try {
            BeanField<T> c = converter.newInstance();
            c.setErrorLocale(errorLocale);
            return c;
        } catch (IllegalAccessException | InstantiationException oldEx) {
            CsvBadConverterException newEx =
                    new CsvBadConverterException(converter,
                            String.format(ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale).getString("custom.converter.invalid"), converter.getCanonicalName()));
            newEx.initCause(oldEx);
            throw newEx;
        }
    }

    @Override
    public void setErrorLocale(Locale errorLocale) {
        this.errorLocale = ObjectUtils.defaultIfNull(errorLocale, Locale.getDefault());
        
        // It's very possible that setType() was called first, which creates all
        // of the BeanFields, so we need to go back through the list and correct
        // them all.
        if(getFieldMap() != null) {
            getFieldMap().setErrorLocale(this.errorLocale);
            getFieldMap().values().forEach(f -> f.setErrorLocale(this.errorLocale));
        }
    }
    
    /**
     * Populates the field corresponding to the column position indicated of the
     * bean passed in according to the rules of the mapping strategy.
     * This method performs conversion on the input string and assigns the
     * result to the proper field in the provided bean.
     *
     * @param bean  Object containing the field to be set.
     * @param value String containing the value to set the field to.
     * @param column The column position from the CSV file under which this
     *   value was found.
     * @throws CsvDataTypeMismatchException    When the result of data conversion returns
     *                                         an object that cannot be assigned to the selected field
     * @throws CsvRequiredFieldEmptyException  When a field is mandatory, but there is no
     *                                         input datum in the CSV file
     * @throws CsvConstraintViolationException When the internal structure of
     *                                         data would be violated by the data in the CSV file
     * @since 4.2
     */
    protected void setFieldValue(T bean, String value, int column)
            throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException,
            CsvConstraintViolationException {
        BeanField<T> beanField = findField(column);
        if (beanField != null) {
            beanField.setFieldValue(bean, value, findHeader(column));
        }
    }
    
    @Override
    public String[] transmuteBean(T bean) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        int numColumns = findMaxFieldIndex()+1;
        List<String> transmutedBean = writeWithReflection(bean, numColumns);
        return transmutedBean.toArray(new String[transmutedBean.size()]);
    }

    private List<String> writeWithReflection(T bean, int numColumns)
            throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        BeanField<T> firstBeanField, subsequentBeanField;
        Object firstIndex, subsequentIndex;
        List<String> contents = new ArrayList<>(numColumns > 0 ? numColumns : 0);
        
        for(int i = 0; i < numColumns;) {
            
            // Determine the first value
            firstBeanField = findField(i);
            firstIndex = chooseMultivaluedFieldIndexFromHeaderIndex(i);
            String[] fields = firstBeanField != null
                    ? firstBeanField.write(bean, firstIndex)
                    : ArrayUtils.EMPTY_STRING_ARRAY;
            
            if(fields.length == 0) {
                
                // Write the only value
                contents.add(StringUtils.EMPTY);
                i++; // Advance the index
            }
            else {
                
                // Multiple values. Write the first.
                contents.add(StringUtils.defaultString(fields[0]));
                
                // Now write the rest
                // We must make certain that we don't write more fields
                // than we have columns of the correct type to cover them
                int j = 1;
                int displacedIndex = i+j;
                subsequentBeanField = findField(displacedIndex);
                subsequentIndex = chooseMultivaluedFieldIndexFromHeaderIndex(displacedIndex);
                while(j < fields.length
                        && displacedIndex < numColumns
                        && Objects.equals(firstBeanField, subsequentBeanField)
                        && Objects.equals(firstIndex, subsequentIndex)) {
                    // This field still has a header, so add it
                    contents.add(StringUtils.defaultString(fields[j]));
                    
                    // Prepare for the next loop through
                    displacedIndex = i + (++j);
                    subsequentBeanField = findField(displacedIndex);
                    subsequentIndex = chooseMultivaluedFieldIndexFromHeaderIndex(displacedIndex);
                }
                
                i = displacedIndex; // Advance the index
                
                // And here's where we fill in any fields that are missing to
                // cover the number of columns of the same type
                if(i < numColumns) {
                    subsequentBeanField = findField(i);
                    subsequentIndex = chooseMultivaluedFieldIndexFromHeaderIndex(i);
                    while(Objects.equals(firstBeanField, subsequentBeanField)
                            && Objects.equals(firstIndex, subsequentIndex)
                            && i < numColumns) {
                        contents.add(StringUtils.EMPTY);
                        subsequentBeanField = findField(++i);
                        subsequentIndex = chooseMultivaluedFieldIndexFromHeaderIndex(i);
                    }
                }
            }
        }
        return contents;
    }
    
    /**
     * Given the information provided, determines the appropriate built-in
     * converter to be passed in to the {@link BeanField} being created.
     *
     * @param field The field of the bean type in question
     * @param elementType The type to be generated by the converter (on reading)
     * @param locale The locale for conversion. May be null or an empty string
     *               if a locale is not in use.
     * @param customConverter An optional custom converter
     * @return The appropriate converter for the necessary conversion
     * @throws CsvBadConverterException If the converter cannot be instantiated
     *
     * @since 4.2
     */
    protected CsvConverter determineConverter(Field field,
            Class<?> elementType, String locale,
            Class<? extends AbstractCsvConverter> customConverter)
            throws CsvBadConverterException {
        CsvConverter converter;

        // A custom converter always takes precedence if specified.
        if(customConverter != null && !customConverter.equals(AbstractCsvConverter.class)) {
            try {
                converter = customConverter.newInstance();
            } catch (IllegalAccessException | InstantiationException oldEx) {
                CsvBadConverterException newEx =
                        new CsvBadConverterException(customConverter,
                                String.format(ResourceBundle.getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale).getString("custom.converter.invalid"), customConverter.getCanonicalName()));
                newEx.initCause(oldEx);
                throw newEx;
            }
            converter.setType(elementType);
            converter.setLocale(locale);
            converter.setErrorLocale(errorLocale);
        }

        // Perhaps a date instead
        else if (field.isAnnotationPresent(CsvDate.class)) {
            String formatString = field.getAnnotation(CsvDate.class).value();
            converter = new ConverterDate(elementType, locale, errorLocale, formatString);
        }

        // Or a number
        else if(field.isAnnotationPresent(CsvNumber.class)) {
            String formatString = field.getAnnotation(CsvNumber.class).value();
            converter = new ConverterNumber(elementType, locale, errorLocale, formatString);
        }

        // Otherwise it must be a primitive
        else {
            converter = new ConverterPrimitiveTypes(elementType, locale, errorLocale);
        }
        return converter;
    }
}
