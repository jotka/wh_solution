package pl.finsys;

import java.time.LocalDateTime;

/**
 * Log entry domain object
 */
public class LogEntry {
    private LocalDateTime time;
    private String ip;
    private int status;

    public String getIp() {
        return ip;
    }

    public LogEntry(LocalDateTime time, String ip, int status) {
        this.time = time;
        this.ip = ip;
        this.status = status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return time + "|" + ip + "|" +  + status;
    }
}
