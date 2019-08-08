package com.opencsv.bean;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A mapping strategy that tries to make the best match between header names
 * and non-annotated member variables.
 *
 * @param <T> The type of bean being processed
 * @author Andrew Rucker Jones
 * @since 5.0
 */
public class FuzzyMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

    /**
     * Nullary constructor to make the style checker happy.
     */
    public FuzzyMappingStrategy() {
    }

    /**
     * This implementation intentionally does nothing in order to allow fuzzy
     * matching in case there are no annotations at all in the class in
     * question.
     */
    @Override
    protected void loadUnadornedFieldMap(ListValuedMap<Class<?>, Field> fields) {}

    @Override
    public void captureHeader(CSVReader reader) throws IOException, CsvRequiredFieldEmptyException {
        super.captureHeader(reader);

        // Find all headers not mapped
        final Set<String> unusedHeaders = Stream.of(headerIndex.getHeaderIndex())
                .filter(Objects::nonNull)
                .filter(k -> fieldMap.get(k.toUpperCase()) == null)
                .collect(Collectors.toSet());

        // Find all non-annotated fields
        final ListValuedMap<Class<?>, Field> unusedFields = partitionFields().get(Boolean.FALSE);

        // Calculate distances and sort
        LevenshteinDistance levenshtein = LevenshteinDistance.getDefaultInstance();
        List<FuzzyComparison> comparisons = new LinkedList<>();
        unusedHeaders.forEach(h -> {
            unusedFields.entries().forEach(f -> {
                comparisons.add(new FuzzyComparison(
                        levenshtein.apply(h.toUpperCase(), f.getValue().getName().toUpperCase()),
                        h, f.getKey(), f.getValue()));
            });
        });
        comparisons.sort(null);

        // Use the best matches
        while (!comparisons.isEmpty()) {
            FuzzyComparison fc = comparisons.get(0);

            // Add the mapping
            CsvConverter converter = determineConverter(
                    fc.field, fc.field.getType(), null, null, null);
            fieldMap.put(fc.header.toUpperCase(), new BeanFieldSingleValue<>(
                    fc.type, fc.field, false, errorLocale, converter, null,
                    null));

            // Remove any other comparisons for the header or field
            comparisons.removeIf(e ->
                    StringUtils.equals(e.header, fc.header)
                            || Objects.equals(e.field, fc.field));
        }
    }

    /**
     * This is a simple class for grouping header name, member variable name,
     * and the result of fuzzy matching in one sortable place.
     */
    private static class FuzzyComparison implements Comparable<FuzzyComparison> {

        final Integer distance;
        final String header;
        final Class<?> type;
        final Field field;

        FuzzyComparison(Integer distance, String header, Class<?> type, Field field) {
            this.distance = distance;
            this.header = header;
            this.type = type;
            this.field = field;
        }

        @Override
        public int compareTo(FuzzyComparison o) {
            return Integer.compare(distance, o.distance);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof FuzzyComparison)) {
                return false;
            }
            FuzzyComparison that = (FuzzyComparison) o;
            return Objects.equals(distance, that.distance);
        }

        @Override
        public int hashCode() {
            return Objects.hash(distance);
        }
    }
}
