package ru.darkkeks.isdelectivebackend;

import java.io.File;

public class ConversionResult {

    private File file;
    private Status status;
    private long timeElapsed;
    private Throwable exception;

    public ConversionResult(File file, Status status, long timeElapsed, Throwable exception) {
        this.file = file;
        this.status = status;
        this.timeElapsed = timeElapsed;
        this.exception = exception;
    }

    public File getFile() {
        return file;
    }

    public Status getStatus() {
        return status;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public Throwable getException() {
        return exception;
    }

    public enum Status {
        OK, TIMEOUT, ERROR
    }

}
