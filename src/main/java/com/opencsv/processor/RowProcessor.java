package com.opencsv.processor;

/**
 * This is the interface for processors for an array of Strings read by the CSVReader BEFORE they are validated.
 * This should only be used if you have a very good understanding and full control of the data being processed or
 * something you want applied to every column in the row.
 * <p>
 * WARNING - using a processor can change the string in ways that could make impossible to be processed or make
 * it into a different format than what you are expecting based on the settings of the parser and reader.   So
 * great care must be taken when creating and using a RowProcessor.
 * <p>
 * NOTE - Because of the potential a bad processor can cause we will close down any defects created in opencsv
 * where a RowProcessor is involved with the recommendation they be reopened as a Support Request.
 * <p>
 * TODO come up with a good example of a RowProcessor
 * <p>
 * TODO write a test to show if quotes and escape characters have been processed at point processor is called.
 *
 * @author Scott Conway
 * @since 5.0
 */
public interface RowProcessor {

    /**
     * Method that contains the code that will transform a single column/element.
     * While not called directly by opencsv it is in the interface to provide an easy way to test
     * if the processor is functioning properly.
     *
     * @param column - String to be processed.
     * @return the processed String.
     */
    String processColumnItem(String column);

    /**
     * Method that will process the entire row.
     *
     * @param row - Array of Strings to be processed.
     */
    void processRow(String[] row);
}
