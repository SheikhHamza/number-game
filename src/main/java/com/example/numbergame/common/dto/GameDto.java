package com.example.numbergame.common.dto;

import com.example.numbergame.game.constant.GameStatus;
import com.example.numbergame.game.entity.GameEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class GameDto {
  private String gameId;
  private String playerId;
  private GameStatus gameStatus;
  private Integer lastMove;
  private Integer currentNumber;
  private boolean playerTurn;
  private GameResult gameResult;
  private List<Integer> inputOptions;

  public static GameDto from(GameEntity gameEntity, boolean isFirstPlayer, boolean playerTurn) {
    GameDto gameDto = new GameDto();

    gameDto.gameId = gameEntity.getGameId();
    gameDto.playerId = isFirstPlayer ? gameEntity.getPlayerOneId() : gameEntity.getPlayerTwoId();
    gameDto.playerTurn = playerTurn;
    gameDto.gameStatus = gameEntity.getGameStatus();
    gameDto.lastMove = gameEntity.getLastMove();
    gameDto.currentNumber = gameEntity.getCurrentNumber();

    if (GameStatus.FINISHED.equals(gameEntity.getGameStatus())) {
      gameDto.gameResult = !playerTurn ? GameResult.WINNER : GameResult.LOSER;
    }
    if (gameEntity.getMoveType() != null) {
      gameDto.inputOptions = gameEntity.getMoveType().getInputOptions();
    }
    return gameDto;
  }

  public List<Integer> getInputOptions() {
    return inputOptions;
  }

  public String getGameId() {
    return gameId;
  }

  public String getPlayerId() {
    return playerId;
  }

  public GameStatus getGameStatus() {
    return gameStatus;
  }

  public Integer getLastMove() {
    return lastMove;
  }

  public Integer getCurrentNumber() {
    return currentNumber;
  }

  public boolean isPlayerTurn() {
    return playerTurn;
  }

  public GameResult getGameResult() {
    return gameResult;
  }

  public enum GameResult {
    WINNER,
    LOSER;
  }
}
