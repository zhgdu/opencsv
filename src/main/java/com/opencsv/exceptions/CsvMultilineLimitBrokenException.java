/*
 * Copyright 2016 Andrew Rucker Jones.
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
package com.opencsv.exceptions;

import java.io.IOException;

/**
 * Exceptions when you break the lime limit of the multiline field
 * @author Edgar Silva
 */
public class CsvMultilineLimitBrokenException extends IOException {
    private static final long serialVersionUID = 1L;
    private long row;
    private String context;
    private int multilineLimit;

    /**
     * @return the multiline limit set during construction of the exception.
     */
    public int getMultilineLimit() {
        return multilineLimit;
    }

    /**
     * @return the row number set during construction of the exception.
     */
    public long getRow() {
        return row;
    }

    /**
     * @return context set during construction of the exception.
     */
    public String getContext() {
        return context;
    }

    /** Nullary constructor. Does nothing. */
    public CsvMultilineLimitBrokenException() {
        super();
    }

    /**
     * Constructor with a message.
     * @param message A human-readable error message.
     * @param row row number where error occurred.
     * @param context line (or part of line) that caused the error.
     * @param multilineLimit multiline limit that was set.
     */
    public CsvMultilineLimitBrokenException(String message,long row, String context, int multilineLimit) {
        super(message);
        this.row = row;
        this.context = context;
        this.multilineLimit = multilineLimit;
    }
}
