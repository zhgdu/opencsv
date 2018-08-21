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
