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
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.iterators.LazyIteratorChain;
import org.apache.commons.collections4.iterators.TransformIterator;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class maintains a mapping from column position out of a CSV file to bean
 * fields.
 *
 * @param <T> Type of the bean being converted
 * @author Andrew Rucker Jones
 * @since 4.2
 */
public class FieldMapByPosition<T> extends AbstractFieldMap<String, Integer, PositionToBeanField<T>, T> implements Iterable<FieldMapByPositionEntry<T>> {
    
    private int maxIndex = Integer.MAX_VALUE;

    /** Holds a {@link java.util.Comparator} to sort columns on writing. */
    private Comparator<Integer> writeOrder = null;
    
    /**
     * Initializes this {@link FieldMap}.
     * 
     * @param errorLocale The locale to be used for error messages
     */
    public FieldMapByPosition(final Locale errorLocale) {
        super(errorLocale);
    }
    
    /**
     * This method generates a header that can be used for writing beans of the
     * type provided back to a file.
     * The ordering of the headers can be determined with the
     * {@link java.util.Comparator} passed in to
     * {@link #setColumnOrderOnWrite(Comparator)}. Otherwise, it is ascending
     * according to position.
     */
    // The rest of the Javadoc is inherited.
    @Override
    public String[] generateHeader(final T bean) throws CsvRequiredFieldEmptyException {
        final List<Field> missingRequiredHeaders = new LinkedList<>();
        final SortedMap<Integer, String> headerMap = new TreeMap<>(writeOrder);
        for(Map.Entry<Integer, BeanField<T, Integer>> entry : simpleMap.entrySet()) {
            headerMap.put(entry.getKey(), entry.getValue().getField().getName());
        }
        for(ComplexFieldMapEntry<String, Integer, T> r : complexMapList) {
            @SuppressWarnings("unchecked")
            final MultiValuedMap<Integer,T> m = (MultiValuedMap<Integer, T>) r.getBeanField().getFieldValue(bean);
            boolean oneEntryMatched = false;
            if(m != null && !m.isEmpty()) {
                for(Map.Entry<Integer,T> entry : m.entries()) {
                    Integer key = entry.getKey();
                    if(r.contains(key)) {
                        headerMap.put(entry.getKey(), r.getBeanField().getField().getName());
                        oneEntryMatched = true;
                    }
                }
            }
            if(m == null || m.isEmpty() || !oneEntryMatched) {
                if(r.getBeanField().isRequired()) {
                    missingRequiredHeaders.add(r.getBeanField().getField());
                }
            }
        }
        
        // Convert to an array of header "names".
        // Since the user can pass in an arbitrary collation, we have to
        // re-sort to get the highest value.
        SortedSet<Integer> headerSet = new TreeSet<>(headerMap.keySet());
        int arraySize = headerSet.isEmpty() ? 0 : headerSet.last()+1;
        final String[] headers = new String[arraySize];
        int previousIndex = headerSet.isEmpty() ? 0 : headerSet.first();
        for(Integer i : headerSet) { // Fill in gaps
            for(int j = previousIndex+1; j < i ; j++) {
                headerMap.put(j, null);
            }
            previousIndex = i;
        }
        previousIndex = 0;
        for(String value : headerMap.values()) {
            headers[previousIndex++] = value;
        }
        
        // Report headers that should have been present
        if(!missingRequiredHeaders.isEmpty()) {
            String errorMessage = String.format(
                    ResourceBundle
                            .getBundle(ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                            .getString("header.required.field.absent"),
                    missingRequiredHeaders.stream()
                            .map(Field::getName)
                            .collect(Collectors.joining(" ")),
                    String.join(" ", headers));
            throw new CsvRequiredFieldEmptyException(bean.getClass(), missingRequiredHeaders, errorMessage);
        }
        
        return headers;
    }
    
    /**
     * @param rangeDefinition A string describing the column positions to be
     *   matched.
     * @see CsvBindAndJoinByPosition#position() 
     */
    // The rest of the Javadoc is inherited
    @Override
    public void putComplex(final String rangeDefinition, final BeanField<T, Integer> field) {
        complexMapList.add(new PositionToBeanField<>(rangeDefinition, maxIndex, field, errorLocale));
    }
    
    /**
     * Sets the maximum index for all ranges specified in the entire field map.
     * No ranges or mappings are ever removed so as to preserve information
     * about required fields, but upper boundries are shortened as much as
     * possible. If ranges or individual column positions were specified that
     * lie wholly above {@code maxIndex}, these are preserved, though ranges
     * are shortened to a single value (the lower boundry).
     * 
     * @param maxIndex The maximum index in the data being imported
     */
    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
        
        // Attenuate all ranges that end past the last index down to the last index
        complexMapList.forEach(p -> p.attenuateRanges(maxIndex));
    }

    @Override
    public Iterator<FieldMapByPositionEntry<T>> iterator() {
        return new LazyIteratorChain<FieldMapByPositionEntry<T>>() {
            
            @Override
            protected Iterator<FieldMapByPositionEntry<T>> nextIterator(int count) {
                if(count <= complexMapList.size()) {
                    return complexMapList.get(count-1).iterator();
                }
                if(count == complexMapList.size()+1) {
                    return new TransformIterator<>(
                            simpleMap.entrySet().iterator(),
                            input -> new FieldMapByPositionEntry<T>(input.getKey(), input.getValue()));
                }
                return null;
            }
        };
    }

    /**
     * Sets the {@link java.util.Comparator} to be used to sort columns when
     * writing beans to a CSV file.
     *
     * @param writeOrder The {@link java.util.Comparator} to use. May be
     *   {@code null}, in which case the natural ordering is used.
     * @since 4.3
     */
    public void setColumnOrderOnWrite(Comparator<Integer> writeOrder) {
        this.writeOrder = writeOrder;
    }
}
