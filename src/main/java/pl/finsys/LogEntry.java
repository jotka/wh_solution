package pl.finsys;

import java.time.LocalDateTime;

/**
 * Log entry domain object
 */
class LogEntry {
    private LocalDateTime time;
    private String ip;
    private int status;

    String getIp() {
        return ip;
    }

    LogEntry(LocalDateTime time, String ip, int status) {
        this.time = time;
        this.ip = ip;
        this.status = status;
    }

    LocalDateTime getTime() {
        return time;
    }

    int getStatus() {
        return status;
    }
}
