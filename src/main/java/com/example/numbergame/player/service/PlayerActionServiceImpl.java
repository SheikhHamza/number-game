package com.example.numbergame.player.service;

import com.example.numbergame.common.dto.GameDto;
import com.example.numbergame.common.error.PlayerException;
import com.example.numbergame.common.dto.PlayerInputNumberRequest;
import com.example.numbergame.player.gateway.GameServiceGateway;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class PlayerActionServiceImpl implements PlayerActionService {
  public static Map<String, String> playerGames = new HashMap<>();
  private final GameServiceGateway serviceGateway;
  public Random random;

  public PlayerActionServiceImpl(GameServiceGateway serviceGateway) {
    this.serviceGateway = serviceGateway;
    this.random = new Random();
  }

  @Override
  public GameDto startGame() {
    GameDto gameDto = serviceGateway.startGame();
    playerGames.put(gameDto.getPlayerId(), gameDto.getGameId());
    return gameDto;
  }

  @Override
  public GameDto sendNumber(PlayerInputNumberRequest playerInputNumberRequest) {
    GameDto gameDto = checkGameStatus(playerInputNumberRequest.getPlayerId());
    Integer number =
        getPlayerInput(
            playerInputNumberRequest.getNumber(),
            playerInputNumberRequest.getPlayerId(),
            gameDto.getInputOptions());
    return serviceGateway.sendNumber(
        playerInputNumberRequest.getPlayerId(), gameDto.getGameId(), number);
  }

  @Override
  public GameDto checkGameStatus(String playerId) {
    if (playerGames.containsKey(playerId)) {
      return serviceGateway.getGameStatus(playerId, playerGames.get(playerId));
    } else {
      throw new PlayerException(
          "Player not found. Please provide valid playerId", HttpStatus.BAD_REQUEST);
    }
  }

  private Integer getPlayerInput(Integer number, String playerId, List<Integer> inputOptions) {
    if (number != null) {
      return number;
    } else {
      return inputOptions.isEmpty()
          ? random.nextInt(1000)+1
          : inputOptions.get(
              random.nextInt(inputOptions.size())); // max number can 1000 just for random reason.
    }
  }
}
