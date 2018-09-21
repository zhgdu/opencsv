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
package com.opencsv.bean.customconverter;

import com.opencsv.bean.AbstractCsvConverter;
import com.opencsv.bean.mocks.join.ErrorCode;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This is a perfectly terrible converter, full of bugs and such, but for
 * simple tests, I just don't care.
 * And yes, it is also a contestant for a conversion to and from the worst
 * data representation ever.
 */
public class ErrorCodeConverter extends AbstractCsvConverter {

    private ResourceBundle res;

    @Override
    public void setLocale(String locale) {
        super.setLocale(locale);
        if(this.locale != null) {
            res = ResourceBundle.getBundle("collectionconverter", this.locale);
        }
        else {
            res = ResourceBundle.getBundle("collectionconverter");
        }
    }

    @Override
    public Object convertToRead(String value) {
        ErrorCode ec = new ErrorCode();
        ec.errorCode = Integer.parseInt(value.substring(0, 2));
        try {
            ec.errorMessage = res.getString(value.substring(2));
        }
        catch(MissingResourceException e) {
            ec.errorMessage = value.substring(2);
        }

        return ec;
    }

    @Override
    public String convertToWrite(Object value) {
        ErrorCode ec = (ErrorCode) value;
        StringBuffer sb = new StringBuffer();
        sb.append(ec.errorCode);
        sb.append("default.error");
        return sb.toString();
    }
}
