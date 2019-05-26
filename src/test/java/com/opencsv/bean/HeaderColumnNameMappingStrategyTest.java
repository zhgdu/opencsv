package com.opencsv.bean;

/*
 Copyright 2007 Kyle Miller.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import com.opencsv.CSVReader;
import com.opencsv.bean.mocks.MockBean;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class HeaderColumnNameMappingStrategyTest {

   private static final String TEST_STRING = "name,orderNumber,num\n" +
         "kyle,abc123456,123\n" +
         "jimmy,def098765,456";

    private static final String TEST_EMPTY_STRING = "name,orderNumber,num\n" +
            "kyle,   ,123\n" +
            "jimmy,,456";

   private static final String TEST_QUOTED_STRING = "\"name\",\"orderNumber\",\"num\"\n" +
         "\"kyle\",\"abc123456\",\"123\"\n" +
         "\"jimmy\",\"def098765\",\"456\"";

   private HeaderColumnNameMappingStrategy<MockBean> strat;
   private static Locale systemLocale;

    @BeforeAll
    public static void storeSystemLocale() {
        systemLocale = Locale.getDefault();
    }

    @AfterEach
    public void setSystemLocaleBackToDefault() {
        Locale.setDefault(systemLocale);
    }

    @BeforeEach
   public void setUp() {
      Locale.setDefault(Locale.US);
      strat = new HeaderColumnNameMappingStrategy<>();
   }

   private List<MockBean> createTestParseResult(String parseString) {
      strat.setType(MockBean.class);
      CsvToBean<MockBean> csv = new CsvToBeanBuilder<MockBean>(new StringReader(parseString))
              .withMappingStrategy(strat).build();
      return csv.parse();
   }

   @Test
   public void testParse() {
      List<MockBean> list = createTestParseResult(TEST_STRING);
      assertNotNull(list);
      assertEquals(2, list.size());
      MockBean bean = list.get(0);
      assertEquals("kyle", bean.getName());
      assertEquals("abc123456", bean.getOrderNumber());
      assertEquals(123, bean.getNum());
   }

    @Test
    public void testParseWithEmptyField() {
        List<MockBean> list = createTestParseResult(TEST_EMPTY_STRING);
        assertNotNull(list);
        assertEquals(2, list.size());
        MockBean bean = list.get(0);
        assertEquals("kyle", bean.getName());
        assertEquals("   ", bean.getOrderNumber());
        assertEquals(123, bean.getNum());
        bean = list.get(1);
        assertEquals("jimmy", bean.getName());
        assertNotNull(bean.getOrderNumber());
        assertEquals("", bean.getOrderNumber());
        assertEquals(456, bean.getNum());
    }

   @Test
   public void testQuotedString() {
      List<MockBean> list = createTestParseResult(TEST_QUOTED_STRING);
      assertNotNull(list);
      assertEquals(2, list.size());
      MockBean bean = list.get(0);
      assertEquals("kyle", bean.getName());
      assertEquals("abc123456", bean.getOrderNumber());
      assertEquals(123, bean.getNum());
   }

   @Test
   public void testParseWithSpacesInHeader() {
      List<MockBean> list = createTestParseResult(TEST_STRING);
      assertNotNull(list);
      assertEquals(2, list.size());
      MockBean bean = list.get(0);
      assertEquals("kyle", bean.getName());
      assertEquals("abc123456", bean.getOrderNumber());
      assertEquals(123, bean.getNum());
   }

   @Test
   public void verifyColumnNames() throws IOException, CsvRequiredFieldEmptyException {
      strat = new HeaderColumnNameMappingStrategy<>();
      strat.setType(MockBean.class);
      assertNull(strat.getColumnName(0));

      StringReader reader = new StringReader(TEST_STRING);

      CSVReader csvReader = new CSVReader(reader);
      strat.captureHeader(csvReader);

      assertEquals("name", strat.getColumnName(0));
   }
   
   @Test
   public void throwsIllegalStateExceptionIfTypeNotSetBeforeParse() {
      strat = new HeaderColumnNameMappingStrategy<>();
      StringReader reader = new StringReader(TEST_STRING);
      CSVReader csvReader = new CSVReader(reader);
      CsvToBean<Object> csvtb = new CsvToBeanBuilder<>(csvReader)
              .withMappingStrategy(strat).build();
      try {
          csvtb.parse();
      }
      catch(RuntimeException e) {
          assertEquals(IllegalStateException.class, e.getCause().getClass());
      }
   }

    @Test
   public void throwsIllegalStateExceptionIfTypeNotSetBeforeGenerateHeaders() throws CsvRequiredFieldEmptyException {
      strat = new HeaderColumnNameMappingStrategy<>();
        Assertions.assertThrows(IllegalStateException.class, () -> {
            strat.generateHeader(new MockBean());
        });
   }
}
