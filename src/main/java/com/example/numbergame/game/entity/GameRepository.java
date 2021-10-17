package com.example.numbergame.game.entity;

import java.util.Optional;

public interface GameRepository {
  Optional<GameEntity> findByPlayerId(String playerId);

  Optional<GameEntity> findByGameIdAndPlayerId(String gameId, String playerId);

  Optional<GameEntity> findInProgressGameHavingPlayerTwoIdNull();

  GameEntity save(GameEntity gameEntity);
}
