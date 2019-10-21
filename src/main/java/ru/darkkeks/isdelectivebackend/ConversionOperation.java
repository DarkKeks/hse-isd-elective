package ru.darkkeks.isdelectivebackend;

import java.util.Date;

public class ConversionOperation {

    private Date date;
    private String filename;
    private String ipAddress;
    private VideoFormat toFormat;
    private ConversionResult.Status status;
    private long timeElapsed;

    public ConversionOperation(Date date, String ipAddress, String filename, VideoFormat toFormat,
                               ConversionResult.Status status, long timeElapsed) {
        this.date = date;
        this.ipAddress = ipAddress;
        this.filename = filename;
        this.toFormat = toFormat;
        this.status = status;
        this.timeElapsed = timeElapsed;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Date getDate() {
        return date;
    }

    public String getFilename() {
        return filename;
    }

    public VideoFormat getToFormat() {
        return toFormat;
    }

    public ConversionResult.Status getStatus() {
        return status;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }
}
