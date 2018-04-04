package pl.finsys;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.finsys.entity.Address;
import pl.finsys.entity.Log;

public class Parser {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
    private final LocalDateTime startDate;
    private final Duration duration;
    private final int treshold;

    private ArrayList<LogEntry> records = new ArrayList<>();
    private final static Logger logger = Logger.getLogger(HttpErrorCodes.class);

    public Parser(LocalDateTime startDate, Duration duration, int treshold) {
        this.startDate = startDate;
        this.duration = duration;
        this.treshold = treshold;
    }

    public void process(String filename) {
        records = readFile(filename);
        Stream<LogEntry> filteredByDate = filterEntriesByDate(records);
        Map<String, List<LogEntry>> groupped = filteredByDate.collect(Collectors.groupingBy(LogEntry::getIp, Collectors.toList()));

        Map<String, List<LogEntry>> filteredAboveThreshold = groupped.entrySet()
                .stream()
                .filter(e -> e.getValue().size() > treshold)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        Session session = DbUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            filteredAboveThreshold.forEach((ip, logEntries) -> {
                System.out.println(ip);
                Address address = new Address(ip);
                session.saveOrUpdate(address);

                logEntries.forEach(logEntry -> {
                    Log log = new Log(DATE_TIME_FORMATTER.format(logEntry.getTime()), logEntry.getStatus(), address);
                    address.getLogs().add(log);
                    session.saveOrUpdate(log);
                });
            });

            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            session.close();
        }
    }

    private Stream<LogEntry> filterEntriesByDate(ArrayList<LogEntry> records) {
        LocalDateTime endDateTime = duration.equals(Duration.hourly) ? startDate.plusHours(1) : startDate.plusDays(1);

        return records.stream().filter(
                record -> record.getTime().isAfter(startDate) && record.getTime().isBefore(endDateTime));
    }

    private ArrayList<LogEntry> readFile(String fileName) {
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            lines.forEachOrdered(line -> records.add(parseEntry(line)));
        } catch (IOException e) {
            logger.error("Cannot read the log file " + fileName);
        }
        return records;
    }

    /**
     * 2017-01-01 23:59:28.740|192.168.112.165|"GET / HTTP/1.1"|200|"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0"
     *
     * @param line log line
     * @return parsed log entry object
     */
    private LogEntry parseEntry(String line) {
        List<String> lineItems = Stream.of(line.split("\\|")).collect(Collectors.toList());
        LocalDateTime date = LocalDateTime.parse(lineItems.get(0), DATE_TIME_FORMATTER);
        String ip = lineItems.get(1);
        int status = Integer.parseInt(lineItems.get(3));

        return new LogEntry(date, ip, status);
    }
}
