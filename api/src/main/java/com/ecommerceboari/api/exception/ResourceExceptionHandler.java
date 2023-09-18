package com.ecommerceboari.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> notFound(BadRequestException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = StandardError.builder()
                .message(e.getMessage())
                .error("Not found")
                .path(request.getRequestURI())
                .status(status.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> duplicate(DataIntegrityViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = StandardError.builder()
                .message(e.getMessage())
                .error("Duplicate")
                .path(request.getRequestURI())
                .status(status.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ConflictRequestException.class)
    public ResponseEntity<StandardError> duplicate(ConflictRequestException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError error = StandardError.builder()
                .message(e.getMessage())
                .error("Conflict")
                .path(request.getRequestURI())
                .status(status.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(AddressNullException.class)
    public ResponseEntity<StandardError> address(AddressNullException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = StandardError.builder()
                .message(e.getMessage())
                .error("Not found")
                .path(request.getRequestURI())
                .status(status.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(EmailNotFound.class)
    public ResponseEntity<StandardError> email(EmailNotFound e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = StandardError.builder()
                .message(e.getMessage())
                .error("Not found")
                .path(request.getRequestURI())
                .status(status.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UserUnauthorizedRequestException.class)
    public ResponseEntity<StandardError> unauthorized(UserUnauthorizedRequestException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError error = StandardError.builder()
                .message(e.getMessage())
                .error("Unauthorized")
                .path(request.getRequestURI())
                .status(status.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(status).body(error);
    }
}
