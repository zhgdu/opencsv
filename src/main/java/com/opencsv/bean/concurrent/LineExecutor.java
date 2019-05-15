package com.opencsv.bean.concurrent;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

import java.util.List;

/**
 * A specific derivative of {@link IntolerantThreadPoolExecutor} intended for
 * submitting lines of input to be converted to beans.
 *
 * @param <T> The type of the bean being converted to
 * @author Andrew Rucker Jones
 * @since 5.0
 */
public class LineExecutor<T> extends IntolerantThreadPoolExecutor<T> {

    /**
     * The only constructor available for this class.
     * @param orderedResults Whether order should be preserved in the results
     */
    public LineExecutor(boolean orderedResults) {super(orderedResults);}

    /**
     * Submit one record for conversion to a bean.
     *
     * @param lineNumber Which record in the input file is being processed
     * @param mapper The mapping strategy to be used
     * @param filter A filter to remove beans from the running, if necessary.
     *   May be null.
     * @param verifiers The list of verifiers to run on beans after creation
     * @param line The line of input to be transformed into a bean
     * @param throwExceptions Whether exceptions should be thrown or captured
     *   for later processing
     */
    public void submitLine(
            long lineNumber, MappingStrategy<? extends T> mapper, CsvToBeanFilter filter,
            List<BeanVerifier<T>> verifiers, String[] line, boolean throwExceptions) {
        try {
            execute(new ProcessCsvLine<>(
                    lineNumber, mapper, filter, verifiers, line,
                    resultQueue, thrownExceptionsQueue,
                    throwExceptions));
        } catch (Exception e) {
            if(accumulateThread != null) {
                accumulateThread.setMustStop(true);
            }
            throw e;
        }
    }
}
