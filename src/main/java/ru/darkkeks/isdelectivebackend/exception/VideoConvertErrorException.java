package ru.darkkeks.isdelectivebackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class VideoConvertErrorException extends RuntimeException {
    public VideoConvertErrorException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
