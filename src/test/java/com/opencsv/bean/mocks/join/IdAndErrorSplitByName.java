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
package com.opencsv.bean.mocks.join;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.customconverter.ErrorCodeConverter;

import java.util.List;

public class IdAndErrorSplitByName {

    @CsvBindByName
    private int id;

    @CsvBindAndSplitByName(elementType = ErrorCode.class, converter = ErrorCodeConverter.class, locale = "de-DE")
    private List<ErrorCode> ec;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ErrorCode> getEc() {
        return ec;
    }

    public void setEc(List<ErrorCode> ec) {
        this.ec = ec;
    }
}
