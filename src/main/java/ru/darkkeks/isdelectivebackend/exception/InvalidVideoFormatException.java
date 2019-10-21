package ru.darkkeks.isdelectivebackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid video format")
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
