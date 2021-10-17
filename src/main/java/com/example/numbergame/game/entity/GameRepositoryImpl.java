package com.example.numbergame.game.entity;

import com.example.numbergame.game.constant.GameStatus;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class GameRepositoryImpl implements GameRepository {

  private static final Map<String, GameEntity> gameEntities = new HashMap<>();

  @Override
  public Optional<GameEntity> findByPlayerId(String playerId) {
    return gameEntities.values().stream().filter(g -> isGamePlayer(g, playerId)).findFirst();
  }

  @Override
  public Optional<GameEntity> findByGameIdAndPlayerId(String gameId, String playerId) {
    return gameEntities.values().stream()
        .filter(g -> g.getGameId().equals(gameId) && isGamePlayer(g, playerId))
        .findFirst();
  }

  @Override
  public Optional<GameEntity> findInProgressGameHavingPlayerTwoIdNull() {
    return gameEntities.values().stream()
        .filter(g -> GameStatus.IN_PROGRESS.equals(g.getGameStatus()) && g.getPlayerTwoId() == null)
        .findFirst();
  }

  @Override
  public GameEntity save(GameEntity gameEntity) {
    gameEntities.put(gameEntity.getGameId(), gameEntity);
    return gameEntity;
  }

  private boolean isGamePlayer(GameEntity gameEntity, String playerId) {
    return gameEntity.getPlayerOneId().equals(playerId)
        || (gameEntity.getPlayerTwoId() != null && gameEntity.getPlayerTwoId().equals(playerId));
  }
}
