package integrationTest.FR138;

import com.opencsv.bean.processor.StringProcessor;

public class ConvertWordNullToNull implements StringProcessor {

    @Override
    public String processString(String value) {
        return "null".equalsIgnoreCase(value) ? null : value;
    }

    @Override
    public void setParameterString(String value) {
        // Unused in this case as all we care about is the word "null"
    }
}
