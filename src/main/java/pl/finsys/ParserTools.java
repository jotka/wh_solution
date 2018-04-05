package pl.finsys;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParserTools {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    /**
     * 2017-01-01 23:59:28.740|192.168.112.165|"GET / HTTP/1.1"|200|"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0"
     *
     * @param line log line
     * @return parsed log entry object
     */
    public static LogEntry parseEntry(String line) {
        List<String> lineItems = Stream.of(line.split("\\|")).collect(Collectors.toList());
        LocalDateTime date = LocalDateTime.parse(lineItems.get(0), DATE_TIME_FORMATTER);
        String ip = lineItems.get(1);
        int status = Integer.parseInt(lineItems.get(3));

        return new LogEntry(date, ip, status);
    }

    public static Stream<LogEntry> filterEntriesByDate(ArrayList<LogEntry> records, Duration duration, LocalDateTime startDate) {
        LocalDateTime endDateTime = duration.equals(Duration.hourly) ? startDate.plusHours(1) : startDate.plusDays(1);

        return records.stream().filter(
                record -> record.getTime().isAfter(startDate) && record.getTime().isBefore(endDateTime));
    }

    public static Map<String, List<LogEntry>> groupByIP(Stream<LogEntry> entries) {
        return entries.collect(Collectors.groupingBy(LogEntry::getIp, Collectors.toList()));
    }

    public static Map<String, List<LogEntry>> filterAboveThreshold(Map<String, List<LogEntry>> entries, int treshold) {
        return entries.entrySet()
                .stream()
                .filter(e -> e.getValue().size() > treshold)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
