package pl.finsys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

/**
 * Handles the database connection
 */
public class Database {
    private final static Logger logger = LoggerFactory.getLogger(Database.class);
    private static final String DB_PROPERTIES = "db.properties";
    private static final String PROP_DRIVER_CLASS = "driverClass";
    private static final String PROP_DB_URL = "dbUrl";
    private static final String PROP_USER_NAME = "userName";
    private static final String PROP_PASSWORD = "password";

    private Connection connection;

    public void connect() throws ClassNotFoundException, SQLException, IOException {
        final Properties prop = new Properties();
        try (final InputStream stream = this.getClass().getResourceAsStream(DB_PROPERTIES)) {
            prop.load(stream);
        }
        Class.forName(prop.getProperty(PROP_DRIVER_CLASS));
        this.connection = DriverManager.getConnection(prop.getProperty(PROP_DB_URL), prop.getProperty(PROP_USER_NAME), prop.getProperty(PROP_PASSWORD));
    }

    public void saveLog(List<LogEntry> entries) throws SQLException {
        for (LogEntry logEntry : entries) {
            saveSingleLogEntry(logEntry);
        }
    }

    private void saveSingleLogEntry(LogEntry logEntry) throws SQLException {
        String query = "REPLACE INTO LOG(DATETIME,IP,METHOD,STATUS,USER_AGENT VALUES (?,?,?,?,?)";
        PreparedStatement ps = this.connection.prepareStatement(query);
        ps.setTimestamp(1, Timestamp.valueOf(logEntry.getTime()));
        ps.setString(2, logEntry.getIp());
        ps.setString(3, logEntry.getMethod());
        ps.setInt(4, logEntry.getStatus());
        ps.setString(5, logEntry.getUserAgent());
    }

    public void saveThresholdLog(LocalDateTime startDate, int threshold, String duration,
                                 List<LogEntry> thresholdEntries) throws SQLException {
        for (LogEntry logEntry : thresholdEntries) {
            saveSingleThresholdLogEntry(logEntry, startDate, threshold, duration);
        }
    }

    private void saveSingleThresholdLogEntry(LogEntry logEntry, LocalDateTime startDate,
                                             int threshold, String duration) throws SQLException {
        String query = "REPLACE INTO THRESHOLD_LOG(DATETIME,IP,START_DATE,THRESHOLD,DURATION,STATUS (?,?,?,?,?,?)";
        PreparedStatement ps = this.connection.prepareStatement(query);
        ps.setTimestamp(1, Timestamp.valueOf(logEntry.getTime()));
        ps.setString(2, logEntry.getIp());
        ps.setTimestamp(3, Timestamp.valueOf(startDate));
        ps.setInt(4, threshold);
        ps.setString(5, duration);
        ps.setInt(6, logEntry.getStatus());
    }

    public void disconnect() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
    }
}
