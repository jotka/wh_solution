package pl.finsys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;


/**
 * Parses the log file and stores it in the database (table LOG).
 * Outputs the IP entries that are above the threshold to the console.
 * Stores the entries above the threshold in the database (table THRESHOLD_LOG)
 */
public class Parser {
    private final LocalDateTime startDate;
    private final Duration duration;
    private final int threshold;

    private final static Logger logger = LoggerFactory.getLogger(Parser.class);

    public Parser(LocalDateTime startDate, Duration duration, int threshold) {
        this.startDate = startDate;
        this.duration = duration;
        this.threshold = threshold;
    }

    public void process(String filename) {
        logger.info("Processing file {}, with start date {}, duration {}, threshold {}.",
                filename, ParserTools.DATE_TIME_FORMATTER.format(startDate), duration, threshold);
        Database database = new Database();
        try {
            List<LogEntry> records = readLog(filename);

            database.connect();
            database.saveLog(records);

            Stream<LogEntry> filteredByDate = ParserTools.filterEntriesByDate(records, duration, startDate);
            Map<String, List<LogEntry>> grouped = ParserTools.groupByIP(filteredByDate);

            Map<String, List<LogEntry>> filteredAboveThreshold = ParserTools.filterAboveThreshold(grouped, threshold);

            filteredAboveThreshold.forEach((ip, logEntries) -> {
                logger.info(ip);
                try {
                    database.saveThresholdLog(startDate, threshold, duration.name(), logEntries);
                } catch (SQLException e) {
                    logger.error("Unable to save threshold log.");
                }
            });
        } catch (Exception exc) {
            logger.error(exc.getMessage());
        } finally {
            database.disconnect();
        }
        logger.info("Processing done.");
    }

    private List<LogEntry> readLog(String fileName) {
        List<LogEntry> records = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            lines.forEachOrdered(line -> records.add(ParserTools.parseEntry(line)));
        } catch (IOException e) {
            logger.error("Cannot read the log file " + fileName);
        }

        return records;
    }
}
