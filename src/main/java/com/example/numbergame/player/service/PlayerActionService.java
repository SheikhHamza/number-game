package com.example.numbergame.player.service;

import com.example.numbergame.common.dto.GameDto;
import com.example.numbergame.common.dto.PlayerInputNumberRequest;

public interface PlayerActionService {
  GameDto startGame();

  GameDto sendNumber(PlayerInputNumberRequest playerInputNumberRequest);

  GameDto checkGameStatus(String playerId);
}
