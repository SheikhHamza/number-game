package com.example.numbergame.game.service;

import com.example.numbergame.common.dto.GameDto;
import com.example.numbergame.common.dto.SendNumberRequest;
import com.example.numbergame.common.error.GameException;
import com.example.numbergame.game.constant.GameStatus;
import com.example.numbergame.game.entity.GameEntity;
import com.example.numbergame.game.entity.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.numbergame.game.constant.MoveType.REGULAR_MOVE;

@Service
public class GameServiceImpl implements GameService {

  private final GameRepository gameRepository;

  public GameServiceImpl(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  @Override
  public GameDto startGame() {
    Optional<GameEntity> existingGame = gameRepository.findInProgressGameHavingPlayerTwoIdNull();
    if (existingGame.isPresent()) {
      String playerTwoId = getNewPlayerId();
      existingGame.get().setPlayerTwoId(playerTwoId);
      return GameDto.from(existingGame.get(), false, !existingGame.get().getPlayerOneTurn());
    }

    GameEntity newGame = new GameEntity(getNewPlayerId());
    return GameDto.from(gameRepository.save(newGame), true, true);
  }

  @Override
  public GameDto sendNumber(SendNumberRequest sendNumberRequest) {
    GameEntity gameEntity =
        findGameByGameIdAndPlayerId(sendNumberRequest.getGameId(), sendNumberRequest.getPlayerId());

    checkGameStatus(gameEntity);

    boolean isFirstPlayer = isFirstPlayer(gameEntity, sendNumberRequest.getPlayerId());

    checkPlayerTurn(gameEntity, isFirstPlayer);

    switch (gameEntity.getMoveType()) {
      case FIRST_MOVE:
        {
          gameEntity.setCurrentNumber(sendNumberRequest.getNumber());
          gameEntity.setLastMove(sendNumberRequest.getNumber());
          gameEntity.setPlayerOneTurn(Boolean.FALSE);
          gameEntity.setMoveType(REGULAR_MOVE);
          break;
        }
      case REGULAR_MOVE:
        {
          validateInput(sendNumberRequest.getNumber());
          Integer currentNumber = gameEntity.getCurrentNumber();
          currentNumber = (currentNumber + sendNumberRequest.getNumber()) / 3;
          gameEntity.setCurrentNumber(currentNumber);
          gameEntity.setLastMove(sendNumberRequest.getNumber());
          gameEntity.setPlayerOneTurn(!isFirstPlayer);

          if (currentNumber == 1) {
            gameEntity.setGameStatus(GameStatus.FINISHED);
          }
        }
    }
    return GameDto.from(gameEntity, isFirstPlayer, false);
  }

  @Override
  public GameDto getGameStatus(String gameId, String playerId) {
    GameEntity gameEntity = findGameByGameIdAndPlayerId(gameId, playerId);
    boolean isFirstPlayer = isFirstPlayer(gameEntity, playerId);

    return GameDto.from(gameEntity, isFirstPlayer, isFirstPlayer == gameEntity.getPlayerOneTurn());
  }

  private void checkPlayerTurn(GameEntity gameEntity, boolean isFirstPlayer) {
    if (isFirstPlayer != gameEntity.getPlayerOneTurn()) {
      throw new GameException("Please wait for other player's turn", HttpStatus.BAD_REQUEST);
    }
  }

  private boolean isFirstPlayer(GameEntity gameEntity, String playerId) {
    return gameEntity.getPlayerOneId().equals(playerId);
  }

  private GameEntity findGameByGameIdAndPlayerId(String gameId, String playerId) {
    return gameRepository
        .findByGameIdAndPlayerId(gameId, playerId)
        .orElseThrow(
            () -> new GameException("Invalid Player Id or Game Id", HttpStatus.BAD_REQUEST));
  }

  private void checkGameStatus(GameEntity gameEntity) {
    if (GameStatus.FINISHED.equals(gameEntity.getGameStatus())) {
      throw new GameException("Game is already finished", HttpStatus.BAD_REQUEST);
    }
  }

  private String getNewPlayerId() {
    return Double.toHexString(Math.random());
  }

  private void validateInput(Integer number) {
    if (!REGULAR_MOVE.getInputOptions().contains(number)) {
      throw new GameException(
          "Number can be " + REGULAR_MOVE.getInputOptions(), HttpStatus.BAD_REQUEST);
    }
  }
}
