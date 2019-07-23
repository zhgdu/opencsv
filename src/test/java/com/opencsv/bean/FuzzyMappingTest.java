package com.opencsv.bean;

import com.opencsv.bean.mocks.FuzzyMock;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link FuzzyMappingStrategy}.
 * @author Andrew Rucker Jones
 */
public class FuzzyMappingTest {

    private static final String HEADER_FRAGMENT = "integerHeader,splitIntegerHeader,joinedIntegerHeader,joinedIntegerHeader,booleanHeader,integer header,split integer header,joined integer header,boolean header,exactMatch";
    private static final String HEADERS = HEADER_FRAGMENT + ",this header name has nothing to do with the member it matches\n";
    private static final String INCOMPLETE_HEADERS = HEADER_FRAGMENT + "\n";
    private static final String EXACT_DATA = "exact works";
    private static final String WILD_DATA = "wildly inexact works";
    private static final String DATA_FRAGMENT = "1,2 3,4,5,wahr,6,7,8,false," + EXACT_DATA;
    private static final String DATA = DATA_FRAGMENT + "," + WILD_DATA + "\n";
    private static final String INCOMPLETE_DATA = DATA_FRAGMENT + "\n";

    private void testAllButWildlyInexact(FuzzyMock bean) {
        assertEquals(1, bean.getIntHeader());
        assertEquals(Arrays.asList(2, 3), bean.getSplitIntHeaders());
        assertEquals(Arrays.asList(4, 5), bean.getJoinedIntHeaders().get("joinedIntegerHeader"));
        assertTrue(bean.getBoolHeader());
        assertEquals(6, bean.getIntegerHeader());
        assertEquals(7, bean.getSplitIntegerHeader());
        assertEquals(8, bean.getJoinedIntegerHeader());
        assertFalse(bean.isBooleanHeader());
        assertEquals(EXACT_DATA, bean.getExactMatch());
    }

    /**
     * Tests reading with {@link FuzzyMappingStrategy}.
     * <p>Also incidentally tests:<ul>
     *     <li>Precedence of explicit annotations (all name-based types)</li>
     *     <li>Exact matches between header names and member variable names</li>
     *     <li>Inexact but close matches between header names and variable names</li>
     *     <li>Wildly inexact matches between header names and variable names</li>
     *     <li>All headers and all member variables consumed in matching</li>
     * </ul></p>
     */
    @Test
    public void testReadingFuzzy() {
        MappingStrategy<FuzzyMock> strategy = new FuzzyMappingStrategy<>();
        strategy.setType(FuzzyMock.class);
        StringReader input = new StringReader(HEADERS + DATA);
        List<FuzzyMock> beans = new CsvToBeanBuilder<FuzzyMock>(input)
                .withMappingStrategy(strategy)
                .build().parse();
        assertNotNull(beans);
        assertEquals(1, beans.size());
        FuzzyMock bean = beans.get(0);
        testAllButWildlyInexact(bean);
        assertEquals(WILD_DATA, bean.getWildlyInexactMatch());
    }

    @Test
    public void testHeadersUnmatched() {
        MappingStrategy<FuzzyMock> strategy = new FuzzyMappingStrategy<>();
        strategy.setType(FuzzyMock.class);
        final String data = "potentially unmatched data";
        StringReader input = new StringReader("potentially unmatched header," + HEADERS + data + "," + DATA);
        List<FuzzyMock> beans = new CsvToBeanBuilder<FuzzyMock>(input)
                .withMappingStrategy(strategy)
                .build().parse();
        assertNotNull(beans);
        assertEquals(1, beans.size());
        FuzzyMock bean = beans.get(0);
        testAllButWildlyInexact(bean);
        assertTrue(WILD_DATA.equals(bean.getWildlyInexactMatch()) ^ data.equals(bean.getWildlyInexactMatch()));
    }

    @Test
    public void testVariablesUnmatched() {
        MappingStrategy<FuzzyMock> strategy = new FuzzyMappingStrategy<>();
        strategy.setType(FuzzyMock.class);
        StringReader input = new StringReader(INCOMPLETE_HEADERS + INCOMPLETE_DATA);
        List<FuzzyMock> beans = new CsvToBeanBuilder<FuzzyMock>(input)
                .withMappingStrategy(strategy)
                .build().parse();
        assertNotNull(beans);
        assertEquals(1, beans.size());
        FuzzyMock bean = beans.get(0);
        testAllButWildlyInexact(bean);
        assertNull(bean.getWildlyInexactMatch());
    }

    @Test
    public void testWritingFuzzy() throws CsvException {
        FuzzyMock bean = new FuzzyMock();
        bean.setBooleanHeader(true);
        bean.setBoolHeader(Boolean.FALSE);
        bean.setExactMatch(EXACT_DATA);
        bean.setIntegerHeader(1);
        bean.setIntHeader(2);
        bean.setJoinedIntegerHeader(3);
        MultiValuedMap<String, Integer> map = new ArrayListValuedHashMap<>();
        map.put("joinedIntegerHeader", 4);
        map.put("joinedIntegerHeader", 5);
        bean.setJoinedIntHeaders(map);
        bean.setSplitIntegerHeader(6);
        bean.setSplitIntHeaders(Arrays.asList(7, 8));
        bean.setWildlyInexactMatch(WILD_DATA);
        StringWriter output = new StringWriter();
        MappingStrategy<FuzzyMock> strategy = new FuzzyMappingStrategy<>();
        strategy.setType(FuzzyMock.class);
        new StatefulBeanToCsvBuilder<FuzzyMock>(output)
                .withMappingStrategy(strategy)
                .withApplyQuotesToAll(false)
                .build().write(bean);
        assertEquals("BOOLEANHEADER,INTEGERHEADER,SPLITINTEGERHEADER,joinedIntegerHeader,joinedIntegerHeader\n" +
                "falsch,2,7 8,4,5\n", output.toString()); // Because nothing else is annotated
    }
}
