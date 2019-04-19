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
package com.opencsv.bean.comparator;

import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.comparators.ComparatorChain;
import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.apache.commons.collections4.comparators.NullComparator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This {@link java.util.Comparator} takes an array of literals that define an
 * order.
 * Anything not included in the array is placed after anything in the array and
 * is then sorted according to its natural order.
 *
 * @param <T> The type to be sorted
 *
 * @since 4.3
 */
// TODO: For 5.0, this should be deprecated in favor of using the comparators from Commons Collections directly.
public class LiteralComparator<T extends Comparable<T>> implements Comparator<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private Comparator<T> c;

    /**
     * Constructor.
     *
     * @param predefinedOrder Objects that define the order of comparison
     */
    public LiteralComparator(T[] predefinedOrder) {
        List<T> predefinedList = predefinedOrder == null ? Collections.<T>emptyList() : Arrays.<T>asList(predefinedOrder);
        FixedOrderComparator<T> fixedComparator = new FixedOrderComparator<>(predefinedList);
        fixedComparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        c = new ComparatorChain<>(Arrays.<Comparator<T>>asList(
                fixedComparator,
                new NullComparator<T>(false),
                new ComparableComparator<T>()));
    }

    @Override
    public int compare(T o1, T o2) {
        return c.compare(o1, o2);
    }
}
