/*
 * Copyright 2017 Andrew Rucker Jones.
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
package com.opencsv.bean.concurrent;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.opencsvUtils;
import com.opencsv.exceptions.CsvBeanIntrospectionException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvRuntimeException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * A class for converting one bean into its string representation for writing to
 * an output.
 * @param <T> The type of the bean to be processed
 * @since 4.0
 * @author Andrew Rucker Jones
 */
public class ProcessCsvBean<T> implements Runnable {
    
    private final long lineNumber;
    private final MappingStrategy<T> mappingStrategy;
    private final T bean;
    private final BlockingQueue<OrderedObject<String[]>> resultantLineQueue;
    private final BlockingQueue<OrderedObject<CsvException>> thrownExceptionsQueue;
    private final boolean throwExceptions;
    private final List<String> contents = new ArrayList<>();
    private final Locale errorLocale;
    
    /**
     * The only constructor for creating a line of CSV output out of a bean.
     * @param lineNumber Which record in the output file is being processed
     * @param mappingStrategy The mapping strategy to be used
     * @param bean The bean to be transformed into a line of output
     * @param resultantLineQueue A queue in which to place the line created
     * @param thrownExceptionsQueue A queue in which to place a thrown
     *   exception, if one is thrown
     * @param throwExceptions Whether exceptions should be thrown or captured
     *   for later processing
     * @param errorLocale Locale for error messages. If null, the default locale
     *   is used.
     */
    public ProcessCsvBean(long lineNumber, MappingStrategy<T> mappingStrategy,
            T bean, BlockingQueue<OrderedObject<String[]>> resultantLineQueue,
            BlockingQueue<OrderedObject<CsvException>> thrownExceptionsQueue,
            boolean throwExceptions, Locale errorLocale) {
        this.lineNumber = lineNumber;
        this.mappingStrategy = mappingStrategy;
        this.bean = bean;
        this.resultantLineQueue = resultantLineQueue;
        this.thrownExceptionsQueue = thrownExceptionsQueue;
        this.throwExceptions = throwExceptions;
        this.errorLocale = ObjectUtils.defaultIfNull(errorLocale, Locale.getDefault());
    }
    
    private void writeWithReflection(int numColumns)
            throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        BeanField beanField;
        for(int i = 0; i <= numColumns; i++) {
            beanField = mappingStrategy.findField(i);
            String s = beanField != null ? beanField.write(bean) : "";
            contents.add(StringUtils.defaultString(s));
        }
    }
    
    private void writeWithIntrospection(int numColumns) {
        PropertyDescriptor desc;
        for(int i = 0; i <= numColumns; i++) {
            try {
                desc = mappingStrategy.findDescriptor(i);
                Object o = desc != null ? desc.getReadMethod().invoke(bean, (Object[]) null) : null;
                contents.add(Objects.toString(o, ""));
            }
            catch(IllegalAccessException | InvocationTargetException e) {
                CsvBeanIntrospectionException csve = new CsvBeanIntrospectionException(
                        bean, null, ResourceBundle.getBundle("opencsv", errorLocale).getString("error.introspecting.beans"));
                csve.initCause(e);
                throw csve;
            }
        }
    }

    @Override
    public void run() {
        try {
            int numColumns = mappingStrategy.findMaxFieldIndex();
            if(mappingStrategy.isAnnotationDriven()) {
                writeWithReflection(numColumns);
            }
            else {
                writeWithIntrospection(numColumns);
            }
            opencsvUtils.queueRefuseToAcceptDefeat(resultantLineQueue,
                    new OrderedObject(lineNumber,
                            contents.toArray(new String[contents.size()])));
        }
        catch (CsvException e) {
            CsvException csve = (CsvException) e;
            csve.setLineNumber(lineNumber);
            if(throwExceptions) {
                throw new RuntimeException(csve);
            }
            opencsvUtils.queueRefuseToAcceptDefeat(thrownExceptionsQueue,
                    new OrderedObject<>(lineNumber, csve));
        }
        catch(CsvRuntimeException csvre) {
            throw csvre;
        }
        catch(Exception t) {
            throw new RuntimeException(t);
        }
    }
    
}
