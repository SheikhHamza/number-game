package com.example.numbergame.game.entity;

import com.example.numbergame.game.constant.GameStatus;
import com.example.numbergame.game.constant.MoveType;

import java.util.UUID;

public class GameEntity {
  private final String gameId;
  private final String playerOneId;
  private String playerTwoId;
  private Integer currentNumber;
  private Integer lastMove;
  private MoveType moveType;
  private GameStatus gameStatus;
  private Boolean playerOneTurn;

  public GameEntity(String playerOneId) {
    this.gameId = UUID.randomUUID().toString();
    this.playerOneId = playerOneId;
    this.moveType = MoveType.FIRST_MOVE;
    this.gameStatus = GameStatus.IN_PROGRESS;
    this.playerOneTurn = true;
  }

  public String getPlayerOneId() {
    return playerOneId;
  }

  public String getPlayerTwoId() {
    return playerTwoId;
  }

  public void setPlayerTwoId(String playerTwoId) {
    this.playerTwoId = playerTwoId;
  }

  public Integer getCurrentNumber() {
    return currentNumber;
  }

  public void setCurrentNumber(Integer currentNumber) {
    this.currentNumber = currentNumber;
  }

  public MoveType getMoveType() {
    return moveType;
  }

  public void setMoveType(MoveType moveType) {
    this.moveType = moveType;
  }

  public GameStatus getGameStatus() {
    return gameStatus;
  }

  public void setGameStatus(GameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }

  public boolean isGamePlayer(String playerId) {
    return playerOneId.equals(playerId) || (playerTwoId != null && playerTwoId.equals(playerId));
  }

  public String getGameId() {
    return gameId;
  }

  public Integer getLastMove() {
    return lastMove;
  }

  public void setLastMove(Integer lastMove) {
    this.lastMove = lastMove;
  }

  public Boolean getPlayerOneTurn() {
    return playerOneTurn;
  }

  public void setPlayerOneTurn(Boolean playerOneTurn) {
    this.playerOneTurn = playerOneTurn;
  }
}
