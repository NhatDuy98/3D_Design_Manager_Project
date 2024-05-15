package org.design_manager_project.exeptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.design_manager_project.dtos.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject> handleGeneralException(Exception exception){
        return ResponseEntity.internalServerError().body(
                ResponseObject.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseObject> handleEntityNotFoundException(EntityNotFoundException e){
        return ResponseEntity.badRequest().body(
                ResponseObject.builder()
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }
    @ExceptionHandler(UserNotActiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseObject> handleUserNotActive(UserNotActiveException e){
        return ResponseEntity.badRequest().body(
                ResponseObject.builder()
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }
    @ExceptionHandler(EmailExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseObject> handleEmailExists(EmailExistsException e){
        return ResponseEntity.badRequest().body(
                ResponseObject.builder()
                        .message(e.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .build()
        );
    }

    @ExceptionHandler(DeletedException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ResponseObject> handleEmailExists(DeletedException e){
        return ResponseEntity.badRequest().body(
                ResponseObject.builder()
                        .message(e.getMessage())
                        .status(HttpStatus.NO_CONTENT)
                        .build()
        );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseObject> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError){
                        return ((FieldError) error).getField() + ": " + error.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                }).toList();

        String combine = String.join(", ", errorMessages);

        return ResponseEntity.badRequest().body(
                ResponseObject.builder()
                        .message(combine)
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseObject> handleConstraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        Set<String> uniqueMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        String combinedErrorMessage = String.join(" - ", uniqueMessages) + ".";

        return ResponseEntity.badRequest().body(
                ResponseObject.builder()
                        .message(combinedErrorMessage)
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }


}
