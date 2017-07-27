package com.opencsv.bean;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.mocks.*;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class CsvToBeanTest {
   private static final String TEST_STRING = "name,orderNumber,num\n" +
         "kyle,abc123456,123\n" +
         "jimmy,def098765,456 ";

   private static final String TEST_STRING_WITHOUT_MANDATORY_FIELD = "name,orderNumber,num\n" +
           "kyle,abc123456,123\n" +
           "jimmy,def098765,";

   private static final String TEST_STRING_ALL_DATATYPES = "familyId,familyName,familySize,averageAge,averageIncome,numberOfPets,numberOfBedrooms,zipcodePrefix,hasBeenContacted\n" +
           "922337203685477580,Jones,5,18.77293748162537,32000.72937634,1,4,Z,true\n" +
           "238801727291675293,Smith,3,28.74826489578307,56643.82631345,2,2,A,false\n" +
           "882101432432123445,,3,38.48347628462843,74200.73912766,0,3,Z,false\n" +
           "619364584659026342,Woods,4,17.12739636774893,48612.12395295,1,,M,true";
   
   private static final String TEST_STRING_FOR_MINIMAL_BUILDER = "1,2,3";

   private CSVReader createReader() {
      return createReader(TEST_STRING);
   }

   private CSVReader createReader(String testString) {
      StringReader reader = new StringReader(testString);
      return new CSVReader(reader);
   }

   private MappingStrategy createErrorHeaderMappingStrategy() {
      return new MappingStrategy() {

         @Override
         public PropertyDescriptor findDescriptor(int col) throws IntrospectionException {
            return null;
         }

         @Override
         public BeanField findField(int col) {
            return null;
         }

         @Override
         public Object createBean() throws InstantiationException, IllegalAccessException {
            return null;
         }

         @Override
         public void captureHeader(CSVReader reader) throws IOException {
            throw new IOException("This is the test exception");
         }

         @Override
         public Integer getColumnIndex(String name) {
            return null;
         }

         @Override
         public boolean isAnnotationDriven() {
            return false;
         }
         
         @Override
         public String[] generateHeader() {
             return new String[0];
         }
         
         @Override
         public int findMaxFieldIndex() {
             return -1;
         }
         
         @Override
         public void registerBeginningOfRecordForReading() {}
         
         @Override
         public void registerEndOfRecordForReading() {}
      };
   }

   private MappingStrategy createErrorLineMappingStrategy() {
      return new MappingStrategy() {

         @Override
         public PropertyDescriptor findDescriptor(int col) throws IntrospectionException {
            return null;
         }

         @Override
         public BeanField findField(int col) {
            return null;
         }

         @Override
         public Object createBean() throws InstantiationException, IllegalAccessException {
            throw new InstantiationException("this is a test Exception");
         }

         @Override
         public void captureHeader(CSVReader reader) throws IOException {
         }

         @Override
         public Integer getColumnIndex(String name) {
            return null;
         }

         @Override
         public boolean isAnnotationDriven() {
            return false;
         }
         
         @Override
         public String[] generateHeader() {
             return new String[0];
         }
         
         @Override
         public int findMaxFieldIndex() {
             return -1;
         }
         
         @Override
         public void registerBeginningOfRecordForReading() {}
         
         @Override
         public void registerEndOfRecordForReading() {}
      };
   }

   @Test(expected = RuntimeException.class)
   public void throwRuntimeExceptionWhenExceptionIsThrown() {
      CsvToBean bean = new CsvToBean();
      bean.parse(createErrorHeaderMappingStrategy(), createReader());
   }

   @Test(expected = RuntimeException.class)
   public void throwRuntimeExceptionLineWhenExceptionIsThrown() {
      CsvToBean bean = new CsvToBean();
      bean.parse(createErrorLineMappingStrategy(), createReader());
   }

   @Test
   public void parseBeanWithNoAnnotations() {
      HeaderColumnNameMappingStrategy<MockBean> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(MockBean.class);
      CsvToBean<MockBean> bean = new CsvToBean<>();

      List<MockBean> beanList = bean.parse(strategy, createReader());
      assertEquals(2, beanList.size());
      assertTrue(beanList.contains(createMockBean("kyle", "abc123456", 123)));
      assertTrue(beanList.contains(createMockBean("jimmy", "def098765", 456)));
   }

   private MockBean createMockBean(String name, String orderNumber, int num) {
      MockBean mockBean = new MockBean();
      mockBean.setName(name);
      mockBean.setOrderNumber(orderNumber);
      mockBean.setNum(num);
      return mockBean;
   }

   @Test
   public void bug133ShouldNotThrowNullPointerExceptionWhenProcessingEmptyWithNoAnnotations() {
      HeaderColumnNameMappingStrategy<Bug133Bean> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(Bug133Bean.class);

      StringReader reader = new StringReader("one;two;three\n" +
              "kyle;;123\n" +
              "jimmy;;456 ");

      CSVParserBuilder parserBuilder = new CSVParserBuilder();
      CSVReaderBuilder readerBuilder = new CSVReaderBuilder(reader);

      CSVParser parser = parserBuilder.withFieldAsNull(CSVReaderNullFieldIndicator.BOTH).withSeparator(';').build();
      CSVReader csvReader = readerBuilder.withCSVParser(parser).build();

      CsvToBean<Bug133Bean> bean = new CsvToBean<>();

      List<Bug133Bean> beanList = bean.parse(strategy, csvReader);
      assertEquals(2, beanList.size());
   }

   @Test(expected = IllegalStateException.class)
   public void throwIllegalStateWhenParseWithoutArgumentsIsCalled() {
       CsvToBean csvtb = new CsvToBean();
       csvtb.parse();
   }
   
   @Test(expected = IllegalStateException.class)
   public void throwIllegalStateWhenOnlyReaderIsSpecifiedToParseWithoutArguments() {
       CsvToBean csvtb = new CsvToBean();
       csvtb.setCsvReader(new CSVReader(new StringReader(TEST_STRING)));
       csvtb.parse();
   }
   
   @Test(expected = IllegalStateException.class)
   public void throwIllegalStateWhenOnlyMapperIsSpecifiedToParseWithoutArguments() {
       CsvToBean csvtb = new CsvToBean();
       HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat = new HeaderColumnNameMappingStrategy<>();
       strat.setType(AnnotatedMockBeanFull.class);
       csvtb.setMappingStrategy(strat);
       csvtb.parse();
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void throwIllegalStateWhenReaderNotProvidedInBuilder() {
       new CsvToBeanBuilder<>(null)
               .withType(AnnotatedMockBeanFull.class)
               .build();
   }
   
   @Test(expected = IllegalStateException.class)
   public void throwIllegalStateWhenTypeAndMapperNotProvidedInBuilder() {
       new CsvToBeanBuilder<>(new StringReader(TEST_STRING_WITHOUT_MANDATORY_FIELD))
               .build();
   }
   
   @Test
   public void testMinimumBuilder() {
       List<MinimalCsvBindByPositionBeanForWriting> result =
               new CsvToBeanBuilder<>(new StringReader("1,2,3\n4,5,6"))
                       .withType(MinimalCsvBindByPositionBeanForWriting.class)
                       .build()
                       .parse();
       assertEquals(2, result.size());
   }
   
   private class BegToBeFiltered implements CsvToBeanFilter {

      @Override
      public boolean allowLine(String[] line) {
         for(String col : line) {
             if(col.equals("filtermebaby")) return false;
         }
         return true;
      }

   }

   @Test
   public void testMaximumBuilder() throws FileNotFoundException {
       HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> map = new HeaderColumnNameMappingStrategy<>();
       map.setType(AnnotatedMockBeanFull.class);
       
       // Yeah, some of these are the default values, but I'm having trouble concocting
       // a CSV file screwy enough to meet the requirements posed by not using
       // defaults for everything.
       CsvToBean csvtb =
               new CsvToBeanBuilder<AnnotatedMockBeanFull>(new FileReader("src/test/resources/testinputmaximumbuilder.csv"))
                       .withEscapeChar('?')
                       .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER) //default
                       .withFilter(new BegToBeFiltered())
                       .withIgnoreLeadingWhiteSpace(false)
                       .withIgnoreQuotations(true)
                       .withKeepCarriageReturn(false) //default
                       .withMappingStrategy(map)
                       .withQuoteChar('!')
                       .withSeparator('#')
                       .withSkipLines(1)
                       .withStrictQuotes(false) // default
                       .withThrowExceptions(false)
                       .withType(AnnotatedMockBeanFull.class)
                       .withVerifyReader(false)
                       .withMultilineLimit(Integer.MAX_VALUE)
                       .build();
       List<AnnotatedMockBeanFull> result = csvtb.parse();
       
       // Three lines, one filtered, one throws an exception
       assertEquals(1, result.size());
       assertEquals(1, csvtb.getCapturedExceptions().size());
       AnnotatedMockBeanFull bean = result.get(0);
       assertEquals("\ttest string of everything!", bean.getStringClass());
       assertTrue(bean.getBoolWrapped());
       assertFalse(bean.isBoolPrimitive());
       assertTrue(bean.getByteWrappedDefaultLocale() == 1);
       // Nothing else really matters
   }
   
   @Test
   public void testColumnMappingStrategyWithBuilder() throws FileNotFoundException {
       List<AnnotatedMockBeanFull> result =
               new CsvToBeanBuilder<AnnotatedMockBeanFull>(new FileReader("src/test/resources/testinputposfullgood.csv"))
                       .withSeparator(';')
                       .withType(AnnotatedMockBeanFull.class)
                       .build()
                       .parse();
       assertEquals(2, result.size());
   }
   
   @Test
   public void testMappingWithoutAnnotationsWithBuilder() {
       List<MockBean> result =
               new CsvToBeanBuilder<MockBean>(new StringReader(TEST_STRING))
                       .withType(MockBean.class)
                       .build()
                       .parse();
       assertEquals(2, result.size());
   }
}
