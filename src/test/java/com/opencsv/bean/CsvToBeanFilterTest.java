package com.opencsv.bean;

import com.opencsv.CSVReader;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CsvToBeanFilterTest {

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

   private CSVReader createReader() {
      StringReader reader = new StringReader(TEST_STRING);
      return new CSVReader(reader);
   }

   private MappingStrategy CreateMappingStrategy() {
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

   private class NonProductionFilter implements CsvToBeanFilter {

      private final MappingStrategy strategy;

      public NonProductionFilter(MappingStrategy strategy) {
         this.strategy = strategy;
      }

      @Override
      public boolean allowLine(String[] line) {
         int index = strategy.getColumnIndex("STATE");
         String value = line[index];
         boolean result = !"production".equals(value);
         return result;
      }

   }

   @Test
   public void testColumnNameTranslationWithLineFiltering() {
      CsvToBean csvToBean = new CsvToBean();
      CSVReader reader = createReader();
      MappingStrategy strategy = CreateMappingStrategy();
      CsvToBeanFilter filter = new NonProductionFilter(strategy);
      List<Feature> list = csvToBean.parse(strategy, reader, filter);
      assertEquals("Parsing resulted in the wrong number of items.", 2, list.size());
      assertEquals("The first item has the wrong name.", "calc age", list.get(0).getName());
      assertEquals("The first item has the wrong state.", "beta", list.get(0).getState());
      assertEquals("The second item has the wrong name.", "wash dishes", list.get(1).getName());
      assertEquals("The second item has the wrong state.", "alpha", list.get(1).getState());
   }

   @Test
   public void testColumnNameTranslationWithLineFilteringAndEmptyState() {
      CsvToBean csvToBean = new CsvToBean();
      StringReader stringReader = new StringReader(TEST_EMPTY_STRING);
      CSVReader reader = new CSVReader(stringReader);
      MappingStrategy strategy = CreateMappingStrategy();
      CsvToBeanFilter filter = new NonProductionFilter(strategy);
      List<Feature> list = csvToBean.parse(strategy, reader);
      assertEquals("    ", list.get(0).getState());
      assertTrue(list.get(1).getState().isEmpty());
      assertEquals("    ", list.get(2).getState());
   }

   @Test
   public void testFilterWithParallelParsing() {
      MappingStrategy<Feature> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(Feature.class);
      List<Feature> list = new CsvToBeanBuilder<Feature>(new StringReader(TEST_STRING))
              .withMappingStrategy(strategy)
              .withFilter(new NonProductionFilter(strategy))
              .build().parse();
      assertEquals("Parsing resulted in the wrong number of items.", 2, list.size());
      assertEquals("The first item has the wrong name.", "calc age", list.get(0).getName());
      assertEquals("The first item has the wrong state.", "beta", list.get(0).getState());
      assertEquals("The second item has the wrong name.", "wash dishes", list.get(1).getName());
      assertEquals("The second item has the wrong state.", "alpha", list.get(1).getState());
   }

   @Test
   public void testFilterWithIteratorParsing() {
      MappingStrategy<Feature> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(Feature.class);
      CsvToBean<Feature> ctb = new CsvToBeanBuilder<Feature>(new StringReader(TEST_STRING))
              .withMappingStrategy(strategy)
              .withFilter(new NonProductionFilter(strategy))
              .build();
      List<Feature> list = new ArrayList<>(2);
      for(Feature f : ctb) { list.add(f); }
      assertEquals("Parsing resulted in the wrong number of items.", 2, list.size());
      assertEquals("The first item has the wrong name.", "calc age", list.get(0).getName());
      assertEquals("The first item has the wrong state.", "beta", list.get(0).getState());
      assertEquals("The second item has the wrong name.", "wash dishes", list.get(1).getName());
      assertEquals("The second item has the wrong state.", "alpha", list.get(1).getState());
   }
}
