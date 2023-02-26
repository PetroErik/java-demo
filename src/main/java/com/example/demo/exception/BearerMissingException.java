package com.example.demo.exception;

public class BearerMissingException extends Exception {

  public BearerMissingException() {
    super("Bearer missing from request");
  }
}
