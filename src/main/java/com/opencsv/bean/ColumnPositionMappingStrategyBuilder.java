package com.opencsv.bean;

/**
 * Builder for a {@link ColumnPositionMappingStrategy}.
 * This allows opencsv to introduce new options for mapping strategies
 * while maintaining backward compatibility and without creating
 * reams of constructors for the mapping strategy.
 *
 * @param <T> The type of the bean being processed
 * @since 5.5
 * @author Andrew Rucker Jones
 */
public class ColumnPositionMappingStrategyBuilder<T> {

    /** Default constructor. */
    public ColumnPositionMappingStrategyBuilder() {}

    /**
     * Builds a new mapping strategy for parsing/writing.
     * @return A new mapping strategy using the options selected
     */
    public ColumnPositionMappingStrategy<T> build() {
        return new ColumnPositionMappingStrategy<>();
    }
}
