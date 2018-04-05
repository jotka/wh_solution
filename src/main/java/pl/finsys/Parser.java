package pl.finsys;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.finsys.entity.Address;
import pl.finsys.entity.Log;

/**
 * Parses the log file and outputs the IP entries that are above the threshold to the console.
 * Stores those entries to database.
 */
public class Parser {
    private final LocalDateTime startDate;
    private final Duration duration;
    private final int threshold;

    private ArrayList<LogEntry> records = new ArrayList<>();
    private final static Logger logger = Logger.getLogger(Parser.class);

    public Parser(LocalDateTime startDate, Duration duration, int threshold) {
        this.startDate = startDate;
        this.duration = duration;
        this.threshold = threshold;
    }

    public void process(String filename) {
        records = readFile(filename);
        Stream<LogEntry> filteredByDate = ParserTools.filterEntriesByDate(records, duration, startDate);
        Map<String, List<LogEntry>> groupped = ParserTools.groupByIP(filteredByDate);

        Map<String, List<LogEntry>> filteredAboveThreshold = ParserTools.filterAboveThreshold(groupped, threshold);

        Session session = DbUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            filteredAboveThreshold.forEach((ip, logEntries) -> {
                System.out.println(ip);
                Address address = new Address(ip);
                logEntries.forEach(logEntry -> address.getLogs().add(new Log(ParserTools.DATE_TIME_FORMATTER.format(logEntry.getTime()), logEntry.getStatus(), address)));
                session.saveOrUpdate(address);
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

    private ArrayList<LogEntry> readFile(String fileName) {
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            lines.forEachOrdered(line -> records.add(ParserTools.parseEntry(line)));
        } catch (IOException e) {
            logger.error("Cannot read the log file " + fileName);
        }
        return records;
    }
}
