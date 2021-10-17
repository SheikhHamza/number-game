package com.example.numbergame.game.service;

import com.example.numbergame.common.dto.GameDto;
import com.example.numbergame.common.dto.SendNumberRequest;
import com.example.numbergame.game.constant.GameStatus;
import com.example.numbergame.game.entity.GameEntity;
import com.example.numbergame.game.entity.GameRepository;
import com.example.numbergame.game.error.GameException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

  private final GameRepository gameRepository;
  private final List<Integer> REGULAR_MOVE_OPTIONS = Arrays.asList(-1, 0, 1);

  public GameServiceImpl(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  @Override
  public GameDto startGame() {
    Optional<GameEntity> existingGame = gameRepository.findInProgressGameHavingPlayerTwoIdNull();
    if (existingGame.isPresent()) {
      String playerTwoId = getNewPlayerId();
      existingGame.get().setPlayerTwoId(playerTwoId);
      return GameDto.from(existingGame.get(), false, true);
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

    switch (gameEntity.getMoveType()) {
      case FIRST_MOVE:
        {
          gameEntity.setCurrentNumber(sendNumberRequest.getNumber());
          gameEntity.setLastMove(sendNumberRequest.getNumber());
          gameEntity.setPlayerOneTurn(Boolean.FALSE);
          break;
        }
      case REGULAR_MOVE:
        {
          if (REGULAR_MOVE_OPTIONS.contains(sendNumberRequest.getNumber())) {
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

    return null;
  }

  @Override
  public GameDto getGameStatus(String gameId, String playerId) {
    GameEntity gameEntity = findGameByGameIdAndPlayerId(gameId, playerId);
    boolean isFirstPlayer = isFirstPlayer(gameEntity, playerId);

    return GameDto.from(gameEntity, isFirstPlayer, isFirstPlayer == gameEntity.getPlayerOneTurn());
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

  private boolean isFinished(Integer number) {
    return number == 1;
  }
}
