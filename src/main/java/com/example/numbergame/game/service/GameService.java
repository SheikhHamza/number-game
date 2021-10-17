package com.example.numbergame.game.service;

import com.example.numbergame.common.dto.GameDto;
import com.example.numbergame.common.dto.SendNumberRequest;

public interface GameService {

  GameDto startGame();

  GameDto sendNumber(SendNumberRequest sendNumberRequest);

  GameDto getGameStatus(String gameId, String playerId);
}
