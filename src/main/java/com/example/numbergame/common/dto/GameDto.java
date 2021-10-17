package com.example.numbergame.common.dto;

import com.example.numbergame.game.constant.GameStatus;
import com.example.numbergame.game.entity.GameEntity;

public class GameDto {
  private String gameId;
  private String playerId;
  private GameStatus gameStatus;
  private Integer lastMove;
  private Integer currentNumber;
  private boolean playerTurn;
  private GameResult gameResult;

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

    return gameDto;
  }

  public enum GameResult {
    WINNER,
    LOSER;
  }
}
