package pl.finsys;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ParserToolsTest {

    @Test
    void parseEntry() {
        String line = "2017-01-01 23:59:28.740|192.168.112.165|\"GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0\"";

        LogEntry entry = ParserTools.parseEntry(line);


    }

    @Test
    void filterEntriesByDate() {
    }

    @Test
    void groupByIP() {
    }

    @Test
    void filterAboveThreshold() {
    }
}