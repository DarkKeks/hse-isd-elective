package ru.darkkeks.isdelectivebackend;

public class InvalidVideoFormatException extends RuntimeException {
    private String formatName;

    public InvalidVideoFormatException(String formatName) {
        super(String.format("Invalid video format: %s", formatName));
        this.formatName = formatName;
    }

    public String getFormatName() {
        return formatName;
    }
}
