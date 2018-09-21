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

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.customconverter.BadCollectionConverter;
import org.apache.commons.collections4.MultiValuedMap;

public class BadJoinConverter {

    @CsvBindAndJoinByName(elementType = Object.class, converter = BadCollectionConverter.class)
    private MultiValuedMap<String, Object> objects;

    public MultiValuedMap<String, Object> getObjects() {
        return objects;
    }

    public void setObjects(MultiValuedMap<String, Object> objects) {
        this.objects = objects;
    }
}
