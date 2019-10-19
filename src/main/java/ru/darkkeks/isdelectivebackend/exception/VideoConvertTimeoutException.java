package ru.darkkeks.isdelectivebackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT, reason = "Video converting took too long")
public class VideoConvertTimeoutException extends RuntimeException {
}
