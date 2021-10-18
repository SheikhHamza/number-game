package com.example.numbergame.common.dto;

public class SendNumberRequest {
  private String playerId;
  private String gameId;
  private Integer number;

  public SendNumberRequest(String playerId, String gameId, Integer number) {
    this.playerId = playerId;
    this.gameId = gameId;
    this.number = number;
  }

  public String getPlayerId() {
    return playerId;
  }

  public String getGameId() {
    return gameId;
  }

  public Integer getNumber() {
    return number;
  }
}
