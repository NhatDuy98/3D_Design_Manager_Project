package org.design_manager_project.exeptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.design_manager_project.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse> handleGeneralException(Exception exception){
        return ResponseEntity.internalServerError().body(
                ApiResponse.builder()
                        .message(exception.getMessage())
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .success(false)
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException e){
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .success(false)
                        .build()
        );
    }
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleUserNotActive(BadRequestException e){
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .success(false)
                        .build()
        );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(fieldError -> {
                    if (fieldError instanceof FieldError){
                        return ((FieldError) fieldError).getField() + ": " + fieldError.getDefaultMessage();
                    }
                    return fieldError.getDefaultMessage();
                }).toList();

        String combine = String.join(" - ", errorMessages);

        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .message(combine)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .success(false)
                        .build()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        Set<String> uniqueMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        String combinedErrorMessage = String.join(" - ", uniqueMessages) + ".";

        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .message(combinedErrorMessage)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .success(false)
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid UUID format: " + ex.getValue();
        ApiResponse responseObject = ApiResponse.builder()
                .message(message)
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        String message = "Invalid input: " + ex.getMessage();
        ApiResponse responseObject = ApiResponse.builder()
                .message(message)
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleNoSuchFieldException(NoSuchFieldException ex){
        return ResponseEntity.badRequest().body(
                ApiResponse.badRequest("Field not found: " + ex.getMessage())
        );
    }
    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleIllegalAccessException(IllegalAccessException ex){
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .message("Illegal access to field: " + ex.getMessage())
                        .code(HttpStatus.FORBIDDEN.value())
                        .status(HttpStatus.FORBIDDEN)
                        .success(false)
                        .build());
    }

}
