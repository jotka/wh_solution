package pl.finsys;

import java.time.LocalDateTime;

/**
 * LogThreshold entry domain object
 */
class LogEntry {
    private LocalDateTime time;
    private String ip;
    private String method;
    private String userAgent;
    private int status;

    String getIp() {
        return ip;
    }

    LogEntry(LocalDateTime time, String ip, int status, String method, String userAgent) {
        this.time = time;
        this.ip = ip;
        this.status = status;
        this.method = method;
        this.userAgent = userAgent;
    }

    public String getMethod() {
        return method;
    }

    public String getUserAgent() {
        return userAgent;
    }

    LocalDateTime getTime() {
        return time;
    }

    int getStatus() {
        return status;
    }
}
