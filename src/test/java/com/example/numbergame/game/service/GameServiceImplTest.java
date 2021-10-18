package com.example.numbergame.game.service;

import com.example.numbergame.common.dto.GameDto;
import com.example.numbergame.common.dto.SendNumberRequest;
import com.example.numbergame.common.error.GameException;
import com.example.numbergame.game.constant.MoveType;
import com.example.numbergame.game.entity.GameEntity;
import com.example.numbergame.game.entity.GameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplTest {

  @Mock private GameRepository gameRepository;

  @InjectMocks private GameServiceImpl gameService;

  @Test
  public void startGame_noExistingGamesExist() {
    when(gameRepository.findInProgressGameHavingPlayerTwoIdNull()).thenReturn(Optional.empty());
    when(gameRepository.save(any(GameEntity.class))).thenAnswer(i -> i.getArguments()[0]);
    GameDto gameDto = gameService.startGame();

    ArgumentCaptor<GameEntity> argument = ArgumentCaptor.forClass(GameEntity.class);
    verify(gameRepository).save(argument.capture());
    assertNotNull(gameDto);
    assertNotNull(argument.getValue().getPlayerOneId());
    assertNull(argument.getValue().getPlayerTwoId());
  }

  @Test
  public void startGame_ThereIsAnExistingGame() {
    GameEntity gameEntity = new GameEntity("playerOneId");
    when(gameRepository.findInProgressGameHavingPlayerTwoIdNull())
        .thenReturn(Optional.of(gameEntity));

    GameDto gameDto = gameService.startGame();

    assertNotNull(gameDto);
    assertNotNull(gameEntity.getPlayerOneId());
    assertNotNull(gameEntity.getPlayerTwoId());
  }

  @Test(expected = GameException.class)
  public void sendNumber_whenGameNotFound() {
    when(gameRepository.findByGameIdAndPlayerId(any(String.class), any(String.class)))
        .thenReturn(Optional.empty());

    gameService.sendNumber(new SendNumberRequest("playerId", "gameId", 1));
  }

  @Test(expected = GameException.class)
  public void sendNumber_whenItsFirstMove() {
    GameEntity gameEntity = new GameEntity("playerOneId");
    when(gameRepository.findByGameIdAndPlayerId(any(String.class), any(String.class)))
        .thenReturn(Optional.of(gameEntity));

    Integer move = 56;
    GameDto gameDto = gameService.sendNumber(new SendNumberRequest("playerId", "gameId", move));

    assertNotNull(gameDto);
    assertEquals(move, gameDto.getCurrentNumber());
    assertEquals(move, gameDto.getLastMove());
    assertFalse(gameDto.isPlayerTurn());
    assertFalse(gameDto.getInputOptions().isEmpty());
  }

  @Test(expected = GameException.class)
  public void sendNumber_whenItsRegularMoveAndInvalidNumberIsProvided() {
    GameEntity gameEntity = new GameEntity("playerOneId");
    gameEntity.setMoveType(MoveType.REGULAR_MOVE);
    when(gameRepository.findByGameIdAndPlayerId(any(String.class), any(String.class)))
        .thenReturn(Optional.of(gameEntity));

    Integer move = 56;
    GameDto gameDto = gameService.sendNumber(new SendNumberRequest("playerId", "gameId", move));
  }

  @Test
  public void getGameStatus() {
    String playerId = "playerOneId";
    GameEntity gameEntity = new GameEntity(playerId);

    when(gameRepository.findByGameIdAndPlayerId(any(String.class), any(String.class)))
        .thenReturn(Optional.of(gameEntity));

    GameDto gameDto = gameService.getGameStatus("gameId", playerId);

    assertNotNull(gameDto);
    assertTrue(gameDto.isPlayerTurn());
    assertTrue(gameDto.getInputOptions().isEmpty());
    assertNull(gameDto.getCurrentNumber());
  }
}
