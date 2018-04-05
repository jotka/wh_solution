package pl.finsys;

import org.junit.Test;

public class ParserToolsTest {

    @Test
    public void parseEntry() {
        String line = "2017-01-01 23:59:28.740|192.168.112.165|\"GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0\"";

        LogEntry entry = ParserTools.parseEntry(line);

    }

    @Test
    public void filterEntriesByDate() {
    }

    @Test
    public void groupByIP() {
    }

    @Test
    public void filterAboveThreshold() {
    }
}