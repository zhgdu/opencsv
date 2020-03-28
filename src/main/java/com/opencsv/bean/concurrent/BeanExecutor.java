package com.opencsv.bean.concurrent;

import com.opencsv.bean.MappingStrategy;

import java.util.Locale;

/**
 * A specific derivative of {@link IntolerantThreadPoolExecutor} intended for
 * submitting beans to be converted to {@link java.lang.String}s for writing.
 *
 * @param <T> The type of the bean being converted
 * @author Andrew Rucker Jones
 * @since 5.0
 */
public class BeanExecutor<T> extends IntolerantThreadPoolExecutor<String[]> {

    /**
     * The only constructor available for this class.
     * @param orderedResults Whether order should be preserved in the results
     * @param errorLocale The locale to use for error messages
     */
    public BeanExecutor(boolean orderedResults, Locale errorLocale) {
        super(orderedResults, errorLocale);
    }

    /**
     * Submit one bean for conversion.
     *
     * @param lineNumber Which record in the output file is being processed
     * @param mappingStrategy The mapping strategy to be used
     * @param bean The bean to be transformed into a line of output
     * @param throwExceptions Whether exceptions should be thrown or captured
     *   for later processing
     */
    public void submitBean(
            long lineNumber, MappingStrategy<T> mappingStrategy,
            T bean, boolean throwExceptions) {
        if (accumulateThread != null) {
            expectedRecords.add(lineNumber);
        }
        try {
            execute(new ProcessCsvBean<>(lineNumber, mappingStrategy, bean, resultQueue,
                    thrownExceptionsQueue, throwExceptions));
        } catch (Exception e) {
            if(accumulateThread != null) {
                expectedRecords.remove(lineNumber);
                accumulateThread.setMustStop(true);
            }
            throw e;
        }
    }
}
