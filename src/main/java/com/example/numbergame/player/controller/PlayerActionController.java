package com.example.numbergame.player.controller;

import com.example.numbergame.common.dto.GameDto;
import com.example.numbergame.common.dto.PlayerInputNumberRequest;
import com.example.numbergame.common.error.PlayerException;
import com.example.numbergame.player.service.PlayerActionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/player/actions")
public class PlayerActionController {
  private final PlayerActionService playerActionService;

  public PlayerActionController(PlayerActionService playerActionService) {
    this.playerActionService = playerActionService;
  }

  @GetMapping("game-status")
  GameDto getGameStatus(@RequestParam(name = "playerId") String playerId) {
    return playerActionService.checkGameStatus(playerId);
  }

  @PostMapping("start-game")
  GameDto startGame() {
    return playerActionService.startGame();
  }

  @PutMapping("send-number")
  GameDto sendNumber(@RequestBody PlayerInputNumberRequest playerInputNumberRequest) {
    if (playerInputNumberRequest.getPlayerId() == null) {
      throw new PlayerException("Please provide playerId", HttpStatus.BAD_REQUEST);
    }
    return playerActionService.sendNumber(playerInputNumberRequest);
  }
}
