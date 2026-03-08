package com.sergzak.deviceapi.exception;

import java.time.OffsetDateTime;

import com.sergzak.deviceapi.v1.dto.ErrorResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(DeviceInUseException.class)
    public ResponseEntity<Object> handleDeviceInUse(DeviceInUseException exception) {
        return buildErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<Object> handleDeviceNotFound(DeviceNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return buildErrorResponse(HttpStatus.valueOf(statusCode.value()), ex.getMessage());
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {
        final ErrorResponse error = new ErrorResponse()
            .timestamp(OffsetDateTime.now())
            .status(status.value())
            .error(status.getReasonPhrase())
            .message(message);

        return new ResponseEntity<>(error, status);
    }
}
