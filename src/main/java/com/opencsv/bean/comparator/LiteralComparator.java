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

import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This {@link java.util.Comparator} takes an array of literals that define an
 * order.
 * Anything not included in the array is placed after anything in the array and
 * is then sorted according to its natural order.
 *
 * @param T The type to be sorted
 * @since 4.3
 */
public class LiteralComparator<T extends Comparable> implements Comparator<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private final T[] predefinedOrder;

    public LiteralComparator(T[] predefinedOrder) {
        this.predefinedOrder = predefinedOrder;
    }

    @Override
    public int compare(T o1, T o2) {
        int indexO1 = ArrayUtils.indexOf(predefinedOrder, o1);
        int indexO2 = ArrayUtils.indexOf(predefinedOrder, o2);
        if(indexO1 != ArrayUtils.INDEX_NOT_FOUND) {
            if(indexO2 == ArrayUtils.INDEX_NOT_FOUND) {
                return -1;
            }
            else {
                return Integer.compare(indexO1, indexO2);
            }
        }
        else {
            if(indexO2 == ArrayUtils.INDEX_NOT_FOUND) {
                if(o1 != null) {
                    if(o2 != null) {
                        return o1.compareTo(o2);
                    }
                    return 1;
                }
                else {
                    if(o2 != null) {
                        return -1;
                    }
                    return 0;
                }
            }
            return 1;
        }
    }
}
