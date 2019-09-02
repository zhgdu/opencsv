package com.opencsv.bean.validators;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class SimpleBeanValidatorTest {
    private static final String GOOD_NAME = "A good name";
    private static final String GOOD_ID_STRING = "256";
    private static final int GOOD_ID_INT = 256;
    public static String HEADER = "name,id\n";


    private CsvToBean<ValidatorTestBean> createBuilderForString(String csvStrings) {
        Reader stringReader = new StringReader(csvStrings);

        CsvToBeanBuilder<ValidatorTestBean> builder = new CsvToBeanBuilder(stringReader);

        return builder.withType(ValidatorTestBean.class).build();
    }

    private String createTestString(String name, String id) {
        StringBuilder builder = new StringBuilder(128);
        builder.append(HEADER);
        builder.append(name);
        builder.append(',');
        builder.append(id);
        builder.append("\n");
        return builder.toString();
    }

    @Test
    @DisplayName("Simple test to show defaults work the way I think they do.")
    public void testGoodString() {
        String testString = createTestString(GOOD_NAME, GOOD_ID_STRING);

        CsvToBean<ValidatorTestBean> builder = createBuilderForString(testString);

        List<ValidatorTestBean> beans = builder.parse();

        Assertions.assertEquals(1, beans.size());

        ValidatorTestBean bean = beans.get(0);
        Assertions.assertEquals(GOOD_NAME, bean.getBeanName());
        Assertions.assertEquals(GOOD_ID_INT, bean.getBeanId());
    }

    @Test
    @DisplayName("Will fail because of name")
    public void badName() {
        String testString = createTestString("1 is the start of a bad name", GOOD_ID_STRING);

        CsvToBean<ValidatorTestBean> builder = createBuilderForString(testString);

        try {
            builder.parse();
            Assertions.fail("Was expecting a CsvValidationException.");
        } catch (RuntimeException rte) {
            Throwable cause = rte.getCause();
            if (!(cause instanceof CsvValidationException)) {
                Assertions.fail(String.format("Was expecting to catch a CsvValidationException but got an %s instead", cause.getClass().getName()));
            }
        }
    }
}
