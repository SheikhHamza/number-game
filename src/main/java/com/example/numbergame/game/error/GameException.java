package com.example.numbergame.game.error;

import org.springframework.http.HttpStatus;

public class GameException extends RuntimeException {
  private final String message;
  private final HttpStatus status;

  public GameException(String message, HttpStatus status) {
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
