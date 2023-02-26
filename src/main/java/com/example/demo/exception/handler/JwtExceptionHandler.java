package com.example.demo.exception.handler;

import com.example.demo.controller.JwtController;
import com.example.demo.response.GenericErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {JwtController.class})
public class JwtExceptionHandler {
  @ExceptionHandler(BadCredentialsException.class)
  protected ResponseEntity<Object> handleResourceNotExistsException(BadCredentialsException ex) {
    GenericErrorResponse genericErrorResponse = new GenericErrorResponse(ex.getMessage());
    return new ResponseEntity<>(genericErrorResponse, HttpStatus.UNAUTHORIZED);
  }
}
