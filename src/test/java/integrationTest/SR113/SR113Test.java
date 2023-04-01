package integrationTest.SR113;

import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SR113Test {

    private static final String TEST_STRING =
            "FEATURE_NAME,STATE,USER_COUNT\n" +
                    "hello world,production,3228\n" +
                    "calc age,beta,74\n" +
                    "wash dishes,alpha,3";

    private static final String TEST_EMPTY_STRING =
            "FEATURE_NAME,STATE,USER_COUNT\n" +
                    "hello world,    ,3228\n" +
                    "calc age,,74\n" +
                    "wash dishes,    ,3";

    private static final String TEST_EMPTY_STRING_NO_STATE =
            "FEATURE_NAME,USER_COUNT\n" +
                    "hello world,3228\n" +
                    "calc age,74\n" +
                    "wash dishes,3";

    private CSVReader createReader() {
        StringReader reader = new StringReader(TEST_STRING);
        return new CSVReader(reader);
    }

    private MappingStrategy<Feature> createMappingStrategy() {
        MappingStrategy<Feature> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(Feature.class);
        return strategy;
    }

    private MappingStrategy<Feature> createTranslateMappingStrategy() {
        HeaderColumnNameTranslateMappingStrategy<Feature> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("FEATURE_NAME", "name");
        columnMap.put("STATE", "state");
        strategy.setColumnMapping(columnMap);
        strategy.setType(Feature.class);
        return strategy;
    }

    public static class Feature {

        @CsvBindByName(column = "FEATURE_NAME")
        private String name;

        @CsvBindByName(required = true)
        private String state;

        public void setName(String name) {
            this.name = name;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }
    }

    @Test
    @DisplayName("Using HeaderColumnNameMappingStrategy null field should throw expection")
    public void testWithNullStringNoState() {
        CsvToBean<Feature> csvToBean = new CsvToBeanBuilder<Feature>(new StringReader(TEST_EMPTY_STRING_NO_STATE))
                .withMappingStrategy(createMappingStrategy())
                .build();
        Assertions.assertThrows(RuntimeException.class, () -> csvToBean.parse());
    }

    @Test
    @DisplayName("Using translate strategy null field should throw expection")
    public void testWithNullStringNoStateTranslate() {
        CsvToBean<Feature> csvToBean = new CsvToBeanBuilder<Feature>(new StringReader(TEST_EMPTY_STRING_NO_STATE))
                .withMappingStrategy(createTranslateMappingStrategy())
                .build();
        List<Feature> list = csvToBean.parse();
        assertNull(list.get(0).getState());
        assertNull(list.get(1).getState());
        assertNull(list.get(2).getState());
    }

    @Test
    @DisplayName("Using mapping strategy to enforce a header missing state")
    public void enforceColumns() throws CsvRequiredFieldEmptyException {
        MappingStrategy<Feature> strategy = createTranslateMappingStrategy();
        CsvToBean<Feature> csvToBean = new CsvToBeanBuilder<Feature>(new StringReader(TEST_EMPTY_STRING_NO_STATE))
                .withMappingStrategy(strategy)
                .build();
        List<Feature> list = csvToBean.parse();
        String[] header = strategy.generateHeader(list.get(0));
        assertFalse(Arrays.asList(header).contains("STATE"));
    }

    @Test
    @DisplayName("Using mapping strategy to enforce a header with state")
    public void enforceColumnsWithColumn() throws CsvRequiredFieldEmptyException {
        MappingStrategy<Feature> strategy = createTranslateMappingStrategy();
        CsvToBean<Feature> csvToBean = new CsvToBeanBuilder<Feature>(new StringReader(TEST_EMPTY_STRING))
                .withMappingStrategy(strategy)
                .build();
        List<Feature> list = csvToBean.parse();
        String[] header = strategy.generateHeader(list.get(0));
        assertTrue(Arrays.asList(header).contains("STATE"));
    }

    @Test
    @DisplayName("write bean with null value")
    public void writeWithNullField() {
        Feature feature = new Feature();
        feature.setName("test name");
        feature.setState(null);

        StringWriter writer = new StringWriter();
        StatefulBeanToCsvBuilder<Feature> builder = new StatefulBeanToCsvBuilder<>(writer);
        StatefulBeanToCsv<Feature> beanToCsv = builder.build();
        Assertions.assertThrows(CsvRequiredFieldEmptyException.class, () -> beanToCsv.write(feature));
    }
}
