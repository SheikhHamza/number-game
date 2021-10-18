package com.example.numbergame.common.error;

import org.springframework.http.HttpStatus;

public class PlayerException extends RuntimeException {
  private final String message;
  private final HttpStatus status;

  public PlayerException(String message, HttpStatus status) {
    super(message);
    this.message = message;
    this.status = status;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
