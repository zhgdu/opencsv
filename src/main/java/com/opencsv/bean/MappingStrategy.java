package com.opencsv.bean;

/*
 Copyright 2007 Kyle Miller.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Locale;

/**
 * The interface for the classes that handle translating between the columns in
 * the CSV file to an actual object.
 * <p>Any implementing class <em>must</em> be thread-safe. Specifically, the
 * following methods must be thread-safe:</p>
 * <ul><li>{@link #createBean()}</li>
 * <li>{@link #findDescriptor(int)}</li>
 * <li>{@link #findField(int)}</li>
 * <li>{@link #findMaxFieldIndex()}</li>
 * <li>{@link #isAnnotationDriven()}</li>
 * <li>{@link #verifyLineLength(int)}</li></ul>
 *
 * @param <T> Type of object you are converting the data to.
 */
public interface MappingStrategy<T> {

    /**
     * Gets the property descriptor for a given column position.
     *
     * @param col The column to find the description for
     * @return The property descriptor for the column position or null if one
     * could not be found.
     * @deprecated Introspection will be replaced with reflection in version 5.0
     */
    @Deprecated
   PropertyDescriptor findDescriptor(int col);

    /**
     * Gets the field for a given column position.
     *
     * @param col The column to find the field for
     * @return BeanField containing the field for a given column position, or
     * null if one could not be found
     * @throws CsvBadConverterException If a custom converter for a field cannot
     *                                  be initialized
     */
    BeanField findField(int col) throws CsvBadConverterException;
    
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
    int findMaxFieldIndex();

    /**
     * Implementation will return a bean of the type of object you are mapping.
     *
     * @return A new instance of the class being mapped.
     * @throws InstantiationException Thrown on error creating object.
     * @throws IllegalAccessException Thrown on error creating object.
     */
    T createBean() throws InstantiationException, IllegalAccessException;

    /**
     * Implementation of this method can grab the header line before parsing
     * begins to use to map columns to bean properties.
     *
     * @param reader The CSVReader to use for header parsing
     * @throws java.io.IOException If parsing fails
     * @throws CsvRequiredFieldEmptyException If a field is required, but the
     *   header or column position for the field is not present in the input
     */
   void captureHeader(CSVReader reader) throws IOException, CsvRequiredFieldEmptyException;
   
   /**
    * Implementations of this method must return an array of column headers
    * based on the contents of the mapping strategy.
    * If no header can or should be generated, an array of zero length should
    * be returned, and not null.
    * @return An array of column names for a header
    * @since 3.9
    */
   String[] generateHeader();

    /**
     * Gets the column index that corresponds to a specific column name.
     * <p>If the CSV file doesn't have a header row, this method will always return
     * null.</p>
     * <p>Inside of opencsv itself this method is only used for testing.</p>
     *
     * @param name The column name
     * @return The column index, or null if the name doesn't exist
     */
    Integer getColumnIndex(String name);

    /**
     * Determines whether the mapping strategy is driven by annotations.
     *
     * @return Whether the mapping strategy is driven by annotations
     */
   boolean isAnnotationDriven();
   
   /**
    * Must be called once the length of input for a line/record is known to
    * verify that the line was complete.
    * Complete in this context means, no required fields are missing. The issue
    * here is, as long as a column is present but empty, we can check whether
    * the field is required and throw an exception if it is not, but if the data
    * end prematurely, we never have this chance without indication that no more
    * data are on the way.
    * 
    * @param numberOfFields The number of fields present in the line of input
    * @throws CsvRequiredFieldEmptyException If a required column is missing
    * @since 4.0
    */
   void verifyLineLength(int numberOfFields) throws CsvRequiredFieldEmptyException;
   
    /**
     * Sets the locale for all error messages.
     * @param errorLocale Locale for error messages. If null, the default locale
     *   is used.
     * @since 4.0
     */
   void setErrorLocale(Locale errorLocale);
   
    /**
     * Sets the class type that is being mapped.
     * May perform additional initialization tasks.
     *
     * @param type Class type.
     * @throws CsvBadConverterException If a field in the bean is annotated
     *   with a custom converter that cannot be initialized. If you are not
     *   using custom converters that you have written yourself, it should be
     *   safe to catch this exception and ignore it.
     */
   public void setType(Class<? extends T> type) throws CsvBadConverterException;
}