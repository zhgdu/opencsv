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

import com.opencsv.exceptions.CsvBeanIntrospectionException;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * This base bean takes over the responsibility of converting the supplied
 * string to the proper type for the destination field and setting the
 * destination field. All custom converters must be descended from this class.
 *
 * @param <T> Type of the bean being populated
 * @author Andrew Rucker Jones
 * @since 3.8
 */
abstract public class AbstractBeanField<T> implements BeanField<T> {
    
    /** The field this class represents. */
    protected Field field;
    
    /**
     * This is just to avoid instantiating a new PropertyUtilsBean for every
     * time it needs to be used.
     */
    private PropertyUtilsBean propUtils;
    
    /** Whether or not this field is required. */
    protected boolean required;
    
    /** Locale for error messages. */
    protected Locale errorLocale;
    
    /**
     * Default nullary constructor, so derived classes aren't forced to create
     * a constructor identical to the one below.
     */
    public AbstractBeanField() {
        required = false;
        errorLocale = Locale.getDefault();
    }

    /**
     * Constructor for an optional field.
     * @param field A {@link java.lang.reflect.Field} object.
     */
    public AbstractBeanField(Field field) {
        this(field, false, Locale.getDefault());
    }

    /**
     * @param field A {@link java.lang.reflect.Field} object.
     * @param required Whether or not this field is required in input
     * @since 3.10
     */
    public AbstractBeanField(Field field, boolean required) {
        this(field, required, Locale.getDefault());
    }

    /**
     * @param field A {@link java.lang.reflect.Field} object.
     * @param required Whether or not this field is required in input
     * @param errorLocale The errorLocale to use for error messages.
     * @since 4.0
     */
    public AbstractBeanField(Field field, boolean required, Locale errorLocale) {
        this.field = field;
        this.required = required;
        this.errorLocale = ObjectUtils.defaultIfNull(errorLocale, Locale.getDefault());
    }

    @Override
    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public Field getField() {
        return this.field;
    }
    
    @Override
    public boolean isRequired() {
        return required;
    }
    
    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    @Override
    public void setErrorLocale(Locale errorLocale) {
        this.errorLocale = ObjectUtils.defaultIfNull(errorLocale, Locale.getDefault());
    }

    @Override
    public final <T> void setFieldValue(T bean, String value)
            throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException,
            CsvConstraintViolationException {
        if (required && StringUtils.isBlank(value)) {
            throw new CsvRequiredFieldEmptyException(
                    bean.getClass(), field,
                    String.format(ResourceBundle.getBundle("opencsv", errorLocale).getString("required.field.empty"),
                            field.getName()));
        }
        
        assignValueToField(bean, convert(value));
    }

    /**
     * Assigns the given object to this field of the destination bean.
     * Uses a custom setter method if available.
     *
     * @param <T>  Type of the bean
     * @param bean The bean in which the field is located
     * @param obj  The data to be assigned to this field of the destination bean
     * @throws CsvDataTypeMismatchException If the data to be assigned cannot
     *                                      be converted to the type of the destination field
     */
    private <T> void assignValueToField(T bean, Object obj)
            throws CsvDataTypeMismatchException {

        // obj == null means that the source field was empty. Then we simply
        // leave the field as it was initialized by the VM. For primitives,
        // that will be values like 0, and for objects it will be null.
        if (obj != null) {
            Class<?> fieldType = field.getType();

            // Find and use a setter method if one is available.
            String setterName = "set" + Character.toUpperCase(field.getName().charAt(0))
                    + field.getName().substring(1);
            try {
                Method setterMethod = bean.getClass().getMethod(setterName, fieldType);
                try {
                    setterMethod.invoke(bean, obj);
                } catch (IllegalAccessException e) {
                    // Can't happen, because we've already established that the
                    // method is public through the use of getMethod().
                } catch (InvocationTargetException e) {
                    CsvDataTypeMismatchException csve =
                            new CsvDataTypeMismatchException(obj, fieldType,
                                    e.getLocalizedMessage());
                    csve.initCause(e);
                    throw csve;
                }
            } catch (NoSuchMethodException | SecurityException e1) {
                // Otherwise set the field directly.
                writeWithoutSetter(bean, obj);
            }
        }
    }

    /**
     * Sets a field in a bean if there is no setter available.
     * Turns off all accessibility checking to accomplish the goal, and handles
     * errors as best it can.
     * 
     * @param <T>  Type of the bean
     * @param bean The bean in which the field is located
     * @param obj  The data to be assigned to this field of the destination bean
     * @throws CsvDataTypeMismatchException If the data to be assigned cannot
     *                                      be assigned
     */
    private <T> void writeWithoutSetter(T bean, Object obj) throws CsvDataTypeMismatchException {
        try {
            FieldUtils.writeField(field, bean, obj, true);
        } catch (IllegalAccessException e2) {
            // The Apache Commons Lang Javadoc claims this can be thrown
            // if the field is final, but it's not true if we override
            // accessibility. This is never thrown.
        } catch (IllegalArgumentException e2) {
            CsvDataTypeMismatchException csve =
                    new CsvDataTypeMismatchException(obj, field.getType());
            csve.initCause(e2);
            throw csve;
        }
    }

    /**
     * Method for converting from a string to the proper datatype of the
     * destination field.
     * This method must be specified in all non-abstract derived classes.
     *
     * @param value The string from the selected field of the CSV file. If the
     *   field is marked as required in the annotation, this value is guaranteed
     *   not to be null, empty or blank according to
     *   {@link org.apache.commons.lang3.StringUtils#isBlank(java.lang.CharSequence)}
     * @return An {@link java.lang.Object} representing the input data converted
     *   into the proper type
     * @throws CsvDataTypeMismatchException    If the input string cannot be converted into
     *                                         the proper type
     * @throws CsvConstraintViolationException When the internal structure of
     *                                         data would be violated by the data in the CSV file
     */
    protected abstract Object convert(String value)
            throws CsvDataTypeMismatchException, CsvConstraintViolationException;
    
    /**
     * This method takes the current value of the field in question in the bean
     * passed in and converts it to a string.
     * It is actually a stub that calls {@link #convertToWrite(java.lang.Object)}
     * for the actual conversion, and itself performs validation and handles
     * exceptions thrown by {@link #convertToWrite(java.lang.Object)}. The
     * validation consists of verifying that both {@code bean} and {@link #field}
     * are not null before calling {@link #convertToWrite(java.lang.Object)}.
     */
    // The rest of the Javadoc is automatically inherited
    @Override
    public final String write(T bean) throws CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException {
        String result = null;
        if(bean != null && field != null) {
            if(propUtils == null) {
                propUtils = new PropertyUtilsBean();
            }
            Object value;
            try {
                value = propUtils.getSimpleProperty(bean, field.getName());
            }
            catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                CsvBeanIntrospectionException csve = new CsvBeanIntrospectionException(bean, field);
                csve.initCause(e);
                throw csve;
            }
            
            if(value == null && required) {
                throw new CsvRequiredFieldEmptyException(
                        bean.getClass(), field,
                        String.format(ResourceBundle.getBundle("opencsv", errorLocale).getString("required.field.empty"),
                                field.getName()));
            }
            
            try {
                result = convertToWrite(value);
            }
            catch(CsvDataTypeMismatchException e) {
                CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(
                        bean, field.getType(), e.getMessage());
                csve.initCause(e.getCause());
                throw csve;
            }
        }
        return result;
    }
    
    /**
     * This is the method that actually performs the conversion from field to
     * string for {@link #write(java.lang.Object)} and should be overridden in
     * derived classes.
     * <p>The default implementation simply calls {@code toString()} on the
     * object in question. Derived classes will, in most cases, want to override
     * this method. Alternatively, for complex types, overriding the
     * {@code toString()} method in the type of the field in question would also
     * work fine.</p>
     * 
     * @param value The contents of the field currently being processed from the
     *   bean to be written. Can be null.
     * @return A string representation of the value of the field in question in
     *   the bean passed in, or an empty string if {@code value} is null
     * @throws CsvDataTypeMismatchException This implementation does not throw
     *   this exception
     * @since 3.9
     * @see #write(java.lang.Object) 
     */
    protected String convertToWrite(Object value)
            throws CsvDataTypeMismatchException {
        // Since we have no concept of which field is required at this level,
        // we can't check for null and throw an exeception.
        return value==null?"":value.toString();
    }
}
