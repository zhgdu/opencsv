package com.opencsv.bean.processor;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeanFieldProcessorTest {
    private static final String GOOD_NAME = "A good name";
    private static final String GOOD_ID_STRING = "256";
    private static final int GOOD_ID_INT = 256;
    private static final String GOOD_BIG_NUMBER_STRING = "some value: 987654321";
    private static final long GOOD_BIG_NUMBER_LONG = 987654321;

    private static final int DEFAULT_ID = -1;
    private static final long DEFAULT_BIG_NUMBER = 31415926;

    public static String HEADER = "id,name,big number\n";


    private CsvToBean<ProcessorTestBean> createBuilderForString(String csvStrings) {
        Reader stringReader = new StringReader(csvStrings);

        CsvToBeanBuilder<ProcessorTestBean> builder = new CsvToBeanBuilder(stringReader);

        return builder.withType(ProcessorTestBean.class).build();
    }

    private String createTestString(String line) {
        StringBuilder builder = new StringBuilder(128);
        builder.append(HEADER);
        builder.append(line);
        builder.append("\n");
        return builder.toString();
    }

    private static Stream<Arguments> createArguements() {
        return Stream.of(
                Arguments.of("1,able,300", 1, "able", 300),
                Arguments.of(",,", DEFAULT_ID, null, DEFAULT_BIG_NUMBER),
                Arguments.of("  ,    ,    ", DEFAULT_ID, null, DEFAULT_BIG_NUMBER)
        );
    }

    @DisplayName("Test out preassignment processor")
    @ParameterizedTest
    @MethodSource("createArguements")
    public void testProcessor(String line, int id, String name, long bigNumber) {
        String testString = createTestString(line);
        CsvToBean<ProcessorTestBean> csvToBean = createBuilderForString(testString);

        List<ProcessorTestBean> beans = csvToBean.parse();
        assertEquals(1, beans.size());
        ProcessorTestBean bean = beans.get(0);
        assertEquals(id, bean.getBeanId());
        assertEquals(name, bean.getBeanName());
        assertEquals(bigNumber, bean.getBigNumber());
    }

}
