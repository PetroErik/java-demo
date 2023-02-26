package com.example.demo.exception.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.example.demo.controller.CourseController;
import com.example.demo.controller.CourseRegistrationController;
import com.example.demo.controller.StudentController;
import com.example.demo.response.GenericErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {StudentController.class, CourseController.class, CourseRegistrationController.class})
public class GeneralExceptionHandler {
  @ExceptionHandler(EmptyResultDataAccessException.class)
  protected ResponseEntity<Object> handleResourceNotExistsException(EmptyResultDataAccessException ex) {
    return new ResponseEntity<>(new GenericErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> fieldValidationFailedResponse(
      MethodArgumentNotValidException exception) {
    List<ObjectError> errors = exception.getBindingResult().getAllErrors();
    String errorMessage = errors.stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage).sorted().collect(Collectors.joining(", "));

    return new ResponseEntity<>(new GenericErrorResponse(errorMessage), BAD_REQUEST);
  }

  @ExceptionHandler(JsonParseException.class)
  protected ResponseEntity<Object> handleInvalidJson() {
    return new ResponseEntity<>(new GenericErrorResponse("Invalid JSON in request body"), BAD_REQUEST);
  }
}
