package com.retrospecsoptometrists.service.authentication.exceptions;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Incorrect data passed for processing")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        log.error("{}", customError, ex);
        return ResponseEntity.badRequest().body(customError);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorMessage> dateTimeParseExceptionHandler(DateTimeParseException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Date input value is wrong")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        log.error("{}", customError, ex);
        return ResponseEntity.badRequest().body(customError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        log.error("{}", errors, ex);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UserDataManipulationException.class)
    public ResponseEntity<ErrorMessage> userManipulationExceptionHandler(UserDataManipulationException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Error occured while manipulating user details")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        log.error("{}", customError, ex);
        return ResponseEntity.badRequest().body(customError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedExceptionHandler(AccessDeniedException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Error occured while processing for requested credentials: The requested authorities could not be granted nor confirmed")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.FORBIDDEN)
                .build();
        log.error("{}", customError, ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(customError);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorMessage> jsonProcessingExceptionHandler(JsonProcessingException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Error occured in Processing a JSON request: The XML string could not be mapped.")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.EXPECTATION_FAILED)
                .build();
        log.error("{}", customError, ex);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(customError);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorMessage> dataAccessExceptionHandler(DataAccessException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Error occured in the Data Access Object: Request to commit a transaction by accessing the data object failed")
                .errorDescription("One or more requested objects not found, causing violation of constraints")
                .errorCode(HttpStatus.EXPECTATION_FAILED)
                .build();
        log.error("{}", customError, ex);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(customError);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ErrorMessage> parseExceptionHandler(ParseException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .error("Error occured while parsing input data: Part of the input data could not be parsed")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        log.error("{}", customError, ex);
        return ResponseEntity.badRequest().body(customError);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> nullPointerExceptionHandler(NullPointerException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Null Data Object Encountered: The passed input data has bad elements causing a processing error")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        log.error("{}", customError, ex);
        return ResponseEntity.internalServerError().body(customError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> generalExceptionHandler(Exception ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Error Ocurred: Request could not be processed")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.FORBIDDEN)
                .build();
        log.error("{}", customError, ex);
        return ResponseEntity.internalServerError().body(customError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Wrong request type was sent")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        log.error("{}", customError, ex);
        return ResponseEntity.badRequest().body(customError);
    }
}
