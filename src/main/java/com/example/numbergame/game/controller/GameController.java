package com.example.numbergame.game.controller;

import com.example.numbergame.common.dto.GameDto;
import com.example.numbergame.game.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("game")
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping("status")
  ResponseEntity<GameDto> getGameStatus(
      @RequestParam(name = "gameId") Integer gameId,
      @RequestParam(name = "player") Integer playerId) {
    return ResponseEntity.of(gameService.getGameStatus(gameId, playerId));
  }
}
