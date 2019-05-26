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

import com.opencsv.bean.mocks.MockBean;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class ColumnPositionMappingStrategyTest {
   private ColumnPositionMappingStrategy<MockBean> strat;

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
      strat = new ColumnPositionMappingStrategy<>();
      strat.setType(MockBean.class);
   }

   @Test
   public void testParse() {
      String s = "" +
            "kyle,123456,emp123,1\n" +
            "jimmy,abcnum,cust09878,2";

      strat.setColumnMapping("name", "orderNumber", "id", "num");

      CsvToBean<MockBean> csv = new CsvToBeanBuilder<MockBean>(new StringReader(s))
              .withMappingStrategy(strat)
              .build();
      List<MockBean> list = csv.parse();
      assertNotNull(list);
      assertEquals(2, list.size());
      MockBean bean = list.get(0);
      assertEquals("kyle", bean.getName());
      assertEquals("123456", bean.getOrderNumber());
      assertEquals("emp123", bean.getId());
      assertEquals(1, bean.getNum());
   }

   @Test
   public void testParseWithTrailingSpaces() {
      String s = "" +
            "kyle  ,123456  ,emp123  ,  1   \n" +
            "jimmy,abcnum,cust09878,2   ";

      String[] columns = new String[]{"name", "orderNumber", "id", "num"};
      strat.setColumnMapping(columns);

      CsvToBean<MockBean> csv = new CsvToBeanBuilder<MockBean>(new StringReader(s))
              .withMappingStrategy(strat)
              .build();
      List<MockBean> list = csv.parse();
      assertNotNull(list);
      assertEquals(2, list.size());
      MockBean bean = list.get(0);
      assertEquals("kyle  ", bean.getName());
      assertEquals("123456  ", bean.getOrderNumber());
      assertEquals("emp123  ", bean.getId());
      assertEquals(1, bean.getNum());
   }

   @Test
   public void testParseEmptyInput() {
      String[] columns = new String[]{"name", "orderNumber", "id", "num"};
      strat.setColumnMapping(columns);

      CsvToBean<MockBean> csv = new CsvToBeanBuilder<MockBean>(new StringReader(StringUtils.EMPTY))
              .withMappingStrategy(strat)
              .build();
      List<MockBean> list = csv.parse();
      assertNotNull(list);
      assertTrue(list.isEmpty());
   }

   @Test
   public void testGetColumnMapping() {
      String[] columnMapping = strat.getColumnMapping();
      assertNotNull(columnMapping);
      assertEquals(0, columnMapping.length);

      String[] columns = new String[]{"name", "orderNumber", "id"};
      strat.setColumnMapping(columns);

      columnMapping = strat.getColumnMapping();
      assertNotNull(columnMapping);
      assertEquals(3, columnMapping.length);
      assertArrayEquals(columns, columnMapping);

   }

   @Test
   public void testGetColumnNames() {

      strat.setColumnMapping("name", null, "id");

      assertEquals("name", strat.getColumnName(0));
      assertNull(strat.getColumnName(1));
      assertEquals("id", strat.getColumnName(2));
      assertNull(strat.getColumnName(3));
   }

   @Test
   public void testGetColumnNamesArray() {

      strat.setColumnMapping("name", null, "id");
      String[] mapping = strat.getColumnMapping();

      assertEquals(3, mapping.length);
      assertEquals("name", mapping[0]);
      assertNull(mapping[1]);
      assertEquals("id", mapping[2]);
   }

   @Test
   public void getColumnNamesWhenNullArray() {
      strat.setColumnMapping((String[]) null);

      assertNull(strat.getColumnName(0));
      assertNull(strat.getColumnName(1));
      assertArrayEquals(new String[0], strat.getColumnMapping());
   }

   @Test
   public void getColumnNamesWhenNullColumnName() {
      String[] columns = {null};
      strat.setColumnMapping(columns);

      assertNull(strat.getColumnName(0));
      assertNull(strat.getColumnName(1));
      assertArrayEquals(columns, strat.getColumnMapping());
   }

   @Test
   public void getColumnNamesWhenEmptyMapping() {
      strat.setColumnMapping();

      assertNull(strat.getColumnName(0));
      assertArrayEquals(new String[0], strat.getColumnMapping());
   }
   
   @Test
   public void throwsIllegalStateExceptionIfTypeNotSet() {
      String englishErrorMessage = null;
      try {
          new CsvToBeanBuilder<MockBean>(new StringReader("doesnt,matter\nat,all"))
                  .withMappingStrategy(new ColumnPositionMappingStrategy<>())
                  .build().parse();
          fail("RuntimeException with inner IllegalStateException should have been thrown.");
      }
      catch(RuntimeException e) {
          assertEquals(IllegalStateException.class, e.getCause().getClass());
          englishErrorMessage = e.getCause().getLocalizedMessage();
      }
      
      // Now with a different locale
      try {
         new CsvToBeanBuilder<MockBean>(new StringReader("doesnt,matter\nat,all"))
                 .withMappingStrategy(new ColumnPositionMappingStrategy<>())
                 .withErrorLocale(Locale.GERMAN)
                 .build().parse();
          fail("RuntimeException with inner IllegalStateException should have been thrown.");
      }
      catch(RuntimeException e) {
          assertEquals(IllegalStateException.class, e.getCause().getClass());
          assertNotSame(englishErrorMessage, e.getCause().getLocalizedMessage());
      }
   }
}
