package com.opencsv.bean.processor;

/**
 * This is the interface for validators for a single
 * {@link java.lang.String} value.
 * <p>Currently this is used by the {@link PreAssignmentProcessor} to possibly
 * modify the value of a string before time is taken to convert it.</p>
 * <p>This can be used when modifying the setter of the bean is not possible.</p>
 * <p>WARNING - using a processor can change the string in ways that could make
 * it impossible to be processed or make it into a different format than what
 * you are expecting based on the settings of the parser and reader. So great
 * care must be taken when creating and using a StringProcessor.</p>
 * <p>NOTE - Because of the potential problems a bad processor can cause we
 * will close down any bug reports created for opencsv where a StringProcessor is
 * involved with the recommendation they be reopened as a support request.</p>
 *
 * @author Scott Conway
 * @since 5.0
 */
public interface StringProcessor {
    /**
     * Method that contains the code that will transform a string into the
     * value that will be validated and converted into the bean field.
     *
     * @param value {@link String} to be processed
     * @return The processed {@link String}
     */
    String processString(String value);

    /**
     * This allows the validator extending {@link StringProcessor} to be used
     * by multiple fields by allowing you to pass in data for the processor to
     * be used.
     * <p>The data could be a default value or whatever the custom processor
     * requires to convert the data.</p>
     * <p>If the processor needs multiple parameters, then you will need to
     * combine them into a single string using some sort of delimiter, say a
     * comma, and parse them out using some library that allows you to parse
     * such strings 😁.</p>
     * <p>If the processor does not need a value then just create an empty
     * method like the ConvertEmptyOrBlankStringsToNull processor used by the
     * BeanFieldProcessorTest.</p>
     *
     * @param value Information used by the processor to process the string
     */
    void setParameterString(String value);
}
