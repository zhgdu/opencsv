package integrationTest.SR93;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class UnescapedQuoteTest {
    private static final String IMPORT_FILE = "src/test/resources/SR93.csv";

    private CSVReader reader;

    @BeforeEach
    public void createReader() throws FileNotFoundException {
        CSVParserBuilder parserBuilder = new CSVParserBuilder().withIgnoreQuotations(true);
        ICSVParser parser = parserBuilder.build();
        CSVReaderBuilder builder = new CSVReaderBuilder(new FileReader(IMPORT_FILE)).withMultilineLimit(1);
        reader = builder.withCSVParser(parser).build();
    }

    @Test
    @DisplayName("Unescaped quotes should not throw exceptions")
    public void processFile() throws IOException {
        String[] line;
        for (line = reader.readNextSilently(); line != null; line = reader.readNextSilently()) {
            System.out.println(Arrays.toString(line));
        }
    }
}
